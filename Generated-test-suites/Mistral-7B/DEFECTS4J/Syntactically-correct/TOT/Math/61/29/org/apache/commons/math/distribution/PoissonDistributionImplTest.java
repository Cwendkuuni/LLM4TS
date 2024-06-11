import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.distribution.PoissonDistributionImpl;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MathRuntimeException;
import org.junit.Assert;
import org.junit.Test;

public class PoissonDistributionImplTest {

    @Test
    public void testPoissonDistributionWithValidMean() {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(5.0);

        // Test probability mass function
        int[] expectedProbabilities = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        double[] probabilities = new double[11];
        for (int i = 0; i < 11; i++) {
            probabilities[i] = poissonDistribution.probability(i);
            Assert.assertEquals("Unexpected probability for x = " + i, expectedProbabilities[i], probabilities[i], 1e-10);
        }

        // Test cumulative distribution function
        double[] expectedCumulativeProbabilities = {0.0, 0.00673645820388, 0.0132735195255, 0.0313895352826, 0.0606512325521, 0.100782800973, 0.163260627438, 0.249738462161, 0.373144781761, 0.543951823126, 0.721884947718};
        double[] cumulativeProbabilities = new double[11];
        for (int i = 0; i < 11; i++) {
            cumulativeProbabilities[i] = poissonDistribution.cumulativeProbability(i);
            Assert.assertEquals("Unexpected cumulative probability for x = " + i, expectedCumulativeProbabilities[i], cumulativeProbabilities[i], 1e-10);
        }

        // Test normal approximation of probability
        for (int i = 0; i < 11; i++) {
            double normalApproximateProbability = poissonDistribution.normalApproximateProbability(i);
            double difference = Math.abs(probabilities[i] - normalApproximateProbability);
            Assert.assertTrue("The difference between the actual and normal approximation probabilities is too large for x = " + i, difference < 0.01);
        }

        // Test sampling
        for (int i = 0; i < 1000; i++) {
            int sample = poissonDistribution.sample();
            Assert.assertTrue("Sample is negative", sample >= 0);
            Assert.assertTrue("Sample is greater than expected maximum", sample <= 1000 * 5);
        }
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testPoissonDistributionWithInvalidMean() {
        new PoissonDistributionImpl(-1.0);
    }

    @Test
    public void testPoissonDistributionWithMaxIterationsAndEpsilon() {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(100000.0, 1e-15, 1000000);

        // Test probability mass function
        int[] expectedProbabilities = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 100000};
        double[] probabilities = new double[12];
        for (int i = 0; i < 12; i++) {
            probabilities[i] = poissonDistribution.probability(i);
            Assert.assertEquals("Unexpected probability for x = " + i, expectedProbabilities[i], probabilities[i], 1e-15);
        }

        // Test cumulative distribution function
        double[] expectedCumulativeProbabilities = {0.0, 7.54827067661e-05, 0.000155737723621, 0.00031102982752, 0.00046416773761, 0.00061572382202, 0.00076721285723, 0.00091907758533, 0.001069761394, 0.001219129424, 0.001366930837, 0.5};
        double[] cumulativeProbabilities = new double[12];
        for (int i = 0; i < 12; i++) {
            cumulativeProbabilities[i] = poissonDistribution.cumulativeProbability(i);
            Assert.assertEquals("Unexpected cumulative probability for x = " + i, expectedCumulativeProbabilities[i], cumulativeProbabilities[i], 1e-15);
        }
    }
}