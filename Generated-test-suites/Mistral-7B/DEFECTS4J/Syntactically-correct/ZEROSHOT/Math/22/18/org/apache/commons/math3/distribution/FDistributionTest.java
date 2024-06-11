import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.RealDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.special.Beta;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class FDistributionTest {

    @Test
    public void testFDistributionCreation() {
        try {
            FDistribution fDist = new FDistribution(2.5, 3.0);
            assertEquals(2.5, fDist.getNumeratorDegreesOfFreedom(), 0.0);
            assertEquals(3.0, fDist.getDenominatorDegreesOfFreedom(), 0.0);
            assertFalse(fDist.isSupportLowerBoundInclusive());
            assertFalse(fDist.isSupportUpperBoundInclusive());
            assertTrue(fDist.isSupportConnected());
        } catch (NotStrictlyPositiveException e) {
            //expected
        }
    }

    @Test
    public void testFDistributionDensity() {
        FDistribution fDist = new FDistribution(2.5, 3.0);
        double result;

        for (double x = 0.0; x <= 5.0; x += 0.1) {
            result = fDist.density(x);
            assertEquals(result, fDist.density(x), 0.0001);
        }
    }

    @Test
    public void testFDistributionCumulativeProbability() {
        FDistribution fDist = new FDistribution(2.5, 3.0);
        double result;

        for (double x = 0.0; x <= 5.0; x += 0.1) {
            result = fDist.cumulativeProbability(x);
            assertEquals(result, fDist.cumulativeProbability(x), 0.0001);
        }
    }

    @Test
    public void testFDistributionMean() {
        FDistribution fDist = new FDistribution(2.5, 3.0);
        double result = fDist.getNumericalMean();
        assertEquals(result, 2.5 / 1.5, 0.01);
    }

    @Test
    public void testFDistributionVariance() {
        FDistribution fDist = new FDistribution(2.5, 3.0);
        double result = fDist.getNumericalVariance();
        assertEquals(result, 2.0 * (2.5 * 2.5) / (3.0 * (3.0 - 2.0) * (3.0 - 4.0)), 0.01);
    }

    @Test
    public void testFDistributionSupport() {
        FDistribution fDist = new FDistribution(2.5, 3.0);
        assertEquals(0.0, fDist.getSupportLowerBound(), 0.0);
        assertEquals(Double.POSITIVE_INFINITY, fDist.getSupportUpperBound(), 0.0);
        assertTrue(fDist.isSupportLowerBoundInclusive());
        assertFalse(fDist.isSupportUpperBoundInclusive());
        assertTrue(fDist.isSupportConnected());
    }
}