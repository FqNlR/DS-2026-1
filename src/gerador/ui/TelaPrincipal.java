package gerador.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import gerador.arquivo.GerenciadorDeArquivos;
import gerador.controlador.ControladorMusical;
import gerador.controlador.ControladorMusical.Estado;
import gerador.dominio.ConfiguracaoMusical;
import gerador.dominio.ParametrosConfiguracao;

public class TelaPrincipal extends JFrame implements ControladorMusical.Observador {

    // Configurações de Janela
    private static final int LARGURA_JANELA_PADRAO = 900;
    private static final int ALTURA_JANELA_PADRAO = 700;
    private static final int LARGURA_JANELA_MINIMA = 750;
    private static final int ALTURA_JANELA_MINIMA = 550;

    // Layouts e Espaçamentos
    private static final int ESPACAMENTO_PRINCIPAL = 8;
    private static final int ESPACAMENTO_BORDA_EXTERNA = 10;
    private static final int ESPACAMENTO_PAINEIS = 4;
    private static final int ESPACAMENTO_BOTOES = 6;

    // Timer
    private static final int TIMER_PROGRESSO_DELAY_MS = 200;

    private final ControladorMusical controlador;
    private final GerenciadorDeArquivos gerenciadorArquivos;

    private final PainelConfiguracaoMusical painelConfiguracao;
    private final PainelTextoEntrada painelTexto;
    private final PainelControlesReproducao painelControles;
    private final PainelLog painelLog;

    private Timer timerProgresso;

    public TelaPrincipal() {
        this.controlador = new ControladorMusical();
        this.gerenciadorArquivos = new GerenciadorDeArquivos();
        controlador.setObservador(this);

        this.painelConfiguracao = new PainelConfiguracaoMusical();
        this.painelTexto = new PainelTextoEntrada();
        this.painelControles = new PainelControlesReproducao();
        this.painelLog = new PainelLog();

        configurarJanela();
        construirInterface();
        configurarTimerProgresso();
        ativarEscutadores();
    }

