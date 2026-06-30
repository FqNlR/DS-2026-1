package gerador.interpretacao;

import gerador.dominio.EventoMusical;
import gerador.dominio.MidiConstantes;
import gerador.dominio.MudancaDeBpm;
import gerador.dominio.Nota;
import gerador.dominio.Pausa;
import gerador.dominio.Voz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    private static final int NOTAS_POR_OITAVA = 12;
    private static final int OITAVA_MINIMA = 0;
    private static final int OITAVA_MAXIMA = 9;
    private static final int BPM_MINIMO = 20;
    private static final int BPM_MAXIMO = 300;

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

    private final List<String> avisos = new ArrayList<>();

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

        int pitchMidi = (oitavaAtual + 1) * NOTAS_POR_OITAVA + valorBase;
        pitchMidi = Math.max(MidiConstantes.VALOR_MINIMO, Math.min(MidiConstantes.VALOR_MAXIMO, pitchMidi));

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
        volumeAtual = Math.min(novoVolume, MidiConstantes.VALOR_MAXIMO);
    }

    public void aumentarOitava() {
        if (oitavaAtual < OITAVA_MAXIMA) {
            oitavaAtual++;
        } else {
            oitavaAtual = oitavaPadrao;
        }
    }

    public void diminuirOitava() {
        if (oitavaAtual > OITAVA_MINIMA) {
            oitavaAtual--;
        } else {
            oitavaAtual = oitavaPadrao;
        }
    }

    public void trocarInstrumento(int novoInstrumento) {
        instrumentoAtual = Math.max(MidiConstantes.VALOR_MINIMO, Math.min(MidiConstantes.VALOR_MAXIMO, novoInstrumento));
    }

    public void aumentarBpm(int incremento) {
        bpmAtual = Math.min(BPM_MAXIMO, bpmAtual + incremento);
        voz.adicionarEvento(new MudancaDeBpm(bpmAtual));
    }

    public void diminuirBpm(int decremento) {
        bpmAtual = Math.max(BPM_MINIMO, bpmAtual - decremento);
        voz.adicionarEvento(new MudancaDeBpm(bpmAtual));
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

    public void registrarAviso(String aviso) {
        avisos.add(aviso);
    }

    public List<String> getAvisos() {
        return avisos;
    }
}
