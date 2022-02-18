package application;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

@Repository
public class ElevatorRepository {
    private final List<ElevatorAgent> elevators = new ArrayList<>();

    private final Deque<RequestHail> hails = new ConcurrentLinkedDeque<>();
    private final Map<Integer, List<Integer>> elevatorIdToFloorDestinations = new ConcurrentHashMap<>();

    public void addElevator(ElevatorAgent elevatorAgent) {
        elevators.add(elevatorAgent);
        elevatorIdToFloorDestinations.put(elevatorAgent.id, new LinkedList<>());
    }

    public List<ElevatorAgent> getAllElevators() {
        return elevators;
    }

    public Deque<RequestHail> getHails() {
        return hails;
    }

    public void addHail(RequestHail hail) {
        hails.add(hail);
    }

    public void addDestinationForElevatorId(int elevatorId, int floorDestination) {
        elevatorIdToFloorDestinations.get(elevatorId).add(floorDestination);
    }

    public List<Integer> getFloorDestinationsForElevatorId(int elevatorId) {
        return elevatorIdToFloorDestinations.get(elevatorId);
    }

}
