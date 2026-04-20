package org.example.GUI;

import org.example.BusinessLogic.SimulationObserver;

import javax.swing.*;
import java.awt.*;

public class Viewer extends JFrame implements SimulationObserver {
    private JTextField timeLimitField = new JTextField("15");
    private JTextField minArrField = new JTextField("2");
    private JTextField maxArrField = new JTextField("5");
    private JTextField minServField = new JTextField("3");
    private JTextField maxServField = new JTextField("4");
    private JTextField serversField = new JTextField("2");
    private JTextField clientsField = new JTextField("5");
    private JComboBox<String> strategyBox = new JComboBox<>(new String[]{"Shortest Time", "Shortest Queue"});

    private JButton startButton = new JButton("Start Simulation");
    private JTextArea logArea = new JTextArea(15, 40);

    public Viewer() {
        setTitle("Queue simulator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));

        addInputGroup(inputPanel, "Time:", timeLimitField);
        addInputGroup(inputPanel, "Min/Max Arrival Time:", minArrField, maxArrField);
        addInputGroup(inputPanel, "Min/Max Process time:", minServField, maxServField);
        addInputGroup(inputPanel, "Number of Queue / Clients:", serversField, clientsField);

        strategyBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        inputPanel.add(strategyBox);
        inputPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        inputPanel.add(startButton);
        inputPanel.add(Box.createRigidArea(new Dimension(0, 10)));


        logArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(logArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));


        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);


        setSize(400, 600);
        setLocationRelativeTo(null);
        setVisible(true);
    }


    private void addInputGroup(JPanel panel, String labelText, JTextField... fields) {
        JLabel label = new JLabel(labelText);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setFont(new Font("Dialog", Font.BOLD, 12));
        panel.add(label);

        for (JTextField field : fields) {
            field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
            panel.add(field);
        }
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
    }

    @Override
    public void updateLog(String message) {

        SwingUtilities.invokeLater(() -> {
            logArea.append(message + "\n");

            logArea.setCaretPosition(logArea.getDocument().getLength());
        });
    }

    public JButton getStartButton() { return startButton; }
    public int getClients() { return Integer.parseInt(clientsField.getText().trim()); }
    public int getServers() { return Integer.parseInt(serversField.getText().trim()); }
    public int getTimeLimit() { return Integer.parseInt(timeLimitField.getText().trim()); }
    public int getMinArr() { return Integer.parseInt(minArrField.getText().trim()); }
    public int getMaxArr() { return Integer.parseInt(maxArrField.getText().trim()); }
    public int getMinServ() { return Integer.parseInt(minServField.getText().trim()); }
    public int getMaxServ() { return Integer.parseInt(maxServField.getText().trim()); }
    public int getStrategyIndex() { return strategyBox.getSelectedIndex(); }
}