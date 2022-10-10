public class Main {
    public static void main(String[] args) {
        int n = 2;
        int m = 1;

        Monitor monitor = new Monitor(2);

        for (int i = 0; i < n; i++) {
            Producer producer = new Producer(monitor);
            Thread thread = new Thread(producer);
            thread.start();
        }

        for (int i = 0; i < m; i++) {
            Consumer consumer = new Consumer(monitor);
            Thread thread = new Thread(consumer);
            thread.start();
        }
    }
}