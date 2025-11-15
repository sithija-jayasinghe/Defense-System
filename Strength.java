public class Strength {
    public static final Strength STRONG = new Strength("Strong", 5);
    public static final Strength HIGH = new Strength("High", 4);
    public static final Strength MEDIUM = new Strength("Medium", 3);
    public static final Strength LOW = new Strength("Low", 2);
    public static final Strength CLOSED = new Strength("Closed", 1);

    private final String displayName;
    private final int level;

    private Strength(String displayName, int level) {
        this.displayName = displayName;
        this.level = level;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getLevel() {
        return level;
    }
}