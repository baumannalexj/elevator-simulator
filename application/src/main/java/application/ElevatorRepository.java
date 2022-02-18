package application;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

@Repository
public class ElevatorRepository {
    private final List<ElevatorAgent> elevators = new ArrayList<>();
    private final Deque<RequestHail> hails = new ConcurrentLinkedDeque<>();

    public void addElevator(ElevatorAgent elevatorAgent) {
        this.elevators.add(elevatorAgent);
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

}
