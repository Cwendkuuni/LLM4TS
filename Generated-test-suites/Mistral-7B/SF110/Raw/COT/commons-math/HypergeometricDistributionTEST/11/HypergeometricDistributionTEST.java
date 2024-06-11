import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest {

    @Test(expected = NotPositiveException.class)
    public void testConstructorWithNegativePopulationSize() {
        int populationSize = -1;
        int numberOfSuccesses = 10;
        int sampleSize = 10;
        try {
            new HypergeometricDistribution(new Well19937c(), populationSize, numberOfSuccesses, sampleSize);
            fail("Expected NotPositiveException");
        } catch (NotPositiveException e) {
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithNonStrictlyPositivePopulationSize() {
        int populationSize = 0;
        int numberOfSuccesses = 10;
        int sampleSize = 10;
        try {
            new HypergeometricDistribution(new Well19937c(), populationSize, numberOfSuccesses, sampleSize);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
        }
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructorWithNegativeNumberOfSuccesses() {
        int populationSize = 10;
        int numberOfSuccesses = -5;
        int sampleSize = 10;
        try {
            new HypergeometricDistribution(new Well19937c(), populationSize, numberOfSuccesses, sampleSize);
            fail("Expected NotPositiveException");
        } catch (NotPositiveException e) {
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithNonStrictlyPositiveNumberOfSuccesses() {
        int populationSize = 10;
        int numberOfSuccesses = 0;
        int sampleSize = 10;
        try {
            new HypergeometricDistribution(new Well19937c(), populationSize, numberOfSuccesses, sampleSize);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
        }

        try {
            new HypergeometricDistribution(new Well19937c(), populationSize, numberOfSuccesses + 1, sampleSize);
        } catch (NotStrictlyPositiveException e) {
            fail("Expected the constructor to accept the new numberOfSuccesses value without throwing an exception");
        }

    }

    @Test(expected = NotPositiveException.class)
    public void testConstructorWithNegativeSampleSize() {
        int populationSize = 10;
        int numberOfSuccesses = 10;
        int sampleSize = -5;
        try {
            new HypergeometricDistribution(new Well19937c(), populationSize, numberOfSuccesses, sampleSize);
            fail("Expected NotPositiveException");
        } catch (NotPositiveException e) {
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithNonStrictlyPositiveSampleSize() {
        int populationSize = 10;
        int numberOfSuccesses = 10;
        int sampleSize = 0;
        try {
            new HypergeometricDistribution(new Well19937c(), populationSize, numberOfSuccesses, sampleSize);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
        }

        try {
            new HypergeometricDistribution(new Well19937c(), populationSize, numberOfSuccesses, sampleSize + 1);
        } catch (NotStrictlyPositiveException e) {
            fail("Expected the constructor to accept the new sampleSize value without throwing an exception");
        }

    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithNumberOfSuccessesLargerThanPopulationSize() {
        int populationSize = 10;
        int numberOfSuccesses = 12;
        int sampleSize = 10;
        try {
            new HypergeometricDistribution(new Well19937c(), populationSize, numberOfSuccesses, sampleSize);
            fail("Expected NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
        }
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithSampleSizeLargerThanPopulationSize() {
        int populationSize = 10;
        int numberOfSuccesses = 10;
        int sampleSize = 12;
        try {
            new HypergeometricDistribution(new Well19937c(), populationSize, numberOfSuccesses, sampleSize);
            fail("Expected NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
        }
    }

    @Test
    public void testCumulativeProbabilityZero() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 2, 5);
        double result = dist.cumulativeProbability(0);
        assertEquals(0.0, result, 1e-9);
    }

    @Test
    public void testCumulativeProbabilitySuccess() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 2, 5);
        int success = 1;
        double result = dist.cumulativeProbability(success);
        double expected = dist.probability(success);
        assertEquals(expected, result, 1e-9);
    }

    @Test
    public void testCumulativeProbabilityFailure() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 2, 5);
        int failure = 2;
        double result = dist.cumulativeProbability(failure);
        double expected = dist.cumulativeProbability(success) + dist.probability(failure);
        assertEquals(expected, result, 1e-9);
    }

    @Test
    public void testProbabilityZero() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 2, 5);
        double result = dist.probability(0);
        assertEquals(0.0, result, 1e-9);
    }

    @Test
    public void testProbabilitySuccess() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 2, 5);
        int success = 1;
        double result = dist.probability(success);
        double expected = dist.cumulativeProbability(success + 1) - dist.cumulativeProbability(success);
        assertEquals(expected, result, 1e-9);
    }

    @Test
    public void testProbabilityFailure() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 2, 5);
        int failure = 2;
        double result = dist.probability(failure);
        double expected = dist.cumulativeProbability(success) - dist.cumulativeProbability(failure + 1);
        assertEquals(expected, result, 1e-9);
    }

    @Test
    public void testUpperCumulativeProbabilityZero() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 2, 5);
        double result = dist.upperCumulativeProbability(0);
        assertEquals(1.0, result, 1e-9);
    }

    @Test
    public void testUpperCumulativeProbabilitySuccess() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 2, 5);
        int success = 1;
        double result = dist.upperCumulativeProbability(success);
        double expected = dist.cumulativeProbability(success);
        assertEquals(expected, result, 1e-9);
    }

    @Test
    public void testUpperCumulativeProbabilityFailure() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 2, 5);
        int failure = 2;
        double result = dist.upperCumulativeProbability(failure);
        double expected = dist.cumulativeProbability(success);
        assertEquals(expected, result, 1e-9);
    }

    @Test
    public void testGetNumberOfSuccesses() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 2, 5);
        int expected = 2;
        int result = dist.getNumberOfSuccesses();
        assertEquals(expected, result);
    }

    @Test
    public void testGetPopulationSize() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 2, 5);
        int expected = 10;
        int result = dist.getPopulationSize();
        assertEquals(expected, result);
    }

    @Test
    public void testGetSampleSize() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 2, 5);
        int expected = 5;
        int result = dist.getSampleSize();
        assertEquals(expected, result);
    }

    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 2, 5);
        double expected = 2.0 * 5.0 / 10.0;
        double result = dist.getNumericalMean();
        assertEquals(expected, result, 1e-9);
    }

    @Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 2, 5);
        double expected = 5.0 * 2.0 * (10.0 - 5.0) * (10.0 - 2.0) / (10.0 * 10.0 * (10.0 - 1.0));
        double result = dist.getNumericalVariance();
        assertEquals(expected, result, 1e-9);
    }

    @Test
    public void testGetSupportLowerBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 2, 5);
        int expected = 5 + 2 - 10;
        int result = dist.getSupportLowerBound();
        assertEquals(expected, result);
    }

    @Test
    public void testGetSupportUpperBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 2, 5);
        int expected = 2;
        int result = dist.getSupportUpperBound();
        assertEquals(expected, result);
    }

    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 2, 5);
        boolean expected = true;
        boolean result = dist.isSupportConnected();
        assertEquals(expected, result);
    }

}