package defaultpackage;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ChatServer {
    public static final int PORT = 4000;
    private ServerSocket serverSocket;
    private final List<ClienteSocket> clientes = new ArrayList<>();

    public void start() {
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Servidor iniciado na porta " + PORT);
            conexaoCliente();
        } catch (IOException ex) {
            System.out.println("Erro ao iniciar a Comunicação: " + ex.getMessage());
        }

        System.out.println("Comunicação Encerrada!");
    }

    private void conexaoCliente() {
        while (true) {
            try {
                ClienteSocket clienteSocket = new ClienteSocket(serverSocket.accept());
                clientes.add(clienteSocket);
                new Thread(() -> mensagemCliente(clienteSocket)).start();
            } catch (IOException ex) {
                System.out.println("Erro ao aceitar conexão do cliente: " + ex.getMessage());
            }
        }
    }

    private void mensagemCliente(ClienteSocket clienteSocket) {
        String mensagem;
        try {
            while ((mensagem = clienteSocket.getMensagem()) != null) {
                if ("sair".equalsIgnoreCase(mensagem)) {
                    return;
                }

                System.out.printf("Mensagem recebida do usuário %s: %s\n", clienteSocket.getNome(), mensagem);

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
            if (sender.equals(clienteSocket)) {
                continue;
            }

            if (!clienteSocket.enviarMensagemParaCliente(sender.getNome() + ": " + msg)) {
                iterator.remove();
            }
        }
    }

    public static void main(String[] args) {
        ChatServer server = new ChatServer();
        server.start();
    }
}
