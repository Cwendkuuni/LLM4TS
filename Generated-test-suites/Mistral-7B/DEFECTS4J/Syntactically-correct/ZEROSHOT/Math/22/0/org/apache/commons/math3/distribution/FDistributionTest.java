import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.RealDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.special.Beta;
import org.junit.Assert;
import org.junit.Test;

public class FDistributionTest {
    @Test
    public void testValidConstruction() throws NotStrictlyPositiveException {
        RealDistribution distribution = new FDistribution(3, 5);
        Assert.assertNotNull(distribution);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testInvalidConstructionWithNegativeDegreesOfFreedom() throws NotStrictlyPositiveException {
        new FDistribution(-3, 5);
    }

    @Test
    public void testDensity() {
        FDistribution distribution = new FDistribution(3, 5);
        double density = distribution.density(1);
        Assert.assertNotNull(density);
        Assert.assertEquals(density, 0.324857174822944, 0.00001);
    }

    @Test
    public void testCumulativeProbability() {
        FDistribution distribution = new FDistribution(3, 5);
        double cumulativeProbability = distribution.cumulativeProbability(0.5);
        Assert.assertEquals(cumulativeProbability, 0.3344888343857298, 0.00001);
    }

    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistribution distribution = new FDistribution(3, 5);
        double numeratorDegreesOfFreedom = distribution.getNumeratorDegreesOfFreedom();
        Assert.assertEquals(numeratorDegreesOfFreedom, 3, 0.0);
    }

    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistribution distribution = new FDistribution(3, 5);
        double denominatorDegreesOfFreedom = distribution.getDenominatorDegreesOfFreedom();
        Assert.assertEquals(denominatorDegreesOfFreedom, 5, 0.0);
    }

    @Test
    public void testGetNumericalMean() {
        FDistribution distribution = new FDistribution(3, 5);
        double numericalMean = distribution.getNumericalMean();
        Assert.assertEquals(numericalMean, 3.15, 0.01);
    }

    @Test
    public void testGetNumericalVariance() {
        FDistribution distribution = new FDistribution(3, 5);
        double numericalVariance = distribution.getNumericalVariance();
        Assert.assertEquals(numericalVariance, 1.361248121494662, 0.001);
    }

    @Test
    public void testGetSupportLowerBound() {
        FDistribution distribution = new FDistribution(3, 5);
        double supportLowerBound = distribution.getSupportLowerBound();
        Assert.assertEquals(supportLowerBound, 0, 0.0);
    }

    @Test
    public void testGetSupportUpperBound() {
        FDistribution distribution = new FDistribution(3, 5);
        double supportUpperBound = distribution.getSupportUpperBound();
        Assert.assertEquals(supportUpperBound, Double.POSITIVE_INFINITY, 0.0);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        FDistribution distribution = new FDistribution(3, 5);
        boolean isSupportLowerBoundInclusive = distribution.isSupportLowerBoundInclusive();
        Assert.assertTrue(isSupportLowerBoundInclusive);
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        FDistribution distribution = new FDistribution(3, 5);
        boolean isSupportUpperBoundInclusive = distribution.isSupportUpperBoundInclusive();
        Assert.assertFalse(isSupportUpperBoundInclusive);
    }

    @Test
    public void testIsSupportConnected() {
        FDistribution distribution = new FDistribution(3, 5);
        boolean isSupportConnected = distribution.isSupportConnected();
        Assert.assertTrue(isSupportConnected);
    }
}