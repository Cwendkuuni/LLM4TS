import static org.junit.Assert.*;
import org.junit.Test;

public class NormalDistributionImplTest {

    private static final double DELTA = 0.001;

    @Test
    public void testNormalDistributionImplWithValidParameters() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl();
        assertEquals(0.0, normalDistribution.getMean(), DELTA);
        assertEquals(1.0, normalDistribution.getStandardDeviation(), DELTA);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNormalDistributionImplWithNegativeStandardDeviation() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNormalDistributionImplWithNegativeMean() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(-1, 1);
    }

    @Test
    public void testCumulativeProbabilityWithValidInput() throws MathException {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0, 1);
        double x = 0;
        double cdf = normalDistribution.cumulativeProbability(x);
        assertEquals(0.5, cdf, DELTA);
    }

    @Test
    public void testCumulativeProbabilityWithXFarFromMean() throws MathException {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0, 1);
        double x = 5;
        double cdf = normalDistribution.cumulativeProbability(x);
        assertTrue(cdf > 0.5);
    }

    @Test
    public void testCumulativeProbabilityWithXEqualToMean() throws MathException {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0, 1);
        double x = 0;
        double cdf = normalDistribution.cumulativeProbability(x);
        assertEquals(0.5, cdf, DELTA);
    }

    @Test
    public void testCumulativeProbabilityWithXEqualToMeanPlusStandardDeviation() throws MathException {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0, 1);
        double x = 1;
        double cdf = normalDistribution.cumulativeProbability(x);
        assertTrue(cdf > 0.5);
    }

    @Test
    public void testCumulativeProbabilityWithXEqualToMeanMinusStandardDeviation() throws MathException {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0, 1);
        double x = -1;
        double cdf = normalDistribution.cumulativeProbability(x);
        assertTrue(cdf < 0.5);
    }

    @Test(expected = MathException.class)
    public void testCumulativeProbabilityWithXFarFromMeanConvergenceException() throws MathException {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0, 1);
        double x = 50;
        normalDistribution.cumulativeProbability(x);
    }
}