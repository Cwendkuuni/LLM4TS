import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UniformRealDistributionTest {

    private UniformRealDistribution distribution;
    private Well19937c random;

    @Before
    public void setUp() {
        random = new Well19937c();
        try {
            distribution = new UniformRealDistribution(random, 0, 1);
        } catch (NumberIsTooLargeException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLowerBound() {
        assertEquals("Lower bound should be 0", 0.0, distribution.getSupportLowerBound(), 1e-9);
    }

    @Test
    public void testUpperBound() {
        assertEquals("Upper bound should be 1", 1.0, distribution.getSupportUpperBound(), 1e-9);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        assertTrue("Support lower bound should be inclusive", distribution.isSupportLowerBoundInclusive());
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        assertFalse("Support upper bound should be exclusive", distribution.isSupportUpperBoundInclusive());
    }

    @Test
    public void testIsSupportConnected() {
        assertTrue("Support should be connected", distribution.isSupportConnected());
    }

    @Test
    public void testDensity() {
        for (double x = 0.0; x <= 1.0; x += 0.01) {
            double density = distribution.density(x);
            double expected = 1 / (1.0 - 0.0);
            assertEquals("Density should be " + expected, expected, density, 1e-9);
        }
    }

    @Test
    public void testCumulativeProbability() {
        for (double x = 0.0; x <= 1.0; x += 0.01) {
            double cdf = distribution.cumulativeProbability(x);
            double expected;
            if (x <= 0.0) {
                expected = 0.0;
            } else if (x >= 1.0) {
                expected = 1.0;
            } else {
                expected = x / (1.0 - 0.0);
            }
            assertEquals("CDF should be " + expected, expected, cdf, 1e-9);
        }
    }

    @Test
    public void testGetSolverAbsoluteAccuracy() {
        assertEquals("Solver absolute accuracy should be " + UniformRealDistribution.DEFAULT_INVERSE_ABSOLUTE_ACCURACY,
                UniformRealDistribution.DEFAULT_INVERSE_ABSOLUTE_ACCURACY, distribution.getSolverAbsoluteAccuracy(), 1e-9);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testInvalidConstructor() {
        new UniformRealDistribution(random, 1.0, 0.0);
    }

    @Test
    public void testSample() {
        for (int i = 0; i < 1000; i++) {
            double sample = distribution.sample();
            assertTrue("Sample should be between 0 and 1", sample >= 0.0 && sample < 1.0);
        }
    }

    @Test
    public void testSampleManyTimes() {
        int[] counts = new int[100];
        for (int i = 0; i < 100000; i++) {
            double sample = distribution.sample();
            int bin = (int) Math.floor((sample + 0.0001) * 100);
            counts[bin]++;
        }
        for (int i = 0; i < 100; i++) {
            double expected = 1000.0 / (100.0 * 10000.0);
            double actual = (double) counts[i] / 100000.0;
            assertEquals("Expected frequency of bin " + i + " is " + expected, expected, actual, 1e-5);
        }
    }
}