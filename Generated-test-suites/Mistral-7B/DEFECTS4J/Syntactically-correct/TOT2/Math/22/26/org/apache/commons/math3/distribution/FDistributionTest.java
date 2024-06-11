import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class FDistributionTest {

    private FDistribution fd;

    @Before
    public void setUp() throws NotStrictlyPositiveException {
        RandomGenerator rng = new Well19937c();
        fd = new FDistribution(rng, 2.5, 4.5);
    }

    @Test
    public void testDensity() {
        double x = 1.0;
        double result = fd.density(x);
        double expected = 0.3258872468543227;
        Assert.assertEquals(expected, result, 1e-8);
    }

    @Test
    public void testCumulativeProbability() {
        double x = 1.5;
        double result = fd.cumulativeProbability(x);
        double expected = 0.9112758996125228;
        Assert.assertEquals(expected, result, 1e-8);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeParameters() {
        FDistribution fdNegative = new FDistribution(new Well19937c(), -1.5, 2.5);
    }

    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        double result = fd.getNumeratorDegreesOfFreedom();
        double expected = 2.5;
        Assert.assertEquals(expected, result, 0.0);
    }

    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        double result = fd.getDenominatorDegreesOfFreedom();
        double expected = 4.5;
        Assert.assertEquals(expected, result, 0.0);
    }

    @Test
    public void testGetSolverAbsoluteAccuracy() {
        double result = fd.getSolverAbsoluteAccuracy();
        double expected = 0.0;
        Assert.assertEquals(expected, result, 0.0);
    }

    @Test
    public void testGetNumericalMean() {
        double result = fd.getNumericalMean();
        double expected = 2.5 / (2.5 - 2);
        Assert.assertEquals(expected, result, 1e-8);
    }

    @Test
    public void testGetNumericalVariance() {
        double result = fd.getNumericalVariance();
        double expected = (2.5 * 2.5 * (2.5 + 4.5 - 2)) /
                (2.5 * (4.5 - 2) * (4.5 - 4));
        Assert.assertEquals(expected, result, 1e-8);
    }

    @Test
    public void testGetSupportLowerBound() {
        double result = fd.getSupportLowerBound();
        double expected = 0.0;
        Assert.assertEquals(expected, result, 0.0);
    }

    @Test
    public void testGetSupportUpperBound() {
        double result = fd.getSupportUpperBound();
        double expected = Double.POSITIVE_INFINITY;
        Assert.assertEquals(expected, result, 0.0);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        boolean result = fd.isSupportLowerBoundInclusive();
        boolean expected = true;
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        boolean result = fd.isSupportUpperBoundInclusive();
        boolean expected = false;
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testIsSupportConnected() {
        boolean result = fd.isSupportConnected();
        boolean expected = true;
        Assert.assertEquals(expected, result);
    }
}