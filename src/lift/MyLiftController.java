
package lift;

/**
 * Null implementation of a Lift Controller.
 * @author K. Bryson
 */
public class MyLiftController implements LiftController {
    private int currentFloor = 0;
    private Direction currentDirection = Direction.UP;
    private boolean doorsOpen = false;
    private int[] peopleGoingUpAtFloor = new int[9];
    private int[] peopleGoingDownAtFloor = new int[9];
    private int[] peopleGettingOutAtFloor = new int[9];


    /* Interface for People */

    public synchronized void pushUpButton(int floor) throws InterruptedException {
        peopleGoingUpAtFloor[floor]++;
        while (!(currentFloor == floor && currentDirection == Direction.UP && doorsOpen)) wait();
        peopleGoingUpAtFloor[floor]--;
        notifyAll();
    }

    public synchronized void pushDownButton(int floor) throws InterruptedException {
        peopleGoingDownAtFloor[floor]++;
        while (!(currentFloor == floor && currentDirection == Direction.DOWN && doorsOpen)) wait();
        peopleGoingDownAtFloor[floor]--;
        notifyAll();
    }
    
    public synchronized void selectFloor(int floor) throws InterruptedException {
        peopleGettingOutAtFloor[floor]++;
        while (!(currentFloor == floor && doorsOpen)) wait();
        peopleGettingOutAtFloor[floor]--;
        notifyAll();
    }

    
    /* Interface for Lifts */
    public synchronized boolean liftAtFloor(int floor, Direction direction) {

        /*
            liftAtFloor changes the state of the currentFloor and currentDirection
            If the number of people who want to enter the lift with the given direction
            and exit the lift at the current floor is not 0 return true, else false
         */

        currentFloor = floor;
        currentDirection = direction;
        int peopleGoingInDirection = (currentDirection == Direction.UP) ? peopleGoingUpAtFloor[floor] : peopleGoingDownAtFloor[floor];
        return (peopleGoingInDirection + peopleGettingOutAtFloor[floor]) != 0 ;
    }

    public synchronized void doorsOpen(int floor) throws InterruptedException {

        /*
            1. NotifyAll before the wait to wake up other methods prior to entering a waiting state
            no threads will be awake if done after.
            2. Condition checks for people leaving the lift and people wanting to enter, not if they actually did
         */

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
