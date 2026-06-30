package gerador.dominio;

public class Nota extends EventoMusical {

    public static final int DURACAO_PADRAO = 480;

    private final int pitchMidi;
    private final int velocity;
    private final int instrumento;

    public Nota(int pitchMidi, int velocity, int instrumento) {
        super(DURACAO_PADRAO);
        this.pitchMidi = limitarValor(pitchMidi, MidiConstantes.VALOR_MINIMO, MidiConstantes.VALOR_MAXIMO);
        this.velocity = limitarValor(velocity, MidiConstantes.VALOR_MINIMO, MidiConstantes.VALOR_MAXIMO);
        this.instrumento = limitarValor(instrumento, MidiConstantes.VALOR_MINIMO, MidiConstantes.VALOR_MAXIMO);
    }

    public int getPitchMidi() {
        return pitchMidi;
    }

    public int getVelocity() {
        return velocity;
    }

    public int getInstrumento() {
        return instrumento;
    }

    @Override
    public boolean produzSom() {
        return true;
    }

    @Override
    public String toString() {
        return String.format("Nota[pitch=%d, vel=%d, inst=%d]", pitchMidi, velocity, instrumento);
    }

    private static int limitarValor(int valor, int min, int max) {
        return Math.max(min, Math.min(max, valor));
    }
}
