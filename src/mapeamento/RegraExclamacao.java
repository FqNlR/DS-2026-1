package gerador.mapeamento;

import gerador.interpretacao.ContextoDeVoz;

public class RegraExclamacao implements RegraDeInterpretacao {

    private static final int HARMONICA = 22;

    @Override
    public boolean aplicavel(char caractere, ContextoDeVoz contexto) {
        return caractere == '!';
    }

    @Override
    public void aplicar(char caractere, ContextoDeVoz contexto) {
        contexto.trocarInstrumento(HARMONICA);
    }
}
