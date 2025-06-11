package cadastroclientv2;

import javax.swing.*;

public class SaidaFrame extends JDialog {

    private JTextArea area;

    public SaidaFrame() {
        setTitle("Saída do Servidor");
        setSize(500, 400);
        setLocationRelativeTo(null);
        area = new JTextArea();
        area.setEditable(false);
        JScrollPane scroll = new JScrollPane(area);
        add(scroll);
        setVisible(true);
    }

    public void adicionarTexto(String texto) {
        area.append(texto + "\n");
    }
}
