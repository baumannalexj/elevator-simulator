package application

import org.springframework.stereotype.Repository
import java.util.Deque
import java.util.LinkedList
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

@Repository
class ElevatorRepository {
    private val elevators: MutableList<ElevatorWorker> = ArrayList()

    val hails: Deque<RequestHail> = LinkedList()

    private val elevatorIdToFloorDestinations: ConcurrentMap<Int, MutableList<Int>> = ConcurrentHashMap()

    fun addElevator(elevatorWorker: ElevatorWorker) {
        elevators.add(elevatorWorker)
        elevatorIdToFloorDestinations[elevatorWorker.id] = LinkedList()
    }

    fun allElevators(): List<ElevatorWorker> {

        return elevators;
    }

    fun addHail(hail: RequestHail) {
        hails.add(hail)
    }

    fun addDestinationForElevatorId(elevatorId: Int, floorDestination: Int) {
        elevatorIdToFloorDestinations[elevatorId]?.add(floorDestination)
    }

    fun getFloorDestinationsForElevatorId(elevatorId: Int): MutableList<Int> {
        return elevatorIdToFloorDestinations[elevatorId]!!
    }
}