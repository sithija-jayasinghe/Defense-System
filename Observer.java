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

    public void update(String defenseType, int strengthValue) {
        if (mainController != null) {
            mainController.receiveUpdate(defenseType, strengthValue);
        }
    }
}