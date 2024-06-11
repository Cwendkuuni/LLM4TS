import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

public class FDistributionTest {

    @Test
    public void testFDistributionCreation() throws NotStrictlyPositiveException {
        FDistribution dist = new FDistribution(2.0, 3.0);
        assertEquals(2.0, dist.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(3.0, dist.getDenominatorDegreesOfFreedom(), 0.0);
        assertNotNull(dist.getRandomGenerator());
    }

    @Test
    public void testFDistributionCreationWithSeededGenerator() throws NotStrictlyPositiveException {
        FDistribution dist = new FDistribution(new Well19937c(1234L), 2.0, 3.0);
        assertEquals(2.0, dist.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(3.0, dist.getDenominatorDegreesOfFreedom(), 0.0);
        assertNotSame(new Well19937c(1234L), dist.getRandomGenerator());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFDistributionCreationWithNegativeParameters() throws NotStrictlyPositiveException {
        new FDistribution(-2.0, 3.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFDistributionCreationWithNegativeNumeratorDegreesOfFreedom() throws NotStrictlyPositiveException {
        new FDistribution(2.0, -3.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFDistributionCreationWithZeroParameters() throws NotStrictlyPositiveException {
        new FDistribution(0.0, 3.0);
    }

    @Test
    public void testFDistributionDensity() {
        FDistribution dist = new FDistribution(2.0, 3.0);
        double[] xValues = new double[]{-1.0, 0.0, 1.0, 2.0, 3.0};

        for (double x : xValues) {
            double density = dist.density(x);
            if (x < 0) {
                assertEquals(0.0, density, 1e-10);
            } else {
                assertTrue(density > 0);
            }
        }
    }

    @Test
    public void testFDistributionCumulativeProbability() {
        FDistribution dist = new FDistribution(2.0, 3.0);
        double[] xValues = new double[]{0.0, 1.0, 2.0, 3.0, 4.0, 5.0};

        for (double x : xValues) {
            double cdf = dist.cumulativeProbability(x);

            if (x <= 0) {
                assertEquals(0.0, cdf, 1e-10);
            } else if (x <= 2.0) {
                // area under the curve up to x is between 0 and x
                double area = Beta.beta(1.5, 3.5) - Beta.beta(1.5, 3.5 + x);
                assertNotSame(0.0, area, 1e-10);
                assertEquals(area, cdf, 1e-10);
            } else {
                // area under the curve between 0 and x is 1, so cdf is 1
                assertEquals(1.0, cdf, 1e-10);
            }
        }
    }

    @Test
    public void testFDistributionMean() {
        FDistribution dist1 = new FDistribution(1.0, 2.0);
        FDistribution dist2 = new FDistribution(2.0, 2.0);
        FDistribution dist3 = new FDistribution(3.0, 2.0);

        assertEquals(2.0, dist1.getNumericalMean(), 1e-10);
        assertEquals(1.5, dist2.getNumericalMean(), 1e-10);
        assertEquals(1.2, dist3.getNumericalMean(), 1e-10);
    }

    @Test
    public void testFDistributionVariance() {
        FDistribution dist1 = new FDistribution(1.0, 2.0);
        FDistribution dist2 = new FDistribution(2.0, 2.0);
        FDistribution dist3 = new FDistribution(3.0, 2.0);

        double[] varValues = new double[]{0.5, 1.5, 1.25};

        for (int i = 0; i < 3; i++) {
            double variance = distValues[i].getNumericalVariance();
            assertEquals(varValues[i], variance, 1e-10);
        }
    }

    @Test
    public void testFDistributionGetters() {
        FDistribution dist = new FDistribution(2.0, 3.0);

        assertEquals(2.0, dist.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(3.0, dist.getDenominatorDegreesOfFreedom(), 0.0);
        assertEquals(0.0, dist.getSolverAbsoluteAccuracy(), 1e-10);
    }

    @Test
    public void testFDistributionSupport() {
        FDistribution dist = new FDistribution(2.0, 3.0);

        assertEquals(0.0, dist.getSupportLowerBound(), 1e-10);
        assertEquals(Double.POSITIVE_INFINITY, dist.getSupportUpperBound(), 1e-10);
        assertTrue(dist.isSupportLowerBoundInclusive());
        assertFalse(dist.isSupportUpperBoundInclusive());
        assertTrue(dist.isSupportConnected());
    }
}