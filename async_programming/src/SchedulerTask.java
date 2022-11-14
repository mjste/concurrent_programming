interface SchedulerTask {
    void run(BufferMonitor bufferMonitor, Response response);

    boolean canRun(BufferMonitor bufferMonitor);
}