package defaultpackage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;

public class ChatClientGUI extends JFrame {

    private static final String SERVER_ADDRESS = "127.0.0.1";
    private static final int PORT = ChatServer.PORT;

    private ClientSocket clientSocket;
    private JTextArea chatArea;
    private JTextField messageField;

    public ChatClientGUI() {
        setTitle("Chat Client");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(chatArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new BorderLayout());
        messageField = new JTextField();
        JButton sendButton = new JButton("Enviar");
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });
        inputPanel.add(messageField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        panel.add(inputPanel, BorderLayout.SOUTH);

        add(panel);

        setVisible(true);

        connectToServer();
    }

    private void connectToServer() {
        try {
            Socket socket = new Socket(SERVER_ADDRESS, PORT); // Cria o Socket
            clientSocket = new ClientSocket(socket); // Passa o Socket para o ClientSocket
            new Thread(this::receiveMessages).start();
        } catch (IOException e) {
            showErrorDialog("Erro ao conectar ao servidor: " + e.getMessage());
            dispose();
        }
    }

    private void sendMessage() {
        String message = messageField.getText().trim();
        if (!message.isEmpty()) {
            // Exibe a mensagem do cliente na área de chat antes de enviá-la para o servidor
            chatArea.append("Você: " + message + "\n");

            // Envia a mensagem para o servidor
            clientSocket.sendMsg(message);
            messageField.setText("");
        }
    }

    private void receiveMessages() {
        String message;
        while ((message = clientSocket.getMessage()) != null) {
            chatArea.append("Servidor: " + message + "\n");
        }
    }

    private void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(this, message, "Erro", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ChatClientGUI();
            }
        });
    }
}
