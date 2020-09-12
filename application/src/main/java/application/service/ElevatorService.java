package application.service;

import application.enums.Direction;
import application.model.Elevator;
import application.repository.ElevatorRepository;
import application.repository.PendingRequests;
import application.request.CommandRequest;
import application.request.HailRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ElevatorService {
    private final ElevatorRepository elevatorRepository;

    @Value("${elevator.maxOpenDoorSeconds}")
    private int openCutoffSeconds;
    private final PendingRequests pendingRequests;

    @Autowired
    public ElevatorService(ElevatorRepository elevatorRepository, PendingRequests pendingRequests) {
        this.elevatorRepository = elevatorRepository;
        this.pendingRequests = pendingRequests;
    }

    public boolean hailElevator(HailRequest hailRequest) {
        return pendingRequests.addHailRequest(hailRequest.requestFloor, hailRequest.direction);
    }

    public boolean requestFloor(CommandRequest commandRequest) {
        int desiredFloor = commandRequest.desiredFloor;

        Optional<Elevator> optElevator = elevatorRepository.getElevatorById(commandRequest.elevatorId);

        if (optElevator.isEmpty()) {
            return false;
        }

        Elevator elevator = optElevator.get();

        final int nextFloor = elevator.nextFloor;
        final Direction currentDirection = getElevatorDirection(elevator);

        if (currentDirection == Direction.UP
                && desiredFloor >= nextFloor) {
            elevator.floorsOnWayUp.add(desiredFloor);
        } else if (currentDirection == Direction.UP
                    && desiredFloor < nextFloor){
            elevator.floorsOnWayDown.add(desiredFloor);
        } else if (currentDirection == Direction.DOWN
                && desiredFloor <= nextFloor) {
            elevator.floorsOnWayDown.add(desiredFloor);
        } else if (currentDirection == Direction.DOWN
                && desiredFloor > nextFloor){
            elevator.floorsOnWayUp.add(desiredFloor);
        } else if (desiredFloor > elevator.currentFloor){
            elevator.floorsOnWayUp.add(desiredFloor);
        } else if (desiredFloor < elevator.currentFloor) {
            elevator.floorsOnWayDown.add(desiredFloor);
        }

        return true;
    }

    private boolean isDoorOpen(LocalDateTime requestTime, Elevator elevator) {
        return requestTime
                .minusSeconds(openCutoffSeconds)
                .isBefore(elevator.lastOpen);
    }

    private Direction getElevatorDirection(Elevator elevator) {
        if (elevator.currentFloor < elevator.nextFloor) {
            return Direction.UP;
        }

        if (elevator.currentFloor > elevator.nextFloor) {
            return Direction.DOWN;
        }

        return Direction.IDLE;
    }

}
