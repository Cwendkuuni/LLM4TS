package org.apache.commons.math3.distribution;

import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.test.AbstractRealDistributionTest;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FDistributionTest extends AbstractRealDistributionTest {

    /**
     * Tests the constructor with valid degrees of freedom.
     *
     * @throws NotStrictlyPositiveException if the degrees of freedom are not positive.
     */
    @Test
    public void testConstructor() throws NotStrictlyPositiveException {
        final double numeratorDegreesOfFreedom = 2.5;
        final double denominatorDegreesOfFreedom = 4.0;
        FDistribution fDist = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        assertEquals("Incorrect numerator degrees of freedom.", numeratorDegreesOfFreedom, fDist.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals("Incorrect denominator degrees of freedom.", denominatorDegreesOfFreedom, fDist.getDenominatorDegreesOfFreedom(), 0.0);
    }

    /**
     * Tests the constructor with invalid degrees of freedom.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorInvalidDegreesOfFreedom() {
        new FDistribution(-1.0, 2.0);
    }

    /**
     * Tests the density method.
     */
    @Test
    public void testDensity() {
        final double numeratorDegreesOfFreedom = 2.0;
        final double denominatorDegreesOfFreedom = 4.0;
        final FDistribution fDist = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);

        // Test valid arguments
        double x = 0.5;
        double density = fDist.density(x);
        assertEquals("Incorrect density for x = 0.5.", 0.512773809524, density, 1.0e-8);

        x = 1.0;
        density = fDist.density(x);
        assertEquals("Incorrect density for x = 1.0.", 0.398942280401, density, 1.0e-8);

        x = 2.0;
        density = fDist.density(x);
        assertEquals("Incorrect density for x = 2.0.", 0.194225759723, density, 1.0e-8);

        // Test negative argument
        x = -0.5;
        density = fDist.density(x);
        assertEquals("Incorrect density for x = -0.5.", 0.0, density, 1.0e-8);

        // Test invalid argument
        x = Double.NaN;
        density = fDist.density(x);
        assertTrue("density should return 0 for NaN input.", Double.isNaN(density));
    }

    /**
     * Tests the cumulativeProbability method.
     */
    @Test
    public void testCumulativeProbability() {
        final double numeratorDegreesOfFreedom = 2.0;
        final double denominatorDegreesOfFreedom = 4.0;
        final FDistribution fDist = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);

        // Test valid arguments
        double x = 0.0;
        double cumulativeProbability = fDist.cumulativeProbability(x);
        assertEquals("Incorrect cumulative probability for x = 0.0.", 0.0, cumulativeProbability, 1.0e-8);

        x = 0.5;
        cumulativeProbability = fDist.cumulativeProbability(x);
        assertEquals("Incorrect cumulative probability for x = 0.5.", 0.306853658764, cumulativeProbability, 1.0e-8);

        x = 1.0;
        cumulativeProbability = fDist.cumulativeProbability(x);
        assertEquals("Incorrect cumulative probability for x = 1.0.", 0.688505606638, cumulativeProbability, 1.0e-8);

        x = 2.0;
        cumulativeProbability = fDist.cumulativeProbability(x);
        assertEquals("Incorrect cumulative probability for x = 2.0.", 1.0, cumulativeProbability, 1.0e-8);

        // Test negative argument
        x = -0.5;
        cumulativeProbability = fDist.cumulativeProbability(x);
        assertEquals("Incorrect cumulative probability for x = -0.5.", 0.0, cumulativeProbability, 1.0e-8);

        // Test invalid argument
        x = Double.NaN;
        cumulativeProbability = fDist.cumulativeProbability(x);
        assertTrue("cumulativeProbability should return 0 for NaN input.", Double.isNaN(cumulativeProbability));
    }

    /**
     * Tests the getNumericalMean method.
     */
    @Test
    public void testGetNumericalMean() {
        final double numeratorDegreesOfFreedom = 2.5;
        final double denominatorDegreesOfFreedom = 4.0;
        FDistribution fDist = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);

        // Test valid arguments
        double mean = fDist.getNumericalMean();
        assertEquals("Incorrect mean.", numeratorDegreesOfFreedom / (numeratorDegreesOfFreedom - 2), mean, 1.0e-8);

        // Test invalid arguments
        numeratorDegreesOfFreedom = -1.0;
        mean = fDist.getNumericalMean();
        assertEquals("Incorrect mean.", Double.NaN, mean, 0.0);

        denominatorDegreesOfFreedom = 1.0;
        mean = fDist.getNumericalMean();
        assertEquals("Incorrect mean.", Double.NaN, mean, 0.0);
    }

    /**
     * Tests the getNumericalVariance method.
     */
    @Test
    public void testGetNumericalVariance() {
        final double numeratorDegreesOfFreedom = 2.5;
        final double denominatorDegreesOfFreedom = 4.0;
        FDistribution fDist = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);

        // Test valid arguments
        double variance = fDist.getNumericalVariance();
        double expectedVariance = (2 * (denominatorDegreesOfFreedom * denominatorDegreesOfFreedom) * (numeratorDegreesOfFreedom + denominatorDegreesOfFreedom - 2)) /
                ((numeratorDegreesOfFreedom * (denominatorDegreesOfFreedom - 2) * (denominatorDegreesOfFreedom - 4)));
        assertEquals("Incorrect variance.", expectedVariance, variance, 1.0e-8);

        // Test invalid arguments
        numeratorDegreesOfFreedom = -1.0;
        variance = fDist.getNumericalVariance();
        assertEquals("Incorrect variance.", Double.NaN, variance, 0.0);

        denominatorDegreesOfFreedom = 1.0;
        variance = fDist.getNumericalVariance();
        assertEquals("Incorrect variance.", Double.NaN, variance, 0.0);
    }

    /**
     * Tests the getSupportLowerBound method.
     */
    @Test
    public void testGetSupportLowerBound() {
        final FDistribution fDist = new FDistribution(2.0, 4.0);
        assertEquals("Incorrect lower bound.", 0.0, fDist.getSupportLowerBound(), 1.0e-8);
    }

    /**
     * Tests the getSupportUpperBound method.
     */
    @Test
    public void testGetSupportUpperBound() {
        final FDistribution fDist = new FDistribution(2.0, 4.0);
        assertEquals("Incorrect upper bound.", Double.POSITIVE_INFINITY, fDist.getSupportUpperBound(), 1.0e-8);
    }

    /**
     * Tests the isSupportLowerBoundInclusive method.
     */
    @Test
    public void testIsSupportLowerBoundInclusive() {
        final FDistribution fDist = new FDistribution(2.0, 4.0);
        assertTrue("Incorrect lower bound inclusiveness.", fDist.isSupportLowerBoundInclusive());
    }

    /**
     * Tests the isSupportUpperBoundInclusive method.
     */
    @Test
    public void testIsSupportUpperBoundInclusive() {
        final FDistribution fDist = new FDistribution(2.0, 4.0);
        assertFalse("Incorrect upper bound inclusiveness.", fDist.isSupportUpperBoundInclusive());
    }

    /**
     * Tests the isSupportConnected method.
     */
    @Test
    public void testIsSupportConnected() {
        final FDistribution fDist = new FDistribution(2.0, 4.0);
        assertTrue("Incorrect support connectivity.", fDist.isSupportConnected());
    }

    /**
     * Tests the generateRandomNumbersInOpenInterval method.
     */
    @Test
    public void testGenerateRandomNumbersInOpenInterval() {
        final double numeratorDegreesOfFreedom = 2.0;
        final double denominatorDegreesOfFreedom = 4.0;
        FDistribution fDist = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        Well19937c rng = fDist.getRandomGenerator();

        // Test valid arguments
        int numberOfSamples = 10;
        double[] samples = new double[numberOfSamples];
        for (int i = 0; i < numberOfSamples; i++) {
            samples[i] = fDist.inverseCumulativeProbability(rng.nextDouble());
        }
        for (int i = 0; i < numberOfSamples; i++) {
            double expectedSample = Beta.regularizedBeta((numeratorDegreesOfFreedom * samples[i]) / (denominatorDegreesOfFreedom + numeratorDegreesOfFreedom * samples[i]),
                    0.5 * numeratorDegreesOfFreedom,
                    0.5 * denominatorDegreesOfFreedom);
            assertEquals("Incorrect sample.", expectedSample, samples[i], 1.0e-8);
        }

        // Test negative number of samples
        numberOfSamples = -5;
        try {
            fDist.generateRandomNumbersInOpenInterval(numberOfSamples, rng);
            fail("Expected an exception for negative number of samples.");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test null random number generator
        rng = null;
        try {
            fDist.generateRandomNumbersInOpenInterval(10, rng);
            fail("Expected an exception for null random number generator.");
        } catch (NullPointerException e) {
            // Expected
        }
    }
}