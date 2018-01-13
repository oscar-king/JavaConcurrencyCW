
package lift;

/**
 * Null implementation of a Lift Controller.
 * @author K. Bryson
 */
public class MyLiftController implements LiftController {
    Instructions instructions = new Instructions();
    int currentFloor = 0;
    Direction currentDirection = Direction.UP;
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
        instructions.addInstruction(floor,Direction.UNSET);
        while (!(liftAtFloor(currentFloor,currentDirection)&&doorsOpen&&currentFloor==floor)) wait();
        instructions.removeInstruction(floor,Direction.UNSET);
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
        wait(5000);
//        int dir = (currentDirection==Direction.UP) ? 0:1;
//        int pushCount = instructions.instructionsArr[floor][dir];
//        while (instructions.instructionsArr[floor][dir]!=0||instructions.instructionsArr[floor][2]!=pushCount) {
//            wait();
//        }
        notifyAll();
    }

    public synchronized void doorsClosed(int floor) {
    	doorsOpen = false;
    }

    //Inner class dealing with instructions
    private class Instructions {
        // instructionsArr[floor][0] =
        private int[][] instructionsArr = new int[9][3];

        private synchronized boolean contains(int floor, Direction direction) {
            switch (direction) {
                case UNSET:
                    return instructionsArr[floor][2] != 0;
                default:
                    int dir = (direction == Direction.UP) ? 0:1;
                    return (instructionsArr[floor][dir]+ instructionsArr[floor][2])!=0;
            }
        }


        private synchronized void addInstruction(int floor, Direction direction) {
            switch (direction) {
                case UP:
                    instructionsArr[floor][0]++;
                    break;
                case DOWN:
                    instructionsArr[floor][1]++;
                    break;
                case UNSET:
                    instructionsArr[floor][2]++;
                    break;
            }
        }

        private synchronized void removeInstruction(int floor, Direction direction) {
            switch (direction) {
                case UP:
                    instructionsArr[floor][0]=(instructionsArr[floor][0]>0) ? instructionsArr[floor][0]--: instructionsArr[floor][0];
                    break;
                case DOWN:
                    instructionsArr[floor][1]=(instructionsArr[floor][1]>0) ? instructionsArr[floor][1]--: instructionsArr[floor][1];
                    break;
                case UNSET:
                    instructionsArr[floor][2]=(instructionsArr[floor][2]>0) ? instructionsArr[floor][2]--: instructionsArr[floor][2];
                    break;
            }
        }
    }
}
