package gerador.mapeamento;

import gerador.interpretacao.ContextoDeVoz;

// '?' e '.' aumentam oitava. Se já na máxima, volta à oitava padrão da voz.
public class RegraInterrogacao implements RegraDeInterpretacao {

    @Override
    public boolean aplicavel(char caractere, ContextoDeVoz contexto) {
        return caractere == '?' || caractere == '.';
    }

    @Override
    public void aplicar(char caractere, ContextoDeVoz contexto) {
        contexto.aumentarOitava();
    }
}
