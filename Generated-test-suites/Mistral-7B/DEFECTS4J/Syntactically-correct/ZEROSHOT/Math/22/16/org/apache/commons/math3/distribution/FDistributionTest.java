import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.special.Beta;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class FDistributionTest {

    private FDistribution distribution;

    @Before
    public void setUp() throws NotStrictlyPositiveException {
        distribution = new FDistribution(3.0, 4.0);
    }

    @Test
    public void testConstructor() {
        assertEquals(3.0, distribution.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(4.0, distribution.getDenominatorDegreesOfFreedom(), 0.0);
        assertEquals(0.0, distribution.getSupportLowerBound(), 0.0);
        assertEquals(Double.POSITIVE_INFINITY, distribution.getSupportUpperBound(), 0.0);
        assertFalse(distribution.isSupportLowerBoundInclusive());
        assertFalse(distribution.isSupportUpperBoundInclusive());
        assertFalse(distribution.isSupportConnected());
    }

    @Test
    public void testGetNumeratorDegreesOfFreedomAndDenominatorDegreesOfFreedom() {
        assertEquals(3.0, distribution.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(4.0, distribution.getDenominatorDegreesOfFreedom(), 0.0);
    }

    @Test
    public void testGetNumericalMean() {
        assertEquals(4.0 / 2.0, distribution.getNumericalMean(), 0.0);
    }

    @Test
    public void testGetNumericalVariance() {
        double variance = distribution.getNumericalVariance();
        assertTrue(variance > 0.0);
    }

    @Test
    public void testDensity() {
        for (double x = 0.0; x <= 10.0; x += 0.1) {
            double density = distribution.density(x);
            if (x == 1.0 || x == 2.0 || x == 3.0) {
                assertEquals(0.04812583734558225, density, 1e-6);
            } else {
                double expected = FastMath.exp(3.0 * FastMath.log(x) + 1.5 * FastMath.log(3.0) - 1.5 * FastMath.log(x + 4.0) - 1.5 * FastMath.log(x + 3.0) - Beta.logBeta(1.5, 1.5));
                assertEquals(expected, density, 1e-6);
            }
        }
    }

    @Test
    public void testCumulativeProbability() {
        for (double x = 0.0; x <= 5.0; x += 0.1) {
            double cdf = distribution.cumulativeProbability(x);
            double expected = 0.00861823884711962; // First 12 decimal digits of the exact value
            assertEquals(expected, cdf, 1e-8);
        }
    }

    @Test
    public void testCalculateNumericalVariance() {
        FDistribution distributionWithCalculatedVariance = new FDistribution(3.0, 4.0);
        double variance = distributionWithCalculatedVariance.getNumericalVariance();
        assertTrue(variance > 0.0);
        assertEquals(variance, distributionWithCalculatedVariance.calculateNumericalVariance(), 1e-8);
    }

}