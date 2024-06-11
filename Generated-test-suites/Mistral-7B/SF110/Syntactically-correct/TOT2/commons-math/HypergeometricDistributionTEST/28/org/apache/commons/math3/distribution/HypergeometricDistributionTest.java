import org.junit.Test;
import org.junit.Assert.*;
import org.apache.commons.math3.distribution.HypergeometricDistribution;

public class HypergeometricDistributionTest {

    @Test
    public void testHypergeometricDistributionConstruction() {
        try {
            HypergeometricDistribution hd = new HypergeometricDistribution(5, 2, 3);
            int populationSize = hd.getPopulationSize();
            int numberOfSuccesses = hd.getNumberOfSuccesses();
            int sampleSize = hd.getSampleSize();
            assertEquals(5, populationSize);
            assertEquals(2, numberOfSuccesses);
            assertEquals(3, sampleSize);
        } catch (NotPositiveException | NotStrictlyPositiveException | NumberIsTooLargeException e) {
            fail("Exception should not be thrown");
        }
    }
}