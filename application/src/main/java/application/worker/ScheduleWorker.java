package application.worker;

import application.repository.ElevatorRepository;
import application.repository.PendingRequests;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Component
public class ScheduleWorker implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(ScheduleWorker.class);
    private final ElevatorRepository elevatorRepository;
    private final PendingRequests pendingRequests;
    private final Thread thread;


    //TODO how to make this worker restartable?
    public ScheduleWorker(ElevatorRepository elevatorRepository, PendingRequests pendingRequests) {
        this.elevatorRepository = elevatorRepository;
        this.pendingRequests = pendingRequests;

        this.thread = new Thread(this);
        this.thread.start();
    }


    @Override
    public void run() {

        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            LOG.info("im running");

// go through each person,

            //go through pending hails

            //go through each elevator
            //look at next floor to det. direction
            // if up, look at floorsGoingUp to see if there is a new closer floor to replace "netfloor"
            // if door is closed, set "door open" in future?
        }




    }
}
