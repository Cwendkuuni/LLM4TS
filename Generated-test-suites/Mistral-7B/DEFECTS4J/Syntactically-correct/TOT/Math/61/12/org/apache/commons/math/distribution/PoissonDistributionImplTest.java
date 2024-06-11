import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.exception.MathException;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.junit.Test;
import static org.junit.Assert.*;

public class PoissonDistributionImplTest {

    @Test
    public void testPoissonDistribution() throws MathException {
        // Test with default constructor
        PoissonDistribution poissonDist = new PoissonDistributionImpl();
        // Mean should be 0 by default
        assertEquals(0.0, poissonDist.getMean(), 0.0);
        // Probability of 0 should be e^(-mean)
        assertEquals(Math.E, poissonDist.probability(0), 0.001);

        // Test with constructor taking mean as argument
        PoissonDistribution poissonDistWithMean = new PoissonDistributionImpl(1.5);
        // Mean should be set correctly
        assertEquals(1.5, poissonDistWithMean.getMean(), 0.001);
        // Probability of 0 should be e^(-mean)
        assertEquals(Math.exp(-1.5), poissonDistWithMean.probability(0), 0.01);
        // Probability of 1 should be mean * e^(-mean)
        assertEquals(1.5 * Math.exp(-1.5), poissonDistWithMean.probability(1), 0.01);

        // Test probability distribution with different values
        double[] probabilities = new double[10];
        for (int i = 0; i < 10; i++) {
            probabilities[i] = poissonDistWithMean.probability(i);
        }
        // Check if the sum of probabilities equals 1
        assertArrayEquals(new double[]{1.0}, new double[]{1.0 + Math.E - poissonDistWithMean.cumulativeProbability(0)}, 0.001);

        // Test cumulative probability distribution
        assertEquals(1.0, poissonDistWithMean.cumulativeProbability(Integer.MIN_VALUE), 0.0);
        assertEquals(0.0, poissonDistWithMean.cumulativeProbability(Integer.MAX_VALUE), 0.0);

        // Test normal approximation probability distribution
        PoissonDistribution poissonDistWithNormalApprox = new PoissonDistributionImpl(1.5);
        NormalDistribution normalDist = new NormalDistributionImpl(1.5, Math.sqrt(1.5));
        for (int i = 0; i < 10; i++) {
            double poissonProbability = poissonDistWithNormalApprox.probability(i);
            double normalApproximationProbability = normalDist.cumulativeProbability(i + 0.5);
            // Check if probabilities are approximately equal
            assertEquals(normalApproximationProbability, poissonProbability, 0.01);
        }

        // Test sample method
        PoissonDistribution poissonDistSample = new PoissonDistributionImpl(1.5);
        int sample = poissonDistSample.sample();
        // Check if sample is in range [0, 1000 * mean]
        assertTrue(sample >= 0 && sample <= 1000 * 1.5);
    }

    @Test
    public void testNegativeMean() {
        try {
            new PoissonDistributionImpl(-1.0);
            fail("Expected IllegalArgumentException");
        } catch (MathRuntimeException e) {
            assertTrue(e.getMessage().contains("Poisson mean must be positive"));
        }
    }

    @Test
    public void testSampleOutOfRange() {
        PoissonDistribution poissonDist = new PoissonDistributionImpl(1.5);
        try {
            poissonDist.sample();
            int sample = poissonDist.sample();
            int sample2 = poissonDist.sample();
            // Check if samples are equal
            assertEquals(sample, sample2);
            // Check if samples are greater than or equal to 0
            assertTrue(sample >= 0);
            // Check if samples are less than or equal to 1000 * mean
            assertTrue(sample <= 1000 * 1.5);
        } catch (MathException e) {
            fail("Unexpected MathException");
        }

        try {
            poissonDist.sample();
            int sample = poissonDist.sample();
            int sample2 = poissonDist.sample();
            // Check if sample is greater than or equal to 0
            assertTrue(sample >= 0);
            // Check if sample is less than or equal to Integer.MIN_VALUE
            assertFalse(sample <= Integer.MIN_VALUE);
            // Check if sample2 is greater than or equal to 0
            assertTrue(sample2 >= 0);
            // Check if sample2 is less than or equal to Integer.MIN_VALUE
            assertFalse(sample2 <= Integer.MIN_VALUE);
            fail("Expected MathException");
        } catch (MathException e) {
            // Expected MathException
        }
    }

    @Test
    public void testMeanAndStandardDeviation() throws MathException {
        PoissonDistribution poissonDist = new PoissonDistributionImpl(1.5);
        // Check if mean equals expected value
        assertEquals(1.5, poissonDist.getMean(), 0.001);
        // Check if standard deviation equals sqrt(mean)
        StandardDeviation stdDev = new StandardDeviation();
        double[] probabilities = new double[10];
        for (int i = 0; i < 10; i++) {
            probabilities[i] = poissonDist.probability(i);
        }
        double variance = 0.0;
        for (int i = 0; i < 10; i++) {
            variance += Math.pow(i - poissonDist.getMean(), 2) * probabilities[i];
        }
        variance /= poissonDist.getMean();
        assertEquals(Math.sqrt(1.5), stdDev.evaluate(probabilities), 0.01);
        assertEquals(Math.sqrt(1.5), Math.sqrt(variance), 0.01);
    }
}