package gerador.dominio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Voz {

    private final int numero;
    private final List<EventoMusical> eventos;
    private final int atrasoEmBeats;

    private int instrumentoInicial;
    private int oitavaInicial;
    private int volumeInicial;

    public Voz(int numero, int atrasoEmBeats, int volumeMaximo) {
        this.numero = numero;
        this.atrasoEmBeats = Math.max(0, atrasoEmBeats);
        this.eventos = new ArrayList<>();
        atribuirPadroesDeVoz(numero, volumeMaximo);
    }

    /**
     * Padrões de fuga: cada voz subsequente desce 1 oitava e reduz volume proporcionalmente.
     * Voz 0 usa Cravo; as demais usam instrumentos fixos da fuga.
     */
    private void atribuirPadroesDeVoz(int numero, int volumeMaximo) {
        int[] oitavas = {6, 5, 4, 3};
        double[] proporcoesVolume = {1.0, 0.8, 0.6, 0.4};
        int[] instrumentos = {6, 20, 0, 70}; // Cravo, Church Organ, Piano, Fagote

        int indice = numero % 4;
        this.oitavaInicial = oitavas[indice];
        this.volumeInicial = (int) Math.round(volumeMaximo * proporcoesVolume[indice]);
        this.volumeInicial = Math.max(0, Math.min(127, this.volumeInicial));
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
