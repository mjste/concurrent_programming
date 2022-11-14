public class Package {
    public SchedulerTask task;
    public Response response = new Response();

    public Package(SchedulerTask task) {
        this.task = task;
    }
}
