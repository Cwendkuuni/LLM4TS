import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.RealDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.special.Beta;
import org.junit.Test;
import org.junit.Assert.assertEquals;
import org.junit.Assert.assertFalse;
import org.junit.Assert.assertTrue;

public class FDistributionTest {

    @Test
    public void testFDistributionConstruction() {
        try {
            FDistribution dist = new FDistribution(1.0, 2.0);
            assertEquals(1.0, dist.getNumeratorDegreesOfFreedom(), 0.0);
            assertEquals(2.0, dist.getDenominatorDegreesOfFreedom(), 0.0);
        } catch (NotStrictlyPositiveException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        try {
            FDistribution dist = new FDistribution(1.0, -2.0);
            fail("Expected NotStrictlyPositiveException but it was not thrown");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }

        try {
            FDistribution dist = new FDistribution(-1.0, 2.0);
            fail("Expected NotStrictlyPositiveException but it was not thrown");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }

        FDistribution dist = new FDistribution(2.0, 3.0);
        assertEquals(2.0, dist.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(3.0, dist.getDenominatorDegreesOfFreedom(), 0.0);
    }

    @Test
    public void testDensity() {
        FDistribution dist = new FDistribution(2.0, 3.0);
        assertEquals(0.1356678533471564, dist.density(1.0), 1e-6);
        assertEquals(0.0, dist.density(0.0), 1e-6);
        assertEquals(0.0, dist.density(-1.0), 1e-6);
    }

    @Test
    public void testCumulativeProbability() {
        FDistribution dist = new FDistribution(2.0, 3.0);
        assertEquals(0.1280349403546355, dist.cumulativeProbability(1.0), 1e-6);
        assertEquals(0.0, dist.cumulativeProbability(0.0), 1e-6);
        assertEquals(1.0, dist.cumulativeProbability(Double.POSITIVE_INFINITY), 1e-6);
    }

    @Test
    public void testMean() {
        FDistribution dist = new FDistribution(2.0, 3.0);
        assertEquals(3.0 / 2.0, dist.getNumericalMean(), 1e-6);
    }

    @Test
    public void testVariance() {
        FDistribution dist = new FDistribution(2.0, 3.0);
        assertEquals(3.0 / (3.0 - 4.0), dist.getNumericalVariance(), 1e-6);
    }

    @Test
    public void testSupportBounds() {
        FDistribution dist = new FDistribution(2.0, 3.0);
        assertTrue(dist.isSupportLowerBoundInclusive());
        assertFalse(dist.isSupportUpperBoundInclusive());
        assertEquals(0.0, dist.getSupportLowerBound(), 0.0);
        assertEquals(Double.POSITIVE_INFINITY, dist.getSupportUpperBound(), 0.0);
    }

    @Test
    public void testSupportConnected() {
        FDistribution dist = new FDistribution(2.0, 3.0);
        assertTrue(dist.isSupportConnected());
    }
}