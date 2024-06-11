import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.exception.MathException;
import org.junit.Test;
import static org.junit.Assert.*;

public class PoissonDistributionImplTest {

    @Test
    public void testProbability() throws MathException {
        PoissonDistribution distribution = new PoissonDistributionImpl(2.5);
        assertEquals(0.10076182, distribution.probability(0), 0.0001);
        assertEquals(0.27068575, distribution.probability(1), 0.0001);
        assertEquals(0.17724525, distribution.probability(2), 0.0001);
        assertEquals(0.16734398, distribution.probability(3), 0.0001);
        assertEquals(0.14496984, distribution.probability(4), 0.0001);
        assertEquals(0.12134649, distribution.probability(5), 0.0001);
        assertEquals(0.09975827, distribution.probability(6), 0.0001);
        assertEquals(0.08168951, distribution.probability(7), 0.0001);
    }

    @Test(expected = MathRuntimeException.class)
    public void testProbabilityNegativeMean() {
        new PoissonDistributionImpl(-1);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        PoissonDistribution distribution = new PoissonDistributionImpl(2.5);
        assertEquals(0.0, distribution.cumulativeProbability(-1), 0.0);
        assertEquals(0.10076182, distribution.cumulativeProbability(0), 0.0001);
        assertEquals(0.27116263, distribution.cumulativeProbability(1), 0.0001);
        assertEquals(0.43433366, distribution.cumulativeProbability(2), 0.0001);
        assertEquals(0.58395802, distribution.cumulativeProbability(3), 0.0001);
        assertEquals(0.72760178, distribution.cumulativeProbability(4), 0.0001);
        assertEquals(0.85440415, distribution.cumulativeProbability(5), 0.0001);
        assertEquals(0.96352059, distribution.cumulativeProbability(6), 0.0001);
        assertEquals(1.0, distribution.cumulativeProbability(Integer.MAX_VALUE), 0.0);
    }

    @Test(expected = MathException.class)
    public void testCumulativeProbabilityOutOfBounds() throws MathException {
        PoissonDistribution distribution = new PoissonDistributionImpl(2.5);
        distribution.cumulativeProbability(-1);
    }

    @Test
    public void testNormalApproximateProbability() throws MathException {
        PoissonDistribution distribution = new PoissonDistributionImpl(2.5);
        assertEquals(0.10076182, distribution.normalApproximateProbability(0), 0.0001);
        assertEquals(0.26971161, distribution.normalApproximateProbability(1), 0.0001);
        assertEquals(0.17689135, distribution.normalApproximateProbability(2), 0.0001);
        assertEquals(0.16743789, distribution.normalApproximateProbability(3), 0.0001);
        assertEquals(0.14487172, distribution.normalApproximateProbability(4), 0.0001);
        assertEquals(0.12129468, distribution.normalApproximateProbability(5), 0.0001);
        assertEquals(0.09963653, distribution.normalApproximateProbability(6), 0.0001);
        assertEquals(0.08159237, distribution.normalApproximateProbability(7), 0.0001);
    }

    @Test
    public void testSample() throws MathException {
        PoissonDistribution distribution = new PoissonDistributionImpl(2.5);
        int sample = distribution.sample();
        assertEquals(2, sample);
    }

    @Test
    public void testSampleMultipleTimes() throws MathException {
        PoissonDistribution distribution = new PoissonDistributionImpl(2.5);
        int[] samples = new int[10];
        for (int i = 0; i < samples.length; i++) {
            samples[i] = distribution.sample();
        }
        int sum = 0;
        for (int i = 0; i < samples.length; i++) {
            sum += samples[i];
        }
        assertEquals(12, sum);
    }
}