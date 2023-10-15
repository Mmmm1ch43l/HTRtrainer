import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;

public class WeightedDie {

    // A map that stores the cumulative weights and the values
    private NavigableMap<Double, Integer> map;
    // A random object to generate random numbers
    private Random random;
    // The total weight of all values
    private double total;

    // A constructor that takes an array of weights and an array of values
    public WeightedDie(double[] weights, int[] values) {
        if (weights.length != values.length) {
            throw new IllegalArgumentException("Weights and values must have the same length");
        }
        map = new TreeMap<>();
        random = new Random();
        total = 0;
        for (int i = 0; i < weights.length; i++) {
            if (weights[i] <= 0) {
                throw new IllegalArgumentException("Weights must be positive");
            }
            total += weights[i];
            map.put(total, values[i]);
        }
    }

    // A method that returns a random value according to the weights
    public int roll() {
        double r = random.nextDouble() * total;
        return map.higherEntry(r).getValue();
    }

}
