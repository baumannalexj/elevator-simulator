package application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ElevatorService {
    private static final Logger LOG = LoggerFactory.getLogger(ElevatorService.class);

    private static final List EMPTY_LIST = Collections.emptyList();

    private final ElevatorRepository repository;
    private final Lock mutateHailsLock = new ReentrantLock();

    @Autowired
    public ElevatorService(ElevatorRepository repository) {
        this.repository = repository;
    }

    public synchronized List<RequestHail> getNextHails(final double yCoordinate,
                                          final Direction elevatorDirection) {


        final Deque<RequestHail> hails = repository.getHails();

        if (hails.isEmpty()) {
            return EMPTY_LIST;
        }

        final Direction directionToUse;

        if (elevatorDirection == Direction.IDLE) {
            directionToUse = hails.peek().getDirection(); // pick a direction to go
        } else {
            directionToUse = elevatorDirection;
        }

        Stream<RequestHail> filteredHails = hails.stream()
                .filter(hail -> directionToUse == hail.getDirection());

        if (elevatorDirection != Direction.IDLE) {
            filteredHails = filteredHails.filter(hail -> {
                double hailFloorNumber = (double) hail.getFromFloorNumber();
                return directionToUse == Direction.UP
                        ? yCoordinate <= hailFloorNumber
                        : yCoordinate >= hailFloorNumber;
            });
        }

        final List<RequestHail> requestHails = filteredHails.collect(Collectors.toUnmodifiableList());

        hails.removeAll(requestHails);
        return requestHails;

    }

    public List<Integer> getAndClearNextFloorDestinationForElevatorId(int elevatorId) {
        List<Integer> nextDestinations = repository.getFloorDestinationsForElevatorId(elevatorId);
        if (nextDestinations.isEmpty()) {
            return EMPTY_LIST;
        }
        List<Integer> listForElevator = new ArrayList<>(nextDestinations); // shallow copy but with primitives
        nextDestinations.clear(); //#check repository too

        return listForElevator;
    }

}
