public class Strength {
    public static final Strength LOW = new Strength("Low", 1);
    public static final Strength MEDIUM = new Strength("Medium", 2);
    public static final Strength HIGH = new Strength("High", 3);
    public static final Strength STRONG = new Strength("Strong", 4);
    public static final Strength CLOSED = new Strength("Closed", 0);

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

    @Override
    public String toString() {
        return displayName;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Strength strength = (Strength) obj;
        return level == strength.level;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(level);
    }

    public static Strength fromLevel(int level) {
        switch (level) {
            case 0: return CLOSED;
            case 1: return LOW;
            case 2: return MEDIUM;
            case 3: return HIGH;
            case 4: return STRONG;
            default: return CLOSED;
        }
    }
}