package cadastroserver;

import controller.ProdutoJpaController;
import controller.UsuarioJpaController;
import java.net.ServerSocket;
import java.net.Socket;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class CadastroServer {

    public static void main(String[] args) {
        try {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("CadastroServerPU");
            ProdutoJpaController ctrlProduto = new ProdutoJpaController(emf);
            UsuarioJpaController ctrlUsuario = new UsuarioJpaController(emf);
            ServerSocket servidor = new ServerSocket(4321);
            System.out.println("Servidor socket iniciado na porta 4321...");
            while (true) {
                Socket cliente = servidor.accept();
                System.out.println("Cliente conectado: " + cliente.getInetAddress());
                CadastroThread thread = new CadastroThread(cliente, ctrlProduto, ctrlUsuario);
                thread.start();
            }
        } catch (Exception e) {
            System.err.println("Erro ao iniciar servidor: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
