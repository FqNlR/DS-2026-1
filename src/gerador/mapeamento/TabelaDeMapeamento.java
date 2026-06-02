package gerador.mapeamento;

import java.util.ArrayList;
import java.util.List;
import gerador.interpretacao.ContextoDeVoz;

//Registra todas as regras de interpretação e as percorre em ordem de prioridade

public class TabelaDeMapeamento {

    private final List<RegraDeInterpretacao> regras;

    public TabelaDeMapeamento() {
        this.regras = new ArrayList<>();
        registrarRegrasPadrao();
    }

    private void registrarRegrasPadrao() {
        regras.add(new RegraNota());
        regras.add(new RegraPausaMinuscula());
        regras.add(new RegraEspaco());
        regras.add(new RegraExclamacao());
        regras.add(new RegraInterrogacao());
        regras.add(new RegraDiminuirOitava());
        regras.add(new RegraPontoVirgula());
        regras.add(new RegraVirgula());
        regras.add(new RegraDigito());
        regras.add(new RegraAcelerarBpm());
        regras.add(new RegraDesacelerarBpm());
        regras.add(new RegraRepetirOuPausa()); // Fallback — deve ser a última
    }

    public void adicionarRegra(RegraDeInterpretacao regra) {
        int posicaoAntesFallback = Math.max(0, regras.size() - 1);
        regras.add(posicaoAntesFallback, regra);
    }

    public void interpretar(char caractere, ContextoDeVoz contexto) {
        for (RegraDeInterpretacao regra : regras) {
            if (regra.aplicavel(caractere, contexto)) {
                regra.aplicar(caractere, contexto);
                return;
            }
        }
    }
}
