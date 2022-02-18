package application;

public enum DoorState {
    OPEN(1, "OPEN"),
    CLOSED(0, "CLOSED");

    private final int intVal;
    private final String strVal;


    DoorState(int intVal, String strVal) {
        this.intVal = intVal;
        this.strVal = strVal;
    }


    public int getIntVal() {
        return intVal;
    }

    public String getStrVal() {
        return strVal;
    }

}
