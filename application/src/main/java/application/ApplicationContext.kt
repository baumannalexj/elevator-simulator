package application

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import javax.annotation.PostConstruct

@Configuration
open class ApplicationContext(private val elevatorService: ElevatorService,
                              private val elevatorRepository: ElevatorRepository) {


    @Value("\${server.port}")
    private val port = 0

    @Value("\${spring.application.name}")
    private val name: String? = null

    @Value("\${elevator.count:2}")
    private val elevatorCount = 0


    @PostConstruct
    fun start() {
        for (index in 0 until elevatorCount) {
            val elevator = ElevatorWorker(index, elevatorService)
            elevatorRepository.addElevator(elevator)
            val elevatorThread = Thread(elevator)
            elevatorThread.start()
        }
        LOG.info("""running $name at http://localhost:$port""")
    }

    companion object {
        private val LOG: Logger = LoggerFactory.getLogger(ApplicationContext::class.java)
    }
}