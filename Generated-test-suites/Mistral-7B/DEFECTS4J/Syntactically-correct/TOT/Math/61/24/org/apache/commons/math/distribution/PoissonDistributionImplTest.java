import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.distribution.PoissonDistributionImpl;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class PoissonDistributionImplTest {

    @Test
    public void testProbability() {
        PoissonDistribution dist = new PoissonDistributionImpl(1.5);
        assertEquals(0.185052327583891, dist.probability(0), 0.0001);
        assertEquals(0.270685756888783, dist.probability(1), 0.0001);
        assertEquals(0.198907779296865, dist.probability(2), 0.0001);
        assertEquals(0.216506356487828, dist.probability(3), 0.0001);
        assertEquals(0.197867223928833, dist.probability(4), 0.0001);
        assertEquals(0.173131519236153, dist.probability(5), 0.0001);
        assertEquals(0.154705582539528, dist.probability(6), 0.0001);
        assertEquals(0.137931034416049, dist.probability(7), 0.0001);
        assertEquals(0.123627145422985, dist.probability(8), 0.0001);
        assertEquals(0.11150730610822, dist.probability(9), 0.0001);
        assertEquals(0.101781873265113, dist.probability(10), 0.0001);
    }

    @Test
    public void testCumulativeProbability() throws Exception {
        PoissonDistribution dist = new PoissonDistributionImpl(1.5);
        assertEquals(0.0, dist.cumulativeProbability(Integer.MIN_VALUE), 0.0);
        assertEquals(0.049816435221452, dist.cumulativeProbability(0), 0.0001);
        assertEquals(0.151112318827382, dist.cumulativeProbability(1), 0.0001);
        assertEquals(0.305263800533043, dist.cumulativeProbability(2), 0.0001);
        assertEquals(0.449213800662517, dist.cumulativeProbability(3), 0.0001);
        assertEquals(0.585836631248981, dist.cumulativeProbability(4), 0.0001);
        assertEquals(0.712736611924102, dist.cumulativeProbability(5), 0.0001);
        assertEquals(0.830570837984375, dist.cumulativeProbability(6), 0.0001);
        assertEquals(0.936796275891607, dist.cumulativeProbability(7), 0.0001);
        assertEquals(0.984809865046383, dist.cumulativeProbability(8), 0.0001);
        assertEquals(0.998973799533147, dist.cumulativeProbability(9), 0.0001);
        assertEquals(1.0, dist.cumulativeProbability(10), 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeMean() {
        new PoissonDistributionImpl(-1.5);
    }

    @Test
    public void testSample() throws Exception {
        PoissonDistribution dist = new PoissonDistributionImpl(1.5);
        int sample = dist.sample();
        assertEquals(1, sample);
        sample = dist.sample();
        assertEquals(1, sample);
        sample = dist.sample();
        assertEquals(2, sample);
        sample = dist.sample();
        assertEquals(1, sample);
        sample = dist.sample();
        assertEquals(1, sample);
        sample = dist.sample();
        assertEquals(3, sample);
    }

    @Test
    public void testNormalApproximateProbability() throws Exception {
        PoissonDistribution dist = new PoissonDistributionImpl(1.5);
        assertEquals(0.185293741263483, dist.normalApproximateProbability(0), 0.001);
        assertEquals(0.269623320522943, dist.normalApproximateProbability(1), 0.001);
        assertEquals(0.216710197890649, dist.normalApproximateProbability(2), 0.001);
        assertEquals(0.198118760621132, dist.normalApproximateProbability(3), 0.001);
        assertEquals(0.173307423636412, dist.normalApproximateProbability(4), 0.001);
        assertEquals(0.154881515294092, dist.normalApproximateProbability(5), 0.001);
        assertEquals(0.137959763353822, dist.normalApproximateProbability(6), 0.001);
        assertEquals(0.123631451898626, dist.normalApproximateProbability(7), 0.001);
        assertEquals(0.111510947445417, dist.normalApproximateProbability(8), 0.001);
        assertEquals(0.101781749719117, dist.normalApproximateProbability(9), 0.001);
        assertEquals(0.092897159929735, dist.normalApproximateProbability(10), 0.001);
    }

    @Test
    public void testMaxIterations() throws Exception {
        try {
            new PoissonDistributionImpl(1.5, 0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            new PoissonDistributionImpl(1.5, -1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        PoissonDistribution dist = new PoissonDistributionImpl(1.5, 1000001);
        assertEquals(0.049816435221452, dist.cumulativeProbability(0), 0.0001);
    }
}