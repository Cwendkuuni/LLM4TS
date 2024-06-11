import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class UniformRealDistributionTest {

    private UniformRealDistribution distribution;
    private Well19937c random;

    @Before
    public void setUp() {
        random = new Well19937c();
        distribution = new UniformRealDistribution(random, 0, 1);
    }

    @Test
    public void testDefaultConstructor() {
        UniformRealDistribution urd = new UniformRealDistribution();
        assertEquals(0.0, urd.getLowerBound(), 0.0);
        assertEquals(1.0, urd.getUpperBound(), 0.0);
    }

    @Test
    public void testConstructorWithBounds() {
        UniformRealDistribution urd = new UniformRealDistribution(random, 1.5, 3.2);
        assertEquals(1.5, urd.getLowerBound(), 0.0);
        assertEquals(3.2, urd.getUpperBound(), 0.0);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithInvalidBounds() {
        new UniformRealDistribution(random, 3.2, 1.5);
    }

    @Test
    public void testDensity() {
        double x = 0.5;
        double result = distribution.density(x);
        assertEquals(2.0, result, 1e-6);
    }

    @Test
    public void testCumulativeProbability() {
        double x = 0.3;
        double result = distribution.cumulativeProbability(x);
        assertEquals(0.3, result, 1e-6);

        x = 0.8;
        result = distribution.cumulativeProbability(x);
        assertEquals(0.8, result, 1e-6);
    }

    @Test
    public void testGetSolverAbsoluteAccuracy() {
        double result = distribution.getSolverAbsoluteAccuracy();
        assertEquals(UniformRealDistribution.DEFAULT_INVERSE_ABSOLUTE_ACCURACY, result, 1e-6);
    }

    @Test
    public void testGetNumericalMean() {
        double result = distribution.getNumericalMean();
        assertEquals(0.5, result, 1e-6);
    }

    @Test
    public void testGetNumericalVariance() {
        double result = distribution.getNumericalVariance();
        assertEquals(0.007853981633974483, result, 1e-6);
    }

    @Test
    public void testGetSupportLowerBound() {
        double result = distribution.getSupportLowerBound();
        assertEquals(0.0, result, 0.0);
    }

    @Test
    public void testGetSupportUpperBound() {
        double result = distribution.getSupportUpperBound();
        assertEquals(1.0, result, 0.0);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        boolean result = distribution.isSupportLowerBoundInclusive();
        assertTrue(result);
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        boolean result = distribution.isSupportUpperBoundInclusive();
        assertFalse(result);
    }

    @Test
    public void testIsSupportConnected() {
        boolean result = distribution.isSupportConnected();
        assertTrue(result);
    }

    @Test
    public void testSample() {
        double result;
        for (int i = 0; i < 100; i++) {
            result = distribution.sample();
            assertTrue(result >= distribution.getLowerBound());
            assertTrue(result < distribution.getUpperBound());
        }
    }
}