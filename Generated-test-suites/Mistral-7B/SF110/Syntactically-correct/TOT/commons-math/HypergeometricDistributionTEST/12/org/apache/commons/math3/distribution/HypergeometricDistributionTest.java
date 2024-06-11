import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import org.junit.Assert.*;

public class HypergeometricDistributionTest {

    @Test
    public void testConstructorWithValidParameters() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
        assertEquals(10, distribution.getPopulationSize());
        assertEquals(5, distribution.getNumberOfSuccesses());
        assertEquals(5, distribution.getSampleSize());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithNegativePopulationSize() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), -5, 5, 5);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructorWithNegativeNumberOfSuccesses() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 10, -5, 5);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructorWithNegativeSampleSize() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 10, 5, -5);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithNumberOfSuccessesLargerThanPopulationSize() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 10, 11, 5);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithSampleSizeLargerThanPopulationSize() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 10, 5, 11);
    }

    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
        int[] domain = distribution.getDomain();

        double cumulativeProbability = distribution.cumulativeProbability(domain[0]);
        assertEquals(0.0, cumulativeProbability, 0.001);

        for (int x = domain[0]; x <= domain[1]; x++) {
            cumulativeProbability = distribution.cumulativeProbability(x);
            double expectedCumulativeProbability = 0.0;
            for (int i = domain[0]; i < x; i++) {
                expectedCumulativeProbability += distribution.probability(i);
            }
            assertEquals(expectedCumulativeProbability, cumulativeProbability, 0.001);
        }

        cumulativeProbability = distribution.cumulativeProbability(domain[1] + 1);
        assertEquals(1.0, cumulativeProbability, 0.001);
    }

    @Test
    public void testGetDomain() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);

        int[] domain = distribution.getDomain();
        assertEquals(5, domain[0]);
        assertEquals(7, domain[1]);
    }

    @Test
    public void testProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);

        for (int x = 0; x <= 10; x++) {
            double probability = distribution.probability(x);
            double expectedProbability = 0.0;

            if (x < 5) {
                expectedProbability = FastMath.comb(10, x) * Math.pow(0.5, x) * Math.pow(0.5, 10 - x);
            } else if (x > 5) {
                expectedProbability = FastMath.comb(10, x) * Math.pow(0.5, x) * Math.pow(0.5, 5) * Math.pow(0.5, 5);
            } else {
                expectedProbability = 1.0;
            }

            assertEquals(expectedProbability, probability, 0.001);
        }
    }

    @Test
    public void testUpperCumulativeProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
        int[] domain = distribution.getDomain();

        double upperCumulativeProbability = distribution.upperCumulativeProbability(domain[0]);
        assertEquals(1.0, upperCumulativeProbability, 0.001);

        for (int x = domain[0]; x < domain[1]; x++) {
            upperCumulativeProbability = distribution.upperCumulativeProbability(x);
            double expectedUpperCumulativeProbability = 1.0;
            for (int i = x + 1; i <= domain[1]; i++) {
                expectedUpperCumulativeProbability += distribution.probability(i);
            }
            assertEquals(1.0 - expectedUpperCumulativeProbability, upperCumulativeProbability, 0.001);
        }

        upperCumulativeProbability = distribution.upperCumulativeProbability(domain[1]);
        assertEquals(0.0, upperCumulativeProbability, 0.001);
    }

    @Test
    public void testNumericalMean() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
        assertEquals(5.0, distribution.getNumericalMean(), 0.001);
    }

    @Test
    public void testNumericalVariance() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
        assertEquals(0.03333333333333333, distribution.getNumericalVariance(), 0.0001);
    }
}