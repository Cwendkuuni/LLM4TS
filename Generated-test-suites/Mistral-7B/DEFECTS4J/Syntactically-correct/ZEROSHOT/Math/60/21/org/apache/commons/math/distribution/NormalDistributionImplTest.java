import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.NormalDistributionImpl;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.special.Erf;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class NormalDistributionImplTest {
    private NormalDistribution dist;

    @Before
    public void setUp() {
        dist = new NormalDistributionImpl(0, 1);
    }

    @Test
    public void testZeroMeanOneStandardDeviation() {
        Assert.assertEquals(0.0, dist.getMean(), 0.0);
        Assert.assertEquals(1.0, dist.getStandardDeviation(), 0.0);
        Assert.assertEquals(0.5, dist.density(0.0), 0.01);
        Assert.assertEquals(0.15430801797415642, dist.cumulativeProbability(0.5), 0.0001);
        Assert.assertEquals(1.2821226451124137, dist.inverseCumulativeProbability(0.841344746), 0.0001);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeStandardDeviation() {
        new NormalDistributionImpl(-1, 1);
    }

    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityOutOfRange() {
        dist.inverseCumulativeProbability(-0.1);
    }

    @Test
    public void testSample() throws MathException {
        Assert.assertArrayEquals(new double[]{1.23456789, 0.43215678}, dist.sample(2), 0.01);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testInverseCumulativeProbabilityConvergenceFailure() {
        dist = new NormalDistributionImpl(1, 1);
        dist.inverseCumulativeProbability(0.5000000000001);
    }

    @Test
    public void testMeanAndStandardDeviation() {
        dist = new NormalDistributionImpl(1, 2);
        Assert.assertEquals(1, dist.getMean(), 0.0);
        Assert.assertEquals(2, dist.getStandardDeviation(), 0.0);
    }

    @Test
    public void testDensityForNegativeX() {
        dist = new NormalDistributionImpl(2, 1);
        double density = dist.density(-1.5);
        Assert.assertEquals(-0.012053588965830961, density, 0.0001);
    }

    @Test
    public void testDensityForLargeX() {
        dist = new NormalDistributionImpl(0, 1);
        double density = dist.density(1000.0);
        Assert.assertEquals(0, density, 0.0);
    }

    @Test
    public void testCumulativeProbabilityForNegativeX() {
        dist = new NormalDistributionImpl(0, 1);
        double cumProb = dist.cumulativeProbability(-1.0);
        Assert.assertEquals(0.0, cumProb, 0.0);
    }

    @Test
    public void testCumulativeProbabilityForLargeX() {
        dist = new NormalDistributionImpl(0, 1);
        double cumProb = dist.cumulativeProbability(1000.0);
        Assert.assertEquals(1.0, cumProb, 0.0);
    }

    @Test
    public void testCumulativeProbabilityForMean() {
        dist = new NormalDistributionImpl(0, 1);
        double cumProb = dist.cumulativeProbability(0.0);
        Assert.assertEquals(0.5, cumProb, 0.0);
    }

    @Test
    public void testCumulativeProbabilityForMeanPlusStandardDeviation() {
        dist = new NormalDistributionImpl(0, 1);
        double cumProb = dist.cumulativeProbability(1.0);
        Assert.assertEquals(0.841344746, cumProb, 0.0001);
    }

    @Test
    public void testCumulativeProbabilityForMeanMinusStandardDeviation() {
        dist = new NormalDistributionImpl(0, 1);
        double cumProb = dist.cumulativeProbability(-1.0);
        Assert.assertEquals(0.15865525316456063, cumProb, 0.0001);
    }

    @Test
    public void testInverseCumulativeProbabilityForZero() {
        dist = new NormalDistributionImpl(0, 1);
        double inverseCumProb = dist.inverseCumulativeProbability(0.0);
        Assert.assertEquals(0.0, inverseCumProb, 0.0);
    }

    @Test
    public void testInverseCumulativeProbabilityForOne() {
        dist = new NormalDistributionImpl(0, 1);
        double inverseCumProb = dist.inverseCumulativeProbability(1.0);
        Assert.assertEquals(1.2821226451124137, inverseCumProb, 0.0001);
    }

    @Test
    public void testInverseCumulativeProbabilityForHalf() {
        dist = new NormalDistributionImpl(0, 1);
        double inverseCumProb = dist.inverseCumulativeProbability(0.5);
        Assert.assertEquals(0.0, inverseCumProb, 0.0); // theoretically should be 0 or close to it
    }

    @Test
    public void testInverseCumulativeProbabilityForMean() {
        dist = new NormalDistributionImpl(0, 1);
        double inverseCumProb = dist.inverseCumulativeProbability(0.5);
        Assert.assertEquals(0.0, inverseCumProb, 0.0); // theoretically should be 0 or close to it
    }

    @Test
    public void testInverseCumulativeProbabilityForMeanPlusOneStandardDeviation() {
        dist = new NormalDistributionImpl(0, 1);
        double inverseCumProb = dist.inverseCumulativeProbability(1.5707963267948966);
        Assert.assertEquals(1.0213203059942382, inverseCumProb, 0.0001);
    }

    @Test
    public void testInverseCumulativeProbabilityForMeanMinusOneStandardDeviation() {
        dist = new NormalDistributionImpl(0, 1);
        double inverseCumProb = dist.inverseCumulativeProbability(-0.5707963267948966);
        Assert.assertEquals(-1.0213203059942382, inverseCumProb, 0.0001);
    }

    @Test
    public void testInverseCumulativeProbabilityForNegativeValues() {
        dist = new NormalDistributionImpl(0, 1);
        double inverseCumProb = dist.inverseCumulativeProbability(-1.0);
        Assert.assertEquals(Double.NEGATIVE_INFINITY, inverseCumProb, 0.0);
    }

    @Test
    public void testInverseCumulativeProbabilityForPositiveValuesGreaterThanOne() {
        dist = new NormalDistributionImpl(0, 1);
        double inverseCumProb = dist.inverseCumulativeProbability(3.0);
        Assert.assertEquals(Double.POSITIVE_INFINITY, inverseCumProb, 0.0);
    }
}