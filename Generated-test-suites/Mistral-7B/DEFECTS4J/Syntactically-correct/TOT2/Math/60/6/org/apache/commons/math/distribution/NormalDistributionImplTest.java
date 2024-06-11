import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.NormalDistributionImpl;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Test;
import static org.junit.Assert.*;

public class NormalDistributionImplTest {

    @Test
    public void testNormalDistributionImplConstruction() {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        assertEquals(0.0, dist.getMean(), 0.0);
        assertEquals(1.0, dist.getStandardDeviation(), 0.0);
    }

    @Test
    public void testNormalDistributionImplMeanAndStdDev() {
        NormalDistribution dist = new NormalDistributionImpl(1.5, 2.1);
        assertEquals(1.5, dist.getMean(), 0.0);
        assertEquals(2.1, dist.getStandardDeviation(), 0.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNormalDistributionImplInvalidStandardDeviation() {
        new NormalDistributionImpl(0.0, -1.0);
    }

    @Test
    public void testNormalDistributionImplDensity() {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        assertEquals(0.39894228040143271, dist.density(0.0), 0.0000001);
        assertEquals(0.39894228040143271, dist.density(1.0), 0.0000001);
    }

    @Test
    public void testNormalDistributionImplCumulativeProbability() {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        assertEquals(0.5, dist.cumulativeProbability(0.0), 0.0000001);
        assertEquals(0.841344746, dist.cumulativeProbability(1.0), 0.0000001);
    }

    @Test(expected = MathException.class)
    public void testNormalDistributionImplCumulativeProbabilityOutOfRange() {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        dist.cumulativeProbability(-5.0);
    }

    @Test
    public void testNormalDistributionImplInverseCumulativeProbability() {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        assertEquals(0.0, dist.inverseCumulativeProbability(0.0), 0.0000001);
        assertEquals(1.0, dist.inverseCumulativeProbability(1.0), 0.0000001);
        assertEquals(-1.282127588, dist.inverseCumulativeProbability(0.158730159), 0.0000001);
    }

    @Test(expected = MathException.class)
    public void testNormalDistributionImplInverseCumulativeProbabilityInvalidProbability() {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        dist.inverseCumulativeProbability(-1.0);
    }

    @Test
    public void testNormalDistributionImplSample() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        double sample = dist.sample();
        assertEquals(0.0, sample, 0.01);
    }
}