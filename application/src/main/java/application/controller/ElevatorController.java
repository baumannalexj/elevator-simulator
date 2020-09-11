package application.controller;

import application.model.Elevator;
import application.model.HailRequest;
import application.repository.ElevatorRepository;
import application.service.ElevatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
        //send off hail request
        return true;
    }

}