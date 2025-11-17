import javax.swing.*;
import java.awt.*;
public class Helicopter extends SuperDefence {
    private int fuelLevel;
    private JLabel fuelLabel;
    private JSlider altitudeSlider;
    private JLabel altitudeLabel;

    public Helicopter(Observer observer) {
        super(observer, "HELICOPTER");
        this.fuelLevel = 100;
        initializeGUI();
    }

    private void initializeGUI() {
        initializeCommonGUI();
        setTitle("Helicopter Control Unit");
        setSize(500, 650);

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(70, 130, 180));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel titleLabel = new JLabel("HELICOPTER UNIT", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);

        // Status Panel with helicopter-specific stats
        JPanel statusPanel = new JPanel(new GridLayout(5, 1, 5, 5));
        statusPanel.setBorder(BorderFactory.createTitledBorder("Status"));

        soldierLabel = new JLabel("Pilots: " + soldierCount);
        ammoLabel = new JLabel("Ammunition: " + ammoCount);
        strengthLabel = new JLabel("Strength: " + currentStrength.getDisplayName());
        fuelLabel = new JLabel("Fuel: " + fuelLevel + "%");

        JPanel altitudePanel = new JPanel(new BorderLayout());
        altitudeLabel = new JLabel("Altitude: 0m", JLabel.CENTER);
        altitudeSlider = new JSlider(0, 5000, 0);
        altitudeSlider.setMajorTickSpacing(1000);
        altitudeSlider.setPaintTicks(true);
        altitudeSlider.setPaintLabels(true);
        altitudeSlider.addChangeListener(e -> {
            altitudeLabel.setText("Altitude: " + altitudeSlider.getValue() + "m");
        });
        altitudePanel.add(altitudeLabel, BorderLayout.NORTH);
        altitudePanel.add(altitudeSlider, BorderLayout.CENTER);

        statusPanel.add(soldierLabel);
        statusPanel.add(ammoLabel);
        statusPanel.add(fuelLabel);
        statusPanel.add(strengthLabel);

        // Control Panel - 4x2 grid
        JPanel controlPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        controlPanel.setBorder(BorderFactory.createTitledBorder("Operations"));

        JButton shootButton = new JButton("Shoot");
        shootButton.setBackground(new Color(220, 20, 60));
        shootButton.setForeground(Color.BLACK);
        shootButton.setFont(new Font("Arial", Font.BOLD, 12));
        shootButton.addActionListener(e -> performShoot());

        JButton missileButton = new JButton("Launch Missile");
        missileButton.setBackground(new Color(255, 69, 0));
        missileButton.setForeground(Color.BLACK);
        missileButton.setFont(new Font("Arial", Font.BOLD, 12));
        missileButton.addActionListener(e -> performMissile());

        JButton laserButton = new JButton("Laser Operation");
        laserButton.setBackground(new Color(0, 191, 255));
        laserButton.setForeground(Color.BLACK);
        laserButton.setFont(new Font("Arial", Font.BOLD, 12));
        laserButton.addActionListener(e -> performLaser());

        JButton rotateButton = new JButton("Rotate");
        rotateButton.setFont(new Font("Arial", Font.BOLD, 12));
        rotateButton.addActionListener(e -> performRotate());

        JButton refuelButton = new JButton("Refuel");
        refuelButton.setBackground(new Color(255, 215, 0));
        refuelButton.setForeground(Color.BLACK);
        refuelButton.setFont(new Font("Arial", Font.BOLD, 12));
        refuelButton.addActionListener(e -> performRefuel());

        JButton rearmButton = new JButton("Rearm");
        rearmButton.setBackground(new Color(139, 69, 19));
        rearmButton.setForeground(Color.BLACK);
        rearmButton.setFont(new Font("Arial", Font.BOLD, 12));
        rearmButton.addActionListener(e -> performRearm());

        JButton areaButton = new JButton("Send Area Message");
        areaButton.setFont(new Font("Arial", Font.BOLD, 12));
        areaButton.addActionListener(e -> sendAreaMsg());

        JButton broadcastButton = new JButton("Broadcast");
        broadcastButton.setFont(new Font("Arial", Font.BOLD, 12));
        broadcastButton.addActionListener(e -> broadcastMsg());

        controlPanel.add(shootButton);
        controlPanel.add(missileButton);
        controlPanel.add(laserButton);
        controlPanel.add(rotateButton);
        controlPanel.add(refuelButton);
        controlPanel.add(rearmButton);
        controlPanel.add(areaButton);
        controlPanel.add(broadcastButton);

        // Main layout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel topPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        topPanel.add(statusPanel);
        topPanel.add(altitudePanel);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(controlPanel, BorderLayout.CENTER);
        mainPanel.add(createMessagePanel(), BorderLayout.SOUTH);

