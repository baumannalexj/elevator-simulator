package application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Queue;

public class ElevatorAgent
        implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(ElevatorAgent.class);
    public final int id;
    private final ElevatorService elevatorService;
    private final long SLEEP_IN_MILLIS = 2000L; // 1/sleep == actions per second
    private final Map<Direction, Queue<Integer>> directionQueueMap = Map.of(
            Direction.UP, new PriorityQueue<>(), // min heap going up
            Direction.DOWN, new PriorityQueue<>(Collections.reverseOrder()), // max heap going down,
            Direction.IDLE, new PriorityQueue<>()
    );
    private boolean ELEVATOR_IS_ON = true;
    private Direction direction = Direction.IDLE;
    private double yCoordinate = 0;
    private DoorState doorState = DoorState.OPEN;


    public ElevatorAgent(int id, ElevatorService elevatorService) {
        this.id = id;
        this.elevatorService = elevatorService;
    }

    private int getNextFloor() {

        if (direction == Direction.IDLE) {
            return (int) yCoordinate;
        }

        Integer nextFloor = directionQueueMap.get(direction).peek();

        if (nextFloor == null) {
            return (int) yCoordinate;
        }

        return nextFloor;
    }


    @Override
    public void run() {

        while (ELEVATOR_IS_ON) {
            LOG.info("I'm running {}, at y:{} i'm going:{}, and going to floors {}",
                    id, yCoordinate, direction, directionQueueMap.get(direction));

            final Optional<RequestHail> nextHailOpt = elevatorService.getNextHail(yCoordinate, direction);

            if (nextHailOpt.isPresent()) {
                RequestHail nextHail = nextHailOpt.get();

                int pickupAtFloor = nextHail.getFromFloorNumber();

                if (direction == Direction.IDLE) {
                    if ((int) yCoordinate == pickupAtFloor) {
//                        hmm
                    } else {

                        doorState = DoorState.CLOSED;

                        direction = yCoordinate < pickupAtFloor
                                ? Direction.UP
                                : Direction.DOWN;
                    }
                }

                final Queue<Integer> pQueue = directionQueueMap.get(direction);
                pQueue.add(pickupAtFloor);

            }

            LOG.info("still running {}, at y:{} i'm going:{}, and going to floors {}",
                    id, yCoordinate, direction, directionQueueMap.get(direction));


            if (Direction.IDLE == direction) {
                //  do nothing
            } else if ((int) Math.floor(yCoordinate) == getNextFloor()) {
//               TODO update range to stop
                yCoordinate = getNextFloor();
                doorState = DoorState.OPEN;
                directionQueueMap.get(direction).poll();

            } else {
                doorState = DoorState.CLOSED;
                yCoordinate += direction.getIntDirection() + (1.0D / SLEEP_IN_MILLIS);
            }

            LOG.info("finished running {}, y:{} i'm going:{} to floors {}",
                    id, yCoordinate, direction, directionQueueMap.get(direction));


            try {
                Thread.sleep(SLEEP_IN_MILLIS);
            } catch (InterruptedException e) {
                LOG.error("error with sleeping", e);
            }
        }


    }
}
