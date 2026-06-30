package gerador.mapeamento;

import gerador.interpretacao.ContextoDeVoz;

/*
  Fallback: se o último evento foi nota, repete-a; senão, gera pausa.
  Caracteres não-letra (símbolos como '(', '@', '#') geram aviso no log.
*/
public class RegraRepetirOuPausa implements RegraDeInterpretacao {

    @Override
    public boolean aplicavel(char caractere, ContextoDeVoz contexto) {
        return true;
    }

    @Override
    public void aplicar(char caractere, ContextoDeVoz contexto) {
        if (!Character.isLetter(caractere)) {
            contexto.registrarAviso(
                "Caractere não reconhecido: '" + caractere + "' (posição " + contexto.getPosicaoAtual() + ")");
        }

        if (contexto.ultimoFoiNota()) {
            contexto.repetirUltimaNota();
        } else {
            contexto.gerarPausa();
        }
    }
}
