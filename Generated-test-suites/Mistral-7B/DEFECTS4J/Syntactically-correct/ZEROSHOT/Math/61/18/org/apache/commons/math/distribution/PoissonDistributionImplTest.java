import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.distribution.PoissonDistributionImpl;
import org.apache.commons.math3.special.Gamma;
import org.junit.Test;
import static org.junit.Assert.*;

public class PoissonDistributionImplTest {

    @Test
    public void testPoissonDistributionImpl_ValidConstructor() {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(3.5);
        assertEquals(3.5, poissonDistribution.getMean(), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPoissonDistributionImpl_InvalidConstructor() {
        new PoissonDistributionImpl(-1.5);
    }

    @Test
    public void testProbability_ValidInput() {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(3.5);
        double probability = poissonDistribution.probability(2);
        assertEquals(0.04453343662662469, probability, 0.0000001);

        probability = poissonDistribution.probability(5);
        assertEquals(0.02316327269838453, probability, 0.0000001);

        probability = poissonDistribution.probability(10);
        assertEquals(0.004536583927888321, probability, 0.0000001);
    }

    @Test
    public void testProbability_InvalidInput() {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(3.5);
        assertFalse(poissonDistribution.probability(-1) > 0);
        assertFalse(poissonDistribution.probability(Integer.MAX_VALUE) > 0);
    }

    @Test(expected = MathException.class)
    public void testCumulativeProbability_ValidInputBeyondMaxIterations() {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(3.5, 1, 1);
        poissonDistribution.cumulativeProbability(1000000);
    }

    @Test(expected = MathException.class)
    public void testCumulativeProbability_ValidInputBeyondEpsilon() {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(3.5, 1, Integer.MAX_VALUE);
        poissonDistribution.cumulativeProbability(100000000);
    }

    @Test
    public void testCumulativeProbability_ValidInput() throws MathException {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(3.5);
        double cumulativeProbability = poissonDistribution.cumulativeProbability(1);
        assertEquals(0.05555555555555555, cumulativeProbability, 0.000001);

        cumulativeProbability = poissonDistribution.cumulativeProbability(5);
        assertEquals(0.7826086956521735, cumulativeProbability, 0.000001);

        cumulativeProbability = poissonDistribution.cumulativeProbability(10);
        assertEquals(0.9820982098209821, cumulativeProbability, 0.000001);
    }

    @Test
    public void testNormalApproximateProbability_ValidInput() throws MathException {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(3.5);
        double normalApproximateProbability = poissonDistribution.normalApproximateProbability(1);
        assertEquals(0.05314168870988722, normalApproximateProbability, 0.000001);

        normalApproximateProbability = poissonDistribution.normalApproximateProbability(5);
        assertEquals(0.7745966692706821, normalApproximateProbability, 0.000001);

        normalApproximateProbability = poissonDistribution.normalApproximateProbability(10);
        assertEquals(0.9820242853385882, normalApproximateProbability, 0.000001);
    }

    @Test
    public void testSample() throws MathException {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(3.5);
        int sample = poissonDistribution.sample();
        assertTrue(sample >= 0 && sample <= 1000 * poissonDistribution.getMean());

        sample = poissonDistribution.sample();
        assertEquals(2, sample);

        sample = poissonDistribution.sample();
        assertEquals(5, sample);

        sample = poissonDistribution.sample();
        assertEquals(7, sample);

        sample = poissonDistribution.sample();
        assertEquals(10, sample);
    }

    @Test
    public void testInverseCumulativeProbability_ValidInput() throws MathException {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(3.5);
        double inverseCumulativeProbability = poissonDistribution.inverseCumulativeProbability(0.05555555555555555);
        assertEquals(1, inverseCumulativeProbability, 0.000001);

        inverseCumulativeProbability = poissonDistribution.inverseCumulativeProbability(0.7826086956521735);
        assertEquals(5, inverseCumulativeProbability, 0.000001);

        inverseCumulativeProbability = poissonDistribution.inverseCumulativeProbability(0.9820982098209821);
        assertEquals(10, inverseCumulativeProbability, 0.000001);
    }

    @Test(expected = MathException.class)
    public void testInverseCumulativeProbability_InvalidInput() throws MathException {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(3.5);
        poissonDistribution.inverseCumulativeProbability(-1);
    }
}