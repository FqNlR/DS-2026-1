package gerador.ui;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

public class PainelTextoEntrada extends JPanel {

    // Fontes e Texto
    private static final int TAMANHO_FONTE_TEXTO = 14;
    private static final int TAMANHO_FONTE_LEGENDA = 11;
    private static final int TAMANHO_TABULACAO = 4;

    // UI
    private static final int LARGURA_SCROLL_TEXTO = 800;
    private static final int ALTURA_SCROLL_TEXTO = 250;

    // Espaçamentos
    private static final int ESPACAMENTO_PAINEIS = 4;
    private static final int ESPACAMENTO_LEGENDA = 4;

    private final JTextArea campoTexto;

    public PainelTextoEntrada() {
        setLayout(new BorderLayout(ESPACAMENTO_PAINEIS, ESPACAMENTO_PAINEIS));
        setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Texto de Entrada (cada linha = uma voz)",
                TitledBorder.LEFT, TitledBorder.TOP));

        campoTexto = criarCampoTexto();

        JScrollPane scroll = new JScrollPane(campoTexto);
        scroll.setPreferredSize(new Dimension(LARGURA_SCROLL_TEXTO, ALTURA_SCROLL_TEXTO));
        add(scroll, BorderLayout.CENTER);

        add(criarLegenda(), BorderLayout.SOUTH);
    }

    private JTextArea criarCampoTexto() {
        JTextArea campo = new JTextArea();
        campo.setFont(new Font("Consolas", Font.PLAIN, TAMANHO_FONTE_TEXTO));
        campo.setLineWrap(true);
        campo.setWrapStyleWord(true);
        campo.setTabSize(TAMANHO_TABULACAO);

        campo.setText(
                "[0] C D E F G A B C\n" +
                        "[4] G A B C D E F G");

        return campo;
    }

    private JLabel criarLegenda() {
        JLabel legenda = new JLabel(
                "<html><b>Legenda:</b> A-H = notas | a-h = pausa | Mb = Mib | " +
                        "Espaco = 2x volume | ? = +oitava | V = -oitava | " +
                        "> = +BPM | < = -BPM | [n] = atraso</html>");
        legenda.setFont(new Font("SansSerif", Font.PLAIN, TAMANHO_FONTE_LEGENDA));
        legenda.setBorder(new EmptyBorder(ESPACAMENTO_LEGENDA, ESPACAMENTO_LEGENDA, 0, 0));
        return legenda;
    }

    public String getTexto() {
        return campoTexto.getText();
    }

    public void setTexto(String texto) {
        campoTexto.setText(texto);
    }
}
