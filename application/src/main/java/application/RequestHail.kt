package application

import org.springframework.lang.NonNull

class RequestHail {
    @NonNull
    var direction: Direction? = Direction.IDLE

    @NonNull
    var fromFloorNumber = 0
}