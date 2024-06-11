import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.RealDistribution;
import org.apache.commons.math3.special.Beta;
import org.junit.Assert;
import org.junit.Test;

import static org.apache.commons.math3.MathConstants.EPSILON;
import static org.apache.commons.math3.MathConstants.PI;

public class FDistributionTest {

    @Test
    public void testFDistributionConstruction() {
        try {
            FDistribution dist = new FDistribution(1.0, 1.0);
            Assert.assertEquals(1.0, dist.getNumeratorDegreesOfFreedom(), EPSILON);
            Assert.assertEquals(1.0, dist.getDenominatorDegreesOfFreedom(), EPSILON);
        } catch (NotStrictlyPositiveException e) {
            Assert.fail("Expected no exception.");
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFDistributionNegativeDegreesOfFreedom() {
        new FDistribution(-1.0, 1.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFDistributionZeroDegreesOfFreedom() {
        new FDistribution(0.0, 1.0);
    }

    @Test
    public void testFDistributionDensity() {
        FDistribution dist = new FDistribution(2.0, 6.0);
        double expected = 0.002222415939156356;
        double actual = dist.density(1.5);
        Assert.assertEquals(expected, actual, EPSILON);
    }

    @Test
    public void testFDistributionCumulativeProbability() {
        FDistribution dist = new FDistribution(2.0, 6.0);
        double expected = 0.957232114394685;
        double actual = dist.cumulativeProbability(3.0);
        Assert.assertEquals(expected, actual, EPSILON);
    }

    @Test
    public void testFDistributionMean() {
        FDistribution dist = new FDistribution(2.0, 6.0);
        double expected = 3.0 / 5.0;
        double actual = dist.getNumericalMean();
        Assert.assertEquals(expected, actual, EPSILON);
    }

    @Test
    public void testFDistributionVariance() {
        FDistribution dist = new FDistribution(2.0, 6.0);
        double expected = 12.0 / 30.0;
        double actual = dist.getNumericalVariance();
        Assert.assertEquals(expected, actual, EPSILON);
    }

    @Test
    public void testFDistributionSupport() {
        FDistribution dist = new FDistribution(1.0, 1.0);
        Assert.assertEquals(0.0, dist.getSupportLowerBound(), EPSILON);
        Assert.assertEquals(Double.POSITIVE_INFINITY, dist.getSupportUpperBound(), EPSILON);
        Assert.assertTrue(dist.isSupportLowerBoundInclusive());
        Assert.assertFalse(dist.isSupportUpperBoundInclusive());
        Assert.assertTrue(dist.isSupportConnected());
    }

    @Test
    public void testFDistributionRandomSample() {
        FDistribution dist = new FDistribution(2.0, 6.0);
        RandomGenerator rng = new Well19937c();
        RealDistribution distSample = new FDistribution(rng, 2.0, 6.0);
        double expectedMean = 3.0 / 5.0;
        double expectedVariance = 12.0 / 30.0;
        double mean = 0.0;
        double variance = 0.0;
        for (int i = 0; i < 10000; i++) {
            double sample = distSample.inverseCumulativeProbability(rng.nextUniform());
            mean += sample;
        }
        mean /= 10000.0;
        for (int i = 0; i < 10000; i++) {
            double sample1 = distSample.inverseCumulativeProbability(rng.nextUniform());
            double sample2 = distSample.inverseCumulativeProbability(rng.nextUniform());
            variance += FastMath.pow(sample1 - mean, 2);
        }
        variance /= 9998.0;
        Assert.assertEquals(expectedMean, mean, EPSILON);
        Assert.assertEquals(expectedVariance, variance, EPSILON);
    }

    @Test
    public void testFDistributionInverseCumulativeProbability() {
        FDistribution dist = new FDistribution(2.0, 6.0);
        double p = 0.95;
        double expected = 3.15563696120234;
        double actual = dist.inverseCumulativeProbability(p);
        Assert.assertEquals(expected, actual, EPSILON);
    }

    @Test
    public void testFDistributionInverseCumulativeProbabilityWithLargeP() {
        FDistribution dist = new FDistribution(1.0, 1.0);
        double p = 1.0;
        double expected = Double.POSITIVE_INFINITY;
        double actual = dist.inverseCumulativeProbability(p);
        Assert.assertEquals(expected, actual, EPSILON);
    }
}