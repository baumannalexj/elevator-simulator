package application;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentMap;

@Repository
public class ElevatorRepository {
    private final List<ElevatorWorker> elevators = new ArrayList<>();

    private final Deque<RequestHail> hails = new LinkedList<>();
    private final ConcurrentMap<Integer, List<Integer>> elevatorIdToFloorDestinations = new ConcurrentHashMap<>();


    public void addElevator(ElevatorWorker elevatorWorker) {
        elevators.add(elevatorWorker);
        elevatorIdToFloorDestinations.put(elevatorWorker.id, new LinkedList<>());
    }

    public List<ElevatorWorker> getAllElevators() {
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
