import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.stat.StatUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest {

    @Test
    public void testValidConstruction() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 3, 5);
        assertEquals(10, dist.getPopulationSize());
        assertEquals(3, dist.getNumberOfSuccesses());
        assertEquals(5, dist.getSampleSize());
    }

    @Test(expected = NotPositiveException.class)
    public void testInvalidNumberOfSuccesses() {
        new HypergeometricDistribution(10, -1, 5);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testInvalidPopulationSize() {
        new HypergeometricDistribution(0, 3, 5);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testInvalidNumberOfSuccessesTooLarge() {
        new HypergeometricDistribution(10, 11, 5);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testInvalidSampleSize() {
        new HypergeometricDistribution(10, 3, 11);
    }

    @Test
    public void testProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 3, 5);
        double prob = dist.probability(2);
        assertNotNull(prob);
        assertEquals(0.2768, prob, 0.001);
    }

    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 3, 5);
        double prob = dist.cumulativeProbability(2);
        assertNotNull(prob);
        assertEquals(0.3013, prob, 0.001);
    }

    @Test
    public void testUpperCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 3, 5);
        double prob = dist.upperCumulativeProbability(2);
        assertNotNull(prob);
        assertEquals(0.6987, prob, 0.001);
    }

    @Test
    public void testMean() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 3, 5);
        double mean = dist.getNumericalMean();
        assertEquals(2.5, mean, 0.01);
    }

    @Test
    public void testVariance() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 3, 5);
        double variance = dist.getNumericalVariance();
        assertEquals(0.5784, variance, 0.01);
    }

    @Test
    public void testGetSupportLowerBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 3, 5);
        int lowerBound = dist.getSupportLowerBound();
        assertEquals(3, lowerBound);
    }

    @Test
    public void testGetSupportUpperBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 3, 5);
        int upperBound = dist.getSupportUpperBound();
        assertEquals(5, upperBound);
    }

    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 3, 5);
        assertTrue(dist.isSupportConnected());
    }

    @Test
    public void testGenerateSample() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 3, 5);
        int[] samples = new int[1000];
        for (int i = 0; i < 1000; i++) {
            samples[i] = dist.nextInt();
        }
        double mean = StatUtils.mean(samples);
        assertEquals(2.5, mean, 0.01);
    }
}