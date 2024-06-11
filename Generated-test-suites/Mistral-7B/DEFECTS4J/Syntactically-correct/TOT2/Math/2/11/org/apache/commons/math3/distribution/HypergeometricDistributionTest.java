import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.distribution.HypergeometricDistributionTest;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Assert;

public class HypergeometricDistributionTest {

    @org.junit.Test
    public void testConstructHypergeometricDistribution() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;

        HypergeometricDistribution hd = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        Assert.assertEquals(populationSize, hd.getPopulationSize());
        Assert.assertEquals(numberOfSuccesses, hd.getNumberOfSuccesses());
        Assert.assertEquals(sampleSize, hd.getSampleSize());
    }

    @org.junit.Test(expected = NotPositiveException.class)
    public void testConstructNegativeNumberOfSuccesses() {
        int populationSize = 10;
        int numberOfSuccesses = -3;
        int sampleSize = 5;

        HypergeometricDistribution hd = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @org.junit.Test(expected = NotStrictlyPositiveException.class)
    public void testConstructZeroPopulationSize() {
        int populationSize = 0;
        int numberOfSuccesses = 3;
        int sampleSize = 5;

        HypergeometricDistribution hd = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @org.junit.Test(expected = NumberIsTooLargeException.class)
    public void testConstructNumberOfSuccessesGreaterThanPopulationSize() {
        int populationSize = 5;
        int numberOfSuccesses = 6;
        int sampleSize = 2;

        HypergeometricDistribution hd = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @org.junit.Test(expected = NumberIsTooLargeException.class)
    public void testConstructSampleSizeGreaterThanPopulationSize() {
        int populationSize = 5;
        int numberOfSuccesses = 3;
        int sampleSize = 6;

        HypergeometricDistribution hd = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @org.junit.Test
    public void testCumulativeProbability() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;

        HypergeometricDistribution hd = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        double cumulativeProbability = hd.cumulativeProbability(0);
        Assert.assertEquals(0.0, cumulativeProbability, 0.001);

        cumulativeProbability = hd.cumulativeProbability(1);
        Assert.assertEquals(0.04545454545454545, cumulativeProbability, 0.001);

        cumulativeProbability = hd.cumulativeProbability(3);
        Assert.assertEquals(0.6279069767441865, cumulativeProbability, 0.001);

        cumulativeProbability = hd.cumulativeProbability(5);
        Assert.assertEquals(1.0, cumulativeProbability, 0.001);
    }

    @org.junit.Test
    public void testProbability() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;

        HypergeometricDistribution hd = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        double probability = hd.probability(0);
        Assert.assertEquals(0.04545454545454545, probability, 0.001);

        probability = hd.probability(1);
        Assert.assertEquals(0.1111111111111111, probability, 0.001);

        probability = hd.probability(3);
        Assert.assertEquals(0.3225806451612903, probability, 0.001);

        probability = hd.probability(5);
        Assert.assertEquals(0.04545454545454545, probability, 0.001);
    }

    @org.junit.Test
    public void testUpperCumulativeProbability() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;

        HypergeometricDistribution hd = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        double upperCumulativeProbability = hd.upperCumulativeProbability(0);
        Assert.assertEquals(1.0, upperCumulativeProbability, 0.001);

        upperCumulativeProbability = hd.upperCumulativeProbability(1);
        Assert.assertEquals(0.8545454545454546, upperCumulativeProbability, 0.001);

        upperCumulativeProbability = hd.upperCumulativeProbability(3);
        Assert.assertEquals(0.6279069767441865, upperCumulativeProbability, 0.001);

        upperCumulativeProbability = hd.upperCumulativeProbability(5);
        Assert.assertEquals(0.4545454545454546, upperCumulativeProbability, 0.001);
    }

    @org.junit.Test
    public void testGetNumberOfSuccesses() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;

        HypergeometricDistribution hd = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int actualNumberOfSuccesses = hd.getNumberOfSuccesses();
        Assert.assertEquals(numberOfSuccesses, actualNumberOfSuccesses);
    }

    @org.junit.Test
    public void testGetPopulationSize() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;

        HypergeometricDistribution hd = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int actualPopulationSize = hd.getPopulationSize();
        Assert.assertEquals(populationSize, actualPopulationSize);
    }

    @org.junit.Test
    public void testGetSampleSize() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;

        HypergeometricDistribution hd = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int actualSampleSize = hd.getSampleSize();
        Assert.assertEquals(sampleSize, actualSampleSize);
    }

    @org.junit.Test
    public void testGetNumericalMean() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;

        HypergeometricDistribution hd = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        double numericalMean = hd.getNumericalMean();
        Assert.assertEquals(3.5, numericalMean, 0.001);
    }

    @org.junit.Test
    public void testGetNumericalVariance() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;

        HypergeometricDistribution hd = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        double numericalVariance = hd.getNumericalVariance();
        Assert.assertEquals(2.338356164589417, numericalVariance, 0.001);
    }

    @org.junit.Test
    public void testGetSupportLowerBound() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;

        HypergeometricDistribution hd = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int supportLowerBound = hd.getSupportLowerBound();
        Assert.assertEquals(2, supportLowerBound);
    }

    @org.junit.Test
    public void testGetSupportUpperBound() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;

        HypergeometricDistribution hd = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int supportUpperBound = hd.getSupportUpperBound();
        Assert.assertEquals(3, supportUpperBound);
    }

    @org.junit.Test
    public void testIsSupportConnected() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;

        HypergeometricDistribution hd = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        boolean isSupportConnected = hd.isSupportConnected();
        Assert.assertTrue(isSupportConnected);
    }
}