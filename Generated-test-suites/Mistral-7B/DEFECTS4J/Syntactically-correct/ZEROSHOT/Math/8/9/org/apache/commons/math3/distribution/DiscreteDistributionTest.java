import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.Well19937c;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.Pair;
import org.junit.Test;
import static org.junit.Assert.*;

public class DiscreteDistributionTest {

    @Test
    public void testEmptyDistribution() {
        List<Pair<Object, Double>> samples = new ArrayList<>();
        try {
            DiscreteDistribution distribution = new DiscreteDistribution(samples);
            fail("Expected NotEmptyListException");
        } catch (NotPositiveException | MathIllegalArgumentException | MathArithmeticException e) {
            // Expected exceptions
        }
    }

    @Test
    public void testInvalidProbability() {
        List<Pair<Object, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, -1.0));
        try {
            DiscreteDistribution distribution = new DiscreteDistribution(samples);
            fail("Expected NotPositiveException");
        } catch (NotPositiveException | MathIllegalArgumentException | MathArithmeticException e) {
            // Expected exception
        }
    }

    @Test
    public void testZeroSumProbability() {
        List<Pair<Object, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 1.0));
        samples.add(new Pair<>(2, -1.0));
        try {
            DiscreteDistribution distribution = new DiscreteDistribution(samples);
            fail("Expected MathArithmeticException");
        } catch (NotPositiveException | MathIllegalArgumentException | MathArithmeticException e) {
            // Expected exception
        }
    }

    @Test
    public void testProbabilityAtValue() {
        List<Pair<Object, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution distribution = new DiscreteDistribution(new Well19937c(), samples);

        assertEquals(0.3, distribution.probability(1), 0.001);
        assertEquals(0.4, distribution.probability(2), 0.001);
        assertEquals(0.3, distribution.probability(3), 0.001);
        assertNull(distribution.probability(4));
    }

    @Test
    public void testSample() {
        List<Pair<Object, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution distribution = new DiscreteDistribution(new Well19937c(), samples);

        for (int i = 0; i < 100; i++) {
            assertEquals(samples.get(randomIndex(samples.size())).getKey(), distribution.sample());
        }
    }

    @Test
    public void testSampleArray() {
        List<Pair<Object, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution distribution = new DiscreteDistribution(new Well19937c(), samples);

        try {
            distribution.sample(-1);
            fail("Expected IndexOutOfBoundsException");
        } catch (NotStrictlyPositiveException e) {
            // Expected exception
        }

        T[] result = distribution.sample(3);

        for (int i = 0; i < result.length; i++) {
            assertEquals(samples.get(randomIndex(samples.size())).getKey(), result[i]);
        }
    }

    private int randomIndex(int n) {
        return (int) (Math.random() * n);
    }

}