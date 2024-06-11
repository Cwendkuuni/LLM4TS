import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.special.Erf;
import org.junit.Test;
import static org.junit.Assert.*;

public class NormalDistributionImplTest {

    @Test
    public void testNormalDistributionImpl() {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        assertEquals("Mean should be 0", 0, dist.getMean(), 0.001);
        assertEquals("Standard deviation should be 1", 1, dist.getStandardDeviation(), 0.001);
    }

    @Test
    public void testDensity() {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        double density = dist.density(0);
        assertEquals("Density at mean should be highest", 0.398942280401, density, 0.001);

        density = dist.density(3);
        assertFalse("Density at extreme value should be low", Double.isNaN(density) || density > 0.01);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNotStrictlyPositiveException() {
        new NormalDistributionImpl(-1, 1);
    }

    @Test
    public void testCumulativeProbability() {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        double x;
        double p;

        x = -3;
        p = dist.cumulativeProbability(x);
        assertEquals("Probability at extreme negative value should be 0", 0, p, 0.01);

        x = 0;
        p = dist.cumulativeProbability(x);
        assertEquals("Probability at mean should be 0.5", 0.5, p, 0.01);

        x = 3;
        p = dist.cumulativeProbability(x);
        assertEquals("Probability at extreme positive value should be 1", 1, p, 0.01);

        // Check exceptions
        x = Double.NEGATIVE_INFINITY;
        try {
            dist.cumulativeProbability(x);
            fail("Expected MathException");
        } catch (MathException e) {
            // expected
        }

        x = Double.POSITIVE_INFINITY;
        try {
            dist.cumulativeProbability(x);
            fail("Expected MathException");
        } catch (MathException e) {
            // expected
        }
    }

    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityNegativeP() {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        dist.inverseCumulativeProbability(0);
    }

    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityOne() {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        dist.inverseCumulativeProbability(1);
    }

    @Test
    public void testInverseCumulativeProbability() {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        double p;
        double x;

        p = 0.1;
        x = dist.inverseCumulativeProbability(p);
        assertEquals("Inverse cumulative probability at low value should be close to -0.84", -0.84264478969, x, 0.01);

        p = 0.5;
        x = dist.inverseCumulativeProbability(p);
        assertEquals("Inverse cumulative probability at mean should be 0", 0, x, 0.01);

        p = 0.9;
        x = dist.inverseCumulativeProbability(p);
        assertEquals("Inverse cumulative probability at high value should be close to 0.84", 0.84264478969, x, 0.01);
    }

    @Test
    public void testSample() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        double sample;

        for (int i = 0; i < 100; i++) {
            sample = dist.sample();
            assertTrue("Sample should be between -3 and 3", sample >= -3 && sample <= 3);
        }
    }
}