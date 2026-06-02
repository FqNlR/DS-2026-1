package gerador.audio;

import gerador.dominio.Partitura;

/**
 * Permite trocar a implementação (MIDI, WAV, etc.) sem alterar o resto do
 * sistema.
 */
public interface SaidaMusical {

    void reproduzir(Partitura partitura) throws Exception;

    void pausar();

    void retomar();

    void parar();

    boolean estaReproduzindo();

    double getProgresso();

    void liberarRecursos();
}
