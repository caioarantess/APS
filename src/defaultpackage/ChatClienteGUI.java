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
    private JTextArea chatArea;
    private JTextField campoMensagem;

    public ChatClienteGUI() {
        setTitle("Chat Cliente");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        JPanel painel = new JPanel(new BorderLayout());

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(chatArea);
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

        conectarServer();
    }

    private void conectarServer() {
        try {
            Socket socket = new Socket(SERVER_ADDRESS, PORT); // Cria o Socket
            clienteSocket = new ClienteSocket(socket); // Passa o Socket para o ClientSocket
            new Thread(this::receberMensagens).start();
        } catch (IOException e) {
            mostrarErro("Erro ao conectar ao servidor: " + e.getMessage());
            dispose();
        }
    }

    private void enviarMensagem() {
        String mensagem = campoMensagem.getText().trim();
        if (!mensagem.isEmpty()) {
            // Exibe a mensagem do cliente na área de chat antes de enviá-la para o servidor
            chatArea.append("Você: " + mensagem + "\n");

            // Envia a mensagem para o servidor
            clienteSocket.enviarMensagem(mensagem);
            campoMensagem.setText("");
        }
    }

    private void receberMensagens() {
        String mensagem;
        while ((mensagem = clienteSocket.getMensagem()) != null) {
            chatArea.append("Servidor: " + mensagem + "\n");
        }
    }

    private void mostrarErro(String message) {
        JOptionPane.showMessageDialog(this, message, "Erro", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ChatClienteGUI();
            }
        });
    }
}
