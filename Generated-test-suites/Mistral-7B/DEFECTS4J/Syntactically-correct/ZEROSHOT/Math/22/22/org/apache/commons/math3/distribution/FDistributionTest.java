import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.special.Beta;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class FDistributionTest {

    @Test
    public void testFDistributionCreation() {
        FDistribution fd = new FDistribution(2.0, 4.0);
        assertNotNull(fd);
        assertEquals(2.0, fd.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(4.0, fd.getDenominatorDegreesOfFreedom(), 0.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFDistributionCreationNegativeDegreesOfFreedom() {
        new FDistribution(-1.0, 1.0);
    }

    @Test
    public void testFDistributionCumulativeProbability() {
        FDistribution fd = new FDistribution(2.0, 4.0);

        double[] xValues = {0.0, 0.5, 1.0, 2.0, 3.0, 5.0, 10.0};
        double[] pValues = {0.0, 0.1128, 0.3557, 0.6827, 0.9599, 0.9998, 1.0};

        for (int i = 0; i < xValues.length; i++) {
            double x = xValues[i];
            double p = pValues[i];
            double result = fd.cumulativeProbability(x);
            double e = Precision.doubleEquals(result, p, 1e-6) ? 0.0 : 1.0 - result;
            assertTrue("Error at x=" + x + "; expected=" + p + "; got=" + result + "; error=" + e, Precision.abs(result - p) < 1e-6);
        }
    }

    @Test
    public void testFDistributionDensity() {
        FDistribution fd = new FDistribution(2.0, 4.0);

        double[] xValues = {0.0, 0.5, 1.0, 2.0, 3.0, 5.0, 10.0};
        double[] densValues = {0.007811965, 0.066324935, 0.151736216, 0.252662241, 0.216743366, 0.00351688, 0.0};

        for (int i = 0; i < xValues.length; i++) {
            double x = xValues[i];
            double dens = densValues[i];
            double result = fd.density(x);
            double e = Precision.doubleEquals(result, dens, 1e-6) ? 0.0 : 1.0 - result / dens;
            assertTrue("Error at x=" + x + "; expected=" + dens + "; got=" + result + "; error=" + e, Precision.abs(result / dens - 1.0) < 1e-6);
        }
    }

    @Test
    public void testFDistributionVariance() {
        FDistribution fd = new FDistribution(2.0, 4.0);
        double variance = fd.getNumericalVariance();
        assertEquals(0.2471383323113858, variance, 1e-6);
    }

    @Test
    public void testFDistributionMean() {
        FDistribution fd = new FDistribution(2.0, 4.0);
        double mean = fd.getNumericalMean();
        assertEquals(1.5, mean, 1e-6);
    }
}