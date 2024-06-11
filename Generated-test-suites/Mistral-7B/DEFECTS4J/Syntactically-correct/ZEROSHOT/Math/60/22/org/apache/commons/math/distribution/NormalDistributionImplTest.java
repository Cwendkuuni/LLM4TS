import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.NormalDistributionImpl;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Test;
import org.junit.Assert.*;

import static org.apache.commons.math3.SpecialFunctions.erf;
import static org.apache.commons.math3.util.Precision.*;

public class NormalDistributionImplTest {

    @Test
    public void testNormalDistributionImplConstruction() {
        NormalDistribution impl = new NormalDistributionImpl(0, 1);
        assertEquals(0, impl.getMean(), 0);
        assertEquals(1, impl.getStandardDeviation(), 0);

        impl = new NormalDistributionImpl(1, 1);
        assertEquals(1, impl.getMean(), 0);
        assertEquals(1, impl.getStandardDeviation(), 0);

        impl = new NormalDistributionImpl(1, 2);
        assertEquals(1, impl.getMean(), 0);
        assertEquals(2, impl.getStandardDeviation(), 0);

        impl = new NormalDistributionImpl(0, 2);
        assertEquals(0, impl.getMean(), 0);
        assertEquals(2, impl.getStandardDeviation(), 0);

        impl = new NormalDistributionImpl(1.5, 0.5);
        assertEquals(1.5, impl.getMean(), 0.1);
        assertEquals(0.5, impl.getStandardDeviation(), 0.1);

        try {
            new NormalDistributionImpl(-1, 2);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }

        try {
            new NormalDistributionImpl(0, -1);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }

        try {
            new NormalDistributionImpl(-2, 1);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }

        try {
            new NormalDistributionImpl(1, -1);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }
    }

    @Test
    public void testDensity() {
        NormalDistributionImpl impl = new NormalDistributionImpl(0, 1);

        for (double x = -5.0; x <= 5.0; x += 0.1) {
            double expected = FastMath.exp(-0.5 * Math.pow(x, 2)) / (1 * Math.sqrt(2 * Math.PI));
            double actual = impl.density(x);
            assertEquals(expected, actual, 1e-7);
        }
    }

    @Test
    public void testCumulativeProbability() {
        NormalDistributionImpl impl = new NormalDistributionImpl(0, 1);

        for (double x = -5.0; x <= 5.0; x += 0.1) {
            double actual = impl.cumulativeProbability(x);
            double expected = 0.5 * (1.0 + erf((x - 0) / (1 * Math.sqrt(2.0))));
            assertEquals(expected, actual, 1e-7);
        }
    }

    @Test
    public void testInverseCumulativeProbability() {
        NormalDistributionImpl impl = new NormalDistributionImpl(0, 1);

        for (double p = 0.0; p <= 1.0; p += 0.01) {
            double actual = impl.inverseCumulativeProbability(p);
            double expected = 0.0 + p * Math.sqrt(2.0);
            assertEquals(expected, actual, 1e-7);
        }

        try {
            impl.inverseCumulativeProbability(-1.0);
            fail("Expected MathException");
        } catch (MathException e) {
            // Expected
        }

        try {
            impl.inverseCumulativeProbability(1.5);
            fail("Expected MathException");
        } catch (MathException e) {
            // Expected
        }
    }

    @Test
    public void testSample() {
        NormalDistributionImpl impl = new NormalDistributionImpl(0, 1);

        for (int i = 0; i < 10000; i++) {
            double x = impl.sample();
            assertTrue(x >= -5.0 && x <= 5.0);
        }
    }
}