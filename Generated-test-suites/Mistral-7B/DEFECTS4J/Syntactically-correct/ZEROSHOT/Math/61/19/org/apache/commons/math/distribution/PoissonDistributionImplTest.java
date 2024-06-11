import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.special.Gamma;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.RandomData;
import org.junit.Test;

public class PoissonDistributionImplTest {

    @Test
    public void testConstructors() {
        double mean = 3.0;
        PoissonDistribution poissonDist = new PoissonDistributionImpl(mean);
        assertEquals(mean, poissonDist.getMean(), 0.0);

        double epsilon = 0.0001;
        int maxIterations = 10000;
        PoissonDistribution poissonDist2 = new PoissonDistributionImpl(mean, epsilon, maxIterations);
        assertEquals(mean, poissonDist2.getMean(), 0.0);
        assertEquals(epsilon, poissonDist2.epsilon, 0.0);
        assertEquals(maxIterations, poissonDist2.maxIterations, 0);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testNegativeMean() {
        double negativeMean = -3.0;
        new PoissonDistributionImpl(negativeMean);
    }

    @Test
    public void testProbability() {
        double mean = 3.0;
        PoissonDistribution poissonDist = new PoissonDistributionImpl(mean);

        int x = 0;
        double probability = poissonDist.probability(x);
        assertEquals(FastMath.exp(-mean), probability, 1e-5);

        x = 3;
        probability = poissonDist.probability(x);
        double factorial = Gamma.factorial(x);
        double normalApproximation = FastMath.exp(-mean) * FastMath.pow(FastMath.E, mean) / factorial;
        assertEquals(normalApproximation, probability, 1e-4);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        double mean = 3.0;
        PoissonDistribution poissonDist = new PoissonDistributionImpl(mean);

        int x = 2;
        double cumulativeProbability = poissonDist.cumulativeProbability(x);
        double expectedCumulativeProbability = 0.997330194228856;
        assertEquals(expectedCumulativeProbability, cumulativeProbability, 1e-5);

        x = 10;
        cumulativeProbability = poissonDist.cumulativeProbability(x);
        expectedCumulativeProbability = 0.9999999999999998;
        assertEquals(expectedCumulativeProbability, cumulativeProbability, 1e-10);
    }

    @Test
    public void testNormalApproximateProbability() throws MathException {
        double mean = 3.0;
        PoissonDistribution poissonDist = new PoissonDistributionImpl(mean);

        int x = 2;
        double normalApproximateProbability = poissonDist.normalApproximateProbability(x);
        double expectedNormalApproximateProbability = 0.997330194228856;
        assertEquals(expectedNormalApproximateProbability, normalApproximateProbability, 1e-5);

        x = 10;
        normalApproximateProbability = poissonDist.normalApproximateProbability(x);
        expectedNormalApproximateProbability = 0.9999999999999998;
        assertEquals(expectedNormalApproximateProbability, normalApproximateProbability, 1e-10);
    }

    @Test
    public void testSample() throws MathException {
        double mean = 3.0;
        PoissonDistribution poissonDist = new PoissonDistributionImpl(mean);

        int sampledValue = poissonDist.sample();
        assertTrue(sampledValue >= 0);
        assertEquals(Precision.isClose(mean, (double) sampledValue, 1e-3), true);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        double mean = 3.0;
        PoissonDistribution poissonDist = new PoissonDistributionImpl(mean);

        double probability = 0.9;
        int inverseCumulativeProbability = poissonDist.inverseCumulativeProbability(probability);
        int expectedInverseCumulativeProbability = 2;
        assertEquals(expectedInverseCumulativeProbability, inverseCumulativeProbability);

        probability = 0.999;
        inverseCumulativeProbability = poissonDist.inverseCumulativeProbability(probability);
        expectedInverseCumulativeProbability = 9;
        assertEquals(expectedInverseCumulativeProbability, inverseCumulativeProbability);
    }

    @Test
    public void testSampleUsingNormalApproximation() {
        double mean = 3.0;
        PoissonDistribution poissonDist = new PoissonDistributionImpl(mean);

        int sampledValue = poissonDist.sampleUsingNormalApproximation();
        assertTrue(sampledValue >= 0);
        assertEquals(Precision.isClose(mean, (double) sampledValue, 1e-3), true);
    }
}