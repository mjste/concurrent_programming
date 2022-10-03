public class Main {
    public static void main(String[] args) {
        IncDec incDec = new IncDec();

        for (int i = 0; i < 5; i++) {
            incDec.createThreads(1, 10000, 1);
            incDec.createThreads(1, 10000, -1);
        }

        try {
            incDec.joinAll();
            System.out.println(incDec.val);
        } catch (InterruptedException e) {

        }


    }
}

// pięć wątków inkrementuje, 5 wątków dekrementuje