package application;

public enum Direction {
    UP(1, "UP"),
    DOWN(-1, "DOWN"),
    IDLE(0, "IDLE");


    private final int intDirection;
    private final String stringDirection;

    Direction(int intDirection, String stringDirection) {
        this.intDirection = intDirection;
        this.stringDirection = stringDirection;
    }

    public int getIntDirection() {
        return intDirection;
    }

    public String getStringDirection() {
        return stringDirection;
    }
}
