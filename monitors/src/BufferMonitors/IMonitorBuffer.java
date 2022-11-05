package BufferMonitors;

public interface IMonitorBuffer {
    void produce(int n);

    void consume(int n);

    long get_total_consumed();

    long get_total_operations();
}
