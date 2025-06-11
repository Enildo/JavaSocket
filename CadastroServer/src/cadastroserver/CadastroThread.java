package cadastroserver;

import controller.ProdutoJpaController;
import controller.UsuarioJpaController;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;
import model.Produto;
import model.Usuario;

public class CadastroThread extends Thread {

    private Socket socket;
    private ProdutoJpaController ctrl;
    private UsuarioJpaController ctrlUsu;

    public CadastroThread(Socket socket, ProdutoJpaController ctrl, UsuarioJpaController ctrlUsu) {
        this.socket = socket;
        this.ctrl = ctrl;
        this.ctrlUsu = ctrlUsu;
    }

    @Override
    public void run() {
        try (
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream())
        ) {
            String login = (String) in.readObject();
            String senha = (String) in.readObject();
            Usuario usuario = ctrlUsu.findUsuario(login, senha);
            if (usuario == null) {
                out.writeObject("LOGIN_INVALIDO");
                socket.close();
                return;
            } else {
                out.writeObject("LOGIN_OK");
            }
            while (true) {
                String comando = (String) in.readObject();
                if ("L".equalsIgnoreCase(comando)) {
                    List<Produto> produtos = ctrl.findProdutoEntities();
                    out.writeObject(produtos);
                } else {
                    out.writeObject("COMANDO_DESCONHECIDO");
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Erro na thread de comunicação: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
