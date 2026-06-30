package gerador.dominio;

/**
 * Classe central de constantes do domínio MIDI.
 * Centraliza os valores-limite definidos pelo padrão General MIDI
 * para evitar duplicação de "magic numbers" pelo código.
 */
public final class MidiConstantes {

    /** Valor mínimo permitido pelo padrão MIDI (pitch, velocity, program). */
    public static final int VALOR_MINIMO = 0;

    /** Valor máximo permitido pelo padrão MIDI (pitch, velocity, program): 127. */
    public static final int VALOR_MAXIMO = 127;

    /** Canal reservado para percussão no General MIDI. */
    public static final int CANAL_PERCUSSAO = 9;

    private MidiConstantes() {
        // Impede instanciação — classe utilitária de constantes.
    }
}
