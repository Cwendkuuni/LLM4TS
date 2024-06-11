package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.junit.Test;

/**
 * Test class for {@link PoissonDistributionImpl}.
 */
public class PoissonDistributionImplTest {

    /**
     * Test the default constructor.
     */
    @Test
    public void testDefaultConstructor() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl();
        assertEquals(Double.POSITIVE_INFINITY, distribution.getMean(), 0.0);
    }

    /**
     * Test constructor with valid parameters.
     */
    @Test
    public void testConstructWithValidParams() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
        assertEquals(2.5, distribution.getMean(), 0.0);
    }

    /**
     * Test constructor with invalid parameter.
     */
    @Test
    public void testConstructWithInvalidParams() {
        assertThrows(MathIllegalArgumentException.class, () -> {
            new PoissonDistributionImpl(-1.5);
        });
    }

    /**
     * Test the probability method with a valid argument.
     */
    @Test
    public void testProbabilityWithValidArgument() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
        assertEquals(0.110223621813171, distribution.probability(1), 0.000001);
    }

    /**
     * Test the probability method with an invalid argument.
     */
    @Test
    public void testProbabilityWithInvalidArgument() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
        assertEquals(0.0, distribution.probability(-1), 0.0);
    }

    /**
     * Test the cumulativeProbability method with a valid argument.
     */
    @Test
    public void testCumulativeProbabilityWithValidArgument() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
        assertEquals(0.126926932876326, distribution.cumulativeProbability(1), 0.000001);
    }

    /**
     * Test the cumulativeProbability method with an invalid argument.
     */
    @Test
    public void testCumulativeProbabilityWithInvalidArgument() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
        assertThrows(MathException.class, () -> {
            distribution.cumulativeProbability(-1);
        });
    }

    /**
     * Test the normalApproximateProbability method with a valid argument.
     */
    @Test
    public void testNormalApproximateProbabilityWithValidArgument() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
        assertEquals(0.110223621813171, distribution.normalApproximateProbability(1), 0.000001);
    }

    /**
     * Test the sample method with a valid mean.
     */
    @Test
    public void testSampleWithValidMean() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
        int sample = distribution.sample();
        assertEquals(1, sample);
    }

    /**
     * Test the sample method with a large mean.
     */
    @Test
    public void testSampleWithLargeMean() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(250.0);
        int sample = distribution.sample();
        assertEquals(100, sample);
    }
}