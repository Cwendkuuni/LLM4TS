import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest {

    @Test
    public void testConstructWithValidArguments() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        assertEquals(populationSize, distribution.getPopulationSize());
        assertEquals(numberOfSuccesses, distribution.getNumberOfSuccesses());
        assertEquals(sampleSize, distribution.getSampleSize());
        int[] domain = distribution.getDomain();
        assertEquals(5, domain[0]);
        assertEquals(8, domain[1]);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructWithNegativeNumberOfSuccesses() {
        int populationSize = 10;
        int numberOfSuccesses = -2;
        int sampleSize = 3;

        try {
            new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
            fail("An exception should have been thrown");
        } catch (NotPositiveException e) {
            // expected
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructWithNonPositivePopulationSize() {
        int populationSize = 0;
        int numberOfSuccesses = 5;
        int sampleSize = 3;

        try {
            new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
            fail("An exception should have been thrown");
        } catch (NotStrictlyPositiveException e) {
            // expected
        }
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructWithNumberOfSuccessesGreaterThanPopulationSize() {
        int populationSize = 10;
        int numberOfSuccesses = 12;
        int sampleSize = 3;

        try {
            new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
            fail("An exception should have been thrown");
        } catch (NumberIsTooLargeException e) {
            // expected
        }
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructWithSampleSizeGreaterThanPopulationSize() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 11;

        try {
            new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
            fail("An exception should have been thrown");
        } catch (NumberIsTooLargeException e) {
            // expected
        }
    }

    @Test
    public void testCumulativeProbability() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int[] domain = distribution.getDomain();
        for (int x = domain[0]; x <= domain[1]; x++) {
            double expected = innerCumulativeProbability(domain[0], x, 1, populationSize, numberOfSuccesses, sampleSize);
            double actual = distribution.cumulativeProbability(x);
            assertEquals(expected, actual, 0.0001);
        }
    }

    private static double innerCumulativeProbability(int x0, int x1, int dx, int N, int m, int n) {
        double p = (double) n / (double) N;
        double q = (double) (N - n) / (double) N;
        double p1 = SaddlePointExpansion.logBinomialProbability(x1, m, p, q);
        double p2 = SaddlePointExpansion.logBinomialProbability(n - x1, N - m, p, q);
        double p3 = SaddlePointExpansion.logBinomialProbability(n, N, p, q);
        return Math.exp(p1 + p2 - p3);
    }

    @Test
    public void testProbability() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int[] domain = distribution.getDomain();
        for (int x = domain[0]; x <= domain[1]; x++) {
            double expected = SaddlePointExpansion.logBinomialProbability(x, numberOfSuccesses,
                    (double) sampleSize / (double) populationSize,
                    (double) (populationSize - sampleSize) / (double) populationSize);
            double actual = distribution.probability(x);
            assertEquals(expected, actual, 0.0001);
        }
    }

    @Test
    public void testUpperCumulativeProbability() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int[] domain = distribution.getDomain();
        for (int x = domain[0]; x <= domain[1]; x++) {
            double expected = innerCumulativeProbability(x, domain[1], -1, populationSize, numberOfSuccesses, sampleSize);
            double actual = distribution.upperCumulativeProbability(x);
            assertEquals(expected, actual, 0.0001);
        }
    }

    @Test
    public void testGetNumericalMean() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        double expected = (double) sampleSize * numberOfSuccesses / (double) populationSize;
        double actual = distribution.getNumericalMean();
        assertEquals(expected, actual, 0.0001);
    }

    @Test
    public void testGetNumericalVariance() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        double expected = ((double) sampleSize * numberOfSuccesses * (populationSize - sampleSize) * (populationSize - numberOfSuccesses)) / (double) (populationSize * populationSize * (populationSize - 1));
        double actual = distribution.getNumericalVariance();
        assertEquals(expected, actual, 0.0001);
    }

    @Test
    public void testGetSupportLowerBound() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int expected = FastMath.max(0,sampleSize + numberOfSuccesses - populationSize);
        int actual = distribution.getSupportLowerBound();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetSupportUpperBound() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int expected = FastMath.min(numberOfSuccesses, sampleSize);
        int actual = distribution.getSupportUpperBound();
        assertEquals(expected, actual);
    }

    @Test
    public void testIsSupportConnected() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        boolean expected = true;
        boolean actual = distribution.isSupportConnected();
        assertEquals(expected, actual);
    }
}