package application

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.Collections
import java.util.PriorityQueue
import java.util.Queue

class ElevatorWorker(val id: Int, private val elevatorService: ElevatorService) : Runnable {
    private val SLEEP_IN_MILLIS = 3000L // 1/sleep == actions per second
    private val directionToFloorQueue = mapOf<Direction, Queue<Int>>(
            Direction.UP to PriorityQueue(),  // min heap going up
            Direction.DOWN to PriorityQueue(Collections.reverseOrder()),  // max heap going down,
            Direction.IDLE to PriorityQueue()
    )
    private var elevatorIsOn = true
    private var direction = Direction.IDLE
    private var yCoordinate = 0.0
    private var doorState = DoorState.OPEN

    companion object {
        private val LOG: Logger = LoggerFactory.getLogger(ElevatorWorker::class.java)
    }

    private fun getNextFloor() :Int {
        val nextFloors = directionToFloorQueue[direction]!!
        if (!nextFloors.isEmpty()) {
            return nextFloors.peek()
        }
        val nextFloor: Int
        if (!directionToFloorQueue[Direction.UP]!!.isEmpty()) {
            direction = Direction.UP
            nextFloor = directionToFloorQueue[direction]!!.peek()
        } else if (!directionToFloorQueue[Direction.DOWN]!!.isEmpty()) {
            direction = Direction.DOWN
            nextFloor = directionToFloorQueue[direction]!!.peek()
        } else {
            direction = Direction.IDLE
            nextFloor = yCoordinate.toInt()
        }
        return nextFloor
    }

    private fun getDirectionOfFloor(nextFloorNumber: Double): Direction {
        if (yCoordinate == nextFloorNumber) {
            return Direction.IDLE
        }
        return if (yCoordinate < nextFloorNumber) Direction.UP else Direction.DOWN
    }

    private fun queueUpNextFloorDestinationsFromButtons() {
        elevatorService.getAndClearNextFloorDestinationForElevatorId(id).forEach { destinationFloorNumber: Int ->
            val requestFloorDirection = getDirectionOfFloor(destinationFloorNumber.toDouble())
            val pQueue = directionToFloorQueue[requestFloorDirection]!!
            pQueue.add(destinationFloorNumber)
        }
    }

    private fun queueUpNextFloorDestinationsFromHails() {
        val nextHails = elevatorService.getNextHails(yCoordinate, direction)
        nextHails.forEach { hail: RequestHail ->
            val pickupAtFloor = hail.fromFloorNumber
            val directionOfHail = getDirectionOfFloor(pickupAtFloor.toDouble())
            val pQueue = directionToFloorQueue[directionOfHail]!!
            pQueue.add(pickupAtFloor)
        }
    }

    private fun clearIdleFloors() {
//        may blindly add to 'this' floor when idling
        directionToFloorQueue[Direction.IDLE]!!.clear()
    }

    override fun run() {
        while (elevatorIsOn) {
            try {
                LOG.info("I'm running {}, at y:{} i'm going:{}, and going to floors {}",
                        id, yCoordinate, direction, directionToFloorQueue[direction])

                queueUpNextFloorDestinationsFromHails()

                LOG.info("handled hails {}, at y:{} i'm going:{}, and going to floors {}",
                        id, yCoordinate, direction, directionToFloorQueue[direction])

                queueUpNextFloorDestinationsFromButtons()

                LOG.info("handled buttons {}, at y:{} i'm going:{}, and going to floors {}",
                        id, yCoordinate, direction, directionToFloorQueue[direction])

                clearIdleFloors()

                val isElevatorAtNextFloor = yCoordinate.toInt() == getNextFloor()
                if (isElevatorAtNextFloor) {
                    doorState = DoorState.OPEN
                    yCoordinate = getNextFloor().toDouble()
                    val nextFloorQueue = directionToFloorQueue[direction]!!
                    val servedFloor = nextFloorQueue.poll()
                    while (servedFloor != null && servedFloor == nextFloorQueue.peek()) {
                        nextFloorQueue.poll()
                    }
                    if (nextFloorQueue.isEmpty()) {
                        direction = Direction.IDLE
                    }
                } else {
                    doorState = DoorState.CLOSED
                    yCoordinate += direction.intDirection + (1.0 / SLEEP_IN_MILLIS)
                }
                LOG.info("finished running {}, y:{} i'm going:{} to floors {}",
                        id, yCoordinate, direction, directionToFloorQueue[direction])
                Thread.sleep(SLEEP_IN_MILLIS)
            } catch (e: Exception) {
                LOG.error("Error for Elevator Agent", e)
            }
        }
    }
}