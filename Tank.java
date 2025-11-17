import javax.swing.*;
import java.awt.*;

public class Tank extends SuperDefence {
    private int armorLevel;
    private JLabel armorLabel;
    private JProgressBar armorBar;
    private int rotationAngle;

    public Tank(Observer observer) {
        super(observer, "TANK");
        this.armorLevel = 100;
        this.rotationAngle = 0;
        initializeGUI();
    }

    public Tank(String s, MainController controller) {
        super();
    }

    private void initializeGUI() {
        initializeCommonGUI();
        setTitle("Tank Control Unit");
        setSize(500, 600);

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(107, 142, 35));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel titleLabel = new JLabel("TANK UNIT", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);

        // Status Panel with tank-specific stats
        JPanel statusPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        statusPanel.setBorder(BorderFactory.createTitledBorder("Status"));

        soldierLabel = new JLabel("Crew: " + soldierCount);
        ammoLabel = new JLabel("Ammunition: " + ammoCount);
        strengthLabel = new JLabel("Strength: " + currentStrength.getDisplayName());
        armorLabel = new JLabel("Armor Integrity: " + armorLevel + "%");

        statusPanel.add(soldierLabel);
        statusPanel.add(ammoLabel);
        statusPanel.add(armorLabel);
        statusPanel.add(strengthLabel);

        // Armor Bar
        JPanel armorPanel = new JPanel(new BorderLayout());
        armorPanel.setBorder(BorderFactory.createTitledBorder("Armor Status"));
        armorBar = new JProgressBar(0, 100);
        armorBar.setValue(armorLevel);
        armorBar.setStringPainted(true);
        armorBar.setForeground(new Color(169, 169, 169));
        armorPanel.add(armorBar, BorderLayout.CENTER);

        // Turret Control
        JPanel turretPanel = new JPanel(new BorderLayout());
        turretPanel.setBorder(BorderFactory.createTitledBorder("Turret Control"));
        JLabel angleLabel = new JLabel("Angle: " + rotationAngle + "°", JLabel.CENTER);
        JSlider angleSlider = new JSlider(0, 360, 0);
        angleSlider.setMajorTickSpacing(90);
        angleSlider.setMinorTickSpacing(45);
        angleSlider.setPaintTicks(true);
        angleSlider.setPaintLabels(true);
        angleSlider.addChangeListener(e -> {
            rotationAngle = angleSlider.getValue();
            angleLabel.setText("Angle: " + rotationAngle + "°");
        });
        turretPanel.add(angleLabel, BorderLayout.NORTH);
        turretPanel.add(angleSlider, BorderLayout.CENTER);

        // Control Panel
        JPanel controlPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        controlPanel.setBorder(BorderFactory.createTitledBorder("Operations"));

        JButton shootButton = new JButton("Fire Main Gun");
        shootButton.setBackground(new Color(178, 34, 34));
        shootButton.setForeground(Color.BLACK);
        shootButton.addActionListener(e -> performShoot());

        JButton heavyShootButton = new JButton("Heavy Fire");
        heavyShootButton.setBackground(new Color(139, 0, 0));
        heavyShootButton.setForeground(Color.BLACK);
        heavyShootButton.addActionListener(e -> performHeavyShoot());

        JButton rotateButton = new JButton("Rotate Position");
        rotateButton.addActionListener(e -> performRotate());

        JButton defenseButton = new JButton("Defensive Position");
        defenseButton.addActionListener(e -> performDefense());

        JButton areaButton = new JButton("Send Area Message");
        areaButton.addActionListener(e -> sendAreaMsg());

        JButton broadcastButton = new JButton("Broadcast");
        broadcastButton.addActionListener(e -> broadcastMsg());

        controlPanel.add(shootButton);
        controlPanel.add(heavyShootButton);
        controlPanel.add(rotateButton);
        controlPanel.add(defenseButton);
        controlPanel.add(areaButton);
        controlPanel.add(broadcastButton);

        // Main layout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel topPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        topPanel.add(statusPanel);
        topPanel.add(armorPanel);
        topPanel.add(turretPanel);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(controlPanel, BorderLayout.CENTER);
        mainPanel.add(createMessagePanel(), BorderLayout.SOUTH);

        add(headerPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);

        setLocationRelativeTo(null);
        setVisible(true);

        updateMessageBox("Tank unit initialized and ready");
    }

    private void performShoot() {
        if (ammoCount >= 10) {
            ammoCount -= 10;
            armorLevel = Math.max(0, armorLevel - 2); // Recoil damages armor slightly
            updateMessageBox("Main gun fired at angle " + rotationAngle + "°");
            sendAreaMessage("Tank firing main gun at bearing " + rotationAngle);
            updateStatus();
        } else {
            updateMessageBox("Insufficient ammunition!");
            JOptionPane.showMessageDialog(this,
                    "Need at least 10 rounds",
                    "Low Ammo", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void performHeavyShoot() {
        if (ammoCount >= 25 && armorLevel >= 20) {
            ammoCount -= 25;
            armorLevel = Math.max(0, armorLevel - 5);
            updateMessageBox("Heavy barrage executed!");
            sendAreaMessage("Tank executing heavy fire barrage");
            updateStatus();
        } else {
            updateMessageBox("Insufficient resources for heavy fire!");
            JOptionPane.showMessageDialog(this,
                    "Need at least 25 ammo and 20% armor",
                    "Low Resources", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void performRotate() {
        updateMessageBox("Tank repositioned");
        sendAreaMessage("Tank changing position");
    }

    private void performDefense() {
        if (armorLevel < 90) {
            armorLevel = Math.min(100, armorLevel + 10);
            updateMessageBox("Defensive position assumed - armor optimized");
            sendAreaMessage("Tank in defensive stance");
            updateStatus();
        } else {
            updateMessageBox("Armor already at optimal level");
        }
    }

    private void sendAreaMsg() {
        String message = JOptionPane.showInputDialog(this,
                "Enter area message:",
                "Area Message",
                JOptionPane.PLAIN_MESSAGE);

        if (message != null && !message.trim().isEmpty()) {
            sendAreaMessage(message);
            updateMessageBox("Area message sent: " + message);
        }
    }

    private void broadcastMsg() {
        String message = JOptionPane.showInputDialog(this,
                "Enter broadcast message:",
                "Broadcast Message",
                JOptionPane.PLAIN_MESSAGE);

        if (message != null && !message.trim().isEmpty()) {
            sendMessage(message);
        }
    }

    private void updateArmorDisplay() {
        armorLabel.setText("Armor Integrity: " + armorLevel + "%");
        armorBar.setValue(armorLevel);

        if (armorLevel < 30) {
            armorBar.setForeground(Color.RED);
            armorLabel.setForeground(Color.RED);
        } else if (armorLevel < 60) {
            armorBar.setForeground(Color.ORANGE);
            armorLabel.setForeground(Color.ORANGE);
        } else {
            armorBar.setForeground(new Color(169, 169, 169));
            armorLabel.setForeground(Color.BLACK);
        }
    }

    @Override
    protected void updateStatus() {
        soldierLabel.setText("Crew: " + soldierCount);
        super.updateStatus();
        updateArmorDisplay();
    }
}