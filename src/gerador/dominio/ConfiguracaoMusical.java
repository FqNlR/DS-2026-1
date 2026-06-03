package gerador.dominio;

public class ConfiguracaoMusical {

    public static final int BPM_PADRAO = 120;
    public static final int VOLUME_PADRAO = 100;
    public static final int OITAVA_PADRAO = 6;
    public static final int INSTRUMENTO_PADRAO = 0;
    public static final int VOLUME_MAXIMO = 127;
    public static final int OITAVA_MAXIMA = 9;
    public static final int OITAVA_MINIMA = 0;
    public static final int BPM_MINIMO = 20;
    public static final int BPM_MAXIMO = 300;
    public static final int VOLUME_MINIMO = 0;
    public static final int INSTRUMENTO_MINIMO = 0;
    public static final int INSTRUMENTO_MAXIMO = 127;

    private int bpm;
    private int volume;
    private int oitava;
    private int instrumento;

    public ConfiguracaoMusical() {
        this.bpm = BPM_PADRAO;
        this.volume = VOLUME_PADRAO;
        this.oitava = OITAVA_PADRAO;
        this.instrumento = INSTRUMENTO_PADRAO;
    }

    public ConfiguracaoMusical(int bpm, int volume, int oitava, int instrumento) {
        this.bpm = limitarBpm(bpm);
        this.volume = limitarVolume(volume);
        this.oitava = limitarOitava(oitava);
        this.instrumento = limitarInstrumento(instrumento);
    }

    public int getBpm() { return bpm; }
    public int getVolume() { return volume; }
    public int getOitava() { return oitava; }
    public int getInstrumento() { return instrumento; }

    public void setBpm(int bpm) { this.bpm = limitarBpm(bpm); }
    public void setVolume(int volume) { this.volume = limitarVolume(volume); }
    public void setOitava(int oitava) { this.oitava = limitarOitava(oitava); }
    public void setInstrumento(int instrumento) { this.instrumento = limitarInstrumento(instrumento); }

    private static int limitarBpm(int valor) {
        return Math.max(BPM_MINIMO, Math.min(BPM_MAXIMO, valor));
    }

    private static int limitarVolume(int valor) {
        return Math.max(VOLUME_MINIMO, Math.min(VOLUME_MAXIMO, valor));
    }

    private static int limitarOitava(int valor) {
        return Math.max(OITAVA_MINIMA, Math.min(OITAVA_MAXIMA, valor));
    }

    //127 é o valor máximo perimitido pelo general MIDI
    private static int limitarInstrumento(int valor) {
        return Math.max(INSTRUMENTO_MINIMO, Math.min(INSTRUMENTO_MAXIMO, valor));
    }

    public ConfiguracaoMusical copiar() {
        return new ConfiguracaoMusical(bpm, volume, oitava, instrumento);
    }

    @Override
    public String toString() {
        return String.format("Config[bpm=%d, vol=%d, oitava=%d, inst=%d]",
                bpm, volume, oitava, instrumento);
    }
}
