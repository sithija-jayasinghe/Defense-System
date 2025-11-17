import javax.swing.*;
import java.awt.*;

public class MainController extends JFrame {
    private Observer observer;

    private JTextArea controllerArea;
    private JTextArea helicopterArea;
    private JTextArea tankArea;
    private JTextArea submarineArea;

    private JLabel strengthLabel;
    private JProgressBar strengthBar;
    private int totalStrength;

    public MainController(Observer observer) {
        this.observer = observer;
        this.totalStrength = 0;
        initializeGUI();
    }

    private void initializeGUI() {
        setTitle("Main Controller - Defense Command Center");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // -------------------- HEADER --------------------
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(25, 25, 112));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("DEFENSE COMMAND CENTER", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        // Strength Panel
        JPanel strengthPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        strengthPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        strengthLabel = new JLabel("Overall Defense Strength: 0", JLabel.CENTER);
        strengthLabel.setFont(new Font("Arial", Font.BOLD, 14));

        strengthBar = new JProgressBar(0, 12);
        strengthBar.setValue(0);
        strengthBar.setStringPainted(true);
        strengthBar.setForeground(new Color(34, 139, 34));

        strengthPanel.add(strengthLabel);
        strengthPanel.add(strengthBar);
        headerPanel.add(strengthPanel, BorderLayout.SOUTH);

        // -------------------- NEW: TABS FOR UNITS --------------------
        JTabbedPane tabPane = new JTabbedPane();

        controllerArea = createTextArea();
        helicopterArea = createTextArea();
        tankArea = createTextArea();
        submarineArea = createTextArea();

        tabPane.addTab("Controller Logs", new JScrollPane(controllerArea));
        tabPane.addTab("Helicopter", new JScrollPane(helicopterArea));
        tabPane.addTab("Tank", new JScrollPane(tankArea));
        tabPane.addTab("Submarine", new JScrollPane(submarineArea));

        // -------------------- CONTROL PANEL --------------------
        JPanel controlPanel = new JPanel(new FlowLayout());

        JButton clearButton = new JButton("Clear Controller Logs");
        clearButton.addActionListener(e -> controllerArea.setText(""));

        JButton statusButton = new JButton("System Status");
        statusButton.addActionListener(e -> displaySystemStatus());

        JButton sendMessageButton = new JButton("Send Message to Units");
        sendMessageButton.addActionListener(e -> sendMessageToUnits());

        JButton commandButton = new JButton("Send Command");
        commandButton.addActionListener(e -> sendCommand());

        controlPanel.add(clearButton);
        controlPanel.add(statusButton);
        controlPanel.add(sendMessageButton);
        controlPanel.add(commandButton);

        add(headerPanel, BorderLayout.NORTH);
        add(tabPane, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
        setVisible(true);

        addMessage("Main Controller initialized...");
        addMessage("Waiting for defense units to connect...");
    }

    private JTextArea createTextArea() {
        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setFont(new Font("Monospaced", Font.PLAIN, 12));
        area.setBackground(new Color(240, 240, 240));
        return area;
    }

    public void broadcastStrength(Strength strength) {
        calculateOverallStrength();
        addMessage("Broadcasting strength change: " + strength);

        for (Observable obs : observer.getObservables()) {
            obs.updateMessageBox("Strength broadcast: " + strength);
        }
    }

    public void relayAreaMessage(String defenseType, String message) {
        addMessage("[" + defenseType + "] Area Message: " + message);
        addUnitMessage(defenseType, message);

        for (Observable obs : observer.getObservables()) {
            if (!obs.getDefenseType().equals(defenseType)) {
                obs.updateMessageBox(defenseType + " reports: " + message);
            }
        }
    }

    public void broadcastMessage(String message) {
        addMessage("BROADCAST: " + message);

        for (Observable obs : observer.getObservables()) {
            obs.updateMessageBox("Global: " + message);
        }

        addUnitMessage("ALL", message);
    }

    public void receiveWarStrength(String defenseType, int strength) {
        addMessage("[" + defenseType + "] War Strength: " + strength);
        addUnitMessage(defenseType, "Reported strength: " + strength);
        calculateOverallStrength();
    }

    private void calculateOverallStrength() {
        totalStrength = 0;
        for (Observable obs : observer.getObservables()) {
        }
        updateStrengthDisplay();
    }

    public void updateOverallStrength(int newTotal) {
        this.totalStrength = newTotal;
        updateStrengthDisplay();
    }

    private void updateStrengthDisplay() {
        strengthLabel.setText("Overall Defense Strength: " + totalStrength);
        strengthBar.setValue(totalStrength);

        if (totalStrength < 4)
            strengthBar.setForeground(Color.RED);
        else if (totalStrength < 8)
            strengthBar.setForeground(Color.ORANGE);
        else
            strengthBar.setForeground(new Color(34, 139, 34));
    }

    private void displaySystemStatus() {
        StringBuilder status = new StringBuilder();
        status.append("=== SYSTEM STATUS ===\n");
        status.append("Active Defense Units: ").append(observer.getObservables().size()).append("\n");
        status.append("Overall Strength: ").append(totalStrength).append("\nUnits:\n");

        for (Observable obs : observer.getObservables()) {
            status.append("  - ").append(obs.getDefenseType()).append("\n");
        }

        JOptionPane.showMessageDialog(this, status.toString(),
                "System Status", JOptionPane.INFORMATION_MESSAGE);
    }

    private void addMessage(String message) {
        SwingUtilities.invokeLater(() -> {
            String timestamp = new java.text.SimpleDateFormat("HH:mm:ss")
                    .format(new java.util.Date());
            controllerArea.append("[" + timestamp + "] " + message + "\n");
        });
    }

    private void addUnitMessage(String defenseType, String message) {
        JTextArea target = switch (defenseType.toUpperCase()) {
            case "HELICOPTER" -> helicopterArea;
            case "TANK" -> tankArea;
            case "SUBMARINE" -> submarineArea;
            default -> controllerArea; // for ALL broadcasts
        };

        SwingUtilities.invokeLater(() -> {
            String ts = new java.text.SimpleDateFormat("HH:mm:ss").format(new java.util.Date());
            target.append("[" + ts + "] " + message + "\n");
        });
    }

    private void sendMessageToUnits() {
        String message = JOptionPane.showInputDialog(this,
                "Enter message to send to all defense units:",
                "Send Message to Units",
                JOptionPane.PLAIN_MESSAGE);

        if (message != null && !message.trim().isEmpty()) {
            addMessage("COMMAND CENTER: Broadcasting - " + message);
            addUnitMessage("ALL", message);

            for (Observable obs : observer.getObservables()) {
                obs.updateMessageBox(">>> COMMAND CENTER: " + message);
            }

            JOptionPane.showMessageDialog(this,
                    "Message sent to " + observer.getObservables().size() + " units",
                    "Message Sent",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void sendCommand() {
        String[] options = new String[observer.getObservables().size() + 1];
        options[0] = "ALL UNITS";

        int i = 1;
        for (Observable obs : observer.getObservables()) {
            options[i++] = obs.getDefenseType();
        }

        String selectedUnit = (String) JOptionPane.showInputDialog(this,
                "Select target unit:",
                "Send Command",
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        if (selectedUnit != null) {
            String command = JOptionPane.showInputDialog(this,
                    "Enter command for " + selectedUnit + ":",
                    "Send Command",
                    JOptionPane.PLAIN_MESSAGE);

            if (command != null && !command.trim().isEmpty()) {
                if (selectedUnit.equals("ALL UNITS")) {
                    addMessage("COMMAND to ALL UNITS: " + command);
                    addUnitMessage("ALL", "COMMAND: " + command);

                    for (Observable obs : observer.getObservables()) {
                        obs.updateMessageBox(">>> COMMAND: " + command);
                    }
                } else {
                    addMessage("COMMAND to " + selectedUnit + ": " + command);
                    addUnitMessage(selectedUnit, "DIRECT COMMAND: " + command);

                    for (Observable obs : observer.getObservables()) {
                        if (obs.getDefenseType().equals(selectedUnit)) {
                            obs.updateMessageBox(">>> DIRECT COMMAND: " + command);
                        }
                    }
                }

                JOptionPane.showMessageDialog(this,
                        "Command sent successfully!",
                        "Command Sent",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    public void sendOrderToUnit(String unitType, String order) {
        addMessage("STRATEGIC ORDER to " + unitType + ": " + order);
        addUnitMessage(unitType, "STRATEGIC ORDER: " + order);

        for (Observable obs : observer.getObservables()) {
            if (obs.getDefenseType().equals(unitType)) {
                obs.updateMessageBox("STRATEGIC ORDER: " + order);
            }
        }
    }

    public void emergencyBroadcast(String message) {
        addMessage("EMERGENCY: " + message);
        addUnitMessage("ALL", "" + message);

        for (Observable obs : observer.getObservables()) {
            obs.updateMessageBox("EMERGENCY: " + message);
        }

        JOptionPane.showMessageDialog(this,
                message,
                "EMERGENCY ALERT",
                JOptionPane.WARNING_MESSAGE);
    }
}
