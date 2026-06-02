package gerador.mapeamento;

import gerador.interpretacao.ContextoDeVoz;

public class RegraNota implements RegraDeInterpretacao {

    private static final String NOTAS_VALIDAS = "ABCDEFGH";

    @Override
    public boolean aplicavel(char caractere, ContextoDeVoz contexto) {
        return NOTAS_VALIDAS.indexOf(caractere) >= 0
                || (caractere == 'M' && contexto.proximoCaractereEh('b'));
    }

    @Override
    public void aplicar(char caractere, ContextoDeVoz contexto) {
        if (caractere == 'M' && contexto.proximoCaractereEh('b')) {
            contexto.gerarNota("Mb");
            contexto.avancarPosicao(); // consome o 'b' seguinte
        } else {
            contexto.gerarNota(String.valueOf(caractere));
        }
    }
}
