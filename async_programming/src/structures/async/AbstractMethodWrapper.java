package structures.async;

import java.util.List;

public abstract class AbstractMethodWrapper {
    protected final Response response = new Response(); // empty response, set at the end of execute method
    protected final BufferMonitor bufferMonitor;
    protected final List<Integer> integerList;

    AbstractMethodWrapper(BufferMonitor bufferMonitor, List<Integer> integerList) {
        this.bufferMonitor = bufferMonitor;
        this.integerList = integerList;
    }

    abstract boolean canBeExecuted();

    abstract void execute();

    public Response getResponse() {
        return response;
    }
}