        add(headerPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);

        setLocationRelativeTo(null);
        setVisible(true);

        updateMessageBox("Helicopter unit initialized and ready");
    }

    private void performShoot() {
        if (ammoCount >= 5 && fuelLevel >= 5) {
            ammoCount -= 5;
            fuelLevel -= 5;
            updateMessageBox("Shooting operation executed!");
            sendAreaMessage("Helicopter engaging target with gunfire");
            updateStatus();
            updateFuelDisplay();
        } else {
            updateMessageBox("Insufficient ammo or fuel for shooting!");
            JOptionPane.showMessageDialog(this,
                    "Need at least 5 ammo and 5% fuel",
                    "Low Resources", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void performMissile() {
        if (ammoCount >= 20 && fuelLevel >= 10) {
            ammoCount -= 20;
            fuelLevel -= 10;
            updateMessageBox("Missile launched successfully!");
            sendAreaMessage("Helicopter deployed air-to-ground missile");
            updateStatus();
            updateFuelDisplay();
        } else {
            updateMessageBox("Insufficient resources for missile launch!");
            JOptionPane.showMessageDialog(this,
                    "Need at least 20 ammo and 10% fuel",
                    "Low Resources", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void performLaser() {
        if (fuelLevel >= 15) {
            fuelLevel -= 15;
            updateMessageBox("Laser operation executed!");
            sendAreaMessage("Helicopter using laser targeting system");
            updateStatus();
            updateFuelDisplay();
        } else {
            updateMessageBox("Insufficient fuel for laser operation!");
            JOptionPane.showMessageDialog(this,
                    "Need at least 15% fuel",
                    "Low Fuel", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void performRotate() {
        if (fuelLevel >= 3) {
            fuelLevel -= 3;
            updateMessageBox("Helicopter rotated position");
            updateFuelDisplay();
        } else {
            updateMessageBox("Insufficient fuel to rotate!");
        }
    }

    /**
     * Refuel the helicopter - must be at low altitude
     */
    private void performRefuel() {
        int altitude = altitudeSlider.getValue();

        if (altitude > 100) {
            updateMessageBox("Cannot refuel at high altitude! Descend below 100m");
            JOptionPane.showMessageDialog(this,
                    "Descend below 100m to refuel\nCurrent altitude: " + altitude + "m",
                    "Altitude Too High",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (fuelLevel >= 90) {
            updateMessageBox("Fuel already near maximum capacity");
            JOptionPane.showMessageDialog(this,
                    "Fuel level is already at " + fuelLevel + "%",
                    "Refuel Not Needed",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int refuelAmount = Math.min(30, 100 - fuelLevel);
        fuelLevel += refuelAmount;
        updateMessageBox("Refueling... +" + refuelAmount + "% fuel added");
        sendAreaMessage("Helicopter refueling at base");
        updateFuelDisplay();
        updateStatus();

        JOptionPane.showMessageDialog(this,
                "Refueling complete!\nFuel level: " + fuelLevel + "%",
                "Refuel Successful",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Rearm the helicopter with ammunition
     */
    private void performRearm() {
        int altitude = altitudeSlider.getValue();

        if (altitude > 100) {
            updateMessageBox("Cannot rearm at high altitude! Descend below 100m");
            JOptionPane.showMessageDialog(this,
                    "Descend below 100m to rearm\nCurrent altitude: " + altitude + "m",
                    "Altitude Too High",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (ammoCount >= 90) {
            updateMessageBox("Ammunition already near maximum capacity");
            JOptionPane.showMessageDialog(this,
                    "Ammo count is already at " + ammoCount,
                    "Rearm Not Needed",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int rearmAmount = Math.min(40, 100 - ammoCount);
        ammoCount += rearmAmount;
        updateMessageBox("Rearming... +" + rearmAmount + " ammunition added");
        sendAreaMessage("Helicopter restocking ammunition");
        updateStatus();

        JOptionPane.showMessageDialog(this,
                "Rearming complete!\nAmmunition: " + ammoCount,
                "Rearm Successful",
                JOptionPane.INFORMATION_MESSAGE);
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

    private void updateFuelDisplay() {
        fuelLabel.setText("Fuel: " + fuelLevel + "%");
        if (fuelLevel < 20) {
            fuelLabel.setForeground(Color.RED);
        } else if (fuelLevel < 40) {
            fuelLabel.setForeground(Color.ORANGE);
        } else {
            fuelLabel.setForeground(Color.BLACK);
        }
    }

    @Override
    protected void updateStatus() {
        soldierLabel.setText("Pilots: " + soldierCount);
        super.updateStatus();
        updateFuelDisplay();
    }
}