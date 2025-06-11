package cadastroclientv2;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;
import model.Produto;
import java.util.List;

public class CadastroClientV2 {

    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 4321);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            Scanner sc = new Scanner(System.in);
            SaidaFrame tela = new SaidaFrame();

            out.writeObject("op1");
            out.writeObject("op1");

            Object resposta = in.readObject();
            if (!"LOGIN_OK".equals(resposta)) {
                tela.adicionarTexto("Login inválido.");
                socket.close();
                return;
            }
            tela.adicionarTexto("Login bem-sucedido.");

            ThreadClient thread = new ThreadClient(in, tela);
            thread.start();

            while (true) {
                System.out.print("Digite comando (L, E, S, X): ");
                String comando = sc.nextLine();

                if (comando.equalsIgnoreCase("L") || comando.equalsIgnoreCase("X")) {
                    out.writeObject(comando);
                    if (comando.equalsIgnoreCase("X")) {
                        tela.adicionarTexto("Encerrando...");
                        socket.close();
                        break;
                    }
                } else if (comando.equalsIgnoreCase("E") || comando.equalsIgnoreCase("S")) {
                    out.writeObject(comando);

                    System.out.print("Id da pessoa: ");
                    int idPessoa = Integer.parseInt(sc.nextLine());
                    out.writeObject(idPessoa);

                    System.out.print("Id do produto: ");
                    int idProduto = Integer.parseInt(sc.nextLine());
                    out.writeObject(idProduto);

                    System.out.print("Quantidade: ");
                    int quantidade = Integer.parseInt(sc.nextLine());
                    out.writeObject(quantidade);

                    System.out.print("Valor unitário: ");
                    double valor = Double.parseDouble(sc.nextLine());
                    out.writeObject(valor);
                } else {
                    System.out.println("Comando inválido. Tente novamente.");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}