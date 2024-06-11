import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.distribution.PoissonDistributionImpl;
import org.apache.commons.math3.exception.MathException;
import org.apache.commons.math3.test.AbstractRealDistributionTest;
import org.junit.Test;
import static org.junit.Assert.*;

public class PoissonDistributionImplTest extends AbstractRealDistributionTest {

    @Test
    public void testProbability() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(2.5);
        assertEquals(dist.probability(0), 0.113105625, 1e-4);
        assertEquals(dist.probability(1), 0.22621125, 1e-4);
        assertEquals(dist.probability(2), 0.22116913, 1e-4);
        assertEquals(dist.probability(5), 0.073833923, 1e-4);
        assertEquals(dist.probability(10), 0.010872876, 1e-4);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(2.5);
        assertEquals(dist.cumulativeProbability(0), 0, 1e-4);
        assertEquals(dist.cumulativeProbability(1), 0.113105625, 1e-4);
        assertEquals(dist.cumulativeProbability(2), 0.339781563, 1e-4);
        assertEquals(dist.cumulativeProbability(5), 0.831791927, 1e-4);
        assertEquals(dist.cumulativeProbability(10), 0.991828366, 1e-4);
        try {
            dist.cumulativeProbability(12);
            fail("Expected MathException");
        } catch (MathException e) {
            // expected
        }
    }

    @Test
    public void testNormalApproximateProbability() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(2.5);
        assertEquals(dist.normalApproximateProbability(0), 0.113105625, 1e-4);
        assertEquals(dist.normalApproximateProbability(1), 0.22621125, 1e-4);
        assertEquals(dist.normalApproximateProbability(2), 0.22116913, 1e-4);
        assertEquals(dist.normalApproximateProbability(5), 0.073833923, 1e-4);
        assertEquals(dist.normalApproximateProbability(10), 0.010872876, 1e-4);
    }

    @Test
    public void testSample() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(2.5);
        int value = dist.sample();
        assertEquals(value, 3);
    }

    @Test(expected = MathException.class)
    public void testSampleFailed() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(-0.5);
        dist.sample();
        fail("Expected MathException");
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(2.5);
        assertEquals(dist.inverseCumulativeProbability(0), 0, 1e-4);
        assertEquals(dist.inverseCumulativeProbability(0.1), 0, 1e-4);
        assertEquals(dist.inverseCumulativeProbability(0.339781563), 1, 1e-4);
        assertEquals(dist.inverseCumulativeProbability(0.831791927), 5, 1e-4);
        assertEquals(dist.inverseCumulativeProbability(0.991828366), 10, 1e-4);
        try {
            dist.inverseCumulativeProbability(1.01);
            fail("Expected MathException");
        } catch (MathException e) {
            // expected
        }
    }
}