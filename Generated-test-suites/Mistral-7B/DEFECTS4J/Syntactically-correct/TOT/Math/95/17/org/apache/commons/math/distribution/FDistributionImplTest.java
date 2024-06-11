import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.FDistributionImpl;
import org.apache.commons.math3.special.Beta;
import org.junit.Test;
import static org.junit.Assert.*;

public class FDistributionImplTest {

    @Test
    public void testFDistributionImpl() {
        FDistribution fd = new FDistributionImpl(2.0, 4.0);
        assertEquals(2.0, fd.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(4.0, fd.getDenominatorDegreesOfFreedom(), 0.0);
        assertNotNull(fd);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeNumeratorDOF() {
        new FDistributionImpl(-1.0, 4.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeDenominatorDOF() {
        new FDistributionImpl(2.0, -1.0);
    }

    @Test
    public void testCumulativeProbability() {
        FDistribution fd = new FDistributionImpl(2.0, 4.0);
        double p = 0.1; // arbitrary probability
        double x = fd.inverseCumulativeProbability(p);
        double cp = fd.cumulativeProbability(x);
        assertEquals(p, cp, 0.001);
    }

    @Test
    public void testZeroCumulativeProbability() {
        FDistribution fd = new FDistributionImpl(2.0, 4.0);
        double p = 0.0;
        double x = fd.inverseCumulativeProbability(p);
        double cp = fd.cumulativeProbability(x);
        assertEquals(p, cp, 0.0);
    }

    @Test
    public void testOneCumulativeProbability() {
        FDistribution fd = new FDistributionImpl(2.0, 4.0);
        double p = 1.0;
        double x = fd.inverseCumulativeProbability(p);
        double cp = fd.cumulativeProbability(x);
        assertEquals(p, cp, 0.0);
    }

    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityNegativeProbability() {
        FDistribution fd = new FDistributionImpl(2.0, 4.0);
        double p = -1.0;
        fd.inverseCumulativeProbability(p);
    }

    @Test
    public void testBetaFunction() {
        double n = 2.0;
        double x = 1.0;
        double m = 4.0;
        double p = Beta.regularizedBeta(n * x / (m + n * x), 0.5 * n, 0.5 * m);
        double cp = fd.cumulativeProbability(x);
        assertEquals(p, cp, 0.001);
    }

    @Test
    public void testDistributionFunction() {
        FDistribution fd = new FDistributionImpl(2.0, 4.0);
        int numbersToTest = 10000;
        double[] xValues = new double[numbersToTest];
        for (int i = 0; i < numbersToTest; i++) {
            xValues[i] = Math.random() * 5.0 + 1.0;
        }
        for (int i = 0; i < numbersToTest; i++) {
            double x = xValues[i];
            double p = fd.cumulativeProbability(x);
            double np = 0.0;
            for (int j = 0; j < 100000; j++) {
                np += fd.density(x + Math.random()) > 0.0 ? 1 : 0;
            }
            np /= 100000.0;
            assertEquals(p, np, 0.001);
        }
    }
}