package structures.async;

import java.util.List;

public class Response {
    public boolean done = false;
    public List<Integer> result;

    public boolean isDone() {
        return done;
    }

    public List<Integer> getResult() {
        return result;
    }
}
