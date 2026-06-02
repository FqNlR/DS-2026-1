package gerador.dominio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Partitura {

    private final List<Voz> vozes;
    private final ConfiguracaoMusical configuracao;

    public Partitura(ConfiguracaoMusical configuracao) {
        this.vozes = new ArrayList<>();
        this.configuracao = configuracao;
    }

    public void adicionarVoz(Voz voz) {
        vozes.add(voz);
    }

    public List<Voz> getVozes() {
        return Collections.unmodifiableList(vozes);
    }

    public int getTotalVozes() {
        return vozes.size();
    }

    public ConfiguracaoMusical getConfiguracao() {
        return configuracao;
    }

    public int getTotalEventos() {
        return vozes.stream().mapToInt(Voz::getTotalEventos).sum();
    }

    @Override
    public String toString() {
        return String.format("Partitura[vozes=%d, eventos=%d, bpm=%d]",
                vozes.size(), getTotalEventos(), configuracao.getBpm());
    }
}
