import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Test;
import static org.junit.Assert.*;

public class FDistributionTest {

    @Test
    public void testFDistributionCreation() {
        try {
            FDistribution dist = new FDistribution(2.0, 3.0);
            assertEquals(2.0, dist.getNumeratorDegreesOfFreedom(), 0.0);
            assertEquals(3.0, dist.getDenominatorDegreesOfFreedom(), 0.0);
        } catch (NotStrictlyPositiveException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        try {
            FDistribution dist = new FDistribution(2.0, 3.0, 0.001);
            assertEquals(2.0, dist.getNumeratorDegreesOfFreedom(), 0.0);
            assertEquals(3.0, dist.getDenominatorDegreesOfFreedom(), 0.0);
            assertEquals(0.001, dist.getSolverAbsoluteAccuracy(), 0.0);
        } catch (NotStrictlyPositiveException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        try {
            FDistribution dist = new FDistribution(new Well19937c(), 2.0, 3.0, 0.001);
            assertEquals(2.0, dist.getNumeratorDegreesOfFreedom(), 0.0);
            assertEquals(3.0, dist.getDenominatorDegreesOfFreedom(), 0.0);
            assertEquals(0.001, dist.getSolverAbsoluteAccuracy(), 0.0);
        } catch (NotStrictlyPositiveException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testDensity() {
        FDistribution dist = new FDistribution(2.0, 3.0);

        assertEquals(0.0, dist.density(0.0), 1e-8);
        assertEquals(0.092808278, dist.density(0.5), 1e-8);
        assertEquals(0.2008550298, dist.density(1.0), 1e-8);
        assertEquals(0.711318373, dist.density(2.0), 1e-8);
        assertEquals(1.281551692, dist.density(3.0), 1e-8);
        assertEquals(3.132968668, dist.density(4.0), 1e-8);
    }

    @Test
    public void testCumulativeProbability() {
        FDistribution dist = new FDistribution(2.0, 3.0);

        assertEquals(0.0, dist.cumulativeProbability(0.0), 1e-8);
        assertEquals(0.120413891, dist.cumulativeProbability(0.5), 1e-8);
        assertEquals(0.5, dist.cumulativeProbability(1.0), 1e-8);
        assertEquals(0.916534776, dist.cumulativeProbability(2.0), 1e-8);
        assertEquals(1.0, dist.cumulativeProbability(3.0), 1e-8);
        assertEquals(1.0, dist.cumulativeProbability(10.0), 1e-8);
    }

    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistribution dist = new FDistribution(2.0, 3.0);
        assertEquals(2.0, dist.getNumeratorDegreesOfFreedom(), 0.0);
    }

    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistribution dist = new FDistribution(2.0, 3.0);
        assertEquals(3.0, dist.getDenominatorDegreesOfFreedom(), 0.0);
    }

    @Test
    public void testGetSolverAbsoluteAccuracy() {
        FDistribution dist = new FDistribution(2.0, 3.0, 0.001);
        assertEquals(0.001, dist.getSolverAbsoluteAccuracy(), 0.0);
    }

    @Test
    public void testGetNumericalMean() {
        FDistribution dist = new FDistribution(2.5, 3.0);
        assertEquals(1.6, dist.getNumericalMean(), 1e-8);

        FDistribution dist2 = new FDistribution(1.5, 3.0);
        assertEquals(Double.NaN, dist2.getNumericalMean(), 0.0);
    }

    @Test
    public void testGetNumericalVariance() {
        FDistribution dist = new FDistribution(3.0, 4.0);
        assertEquals(1.3158354758333333, dist.getNumericalVariance(), 1e-8);

        FDistribution dist2 = new FDistribution(2.0, 2.0);
        assertEquals(Double.NaN, dist2.getNumericalVariance(), 0.0);
    }

    @Test
    public void testGetSupportLowerBound() {
        FDistribution dist = new FDistribution(2.0, 3.0);
        assertEquals(0.0, dist.getSupportLowerBound(), 1e-8);
    }

    @Test
    public void testGetSupportUpperBound() {
        FDistribution dist = new FDistribution(2.0, 3.0);
        assertEquals(Double.POSITIVE_INFINITY, dist.getSupportUpperBound(), 1e-8);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentException() {
        new FDistribution(-1.0, 3.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentException2() {
        new FDistribution(2.0, -1.0);
    }
}