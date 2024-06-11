package org.apache.commons.math3.distribution;

import org.apache.commons.math3.distribution.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.exception.util.*;
import org.apache.commons.math3.util.*;
import org.junit.*;
import static org.junit.Assert.*;;

public class HypergeometricDistributionTest {

    @Test(expected = NotStrictlyPositiveException.class)
    public void testHypergeometricDistributionConstructorWithNegativePopulationSize() {
        try {
            new HypergeometricDistribution(-1, 3, 5);
            Assert.fail();
        } catch (NotStrictlyPositiveException e) {
            // Expected exception
        }
    }

    @Test(expected = NotPositiveException.class)
    public void testHypergeometricDistributionConstructorWithNegativeNumberOfSuccesses() {
        try {
            new HypergeometricDistribution(5, -1, 5);
            Assert.fail();
        } catch (NotPositiveException e) {
            // Expected exception
        }
    }

    @Test(expected = NotPositiveException.class)
    public void testHypergeometricDistributionConstructorWithNegativeSampleSize() {
        try {
            new HypergeometricDistribution(5, 3, -1);
            Assert.fail();
        } catch (NotPositiveException e) {
            // Expected exception
        }
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testHypergeometricDistributionConstructorWithNumberOfSuccessesLargerThanPopulationSize() {
        try {
            new HypergeometricDistribution(5, 6, 5);
            Assert.fail();
        } catch (NumberIsTooLargeException e) {
            // Expected exception
        }
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testHypergeometricDistributionConstructorWithSampleSizeLargerThanPopulationSize() {
        try {
            new HypergeometricDistribution(5, 5, 6);
            Assert.fail();
        } catch (NumberIsTooLargeException e) {
            // Expected exception
        }
    }

    @Test
    public void testHypergeometricDistributionConstructorWithValidArguments() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
        Assert.assertEquals(10, dist.getPopulationSize());
        Assert.assertEquals(5, dist.getNumberOfSuccesses());
        Assert.assertEquals(5, dist.getSampleSize());
    }

    @Test
    public void testCumulativeProbabilityWithValidArgument() {
        HypergeometricDistribution dist = new HypergeometricDistribution(5, 3, 5);
        double expected = dist.innerCumulativeProbability(0, 2, 1); // Approximate values due to floating-point errors
        double actual = dist.cumulativeProbability(2);
        Assert.assertEquals(expected, actual, 0.001);
    }

    @Test
    public void testCumulativeProbabilityWithLowerDomain() {
        HypergeometricDistribution dist = new HypergeometricDistribution(5, 3, 5);
        double expected = 0.0;
        double actual = dist.cumulativeProbability(0);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testCumulativeProbabilityWithUpperDomain() {
        HypergeometricDistribution dist = new HypergeometricDistribution(5, 3, 5);
        double expected = 1.0;
        double actual = dist.cumulativeProbability(5);
        Assert.assertEquals(expected, actual);
    }
}