package gerador.dominio;

public abstract class EventoMusical {

    private final int duracaoEmTicks;

    protected EventoMusical(int duracaoEmTicks) {
        this.duracaoEmTicks = duracaoEmTicks;
    }

    public int getDuracaoEmTicks() {
        return duracaoEmTicks;
    }

    public abstract boolean produzSom();
}
