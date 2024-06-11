import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.RealDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.*;

public class FDistributionTest {

    @Test
    public void testFDistributionCreation() {
        double numeratorDegreesOfFreedom = 4.0;
        double denominatorDegreesOfFreedom = 6.0;
        FDistribution fDist = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        assertEquals(numeratorDegreesOfFreedom, fDist.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(denominatorDegreesOfFreedom, fDist.getDenominatorDegreesOfFreedom(), 0.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFDistributionNegativeNumeratorDOF() {
        double numeratorDegreesOfFreedom = -4.0;
        double denominatorDegreesOfFreedom = 6.0;
        try {
            new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        } catch (NotStrictlyPositiveException e) {
            assertEquals(LocalizedFormats.NEGATIVE_NUMBER, e.getMessage());
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFDistributionNegativeDenominatorDOF() {
        double numeratorDegreesOfFreedom = 4.0;
        double denominatorDegreesOfFreedom = -6.0;
        try {
            new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        } catch (NotStrictlyPositiveException e) {
            assertEquals(LocalizedFormats.NEGATIVE_NUMBER, e.getMessage());
        }
    }

    @Test
    public void testFDistributionDensity() {
        FDistribution fDist = new FDistribution(1.0, 2.0);
        double x = 2.5;
        double density = fDist.density(x);
        assertEquals(1.5129426811828425, density, 0.00001);
    }

    @Test
    public void testFDistributionCumulativeProbability() {
        FDistribution fDist = new FDistribution(2.0, 4.0);
        double x = 1.5;
        double cp = fDist.cumulativeProbability(x);
        assertEquals(0.876311633543907, cp, 0.00001);
    }

    @Test
    public void testFDistributionInvalidArgumentCumulativeProbability() {
        FDistribution fDist = new FDistribution(1.0, 2.0);
        double x = -1.0;
        try {
            fDist.cumulativeProbability(x);
        } catch (IllegalArgumentException e) {
            assertEquals("argument must be non-negative", e.getMessage());
        }
    }

    @Test
    public void testFDistributionGetSolverAbsoluteAccuracy() {
        FDistribution fDist = new FDistribution(1.0, 2.0);
        double third = Math.pow(10, -3);
        assertEquals(third, fDist.getSolverAbsoluteAccuracy(), 0.0);
    }

    @Test
    public void testFDistributionNumericalMean() {
        FDistribution fDist = new FDistribution(3.0, 6.0);
        double mean = fDist.getNumericalMean();
        assertEquals(3.125, mean, 0.00001);
    }

    @Test
    public void testFDistributionNumericalVariance() {
        FDistribution fDist = new FDistribution(3.0, 6.0);
        double variance = fDist.getNumericalVariance();
        assertEquals(1.602070175, variance, 0.00001);
    }

    @Test
    public void testFDistributionSupportProperties() {
        FDistribution fDist = new FDistribution(1.0, 2.0);
        assertEquals(0.0, fDist.getSupportLowerBound(), 0.0);
        assertEquals(Double.POSITIVE_INFINITY, fDist.getSupportUpperBound(), 0.0);
        assertTrue(fDist.isSupportLowerBoundInclusive());
        assertFalse(fDist.isSupportUpperBoundInclusive());
        assertTrue(fDist.isSupportConnected());
    }

    @Test
    public void testFDistributionRandomGeneration() {
        FDistribution fDist = new FDistribution(1.0, 2.0);
        Well19937c rng = new Well19937c();
        for (int i = 0; i < 1000; i++) {
            double randomNumber = fDist.inverseCumulativeProbability(rng.nextDouble());
            assertTrue(fDist.isInSupport(randomNumber));
        }
    }
}