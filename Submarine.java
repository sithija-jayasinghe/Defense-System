import javax.swing.*;
import java.awt.*;

public class Submarine extends SuperDefence {
    private int oxygenLevel;
    private int energyLevel;
    private int depth;
    private JLabel oxygenLabel;
    private JLabel energyLabel;
    private JLabel depthLabel;
    private JProgressBar oxygenBar;
    private JProgressBar energyBar;

    public Submarine(Observer observer) {
        super(observer, "SUBMARINE");
        this.oxygenLevel = 100;
        this.energyLevel = 100;
        this.depth = 0;
        initializeGUI();
    }

    public Submarine(String s, MainController controller) {
    }


    private void initializeGUI() {
        initializeCommonGUI();
        setTitle("Submarine Control Unit");
        setSize(550, 700);

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(0, 105, 148));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel titleLabel = new JLabel("SUBMARINE UNIT", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);

        // Status Panel with submarine-specific stats
        JPanel statusPanel = new JPanel(new GridLayout(5, 1, 5, 5));
        statusPanel.setBorder(BorderFactory.createTitledBorder("Status"));

        soldierLabel = new JLabel("Crew: " + soldierCount);
        ammoLabel = new JLabel("Torpedoes: " + ammoCount);
        strengthLabel = new JLabel("Strength: " + currentStrength.getDisplayName());
        oxygenLabel = new JLabel("Oxygen: " + oxygenLevel + "%");
        energyLabel = new JLabel("Energy: " + energyLevel + "%");

        statusPanel.add(soldierLabel);
        statusPanel.add(ammoLabel);
        statusPanel.add(oxygenLabel);
        statusPanel.add(energyLabel);
        statusPanel.add(strengthLabel);

        // Resource Bars Panel
        JPanel resourcePanel = new JPanel(new GridLayout(2, 1, 5, 5));
        resourcePanel.setBorder(BorderFactory.createTitledBorder("Resources"));

        JPanel oxygenPanel = new JPanel(new BorderLayout());
        oxygenPanel.add(new JLabel("Oxygen:"), BorderLayout.WEST);
        oxygenBar = new JProgressBar(0, 100);
        oxygenBar.setValue(oxygenLevel);
        oxygenBar.setStringPainted(true);
        oxygenBar.setForeground(new Color(135, 206, 250));
        oxygenPanel.add(oxygenBar, BorderLayout.CENTER);

        JPanel energyPanel = new JPanel(new BorderLayout());
        energyPanel.add(new JLabel("Energy:"), BorderLayout.WEST);
        energyBar = new JProgressBar(0, 100);
        energyBar.setValue(energyLevel);
        energyBar.setStringPainted(true);
        energyBar.setForeground(new Color(255, 215, 0));
        energyPanel.add(energyBar, BorderLayout.CENTER);

        resourcePanel.add(oxygenPanel);
        resourcePanel.add(energyPanel);

        // Depth Control
        JPanel depthPanel = new JPanel(new BorderLayout());
        depthPanel.setBorder(BorderFactory.createTitledBorder("Depth Control"));
        depthLabel = new JLabel("Depth: " + depth + "m", JLabel.CENTER);
        JSlider depthSlider = new JSlider(JSlider.VERTICAL, 0, 500, 0);
        depthSlider.setMajorTickSpacing(100);
        depthSlider.setMinorTickSpacing(50);
        depthSlider.setPaintTicks(true);
        depthSlider.setPaintLabels(true);
        depthSlider.setInverted(true); // Deeper = higher value
        depthSlider.addChangeListener(e -> {
            depth = depthSlider.getValue();
            depthLabel.setText("Depth: " + depth + "m");
            if (depth > 0) {
                consumeResources(0, 1); // Diving consumes energy
            }
        });
        depthPanel.add(depthLabel, BorderLayout.NORTH);
        depthPanel.add(depthSlider, BorderLayout.CENTER);

        // Control Panel
        JPanel controlPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        controlPanel.setBorder(BorderFactory.createTitledBorder("Operations"));

        JButton shootButton = new JButton("Fire Torpedo");
        shootButton.setBackground(new Color(0, 100, 200));
        shootButton.setForeground(Color.BLACK);
        shootButton.addActionListener(e -> performShoot());

        JButton missileButton = new JButton("Launch Missile");
        missileButton.setBackground(new Color(255, 69, 0));
        missileButton.setForeground(Color.BLACK);
        missileButton.addActionListener(e -> performMissile());

        JButton sonarButton = new JButton("Activate Sonar");
        sonarButton.setBackground(new Color(32, 178, 170));
        sonarButton.setForeground(Color.BLACK);
        sonarButton.addActionListener(e -> performSonar());

        JButton surfaceButton = new JButton("Surface");
        surfaceButton.addActionListener(e -> performSurface());

        JButton rechargeButton = new JButton("Recharge");
        rechargeButton.addActionListener(e -> performRecharge());

        JButton oxygenButton = new JButton("Refill Oxygen");
        oxygenButton.addActionListener(e -> performOxygenRefill());

        JButton areaButton = new JButton("Send Area Message");
        areaButton.addActionListener(e -> sendAreaMsg());

        JButton broadcastButton = new JButton("Broadcast");
        broadcastButton.addActionListener(e -> broadcastMsg());

        controlPanel.add(shootButton);
        controlPanel.add(missileButton);
        controlPanel.add(sonarButton);
        controlPanel.add(surfaceButton);
        controlPanel.add(rechargeButton);
        controlPanel.add(oxygenButton);
        controlPanel.add(areaButton);
        controlPanel.add(broadcastButton);

