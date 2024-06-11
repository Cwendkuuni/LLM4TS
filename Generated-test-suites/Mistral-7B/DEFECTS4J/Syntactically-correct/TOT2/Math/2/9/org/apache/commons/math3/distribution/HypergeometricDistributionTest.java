import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.distribution.HypergeometricDistributionTest;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.junit.Test;
import org.junit.Assert.*;

public class HypergeometricDistributionTest {

    @Test
    public void testConstructWithValidArguments() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
        assertEquals(10, dist.getPopulationSize());
        assertEquals(5, dist.getNumberOfSuccesses());
        assertEquals(5, dist.getSampleSize());
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructWithNegativeNumberOfSuccesses() {
        new HypergeometricDistribution(10, -2, 5);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructWithZeroPopulationSize() {
        new HypergeometricDistribution(0, 5, 5);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructWithNumberOfSuccessesGreaterThanPopulationSize() {
        new HypergeometricDistribution(10, 11, 5);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructWithSampleSizeGreaterThanPopulationSize() {
        new HypergeometricDistribution(10, 5, 11);
    }

    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
        int[] domain = dist.getDomain();

        for (int x = domain[0]; x <= domain[1]; x++) {
            double expected = x / (double) (populationSize);
            double actual = dist.cumulativeProbability(x);
            assertEquals(expected, actual, 0.00001);
        }
    }

    @Test
    public void testProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);

        for (int x = 0; x <= 10; x++) {
            double actual = dist.probability(x);
            assertEquals(dist.getProbabilityMassFunctionValue(x), actual, 0.00001);
        }
    }

    @Test
    public void testUpperCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
        int[] domain = dist.getDomain();

        for (int x = domain[0]; x <= domain[1]; x++) {
            double actual = dist.upperCumulativeProbability(x);
            double expected = 1.0 - dist.cumulativeProbability(x);
            assertEquals(expected, actual, 0.00001);
        }
    }

    @Test
    public void testNumericalMean() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
        double expected = 5.0;
        double actual = dist.getNumericalMean();
        assertEquals(expected, actual, 0.00001);
    }

    @Test
    public void testNumericalVariance() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
        double expected = 1.9602499842519685;
        double actual = dist.getNumericalVariance();
        assertEquals(expected, actual, 0.00001);
    }

    @Test
    public void testSupportLowerBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
        int expected = 2;
        int actual = dist.getSupportLowerBound();
        assertEquals(expected, actual);
    }

    @Test
    public void testSupportUpperBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
        int expected = 5;
        int actual = dist.getSupportUpperBound();
        assertEquals(expected, actual);
    }

    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
        boolean expected = true;
        boolean actual = dist.isSupportConnected();
        assertTrue(expected == actual);
    }
}