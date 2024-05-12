package defaultpackage;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ChatServer {
    public static final int PORT = 4000;
    private ServerSocket serverSocket;
    private final List<ClienteSocket> clientes = new LinkedList<>();

    public void start() throws IOException {
        serverSocket = new ServerSocket(PORT);
        conexaoCliente();
        System.out.println("Servidor iniciado na porta " + PORT);
    }

    private void conexaoCliente() throws IOException {
        while (true) {
            ClienteSocket clienteSocket = new ClienteSocket(serverSocket.accept());
            clientes.add(clienteSocket);
            new Thread(() -> mensagemCliente(clienteSocket)).start();
        }
    }

    private void mensagemCliente(ClienteSocket clienteSocket) {
        String mensagem;
        try {
            while ((mensagem = clienteSocket.getMensagem()) != null) {
                if ("sair".equalsIgnoreCase(mensagem))
                    return;

                System.out.printf("Mensagem recebida do usuário %s: %s\n", clienteSocket.getEnderecoSocketRemoto(), mensagem);

                enviarMensagemParaTodos(clienteSocket, mensagem);
            }
        } finally {
            clienteSocket.fechar();
        }
    }

    private void enviarMensagemParaTodos(ClienteSocket sender, String msg) {
        Iterator<ClienteSocket> iterator = clientes.iterator();
        while (iterator.hasNext()) {
            ClienteSocket clienteSocket = iterator.next();
            if (sender.equals(clienteSocket))
                continue; 

            if (!clienteSocket.enviarMensagem("cliente " + sender.getEnderecoSocketRemoto() + ": " + msg)) {
                iterator.remove(); 
            }
        }
    }

    public static void main(String[] args) {
        try {
            ChatServer server = new ChatServer();
            server.start();
        } catch (IOException ex) {
            System.out.println("Erro ao iniciar a Comunicação: " + ex.getMessage());
        }

        System.out.println("Comunicação Encerrada!");
    }
}
