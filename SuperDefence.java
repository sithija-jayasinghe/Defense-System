import javax.swing.*;
import java.awt.*;

public abstract class SuperDefence extends JFrame implements Observable {
    protected Observer observer;
    protected int soldierCount;
    protected int ammoCount;
    protected Strength currentStrength;
    protected JTextArea messageBox;
    protected JLabel soldierLabel;
    protected JLabel ammoLabel;
    protected JLabel strengthLabel;
    protected String defenseType;

    public SuperDefence(Observer observer, String defenseType) {
        this.observer = observer;
        this.defenseType = defenseType;
        this.soldierCount = 10;
        this.ammoCount = 100;
        this.currentStrength = Strength.MEDIUM;
    }

    public SuperDefence() {

    }

    protected void initializeCommonGUI() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
    }

    protected JPanel createStatusPanel() {
        JPanel statusPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        statusPanel.setBorder(BorderFactory.createTitledBorder("Status"));

        soldierLabel = new JLabel("Soldiers: " + soldierCount);
        ammoLabel = new JLabel("Ammunition: " + ammoCount);
        strengthLabel = new JLabel("Strength: " + currentStrength.getDisplayName());

        statusPanel.add(soldierLabel);
        statusPanel.add(ammoLabel);
        statusPanel.add(strengthLabel);

        return statusPanel;
    }

    protected JPanel createMessagePanel() {
        JPanel messagePanel = new JPanel(new BorderLayout());
        messagePanel.setBorder(BorderFactory.createTitledBorder("Messages"));

        messageBox = new JTextArea(8, 30);
        messageBox.setEditable(false);
        messageBox.setFont(new Font("Monospaced", Font.PLAIN, 11));
        JScrollPane scrollPane = new JScrollPane(messageBox);

        messagePanel.add(scrollPane, BorderLayout.CENTER);

        return messagePanel;
    }

    protected void updateStatus() {
        soldierLabel.setText("Soldiers: " + soldierCount);
        ammoLabel.setText("Ammunition: " + ammoCount);
        strengthLabel.setText("Strength: " + currentStrength.getDisplayName());
        calculateAndSendStrength();
    }

    protected void calculateAndSendStrength() {
        int strengthValue = 0;

        if (soldierCount > 7 && ammoCount > 70) {
            currentStrength = Strength.STRONG;
            strengthValue = 4;
        } else if (soldierCount > 5 && ammoCount > 50) {
            currentStrength = Strength.HIGH;
            strengthValue = 3;
        } else if (soldierCount > 3 && ammoCount > 30) {
            currentStrength = Strength.MEDIUM;
            strengthValue = 2;
        } else if (soldierCount > 0 && ammoCount > 0) {
            currentStrength = Strength.LOW;
            strengthValue = 1;
        } else {
            currentStrength = Strength.CLOSED;
            strengthValue = 0;
        }

        strengthLabel.setText("Strength: " + currentStrength.getDisplayName());
        sendWarStrength(strengthValue);
    }

    @Override
    public void sendAreaMessage(String message) {
        observer.notifyAreaMessage(defenseType, message);
    }

    @Override
    public void updateMessageBox(String message) {
        SwingUtilities.invokeLater(() -> {
            String timestamp = new java.text.SimpleDateFormat("HH:mm:ss").format(new java.util.Date());
            messageBox.append("[" + timestamp + "] " + message + "\n");
            messageBox.setCaretPosition(messageBox.getDocument().getLength());
        });
    }

    @Override
    public void sendWarStrength(int strength) {
        // This will be handled by MainController through observer
        updateMessageBox("War strength updated: " + strength);
    }

    @Override
    public void sendMessage(String message) {
        observer.notifyGlobalMessage(defenseType + ": " + message);
        updateMessageBox("Sent: " + message);
    }

    @Override
    public String getDefenseType() {
        return defenseType;
    }

    public int getSoldierCount() {
        return soldierCount;
    }

    public int getAmmoCount() {
        return ammoCount;
    }

    public Strength getCurrentStrength() {
        return currentStrength;
    }

    public int getStrengthValue() {
        return currentStrength.getLevel();
    }
}