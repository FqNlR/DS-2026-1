package gerador.dominio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Voz {

    private static final int VOLUME_MAXIMO_MIDI = 127;
    private static final int VOLUME_MINIMO_MIDI = 0;
    private static final int OITAVA_MINIMA = 0;
    private static final int OITAVA_MAXIMA = 9;

    private static final int NUMERO_DE_PADROES = 4;
    private static final int[] OFFSETS_OITAVA_FUGA = {0, -1, -2, -3};
    private static final double[] PROPORCOES_VOLUME_FUGA = {1.0, 0.8, 0.6, 0.4};

    private static final int INSTRUMENTO_CHURCH_ORGAN = 20;
    private static final int INSTRUMENTO_PIANO_ACUSTICO = 0;
    private static final int INSTRUMENTO_FAGOTE = 70;

    private final int numero;
    private final List<EventoMusical> eventos;
    private final int atrasoEmBeats;

    private int instrumentoInicial;
    private int oitavaInicial;
    private int volumeInicial;

    public Voz(int numero, int atrasoEmBeats, int volumeMaximo, int instrumentoBase, int oitavaBase) {
        this.numero = numero;
        this.atrasoEmBeats = Math.max(0, atrasoEmBeats);
        this.eventos = new ArrayList<>();
        atribuirPadroesDeVoz(numero, volumeMaximo, instrumentoBase, oitavaBase);
    }

    /*
        Padrões de fuga: cada voz subsequente desce 1 oitava e reduz volume proporcionalmente.
        Voz 0 usa o instrumento configurado pelo usuário; as demais usam instrumentos fixos da fuga.
    */
    private void atribuirPadroesDeVoz(int numero, int volumeMaximo, int instrumentoBase, int oitavaBase) {
        int[] instrumentos = {
            instrumentoBase, 
            INSTRUMENTO_CHURCH_ORGAN, 
            INSTRUMENTO_PIANO_ACUSTICO, 
            INSTRUMENTO_FAGOTE
        };

        int indice = numero % NUMERO_DE_PADROES;
        this.oitavaInicial = Math.max(OITAVA_MINIMA, Math.min(OITAVA_MAXIMA, oitavaBase + OFFSETS_OITAVA_FUGA[indice]));
        this.volumeInicial = (int) Math.round(volumeMaximo * PROPORCOES_VOLUME_FUGA[indice]);
        this.volumeInicial = Math.max(VOLUME_MINIMO_MIDI, Math.min(VOLUME_MAXIMO_MIDI, this.volumeInicial));
        this.instrumentoInicial = instrumentos[indice];
    }

    public void adicionarEvento(EventoMusical evento) {
        eventos.add(evento);
    }

    public int getNumero() {
        return numero;
    }

    public int getAtrasoEmBeats() {
        return atrasoEmBeats;
    }

    public int getInstrumentoInicial() {
        return instrumentoInicial;
    }

    public int getOitavaInicial() {
        return oitavaInicial;
    }

    public int getVolumeInicial() {
        return volumeInicial;
    }

    public List<EventoMusical> getEventos() {
        return Collections.unmodifiableList(eventos);
    }

    public int getTotalEventos() {
        return eventos.size();
    }

    @Override
    public String toString() {
        return String.format("Voz[num=%d, atraso=%d, eventos=%d, inst=%d, oitava=%d, vol=%d]",
                numero, atrasoEmBeats, eventos.size(),
                instrumentoInicial, oitavaInicial, volumeInicial);
    }
}
