package gerador.ui;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;

public class PainelLog extends JPanel {

    // Fontes e Texto
    private static final int TAMANHO_FONTE_LOG = 12;
    private static final int LOG_LINHAS = 5;
    private static final int LOG_COLUNAS = 60;

    // Cores
    private static final Color COR_FUNDO_LOG = new Color(245, 245, 245);

    private final JTextArea areaLog;

    public PainelLog() {
        setLayout(new BorderLayout());

        areaLog = new JTextArea(LOG_LINHAS, LOG_COLUNAS);
        areaLog.setFont(new Font("Consolas", Font.PLAIN, TAMANHO_FONTE_LOG));
        areaLog.setEditable(false);
        areaLog.setBackground(COR_FUNDO_LOG);

        JScrollPane scrollLog = new JScrollPane(areaLog);
        scrollLog.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Log de Feedback",
                TitledBorder.LEFT, TitledBorder.TOP));

        add(scrollLog, BorderLayout.CENTER);
    }

    public void adicionarLog(String mensagem) {
        areaLog.append(mensagem + "\n");
        areaLog.setCaretPosition(areaLog.getDocument().getLength());
    }

    public void limpar() {
        areaLog.setText("");
    }
}
