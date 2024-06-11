import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.FDistributionTest;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.*;

public class FDistributionTest extends FDistribution {

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructWithNegativeDegreesOfFreedom() {
        new FDistribution(-1.0, 2.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructWithNegativeNumeratorDegreesOfFreedom() {
        new FDistribution(0.5, -1.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructWithNegativeDenominatorDegreesOfFreedom() {
        new FDistribution(1.0, -1.0);
    }

    @Test
    public void testCreateWithValidParameters() {
        FDistribution dist = new FDistribution(1.0, 2.0);
        assertEquals(1.0, dist.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(2.0, dist.getDenominatorDegreesOfFreedom(), 0.0);
    }

    @Test
    public void testCreateWithDefaultPrecision() {
        FDistribution dist = new FDistribution(1.0, 2.0);
        assertEquals(1.0e-9, dist.getSolverAbsoluteAccuracy(), 0.0);
    }

    @Test
    public void testCreateWithCustomPrecision() {
        FDistribution dist = new FDistribution(1.0, 2.0, 1.0e-6);
        assertEquals(1.0e-6, dist.getSolverAbsoluteAccuracy(), 0.0);
    }

    @Test
    public void testCreateWithRandomNumberGenerator() {
        RandomGenerator rng = new Well19937c();
        FDistribution dist = new FDistribution(rng, 1.0, 2.0);
        assertEquals(rng, dist.getRandomGenerator());
    }

    @Test
    public void testDensityAtZero() {
        FDistribution dist = new FDistribution(2.0, 2.0);
        double density = dist.density(0.0);
        assertEquals(0.0, density, 1.0e-5);
    }

    @Test
    public void testDensityAtPositiveValue() {
        FDistribution dist = new FDistribution(2.0, 2.0);
        double density = dist.density(1.5);
        assertEquals(0.2844897358346157, density, 1.0e-5);
    }

    @Test
    public void testCumulativeProbabilityAtZero() {
        FDistribution dist = new FDistribution(2.0, 2.0);
        double probability = dist.cumulativeProbability(0.0);
        assertEquals(0.0, probability, 1.0e-9);
    }

    @Test
    public void testCumulativeProbabilityAtMean() {
        FDistribution dist = new FDistribution(1.5, 2.0);
        double mean = dist.getNumericalMean();
        double probability = dist.cumulativeProbability(mean);
        assertEquals(0.5, probability, 1.0e-9);
    }

    @Test
    public void testCumulativeProbabilityAtUpperBound() {
        FDistribution dist = new FDistribution(2.0, 2.0);
        double probability = dist.cumulativeProbability(Double.POSITIVE_INFINITY);
        assertEquals(1.0, probability, 1.0e-9);
    }

    @Test
    public void testVariance() {
        FDistribution dist = new FDistribution(3.0, 4.0);
        double variance = dist.getNumericalVariance();
        assertEquals(1.2052923294117653, variance, 1.0e-5);
    }

    @Test
    public void testSupportLowerBound() {
        FDistribution dist = new FDistribution(1.0, 2.0);
        double lowerBound = dist.getSupportLowerBound();
        assertEquals(0.0, lowerBound, 1.0e-9);
    }

    @Test
    public void testSupportUpperBound() {
        FDistribution dist = new FDistribution(1.0, 2.0);
        double upperBound = dist.getSupportUpperBound();
        assertEquals(Double.POSITIVE_INFINITY, upperBound, 1.0e-9);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        FDistribution dist = new FDistribution(1.0, 2.0);
        boolean isInclusive = dist.isSupportLowerBoundInclusive();
        assertTrue(isInclusive);
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        FDistribution dist = new FDistribution(1.0, 2.0);
        boolean isInclusive = dist.isSupportUpperBoundInclusive();
        assertFalse(isInclusive);
    }

    @Test
    public void testIsSupportConnected() {
        FDistribution dist = new FDistribution(1.0, 2.0);
        boolean isConnected = dist.isSupportConnected();
        assertTrue(isConnected);
    }
}