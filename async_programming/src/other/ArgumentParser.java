package other;

public class ArgumentParser {
    public String type = "sync";
    public int producers = 10;
    public int consumers = 10;
    public long agentWork = 10;
    public long objectWork = 10;
    public long totalGet = 50000;
    public int bound = 10;

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
                case "--agentWork" -> agentWork = Long.parseLong(parts[1]);
                case "--objectWork" -> objectWork = Long.parseLong(parts[1]);
                case "--totalGet" -> totalGet = Long.parseLong(parts[1]);
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
