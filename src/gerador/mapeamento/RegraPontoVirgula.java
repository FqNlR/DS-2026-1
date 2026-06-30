package gerador.mapeamento;

import gerador.dominio.InstrumentoMidi;
import gerador.interpretacao.ContextoDeVoz;

public class RegraPontoVirgula implements RegraDeInterpretacao {

    @Override
    public boolean aplicavel(char caractere, ContextoDeVoz contexto) {
        return caractere == ';';
    }

    @Override
    public void aplicar(char caractere, ContextoDeVoz contexto) {
        contexto.trocarInstrumento(InstrumentoMidi.TUBULAR_BELLS.getCodigo());
    }
}
