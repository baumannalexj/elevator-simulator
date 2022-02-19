package application;

import org.springframework.lang.NonNull;

public class RequestHail {

    @NonNull
    private Direction direction;
    @NonNull
    private int fromFloorNumber;

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public int getFromFloorNumber() {
        return fromFloorNumber;
    }

    public void setFromFloorNumber(int fromFloorNumber) {
        this.fromFloorNumber = fromFloorNumber;
    }
}
