package structures;

import java.util.List;

public abstract class MethodWrapper {
    protected final Response response = new Response();

    protected final BufferMonitor bufferMonitor;
    protected final List<Integer> integerList;

    MethodWrapper(BufferMonitor bufferMonitor, List<Integer> integerList) {
        this.bufferMonitor = bufferMonitor;
        this.integerList = integerList;
    }

    abstract boolean canBeExecuted();

    abstract void execute();

    public Response getResponse() {
        return response;
    }
}
