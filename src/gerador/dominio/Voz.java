package gerador.dominio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Voz {

    private static final int NUMERO_DE_PADROES = 4;
    private static final int[] OFFSETS_OITAVA_FUGA = {0, -1, -2, -3};
    private static final double[] PROPORCOES_VOLUME_FUGA = {1.0, 0.8, 0.6, 0.4};

    private final int numero;
    private final List<EventoMusical> eventos;
    private final int atrasoEmBeats;

    private int instrumentoInicial;
    private int oitavaInicial;
    private int volumeInicial;

    public Voz(ParametrosVoz parametros) {
        this.numero = parametros.getNumero();
        this.atrasoEmBeats = Math.max(0, parametros.getAtrasoEmBeats());
        this.eventos = new ArrayList<>();
        atribuirPadroesDeVoz(parametros);
    }

    /*
        Padrões de fuga: cada voz subsequente desce 1 oitava e reduz volume proporcionalmente.
        Voz 0 usa o instrumento configurado pelo usuário; as demais usam instrumentos fixos da fuga.
    */
    private void atribuirPadroesDeVoz(ParametrosVoz parametros) {
        int[] instrumentos = {
            parametros.getInstrumentoBase(), 
            InstrumentoMidi.CHURCH_ORGAN.getCodigo(), 
            InstrumentoMidi.PIANO.getCodigo(), 
            InstrumentoMidi.FAGOTE.getCodigo()
        };

        int indice = parametros.getNumero() % NUMERO_DE_PADROES;
        this.oitavaInicial = Math.max(MidiConstantes.VALOR_MINIMO, Math.min(9, parametros.getOitavaBase() + OFFSETS_OITAVA_FUGA[indice]));
        this.volumeInicial = (int) Math.round(parametros.getVolumeMaximo() * PROPORCOES_VOLUME_FUGA[indice]);
        this.volumeInicial = Math.max(MidiConstantes.VALOR_MINIMO, Math.min(MidiConstantes.VALOR_MAXIMO, this.volumeInicial));
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
