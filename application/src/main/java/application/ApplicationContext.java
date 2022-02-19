package application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class ApplicationContext {
    private static final Logger LOG = LoggerFactory.getLogger(ApplicationContext.class);

    @Value("${server.port}")
    private int port;

    @Value("${spring.application.name}")
    private String name;

    @Value("${elevator.count:2}")
    private int elevatorCount;

    @Autowired
    private ElevatorService elevatorService;
    @Autowired
    private ElevatorRepository elevatorRepository;


    @PostConstruct
    public void start() {

        for (int index = 0; index < elevatorCount; index++) {
            ElevatorWorker elevator = new ElevatorWorker(index, elevatorService);
            elevatorRepository.addElevator(elevator);

            Thread elevatorThread = new Thread(elevator);
            elevatorThread.start();
        }


        LOG.info("running \"" + name + "\" at http://localhost:" + port);


    }

}
