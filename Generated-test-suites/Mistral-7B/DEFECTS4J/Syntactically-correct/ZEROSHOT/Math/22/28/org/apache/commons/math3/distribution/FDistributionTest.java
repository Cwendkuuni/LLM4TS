import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.RealDistribution;
import org.apache.commons.math3.special.Beta;
import org.junit.Test;
import static org.junit.Assert.*;

public class FDistributionTest {

    @Test
    public void testFDistributionConstruction() {
        try {
            FDistribution fDist = new FDistribution(2.0, 4.0);
            assertEquals(2.0, fDist.getNumeratorDegreesOfFreedom(), 0.0);
            assertEquals(4.0, fDist.getDenominatorDegreesOfFreedom(), 0.0);
        } catch (NotStrictlyPositiveException e) {
            fail("Construction should not throw an exception.");
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFDistributionNegativeDegreesOfFreedom() {
        new FDistribution(-2.0, 4.0);
    }

    @Test
    public void testFDistributionDensity() {
        FDistribution fDist = new FDistribution(2.0, 4.0);
        double x = 1.0;
        double expectedDensity = 0.4843988728047322;
        double actualDensity = fDist.density(x);
        assertEquals(expectedDensity, actualDensity, 1e-6);
    }

    @Test
    public void testFDistributionCumulativeProbability() {
        FDistribution fDist = new FDistribution(2.0, 4.0);
        double x = 1.0;
        double expectedCP = 0.6911305072481209;
        double actualCP = fDist.cumulativeProbability(x);
        assertEquals(expectedCP, actualCP, 1e-6);
    }

    @Test
    public void testFDistributionGetNumericalMean() {
        FDistribution fDist = new FDistribution(2.0, 4.0);
        double expectedMean = 2.0 / 0.5;
        double actualMean = fDist.getNumericalMean();
        assertEquals(expectedMean, actualMean, 1e-6);
    }

    @Test
    public void testFDistributionGetNumericalVariance() {
        FDistribution fDist = new FDistribution(2.0, 4.0);
        double expectedVariance = 1.5 / (0.5 * 0.5 * 0.5 * 3.0);
        double actualVariance = fDist.getNumericalVariance();
        assertEquals(expectedVariance, actualVariance, 1e-6);
    }

    @Test
    public void testFDistributionGetSupportBounds() {
        FDistribution fDist = new FDistribution(2.0, 4.0);
        double lowerBound = fDist.getSupportLowerBound();
        double upperBound = fDist.getSupportUpperBound();

        assertEquals(0.0, lowerBound, 1e-6);
        assertEquals(Double.POSITIVE_INFINITY, upperBound, 1e-6);
    }

    @Test
    public void testFDistributionIsSupportConnected() {
        FDistribution fDist = new FDistribution(2.0, 4.0);
        assertTrue(fDist.isSupportConnected());
    }
}