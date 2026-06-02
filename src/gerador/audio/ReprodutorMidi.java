package gerador.audio;

import gerador.dominio.*;

import javax.sound.midi.*;
import java.util.List;

public class ReprodutorMidi implements SaidaMusical {

    private Sequencer sequencer;
    private Sequence sequenciaAtual;
    private long posicaoPausada;
    private boolean pausado;

    public ReprodutorMidi() {
        this.posicaoPausada = 0;
        this.pausado = false;
    }

    @Override
    public void reproduzir(Partitura partitura) throws Exception {
        parar();
        sequenciaAtual = construirSequenciaMidi(partitura);

        sequencer = MidiSystem.getSequencer();
        sequencer.open();
        sequencer.setSequence(sequenciaAtual);
        sequencer.setTempoInBPM(partitura.getConfiguracao().getBpm());
        posicaoPausada = 0;
        pausado = false;
        sequencer.start();
    }

    @Override
    public void pausar() {
        if (sequencer != null && sequencer.isRunning()) {
            posicaoPausada = sequencer.getTickPosition();
            sequencer.stop();
            pausado = true;
        }
    }

    @Override
    public void retomar() {
        if (sequencer != null && pausado) {
            sequencer.setTickPosition(posicaoPausada);
            sequencer.start();
            pausado = false;
        }
    }

    @Override
    public void parar() {
        if (sequencer != null) {
            if (sequencer.isRunning()) {
                sequencer.stop();
            }
            sequencer.close();
            sequencer = null;
        }
        posicaoPausada = 0;
        pausado = false;
    }

    @Override
    public boolean estaReproduzindo() {
        return sequencer != null && sequencer.isRunning();
    }

    @Override
    public double getProgresso() {
        if (sequencer == null || sequenciaAtual == null) return 0.0;
        long total = sequencer.getTickLength();
        if (total == 0) return 0.0;
        long atual = pausado ? posicaoPausada : sequencer.getTickPosition();
        return (double) atual / total;
    }

    @Override
    public void liberarRecursos() {
        parar();
    }

    public Sequence getSequenciaAtual() {
        return sequenciaAtual;
    }

    /**
     * Cada voz vira um canal MIDI separado (máximo 15 vozes).
     * Canal 9 é pulado pois é reservado para percussão no General MIDI.
     */
    public Sequence construirSequenciaMidi(Partitura partitura) throws InvalidMidiDataException {
        Sequence sequence = new Sequence(Sequence.PPQ, Nota.DURACAO_PADRAO);
        List<Voz> vozes = partitura.getVozes();

        for (int i = 0; i < vozes.size() && i < 15; i++) {
            Voz voz = vozes.get(i);
            int canal = (i >= 9) ? i + 1 : i; // Pula canal 9 (percussão)
            Track track = sequence.createTrack();

            adicionarEventosDaVoz(track, voz, canal, partitura.getConfiguracao());
        }

        return sequence;
    }

    private void adicionarEventosDaVoz(Track track, Voz voz, int canal,
                                        ConfiguracaoMusical config)
            throws InvalidMidiDataException {

        long tickAtual = 0;
        int instrumentoAtual = -1;

        for (EventoMusical evento : voz.getEventos()) {
            if (evento instanceof Nota) {
                Nota nota = (Nota) evento;

                if (nota.getInstrumento() != instrumentoAtual) {
                    instrumentoAtual = nota.getInstrumento();
                    ShortMessage programChange = new ShortMessage();
                    programChange.setMessage(ShortMessage.PROGRAM_CHANGE, canal,
                            instrumentoAtual, 0);
                    track.add(new MidiEvent(programChange, tickAtual));
                }

                ShortMessage noteOn = new ShortMessage();
                noteOn.setMessage(ShortMessage.NOTE_ON, canal,
                        nota.getPitchMidi(), nota.getVelocity());
                track.add(new MidiEvent(noteOn, tickAtual));

                ShortMessage noteOff = new ShortMessage();
                noteOff.setMessage(ShortMessage.NOTE_OFF, canal,
                        nota.getPitchMidi(), 0);
                track.add(new MidiEvent(noteOff, tickAtual + nota.getDuracaoEmTicks()));
            }

            tickAtual += evento.getDuracaoEmTicks();
        }
    }
}
