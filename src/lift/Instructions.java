package lift;

import java.util.ArrayList;

public class Instructions {
    private ArrayList<LiftInstruction> instructions = new ArrayList<>();

    public synchronized boolean contains(int floor, LiftController.Direction direction) {
        return getInstruction(floor, direction)!=null;
    }

    private LiftInstruction getInstruction(int floor, LiftController.Direction direction) {
        for (LiftInstruction instruction:instructions) {
            int instructionFloor = instruction.getFloor();
            LiftController.Direction instructionDirection = instruction.getDirection();
            if ((floor==instructionFloor)&&(instructionDirection==direction)) {
                return instruction;
            }
        }
        return null;
    }

    public synchronized void addInstruction(int floor, LiftController.Direction direction) {
        LiftInstruction instruction = getInstruction(floor, direction);
        if (contains(floor,direction)) {
            instruction.increment();
        } else {
            instructions.add(new LiftInstruction(floor, direction));
        }
    }

    public synchronized void removeInstruction(int floor, LiftController.Direction direction) {
        LiftInstruction instruction = getInstruction(floor, direction);
        if (instruction.getCount()==1) {
            instructions.remove(instruction);
        } else {
            instruction.decrement();
        }
    }
}
