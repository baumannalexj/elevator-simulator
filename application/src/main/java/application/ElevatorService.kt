package application

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*
import java.util.stream.Collectors

@Service
class ElevatorService @Autowired constructor(private val repository: ElevatorRepository) {
    companion object {
        private val LOG: Logger = LoggerFactory.getLogger(ElevatorService::class.java)
    }

    @Synchronized
    fun getNextHails(yCoordinate: Double,
                     elevatorDirection: Direction): List<RequestHail> {
        val hails = repository.hails
        if (hails.isEmpty()) {
            return emptyList()
        }

        val directionToUse: Direction = if (elevatorDirection === Direction.IDLE) {
            hails.peek().direction!! // pick a direction to go
        } else {
            elevatorDirection
        }

        var filteredHailsStream = hails.stream()
                .filter { hail: RequestHail -> directionToUse === hail.direction }
        if (elevatorDirection !== Direction.IDLE) {
            filteredHailsStream = filteredHailsStream.filter { hail: RequestHail ->
                val hailFloorNumber = hail.fromFloorNumber.toDouble()
                if (directionToUse === Direction.UP) yCoordinate <= hailFloorNumber else yCoordinate >= hailFloorNumber
            }
        }
        val requestHails = filteredHailsStream.collect(Collectors.toUnmodifiableList())
        hails.removeAll(requestHails.toSet())
        return requestHails
    }

    fun getAndClearNextFloorDestinationForElevatorId(elevatorId: Int): List<Int> {
        val nextDestinations = repository.getFloorDestinationsForElevatorId(elevatorId)
        if (nextDestinations.isEmpty()) {
            return emptyList()
        }
        val listForElevator: List<Int> = ArrayList(nextDestinations) // shallow copy but with primitives
        nextDestinations.clear() //TODO change to atomic compare and set
        return listForElevator
    }


}