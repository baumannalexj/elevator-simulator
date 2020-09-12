package application.controller;

import application.model.Elevator;
import application.request.CommandRequest;
import application.request.HailRequest;
import application.repository.ElevatorRepository;
import application.service.ElevatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController()
@RequestMapping(path = "/elevators",
        consumes = "application/json",
        produces = "application/json"
)
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

    @PostMapping("/hail")
    public boolean postHail(@RequestBody HailRequest hailRequest) {
        return elevatorService.hailElevator(hailRequest);
    }

    @PostMapping("/command")
    public boolean postCommand(@RequestBody CommandRequest commandRequest) {
        return elevatorService.requestFloor(commandRequest);

    }

}