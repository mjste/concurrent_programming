package other;

import java.util.List;

public class Calc {
    public static double meanLong(List<Long> list) {
        double sum = 0;
        for (Long x : list) sum += x;
        return sum / list.size();
    }

    public static double meanDouble(List<Double> list) {
        double sum = 0;
        for (Double x : list) sum += x;
        return sum / list.size();
    }
}
