package defaultpackage;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient implements Runnable{   
    private static final String SERVER_ADRESS = "127.0.0.1";
    private ClientSocket clientSocket;
    private Scanner scanner;

    public ChatClient(){
        scanner = new Scanner(System.in);
    }

    public void start() throws IOException{
        clientSocket = new ClientSocket(new Socket(SERVER_ADRESS, ChatServer.PORT));  
        System.out.println("Cliente conctado ao servidor em " + SERVER_ADRESS + ":" + ChatServer.PORT);
        new Thread(this).start();;
        messageLoop();
    } 

    @Override
    public void run(){
        String msg;
        while ((msg = clientSocket.getMessage()) != null) {
            System.out.printf("Mensagem recebida do servidor: %s\n", msg);
        }
        
    }

    private void messageLoop() throws IOException{
        String msg;
        do {
            System.out.print("Digite uma mensagem (ou sair para finalizar): ");
            msg = scanner.nextLine();
            clientSocket.sendMsg(msg);
        } while (!msg.equalsIgnoreCase("sair"));
    }
    public static void main(String[] args) {
        try {
            ChatClient client = new ChatClient();
            client.start();
        } catch (IOException ex) {
            System.out.println("Erro ao inciiar cliente: " + ex.getMessage());        }
        
        System.out.println("Cliente Finalizado!");
    }
}
