import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.NormalDistributionImpl;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class NormalDistributionImplTest {

    @Test
    public void testNormalDistributionImpl_MeanZeroStdOne() {
        NormalDistribution distribution = new NormalDistributionImpl();
        assertEquals(0.0, distribution.getMean(), 0.0);
        assertEquals(1.0, distribution.getStandardDeviation(), 0.0);
    }

    @Test
    public void testNormalDistributionImpl_MeanPositiveStdPositive() {
        NormalDistribution distribution = new NormalDistributionImpl(5.0, 2.0);
        assertEquals(5.0, distribution.getMean(), 0.0);
        assertEquals(2.0, distribution.getStandardDeviation(), 0.0);
        assertEquals(0.0471238941783264, distribution.density(5.5), 0.001);
        assertEquals(0.682689492137354, distribution.cumulativeProbability(3.0), 0.001);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNormalDistributionImpl_NegativeStdDeviation() {
        new NormalDistributionImpl(0.0, -1.0);
    }

    @Test
    public void testNormalDistributionImpl_Sample() throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl(1.0, 2.0);
        double sample = distribution.sample();
        assertTrue(sample > -3 && sample < 3);
    }

    @Test(expected = MathException.class)
    public void testNormalDistributionImpl_InverseCumulativeProbabilityOutOfRange() throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl(0.0, 1.0);
        distribution.inverseCumulativeProbability(-1.0);
    }

    @Test
    public void testNormalDistributionImpl_InverseCumulativeProbability() throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl(0.0, 1.0);
        assertEquals(1.28155200088393, distribution.inverseCumulativeProbability(0.841344746), 0.001);
    }
}