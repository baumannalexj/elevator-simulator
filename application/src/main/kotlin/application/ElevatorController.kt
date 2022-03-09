package application

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/elevators")
class ElevatorController @Autowired constructor(private val elevatorRepository: ElevatorRepository) {

    @GetMapping
    fun getElevators(): ResponseEntity<List<ElevatorWorker>> {
        return ResponseEntity.ok(elevatorRepository.allElevators())
    }

    @PostMapping("/destination")
    fun destination(@RequestBody requestElevatorPanel: RequestElevatorPanel): ResponseEntity<String> {
        elevatorRepository.addDestinationForElevatorId(requestElevatorPanel.elevatorId, requestElevatorPanel.floorNumber)

        return ResponseEntity.status(HttpStatus.ACCEPTED).build()
    }

    @PostMapping("/hail")
    fun hail(@RequestBody @Validated requestHail: RequestHail): ResponseEntity<String> {
        elevatorRepository.addHail(requestHail)

        return ResponseEntity.status(HttpStatus.ACCEPTED).build()
    }
}