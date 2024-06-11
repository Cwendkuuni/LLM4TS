import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.RealDistribution;
import org.apache.commons.math3.special.Beta;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FDistributionTest {

    @Test
    public void testFDistributionCreation() {
        double numeratorDegreesOfFreedom = 5.0;
        double denominatorDegreesOfFreedom = 10.0;
        RealDistribution distribution = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        assertEquals(numeratorDegreesOfFreedom, distribution.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(denominatorDegreesOfFreedom, distribution.getDenominatorDegreesOfFreedom(), 0.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFDistributionCreationNegativeNumeratorDegreesOfFreedom() {
        double numeratorDegreesOfFreedom = -2.0;
        double denominatorDegreesOfFreedom = 10.0;
        FDistribution distribution = null;
        try {
            distribution = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        } catch (NotStrictlyPositiveException e) {
            distribution = e.getInstance();
        }
        assertEquals(0.0, distribution.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(denominatorDegreesOfFreedom, distribution.getDenominatorDegreesOfFreedom(), 0.0);
        assertTrue(distribution.isSupportLowerBoundInclusive());
        assertTrue(distribution.isSupportUpperBoundInclusive());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFDistributionCreationNegativeDenominatorDegreesOfFreedom() {
        double numeratorDegreesOfFreedom = 10.0;
        double denominatorDegreesOfFreedom = -2.0;
        FDistribution distribution = null;
        try {
            distribution = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        } catch (NotStrictlyPositiveException e) {
            distribution = e.getInstance();
        }
        assertEquals(numeratorDegreesOfFreedom, distribution.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(0.0, distribution.getDenominatorDegreesOfFreedom(), 0.0);
        assertTrue(distribution.isSupportLowerBoundInclusive());
        assertTrue(distribution.isSupportUpperBoundInclusive());
    }

    @Test
    public void testFDistributionDensity() {
        double numeratorDegreesOfFreedom = 5.0;
        double denominatorDegreesOfFreedom = 10.0;
        RealDistribution distribution = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        double x = 1.0;
        double density = distribution.density(x);
        double expectedDensity = Beta.beta(numeratorDegreesOfFreedom / 2, denominatorDegreesOfFreedom / 2) /
                (Beta.beta(numeratorDegreesOfFreedom / 2, denominatorDegreesOfFreedom / 2 + x) *
                        Math.pow(denominatorDegreesOfFreedom, x) *
                        Math.pow(numeratorDegreesOfFreedom, x));
        assertEquals(expectedDensity, density, 0.0001);
    }

    @Test
    public void testFDistributionCumulativeProbability() {
        double numeratorDegreesOfFreedom = 5.0;
        double denominatorDegreesOfFreedom = 10.0;
        RealDistribution distribution = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        double x = 2.5;
        double cumulativeProbability = distribution.cumulativeProbability(x);
        double expectedCumulativeProbability = Beta.regularizedBeta(
                (numeratorDegreesOfFreedom * x) / (denominatorDegreesOfFreedom + numeratorDegreesOfFreedom * x),
                0.5 * numeratorDegreesOfFreedom, 0.5 * denominatorDegreesOfFreedom);
        assertEquals(expectedCumulativeProbability, cumulativeProbability, 0.0001);
    }

    @Test
    public void testFDistributionMean() {
        double numeratorDegreesOfFreedom = 5.0;
        double denominatorDegreesOfFreedom = 7.0;
        RealDistribution distribution = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        double mean = distribution.getNumericalMean();
        assertEquals(denominatorDegreesOfFreedom / 2.0, mean, 0.0001);
    }

    @Test
    public void testFDistributionVariance() {
        double numeratorDegreesOfFreedom = 5.0;
        double denominatorDegreesOfFreedom = 7.0;
        RealDistribution distribution = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        double variance = distribution.getNumericalVariance();
        if (denominatorDegreesOfFreedom > 4.0) {
            double expectedVariance = (2 * Math.pow(denominatorDegreesOfFreedom, 2) *
                    (numeratorDegreesOfFreedom + denominatorDegreesOfFreedom - 2)) /
                    ((numeratorDegreesOfFreedom * (denominatorDegreesOfFreedom - 2) *
                            (denominatorDegreesOfFreedom - 4)));
            assertEquals(expectedVariance, variance, 0.0001);
        } else {
            assertTrue(Double.isNaN(variance));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFDistributionInverseCumulativeProbabilityNegativeX() {
        double numeratorDegreesOfFreedom = 5.0;
        double denominatorDegreesOfFreedom = 10.0;
        RealDistribution distribution = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        distribution.inverseCumulativeProbability(-1.0);
    }

    @Test
    public void testFDistributionInverseCumulativeProbability() {
        double numeratorDegreesOfFreedom = 5.0;
        double denominatorDegreesOfFreedom = 10.0;
        RealDistribution distribution = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        double inverseCumulativeProbability = distribution.inverseCumulativeProbability(0.95);
        double x = FastMath.quantile(distribution, 0.95);
        assertEquals(x, inverseCumulativeProbability, 0.0001);
    }
}