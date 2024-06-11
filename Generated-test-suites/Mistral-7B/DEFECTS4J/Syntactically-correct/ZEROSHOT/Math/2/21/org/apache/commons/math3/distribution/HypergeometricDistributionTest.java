import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for HypergeometricDistribution.
 */
public class HypergeometricDistributionTest {

    /**
     * Test constructor with valid arguments.
     * @throws NotPositiveException if population size, number of successes, or sample size is not positive.
     * @throws NumberIsTooLargeException if number of successes is larger than population size, or sample size is larger than population size.
     */
    @Test
    public void testConstructorValidArguments() throws NotPositiveException, NumberIsTooLargeException {
        final int populationSize = 10;
        final int numberOfSuccesses = 5;
        final int sampleSize = 5;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        Assert.assertEquals(populationSize, distribution.getPopulationSize());
        Assert.assertEquals(numberOfSuccesses, distribution.getNumberOfSuccesses());
        Assert.assertEquals(sampleSize, distribution.getSampleSize());
    }

    /**
     * Test constructor with invalid arguments.
     */
    @Test(expected = NotPositiveException.class)
    public void testConstructorInvalidPopulationSize() {
        new HypergeometricDistribution(0, 5, 5);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructorInvalidNumberOfSuccesses() {
        new HypergeometricDistribution(10, -1, 5);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructorInvalidSampleSize() {
        new HypergeometricDistribution(10, 5, -1);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorInvalidNumberOfSuccessesLargerThanPopulationSize() {
        new HypergeometricDistribution(10, 11, 5);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorInvalidSampleSizeLargerThanPopulationSize() {
        new HypergeometricDistribution(10, 5, 11);
    }

    /**
     * Test getDomain method.
     */
    @Test
    public void testGetDomain() {
        final int populationSize = 10;
        final int numberOfSuccesses = 5;
        final int sampleSize = 5;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int[] domain = distribution.getDomain();

        Assert.assertArrayEquals(new int[]{0, 5}, domain);
    }

    /**
     * Test getNumericalMean method.
     */
    @Test
    public void testGetNumericalMean() {
        final int populationSize = 10;
        final int numberOfSuccesses = 5;
        final int sampleSize = 5;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        double numericalMean = distribution.getNumericalMean();

        Assert.assertEquals(5.0, numericalMean, 0.001);
    }

    /**
     * Test getNumericalVariance method.
     */
    @Test
    public void testGetNumericalVariance() {
        final int populationSize = 10;
        final int numberOfSuccesses = 5;
        final int sampleSize = 5;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        double numericalVariance = distribution.getNumericalVariance();

        Assert.assertEquals(0.14285714285714285, numericalVariance, 0.0000001);
    }

    /**
     * Test probability method with valid argument.
     */
    @Test
    public void testProbabilityValidArgument() {
        final int populationSize = 10;
        final int numberOfSuccesses = 5;
        final int sampleSize = 5;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        double probability = distribution.probability(2);

        Assert.assertEquals(0.2132030599213701, probability, 0.0000001);
    }

    /**
     * Test probability method with invalid argument.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testProbabilityInvalidArgument() {
        final int populationSize = 10;
        final int numberOfSuccesses = 5;
        final int sampleSize = 5;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        distribution.probability(-1);
    }

    /**
     * Test cumulativeProbability method with valid argument.
     */
    @Test
    public void testCumulativeProbabilityValidArgument() {
        final int populationSize = 10;
        final int numberOfSuccesses = 5;
        final int sampleSize = 5;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        double cumulativeProbability = distribution.cumulativeProbability(2);

        Assert.assertEquals(0.4267969624547441, cumulativeProbability, 0.0000001);
    }

    /**
     * Test cumulativeProbability method with invalid argument.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testCumulativeProbabilityInvalidArgument() {
        final int populationSize = 10;
        final int numberOfSuccesses = 5;
        final int sampleSize = 5;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        distribution.cumulativeProbability(-1);
    }

    /**
     * Test upperCumulativeProbability method with valid argument.
     */
    @Test
    public void testUpperCumulativeProbabilityValidArgument() {
        final int populationSize = 10;
        final int numberOfSuccesses = 5;
        final int sampleSize = 5;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        double upperCumulativeProbability = distribution.upperCumulativeProbability(2);

        Assert.assertEquals(0.5732030375452558, upperCumulativeProbability, 0.0000001);
    }

    /**
     * Test upperCumulativeProbability method with invalid argument.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testUpperCumulativeProbabilityInvalidArgument() {
        final int populationSize = 10;
        final int numberOfSuccesses = 5;
        final int sampleSize = 5;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        distribution.upperCumulativeProbability(-1);
    }

    /**
     * Test getSupportLowerBound method.
     */
    @Test
    public void testGetSupportLowerBound() {
        final int populationSize = 10;
        final int numberOfSuccesses = 5;
        final int sampleSize = 5;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int supportLowerBound = distribution.getSupportLowerBound();

        Assert.assertEquals(0, supportLowerBound);
    }

    /**
     * Test getSupportUpperBound method.
     */
    @Test
    public void testGetSupportUpperBound() {
        final int populationSize = 10;
        final int numberOfSuccesses = 5;
        final int sampleSize = 5;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int supportUpperBound = distribution.getSupportUpperBound();

        Assert.assertEquals(5, supportUpperBound);
    }

    /**
     * Test isSupportConnected method.
     */
    @Test
    public void testIsSupportConnected() {
        final int populationSize = 10;
        final int numberOfSuccesses = 5;
        final int sampleSize = 5;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        boolean isSupportConnected = distribution.isSupportConnected();

        Assert.assertTrue(isSupportConnected);
    }

    /**
     * Test instantiation with random generator.
     * @throws NotPositiveException if population size, number of successes, or sample size is not positive.
     * @throws NumberIsTooLargeException if number of successes is larger than population size, or sample size is larger than population size.
     */
    @Test
    public void testInstantiationWithRandomGenerator() throws NotPositiveException, NumberIsTooLargeException {
        final int populationSize = 10;
        final int numberOfSuccesses = 5;
        final int sampleSize = 5;

        RandomGenerator rng = new Well19937c();

        HypergeometricDistribution distribution = new HypergeometricDistribution(rng, populationSize, numberOfSuccesses, sampleSize);

        Assert.assertEquals(populationSize, distribution.getPopulationSize());
        Assert.assertEquals(numberOfSuccesses, distribution.getNumberOfSuccesses());
        Assert.assertEquals(sampleSize, distribution.getSampleSize());
    }
}