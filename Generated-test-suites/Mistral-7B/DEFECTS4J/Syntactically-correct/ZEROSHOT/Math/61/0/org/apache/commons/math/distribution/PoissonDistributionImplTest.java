import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.distribution.PoissonDistributionImpl;
import org.apache.commons.math3.special.Gamma;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PoissonDistributionImplTest {

    private PoissonDistributionImpl poissonDistribution;

    @Before
    public void setUp() {
        poissonDistribution = new PoissonDistributionImpl(5.0);
    }

    @Test
    public void testProbability() {
        int[] expectedProbabilities = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
        int[] actualProbabilities = new int[12];

        for (int i = 0; i < 12; i++) {
            actualProbabilities[i] = (int) Math.round(poissonDistribution.probability(i));
        }

        Assert.assertArrayEquals(expectedProbabilities, actualProbabilities);
    }

    @Test
    public void testCumulativeProbability() throws Exception {
        double[] expectedCumulativeProbabilities = {0.0, 0.0067, 0.0599, 0.1443, 0.3089, 0.5133, 0.7017, 0.8634, 0.9693, 0.9993, 0.9999, 1.0};
        double[] actualCumulativeProbabilities = new double[12];

        for (int i = 0; i < 12; i++) {
            actualCumulativeProbabilities[i] = poissonDistribution.cumulativeProbability(i);
        }

        Assert.assertArrayEquals(expectedCumulativeProbabilities, actualCumulativeProbabilities, 0.0001);
    }

    @Test
    public void testNormalApproximateProbability() throws Exception {
        double EPSILON = 0.0001;
        int[] expectedProbabilities = {0, 4, 10, 17, 23, 28, 34, 40, 46, 52, 57, 63};
        int[] actualProbabilities = new int[12];

        for (int i = 0; i < 12; i++) {
            actualProbabilities[i] = (int) Math.round(poissonDistribution.normalApproximateProbability(i));
        }

        Assert.assertArrayEquals(expectedProbabilities, actualProbabilities);

        double totalError = 0.0;
        for (int i = 0; i < 12; i++) {
            double error = Math.abs(poissonDistribution.cumulativeProbability(i) - Gamma.cumulativeProbability(i + 1, poissonDistribution.getMean()));
            totalError += error;
        }

        Assert.assertTrue("The maximum difference between exact and normal approximation probabilities is " + totalError, totalError < EPSILON);
    }

    @Test
    public void testSample() throws Exception {
        int[] samples = new int[1000];
        for (int i = 0; i < 1000; i++) {
            samples[i] = poissonDistribution.sample();
        }

        int expectedCount = 0;
        for (int i = 0; i < 1000; i++) {
            if (samples[i] == 5) {
                expectedCount++;
            }
        }

        double probability = poissonDistribution.probability(5);
        double expectedFrequency = probability * 1000;
        Assert.assertEquals("The expected frequency of observing 5 is " + expectedFrequency, expectedFrequency, expectedCount, 0.01);
    }

    @Test(expected = MathException.class)
    public void testInvalidMean() {
        new PoissonDistributionImpl(-1.0);
    }
}