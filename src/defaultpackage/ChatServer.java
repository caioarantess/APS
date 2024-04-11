package defaultpackage;

import java.io.IOException;
import java.net.ServerSocket;

public class ChatServer {
    private final int PORT = 4000;
    private ServerSocket serverSocket;

    public void start() throws IOException{
        System.out.println("Servidor inciiado na porta " + PORT);
        serverSocket = new ServerSocket(PORT);
    }
    public static void main(String[] args) {
        try {
            ChatServer server = new ChatServer();
            server.start();
        } catch (IOException ex) {
            System.out.println("Erro ao iniciar o servidor: " + ex.getMessage());
        }

        System.out.println("Servidor finalizado");
    }   
}

//27:33