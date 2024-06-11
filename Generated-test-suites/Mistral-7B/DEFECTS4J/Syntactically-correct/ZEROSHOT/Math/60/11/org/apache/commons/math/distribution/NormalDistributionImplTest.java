import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.NormalDistributionImpl;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Test;
import static org.junit.Assert.*;

public class NormalDistributionImplTest {

    @Test
    public void testNormalDistributionImplConstruction() {
        NormalDistribution distribution = new NormalDistributionImpl(0, 1);
        assertEquals(0, distribution.getMean(), 0.001);
        assertEquals(1, distribution.getStandardDeviation(), 0.001);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNormalDistributionImplNegativeStandardDeviation() {
        new NormalDistributionImpl(0, -1);
    }

    @Test
    public void testNormalDistributionImplMeanAndStandardDeviation() {
        NormalDistribution distribution = new NormalDistributionImpl(2, 3);
        assertEquals(2, distribution.getMean(), 0.001);
        assertEquals(3, distribution.getStandardDeviation(), 0.001);
    }

    @Test
    public void testNormalDistributionImplDensity() {
        NormalDistribution distribution = new NormalDistributionImpl(0, 1);
        double density = distribution.density(1);
        assertEquals(0.398942280401433, density, 0.0001);
    }

    @Test
    public void testNormalDistributionImplCumulativeProbability() throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl(0, 1);
        double cumulativeProbability = distribution.cumulativeProbability(0.5);
        assertEquals(0.682689462137);
    }

    @Test
    public void testNormalDistributionImplInverseCumulativeProbability() throws MathException, NotStrictlyPositiveException {
        NormalDistribution distribution = new NormalDistributionImpl(0, 1);
        double inverseCumulativeProbability = distribution.inverseCumulativeProbability(0.341344745587);
        assertEquals(-0.471532926338);
    }

    @Test(expected = MathException.class)
    public void testNormalDistributionImplInverseCumulativeProbabilityOutOfRange() throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl(0, 1);
        distribution.inverseCumulativeProbability(1.5);
    }

    @Test
    public void testNormalDistributionImplSample() throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl(0, 1);
        double sample = distribution.sample();
        assertEquals(0.0, sample, 0.01);
    }
}