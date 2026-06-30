package gerador.dominio;

/**
 * Enum que mapeia instrumentos musicais aos seus respectivos
 * códigos General MIDI (program numbers) e nomes descritivos.
 */
public enum InstrumentoMidi {

    PIANO(0, "Piano"),
    CRAVO(6, "Cravo"),
    TUBULAR_BELLS(15, "Tubular Bells"),
    CHURCH_ORGAN(20, "Church Organ"),
    HARMONICA(22, "Harmonica"),
    FAGOTE(70, "Fagote");

    private final int codigo;
    private final String nome;

    InstrumentoMidi(int codigo, String nome) {
        this.codigo = codigo;
        this.nome = nome;
    }

    /**
     * Retorna o código (program number) General MIDI do instrumento.
     */
    public int getCodigo() {
        return codigo;
    }

    /**
     * Retorna o nome descritivo do instrumento.
     */
    public String getNome() {
        return nome;
    }
}
