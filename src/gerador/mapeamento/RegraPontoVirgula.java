package gerador.mapeamento;

import gerador.interpretacao.ContextoDeVoz;

public class RegraPontoVirgula implements RegraDeInterpretacao {

    private static final int TUBULAR_BELLS = 15;

    @Override
    public boolean aplicavel(char caractere, ContextoDeVoz contexto) {
        return caractere == ';';
    }

    @Override
    public void aplicar(char caractere, ContextoDeVoz contexto) {
        contexto.trocarInstrumento(TUBULAR_BELLS);
    }
}
