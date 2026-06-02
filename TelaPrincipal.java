package gerador.ui;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import gerador.arquivo.GerenciadorDeArquivos;
import gerador.controlador.ControladorMusical;
import gerador.controlador.ControladorMusical.Estado;
import gerador.dominio.ConfiguracaoMusical;

public class TelaPrincipal extends JFrame implements ControladorMusical.Observador {

    private JTextArea campoTexto;
    private JTextArea areaLog;

    private JButton botaoPlay;
    private JButton botaoPause;
    private JButton botaoParar;
    private JProgressBar barraProgresso;

    private JSpinner spinnerBpm;
    private JSpinner spinnerVolume;
    private JSpinner spinnerOitava;
    private JComboBox<String> comboInstrumento;

    private JButton botaoImportarTxt;
    private JButton botaoSalvarTxt;
    private JButton botaoExportarMidi;

    private final ControladorMusical controlador;
    private final GerenciadorDeArquivos gerenciadorArquivos;

    private Timer timerProgresso;

    private static final String[] INSTRUMENTOS = {
            "0 - Piano", "6 - Cravo (Harpsichord)", "15 - Tubular Bells",
            "20 - Church Organ", "22 - Harmonica", "24 - Tango Accordion",
            "40 - Violino", "42 - Cello", "56 - Trompete",
            "65 - Sax Alto", "70 - Fagote (Bassoon)", "73 - Flauta",
            "110 - Gaita de Foles", "114 - Agogo", "123 - Ondas do Mar"
    };

    public TelaPrincipal() {
        this.controlador = new ControladorMusical();
        this.gerenciadorArquivos = new GerenciadorDeArquivos();
        controlador.setObservador(this);

        configurarJanela();
        construirInterface();
        configurarTimerProgresso();
        ativarEscutadores();
    }

    private void configurarJanela() {
        setTitle("Gerador de Musica a partir de Texto - Fuga de Bach");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(900, 700);
        setMinimumSize(new Dimension(750, 550));
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
        JPanel painelPrincipal = new JPanel(new BorderLayout(8, 8));
        painelPrincipal.setBorder(new EmptyBorder(10, 10, 10, 10));

        painelPrincipal.add(criarPainelSuperior(), BorderLayout.NORTH);
        painelPrincipal.add(criarPainelCentral(), BorderLayout.CENTER);
        painelPrincipal.add(criarPainelInferior(), BorderLayout.SOUTH);

        setContentPane(painelPrincipal);
    }

