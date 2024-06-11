import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.NormalDistributionImpl;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.junit.Test;
import static org.junit.Assert.*;

public class NormalDistributionImplTest {

    @Test
    public void testNormalDistributionImpl() {
        try {
            NormalDistribution dist = new NormalDistributionImpl(0, 1);
            assertEquals(0, dist.getMean(), 0);
            assertEquals(1, dist.getStandardDeviation(), 0);
        } catch (NotStrictlyPositiveException e) {
            fail("Expected no exception");
        }

        try {
            NormalDistribution dist = new NormalDistributionImpl(1, 0);
            assertEquals(1, dist.getMean(), 0);
            assertEquals(0, dist.getStandardDeviation(), 0);
        } catch (NotStrictlyPositiveException e) {
            fail("Expected no exception");
        }

        try {
            NormalDistribution dist = new NormalDistributionImpl(-1, 1);
            assertEquals(-1, dist.getMean(), 0);
            assertEquals(1, dist.getStandardDeviation(), 0);
        } catch (NotStrictlyPositiveException e) {
            fail("Expected no exception");
        }

        try {
            NormalDistribution dist = new NormalDistributionImpl(0, -1);
            assertEquals(0, dist.getMean(), 0);
            assertEquals(-1, dist.getStandardDeviation(), 0);
        } catch (NotStrictlyPositiveException e) {
            fail("Expected no exception");
        }

        try {
            NormalDistribution dist = new NormalDistributionImpl(0, 0);
            assertEquals(0, dist.getMean(), 0);
            assertEquals(0, dist.getStandardDeviation(), 0);
        } catch (NotStrictlyPositiveException e) {
            fail("Expected no exception");
        }

        try {
            NormalDistribution dist = new NormalDistributionImpl(1, 0.5);
            assertEquals(1, dist.getMean(), 0);
            assertEquals(0.5, dist.getStandardDeviation(), 0);
        } catch (NotStrictlyPositiveException e) {
            fail("Expected no exception");
        }
    }

    @Test
    public void testDensity() {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);

        for (double x = -5; x <= 5; x += 0.1) {
            double density = dist.density(x);
            double expected = FastMath.exp(-0.5 * Math.pow(x, 2)) / (Math.sqrt(2 * Math.PI));
            assertEquals(expected, density, 0.001);
        }
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);

        for (double x = -5; x <= 5; x += 0.1) {
            double p = dist.cumulativeProbability(x);
            double expected = 0.5 * (1 + Erf.erf(x / Math.sqrt(2)));
            assertEquals(expected, p, 0.001);
        }
    }

    @Test(expected = MathException.class)
    public void testCumulativeProbabilityOutOfRange() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        dist.cumulativeProbability(Double.NEGATIVE_INFINITY);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);

        for (double p = 0; p <= 1; p += 0.01) {
            double x = dist.inverseCumulativeProbability(p);
            double expected = dist.cumulativeProbability(x);
            assertEquals(p, expected, 0.001);
        }
    }

    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityOutOfRange() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        dist.inverseCumulativeProbability(1.1);
    }

    @Test
    public void testSample() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);

        for (int i = 0; i < 1000; i++) {
            double x = dist.sample();
            assertTrue(x >= -5 && x <= 5);
        }
    }
}