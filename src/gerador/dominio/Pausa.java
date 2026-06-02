package gerador.dominio;

public class Pausa extends EventoMusical {

    public static final int DURACAO_PADRAO = 480;

    public Pausa() {
        super(DURACAO_PADRAO);
    }

    public Pausa(int duracaoEmTicks) {
        super(duracaoEmTicks);
    }

    @Override
    public boolean produzSom() {
        return false;
    }

    @Override
    public String toString() {
        return String.format("Pausa[ticks=%d]", getDuracaoEmTicks());
    }
}