    private JPanel criarPainelSuperior() {
        JPanel painel = new JPanel(new BorderLayout(8, 4));

        JPanel painelConfig = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 4));
        painelConfig.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Configuracoes Iniciais",
                TitledBorder.LEFT, TitledBorder.TOP));

        painelConfig.add(new JLabel("BPM:"));
        spinnerBpm = new JSpinner(new SpinnerNumberModel(120, 20, 300, 5));
        painelConfig.add(spinnerBpm);

        painelConfig.add(new JLabel("Volume:"));
        spinnerVolume = new JSpinner(new SpinnerNumberModel(100, 0, 127, 5));
        painelConfig.add(spinnerVolume);

        painelConfig.add(new JLabel("Oitava:"));
        spinnerOitava = new JSpinner(new SpinnerNumberModel(6, 0, 9, 1));
        painelConfig.add(spinnerOitava);

        painelConfig.add(new JLabel("Instrumento:"));
        comboInstrumento = new JComboBox<>(INSTRUMENTOS);
        painelConfig.add(comboInstrumento);

        painel.add(painelConfig, BorderLayout.CENTER);

        JPanel painelArquivo = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 4));
        botaoImportarTxt = new JButton("Importar TXT");
        botaoSalvarTxt = new JButton("Salvar TXT");
        botaoExportarMidi = new JButton("Exportar MIDI");
        botaoSalvarTxt.setEnabled(false);

        painelArquivo.add(botaoImportarTxt);
        painelArquivo.add(botaoSalvarTxt);
        painelArquivo.add(botaoExportarMidi);

        painel.add(painelArquivo, BorderLayout.SOUTH);

        return painel;
    }

    private JPanel criarPainelCentral() {
        JPanel painel = new JPanel(new BorderLayout(4, 4));
        painel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Texto de Entrada (cada linha = uma voz)",
                TitledBorder.LEFT, TitledBorder.TOP));

        campoTexto = new JTextArea();
        campoTexto.setFont(new Font("Consolas", Font.PLAIN, 14));
        campoTexto.setLineWrap(true);
        campoTexto.setWrapStyleWord(true);
        campoTexto.setTabSize(4);

        campoTexto.setText(
                "[0] C D E F G A B C\n" +
                        "[4] G A B C D E F G");

        JScrollPane scroll = new JScrollPane(campoTexto);
        scroll.setPreferredSize(new Dimension(800, 250));
        painel.add(scroll, BorderLayout.CENTER);

        JLabel legenda = new JLabel(
                "<html><b>Legenda:</b> A-H = notas | a-h = pausa | Mb = Mib | " +
                        "Espaco = 2x volume | ? = +oitava | V = -oitava | " +
                        "> = +BPM | < = -BPM | [n] = atraso</html>");
        legenda.setFont(new Font("SansSerif", Font.PLAIN, 11));
        legenda.setBorder(new EmptyBorder(4, 4, 0, 0));
        painel.add(legenda, BorderLayout.SOUTH);

        return painel;
    }

    private JPanel criarPainelInferior() {
        JPanel painel = new JPanel(new BorderLayout(4, 6));

        JPanel painelControles = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 4));
        botaoPlay = new JButton("Play");
        botaoPause = new JButton("Pause");
        botaoParar = new JButton("Parar");

        botaoPause.setEnabled(false);
        botaoParar.setEnabled(false);

        painelControles.add(botaoPlay);
        painelControles.add(botaoPause);
        painelControles.add(botaoParar);

        barraProgresso = new JProgressBar(0, 100);
        barraProgresso.setStringPainted(true);
        barraProgresso.setString("Pronto");
        painelControles.add(barraProgresso);

        painel.add(painelControles, BorderLayout.NORTH);

        areaLog = new JTextArea(5, 60);
        areaLog.setFont(new Font("Consolas", Font.PLAIN, 12));
        areaLog.setEditable(false);
        areaLog.setBackground(new Color(245, 245, 245));

        JScrollPane scrollLog = new JScrollPane(areaLog);
        scrollLog.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Log de Feedback",
                TitledBorder.LEFT, TitledBorder.TOP));

        painel.add(scrollLog, BorderLayout.CENTER);

        return painel;
    }

    private void ativarEscutadores() {
        botaoPlay.addActionListener(e -> reagirAoPlay());
        botaoPause.addActionListener(e -> reagirAoPause());
        botaoParar.addActionListener(e -> reagirAoParar());
        botaoImportarTxt.addActionListener(e -> reagirAoImportarTxt());
        botaoSalvarTxt.addActionListener(e -> reagirAoSalvarTxt());
        botaoExportarMidi.addActionListener(e -> reagirAoExportarMidi());
    }

    private void reagirAoPlay() {
        Estado estado = controlador.getEstadoAtual();

        if (estado == Estado.PAUSADO) {
            controlador.retomar();
            return;
        }

        String texto = campoTexto.getText();
        if (texto == null || texto.isBlank()) {
            adicionarLog("! Insira um texto antes de reproduzir.");
            return;
        }

        limparLog();
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
            campoTexto.setText(conteudo);
            botaoSalvarTxt.setEnabled(true);
            adicionarLog("Ok: Arquivo importado: " + gerenciadorArquivos.getNomeArquivoOriginal());
        }
    }

    private void reagirAoSalvarTxt() {
        boolean sucesso = gerenciadorArquivos.salvarTextoOriginal(campoTexto.getText());
        if (sucesso) {
            adicionarLog("Ok: Arquivo salvo com sucesso.");
        } else {
            adicionarLog("X Erro ao salvar o arquivo.");
        }
    }

    private void reagirAoExportarMidi() {
        if (!controlador.temPartitura()) {
            String texto = campoTexto.getText();
            if (texto == null || texto.isBlank()) {
                adicionarLog("! Insira um texto antes de exportar.");
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

    @Override
    public void aoMudarEstado(Estado novoEstado) {
        SwingUtilities.invokeLater(() -> {
            switch (novoEstado) {
                case REPRODUZINDO:
                    botaoPlay.setText("> Play");
                    botaoPlay.setEnabled(false);
                    botaoPause.setEnabled(true);
                    botaoParar.setEnabled(true);
                    barraProgresso.setString("Reproduzindo...");
                    break;
                case PAUSADO:
                    botaoPlay.setText("> Retomar");
                    botaoPlay.setEnabled(true);
                    botaoPause.setEnabled(false);
                    botaoParar.setEnabled(true);
                    barraProgresso.setString("Pausado");
                    break;
                case PARADO:
                    botaoPlay.setText("> Play");
                    botaoPlay.setEnabled(true);
                    botaoPause.setEnabled(false);
                    botaoParar.setEnabled(false);
                    barraProgresso.setValue(0);
                    barraProgresso.setString("Pronto");
                    break;
            }
        });
    }

    @Override
    public void aoOcorrerErro(String mensagem) {
        SwingUtilities.invokeLater(() -> adicionarLog("X " + mensagem));
    }

    @Override
    public void aoReceberLog(List<String> mensagens) {
        SwingUtilities.invokeLater(() -> {
            for (String msg : mensagens) {
                adicionarLog("i " + msg);
            }
        });
    }

    private ConfiguracaoMusical obterConfiguracaoDaInterface() {
        int bpm = (int) spinnerBpm.getValue();
        int volume = (int) spinnerVolume.getValue();
        int oitava = (int) spinnerOitava.getValue();
        int instrumento = extrairNumeroInstrumento(
                (String) comboInstrumento.getSelectedItem());

        return new ConfiguracaoMusical(bpm, volume, oitava, instrumento);
    }

    private int extrairNumeroInstrumento(String item) {
        if (item == null)
            return 0;
        try {
            return Integer.parseInt(item.split(" - ")[0].trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private void adicionarLog(String mensagem) {
        areaLog.append(mensagem + "\n");
        areaLog.setCaretPosition(areaLog.getDocument().getLength());
    }

    private void limparLog() {
        areaLog.setText("");
    }

    private void configurarTimerProgresso() {
        timerProgresso = new Timer(200, e -> {
            if (controlador.getEstadoAtual() == Estado.REPRODUZINDO) {
                double progresso = controlador.getProgresso();
                barraProgresso.setValue((int) (progresso * 100));

                if (progresso >= 0.99) {
                    controlador.parar();
                }
            }
        });
        timerProgresso.start();
    }
}
