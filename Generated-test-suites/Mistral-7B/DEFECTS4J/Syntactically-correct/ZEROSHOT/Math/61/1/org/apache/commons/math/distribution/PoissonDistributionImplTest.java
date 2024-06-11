import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.distribution.PoissonDistributionImpl;
import org.apache.commons.math3.special.Gamma;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PoissonDistributionImplTest {

    @Test
    public void testPoissonDistributionImplConstruction() {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(2.5);
        assertEquals(2.5, poissonDistribution.getMean(), 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPoissonDistributionImplNegativeMean() {
        new PoissonDistributionImpl(-1.5);
    }

    @Test
    public void testProbabilityZero() {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(1.0);
        double probabilityZero = poissonDistribution.probability(0);
        assertEquals(0.2704859753425934, probabilityZero, 0.00001);
    }

    @Test
    public void testProbabilityOne() {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(1.0);
        double probabilityOne = poissonDistribution.probability(1);
        assertEquals(0.3679253194249362, probabilityOne, 0.00001);
    }

    @Test
    public void testProbabilityLargeNumber() {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(10.0);
        double probabilityLargeNumber = poissonDistribution.probability(100);
        assertEquals(0.9048378558415114, probabilityLargeNumber, 0.00001);
    }

    @Test
    public void testCumulativeProbabilityZero() throws MathException {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(1.0);
        double cumulativeProbabilityZero = poissonDistribution.cumulativeProbability(0);
        assertEquals(0.0, cumulativeProbabilityZero, 0.00001);
    }

    @Test
    public void testCumulativeProbabilityOne() throws MathException {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(1.0);
        double cumulativeProbabilityOne = poissonDistribution.cumulativeProbability(1);
        assertEquals(0.3679253194249362, cumulativeProbabilityOne, 0.00001);
    }

    @Test
    public void testCumulativeProbabilityLargeNumber() throws MathException {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(10.0);
        double cumulativeProbabilityLargeNumber = poissonDistribution.cumulativeProbability(100);
        assertEquals(0.9048378558415114, cumulativeProbabilityLargeNumber, 0.00001);
    }

    @Test
    public void testNormalApproximateProbabilityZero() throws MathException {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(1.0);
        double normalApproximateProbabilityZero = poissonDistribution.normalApproximateProbability(0);
        assertEquals(0.3679253194249362, normalApproximateProbabilityZero, 0.00001);
    }

    @Test
    public void testNormalApproximateProbabilityOne() throws MathException {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(1.0);
        double normalApproximateProbabilityOne = poissonDistribution.normalApproximateProbability(1);
        assertEquals(0.3679253194249362, normalApproximateProbabilityOne, 0.00001);
    }

    @Test
    public void testSampleZero() throws MathException {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(1.0);
        int sampleZero = poissonDistribution.sample();
        assertEquals(0, sampleZero);
    }

    @Test
    public void testSampleOne() throws MathException {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(1.0);
        int sampleOne = poissonDistribution.sample();
        assertEquals(1, sampleOne);
    }

    @Test
    public void testSampleLargeNumber() throws MathException {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(10.0);
        int sampleLargeNumber = poissonDistribution.sample();
        assertTrue(sampleLargeNumber >= 0 && sampleLargeNumber <= 100);
    }

    @Test
    public void testSampleNegativeNumber() throws MathException {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(1.0);
        int sampleNegativeNumber = poissonDistribution.sample();
        assertFalse(sampleNegativeNumber < 0);
    }

    @Test
    public void testSampleOutOfBounds() throws MathException {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(Integer.MAX_VALUE);
        int sampleOutOfBounds = poissonDistribution.sample();
        assertEquals(Integer.MAX_VALUE, sampleOutOfBounds);
    }
}