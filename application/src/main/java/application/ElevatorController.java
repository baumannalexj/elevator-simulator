package application;

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

    private final ElevatorRepository elevatorRepository;

    @Autowired
    public ElevatorController(ElevatorRepository elevatorRepository) {
        this.elevatorRepository = elevatorRepository;
    }

    @GetMapping()
    public ResponseEntity<List<ElevatorWorker>> getElevators() {
        return ResponseEntity.ok(elevatorRepository.getAllElevators());
    }


    @PostMapping("/destination")
    public ResponseEntity<String> destination(@RequestBody RequestElevatorPanel requestElevatorPanel) {
        elevatorRepository.addDestinationForElevatorId(requestElevatorPanel.getElevatorId(), requestElevatorPanel.getFloorNumber());
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @PostMapping("/hail")
    public ResponseEntity<String> hail(@RequestBody @Validated RequestHail requestHail) {
        elevatorRepository.addHail(requestHail);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

}