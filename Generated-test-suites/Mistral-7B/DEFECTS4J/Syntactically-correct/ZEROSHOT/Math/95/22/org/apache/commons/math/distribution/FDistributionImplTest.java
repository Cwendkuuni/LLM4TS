import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.FDistributionImpl;
import org.apache.commons.math3.special.Beta;
import org.junit.Assert;
import org.junit.Test;

public class FDistributionImplTest {

    @Test
    public void testFDistributionImpl() throws MathException {
        FDistribution distribution = new FDistributionImpl(2.0, 3.0);
        Assert.assertEquals(2.0, distribution.getNumeratorDegreesOfFreedom(), 0.0);
        Assert.assertEquals(3.0, distribution.getDenominatorDegreesOfFreedom(), 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeNumeratorDegreesOfFreedom() {
        new FDistributionImpl(-1.0, 3.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeDenominatorDegreesOfFreedom() {
        new FDistributionImpl(2.0, -1.0);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        FDistribution distribution = new FDistributionImpl(2.0, 3.0);
        double x = 1.5;
        double expected = Beta.regularizedBeta((2.0 * x) / (3.0 + 2.0 * x),
                0.5 * 2.0, 0.5 * 3.0);
        double actual = distribution.cumulativeProbability(x);
        Assert.assertEquals(expected, actual, 1e-12);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        FDistribution distribution = new FDistributionImpl(2.0, 3.0);
        double p = 0.95;
        double x = distribution.inverseCumulativeProbability(p);
        double expected = 2.2435315479397886; // calculated using Wolfram Alpha
        Assert.assertEquals(expected, x, 1e-6);
    }
}