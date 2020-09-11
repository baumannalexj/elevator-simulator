package application.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Elevator {

    public int id;
    public int currentFloor = 1;
    public LocalDateTime lastOpen = LocalDateTime.now();

    public List<Person> persons = new ArrayList<>();

    public Set<Integer> floorsOnWayUp = new HashSet<>();
    public Set<Integer> floorsOnWayDown = new HashSet<>();


}
