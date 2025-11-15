

public interface Observable {
    void sendAreaMessage(String message);
    void updateMessageBox(String message);
    void sendWarStrength(int strength);
    void sendMessage(String message);
    String getDefenseType();
}