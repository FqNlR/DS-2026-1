package gerador.mapeamento;

import gerador.interpretacao.ContextoDeVoz;

/**
 * Dígito par: instrumento = atual + valor do dígito.
 * Dígito ímpar: troca para Tubular Bells (GM 15).
 */
public class RegraDigito implements RegraDeInterpretacao {

    private static final int TUBULAR_BELLS = 15;

    @Override
    public boolean aplicavel(char caractere, ContextoDeVoz contexto) {
        return Character.isDigit(caractere);
    }

    @Override
    public void aplicar(char caractere, ContextoDeVoz contexto) {
        int valorDigito = Character.getNumericValue(caractere);

        if (valorDigito % 2 == 0) {
            int novoInstrumento = contexto.getInstrumentoAtual() + valorDigito;
            contexto.trocarInstrumento(novoInstrumento);
        } else {
            contexto.trocarInstrumento(TUBULAR_BELLS);
        }
    }
}
