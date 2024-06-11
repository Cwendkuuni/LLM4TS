import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.distribution.PoissonDistributionImpl;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MathRuntimeException;
import org.junit.Test;
import static org.junit.Assert.*;

public class PoissonDistributionImplTest {

    @Test
    public void testPoissonDistributionWithValidMean() {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(2.5);
        assertEquals(2.5, poissonDistribution.getMean(), 0.001);
        for (int i = 0; i < 10; i++) {
            int result = poissonDistribution.sample();
            assertTrue(result >= 0 && result <= 10 * 2.5);
        }
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testPoissonDistributionWithInvalidMean() {
        new PoissonDistributionImpl(-1.5);
    }

    @Test
    public void testProbabilityWithValidParameters() {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(3.5);
        double probability = poissonDistribution.probability(2);
        assertEquals(0.21568365267631353, probability, 0.0000001);
    }

    @Test
    public void testProbabilityWithInvalidParameter() {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(3.5);
        try {
            poissonDistribution.probability(-1);
        } catch (IllegalArgumentException e) {
            assertEquals("x should be non-negative", e.getMessage());
        }
    }

    @Test(expected = MathRuntimeException.class)
    public void testCumulativeProbabilityWithInvalidParameter() {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(3.5);
        poissonDistribution.cumulativeProbability(-1);
    }

    @Test
    public void testCumulativeProbabilityWithValidParameters() {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(3.5);
        double cumulativeProbability = poissonDistribution.cumulativeProbability(5);
        double expectedCumulativeProbability = Gamma.regularizedGammaP(5.5, 3.5);
        assertEquals(expectedCumulativeProbability, cumulativeProbability, 0.000001);
    }

    @Test
    public void testNormalApproximateProbabilityWithValidParameters() {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(3.5);
        double normalApproximateProbability = poissonDistribution.normalApproximateProbability(5);
        NormalDistribution normalDistribution = new NormalDistributionImpl(3.5, FastMath.sqrt(3.5));
        double expectedNormalApproximateProbability = normalDistribution.cumulativeProbability(5.5);
        assertEquals(expectedNormalApproximateProbability, normalApproximateProbability, 0.000001);
    }

    @Test
    public void testSampleWithValidParameters() {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(3.5);
        int result = poissonDistribution.sample();
        assertTrue(result >= 0 && result <= 10 * 3.5);
    }

    @Test
    public void testSampleWithLargeMean() {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(10000.0);
        int result = poissonDistribution.sample();
        assertTrue(result >= 0 && result <= 10000 * 10000);
    }

    @Test
    public void testInverseCumulativeProbability() {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(3.5);
        double probability = 0.95;
        int expectedResult = (int) Math.ceil(poissonDistribution.inverseCumulativeProbability(probability));
        int result = poissonDistribution.sample();
        assertEquals(expectedResult, result);
    }

    @Test
    public void testInverseCumulativeProbabilityWithZeroProbability() {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(3.5);
        double probability = 0.0;
        int expectedResult = 0;
        int result = (int) poissonDistribution.inverseCumulativeProbability(probability);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testInverseCumulativeProbabilityWithOneProbability() {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(3.5);
        double probability = 1.0;
        int expectedResult = Integer.MAX_VALUE;
        int result = (int) poissonDistribution.inverseCumulativeProbability(probability);
        assertEquals(expectedResult, result);
    }
}