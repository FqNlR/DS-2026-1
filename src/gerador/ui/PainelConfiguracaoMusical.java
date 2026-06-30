package gerador.ui;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import gerador.dominio.InstrumentoMidi;
import gerador.dominio.MidiConstantes;

public class PainelConfiguracaoMusical extends JPanel {

    // Configurações Musicais Iniciais e Limites
    private static final int BPM_INICIAL = 120;
    private static final int BPM_MINIMO = 20;
    private static final int BPM_MAXIMO = 300;
    private static final int BPM_PASSO = 5;

    private static final int VOLUME_INICIAL = 100;
    private static final int VOLUME_MINIMO = MidiConstantes.VALOR_MINIMO;
    private static final int VOLUME_MAXIMO = MidiConstantes.VALOR_MAXIMO;
    private static final int VOLUME_PASSO = 5;

    private static final int OITAVA_INICIAL = 6;
    private static final int OITAVA_MINIMA = 0;
    private static final int OITAVA_MAXIMA = 9;
    private static final int OITAVA_PASSO = 1;

    // Layouts e Espaçamentos
    private static final int ESPACAMENTO_PRINCIPAL = 8;
    private static final int ESPACAMENTO_BORDA_EXTERNA = 10;
    private static final int ESPACAMENTO_PAINEIS = 4;
    private static final int ESPACAMENTO_BOTOES = 6;

    private static final String[] INSTRUMENTOS = {
            "0 - Piano", "6 - Cravo (Harpsichord)", "15 - Tubular Bells",
            "20 - Church Organ", "22 - Harmonica", "24 - Tango Accordion",
            "40 - Violino", "42 - Cello", "56 - Trompete",
            "65 - Sax Alto", "70 - Fagote (Bassoon)", "73 - Flauta",
            "110 - Gaita de Foles", "114 - Agogo", "123 - Ondas do Mar"
    };

    private JSpinner spinnerBpm;
    private JSpinner spinnerVolume;
    private JSpinner spinnerOitava;
    private JComboBox<String> comboInstrumento;

    private JButton botaoImportarTxt;
    private JButton botaoSalvarTxt;
    private JButton botaoExportarMidi;

    public PainelConfiguracaoMusical() {
        setLayout(new BorderLayout(ESPACAMENTO_PRINCIPAL, ESPACAMENTO_PAINEIS));
        add(criarPainelConfiguracoes(), BorderLayout.CENTER);
        add(criarPainelArquivos(), BorderLayout.SOUTH);
    }

    private JPanel criarPainelConfiguracoes() {
        JPanel painelConfig = new JPanel(
                new FlowLayout(FlowLayout.LEFT, ESPACAMENTO_BORDA_EXTERNA, ESPACAMENTO_PAINEIS));
        painelConfig.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Configuracoes Iniciais",
                TitledBorder.LEFT, TitledBorder.TOP));

        painelConfig.add(new JLabel("BPM:"));
        spinnerBpm = new JSpinner(new SpinnerNumberModel(BPM_INICIAL, BPM_MINIMO, BPM_MAXIMO, BPM_PASSO));
        painelConfig.add(spinnerBpm);

        painelConfig.add(new JLabel("Volume:"));
        spinnerVolume = new JSpinner(
                new SpinnerNumberModel(VOLUME_INICIAL, VOLUME_MINIMO, VOLUME_MAXIMO, VOLUME_PASSO));
        painelConfig.add(spinnerVolume);

        painelConfig.add(new JLabel("Oitava:"));
        spinnerOitava = new JSpinner(
                new SpinnerNumberModel(OITAVA_INICIAL, OITAVA_MINIMA, OITAVA_MAXIMA, OITAVA_PASSO));
        painelConfig.add(spinnerOitava);

        painelConfig.add(new JLabel("Instrumento:"));
        comboInstrumento = new JComboBox<>(INSTRUMENTOS);
        painelConfig.add(comboInstrumento);

        return painelConfig;
    }

    private JPanel criarPainelArquivos() {
        JPanel painelArquivo = new JPanel(new FlowLayout(FlowLayout.RIGHT, ESPACAMENTO_BOTOES, ESPACAMENTO_PAINEIS));
        botaoImportarTxt = new JButton("Importar TXT");
        botaoSalvarTxt = new JButton("Salvar TXT");
        botaoExportarMidi = new JButton("Exportar MIDI");
        botaoSalvarTxt.setEnabled(false);

        painelArquivo.add(botaoImportarTxt);
        painelArquivo.add(botaoSalvarTxt);
        painelArquivo.add(botaoExportarMidi);

        return painelArquivo;
    }

    public int getBpm() {
        return (int) spinnerBpm.getValue();
    }

    public int getVolume() {
        return (int) spinnerVolume.getValue();
    }

    public int getOitava() {
        return (int) spinnerOitava.getValue();
    }

    public int getInstrumento() {
        return extrairNumeroInstrumento((String) comboInstrumento.getSelectedItem());
    }

    public JButton getBotaoImportarTxt() {
        return botaoImportarTxt;
    }

    public JButton getBotaoSalvarTxt() {
        return botaoSalvarTxt;
    }

    public JButton getBotaoExportarMidi() {
        return botaoExportarMidi;
    }

    public void habilitarBotaoSalvar(boolean habilitado) {
        botaoSalvarTxt.setEnabled(habilitado);
    }

    private int extrairNumeroInstrumento(String item) {
        if (item == null)
            return InstrumentoMidi.PIANO.getCodigo();
        try {
            return Integer.parseInt(item.split(" - ")[0].trim());
        } catch (NumberFormatException e) {
            return InstrumentoMidi.PIANO.getCodigo();
        }
    }
}
