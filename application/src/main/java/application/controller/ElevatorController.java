package application.controller;

import application.model.Elevator;
import application.repository.ElevatorRepository;
import application.service.ElevatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController()
@RequestMapping("/elevators")
public class ElevatorController {

    private ElevatorService elevatorService;
    private ElevatorRepository elevatorRepository;

    @Autowired
    public ElevatorController(ElevatorService elevatorService, ElevatorRepository elevatorRepository) {
        this.elevatorService = elevatorService;
        this.elevatorRepository = elevatorRepository;
    }

    @GetMapping()
    public List<Elevator> getElevators() {
        return elevatorRepository.getAllElevators();
    }

}