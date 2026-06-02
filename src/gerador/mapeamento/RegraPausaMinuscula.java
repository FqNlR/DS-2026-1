package gerador.mapeamento;

import gerador.interpretacao.ContextoDeVoz;

public class RegraPausaMinuscula implements RegraDeInterpretacao {

    @Override
    public boolean aplicavel(char caractere, ContextoDeVoz contexto) {
        return caractere >= 'a' && caractere <= 'h';
    }

    @Override
    public void aplicar(char caractere, ContextoDeVoz contexto) {
        contexto.gerarPausa();
    }
}
