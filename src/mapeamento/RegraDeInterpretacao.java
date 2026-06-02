package gerador.mapeamento;

import gerador.interpretacao.ContextoDeVoz;

// Para adicionar uma nova regra, basta implementar esta interface e registrá-la na TabelaDeMapeamento.
public interface RegraDeInterpretacao {

    boolean aplicavel(char caractere, ContextoDeVoz contexto);

    void aplicar(char caractere, ContextoDeVoz contexto);
}
