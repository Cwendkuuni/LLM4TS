import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.junit.Test;
import org.junit.Assert.*;

public class HypergeometricDistributionTest {

    @Test
    public void testConstructor() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;

        // Test valid input
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        int[] domain = distribution.getDomain();
        int expectedLowerDomain = 3;
        int expectedUpperDomain = 7;
        assertEquals(expectedLowerDomain, domain[0]);
        assertEquals(expectedUpperDomain, domain[1]);

        // Test exception when population size is not positive
        populationSize = 0;
        Assert.throwsException(NotStrictlyPositiveException.class, () -> new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize));

        // Test exception when number of successes is not positive
        numberOfSuccesses = -1;
        Assert.throwsException(NotPositiveException.class, () -> new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize));

        // Test exception when sample size is not positive
        sampleSize = -2;
        Assert.throwsException(NotPositiveException.class, () -> new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize));

        // Test exception when number of successes is larger than population size
        numberOfSuccesses = populationSize + 1;
        Assert.throwsException(NumberIsTooLargeException.class, () -> new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize));

        // Test exception when sample size is larger than population size
        sampleSize = populationSize + 1;
        Assert.throwsException(NumberIsTooLargeException.class, () -> new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize));
    }
}