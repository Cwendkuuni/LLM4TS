import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.RealDistribution;
import org.apache.commons.math3.special.Beta;
import org.junit.Assert;
import org.junit.Test;

public class FDistributionTest {

    @Test
    public void testFDistribution() {
        double numeratorDegreesOfFreedom = 3.0;
        double denominatorDegreesOfFreedom = 5.0;

        RealDistribution distribution = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);

        Assert.assertEquals(numeratorDegreesOfFreedom, distribution.getNumeratorDegreesOfFreedom(), 0.0);
        Assert.assertEquals(denominatorDegreesOfFreedom, distribution.getDenominatorDegreesOfFreedom(), 0.0);
        Assert.assertEquals(Double.POSITIVE_INFINITY, distribution.getSupportUpperBound(), 0.0);
        Assert.assertEquals(0.0, distribution.getSupportLowerBound(), 0.0);
        Assert.assertTrue(distribution.isSupportConnected());

        Assert.assertEquals(Double.NaN, distribution.getNumericalMean(), 0.0);
        Assert.assertEquals(Double.NaN, distribution.getNumericalVariance(), 0.0);

        for (int i = 0; i < 10; i++) {
            double sample = distribution.inverseCumulativeProbability(FastMath.nextGaussian(0.0, 1.0));
            System.out.println(sample);
            Assert.assertTrue(sample > 0.0);
        }

        for (int i = 0; i < 10000; i++) {
            double x = distribution.sample();
            double pdf = distribution.density(x);
            double cumulativeProbability = distribution.cumulativeProbability(x);
            Assert.assertTrue(x > 0.0);
            Assert.assertTrue(pdf > 0.0);
            Assert.assertEquals(cumulativeProbability, distribution.cumulativeProbability(x), 0.001);
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeDegreesOfFreedom() {
        double numeratorDegreesOfFreedom = -1.0;
        double denominatorDegreesOfFreedom = 5.0;

        new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testZeroDegreesOfFreedom() {
        double numeratorDegreesOfFreedom = 0.0;
        double denominatorDegreesOfFreedom = 5.0;

        new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeDenominatorDegreesOfFreedom() {
        double numeratorDegreesOfFreedom = 5.0;
        double denominatorDegreesOfFreedom = -1.0;

        new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testZeroDenominatorDegreesOfFreedom() {
        double numeratorDegreesOfFreedom = 5.0;
        double denominatorDegreesOfFreedom = 0.0;

        new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
    }
}