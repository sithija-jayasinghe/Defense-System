import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {
        // Set look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Run on Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            // Initialize the Observer
            Observer observer = new Observer();

            // Initialize MainController
            MainController mainController = new MainController(observer);
            observer.setMainController(mainController);

            // Create defense units
            Helicopter helicopter = new Helicopter(observer);
            Tank tank = new Tank(observer);
            Submarine submarine = new Submarine(observer);

            // Register defense units with observer
            observer.addObservable(helicopter);
            observer.addObservable(tank);
            observer.addObservable(submarine);

            // Position windows
            positionWindows(mainController, helicopter, tank, submarine);

            // Send initial strength signal
            sendInitialStrengthSignal(observer, mainController);

            // Display welcome message
            JOptionPane.showMessageDialog(null,
                    "Defense System Initialized\n\n" +
                            "Active Units:\n" +
                            "- Helicopter (Aerial Defense)\n" +
                            "- Tank (Ground Defense)\n" +
                            "- Submarine (Naval Defense)\n\n" +
                            "All systems operational!",
                    "System Ready",
                    JOptionPane.INFORMATION_MESSAGE);
        });
    }

    private static void positionWindows(MainController mainController,
                                        Helicopter helicopter,
                                        Tank tank,
                                        Submarine submarine) {
        // Get screen dimensions
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;

        // Position MainController at top center
        int mcX = (screenWidth - mainController.getWidth()) / 2;
        mainController.setLocation(mcX, 20);

        // Position defense units below in a row
        int unitY = mainController.getY() + mainController.getHeight() + 20;
        int spacing = 20;
        int totalWidth = helicopter.getWidth() + tank.getWidth() + submarine.getWidth() + (spacing * 2);
        int startX = (screenWidth - totalWidth) / 2;

        helicopter.setLocation(startX, unitY);
        tank.setLocation(startX + helicopter.getWidth() + spacing, unitY);
        submarine.setLocation(startX + helicopter.getWidth() + tank.getWidth() + (spacing * 2), unitY);
    }

    private static void sendInitialStrengthSignal(Observer observer, MainController mainController) {
        // Simulate initial system check
        Timer timer = new Timer(1000, e -> {
            observer.notifyStrengthChange(Strength.MEDIUM);

            // Calculate and update total strength
            int totalStrength = 0;
            for (Observable obs : observer.getObservables()) {
                if (obs instanceof SuperDefence) {
                    totalStrength += ((SuperDefence) obs).getStrengthValue();
                }
            }
            mainController.updateOverallStrength(totalStrength);
        });
        timer.setRepeats(false);
        timer.start();

        Timer monitorTimer = new Timer(10000, e -> {
            int totalStrength = 0;
            for (Observable obs : observer.getObservables()) {
                if (obs instanceof SuperDefence) {
                    totalStrength += ((SuperDefence) obs).getStrengthValue();
                }
            }
            mainController.updateOverallStrength(totalStrength);
        });
        monitorTimer.start();
    }
}
