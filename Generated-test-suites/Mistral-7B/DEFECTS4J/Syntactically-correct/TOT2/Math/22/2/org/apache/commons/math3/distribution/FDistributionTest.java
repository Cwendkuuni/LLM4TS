import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.RealDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Test;
import static org.junit.Assert.*;

public class FDistributionTest {

    @Test
    public void testFDistributionCreation() throws NotStrictlyPositiveException {
        double numeratorDegreesOfFreedom = 5.0;
        double denominatorDegreesOfFreedom = 7.0;
        FDistribution fd = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        assertNotNull(fd);
        assertEquals(numeratorDegreesOfFreedom, fd.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(denominatorDegreesOfFreedom, fd.getDenominatorDegreesOfFreedom(), 0.0);
    }

    @Test
    public void testFDistributionCumulativeProbability() {
        FDistribution fd = new FDistribution(2.0, 3.0);
        double p;

        p = fd.cumulativeProbability(0.0);
        assertEquals(0.0, p, 1e-12);

        p = fd.cumulativeProbability(1.0 / (2.0 * 3.0));
        assertEquals(0.1279736111452754, p, 1e-12);

        p = fd.cumulativeProbability(1.0);
        assertEquals(1.0, p, 1e-12);

        p = fd.cumulativeProbability(2.0);
        assertEquals(1.0, p, 1e-12);

        p = fd.cumulativeProbability(Double.POSITIVE_INFINITY);
        assertEquals(1.0, p, 1e-12);
    }

    @Test
    public void testFDistributionDensity() {
        FDistribution fd = new FDistribution(2.0, 3.0);
        double x;
        double density;

        x = 0.0;
        density = fd.density(x);
        assertEquals(0.0, density, 1e-12);

        x = 1.0 / (2.0 * 3.0);
        density = fd.density(x);
        assertEquals(0.1279736111452754, density, 1e-12);

        x = 1.0;
        density = fd.density(x);
        assertEquals(0.0, density, 1e-12);

        x = 2.0;
        density = fd.density(x);
        assertEquals(0.0, density, 1e-12);

        x = Double.POSITIVE_INFINITY;
        density = fd.density(x);
        assertEquals(0.0, density, 1e-12);
    }

    @Test
    public void testFDistributionNegativeParameters() {
        try {
            new FDistribution(-1.0, 1.0);
            fail("Expecting NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }

        try {
            new FDistribution(1.0, -1.0);
            fail("Expecting NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }
    }

    @Test
    public void testFDistributionGetSupportBounds() {
        FDistribution fd = new FDistribution(2.0, 3.0);
        double lowerBound = fd.getSupportLowerBound();
        double upperBound = fd.getSupportUpperBound();

        assertEquals(0.0, lowerBound, 1e-12);
        assertEquals(Double.POSITIVE_INFINITY, upperBound, 1e-12);
    }

    @Test
    public void testFDistributionIsSupportConnected() {
        FDistribution fd = new FDistribution(2.0, 3.0);
        assertTrue(fd.isSupportConnected());
    }

    @Test
    public void testFDistributionNumericalMean() {
        FDistribution fd = new FDistribution(3.0, 3.0);
        double mean = fd.getNumericalMean();

        assertEquals(3.0 / 2.0, mean, 1e-12);
    }

    @Test
    public void testFDistributionNumericalVariance() {
        FDistribution fd = new FDistribution(3.0, 3.0);
        double variance = fd.getNumericalVariance();

        assertEquals(9.0 / 4.0, variance, 1e-12);
    }
}