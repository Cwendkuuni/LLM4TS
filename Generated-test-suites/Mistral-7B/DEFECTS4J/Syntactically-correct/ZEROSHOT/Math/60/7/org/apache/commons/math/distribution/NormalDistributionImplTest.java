import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.NormalDistributionImpl;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.junit.Test;
import static org.junit.Assert.*;

public class NormalDistributionImplTest {

    @Test
    public void testNormalDistributionInstance() {
        NormalDistribution distribution = new NormalDistributionImpl(0, 1);
        assertInstanceOf(NormalDistribution.class, distribution);
        assertInstanceOf(NormalDistributionImpl.class, distribution);
        assertEquals(0.0, distribution.getMean(), 0.0);
        assertEquals(1.0, distribution.getStandardDeviation(), 0.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeStandardDeviation() {
        new NormalDistributionImpl(0.0, -1.0);
    }

    @Test
    public void testNormalDistributionDensity() {
        NormalDistribution distribution = new NormalDistributionImpl(0, 1);
        double density = distribution.density(0.5);
        assertEquals(0.12566370614359172, density, 0.00001);
    }

    @Test
    public void testNormalDistributionCumulativeProbability() {
        NormalDistribution distribution = new NormalDistributionImpl(0, 1);
        double p = 0.5;
        double cumulativeProbability = distribution.cumulativeProbability(0.5);
        assertEquals(p, cumulativeProbability, 0.00001);
    }

    @Test
    public void testNormalDistributionInverseCumulativeProbability() {
        NormalDistribution distribution = new NormalDistributionImpl(0, 1);
        double p = 0.5;
        double inverseCumulativeProbability = distribution.inverseCumulativeProbability(p);
        assertEquals(0.5, inverseCumulativeProbability, 0.00001);
    }

    @Test(expected = MathException.class)
    public void testNormalDistributionInverseCumulativeProbabilityOutOfRange() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        distribution.inverseCumulativeProbability(1.5);
    }

    @Test
    public void testNormalDistributionSample() {
        NormalDistribution distribution = new NormalDistributionImpl(0, 1);
        double sample = distribution.sample();
        assertTrue(sample >= -3.0 && sample <= 3.0);
    }

    @Test
    public void testNormalDistributionSampleStandardDeviation() {
        NormalDistribution distribution = new NormalDistributionImpl(1, 2);
        double sample = distribution.sample();
        assertTrue(Math.abs(sample - 1.0) <= 2.0);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testNormalDistributionSamplePrecisionTooLow() {
        NormalDistribution distribution = new NormalDistributionImpl(0, 1);
        distribution.sample(0.000001);
    }
}