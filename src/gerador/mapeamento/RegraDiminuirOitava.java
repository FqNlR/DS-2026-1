package gerador.mapeamento;

import gerador.interpretacao.ContextoDeVoz;

/**
 * 'V' diminui oitava. Se já na mínima, volta à oitava padrão da voz.
 */
public class RegraDiminuirOitava implements RegraDeInterpretacao {

    @Override
    public boolean aplicavel(char caractere, ContextoDeVoz contexto) {
        return caractere == 'V';
    }

    @Override
    public void aplicar(char caractere, ContextoDeVoz contexto) {
        contexto.diminuirOitava();
    }
}
