package defaultpackage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;

public class ChatClienteGUI extends JFrame {

    private static final String SERVER_ADDRESS = "127.0.0.1";
    private static final int PORT = ChatServer.PORT;

    private ClienteSocket clienteSocket;
    private JTextArea chat;
    private JTextField campoMensagem;
    private String nomeCliente;

    public ChatClienteGUI(String nomeCliente) {
        this.nomeCliente = nomeCliente;
        criarGUI();
        conectarServer();
    }

    private void criarGUI() {
        setTitle("Chat Comunicação Rio Tietê");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        JPanel painel = new JPanel(new BorderLayout());

        chat = new JTextArea();
        chat.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(chat);
        painel.add(scrollPane, BorderLayout.CENTER);

        JPanel inputPainel = new JPanel(new BorderLayout());
        campoMensagem = new JTextField();
        JButton botaoEnviar = new JButton("Enviar");
        botaoEnviar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enviarMensagem();
            }
        });
        inputPainel.add(campoMensagem, BorderLayout.CENTER);
        inputPainel.add(botaoEnviar, BorderLayout.EAST);

        painel.add(inputPainel, BorderLayout.SOUTH);

        add(painel);

        setVisible(true);
    }

    private void conectarServer() {
        try {
            Socket socket = new Socket(SERVER_ADDRESS, PORT);
            clienteSocket = new ClienteSocket(socket, nomeCliente);
            new Thread(this::receberMensagens).start();
        } catch (IOException e) {
            mostrarErro("Erro ao conectar ao servidor: " + e.getMessage());
            dispose();
        }
    }

    private void enviarMensagem() {
        String mensagem = campoMensagem.getText().trim();
        if (!mensagem.isEmpty()) {
            chat.append(nomeCliente + ": " + mensagem + "\n");
            clienteSocket.enviarMensagemParaCliente(nomeCliente + ": " + mensagem);
            campoMensagem.setText("");
        }
    }

    private void receberMensagens() {
        String mensagem;
        while ((mensagem = clienteSocket.getMensagem()) != null) {
            chat.append(mensagem + "\n");
        }
    }

    private void mostrarErro(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem, "Erro", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {

    }
}


