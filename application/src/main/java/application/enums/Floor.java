package application.enums;

public enum Floor {
    LOBBY(1),
    ONE(1),
    TWO(2),
    ;

    public final int floorNumber;

    Floor(int floorNumber) {
        this.floorNumber = floorNumber;
    }
}
