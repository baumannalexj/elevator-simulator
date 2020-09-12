package application.repository;

import application.enums.Direction;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

@Repository
public class PendingRequests {

    private final SortedSet<Integer> upHailsForFloor = Collections.synchronizedSortedSet(new TreeSet<>());
    private final SortedSet<Integer> downHailsForFloor = Collections.synchronizedSortedSet(new TreeSet<>());

    public boolean addHailRequest(int requestFloor, Direction direction) {
        if (direction == Direction.UP){
            upHailsForFloor.add(requestFloor);
        }

        if (direction == Direction.DOWN){
            downHailsForFloor.add(requestFloor);
        }

        return true;
    }

    //keep threadsafe cache of pending hails (Pair<floor, direction>)

    //keep threadsafe cache of pending orders (inside elevator)



}
