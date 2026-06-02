package gerador.mapeamento;

import gerador.interpretacao.ContextoDeVoz;

public class RegraAcelerarBpm implements RegraDeInterpretacao {

    private static final int INCREMENTO = 10;

    @Override
    public boolean aplicavel(char caractere, ContextoDeVoz contexto) {
        return caractere == '>';
    }

    @Override
    public void aplicar(char caractere, ContextoDeVoz contexto) {
        contexto.aumentarBpm(INCREMENTO);
    }
}
