package org.apache.commons.math.distribution;

import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.FServiceRegistry;
import org.apache.commons.math3.special.Beta;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link FDistributionImpl}.
 */
public class FDistributionImplTest {

    /**
     * Test {@code FDistributionImpl} constructor.
     */
    @Test
    public void testConstructor() {
        FDistribution distribution = new FDistributionImpl(5.0, 7.0);
        assertEquals(5.0, distribution.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(7.0, distribution.getDenominatorDegreesOfFreedom(), 0.0);
    }

    /**
     * Test {@code cumulativeProbability} method with valid inputs.
     */
    @Test
    public void testCumulativeProbability() throws MathException {
        FDistribution distribution = new FDistributionImpl(5.0, 7.0);
        double x;
        double expectedProbability;
        for (x = 0.0; x <= 5.0; x += 1.0) {
            expectedProbability = Beta.regularizedBeta((5.0 * x) / (7.0 + 5.0 * x),
                    0.5 * 5.0,
                    0.5 * 7.0);
            assertEquals(expectedProbability, distribution.cumulativeProbability(x), 0.001);
        }
    }

    /**
     * Test {@code inverseCumulativeProbability} method with valid inputs.
     */
    @Test
    public void testInverseCumulativeProbability() throws MathException {
        FDistribution distribution = new FDistributionImpl(5.0, 7.0);
        for (double p = 0.01; p <= 0.99; p += 0.1) {
            double expectedValue = FServiceRegistry.getFDistribution(5.0, 7.0)
                    .inverseCumulativeProbability(p);
            assertEquals(expectedValue, distribution.inverseCumulativeProbability(p), 0.001);
        }
    }

    /**
     * Test {@code inverseCumulativeProbability} method with invalid input.
     */
    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityInvalidInput() throws MathException {
        FDistribution distribution = new FDistributionImpl(5.0, 7.0);
        distribution.inverseCumulativeProbability(-0.1);
    }

    /**
     * Test {@code inverseCumulativeProbability} method with invalid input (p > 1).
     */
    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityInvalidInputGreaterThanOne() throws MathException {
        FDistribution distribution = new FDistributionImpl(5.0, 7.0);
        distribution.inverseCumulativeProbability(1.1);
    }
}