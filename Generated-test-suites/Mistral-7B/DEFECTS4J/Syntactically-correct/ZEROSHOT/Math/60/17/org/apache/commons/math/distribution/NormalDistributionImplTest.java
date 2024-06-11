import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.NormalDistributionImpl;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Test;
import static org.junit.Assert.*;

public class NormalDistributionImplTest {

    @Test
    public void testNormalDistributionImplCreation() {
        NormalDistribution distribution = new NormalDistributionImpl(0, 1);
        assertEquals("Mean should be set correctly", 0.0, distribution.getMean(), 0.001);
        assertEquals("Standard deviation should be set correctly", 1.0, distribution.getStandardDeviation(), 0.001);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNormalDistributionImplNegativeStandardDeviation() {
        new NormalDistributionImpl(0, -1);
    }

    @Test
    public void testNormalDistributionImplDensity() {
        NormalDistribution distribution = new NormalDistributionImpl(0, 1);

        double density = distribution.density(0.5);
        assertEquals("Density should be correct", 0.1508113281254, density, 0.00001);

        density = distribution.density(5);
        assertEquals("Density should be close to zero for large x", 0.0, density, 0.01);
    }

    @Test
    public void testNormalDistributionImplCumulativeProbability() throws Exception {
        NormalDistribution distribution = new NormalDistributionImpl(0, 1);

        double probability = distribution.cumulativeProbability(-3);
        assertEquals("Probability for x < -3 should be zero", 0.0, probability, 0.001);

        probability = distribution.cumulativeProbability(1.5);
        assertEquals("Probability for x = 1.5 should be close to 0.433", 0.433192758818, probability, 0.001);

        probability = distribution.cumulativeProbability(5);
        assertEquals("Probability for x = 5 should be close to 1.0", 0.9999999999999975, probability, 0.00001);
    }

    @Test
    public void testNormalDistributionImplInverseCumulativeProbability() throws Exception {
        NormalDistribution distribution = new NormalDistributionImpl(0, 1);

        double inverseProbability = distribution.inverseCumulativeProbability(0.25);
        assertEquals("Inverse cumulative probability for p = 0.25 should be close to -0.6745", -0.674489750794, inverseProbability, 0.001);

        inverseProbability = distribution.inverseCumulativeProbability(0.75);
        assertEquals("Inverse cumulative probability for p = 0.75 should be close to 0.6745", 0.674489750794, inverseProbability, 0.001);

        inverseProbability = distribution.inverseCumulativeProbability(0);
        assertEquals("Inverse cumulative probability for p = 0 should return negative infinity", Double.NEGATIVE_INFINITY, inverseProbability);

        inverseProbability = distribution.inverseCumulativeProbability(1);
        assertEquals("Inverse cumulative probability for p = 1 should return positive infinity", Double.POSITIVE_INFINITY, inverseProbability);
    }

    @Test
    public void testNormalDistributionImplSample() throws Exception {
        NormalDistribution distribution = new NormalDistributionImpl(0, 1);

        double sample = distribution.sample();
        double mean = distribution.getMean();
        double stddev = distribution.getStandardDeviation();

        assertEquals("Sample should be close to mean", mean, sample, stddev);

        sample = distribution.sample();
        assertEquals("Sample should be close to mean", mean, sample, stddev);
    }
}