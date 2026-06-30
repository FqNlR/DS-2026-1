package gerador.dominio;

public class ParametrosConfiguracao {

    private final int bpm;
    private final int volume;
    private final int oitava;
    private final int instrumento;

    public ParametrosConfiguracao(int bpm, int volume, int oitava, int instrumento) {
        this.bpm = bpm;
        this.volume = volume;
        this.oitava = oitava;
        this.instrumento = instrumento;
    }

    public int getBpm() {
        return bpm;
    }

    public int getVolume() {
        return volume;
    }

    public int getOitava() {
        return oitava;
    }

    public int getInstrumento() {
        return instrumento;
    }
}
