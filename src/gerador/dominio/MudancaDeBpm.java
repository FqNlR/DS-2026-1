package gerador.dominio;

/*
    Evento de mudança de BPM. Duração 0 pois não avança o tempo musical,
    apenas altera o andamento a partir deste ponto.
*/
public class MudancaDeBpm extends EventoMusical {

    private final int novoBpm;

    public MudancaDeBpm(int novoBpm) {
        super(0);
        this.novoBpm = novoBpm;
    }

    public int getNovoBpm() {
        return novoBpm;
    }

    @Override
    public boolean produzSom() {
        return false;
    }

    @Override
    public String toString() {
        return String.format("MudancaDeBpm[bpm=%d]", novoBpm);
    }
}
