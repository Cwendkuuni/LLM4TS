import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.NormalDistributionImpl;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.junit.Test;
import static org.junit.Assert.*;

public class NormalDistributionImplTest {

    @Test
    public void testNormalDistributionConstruction() {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        assertEquals("Mean should be set correctly", 0.0, dist.getMean(), 1e-6);
        assertEquals("Standard deviation should be set correctly", 1.0, dist.getStandardDeviation(), 1e-6);

        dist = new NormalDistributionImpl(2.5, 1.5);
        assertEquals("Mean should be set correctly", 2.5, dist.getMean(), 1e-6);
        assertEquals("Standard deviation should be set correctly", 1.5, dist.getStandardDeviation(), 1e-6);

        try {
            new NormalDistributionImpl(-1.0, 1.0);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }

        try {
            new NormalDistributionImpl(0.0, -1.0);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }
    }

    @Test
    public void testDensity() {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);

        double density = dist.density(0.5);
        assertEquals("Density should be correct", 0.3989422804, density, 1e-6);

        try {
            dist.density(-1.0);
            fail("Expected MathException");
        } catch (MathException e) {
            // Expected
        }

        try {
            dist.density(5.0);
            fail("Expected MathException");
        } catch (MathException e) {
            // Expected
        }
    }

    @Test
    public void testCumulativeProbability() {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);

        double p = 0.3;
        double cdf = dist.cumulativeProbability(0.5);
        assertEquals("CDF should be correct", 0.6826894921, cdf, 1e-6);

        p = 0.15;
        cdf = dist.cumulativeProbability(1.0);
        assertEquals("CDF should be correct", 0.8413447460, cdf, 1e-6);

        try {
            dist.cumulativeProbability(-1.0);
            fail("Expected MathException");
        } catch (MathException e) {
            // Expected
        }

        try {
            dist.cumulativeProbability(3.0);
            fail("Expected MathException");
        } catch (MathException e) {
            // Expected
        }
    }

    @Test
    public void testInverseCumulativeProbability() {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);

        double p = 0.3;
        double x = dist.inverseCumulativeProbability(p);
        assertEquals("Inverse CDF should be correct", 0.5321220663, x, 1e-6);

        p = 0.15;
        x = dist.inverseCumulativeProbability(p);
        assertEquals("Inverse CDF should be correct", 1.2815516923, x, 1e-6);

        p = 0.5;
        x = dist.inverseCumulativeProbability(p);
        assertEquals("Inverse CDF should be correct", 0.0, x, 1e-6);

        try {
            dist.inverseCumulativeProbability(-0.1);
            fail("Expected MathException");
        } catch (MathException e) {
            // Expected
        }

        try {
            dist.inverseCumulativeProbability(2.0);
            fail("Expected MathException");
        } catch (MathException e) {
            // Expected
        }
    }

    @Test
    public void testSample() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);

        double sample = dist.sample();
        assertEquals("Sample should be close to mean", 0.0, sample, 1e-3);

        sample = dist.sample();
        assertEquals("Sample should be close to mean", 0.0, sample, 1e-3);

        sample = dist.sample();
        assertEquals("Sample should be close to mean", 0.0, sample, 1e-3);

        sample = dist.sample();
        assertEquals("Sample should be close to mean", 0.0, sample, 1e-3);

        sample = dist.sample();
        assertEquals("Sample should be close to mean", 0.0, sample, 1e-3);

        try {
            dist.sample(); // number of samples exceeded
            fail("Expected MathException");
        } catch (MathException e) {
            // Expected
        }
    }
}