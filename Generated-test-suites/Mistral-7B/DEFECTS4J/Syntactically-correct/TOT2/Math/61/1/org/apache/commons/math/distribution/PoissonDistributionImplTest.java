import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.special.Gamma;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class PoissonDistributionImplTest {

    @Test
    public void testProbabilityMethod() {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(2.5);
        double expectedProbability = 0.118232; // calculated using Poisson PMF formula
        double actualProbability = poissonDistribution.probability(1);
        assertEquals(expectedProbability, actualProbability, 0.0001);
    }

    @Test
    public void testCumulativeProbabilityMethod() throws Exception {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(5.0);
        double expectedCumulativeProbability = 0.999497; // calculated using Poisson CDF formula
        double actualCumulativeProbability = poissonDistribution.cumulativeProbability(4);
        assertEquals(expectedCumulativeProbability, actualCumulativeProbability, 0.0001);
    }

    @Test
    public void testNormalApproximateProbabilityMethod() throws Exception {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(3.0);
        double expectedNormalApproximateProbability = 0.997349; // calculated using Normal Approximation
        double actualNormalApproximateProbability = poissonDistribution.normalApproximateProbability(2);
        assertEquals(expectedNormalApproximateProbability, actualNormalApproximateProbability, 0.0001);
    }

    @Test(expected = MathException.class)
    public void testCumulativeProbabilityNegativeValue() throws Exception {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(2.5);
        poissonDistribution.cumulativeProbability(-1);
    }

    @Test(expected = MathException.class)
    public void testCumulativeProbabilityMaxValue() throws Exception {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(2.5);
        poissonDistribution.cumulativeProbability(Integer.MAX_VALUE);
    }

    @Test
    public void testSampleMethod() throws Exception {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(3.0);
        int expectedSample = 3;
        int actualSample = poissonDistribution.sample();
        assertEquals(expectedSample, actualSample);
    }
}