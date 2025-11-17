import java.util.ArrayList;
import java.util.List;

public class Observer {
    private List<Observable> observables;
    private MainController mainController;

    public Observer() {
        this.observables = new ArrayList<>();
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void addObservable(Observable observable) {
        observables.add(observable);
    }

    public void removeObservable(Observable observable) {
        observables.remove(observable);
    }

    public List<Observable> getObservables() {
        return observables;
    }

    public void notifyStrengthChange(Strength strength) {
        if (mainController != null) {
            mainController.broadcastStrength(strength);
        }
    }

    public void notifyAreaMessage(String defenseType, String message) {
        if (mainController != null) {
            mainController.relayAreaMessage(defenseType, message);
        }
    }

    public void notifyGlobalMessage(String message) {
        if (mainController != null) {
            mainController.broadcastMessage(message);
        }
    }
}