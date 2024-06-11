import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.junit.Test;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest {
    @Test
    public void testHypergeometricDistributionConstruction() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        assertEquals(populationSize, distribution.getPopulationSize());
        assertEquals(numberOfSuccesses, distribution.getNumberOfSuccesses());
        assertEquals(sampleSize, distribution.getSampleSize());

        // Testing for invalid inputs
        try {
            new HypergeometricDistribution(-5, numberOfSuccesses, sampleSize);
            fail("Expected NotPositiveException");
        } catch (NotPositiveException e) {
            // Expected
        }

        try {
            new HypergeometricDistribution(populationSize, -numberOfSuccesses, sampleSize);
            fail("Expected NotPositiveException");
        } catch (NotPositiveException e) {
            // Expected
        }

        try {
            new HypergeometricDistribution(populationSize, numberOfSuccesses, -sampleSize);
            fail("Expected NotPositiveException");
        } catch (NotPositiveException e) {
            // Expected
        }

        try {
            new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize + populationSize);
            fail("Expected NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            // Expected
        }

        try {
            new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize + numberOfSuccesses);
            fail("Expected NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            // Expected
        }
    }

    // Add test cases for other methods here
}