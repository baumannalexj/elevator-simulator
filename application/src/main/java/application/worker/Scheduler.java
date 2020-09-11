package application.worker;

import application.repository.ElevatorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope()
public class Scheduler {

    private final ElevatorRepository elevatorRepository;

    public Scheduler(ElevatorRepository elevatorRepository) {
        this.elevatorRepository = elevatorRepository;
    }


}
