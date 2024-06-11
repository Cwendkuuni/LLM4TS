package org.apache.commons.math3.distribution;

import static org.junit.Assert.*;
import static org.apache.commons.math3.util.Precision.abs;
import static org.apache.commons.math3.util.Precision.eq;
import static org.apache.commons.math3.util.Precision.isClose;
import org.junit.Test;

public class HypergeometricDistributionTest {

    @Test
    public void testHypergeometricDistributionConstruction() {
        try {
            HypergeometricDistribution hDist = new HypergeometricDistribution(1, 1, 1);
            assertEquals(1, hDist.getNumberOfSuccesses());
            assertEquals(1, hDist.getPopulationSize());
            assertEquals(1, hDist.getSampleSize());
            assertEquals(0.5, hDist.probability(0), 0.001);
            assertEquals(0.5, hDist.probability(1), 0.001);
            assertEquals(1.0, hDist.cumulativeProbability(1), 0.001);
            assertEquals(0.0, hDist.cumulativeProbability(2), 0.001);
        } catch (Exception e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }

        try {
            HypergeometricDistribution hDist = new HypergeometricDistribution(1, 2, 1);
            assertEquals(2, hDist.getNumberOfSuccesses());
            assertEquals(1, hDist.getPopulationSize());
            assertEquals(1, hDist.getSampleSize());
            assertEquals(0.0, hDist.probability(0), 0.001);
            assertEquals(1.0, hDist.probability(1), 0.001);
            assertEquals(1.0, hDist.probability(2), 0.001);
            assertEquals(1.0, hDist.cumulativeProbability(2), 0.001);
        } catch (Exception e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }

        try {
            HypergeometricDistribution hDist = new HypergeometricDistribution(1, 2, 2);
            assertEquals(2, hDist.getNumberOfSuccesses());
            assertEquals(1, hDist.getPopulationSize());
            assertEquals(2, hDist.getSampleSize());
            assertEquals(0.0, hDist.probability(0), 0.001);
            assertEquals(1.0 / 3.0, hDist.probability(1), 0.001);
            assertEquals(1.0 / 3.0, hDist.probability(2), 0.001);
            assertEquals(2.0 / 3.0, hDist.cumulativeProbability(2), 0.001);
        } catch (Exception e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }

        try {
            HypergeometricDistribution hDist = new HypergeometricDistribution(1, 2, 3);
            assertEquals(2, hDist.getNumberOfSuccesses());
            assertEquals(1, hDist.getPopulationSize());
            assertEquals(3, hDist.getSampleSize());
            assertEquals(0.0, hDist.probability(0), 0.001);
            assertEquals(1.0 / 4.0, hDist.probability(1), 0.001);
            assertEquals(1.0 / 4.0, hDist.probability(2), 0.001);
            assertEquals(1.0 / 2.0, hDist.probability(3), 0.001);
            assertEquals(3.0 / 4.0, hDist.cumulativeProbability(3), 0.001);
        } catch (Exception e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }

        try {
            HypergeometricDistribution hDist = new HypergeometricDistribution(1, 2, 4);
            assertEquals(2, hDist.getNumberOfSuccesses());
            assertEquals(1, hDist.getPopulationSize());
            assertEquals(4, hDist.getSampleSize());
            assertEquals(0.0, hDist.probability(0), 0.001);
            assertEquals(1.0 / 6.0, hDist.probability(1), 0.001);
            assertEquals(1.0 / 6.0, hDist.probability(2), 0.001);
            assertEquals(1.0 / 3.0, hDist.probability(3), 0.001);
            assertEquals(1.0 / 2.0, hDist.probability(4), 0.001);
            assertEquals(1.0, hDist.cumulativeProbability(4), 0.001);
        } catch (Exception e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }

        try {
            HypergeometricDistribution hDist = new HypergeometricDistribution(1, -1, 1);
            fail("Should not be able to create a HypergeometricDistribution with a negative number of successes.");
        } catch (NumberIsTooLargeException e) {
            // Expected
        }

        try {
            HypergeometricDistribution hDist = new HypergeometricDistribution(1, 1, -1);
            fail("Should not be able to create a HypergeometricDistribution with a negative sample size.");
        } catch (NumberIsTooLargeException e) {
            // Expected
        }

        try {
            HypergeometricDistribution hDist = new HypergeometricDistribution(1, 1, Integer.MAX_VALUE);
            fail("Should not be able to create a HypergeometricDistribution with a sample size larger than the population size.");
        } catch (NumberIsTooLargeException e) {
            // Expected
        }

        try {
            HypergeometricDistribution hDist = new HypergeometricDistribution(1, 1, Integer.MIN_VALUE);
            fail("Should not be able to create a HypergeometricDistribution with a sample size smaller than zero.");
        } catch (NumberIsTooLargeException e) {
            // Expected
        }

        try {
            HypergeometricDistribution hDist = new HypergeometricDistribution(0, 1, 1);
            fail("Should not be able to create a HypergeometricDistribution with a population size equal to zero.");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }
    }

    @Test
    public void testHypergeometricDistributionProbability() {
        HypergeometricDistribution hDist = new HypergeometricDistribution(10, 3, 100);
        assertEquals(0.3, hDist.probability(3), 0.001);
        assertEquals(0.27832726580238, hDist.probability(4), 0.001);
        assertEquals(0.12654124106881, hDist.probability(5), 0.001);
        assertEquals(0.02878787878788, hDist.probability(6), 0.001);
        assertEquals(0.00278327265801, hDist.probability(7), 0.001);
        assertEquals(0.00027832726580, hDist.probability(8), 0.001);
        assertEquals(0.00002783272658, hDist.probability(9), 0.001);
        assertEquals(0.00000278327266, hDist.probability(10), 0.001);

        HypergeometricDistribution hDist2 = new HypergeometricDistribution(10, 7, 100);
        assertEquals(0.388888888889, hDist2.probability(7), 0.001);
        assertEquals(0.305555555556, hDist2.probability(8), 0.001);
        assertEquals(0.177777777778, hDist2.probability(9), 0.001);
        assertEquals(0.066666666667, hDist2.probability(10), 0.001);
    }

    @Test
    public void testHypergeometricDistributionCumulativeProbability() {
        HypergeometricDistribution hDist = new HypergeometricDistribution(10, 3, 100);
        assertEquals(0.0, hDist.cumulativeProbability(0), 0.001);
        assertEquals(0.03, hDist.cumulativeProbability(3), 0.001);
        assertEquals(0.32, hDist.cumulativeProbability(6), 0.001);
        assertEquals(0.97, hDist.cumulativeProbability(9), 0.001);
        assertEquals(1.0, hDist.cumulativeProbability(10), 0.001);

        HypergeometricDistribution hDist2 = new HypergeometricDistribution(10, 7, 100);
        assertEquals(0.0, hDist2.cumulativeProbability(0), 0.001);
        assertEquals(0.111111111111, hDist2.cumulativeProbability(1), 0.001);
        assertEquals(0.352941176471, hDist2.cumulativeProbability(4), 0.001);
        assertEquals(0.997222222222, hDist2.cumulativeProbability(9), 0.001);
        assertEquals(1.0, hDist2.cumulativeProbability(10), 0.001);
    }

    @Test
    public void testHypergeometricDistributionNumericalVariance() {
        HypergeometricDistribution hDist = new HypergeometricDistribution(10, 5, 100);
        assertEquals(0.545, hDist.getNumericalVariance(), 0.001);
    }
}