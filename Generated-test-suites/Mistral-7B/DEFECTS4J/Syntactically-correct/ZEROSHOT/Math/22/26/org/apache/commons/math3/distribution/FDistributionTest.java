import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.special.Beta;
import org.junit.Assert;
import org.junit.Test;

import static org.apache.commons.math3.util.Precision.abs;
import static org.apache.commons.math3.util.Precision.equalTo;

/**
 * Test class for FDistribution.
 */
public class FDistributionTest {

    /**
     * Test constructor.
     * @throws NotStrictlyPositiveException
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorNegativeDegreesOfFreedom() throws NotStrictlyPositiveException {
        new FDistribution(-1.0, 1.0);
    }

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        FDistribution fd = new FDistribution(1.0, 2.0);
        Assert.assertEquals(1.0, fd.getNumeratorDegreesOfFreedom(), 0.0);
        Assert.assertEquals(2.0, fd.getDenominatorDegreesOfFreedom(), 0.0);
    }

    /**
     * Test density method.
     */
    @Test
    public void testDensity() {
        FDistribution fd = new FDistribution(1.0, 2.0);
        double x = 1.5;
        double density = fd.density(x);
        double expected = FastMath.exp(
                -FastMath.log(2.0) + FastMath.log(x) +
                FastMath.log(1.0) - FastMath.log(x + 1.0) +
                FastMath.log(Beta.LOG_BETA) - 1.0);

        Assert.assertEquals(expected, density, 1e-10);
    }

    /**
     * Test cumulativeProbability method.
     */
    @Test
    public void testCumulativeProbability() {
        FDistribution fd = new FDistribution(1.0, 2.0);
        double x = 1.5;
        double cdf = fd.cumulativeProbability(x);
        double betaResult = Beta.regularizedBeta(1.5, 0.5, 1.0);

        Assert.assertEquals(betaResult, cdf, 1e-10);
    }

    /**
     * Test method getNumericalMean.
     */
    @Test
    public void testGetNumericalMean() {
        FDistribution fd = new FDistribution(1.0, 2.0);
        double numericalMean = fd.getNumericalMean();
        double expected = 0.75;

        Assert.assertEquals(expected, numericalMean, 1e-10);
    }

    /**
     * Test method getNumericalVariance.
     */
    @Test
    public void testGetNumericalVariance() {
        FDistribution fd = new FDistribution(1.0, 2.0);
        double numericalVariance = fd.getNumericalVariance();
        double expected = 0.585;

        Assert.assertEquals(expected, numericalVariance, 1e-10);
    }
}