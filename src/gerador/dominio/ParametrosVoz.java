package gerador.dominio;

public class ParametrosVoz {

    private final int numero;
    private final int atrasoEmBeats;
    private final int volumeMaximo;
    private final int instrumentoBase;
    private final int oitavaBase;

    public ParametrosVoz(int numero, int atrasoEmBeats, int volumeMaximo,
                         int instrumentoBase, int oitavaBase) {
        this.numero = numero;
        this.atrasoEmBeats = atrasoEmBeats;
        this.volumeMaximo = volumeMaximo;
        this.instrumentoBase = instrumentoBase;
        this.oitavaBase = oitavaBase;
    }

    public int getNumero() {
        return numero;
    }

    public int getAtrasoEmBeats() {
        return atrasoEmBeats;
    }

    public int getVolumeMaximo() {
        return volumeMaximo;
    }

    public int getInstrumentoBase() {
        return instrumentoBase;
    }

    public int getOitavaBase() {
        return oitavaBase;
    }
}
