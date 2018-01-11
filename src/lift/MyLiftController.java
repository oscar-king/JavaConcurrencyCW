
package lift;

/**
 * Null implementation of a Lift Controller.
 * @author K. Bryson
 */
public class MyLiftController implements LiftController {
    Instructions instructions = new Instructions();
    int currentFloor = 0;
    Direction currentDirection = Direction.UNSET;
    boolean doorsOpen = false;


    /* Interface for People */
    public synchronized void pushUpButton(int floor) throws InterruptedException {
        instructions.addInstruction(floor,Direction.UP);
        while (!(liftAtFloor(currentFloor,currentDirection)&&doorsOpen&&currentFloor==floor&&currentDirection==Direction.UP)) wait();
        instructions.removeInstruction(floor,Direction.UP);
        notifyAll();
    }

    public synchronized void pushDownButton(int floor) throws InterruptedException {
        instructions.addInstruction(floor,Direction.DOWN);
        while (!(liftAtFloor(currentFloor,currentDirection)&&doorsOpen&&currentFloor==floor&&currentDirection==Direction.DOWN)) wait();
        instructions.removeInstruction(floor,Direction.DOWN);
        notifyAll();
    }
    
    public synchronized void selectFloor(int floor) throws InterruptedException {
        instructions.addInstruction(floor,currentDirection);
        while (!(liftAtFloor(currentFloor,currentDirection)&&doorsOpen&&currentFloor==floor)) wait();
        instructions.removeInstruction(floor,currentDirection);
        notifyAll();
    }

    
    /* Interface for Lifts */
    public synchronized boolean liftAtFloor(int floor, Direction direction) {
        currentFloor = floor;
        currentDirection = direction;
        return instructions.contains(floor,direction);
    }

    public synchronized void doorsOpen(int floor) throws InterruptedException {
        doorsOpen = true;
        notifyAll();
        wait(10000);
        notifyAll();
    }

    public synchronized void doorsClosed(int floor) {
    	doorsOpen = false;
    	notifyAll();
    }
}
