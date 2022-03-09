package application

enum class Direction(val intDirection: Int, val stringDirection: String) {
    UP(1, "UP"),
    DOWN(-1, "DOWN"),
    IDLE(0, "IDLE");

}