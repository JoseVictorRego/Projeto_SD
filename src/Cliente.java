import java.io.IOException;
import java.net.Socket;
import javax.swing.JOptionPane;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.InitialDirContext;
import java.util.Hashtable;

public class Cliente {

    public static void main(String[] args){
        
        int serverPort = 4000;            // Porta do servidor
        String serveIp = getServerIpFromDns(); // Obtendo o IP do DNS

        //Funções de Envio de dados
        Funcoes ClienteFuncao = new Funcoes(); //Funções Necessarias
        String escolha;

        do { //Loop para o Cliente ficar sempre em conexão com o servidor
            try(Socket socket = new Socket(serveIp, serverPort)) {
                
                // Conectando ao servidor
                System.out.println("Conectado ao servidor.");

                // Solicitação do nome do cliente
                String clientName = JOptionPane.showInputDialog("!!Bem-vindo ao nosso servidor!!\n\nDigite o seu nome, para continuar:");

                // Solicitação do caminho do arquivo a ser enviado
                String filePath = JOptionPane.showInputDialog("Digite o caminho completo do arquivo a ser enviado:");

                if(clientName==null || filePath==null){
                    JOptionPane.showMessageDialog(null, "Nome do cliente ou arquivo não indentificado!");
                }
                else{ // Enviando o nome do cliente e o arquivo
                    ClienteFuncao.enviarArquivo(socket, clientName, filePath);   
                }

                // Fechando a conexão
                System.out.println("#Conexão encerrada!");

                //Mensagem de escolha de iniciar uma nova conexão ou finalizar conexão.
                escolha = JOptionPane.showInputDialog("Digite qualquer dígito para iniciar uma nova conexão ou '0' para Finalizar Conexão:");

            } catch (IOException e) { // Caso não haja conexão com o servidor
                JOptionPane.showMessageDialog(null, "Servidor não Encontrado "); break;
            }
        } while(!escolha.equals("0"));
    }

    public static String getServerIpFromDns() {
        String servidorDns = "192.168.137.85:52";
        String nomeHost = "servidorsdjorge.dns";
        
        Hashtable<String, String> env = new Hashtable<>();
        env.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");
        env.put("java.naming.provider.url", "dns://" + servidorDns);

        try {
            InitialDirContext idc = new InitialDirContext(env);
            Attributes attrs = idc.getAttributes(nomeHost, new String[] {"A"});
            return attrs.get("A").get().toString();
        } catch (NamingException e) {
            e.printStackTrace();
            System.out.println("Falha na resolução do nome do host.");
            return "127.0.0.1"; // valor padrão em caso de erro
        }
    }
}
