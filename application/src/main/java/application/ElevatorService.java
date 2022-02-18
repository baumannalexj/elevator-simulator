package application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Deque;
import java.util.Optional;

@Service
public class ElevatorService {

    private final ElevatorRepository repository;

    @Autowired
    public ElevatorService(ElevatorRepository repository) {
        this.repository = repository;
    }

    public Optional<RequestHail> getNextHail(final double yCoordinate,
                                             final Direction elevatorDirection) {
        final Deque<RequestHail> hails = repository.getHails();


        final Optional<RequestHail> nextHail;
        if (elevatorDirection == Direction.IDLE) {
            nextHail = Optional.ofNullable(hails.pollFirst());
        } else {
            nextHail = hails.stream().filter(hail -> {

                boolean goingSameDirection = elevatorDirection == hail.getDirection();

                boolean isElevatorHeadingTowards = elevatorDirection == Direction.UP
                        ? yCoordinate <= hail.getFromFloorNumber()
                        : yCoordinate >= hail.getFromFloorNumber();

                return goingSameDirection && isElevatorHeadingTowards;

            }).findAny();

            if (nextHail.isPresent()) {
                hails.remove(nextHail.get());
            }
        }



        return nextHail;

    }

}
