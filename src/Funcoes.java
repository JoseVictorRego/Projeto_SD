import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Funcoes {
    private static Map<String, String> clientFolders = new HashMap<>(); // Mapear o nome do cliente para a pasta

    public void receberArquivo(Socket clientSocket) throws IOException {
        
        InputStream is = clientSocket.getInputStream();

        // Receber o nome do cliente
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String clientName = br.readLine();

        // Verificar se a pasta do cliente já foi criada
        String saveDir = clientFolders.get(clientName);
        if (saveDir == null) {
            // Se a pasta ainda não existe, criar uma nova pasta
            String currentDir = System.getProperty("user.dir");
            saveDir = currentDir + "\\arquivos\\" + clientName + "\\";
            
            File directory = new File(saveDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }
            // Adicionar a pasta ao mapa para reutilização futura
            clientFolders.put(clientName, saveDir);
        }

        // Receber o nome do arquivo
        String fileName = br.readLine();

        // Criar um novo arquivo com o nome recebido
        File file = new File(saveDir + fileName);
        FileOutputStream fos = new FileOutputStream(file);

        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = is.read(buffer)) != -1) {
            fos.write(buffer, 0, bytesRead);
        }

        fos.flush();
        fos.close();
        System.out.println("Cliente: "+clientName+".\n ->Enviou um Arquivo e foi salvo com sucesso: " + fileName);

        // Fechando a conexão com o cliente
        clientSocket.close();
        System.out.println("Cliente: "+clientName+" Saiu!!\n");
    }

    public void enviarArquivo(Socket socket, String clientName, String filePath) throws IOException {
        
        File arquivo = new File(filePath); // Usando a Função File.

        if (!arquivo.exists()) {           //Caso o Arquivo não exista ou não encontrado
            System.out.println("\n ->Arquivo não encontrado.\n");
            return;
        }

        // Enviar o nome do cliente antes do nome do arquivo e dos dados do arquivo
        OutputStream os = socket.getOutputStream();
        os.write(clientName.getBytes());
        os.write("\n".getBytes());

        //Copia o nome do arquivo para a string fileName para enviar ao servidor.
        String fileName = arquivo.getName();
        os.write(fileName.getBytes());
        os.write("\n".getBytes());

        // Reseve o Arquivo para preparar o envio 
        FileInputStream fis = new FileInputStream(arquivo); 

        byte[] buffer = new byte[4096]; //buffer usado para armazenar temporariamente os bytes lidos do arquivo antes de serem enviados
        
        //Quarda a quantidade de bytes lidos
        int bytesRead;

        //Faz a leiturua dos byte do arquivo e envia ao servido e finaliza quando chegar em '-1'
        while ((bytesRead = fis.read(buffer)) != -1) {
            os.write(buffer, 0, bytesRead);
        }

        os.flush();
        fis.close();
        
        System.out.println("\n ->Arquivo enviado com sucesso.\n");
    }

}

