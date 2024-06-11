import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.NormalDistributionImpl;
import org.apache.commons.math3.exception.MathException;
import org.junit.Test;
import static org.junit.Assert.*;

public class NormalDistributionImplTest {

    @Test
    public void testNormalDistributionImpl_mean0_sd1() throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl();

        // Check mean and standard deviation
        assertEquals(0, distribution.getMean(), 0.001);
        assertEquals(1, distribution.getStandardDeviation(), 0.001);

        // Check density
        assertEquals(0.398942280401, distribution.density(0), 0.001);
        assertEquals(0.0, distribution.density(-3), 0.01);
        assertEquals(0.0, distribution.density(3), 0.01);

        // Check cumulative probability
        assertEquals(0.5, distribution.cumulativeProbability(0), 0.01);
        assertEquals(0, distribution.cumulativeProbability(-3), 0.01);
        assertEquals(1, distribution.cumulativeProbability(3), 0.01);

        // Check inverse cumulative probability
        assertEquals(0, distribution.inverseCumulativeProbability(0), 0.01);
        assertEquals(Math.pow(-1 * Math.E, 1.5),
                distribution.inverseCumulativeProbability(0.15625), 0.01);

        // Check sample
        assertEquals(0, distribution.sample(), 0.01);
        assertEquals(0.54455, distribution.sample(), 0.5);
    }

    @Test
    public void testNormalDistributionImpl_mean5_sd2() throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl(5, 2);

        // Check mean and standard deviation
        assertEquals(5, distribution.getMean(), 0.001);
        assertEquals(2, distribution.getStandardDeviation(), 0.001);

        // Check density
        assertEquals(0.08545683265, distribution.density(5.5), 0.001);
        assertEquals(0.0, distribution.density(-3), 0.01);
        assertEquals(0.0, distribution.density(8), 0.01);

        // Check cumulative probability
        assertEquals(0.5, distribution.cumulativeProbability(5), 0.01);
        assertEquals(0, distribution.cumulativeProbability(-3), 0.01);
        assertEquals(1, distribution.cumulativeProbability(8), 0.01);

        // Check inverse cumulative probability
        assertEquals(5, distribution.inverseCumulativeProbability(0.5), 0.01);
        assertEquals(5.54285713859,
                distribution.inverseCumulativeProbability(0.9973), 0.01);

        // Check sample
        assertEquals(5, distribution.sample(), 0.01);
        assertEquals(7.02671, distribution.sample(), 0.5);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNormalDistributionImpl_negativeStandardDeviation() {
        new NormalDistributionImpl(0, -1);
    }

    @Test(expected = MathException.class)
    public void testNormalDistributionImpl_inverseCumulativeProbabilityOutOfRange()
            throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl(0, 1);
        distribution.inverseCumulativeProbability(-0.1);
    }
}