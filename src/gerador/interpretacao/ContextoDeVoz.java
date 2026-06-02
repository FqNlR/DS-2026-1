package gerador.interpretacao;

import gerador.dominio.EventoMusical;
import gerador.dominio.Nota;
import gerador.dominio.Pausa;
import gerador.dominio.Voz;

import java.util.HashMap;
import java.util.Map;

public class ContextoDeVoz {

    // Valores base das notas na oitava 0 (MIDI: C4 = 60 = oitava 4 * 12 + 0)
    private static final Map<String, Integer> NOTAS_MIDI = new HashMap<>();

    static {
        NOTAS_MIDI.put("C", 0);   // Dó
        NOTAS_MIDI.put("D", 2);   // Ré
        NOTAS_MIDI.put("E", 4);   // Mi
        NOTAS_MIDI.put("Mb", 3);  // Mi bemol (Eb)
        NOTAS_MIDI.put("F", 5);   // Fá
        NOTAS_MIDI.put("G", 7);   // Sol
        NOTAS_MIDI.put("A", 9);   // Lá
        NOTAS_MIDI.put("B", 11);  // Si
        NOTAS_MIDI.put("H", 10);  // Si bemol (Bb)
    }

    private final Voz voz;
    private int instrumentoAtual;
    private int oitavaAtual;
    private int volumeAtual;
    private int bpmAtual;
    private int oitavaPadrao;
    private boolean ultimoFoiNota;
    private int ultimoPitchMidi;

    private String textoCompleto;
    private int posicaoAtual;

    public ContextoDeVoz(Voz voz, int bpmInicial) {
        this.voz = voz;
        this.instrumentoAtual = voz.getInstrumentoInicial();
        this.oitavaAtual = voz.getOitavaInicial();
        this.volumeAtual = voz.getVolumeInicial();
        this.bpmAtual = bpmInicial;
        this.oitavaPadrao = voz.getOitavaInicial();
        this.ultimoFoiNota = false;
        this.ultimoPitchMidi = -1;
    }

    public void gerarNota(String nomeNota) {
        Integer valorBase = NOTAS_MIDI.get(nomeNota);
        if (valorBase == null) return;

        int pitchMidi = (oitavaAtual + 1) * 12 + valorBase;
        pitchMidi = Math.max(0, Math.min(127, pitchMidi));

        Nota nota = new Nota(pitchMidi, volumeAtual, instrumentoAtual);
        voz.adicionarEvento(nota);
        ultimoFoiNota = true;
        ultimoPitchMidi = pitchMidi;
    }

    public void repetirUltimaNota() {
        if (ultimoFoiNota && ultimoPitchMidi >= 0) {
            Nota nota = new Nota(ultimoPitchMidi, volumeAtual, instrumentoAtual);
            voz.adicionarEvento(nota);
        } else {
            gerarPausa();
        }
    }

    public void gerarPausa() {
        voz.adicionarEvento(new Pausa());
        ultimoFoiNota = false;
    }

    public void dobrarVolume() {
        int novoVolume = volumeAtual * 2;
        volumeAtual = Math.min(novoVolume, 127);
    }

    public void aumentarOitava() {
        if (oitavaAtual < 9) {
            oitavaAtual++;
        } else {
            oitavaAtual = oitavaPadrao;
        }
    }

    public void diminuirOitava() {
        if (oitavaAtual > 0) {
            oitavaAtual--;
        } else {
            oitavaAtual = oitavaPadrao;
        }
    }

    public void trocarInstrumento(int novoInstrumento) {
        instrumentoAtual = Math.max(0, Math.min(127, novoInstrumento));
    }

    public void aumentarBpm(int incremento) {
        bpmAtual = Math.min(300, bpmAtual + incremento);
    }

    public void diminuirBpm(int decremento) {
        bpmAtual = Math.max(20, bpmAtual - decremento);
    }

    public boolean ultimoFoiNota() {
        return ultimoFoiNota;
    }

    public int getInstrumentoAtual() {
        return instrumentoAtual;
    }

    public int getOitavaAtual() {
        return oitavaAtual;
    }

    public int getVolumeAtual() {
        return volumeAtual;
    }

    public int getBpmAtual() {
        return bpmAtual;
    }

    public Voz getVoz() {
        return voz;
    }

    // --- Lookahead para detectar "Mb" (Mi bemol = 2 caracteres) ---

    public void setTextoCompleto(String texto) {
        this.textoCompleto = texto;
    }

    public void setPosicaoAtual(int posicao) {
        this.posicaoAtual = posicao;
    }

    public int getPosicaoAtual() {
        return posicaoAtual;
    }

    public boolean proximoCaractereEh(char c) {
        if (textoCompleto == null) return false;
        int proximaPos = posicaoAtual + 1;
        return proximaPos < textoCompleto.length() && textoCompleto.charAt(proximaPos) == c;
    }

    public void avancarPosicao() {
        posicaoAtual++;
    }
}
