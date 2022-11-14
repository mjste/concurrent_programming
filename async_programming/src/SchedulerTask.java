interface SchedulerTask {
    void run(BufferMonitor bufferMonitor);

    boolean canRun(BufferMonitor bufferMonitor);
}