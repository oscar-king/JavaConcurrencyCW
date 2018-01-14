
package lift;

/**
 * Null implementation of a Lift Controller.
 * @author K. Bryson
 */
public class MyLiftController implements LiftController {
    int currentFloor = 0;
    Direction currentDirection = Direction.UP;
    boolean doorsOpen = false;
    int[] peopleGoingUpAtFloor = new int[9];
    int[] peopleGoingDownAtFloor = new int[9];
    int[] peopleGettingOutAtFloor = new int[9];


    /* Interface for People */
    public synchronized void pushUpButton(int floor) throws InterruptedException {
        peopleGoingUpAtFloor[floor]++;
        while (!(currentFloor == floor && currentDirection == Direction.UP && doorsOpen)) wait();
        peopleGoingUpAtFloor[floor]--;
        notifyAll();
    }

    public synchronized void pushDownButton(int floor) throws InterruptedException {
        peopleGoingDownAtFloor[floor]++;
        while (!(currentFloor == floor && currentDirection == Direction.UP && doorsOpen)) wait();
        peopleGoingDownAtFloor[floor]--;
        notifyAll();
    }
    
    public synchronized void selectFloor(int floor) throws InterruptedException {
        peopleGettingOutAtFloor[floor]++;
        while (!(currentFloor == floor && currentDirection == Direction.UP && doorsOpen)) wait();
        peopleGettingOutAtFloor[floor]--;
        notifyAll();
    }

    
    /* Interface for Lifts */
    public synchronized boolean liftAtFloor(int floor, Direction direction) {
        currentFloor = floor;
        currentDirection = direction;
        int peopleGoingInDirection = (currentDirection == Direction.UP) ? peopleGoingUpAtFloor[floor] : peopleGoingDownAtFloor[floor];
        return (peopleGoingInDirection + peopleGettingOutAtFloor[floor]) != 0 ;
    }

    public synchronized void doorsOpen(int floor) throws InterruptedException {
        doorsOpen = true;
        notifyAll();
        int[] peopleGoingInDirection = (currentDirection == Direction.UP) ? peopleGoingUpAtFloor : peopleGoingDownAtFloor;
        while (peopleGoingInDirection[floor]>0||peopleGettingOutAtFloor[floor]>0) wait();

    }

    public synchronized void doorsClosed(int floor) {
    	doorsOpen = false;
    	notifyAll();
    }
}
