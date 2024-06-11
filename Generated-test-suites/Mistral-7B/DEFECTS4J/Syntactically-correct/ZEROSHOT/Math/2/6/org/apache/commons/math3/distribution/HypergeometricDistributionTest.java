import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest {

    @Test
    public void testHypergeometricDistribution() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        RandomGenerator rng = new Well19937c();
        HypergeometricDistribution dist = new HypergeometricDistribution(rng, 10, 5, 5);

        int[] expectedDomain = {5, 5};
        int[] actualDomain = dist.getDomain();
        assertArrayEquals(expectedDomain, actualDomain);

        double expectedNumericalMean = 5.0;
        double actualNumericalMean = dist.getNumericalMean();
        assertEquals(expectedNumericalMean, actualNumericalMean, 0.001);

        double expectedNumericalVariance = 1.23606797;
        double actualNumericalVariance = dist.getNumericalVariance();
        assertEquals(expectedNumericalVariance, actualNumericalVariance, 0.001);

        int x = 5;
        double expectedCumulativeProbability = 0.96925375;
        double actualCumulativeProbability = dist.cumulativeProbability(x);
        assertEquals(expectedCumulativeProbability, actualCumulativeProbability, 0.001);

        double expectedProbability = 0.17138983;
        double actualProbability = dist.probability(x);
        assertEquals(expectedProbability, actualProbability, 0.001);

        int xMinusOne = 4;
        double expectedProbabilityMinusOne = 0.18749997;
        double actualProbabilityMinusOne = dist.probability(xMinusOne);
        assertEquals(expectedProbabilityMinusOne, actualProbabilityMinusOne, 0.001);

        int xPlusOne = 6;
        double expectedProbabilityPlusOne = 0.08861003;
        double actualProbabilityPlusOne = dist.probability(xPlusOne);
        assertEquals(expectedProbabilityPlusOne, actualProbabilityPlusOne, 0.001);

        double expectedUpperCumulativeProbability = 0.03074625;
        double actualUpperCumulativeProbability = dist.upperCumulativeProbability(x);
        assertEquals(expectedUpperCumulativeProbability, actualUpperCumulativeProbability, 0.001);

        int xMinusOneUpper = 4;
        double expectedUpperCumulativeProbabilityMinusOne = 0.03125375;
        double actualUpperCumulativeProbabilityMinusOne = dist.upperCumulativeProbability(xMinusOne);
        assertEquals(expectedUpperCumulativeProbabilityMinusOne, actualUpperCumulativeProbabilityMinusOne, 0.001);

        int xPlusOneLower = 6;
        double expectedUpperCumulativeProbabilityPlusOne = 0.03124625;
        double actualUpperCumulativeProbabilityPlusOne = dist.upperCumulativeProbability(xPlusOne);
        assertEquals(expectedUpperCumulativeProbabilityPlusOne, actualUpperCumulativeProbabilityPlusOne, 0.001);
    }

    @Test(expected = NotPositiveException.class)
    public void testNegativeNumberOfSuccesses() throws NotPositiveException {
        RandomGenerator rng = new Well19937c();
        int populationSize = 10;
        int numberOfSuccesses = -5;
        int sampleSize = 5;

        try {
            new HypergeometricDistribution(rng, populationSize, numberOfSuccesses, sampleSize);
            fail();
        } catch (NotPositiveException e) {
            // Expected exception
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testZeroPopulationSize() throws NotPositiveException, NotStrictlyPositiveException {
        RandomGenerator rng = new Well19937c();
        int populationSize = 0;
        int numberOfSuccesses = 5;
        int sampleSize = 5;

        try {
            new HypergeometricDistribution(rng, populationSize, numberOfSuccesses, sampleSize);
            fail();
        } catch (NotStrictlyPositiveException e) {
            // Expected exception
        }
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testNumberOfSuccessesGreaterThanPopulationSize() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        RandomGenerator rng = new Well19937c();
        int populationSize = 10;
        int numberOfSuccesses = 11;
        int sampleSize = 5;

        try {
            new HypergeometricDistribution(rng, populationSize, numberOfSuccesses, sampleSize);
            fail();
        } catch (NumberIsTooLargeException e) {
            // Expected exception
        }
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testSampleSizeGreaterThanPopulationSize() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        RandomGenerator rng = new Well19937c();
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 11;

        try {
            new HypergeometricDistribution(rng, populationSize, numberOfSuccesses, sampleSize);
            fail();
        } catch (NumberIsTooLargeException e) {
            // Expected exception
        }
    }
}