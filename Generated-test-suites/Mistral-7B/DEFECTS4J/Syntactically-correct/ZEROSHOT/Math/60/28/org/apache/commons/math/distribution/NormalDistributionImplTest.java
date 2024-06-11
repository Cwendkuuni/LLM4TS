import static org.junit.Assert.assertEquals;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.special.Erf;
import org.junit.Test;

public class NormalDistributionImplTest {

    @Test
    public void testNormalDistributionCreation() {
        NormalDistribution distribution = new NormalDistributionImpl(0, 1);
        assertEquals(0.0, distribution.getMean(), 0.0);
        assertEquals(1.0, distribution.getStandardDeviation(), 0.0);
    }

    @Test
    public void testDensity() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        assertEquals(0.398942280401, distribution.density(0), 5e-6);
        assertEquals(0.319285714286, distribution.density(1), 5e-6);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        assertEquals(0.5, distribution.cumulativeProbability(0), 5e-6);
        assertEquals(0.841344746042, distribution.cumulativeProbability(1), 5e-6);
        assertEquals(Approx.RELATIVE.tolerance(0.0), distribution.cumulativeProbability(-3), 5e-6);
        assertEquals(Approx.RELATIVE.tolerance(1.0), distribution.cumulativeProbability(3), 5e-6);
    }

    @Test(expected = MathException.class)
    public void testCumulativeProbabilityException() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        distribution.cumulativeProbability(-5);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        assertEquals(-1.28213034561, distribution.inverseCumulativeProbability(0.15865525), 5e-6);
        assertEquals(1.28213034561, distribution.inverseCumulativeProbability(0.841344746), 5e-6);
        assertEquals(Approx.RELATIVE.tolerance(0.0), distribution.inverseCumulativeProbability(0), 5e-6);
        assertEquals(Approx.RELATIVE.tolerance(1.0), distribution.inverseCumulativeProbability(1), 5e-6);
    }

    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityException() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        distribution.inverseCumulativeProbability(0.51);
    }

    @Test
    public void testSample() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        assertEquals(0.081318120823, distribution.sample(), 5e-6);
    }
}