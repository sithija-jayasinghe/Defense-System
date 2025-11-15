import javax.swing.*;
import java.awt.*;


public abstract class SuperDefense extends JFrame implements Observable {
    protected Observer observer;
    protected int soldierCount;
    protected int ammoCount;
    protected Strength currentStrength;
    protected JTextArea messageBox;
    protected JLabel soldierLabel;
    protected JLabel ammoLabel;
    protected JLabel strengthLabel;
    protected String defenseType;

    public SuperDefense(Observer observer, String defenseType) {
        this.observer = observer;
        this.defenseType = defenseType;
        this.soldierCount = 10;
        this.ammoCount = 100;
        this.currentStrength = Strength.MEDIUM;
    }

    protected void commonGUI() {
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

    protected void calculateStrength() {
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
        observer.update(defenseType, strengthValue);
    }
}