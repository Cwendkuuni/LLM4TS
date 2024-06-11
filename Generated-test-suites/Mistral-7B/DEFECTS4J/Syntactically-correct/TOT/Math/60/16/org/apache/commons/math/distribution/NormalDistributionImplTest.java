package org.apache.commons.math.distribution.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.special.Erf;
import org.junit.jupiter.api.Test;

class NormalDistributionImplTest {

    @Test
    void testNormalDistributionImpl_mean0_sd1() {
        NormalDistribution distribution = new NormalDistributionImpl();
        assertEquals(0.0, distribution.getMean());
        assertEquals(1.0, distribution.getStandardDeviation());

        // Test density
        assertEquals(0.0175, distribution.density(0.0), 1e-5);
        assertEquals(0.3989, distribution.density(0.5), 1e-5);
        assertEquals(0.0068, distribution.density(1.0), 1e-5);

        // Test cumulativeProbability
        assertEquals(0.5, distribution.cumulativeProbability(0.0), 1e-5);
        assertEquals(0.8413, distribution.cumulativeProbability(0.5), 1e-5);
        assertEquals(1.0, distribution.cumulativeProbability(1.0), 1e-5);

        // Test inverseCumulativeProbability
        assertEquals(0.0, distribution.inverseCumulativeProbability(0.0), 1e-5);
        assertEquals(0.5, distribution.inverseCumulativeProbability(0.5), 1e-5);
        assertEquals(1.0, distribution.inverseCumulativeProbability(1.0), 1e-5);

        // Test sample
        double sample = distribution.sample();
        assertEquals(0.0, sample, 1e-5);

        // Test mean and standard deviation
        NormalDistribution distribution2 = new NormalDistributionImpl(1.0, 2.0);
        assertEquals(1.0, distribution2.getMean());
        assertEquals(2.0, distribution2.getStandardDeviation());

        // Test exception
        assertThrows(MathIllegalArgumentException.class, () -> new NormalDistributionImpl(-1.0, 2.0));
    }

    @Test
    void testNormalDistributionImpl_mean_sd() {
        // Test density
        NormalDistribution distribution = new NormalDistributionImpl(2.5, 1.5);
        assertEquals(0.0412, distribution.density(1.5), 1e-5);

        // Test cumulativeProbability
        assertEquals(0.8813, distribution.cumulativeProbability(1.5), 1e-5);

        // Test inverseCumulativeProbability
        assertEquals(1.5, distribution.inverseCumulativeProbability(0.975), 1e-5);

        // Test sample
        double sample = distribution.sample();
        assertEquals(2.5, sample, 1e-5);
    }

    @Test
    void testNormalDistributionImpl_mean_sd_negativeSD() {
        // Test exception
        assertThrows(MathIllegalArgumentException.class, () -> new NormalDistributionImpl(2.5, -1.5));
    }

    @Test
    void testNormalDistributionImpl_mean_sd_zeroSD() {
        // Test exception
        assertThrows(MathIllegalArgumentException.class, () -> new NormalDistributionImpl(2.5, 0.0));
    }

    @Test
    void testNormalDistributionImpl_mean_sd_inverseCumulativeProbabilityOutOfRange() {
        NormalDistribution distribution = new NormalDistributionImpl(0.5, 1.0);
        // Test exception
        assertThrows(IllegalArgumentException.class, () -> distribution.inverseCumulativeProbability(-1.0));
        // Test exception
        assertThrows(IllegalArgumentException.class, () -> distribution.inverseCumulativeProbability(2.0));
    }

    @Test
    void testNormalDistributionImpl_mean_sd_cumulativeProbabilityOutOfRange() {
        NormalDistribution distribution = new NormalDistributionImpl(0.5, 1.0);
        // Test exception
        assertThrows(IllegalArgumentException.class, () -> distribution.cumulativeProbability(-1.0));
        // Test exception
        assertThrows(IllegalArgumentException.class, () -> distribution.cumulativeProbability(2.0));
    }
}