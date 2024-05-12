package defaultpackage;

import java.io.*;
import java.net.Socket;
import java.net.SocketAddress;

public class ClienteSocket {
    private final Socket socket;
    private final BufferedReader in;
    private final PrintWriter out;
    private final String nome;

    public ClienteSocket(Socket socket) throws IOException {
        this.socket = socket;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.nome = "";
    }

    public ClienteSocket(Socket socket, String nome) throws IOException {
        this.socket = socket;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void fechar() {
        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            System.out.println("Erro ao fechar socket: " + e.getMessage());
        }
    }

    public String getMensagem() {
        try {
            return in.readLine();
        } catch(IOException e) {
            return null;
        }
    }

    public boolean enviarMensagemParaCliente(String msg) {
        out.println(msg);
        return !out.checkError();
    }
}

