package other;

public class ArgumentParser {
    public String type = "async";
    public int producers = 10;
    public int consumers = 10;
    public long workToDo = 10;
    public int bound = 10;
    public long time = 2000;

    public ArgumentParser(String[] args) {
        for (String arg : args) {
            String[] parts = arg.split("=");
            if (parts.length != 2) {
                System.out.println("Wrong argument structure: " + arg);
                System.exit(1);
            }


            switch (parts[0]) {
                case "--producers" -> producers = Integer.parseInt(parts[1]);
                case "--consumers" -> consumers = Integer.parseInt(parts[1]);
                case "--work" -> workToDo = Long.parseLong(parts[1]);
                case "--time" -> time = Long.parseLong(parts[1]);
                case "--type" -> {
                    switch (parts[1]) {
                        case "sync" -> type="sync";
                        case "async" -> type="async";
                        default -> {
                            System.out.println("Wrong type {sync/async}: " + arg);
                            System.exit(2);
                        }
                    }
                }
                default -> {
                    System.out.println("Wrong parameter type: " + arg);
                    System.exit(2);
                }
            }
        }
    }
}
