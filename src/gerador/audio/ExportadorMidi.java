package gerador.audio;

import gerador.dominio.Partitura;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import java.io.File;

public class ExportadorMidi {

    private final ReprodutorMidi reprodutor;

    public ExportadorMidi(ReprodutorMidi reprodutor) {
        this.reprodutor = reprodutor;
    }

    public void exportar(Partitura partitura, String caminhoArquivo) throws Exception {
        Sequence sequence = reprodutor.construirSequenciaMidi(partitura);
        File arquivo = new File(caminhoArquivo);

        // Tipo 1 = múltiplas tracks (uma por voz), preferido para polifonia
        int[] tiposSuportados = MidiSystem.getMidiFileTypes(sequence);
        int tipo = 0;
        for (int t : tiposSuportados) {
            if (t == 1) { tipo = 1; break; }
        }

        MidiSystem.write(sequence, tipo, arquivo);
    }
}
