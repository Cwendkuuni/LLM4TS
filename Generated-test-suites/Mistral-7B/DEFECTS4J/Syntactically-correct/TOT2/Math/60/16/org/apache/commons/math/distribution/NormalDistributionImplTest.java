package org.apache.commons.math.distribution;

import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.special.Erf;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class NormalDistributionImplTest {

    @Test
    public void testNormalDistributionImpl_MeanZeroStandardDeviationOne() {
        NormalDistributionImpl dist = new NormalDistributionImpl();

        // Check mean
        double mean = dist.getMean();
        assertEquals("Mean is not zero", 0.0, mean, 1e-9);

        // Check standard deviation
        double standardDeviation = dist.getStandardDeviation();
        assertEquals("Standard deviation is not one", 1.0, standardDeviation, 1e-9);

        // Check density
        double x = 0.5;
        double density = dist.density(x);
        assertEquals("Density is not correct", 0.3989422804010993, density, 1e-6);

        // Check cumulative probability
        double p = 0.5;
        double cumulativeProbability = dist.cumulativeProbability(x);
        assertEquals("Cumulative probability is not correct", 0.5, cumulativeProbability, 1e-6);

        // Check inverse cumulative probability
        double inverseCumulativeProbability = dist.inverseCumulativeProbability(p);
        assertEquals("Inverse cumulative probability is not correct", x, inverseCumulativeProbability, 1e-6);

        // Check sample
        double sample = dist.sample();
        assertTrue("Sample is not within expected range", sample > -3 && sample < 3);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNormalDistributionImpl_NegativeStandardDeviation() {
        NormalDistributionImpl dist = new NormalDistributionImpl(0, -1);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testNormalDistributionImpl_MaxIterationsExceededException_CumulativeProbability() {
        NormalDistributionImpl dist = new NormalDistributionImpl(0, 1);
        double x = Double.POSITIVE_INFINITY;
        dist.cumulativeProbability(x);
    }

    @Test(expected = org.apache.commons.math3.exception.OutOfRangeException.class)
    public void testNormalDistributionImpl_OutOfRangeException_InverseCumulativeProbability() {
        NormalDistributionImpl dist = new NormalDistributionImpl(0, 1);
        double p = 1.1;
        dist.inverseCumulativeProbability(p);
    }
}