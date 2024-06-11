import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.distribution.HypergeometricDistributionTest;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.junit.Test;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest {

    @Test
    public void testConstruction() {
        try {
            new HypergeometricDistribution(1, 1, 1);
            fail("Expected exception when population size is 1.");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }

        try {
            new HypergeometricDistribution(-1, 1, 1);
            fail("Expected exception when number of successes is negative.");
        } catch (NotPositiveException e) {
            // Expected
        }

        try {
            new HypergeometricDistribution(1, -1, 1);
            fail("Expected exception when number of successes is negative.");
        } catch (NotPositiveException e) {
            // Expected
        }

        try {
            new HypergeometricDistribution(1, 2, -1);
            fail("Expected exception when sample size is negative.");
        } catch (NotPositiveException e) {
            // Expected
        }

        try {
            new HypergeometricDistribution(1, 2, 3);
            fail("Expected exception when sample size is larger than population size.");
        } catch (NumberIsTooLargeException e) {
            // Expected
        }

        try {
            new HypergeometricDistribution(1, 2, 1);
        } catch (Exception e) {
            fail("Unexpected exception.");
        }

        HypergeometricDistribution hd = new HypergeometricDistribution(10, 5, 5);
        assertEquals(5, hd.getNumberOfSuccesses());
        assertEquals(10, hd.getPopulationSize());
        assertEquals(5, hd.getSampleSize());
    }

    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution hd = new HypergeometricDistribution(10, 5, 5);

        int[] domain = hd.getDomain();
        for (int x = domain[0]; x <= domain[1]; x++) {
            double expected = hd.cumulativeProbability(x);
            double observed = HypergeometricDistributionTest.obtainCumulativeProbability(hd, x);
            assertEquals(expected, observed, 1e-9);
        }
    }

    @Test
    public void testProbability() {
        HypergeometricDistribution hd = new HypergeometricDistribution(10, 5, 5);

        int[] domain = hd.getDomain();
        for (int x = domain[0]; x <= domain[1]; x++) {
            double expected = hd.probability(x);
            double observed = HypergeometricDistributionTest.obtainProbability(hd, x);
            assertEquals(expected, observed, 1e-9);
        }
    }

    @Test
    public void testUpperCumulativeProbability() {
        HypergeometricDistribution hd = new HypergeometricDistribution(10, 5, 5);

        int[] domain = hd.getDomain();
        for (int x = domain[0]; x <= domain[1]; x++) {
            double expected = hd.upperCumulativeProbability(x);
            double observed = HypergeometricDistributionTest.obtainUpperCumulativeProbability(hd, x);
            assertEquals(expected, observed, 1e-9);
        }
    }

    @Test
    public void testMean() {
        HypergeometricDistribution hd = new HypergeometricDistribution(10, 5, 5);
        double expected = (double) (5 * 5 / 10.0);
        double observed = hd.getNumericalMean();
        assertEquals(expected, observed, 1e-9);
    }

    @Test
    public void testVariance() {
        HypergeometricDistribution hd = new HypergeometricDistribution(10, 5, 5);
        double expected = (5 * 5 * (10 - 5) * (10 - 5)) / (10 * 10 * (10 - 1));
        double observed = hd.getNumericalVariance();
        assertEquals(expected, observed, 1e-9);
    }

    private static double obtainCumulativeProbability(HypergeometricDistribution hd, int x) {
        return hd.cumulativeProbability(x);
    }

    private static double obtainProbability(HypergeometricDistribution hd, int x) {
        return hd
            .probability(x);
    }

    private static double obtainUpperCumulativeProbability(HypergeometricDistribution hd, int x) {
        return hd.upperCumulativeProbability(x);
    }
}