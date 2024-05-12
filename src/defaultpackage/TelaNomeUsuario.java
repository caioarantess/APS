package defaultpackage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TelaNomeUsuario extends JFrame {

    private JTextField campoNome;

    public TelaNomeUsuario() {
        setTitle("Login");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(300, 150);
        setLocationRelativeTo(null);

        JPanel painel = new JPanel(new BorderLayout());

        JPanel centroPainel = new JPanel(new FlowLayout());
        JLabel labelNome = new JLabel("Nome:");
        campoNome = new JTextField(20);
        centroPainel.add(labelNome);
        centroPainel.add(campoNome);

        JPanel sulPainel = new JPanel(new FlowLayout());
        JButton botaoAvancar = new JButton("Avançar");
        sulPainel.add(botaoAvancar);

        painel.add(centroPainel, BorderLayout.CENTER);
        painel.add(sulPainel, BorderLayout.SOUTH);

        add(painel);

        botaoAvancar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nome = campoNome.getText().trim();
                if (!nome.isEmpty()) {
                    dispose();
                    new ChatClienteGUI(nome);
                } else {
                    JOptionPane.showMessageDialog(TelaNomeUsuario.this, "Por favor, insira seu nome.", "Nome em branco", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new TelaNomeUsuario();
            }
        });
    }
}

