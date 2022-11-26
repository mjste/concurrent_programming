package structures.async;

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
        response.result = bufferMonitor.getList(take);
        response.done = true;
    }
}
