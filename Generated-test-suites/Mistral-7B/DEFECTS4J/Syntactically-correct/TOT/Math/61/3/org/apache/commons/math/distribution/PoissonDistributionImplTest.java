import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MathRuntimeException;
import org.apache.commons.math3.stat.StatUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class PoissonDistributionImplTest {

    /**
     * Test constructor with valid mean.
     */
    @Test
    public void testConstructorValidMean() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(2.5);
        assertEquals(2.5, dist.getMean(), 0.001);
    }

    /**
     * Test constructor with negative mean.
     */
    @Test(expected = MathRuntimeException.class)
    public void testConstructorNegativeMean() {
        new PoissonDistributionImpl(-1.0);
    }

    /**
     * Test probability method with valid parameters.
     */
    @Test
    public void testProbabilityValid() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(3.0);
        double[] expectedProbs = new double[]{0.0738, 0.1359, 0.2008, 0.2307, 0.2165, 0.1429, 0.0811, 0.0361, 0.0122, 0.0039, 0.0008, 0.0001};
        for (int i = 0; i < 12; i++) {
            assertEquals(expectedProbs[i], dist.probability(i), 0.001);
        }
    }

    /**
     * Test probability method with invalid parameter (negative value).
     */
    @Test
    public void testProbabilityInvalid() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(3.0);
        try {
            dist.probability(-1);
            fail("Expected MathIllegalArgumentException");
        } catch (MathIllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Test probability method with invalid parameter (Integer.MAX_VALUE).
     */
    @Test
    public void testProbabilityMaxValue() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(3.0);
        try {
            dist.probability(Integer.MAX_VALUE);
            fail("Expected MathIllegalArgumentException");
        } catch (MathIllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Test cumulativeProbability method with valid parameters.
     */
    @Test
    public void testCumulativeProbabilityValid() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(3.0);
        double[] expectedCProbs = new double[]{0.0000, 0.0039, 0.0122, 0.0361, 0.0811, 0.1429, 0.2008, 0.2307, 0.2165, 0.1359, 0.0738, 0.0228};
        for (int i = 0; i < 12; i++) {
            assertEquals(expectedCProbs[i], dist.cumulativeProbability(i), 0.001);
        }
        assertEquals(1.0, dist.cumulativeProbability(Integer.MAX_VALUE), 0.001);
    }

    /**
     * Test cumulativeProbability method with invalid parameter (negative value).
     */
    @Test
    public void testCumulativeProbabilityNegative() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(3.0);
        try {
            dist.cumulativeProbability(-1);
            fail("Expected MathIllegalArgumentException");
        } catch (MathIllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Test cumulativeProbability method with invalid parameter (large value).
     */
    @Test
    public void testCumulativeProbabilityLarge() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(3.0);
        try {
            dist.cumulativeProbability(Integer.MAX_VALUE + 1);
            fail("Expected MathIllegalArgumentException");
        } catch (MathIllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Test normalApproximateProbability method with valid parameters.
     */
    @Test
    public void testNormalApproximateProbabilityValid() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(3.0);
        double[] expectedNProbs = new double[]{0.0739, 0.1358, 0.1649, 0.1719, 0.1584, 0.1273, 0.0881, 0.0525, 0.0228, 0.0086, 0.0021, 0.0005};
        for (int i = 0; i < 12; i++) {
            assertEquals(expectedNProbs[i], dist.normalApproximateProbability(i), 0.001);
        }
    }

    /**
     * Test normalApproximateProbability method with invalid parameter (negative value).
     */
    @Test
    public void testNormalApproximateProbabilityNegative() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(3.0);
        try {
            dist.normalApproximateProbability(-1);
            fail("Expected MathIllegalArgumentException");
        } catch (MathIllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Test normalApproximateProbability method with invalid parameter (large value).
     */
    @Test
    public void testNormalApproximateProbabilityLarge() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(3.0);
        try {
            dist.normalApproximateProbability(Integer.MAX_VALUE);
            fail("Expected MathIllegalArgumentException");
        } catch (MathIllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Test sample method with mean close to zero.
     */
    @Test
    public void testSampleMeanCloseToZero() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(0.1);
        for (int i = 0; i < 100; i++) {
            int sample = dist.sample();
            assertTrue(sample >= 0 && sample <= 10);
        }
    }

    /**
     * Test sample method with mean far from zero.
     */
    @Test
    public void testSampleMeanFarFromZero() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(100.0);
        for (int i = 0; i < 100; i++) {
            int sample = dist.sample();
            assertTrue(sample >= 0 && sample <= 200);
        }
    }
}