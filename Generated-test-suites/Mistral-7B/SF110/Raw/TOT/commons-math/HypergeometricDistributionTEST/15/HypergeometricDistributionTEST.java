package org.apache.commons.math3.distribution;

import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Assert;
import org.junit.Test;

public class HypergeometricDistributionTest {

    @Test
    public void testHypergeometricDistribution_ValidParameters() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;

        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), populationSize, numberOfSuccesses, sampleSize);

        int[] domain = dist.getDomain();
        Assert.assertEquals(domain[0], 3);
        Assert.assertEquals(domain[1], 6);

        double mean = dist.getNumericalMean();
        Assert.assertEquals(mean, 5.0 * numberOfSuccesses / populationSize, 0.001);

        double variance = dist.getNumericalVariance();
        Assert.assertEquals(variance, 5.0 * numberOfSuccesses * (populationSize - numberOfSuccesses) / (populationSize * populationSize * (populationSize - 1.0)), 0.001);

        for (int i = domain[0]; i <= domain[1]; i++) {
            double prob = dist.probability(i);
            Assert.assertNotNull(prob);
            Assert.assertEquals(dist.cumulativeProbability(i), dist.cumulativeProbability(i - 1) + prob, 0.001);
        }
    }

    @Test(expected = NotPositiveException.class)
    public void testHypergeometricDistribution_NegativePopulationSize() {
        new HypergeometricDistribution(new Well19937c(), -5, 3, 5);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testHypergeometricDistribution_NonPositiveNumberOfSuccesses() {
        new HypergeometricDistribution(new Well19937c(), 5, 0, 5);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testHypergeometricDistribution_NonPositiveSampleSize() {
        new HypergeometricDistribution(new Well19937c(), 5, 3, 0);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testHypergeometricDistribution_NumberOfSuccessesLargerThanPopulationSize() {
        new HypergeometricDistribution(new Well19937c(), 5, 6, 5);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testHypergeometricDistribution_SampleSizeLargerThanPopulationSize() {
        new HypergeometricDistribution(new Well19937c(), 5, 3, 6);
    }

    @Test
    public void testHypergeometricDistribution_UpperCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);

        double prob = dist.upperCumulativeProbability(5);
        Assert.assertEquals(prob, 0.2058860667849853, 0.001);

        prob = dist.upperCumulativeProbability(6);
        Assert.assertEquals(prob, 0.3379310334252081, 0.001);
    }

    @Test
    public void testHypergeometricDistribution_LowerCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);

        double prob = dist.cumulativeProbability(3);
        Assert.assertEquals(prob, 0.0578687342041017, 0.001);

        prob = dist.cumulativeProbability(4);
        Assert.assertEquals(prob, 0.1157374684082034, 0.001);
    }

    @Test
    public void testHypergeometricDistribution_GetNumberOfSuccesses() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);

        int numberOfSuccesses = dist.getNumberOfSuccesses();
        Assert.assertEquals(numberOfSuccesses, 5);
    }

    @Test
    public void testHypergeometricDistribution_GetPopulationSize() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);

        int populationSize = dist.getPopulationSize();
        Assert.assertEquals(populationSize, 10);
    }

    @Test
    public void testHypergeometricDistribution_GetSampleSize() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);

        int sampleSize = dist.getSampleSize();
        Assert.assertEquals(sampleSize, 5);
    }
}