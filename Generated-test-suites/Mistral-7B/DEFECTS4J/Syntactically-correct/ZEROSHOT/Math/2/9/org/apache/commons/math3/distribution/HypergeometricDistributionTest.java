import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Test;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest {

    @Test
    public void testHypergeometricDistributionConstruction() {
        // Test valid construction
        int populationSize = 10;
        int numberOfSuccesses = 4;
        int sampleSize = 5;
        HypergeometricDistribution hd = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        assertEquals(populationSize, hd.getPopulationSize());
        assertEquals(numberOfSuccesses, hd.getNumberOfSuccesses());
        assertEquals(sampleSize, hd.getSampleSize());

        // Test exception when population size is non-positive
        try {
            new HypergeometricDistribution(0, numberOfSuccesses, sampleSize);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }

        // Test exception when number of successes is negative
        try {
            new HypergeometricDistribution(populationSize, -1, sampleSize);
            fail("Expected NotPositiveException");
        } catch (NotPositiveException e) {
            // Expected
        }

        // Test exception when sample size is negative
        try {
            new HypergeometricDistribution(populationSize, numberOfSuccesses, -1);
            fail("Expected NotPositiveException");
        } catch (NotPositiveException e) {
            // Expected
        }

        // Test exception when number of successes is larger than population size
        try {
            new HypergeometricDistribution(populationSize, populationSize + 1, sampleSize);
            fail("Expected NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            // Expected
        }

        // Test exception when sample size is larger than population size
        try {
            new HypergeometricDistribution(populationSize, numberOfSuccesses, populationSize + 1);
            fail("Expected NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            // Expected
        }
    }

    @Test
    public void testCumulativeProbability() {
        int populationSize = 10;
        int numberOfSuccesses = 4;
        int sampleSize = 5;
        HypergeometricDistribution hd = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int[] domain = hd.getDomain();
        for (int x = domain[0]; x <= domain[1]; x++) {
            double cp = hd.cumulativeProbability(x);
            double np = hd.probability(x);
            double sum = cp + np;
            double eps = 1e-12;
            assertTrue("Error in cumulative probability calculation for x = " + x,
                    Math.abs(sum - 1.0) < eps);
        }
    }

    @Test
    public void testProbability() {
        int populationSize = 10;
        int numberOfSuccesses = 4;
        int sampleSize = 5;
        HypergeometricDistribution hd = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int[] domain = hd.getDomain();
        for (int x = domain[0]; x <= domain[1]; x++) {
            double p = hd.probability(x);
            double cp = hd.cumulativeProbability(x + 1) - hd.cumulativeProbability(x);
            double sum = p + cp;
            double eps = 1e-12;
            assertTrue("Error in probability calculation for x = " + x,
                    Math.abs(sum - 1.0) < eps);
        }
    }

    @Test
    public void testUpperCumulativeProbability() {
        int populationSize = 10;
        int numberOfSuccesses = 4;
        int sampleSize = 5;
        HypergeometricDistribution hd = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int[] domain = hd.getDomain();
        for (int x = domain[0]; x <= domain[1]; x++) {
            double ucp = hd.upperCumulativeProbability(x);
            double cpd = hd.cumulativeProbability(domain[1]);
            double sum = ucp + cpd - 1.0;
            double eps = 1e-12;
            assertTrue("Error in upper cumulative probability calculation for x = " + x,
                    Math.abs(sum) < eps);
        }
    }

    @Test
    public void testVariance() {
        int populationSize = 10;
        int numberOfSuccesses = 4;
        int sampleSize = 5;
        HypergeometricDistribution hd = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        double numericalVariance = hd.getNumericalVariance();
        double analyticalVariance = ((double) sampleSize * numberOfSuccesses * (populationSize - sampleSize) * (populationSize - numberOfSuccesses))
                / ((double) populationSize * populationSize * (populationSize - 1));
        double eps = 1e-12;
        assertTrue("Error in numerical variance calculation", Math.abs(numericalVariance - analyticalVariance) < eps);
    }
}