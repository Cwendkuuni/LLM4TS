package org.apache.commons.math3.distribution;

import org.apache.commons.math3.random.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.exception.util.*;
import org.apache.commons.math3.util.*;
import org.junit.*;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest {

    @Test
    public void testHypergeometricDistributionConstructorValidInput() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        assertEquals(populationSize, distribution.getPopulationSize());
        assertEquals(numberOfSuccesses, distribution.getNumberOfSuccesses());
        assertEquals(sampleSize, distribution.getSampleSize());
    }

    @Test
    public void testProbabilityValidInputAndEdgeCases() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 3, 5);

        int x = 0;
        double p = distribution.probability(x);
        assertEquals(p, 0.0, 0.001);

        x = 3;
        p = distribution.probability(x);
        assertEquals(p, 0.375, 0.001);

        x = 7;
        p = distribution.probability(x);
        assertEquals(p, 0.1875, 0.001);

        x = 10;
        p = distribution.probability(x);
        assertEquals(p, 0.0, 0.001);
    }

    @Test
    public void testCumulativeProbabilityValidInputAndEdgeCases() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 3, 5);

        int x = 0;
        double p = distribution.cumulativeProbability(x);
        assertEquals(p, 0.0, 0.001);

        x = 3;
        p = distribution.cumulativeProbability(x);
        assertEquals(p, 0.375, 0.001);

        x = 7;
        p = distribution.cumulativeProbability(x);
        assertEquals(p, 0.5625, 0.001);

        x = 10;
        p = distribution.cumulativeProbability(x);
        assertEquals(p, 1.0, 0.001);
    }

    @Test(expected = NotPositiveException.class)
    public void testHypergeometricDistributionConstructorNegativeInputs() {
        int populationSize = -1;
        int numberOfSuccesses = -1;
        int sampleSize = -1;

        assertThrows(NotPositiveException.class, () -> new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize));
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testHypergeometricDistributionConstructorZeroInputs() {
        int populationSize = 0;
        int numberOfSuccesses = 0;
        int sampleSize = 0;

        assertThrows(NotStrictlyPositiveException.class, () -> new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize));

        assertThrows(NotStrictlyPositiveException.class, () -> new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize + 1));
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testHypergeometricDistributionConstructorNumberTooLarge() {
        int populationSize = 10;
        int numberOfSuccesses = 11;
        int sampleSize = 11;

        assertThrows(NumberIsTooLargeException.class, () -> new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize));
    }
}