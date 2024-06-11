import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.NormalDistributionImpl;
import org.junit.Test;
import static org.junit.Assert.*;

public class NormalDistributionImplTest {

    @Test
    public void testNormalDistributionImplConstruction() {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        assertEquals(0.0, dist.getMean(), 0.0);
        assertEquals(1.0, dist.getStandardDeviation(), 0.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNormalDistributionImplNegativeStandardDeviation() {
        new NormalDistributionImpl(0, -1.0);
    }

    @Test
    public void testNormalDistributionImplDensity() {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        for (double x = -5.0; x <= 5.0; x += 0.1) {
            double density = dist.density(x);
            double expected = FastMath.exp(-0.5 * Math.pow(x, 2)) / (Math.sqrt(2 * Math.PI));
            assertEquals(expected, density, 0.0001);
        }
    }

    @Test
    public void testNormalDistributionImplCumulativeProbability() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        for (double x = -5.0; x <= 5.0; x += 0.1) {
            double prob = dist.cumulativeProbability(x);
            double expected = 0.5 * (1.0 + Erf.erf(x / Math.sqrt(2.0)));
            assertEquals(expected, prob, 0.0001);
        }
    }

    @Test
    public void testNormalDistributionImplInverseCumulativeProbability() throws MathException, NotStrictlyPositiveException {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        for (double p = 0.0; p <= 1.0; p += 0.01) {
            double x = dist.inverseCumulativeProbability(p);
            double expected = Math.sqrt(2.0) * Erf.erfinv(2 * p - 1.0);
            assertEquals(expected, x, 0.0001);
        }
    }

    @Test
    public void testNormalDistributionImplSample() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        for (int i = 0; i < 1000; i++) {
            double sample = dist.sample();
            assertTrue(sample >= -5.0 && sample <= 5.0);
        }
    }
}