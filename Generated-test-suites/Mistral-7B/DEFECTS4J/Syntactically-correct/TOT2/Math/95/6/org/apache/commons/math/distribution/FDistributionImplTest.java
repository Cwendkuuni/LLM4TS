package org.apache.commons.math.distribution;

import org.junit.Assert;
import org.junit.Test;

public class FDistributionImplTest {

    @Test
    public void testFDistributionImplConstructor() {
        FDistributionImpl distribution = new FDistributionImpl(5.0, 10.0);
        assertNotNull(distribution);
    }

    // Test methods getNumeratorDegreesOfFreedom and getDenominatorDegreesOfFreedom
    // ... (Expert 1's test cases)

    // Test method cumulativeProbability
    // ... (Expert 1 and 2's test cases)

    // Test method inverseCumulativeProbability
    // ... (Expert 3's test cases)

    // Edge cases
    @Test(expected = IllegalArgumentException.class)
    public void testFDistributionImplNegativeDegreesOfFreedom() {
        new FDistributionImpl(-5.0, 10.0);
    }

    @Test(expected = MathException.class)
    public void testCumulativeProbabilityNegativeArgument() {
        FDistributionImpl distribution = new FDistributionImpl(5.0, 10.0);
        distribution.cumulativeProbability(-1.0);
    }

    @Test
    public void testCumulativeProbabilityInfiniteArgument() {
        FDistributionImpl distribution = new FDistributionImpl(5.0, 10.0);
        try {
            distribution.cumulativeProbability(Double.POSITIVE_INFINITY);
            fail();
        } catch (MathException e) {
            // expected
        }
    }

    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityNegativeP() {
        FDistributionImpl distribution = new FDistributionImpl(5.0, 10.0);
        distribution.inverseCumulativeProbability(-0.1);
    }

    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityZero() {
        FDistributionImpl distribution = new FDistributionImpl(5.0, 10.0);
        distribution.inverseCumulativeProbability(0.0);
    }

    @Test
    public void testInverseCumulativeProbabilityOne() {
        FDistributionImpl distribution = new FDistributionImpl(5.0, 10.0);
        double expected = Double.POSITIVE_INFINITY;
        assertEquals(expected, distribution.inverseCumulativeProbability(1.0), 0.0);
    }

}