import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.distribution.PoissonDistributionImpl;
import org.apache.commons.math3.exception.MathException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PoissonDistributionImplTest {

    @Test
    public void testConstructorWithPositiveMean() {
        PoissonDistributionImpl poissonDist = new PoissonDistributionImpl(2.5);
        assertEquals(2.5, poissonDist.getMean(), 0.001);
    }

    @Test(expected = MathRuntimeException.class)
    public void testConstructorWithNegativeMean() {
        new PoissonDistributionImpl(-1.5);
    }

    @Test
    public void testProbabilityMethodWithValidInput() {
        PoissonDistributionImpl poissonDist = new PoissonDistributionImpl(2.5);
        assertEquals(0.170178192174467, poissonDist.probability(1), 0.001);
        assertEquals(0.270685285395817, poissonDist.probability(2), 0.001);
        assertEquals(0.171678583375854, poissonDist.probability(3), 0.001);
    }

    @Test
    public void testProbabilityMethodWithInvalidInput() {
        PoissonDistributionImpl poissonDist = new PoissonDistributionImpl(2.5);
        assertEquals(0.0, poissonDist.probability(-1), 0.0);
        assertEquals(1.0, poissonDist.probability(Integer.MAX_VALUE), 0.0);
    }

    @Test
    public void testCumulativeProbabilityMethodWithValidInput() throws MathException {
        PoissonDistributionImpl poissonDist = new PoissonDistributionImpl(2.5);
        assertEquals(0.049802688570839, poissonDist.cumulativeProbability(0), 0.001);
        assertEquals(0.170193162227823, poissonDist.cumulativeProbability(1), 0.001);
        assertEquals(0.319593807368281, poissonDist.cumulativeProbability(2), 0.001);
    }

    @Test
    public void testCumulativeProbabilityMethodWithInvalidInput() throws MathException {
        PoissonDistributionImpl poissonDist = new PoissonDistributionImpl(2.5);
        assertEquals(0.0, poissonDist.cumulativeProbability(-1), 0.0);
        assertEquals(1.0, poissonDist.cumulativeProbability(Integer.MAX_VALUE), 0.0);
    }

    @Test
    public void testNormalApproximateProbabilityMethodWithValidInput() throws MathException {
        PoissonDistributionImpl poissonDist = new PoissonDistributionImpl(2.5);
        assertEquals(0.170178192174467, poissonDist.normalApproximateProbability(1), 0.001);
        assertEquals(0.270685285395817, poissonDist.normalApproximateProbability(2), 0.001);
        assertEquals(0.171678583375854, poissonDist.normalApproximateProbability(3), 0.001);
    }

    @Test
    public void testNormalApproximateProbabilityMethodWithInvalidInput() throws MathException {
        PoissonDistributionImpl poissonDist = new PoissonDistributionImpl(2.5);
        assertEquals(0.0, poissonDist.normalApproximateProbability(-1), 0.0);
        assertEquals(1.0, poissonDist.normalApproximateProbability(Integer.MAX_VALUE), 0.0);
    }

    @Test
    public void testSampleMethodWithValidInput() throws MathException {
        PoissonDistributionImpl poissonDist = new PoissonDistributionImpl(2.5);
        int sample;
        for (int i = 0; i < 10; i++) {
            sample = poissonDist.sample();
            assertTrue(sample >= 0 && sample <= 1000 * 2.5);
        }
    }

    @Test
    public void testSampleMethodWithInvalidInput() throws MathException {
        PoissonDistributionImpl poissonDist = new PoissonDistributionImpl(2.5);
        int sample = poissonDist.sample();
        assertEquals(sample, poissonDist.sample());
    }

}