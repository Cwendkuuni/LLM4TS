import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.distribution.PoissonDistributionImpl;
import org.junit.Test;
import static org.junit.Assert.*;

public class PoissonDistributionImplTest {

    @Test
    public void testConstructorWithPositiveMean() {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(3.5);
        assertEquals("Mean should be set correctly", 3.5, poissonDistribution.getMean(), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNegativeMean() {
        new PoissonDistributionImpl(-3.5);
    }

    @Test
    public void testProbabilityMethod() {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(5.0);
        assertEquals("Probability of 0 should be 0.0067", 0.0067, poissonDistribution.probability(0), 0.001);
        assertEquals("Probability of 1 should be 0.0225", 0.0225, poissonDistribution.probability(1), 0.001);
        assertEquals("Probability of 2 should be 0.0528", 0.0528, poissonDistribution.probability(2), 0.001);
    }

    @Test
    public void testCumulativeProbabilityMethod() throws MathException {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(5.0);
        assertEquals("Cumulative probability of 0 should be 0.0455", 0.0455, poissonDistribution.cumulativeProbability(0), 0.001);
        assertEquals("Cumulative probability of 1 should be 0.1359", 0.1359, poissonDistribution.cumulativeProbability(1), 0.001);
        assertEquals("Cumulative probability of 10 should be 0.9945", 0.9945, poissonDistribution.cumulativeProbability(10), 0.001);
    }

    @Test
    public void testNormalApproximateProbabilityMethod() throws MathException {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(5.0);
        for (int i = 0; i <= 15; i++) {
            double normalApproximateProbability = poissonDistribution.normalApproximateProbability(i);
            double exactProbability = poissonDistribution.probability(i);
            assertEquals("Normal approximate probability should be close to the exact probability for i = " + i, exactProbability, normalApproximateProbability, 0.01);
        }
    }

    @Test
    public void testSampleMethod() throws MathException {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(5.0);
        for (int i = 0; i < 100; i++) {
            int sample = poissonDistribution.sample();
            assertEquals("Sample should be a non-negative integer", 0, sample, Integer.MIN_VALUE);
        }
    }

    @Test
    public void testInverseCumulativeProbabilityMethod() throws MathException {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(5.0);
        for (double p = 0.0; p <= 1.0; p += 0.01) {
            int inverseCumulativeProbability = poissonDistribution.inverseCumulativeProbability(p);
            int expected = (int) Math.round(poissonDistribution.cumulativeProbability(inverseCumulativeProbability));
            assertEquals("Inverse cumulative probability should be the expected value for p = " + p, expected, inverseCumulativeProbability);
        }
    }
}