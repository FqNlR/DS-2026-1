package gerador.ui;

import java.awt.*;

import javax.swing.*;

public class PainelControlesReproducao extends JPanel {

    // Espaçamentos
    private static final int ESPACAMENTO_PRINCIPAL = 8;
    private static final int ESPACAMENTO_PAINEIS = 4;

    // Barra de Progresso
    private static final int PROGRESSO_MINIMO = 0;
    private static final int PROGRESSO_MAXIMO = 100;

    private final JButton botaoPlay;
    private final JButton botaoPause;
    private final JButton botaoParar;
    private final JProgressBar barraProgresso;

    public PainelControlesReproducao() {
        setLayout(new FlowLayout(FlowLayout.CENTER, ESPACAMENTO_PRINCIPAL, ESPACAMENTO_PAINEIS));

        botaoPlay = new JButton("> Play");
        botaoPause = new JButton("|| Pause");
        botaoParar = new JButton("[] Parar");

        botaoPause.setEnabled(false);
        botaoParar.setEnabled(false);

        add(botaoPlay);
        add(botaoPause);
        add(botaoParar);

        barraProgresso = new JProgressBar(PROGRESSO_MINIMO, PROGRESSO_MAXIMO);
        barraProgresso.setStringPainted(true);
        barraProgresso.setString("Pronto");
        add(barraProgresso);
    }

    public JButton getBotaoPlay() {
        return botaoPlay;
    }

    public JButton getBotaoPause() {
        return botaoPause;
    }

    public JButton getBotaoParar() {
        return botaoParar;
    }

    public void setProgresso(int valor) {
        barraProgresso.setValue(valor);
    }

    // Métodos de atualização de estado (extraídos do método longo aoMudarEstado)

    public void aplicarEstadoReproduzindo() {
        botaoPlay.setText("> Play");
        botaoPlay.setEnabled(false);
        botaoPause.setEnabled(true);
        botaoParar.setEnabled(true);
        barraProgresso.setString("Reproduzindo...");
    }

    public void aplicarEstadoPausado() {
        botaoPlay.setText("> Retomar");
        botaoPlay.setEnabled(true);
        botaoPause.setEnabled(false);
        botaoParar.setEnabled(true);
        barraProgresso.setString("Pausado");
    }

    public void aplicarEstadoParado() {
        botaoPlay.setText("> Play");
        botaoPlay.setEnabled(true);
        botaoPause.setEnabled(false);
        botaoParar.setEnabled(false);
        barraProgresso.setValue(0);
        barraProgresso.setString("Pronto");
    }
}
