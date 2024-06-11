import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Assert;
import org.junit.Test;

public class HypergeometricDistributionTest {

    @Test
    public void testHypergeometricDistributionConstruction() {
        HypergeometricDistribution hd;
        try {
            hd = new HypergeometricDistribution(1, 1, 1);
            Assert.assertEquals(1, hd.getNumberOfSuccesses());
            Assert.assertEquals(1, hd.getPopulationSize());
            Assert.assertEquals(1, hd.getSampleSize());
        } catch (NotPositiveException | NotStrictlyPositiveException | NumberIsTooLargeException e) {
            // expected exceptions
        }

        try {
            hd = new HypergeometricDistribution(new Well19937c(), 1, 1, 1);
            Assert.assertEquals(1, hd.getNumberOfSuccesses());
            Assert.assertEquals(1, hd.getPopulationSize());
            Assert.assertEquals(1, hd.getSampleSize());
        } catch (NotPositiveException | NotStrictlyPositiveException | NumberIsTooLargeException e) {
            // expected exceptions
        }

        try {
            hd = new HypergeometricDistribution(new Well19937c(), 10, 5, 15);
            Assert.assertEquals(5, hd.getNumberOfSuccesses());
            Assert.assertEquals(10, hd.getPopulationSize());
            Assert.assertEquals(15, hd.getSampleSize());
        } catch (NotPositiveException | NotStrictlyPositiveException | NumberIsTooLargeException e) {
            // expected exceptions
        }
    }

    @Test
    public void testHypergeometricDistributionBasicFunctionality() {
        HypergeometricDistribution hd = null;
        try {
            hd = new HypergeometricDistribution(new Well19937c(), 10, 5, 15);
        } catch (Exception e) {
            // expected exception
        }

        Assert.assertEquals(5, hd.getNumberOfSuccesses());
        Assert.assertEquals(10, hd.getPopulationSize());
        Assert.assertEquals(15, hd.getSampleSize());

        Assert.assertEquals(0.0, hd.cumulativeProbability(0), 0.001);
        Assert.assertEquals(0.011, hd.cumulativeProbability(1), 0.001);
        Assert.assertEquals(0.123, hd.cumulativeProbability(3), 0.001);
        Assert.assertEquals(0.378, hd.cumulativeProbability(5), 0.001);
        Assert.assertEquals(0.592, hd.cumulativeProbability(7), 0.001);
        Assert.assertEquals(0.875, hd.cumulativeProbability(8), 0.001);
        Assert.assertEquals(0.989, hd.cumulativeProbability(10), 0.001);
        Assert.assertEquals(1.0, hd.cumulativeProbability(11), 0.001);
        Assert.assertEquals(1.0, hd.cumulativeProbability(12), 0.001);
        Assert.assertEquals(1.0, hd.cumulativeProbability(14), 0.001);
        Assert.assertEquals(1.0, hd.cumulativeProbability(15), 0.001);

        Assert.assertEquals(1.0 / 15.0, hd.probability(0), 0.001);
        Assert.assertEquals(5.0 / 15.0, hd.probability(1), 0.001);
        Assert.assertEquals(10.0 / 15.0, hd.probability(2), 0.001);
        Assert.assertEquals(10.0 / 15.0, hd.probability(3), 0.001);
        Assert.assertEquals(5.0 / 15.0, hd.probability(4), 0.001);
        Assert.assertEquals(1.0 / 16.0, hd.probability(5), 0.001);
        Assert.assertEquals(1.0 / 16.0, hd.probability(6), 0.001);
        Assert.assertEquals(1.0 / 16.0, hd.probability(7), 0.001);
        Assert.assertEquals(1.0 / 16.0, hd.probability(8), 0.001);
        Assert.assertEquals(1.0 / 16.0, hd.probability(9), 0.001);
        Assert.assertEquals(1.0 / 16.0, hd.probability(10), 0.001);
        Assert.assertEquals(1.0 / 16.0, hd.probability(11), 0.001);
        Assert.assertEquals(1.0 / 16.0, hd.probability(12), 0.001);
        Assert.assertEquals(1.0 / 16.0, hd.probability(13), 0.001);
        Assert.assertEquals(1.0 / 16.0, hd.probability(14), 0.001);
        Assert.assertEquals(1.0 / 16.0, hd.probability(15), 0.001);

        Assert.assertEquals(0.0, hd.upperCumulativeProbability(0), 0.001);
        Assert.assertEquals(0.123, hd.upperCumulativeProbability(3), 0.001);
        Assert.assertEquals(0.378, hd.upperCumulativeProbability(5), 0.001);
        Assert.assertEquals(0.592, hd.upperCumulativeProbability(7), 0.001);
        Assert.assertEquals(0.875, hd.upperCumulativeProbability(8), 0.001);
        Assert.assertEquals(0.989, hd.upperCumulativeProbability(10), 0.001);
        Assert.assertEquals(1.0, hd.upperCumulativeProbability(11), 0.001);
        Assert.assertEquals(1.0, hd.upperCumulativeProbability(12), 0.001);
        Assert.assertEquals(1.0, hd.upperCumulativeProbability(13), 0.001);
        Assert.assertEquals(1.0, hd.upperCumulativeProbability(14), 0.001);
        Assert.assertEquals(1.0, hd.upperCumulativeProbability(15), 0.001);
    }

    @Test(expected = AssertionError.class)
    public void testHypergeometricDistributionOutOfBounds() {
        HypergeometricDistribution hd = new HypergeometricDistribution(new Well19937c(), 1, 1, 1);
        hd.cumulativeProbability(-1);
    }

    @Test(expected = AssertionError.class)
    public void testHypergeometricDistributionOutOfBounds2() {
        HypergeometricDistribution hd = new HypergeometricDistribution(new Well19937c(), 1, 1, 1);
        hd.probability(2);
    }

    @Test
    public void testHypergeometricDistributionGetters() {
        HypergeometricDistribution hd = new HypergeometricDistribution(new Well19937c(), 10, 5, 15);
        Assert.assertEquals(10, hd.getPopulationSize());
        Assert.assertEquals(5, hd.getNumberOfSuccesses());
        Assert.assertEquals(15, hd.getSampleSize());
    }

    @Test
    public void testHypergeometricDistributionNumericalMean() {
        HypergeometricDistribution hd = new HypergeometricDistribution(new Well19937c(), 10, 5, 15);
        Assert.assertEquals(5.0, hd.getNumericalMean(), 0.001);
    }

    @Test
    public void testHypergeometricDistributionNumericalVariance() {
        HypergeometricDistribution hd = new HypergeometricDistribution(new Well19937c(), 10, 5, 15);
        Assert.assertEquals(0.13888888888888888, hd.getNumericalVariance(), 0.0001);
    }
}