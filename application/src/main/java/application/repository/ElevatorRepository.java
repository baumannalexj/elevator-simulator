package application.repository;

import application.model.Elevator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ElevatorRepository {
    private final List<Elevator> elevators;

    @Autowired
    public ElevatorRepository(@Value("${elevator.count:4}") int elevatorCount) {
        this.elevators = new ArrayList<>(elevatorCount);

        for (int id = 1; id <= elevatorCount; id++) {
            Elevator elevator = new Elevator();
            elevator.id = id;

            this.elevators.add(elevator);
        }
    }

    public List<Elevator> getAllElevators() {
        return elevators;
    }
}
