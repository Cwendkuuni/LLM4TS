import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.special.Gamma;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.RandomData;

import org.apache.commons.math.distribution.PoissonDistributionImpl;

public class PoissonDistributionImplTest {

    @org.junit.Test
    public void testProbability() {
        PoissonDistribution dist = new PoissonDistributionImpl(2.5);
        for (int i = 0; i <= 10; i++) {
            double prob = dist.probability(i);
            double approx = Gamma.regularizedGammaP(i + 1, 2.5);
            assertEquals(String.format("i = %d, Probability = %f, Approximation = %f", i, prob, approx),
                    approx, prob, 0.001);
        }
    }

    @org.junit.Test
    public void testCumulativeProbability() throws Exception {
        PoissonDistribution dist = new PoissonDistributionImpl(2.5);
        for (int i = 0; i <= 10; i++) {
            double cdf = dist.cumulativeProbability(i);
            double approx = Gamma.regularizedGammaQ(i + 1, 2.5);
            assertEquals(String.format("i = %d, CDF = %f, Approximation = %f", i, cdf, approx),
                    approx, cdf, 0.001);
        }
    }

    @org.junit.Test
    public void testNormalApproximateProbability() throws Exception {
        PoissonDistribution dist = new PoissonDistributionImpl(2.5);
        for (int i = 0; i <= 10; i++) {
            double prob = dist.normalApproximateProbability(i);
            double cdf = dist.cumulativeProbability(i);
            assertEquals(String.format("i = %d, Probability = %f, CDF = %f", i, prob, cdf),
                    cdf, prob, 0.001);
        }
    }

    @org.junit.Test
    public void testSample() throws Exception {
        PoissonDistribution dist = new PoissonDistributionImpl(2.5);
        for (int i = 0; i < 100; i++) {
            int sample = dist.sample();
            assertTrue(sample >= 0 && sample <= 1000 * 2.5);
        }
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testNegativeMean() {
        new PoissonDistributionImpl(-1.0);
    }

    @org.junit.Test
    public void testInverseCumulativeProbability() throws Exception {
        PoissonDistribution dist = new PoissonDistributionImpl(2.5);
        for (double p = 0; p <= 1; p += 0.01) {
            int i = dist.inverseCumulativeProbability(p);
            double cdf = dist.cumulativeProbability(i);
            assertEquals(String.format("p = %f, Inverse CDF = %d, CDF = %f", p, i, cdf), p, cdf, 0.001);
        }
    }

    @org.junit.Test
    public void testSampleAccuracy() throws Exception {
        PoissonDistribution dist = new PoissonDistributionImpl(10.0);
        int samplesCount = 100000;
        int samplesSum = 0;
        for (int i = 0; i < samplesCount; i++) {
            samplesSum += dist.sample();
        }
        double mean = (double) samplesCount * dist.getMean() / samplesCount;
        assertEquals(String.format("Mean = %f, Samples sum = %d", mean, samplesSum), mean, samplesSum, Precision.DEFAULT);
    }
}