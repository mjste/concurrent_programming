package structures.async;

import java.util.List;

public class PutMethodWrapper extends AbstractMethodWrapper {

    public PutMethodWrapper(BufferMonitor bufferMonitor, List<Integer> integerList) {
        super(bufferMonitor, integerList);
    }

    @Override
    boolean canBeExecuted() {
        return bufferMonitor.getCapacity() - bufferMonitor.getStored() >= integerList.size();
    }

    @Override
    void execute() {
        bufferMonitor.putList(integerList);
        response.done = true;
    }
}