        // Main layout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel topPanel = new JPanel(new BorderLayout(5, 5));
        JPanel leftPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        leftPanel.add(statusPanel);
        leftPanel.add(resourcePanel);
        topPanel.add(leftPanel, BorderLayout.CENTER);
        topPanel.add(depthPanel, BorderLayout.EAST);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(controlPanel, BorderLayout.CENTER);
        mainPanel.add(createMessagePanel(), BorderLayout.SOUTH);

        add(headerPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);

        setLocationRelativeTo(null);
        setVisible(true);

        updateMessageBox("Submarine unit initialized and ready");

        Timer resourceTimer = new Timer(5000, e -> consumePassiveResources());
        resourceTimer.start();
    }

    private void performShoot() {
        if (ammoCount >= 5 && energyLevel >= 10 && depth > 0) {
            ammoCount -= 5;
            energyLevel -= 10;
            updateMessageBox("Torpedo fired at depth " + depth + "m!");
            sendAreaMessage("Submarine engaging underwater target with torpedo");
            updateStatus();
        } else if (depth == 0) {
            updateMessageBox("Must be submerged to fire torpedo!");
            JOptionPane.showMessageDialog(this,
                    "Submarine must be underwater",
                    "Invalid Operation", JOptionPane.WARNING_MESSAGE);
        } else {
            updateMessageBox("Insufficient resources!");
            JOptionPane.showMessageDialog(this,
                    "Need at least 5 torpedoes and 10% energy",
                    "Low Resources", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void performMissile() {
        if (ammoCount >= 15 && energyLevel >= 20 && depth < 50) {
            ammoCount -= 15;
            energyLevel -= 20;
            updateMessageBox("Ballistic missile launched!");
            sendAreaMessage("Submarine deployed ballistic missile");
            updateStatus();
        } else if (depth >= 50) {
            updateMessageBox("Too deep to launch missile!");
            JOptionPane.showMessageDialog(this,
                    "Must be at depth < 50m to launch missiles",
                    "Invalid Depth", JOptionPane.WARNING_MESSAGE);
        } else {
            updateMessageBox("Insufficient resources!");
            JOptionPane.showMessageDialog(this,
                    "Need at least 15 torpedoes and 20% energy",
                    "Low Resources", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void performSonar() {
        if (energyLevel >= 15 && oxygenLevel >= 5) {
            energyLevel -= 15;
            oxygenLevel -= 5;
            updateMessageBox("Sonar ping activated - scanning area");
            sendAreaMessage("Submarine using active sonar");
            updateStatus();

            JOptionPane.showMessageDialog(this,
                    "Sonar Results:\n" +
                            "- 2 vessels detected at bearing 045°\n" +
                            "- 1 possible contact at bearing 180°\n" +
                            "- Ocean floor at " + (depth + 100) + "m",
                    "Sonar Results", JOptionPane.INFORMATION_MESSAGE);
        } else {
            updateMessageBox("Insufficient resources for sonar!");
            JOptionPane.showMessageDialog(this,
                    "Need at least 15% energy and 5% oxygen",
                    "Low Resources", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void performSurface() {
        if (depth > 0) {
            depth = 0;
            updateMessageBox("Submarine surfacing...");
            sendAreaMessage("Submarine surfacing to sea level");
        } else {
            updateMessageBox("Already at surface");
        }
    }

    private void performRecharge() {
        if (depth == 0 && energyLevel < 90) {
            energyLevel = Math.min(100, energyLevel + 20);
            updateMessageBox("Energy recharged at surface");
            updateStatus();
        } else if (depth > 0) {
            updateMessageBox("Must surface to recharge!");
        } else {
            updateMessageBox("Energy already at optimal level");
        }
    }

    private void performOxygenRefill() {
        if (depth == 0 && oxygenLevel < 90) {
            oxygenLevel = Math.min(100, oxygenLevel + 30);
            updateMessageBox("Oxygen tanks refilled");
            updateStatus();
        } else if (depth > 0) {
            updateMessageBox("Must surface to refill oxygen!");
        } else {
            updateMessageBox("Oxygen already at optimal level");
        }
    }

    private void consumePassiveResources() {
        if (depth > 0) {
            oxygenLevel = Math.max(0, oxygenLevel - 2);
            energyLevel = Math.max(0, energyLevel - 1);
            updateStatus();

            if (oxygenLevel < 20 || energyLevel < 20) {
                updateMessageBox("WARNING: Critical resource levels!");
            }
        }
    }

    private void consumeResources(int oxygen, int energy) {
        oxygenLevel = Math.max(0, oxygenLevel - oxygen);
        energyLevel = Math.max(0, energyLevel - energy);
        updateStatus();
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

    private void updateResourceDisplays() {
        oxygenLabel.setText("Oxygen: " + oxygenLevel + "%");
        energyLabel.setText("Energy: " + energyLevel + "%");

        oxygenBar.setValue(oxygenLevel);
        energyBar.setValue(energyLevel);

        if (oxygenLevel < 30) {
            oxygenBar.setForeground(Color.RED);
            oxygenLabel.setForeground(Color.RED);
        } else {
            oxygenBar.setForeground(new Color(135, 206, 250));
            oxygenLabel.setForeground(Color.BLACK);
        }

        if (energyLevel < 30) {
            energyBar.setForeground(Color.RED);
            energyLabel.setForeground(Color.RED);
        } else {
            energyBar.setForeground(new Color(255, 215, 0));
            energyLabel.setForeground(Color.BLACK);
        }
    }

    @Override
    protected void updateStatus() {
        soldierLabel.setText("Crew: " + soldierCount);
        ammoLabel.setText("Torpedoes: " + ammoCount);
        super.updateStatus();
        updateResourceDisplays();
    }
}