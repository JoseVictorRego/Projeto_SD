import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {
    public static void main(String[] args){

        int serverPort = 4000; // Porta do servidor
        Funcoes ServerFuncao = new Funcoes(); //Funções Necessarias

        //Abrindo Conexão do servidor
        try (ServerSocket serverSocket = new ServerSocket(serverPort)) {

            System.out.println("Servidor aguardando conexões...");

            //Fazendo com que a conexão com o cliente continue em loop
            while (true) {

                //conectando o cliente
                Socket clientSocket = serverSocket.accept();
                System.out.println("\n-----Cliente conectado------\n");

                // Tratar a conexão em uma thread separada para permitir o atendimento a múltiplos clientes
                Thread thread = new Thread(() -> {
                    try {
                        ServerFuncao.receberArquivo(clientSocket);
                    } catch (IOException e) {
                        System.out.println("-> Servidor perdeu Conexão com o cliente!!");
                    }
                });
                thread.start();
            }

        } catch (IOException e) {
            System.out.println("-> Servidor Parou!!");
        }
    }
}
