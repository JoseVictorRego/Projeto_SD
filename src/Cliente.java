import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Cliente {
    public static void main(String[] args) throws InterruptedException{
        
        int serverPort = 4000;             // Porta do servidor
        String serveIp = "127.0.0.1";      // IP do Servidor
        String escolha;

        //Funções de Envio de dados
        Funcoes ClienteFuncao = new Funcoes(); //Funções Necessarias

        do { //Loop Basico para o Cliente ficar sempre em conexão com o servidor
            try(Socket socket = new Socket(serveIp, serverPort)) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
                
                // Conectando ao servidor
                System.out.println("Conectado ao servidor.");

                // Solicitação para selecionar um arquivo
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

                // Solicitação do nome do cliente
                System.out.print("Digite o seu nome: "); 
                String clientName = br.readLine();

                // Solicitação do caminho do arquivo a ser enviado
                System.out.print("Digite o caminho completo do arquivo a ser enviado: ");
                String filePath = br.readLine();

                // Enviando o nome do cliente e o arquivo com o nome do arquivo
                ClienteFuncao.enviarArquivo(socket, clientName, filePath);

                // Fechando a conexão
                System.out.println("Conexão encerrada!");

                System.out.print("\nÉ de seu desejo Iniciar uma Nova Conexão ou Finalizar à " 
                    + "Aplicação?\n -> Digite qualquer digito para Iniciar uma Nova conexão ou '0' para Finalizar Conexão: ");
                escolha = br.readLine();

            } catch (IOException e) { // Caso não haja conexão com o servidor
                System.out.println("Servidor não Encontrado."); break;
            }
        }while(!escolha.equals("0"));
    }
}
