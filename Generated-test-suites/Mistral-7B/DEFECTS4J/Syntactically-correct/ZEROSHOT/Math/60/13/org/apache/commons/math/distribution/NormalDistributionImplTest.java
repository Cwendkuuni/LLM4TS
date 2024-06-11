import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.NormalDistributionImpl;
import org.junit.Test;
import static org.junit.Assert.*;

public class NormalDistributionImplTest {

    @Test
    public void testNormalDistributionImplCreation() {
        NormalDistribution distribution = new NormalDistributionImpl(0, 1);
        assertEquals("Mean should be 0", 0, distribution.getMean(), 0.001);
        assertEquals("Standard deviation should be 1", 1, distribution.getStandardDeviation(), 0.001);
        assertEquals("Class name should be NormalDistributionImpl", NormalDistributionImpl.class, distribution.getClass());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNormalDistributionImplNegativeStandardDeviation() {
        new NormalDistributionImpl(0, -1);
    }

    @Test
    public void testNormalDistributionImplDensity() {
        NormalDistribution distribution = new NormalDistributionImpl(0, 1);
        double density = distribution.density(0);
        assertEquals("Density at mean should be 0.3989422804014327", 0.3989422804014327, density, 0.0001);

        density = distribution.density(1.5);
        assertEquals("Density at 1.5 standard deviation should be 0.0737764353994542", 0.0737764353994542, density, 0.0001);

        density = distribution.density(-2);
        assertEquals("Density at negative infinities should be 0", 0, density, 0.001);
    }

    @Test
    public void testNormalDistributionImplCumulativeProbability() throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl(0, 1);
        double prob = distribution.cumulativeProbability(0);
        assertEquals("Probability at mean should be 0.5", 0.5, prob, 0.001);

        prob = distribution.cumulativeProbability(1.5);
        assertEquals("Probability at 1.5 standard deviation should be 0.4331927536656383", 0.4331927536656383, prob, 0.001);

        prob = distribution.cumulativeProbability(-2);
        assertEquals("Probability at negative infinities should be 0", 0, prob, 0.001);
    }

    @Test
    public void testNormalDistributionImplSample() throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl(0, 1);
        double sample = distribution.sample();
        assertEquals("Sample should be close to mean", 0, sample - 0, 0.01);

        sample = distribution.sample();
        assertEquals("Sample should be close to mean", 0, sample - 0, 0.01);
    }

    @Test(expected = MathException.class)
    public void testNormalDistributionImplInverseCumulativeProbabilityNegativeProbability() throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl(0, 1);
        distribution.inverseCumulativeProbability(-1);
    }

    @Test
    public void testNormalDistributionImplInverseCumulativeProbability() throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl(0, 1);
        double prob = 0.1;
        double result = distribution.inverseCumulativeProbability(prob);
        double expected = FastMath.sqrt(-2 * Math.log(1 - prob)) * FastMath.erfInv(2 * prob - 1);
        assertEquals("Inverse cumulative probability should be close to expected value", expected, result, 0.001);

        prob = 0.5;
        result = distribution.inverseCumulativeProbability(prob);
        expected = 0;
        assertEquals("Inverse cumulative probability at mean should be mean", expected, result, 0.001);

        prob = 0.9;
        result = distribution.inverseCumulativeProbability(prob);
        expected = FastMath.sqrt(-2 * Math.log(prob)) * FastMath.erfInv(2 * prob - 1);
        assertEquals("Inverse cumulative probability should be close to expected value", expected, result, 0.001);
    }
}