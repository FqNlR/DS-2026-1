package gerador.controlador;

import gerador.audio.ExportadorMidi;
import gerador.audio.ReprodutorMidi;
import gerador.audio.SaidaMusical;
import gerador.dominio.ConfiguracaoMusical;
import gerador.dominio.Partitura;
import gerador.interpretacao.InterpretadorDeTexto;
import gerador.mapeamento.TabelaDeMapeamento;

import java.util.List;

/*
    Orquestra a interação entre a interface do usuário e os componentes
    de interpretação e reprodução musical.
    Colabora com InterpretadorDeTexto (interpretação), SaidaMusical
    (reprodução) e ExportadorMidi (exportação) invocando seus serviços.
*/
public class ControladorMusical {

    // Estados possíveis do controlador.
    public enum Estado { PARADO, REPRODUZINDO, PAUSADO }

    private final InterpretadorDeTexto interpretador;
    private final ReprodutorMidi reprodutor;
    private final ExportadorMidi exportador;

    private Partitura partituraAtual;
    private Estado estadoAtual;

    // Observador para notificar a UI sobre mudanças de estado e erros.
    public interface Observador {
        void aoMudarEstado(Estado novoEstado);
        void aoOcorrerErro(String mensagem);
        void aoReceberLog(List<String> mensagens);
    }

    private Observador observador;

    public ControladorMusical() {
        TabelaDeMapeamento tabela = new TabelaDeMapeamento();
        this.interpretador = new InterpretadorDeTexto(tabela);
        this.reprodutor = new ReprodutorMidi();
        this.exportador = new ExportadorMidi(reprodutor);
        this.estadoAtual = Estado.PARADO;
    }

    public void setObservador(Observador observador) {
        this.observador = observador;
    }

    // Processa o texto e inicia a reprodução musical.
    public void iniciarReproducao(String texto, ConfiguracaoMusical configuracao) {
        try {
            // Interpretar o texto
            partituraAtual = interpretador.interpretar(texto, configuracao);

            // Enviar log de erros/avisos
            List<String> erros = interpretador.getLogDeErros();
            if (observador != null && !erros.isEmpty()) {
                observador.aoReceberLog(erros);
            }

            if (partituraAtual.getTotalVozes() == 0) {
                notificarErro("Nenhuma voz encontrada. Verifique o texto de entrada.");
                return;
            }

            // Iniciar reprodução
            reprodutor.reproduzir(partituraAtual);
            mudarEstado(Estado.REPRODUZINDO);

            // Notificar informações da partitura
            if (observador != null) {
                List<String> info = List.of(
                    String.format("Partitura gerada: %d voz(es), %d eventos totais.",
                            partituraAtual.getTotalVozes(), partituraAtual.getTotalEventos()),
                    String.format("BPM: %d | Reproduzindo...", configuracao.getBpm())
                );
                observador.aoReceberLog(info);
            }

        } catch (Exception e) {
            notificarErro("Erro ao reproduzir: " + e.getMessage());
        }
    }

    // Pausa a reprodução em andamento.
    public void pausar() {
        if (estadoAtual == Estado.REPRODUZINDO) {
            reprodutor.pausar();
            mudarEstado(Estado.PAUSADO);
        }
    }

    // Retoma a reprodução pausada.
    public void retomar() {
        if (estadoAtual == Estado.PAUSADO) {
            reprodutor.retomar();
            mudarEstado(Estado.REPRODUZINDO);
        }
    }

    // Para a reprodução completamente.
    public void parar() {
        reprodutor.parar();
        mudarEstado(Estado.PARADO);
    }

    // Exporta a partitura atual como arquivo MIDI.
    public void exportarMidi(String caminhoArquivo) {
        if (partituraAtual == null) {
            notificarErro("Nenhuma partitura gerada. Processe um texto primeiro.");
            return;
        }

        try {
            exportador.exportar(partituraAtual, caminhoArquivo);
            if (observador != null) {
                observador.aoReceberLog(List.of(
                    "Arquivo MIDI exportado com sucesso: " + caminhoArquivo
                ));
            }
        } catch (Exception e) {
            notificarErro("Erro ao exportar MIDI: " + e.getMessage());
        }
    }

    // Retorna o progresso atual da reprodução (0.0 a 1.0).
    public double getProgresso() {
        return reprodutor.getProgresso();
    }

    public Estado getEstadoAtual() {
        return estadoAtual;
    }

    public boolean temPartitura() {
        return partituraAtual != null;
    }

    // Libera recursos de áudio ao fechar o programa.
    public void liberarRecursos() {
        reprodutor.liberarRecursos();
    }

    // Métodos privados
    private void mudarEstado(Estado novoEstado) {
        this.estadoAtual = novoEstado;
        if (observador != null) {
            observador.aoMudarEstado(novoEstado);
        }
    }

    private void notificarErro(String mensagem) {
        if (observador != null) {
            observador.aoOcorrerErro(mensagem);
        }
    }
}