    private void configurarJanela() {
        setTitle("Gerador de Musica a partir de Texto - Fuga de Bach");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(LARGURA_JANELA_PADRAO, ALTURA_JANELA_PADRAO);
        setMinimumSize(new Dimension(LARGURA_JANELA_MINIMA, ALTURA_JANELA_MINIMA));
        setLocationRelativeTo(null);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                controlador.liberarRecursos();
                dispose();
                System.exit(0);
            }
        });
    }

    private void construirInterface() {
        JPanel painelPrincipal = new JPanel(new BorderLayout(ESPACAMENTO_PRINCIPAL, ESPACAMENTO_PRINCIPAL));
        painelPrincipal.setBorder(new EmptyBorder(
                ESPACAMENTO_BORDA_EXTERNA, ESPACAMENTO_BORDA_EXTERNA,
                ESPACAMENTO_BORDA_EXTERNA, ESPACAMENTO_BORDA_EXTERNA));

        painelPrincipal.add(painelConfiguracao, BorderLayout.NORTH);
        painelPrincipal.add(painelTexto, BorderLayout.CENTER);

        JPanel painelInferior = new JPanel(new BorderLayout(ESPACAMENTO_PAINEIS, ESPACAMENTO_BOTOES));
        painelInferior.add(painelControles, BorderLayout.NORTH);
        painelInferior.add(painelLog, BorderLayout.CENTER);

        painelPrincipal.add(painelInferior, BorderLayout.SOUTH);

        setContentPane(painelPrincipal);
    }

    private void ativarEscutadores() {
        painelControles.getBotaoPlay().addActionListener(e -> reagirAoPlay());
        painelControles.getBotaoPause().addActionListener(e -> reagirAoPause());
        painelControles.getBotaoParar().addActionListener(e -> reagirAoParar());
        painelConfiguracao.getBotaoImportarTxt().addActionListener(e -> reagirAoImportarTxt());
        painelConfiguracao.getBotaoSalvarTxt().addActionListener(e -> reagirAoSalvarTxt());
        painelConfiguracao.getBotaoExportarMidi().addActionListener(e -> reagirAoExportarMidi());
    }

    // Reações aos eventos do usuário

    private void reagirAoPlay() {
        Estado estado = controlador.getEstadoAtual();

        if (estado == Estado.PAUSADO) {
            controlador.retomar();
            return;
        }

        String texto = painelTexto.getTexto();
        if (texto == null || texto.isBlank()) {
            painelLog.adicionarLog("! Insira um texto antes de reproduzir.");
            return;
        }

        painelLog.limpar();
        ConfiguracaoMusical config = obterConfiguracaoDaInterface();
        controlador.iniciarReproducao(texto, config);
    }

    private void reagirAoPause() {
        controlador.pausar();
    }

    private void reagirAoParar() {
        controlador.parar();
    }

    private void reagirAoImportarTxt() {
        String conteudo = gerenciadorArquivos.lerArquivoTexto(this);
        if (conteudo != null) {
            painelTexto.setTexto(conteudo);
            painelConfiguracao.habilitarBotaoSalvar(true);
            painelLog.adicionarLog("Ok: Arquivo importado: " + gerenciadorArquivos.getNomeArquivoOriginal());
        }
    }

    private void reagirAoSalvarTxt() {
        boolean sucesso = gerenciadorArquivos.salvarTextoOriginal(painelTexto.getTexto());
        if (sucesso) {
            painelLog.adicionarLog("Ok: Arquivo salvo com sucesso.");
        } else {
            painelLog.adicionarLog("X Erro ao salvar o arquivo.");
        }
    }

    private void reagirAoExportarMidi() {
        if (!controlador.temPartitura()) {
            String texto = painelTexto.getTexto();
            if (texto == null || texto.isBlank()) {
                painelLog.adicionarLog("! Insira um texto antes de exportar.");
                return;
            }
            ConfiguracaoMusical config = obterConfiguracaoDaInterface();
            controlador.iniciarReproducao(texto, config);
            controlador.parar();
        }

        String caminho = gerenciadorArquivos.escolherCaminhoMidi(this);
        if (caminho != null) {
            controlador.exportarMidi(caminho);
        }
    }

    // Callbacks do Observador (ControladorMusical.Observador)

    @Override
    public void aoMudarEstado(Estado novoEstado) {
        SwingUtilities.invokeLater(() -> {
            switch (novoEstado) {
                case REPRODUZINDO:
                    painelControles.aplicarEstadoReproduzindo();
                    break;
                case PAUSADO:
                    painelControles.aplicarEstadoPausado();
                    break;
                case PARADO:
                    painelControles.aplicarEstadoParado();
                    break;
            }
        });
    }

    @Override
    public void aoOcorrerErro(String mensagem) {
        SwingUtilities.invokeLater(() -> painelLog.adicionarLog("X " + mensagem));
    }

    @Override
    public void aoReceberLog(List<String> mensagens) {
        SwingUtilities.invokeLater(() -> {
            for (String msg : mensagens) {
                painelLog.adicionarLog("i " + msg);
            }
        });
    }

    // Métodos auxiliares

    private ConfiguracaoMusical obterConfiguracaoDaInterface() {
        int bpm = painelConfiguracao.getBpm();
        int volume = painelConfiguracao.getVolume();
        int oitava = painelConfiguracao.getOitava();
        int instrumento = painelConfiguracao.getInstrumento();

        return new ConfiguracaoMusical(new ParametrosConfiguracao(bpm, volume, oitava, instrumento));
    }

    private void configurarTimerProgresso() {
        timerProgresso = new Timer(TIMER_PROGRESSO_DELAY_MS, e -> {
            if (controlador.getEstadoAtual() == Estado.REPRODUZINDO) {
                double progresso = controlador.getProgresso();
                painelControles.setProgresso((int) (progresso * 100));

                if (progresso >= 0.99) {
                    controlador.parar();
                }
            }
        });
        timerProgresso.start();
    }
}
