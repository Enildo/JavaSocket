
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import model.Produto;

public class CadastroClient {

    public static void main(String[] args) {
        try {
            // 1. Conecta no servidor
            Socket socket = new Socket("localhost", 4321);

            // 2. Cria canais de entrada/saída
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            // 3. Envia login e senha
            out.writeObject("op1"); // login
            out.writeObject("op1"); // senha

            // 4. Recebe resposta
            String resposta = (String) in.readObject();
            if (!"LOGIN_OK".equals(resposta)) {
                System.out.println("Login inválido.");
                socket.close();
                return;
            }

            System.out.println("Login realizado com sucesso.");

            // 5. Envia comando "L" (listar produtos)
            out.writeObject("L");

            // 6. Recebe e exibe a lista de produtos
            Object respostaProdutos = in.readObject();
            if (respostaProdutos instanceof List) {
                List<Produto> produtos = (List<Produto>) respostaProdutos;
                for (Produto p : produtos) {
                    System.out.println("Produto: " + p.getNome());
                }
            }

            // 7. Encerra conexão
            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
