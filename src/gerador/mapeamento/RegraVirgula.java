package gerador.mapeamento;

import gerador.interpretacao.ContextoDeVoz;

public class RegraVirgula implements RegraDeInterpretacao {

    private static final int CHURCH_ORGAN = 20;

    @Override
    public boolean aplicavel(char caractere, ContextoDeVoz contexto) {
        return caractere == ',';
    }

    @Override
    public void aplicar(char caractere, ContextoDeVoz contexto) {
        contexto.trocarInstrumento(CHURCH_ORGAN);
    }
}
