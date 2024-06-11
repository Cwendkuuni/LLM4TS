import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.RealDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.*;

public class FDistributionTest {

    @Test
    public void testFDistributionCreation() {
        try {
            FDistribution fd = new FDistribution(1.0, 1.0);
            assertTrue(fd instanceof FDistribution);
        } catch (NotStrictlyPositiveException e) {
            fail("Creation of FDistribution with non-positive degrees of freedom should throw NotStrictlyPositiveException");
        }
    }

    @Test
    public void testFDistributionDensity() {
        FDistribution fd = new FDistribution(1.0, 1.0);
        double density = fd.density(1.0);
        assertTrue(density > 0);
    }

    @Test
    public void testFDistributionCumulativeProbability() {
        FDistribution fd = new FDistribution(1.0, 1.0);
        double cumulativeProbability = fd.cumulativeProbability(1.0);
        assertTrue(cumulativeProbability > 0 && cumulativeProbability <= 1);
    }

    @Test
    public void testFDistributionGetNumeratorDegreesOfFreedom() {
        FDistribution fd = new FDistribution(1.0, 1.0);
        double numeratorDegreesOfFreedom = fd.getNumeratorDegreesOfFreedom();
        assertEquals(1.0, numeratorDegreesOfFreedom, 0.0);
    }

    @Test
    public void testFDistributionGetDenominatorDegreesOfFreedom() {
        FDistribution fd = new FDistribution(1.0, 1.0);
        double denominatorDegreesOfFreedom = fd.getDenominatorDegreesOfFreedom();
        assertEquals(1.0, denominatorDegreesOfFreedom, 0.0);
    }

    @Test
    public void testFDistributionGetSolverAbsoluteAccuracy() {
        FDistribution fd = new FDistribution(1.0, 1.0);
        double solverAbsoluteAccuracy = fd.getSolverAbsoluteAccuracy();
        assertEquals(FDistribution.DEFAULT_INVERSE_ABSOLUTE_ACCURACY, solverAbsoluteAccuracy, 0.0);
    }

    @Test
    public void testFDistributionGetNumericalMean() {
        FDistribution fd = new FDistribution(1.0, 2.0);
        double numericalMean = fd.getNumericalMean();
        assertEquals(1.5, numericalMean, 0.0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testFDistributionGetCumulativeProbabilityWithNonPositiveArgument() {
        FDistribution fd = new FDistribution(1.0, 1.0);
        fd.cumulativeProbability(-1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFDistributionConstructorWithInvalidDegreesOfFreedom() {
        new FDistribution(-1.0, 1.0);
    }

    @Test
    public void testFDistributionRandomGeneration() {
        FDistribution fd = new FDistribution(new Well19937c(), 1.0, 2.0);
        RandomGenerator rng = fd.getRandomGenerator();
        for (int i = 0; i < 1000; i++) {
            double value = fd.inverseCumulativeProbability(rng.nextDouble());
            assertTrue(value >= 0);
        }
    }

    @Test
    public void testFDistributionCalculateNumericalVariance() {
        FDistribution fd = new FDistribution(1.0, 2.0);
        double numericalVariance = fd.getNumericalVariance();
        assertEquals(1.5, numericalVariance, 0.0);
    }
}