package cadastroclientv2;

import java.io.ObjectInputStream;
import java.util.Comparator;
import java.util.List;
import javax.swing.SwingUtilities;
import model.Produto;

public class ThreadClient extends Thread {

    private ObjectInputStream in;
    private SaidaFrame tela;

    public ThreadClient(ObjectInputStream in, SaidaFrame tela) {
        this.in = in;
        this.tela = tela;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Object obj = in.readObject();

                if (obj instanceof String) {
                    String msg = (String) obj;
                    SwingUtilities.invokeLater(() -> tela.adicionarTexto(msg));
                } else if (obj instanceof List) {
                    List<Produto> produtos = (List<Produto>) obj;

                    // Ordenar por nome (alfabeticamente, ignorando maiúsculas/minúsculas)
                    produtos.sort(Comparator.comparing(p -> p.getNome().toLowerCase()));

                    for (Produto p : produtos) {
                        String texto = p.getNome() + ":" + p.getQuantidade();
                        SwingUtilities.invokeLater(() -> tela.adicionarTexto(texto));
                    }
                }
            }
        } catch (Exception e) {
            SwingUtilities.invokeLater(() -> tela.adicionarTexto("Conexão encerrada."));
        }
    }
}
