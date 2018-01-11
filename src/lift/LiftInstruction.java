package lift;

public class LiftInstruction{
    private int floor;
    private LiftController.Direction direction;
    private int count;

    public LiftInstruction(int floor, LiftController.Direction direction) {
        this.floor = floor;
        this.direction = direction;
        this.count = 1;
    }

    void increment() {
        count++;
    }

    void decrement() {
        count--;
    }

    public int getFloor() {
        return floor;
    }

    public LiftController.Direction getDirection() {
        return direction;
    }

    public int getCount() {
        return count;
    }
}
