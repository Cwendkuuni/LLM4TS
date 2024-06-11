import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.NormalDistributionImpl;
import org.junit.Test;
import static org.junit.Assert.*;

public class NormalDistributionImplTest {

    @Test
    public void testNormalDistributionImpl_ZeroMeanOneStdDev() {
        NormalDistribution distribution = new NormalDistributionImpl();

        // Test mean and standard deviation
        assertEquals(0.0, distribution.getMean(), 0.001);
        assertEquals(1.0, distribution.getStandardDeviation(), 0.001);

        // Test density
        double x = 0.0;
        double density = distribution.density(x);
        assertEquals(0.39894228040143271, density, 0.0001);

        x = 1.0;
        density = distribution.density(x);
        assertEquals(0.39894228040143271, density, 0.0001);

        // Test cumulative probability
        double cumulativeProbability = distribution.cumulativeProbability(x);
        assertEquals(0.5, cumulativeProbability, 0.0001);

        // Test inverse cumulative probability
        double inverseCumulativeProbability = distribution.inverseCumulativeProbability(0.5);
        assertEquals(0.0, inverseCumulativeProbability, 0.0001);

        inverseCumulativeProbability = distribution.inverseCumulativeProbability(0.999);
        assertEquals(3.04437622652481, inverseCumulativeProbability, 0.0001);

        // Test sample
        double sample = distribution.sample();
        assertEquals(0.0, sample, 0.001);

        sample = distribution.sample();
        assertEquals(1.0, sample, 0.001);
    }

    @Test (expected = MathException.class)
    public void testNormalDistributionImpl_NegativeStandardDeviation() {
        double mean = 0.0;
        double standardDeviation = -1.0;
        NormalDistributionImpl distribution = new NormalDistributionImpl(mean, standardDeviation);
    }

    @Test (expected = MathException.class)
    public void testNormalDistributionImpl_InverseCumulativeProbabilityOutOfRange() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);
        distribution.inverseCumulativeProbability(1.1);
    }
}