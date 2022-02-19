package application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

public class ElevatorWorker
        implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(ElevatorWorker.class);
    public final int id;
    private final ElevatorService elevatorService;
    private final long SLEEP_IN_MILLIS = 3000L; // 1/sleep == actions per second
    private final Map<Direction, Queue<Integer>> directionToFloorQueue = Map.of(
            Direction.UP, new PriorityQueue<>(), // min heap going up
            Direction.DOWN, new PriorityQueue<>(Collections.reverseOrder()), // max heap going down,
            Direction.IDLE, new PriorityQueue<>()
    );
    private boolean ELEVATOR_IS_ON = true;
    private Direction direction = Direction.IDLE;
    private double yCoordinate = 0;
    private DoorState doorState = DoorState.OPEN;


    public ElevatorWorker(int id, ElevatorService elevatorService) {
        this.id = id;
        this.elevatorService = elevatorService;
    }

    private int getNextFloor() {

        final Queue<Integer> nextFloors = directionToFloorQueue.get(direction);

        if (!nextFloors.isEmpty()) {
            return nextFloors.peek();
        }

        final int nextFloor;
        if (!directionToFloorQueue.get(Direction.UP).isEmpty()) {
            direction = Direction.UP;
            nextFloor = directionToFloorQueue.get(direction).peek();
        } else if (!directionToFloorQueue.get(Direction.DOWN).isEmpty()) {
            direction = Direction.DOWN;
            nextFloor = directionToFloorQueue.get(direction).peek();
        } else {
            direction = Direction.IDLE;
            nextFloor = (int) yCoordinate;
        }

        return nextFloor;
    }

    private Direction getDirectionOfFloor(double nextFloorNumber) {
        if (yCoordinate == nextFloorNumber) {
            return Direction.IDLE;
        }

        return yCoordinate < nextFloorNumber
                ? Direction.UP
                : Direction.DOWN;
    }

    private void queueUpNextFloorDestinationsFromButtons() {
        elevatorService.getAndClearNextFloorDestinationForElevatorId(id).forEach(destinationFloorNumber -> {
            final Direction requestFloorDirection = getDirectionOfFloor(destinationFloorNumber);
            final Queue<Integer> pQueue = directionToFloorQueue.get(requestFloorDirection);
            pQueue.add(destinationFloorNumber);
        });
    }

    private void queueUpNextFloorDestinationsFromHails() {
        final List<RequestHail> nextHails = elevatorService.getNextHails(yCoordinate, direction);

        nextHails.forEach(hail->{
            int pickupAtFloor = hail.getFromFloorNumber();
            final Direction directionOfHail = getDirectionOfFloor(pickupAtFloor);
            final Queue<Integer> pQueue = directionToFloorQueue.get(directionOfHail);
            pQueue.add(pickupAtFloor);
        });

    }

    private void clearIdleFloors() {
//        may blindly add to 'this' floor when idling
        directionToFloorQueue.get(Direction.IDLE).clear();
    }

    @Override
    public void run() {

        while (ELEVATOR_IS_ON) {
            try {
                LOG.info("I'm running {}, at y:{} i'm going:{}, and going to floors {}",
                        id, yCoordinate, direction, directionToFloorQueue.get(direction));

                queueUpNextFloorDestinationsFromHails();

                LOG.info("handled hails {}, at y:{} i'm going:{}, and going to floors {}",
                        id, yCoordinate, direction, directionToFloorQueue.get(direction));

                queueUpNextFloorDestinationsFromButtons();

                LOG.info("handled buttons {}, at y:{} i'm going:{}, and going to floors {}",
                        id, yCoordinate, direction, directionToFloorQueue.get(direction));

                clearIdleFloors();

                final boolean isElevatorAtNextFloor = (int) yCoordinate == getNextFloor();
                if (isElevatorAtNextFloor) {
                    this.doorState = DoorState.OPEN;
                    yCoordinate = getNextFloor();
                    final Queue<Integer> nextFloorQueue = directionToFloorQueue.get(direction);

                    Integer servedFloor = nextFloorQueue.poll();
                    while (servedFloor != null && servedFloor.equals(nextFloorQueue.peek())) {
                        nextFloorQueue.poll();
                    }

                    if (nextFloorQueue.isEmpty()) {
                        direction = Direction.IDLE;
                    }


                } else {
                    doorState = DoorState.CLOSED;
                    yCoordinate += direction.getIntDirection() + (1.0D / SLEEP_IN_MILLIS);
                }

                LOG.info("finished running {}, y:{} i'm going:{} to floors {}",
                        id, yCoordinate, direction, directionToFloorQueue.get(direction));


                Thread.sleep(SLEEP_IN_MILLIS);

            } catch (Exception e) {
                LOG.error("Error for Elevator Agent", e);
            }
        }


    }
}
