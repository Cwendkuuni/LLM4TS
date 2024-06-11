import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.RealDistribution;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.special.Beta;
import org.junit.Assert;
import org.junit.Test;

public class FDistributionTest {

    @Test
    public void testFDistributionConstructor() {
        double numeratorDegreesOfFreedom = 3.0;
        double denominatorDegreesOfFreedom = 5.0;
        FDistribution fDist = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        Assert.assertEquals(numeratorDegreesOfFreedom, fDist.getNumeratorDegreesOfFreedom(), 0.001);
        Assert.assertEquals(denominatorDegreesOfFreedom, fDist.getDenominatorDegreesOfFreedom(), 0.001);
    }

    @Test
    public void testFDistributionDensity() {
        double numeratorDegreesOfFreedom = 3.0;
        double denominatorDegreesOfFreedom = 5.0;
        FDistribution fDist = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);

        double x = 1.5;
        double expectedValue = 0.238251280161;
        double actualValue = fDist.density(x);
        Assert.assertEquals(expectedValue, actualValue, 0.001);
    }

    @Test
    public void testFDistributionCumulativeProbability() {
        double numeratorDegreesOfFreedom = 3.0;
        double denominatorDegreesOfFreedom = 5.0;
        FDistribution fDist = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);

        double x = 1.5;
        double expectedValue = 0.918065594139;
        double actualValue = fDist.cumulativeProbability(x);
        Assert.assertEquals(expectedValue, actualValue, 0.001);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFDistributionNotStrictlyPositiveException() {
        double numeratorDegreesOfFreedom = -2.0;
        double denominatorDegreesOfFreedom = 5.0;
        FDistribution fDist = null;
        try {
            fDist = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        } catch (NotStrictlyPositiveException e) {
            Assert.assertEquals("degrees of freedom must be positive: " + numeratorDegreesOfFreedom, e.getMessage());
        }
        Assert.assertNull(fDist);
    }

    @Test
    public void testFDistributionNumericalMean() {
        double numeratorDegreesOfFreedom = 3.0;
        double denominatorDegreesOfFreedom = 5.0;
        FDistribution fDist = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        double expectedValue = 3.0 / 2.0;
        double actualValue = fDist.getNumericalMean();
        Assert.assertEquals(expectedValue, actualValue, 0.001);
    }

    @Test
    public void testFDistributionNumericalVariance() {
        double numeratorDegreesOfFreedom = 3.0;
        double denominatorDegreesOfFreedom = 5.0;
        FDistribution fDist = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        double expectedValue = 2.21825081531;
        double actualValue = fDist.getNumericalVariance();
        Assert.assertEquals(expectedValue, actualValue, 0.001);
    }

    @Test
    public void testFDistributionGenerateRandomNumbers() {
        double numeratorDegreesOfFreedom = 3.0;
        double denominatorDegreesOfFreedom = 5.0;
        RandomGenerator rng = new Well19937c();
        FDistribution fDist = new FDistribution(rng, numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);

        int numberOfSamples = 10_000;
        double sum = 0;

        for (int i = 0; i < numberOfSamples; i++) {
            sum += fDist.inverseCumulativeProbability(rng.nextDouble());
        }

        double expectedValue = numberOfSamples * fDist.getNumericalMean();
        double actualValue = sum;
        Assert.assertEquals(expectedValue, actualValue, 0.01);
    }
}