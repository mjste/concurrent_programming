package structures;

import java.util.List;

public class PutMethod extends MethodWrapper {

    public PutMethod(BufferMonitor bufferMonitor, List<Integer> integerList) {
        super(bufferMonitor, integerList);
    }

    @Override
    boolean canBeExecuted() {
        return bufferMonitor.getCapacity() - bufferMonitor.getStored() >= integerList.size();
    }

    @Override
    void execute() {
        for (Integer i : integerList) {
            bufferMonitor.put(i);
        }
        response.done = true;
    }
}
