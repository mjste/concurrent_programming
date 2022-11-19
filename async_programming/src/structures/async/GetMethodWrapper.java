package structures.async;

import structures.BufferMonitor;

import java.util.ArrayList;
import java.util.List;

public class GetMethodWrapper extends AbstractMethodWrapper {
    private final int take;

    public GetMethodWrapper(BufferMonitor bufferMonitor, int take) {
        super(bufferMonitor, null);
        this.take = take;
    }

    @Override
    boolean canBeExecuted() {
        return bufferMonitor.getStored() >= take;
    }

    @Override
    void execute() {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < take; i++) {
            list.add(bufferMonitor.get());
        }
        response.result = list;
        response.done = true;
    }
}
