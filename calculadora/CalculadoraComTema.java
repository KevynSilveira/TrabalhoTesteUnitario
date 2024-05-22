import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class CalculadoraComTema {

    private JFrame frame;
    private JTextField display;
    private boolean temaEscuro = false;

    public CalculadoraComTema() {
        frame = new JFrame("Calculadora");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 400);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null); // Centraliza a janela na tela

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(temaEscuro ? Color.DARK_GRAY : Color.WHITE);

        display = new JTextField();
        display.setEditable(false);
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.setPreferredSize(new Dimension(300, 50));
        display.setFont(new Font("Arial", Font.PLAIN, 20));
        display.setBackground(temaEscuro ? Color.BLACK : Color.WHITE);
        display.setForeground(temaEscuro ? Color.WHITE : Color.BLACK);

        panel.add(display, BorderLayout.NORTH);

        JPanel buttonsPanel = new JPanel(new GridLayout(5, 4, 5, 5));
        buttonsPanel.setBackground(temaEscuro ? Color.DARK_GRAY : Color.WHITE);

        String[] buttonLabels = {
            "7", "8", "9", "/",
            "4", "5", "6", "*",
            "1", "2", "3", "-",
            "0", ".", "⌫", "+",
            "=", "Tempo" // Adicionando botão de resultado e botão de tema
        };

        for (String label : buttonLabels) {
            JButton button = new JButton(label);
            button.addActionListener(new ButtonClickListener());
            button.setPreferredSize(new Dimension(50, 50));
            button.setFont(new Font("Arial", Font.PLAIN, 20));
            buttonsPanel.add(button);
        }

        panel.add(buttonsPanel, BorderLayout.CENTER);
        frame.add(panel);
        frame.setVisible(true);
    }

    private void updateTema() {
        Color background = temaEscuro ? Color.DARK_GRAY : Color.WHITE;
        Color foreground = temaEscuro ? Color.WHITE : Color.BLACK;

        frame.getContentPane().setBackground(background);
        display.setBackground(background);
        display.setForeground(foreground);

        Component[] components = frame.getContentPane().getComponents();
        for (Component component : components) {
            if (component instanceof JPanel) {
                component.setBackground(background);
                for (Component button : ((JPanel) component).getComponents()) {
                    if (button instanceof JButton) {
                        button.setBackground(background);
                        button.setForeground(foreground);
                    }
                }
            }
        }
    }

    private class ButtonClickListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            if (command.equals("=")) {
                // Calcular
                String expression = display.getText();
                try {
                    ScriptEngineManager manager = new ScriptEngineManager();
                    ScriptEngine engine = manager.getEngineByName("JavaScript");
                    Object result = engine.eval(expression);
                    display.setText(result.toString());
                } catch (ScriptException ex) {
                    display.setText("Erro");
                }
            } else if (command.equals("⌫")) {
                // Apagar o último caractere
                String currentText = display.getText();
                if (!currentText.isEmpty()) {
                    display.setText(currentText.substring(0, currentText.length() - 1));
                }
            } else if (command.equals("Tema")) {
                // Alternar entre temas
                temaEscuro = !temaEscuro;
                updateTema();
            } else {
                display.setText(display.getText() + command);
            }
        }
    }

    public static void main(String[] args) {
        new CalculadoraComTema();
    }
}
