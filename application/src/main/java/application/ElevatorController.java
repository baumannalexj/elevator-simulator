package application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController()
@RequestMapping("/elevators")
public class ElevatorController {

    private static final Logger LOG = LoggerFactory.getLogger(ElevatorController.class);
    private ElevatorService elevatorService;
    private ElevatorRepository elevatorRepository;

    @Autowired
    public ElevatorController(ElevatorService elevatorService, ElevatorRepository elevatorRepository) {
        this.elevatorService = elevatorService;
        this.elevatorRepository = elevatorRepository;
    }

    @GetMapping()
    public List<ElevatorAgent> getElevators() {
        return elevatorRepository.getAllElevators();
    }

//
//    @PostMapping("/destination")
//    public destination() {
////        which floor do you want to go to? needs destinationFloorNumber, elevatorNumber
//    }
//
    @PostMapping("/hail")
    public ResponseEntity<String> hail(@RequestBody @Validated RequestHail requestHail) {
//        up or down, current floor

        elevatorRepository.addHail(requestHail);

        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

}