package org.apache.commons.math3.distribution;

import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class FDistributionTest {

    @Test
    public void testFDistributionCreation() {
        try {
            FDistribution fd1 = new FDistribution(2.0, 3.0);
            assertNotNull(fd1);
            FDistribution fd2 = new FDistribution(3.0, 2.0);
            assertNotNull(fd2);
            FDistribution fd3 = new FDistribution(new Well19937c(), 2.0, 3.0);
            assertNotNull(fd3);
            FDistribution fd4 = new FDistribution(new Well19937c(), 3.0, 2.0);
            assertNotNull(fd4);
        } catch (NotStrictlyPositiveException e) {
            // expected
        }
    }

    @Test
    public void testGetters() {
        FDistribution fd = new FDistribution(2.0, 3.0);
        assertEquals(2.0, fd.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(3.0, fd.getDenominatorDegreesOfFreedom(), 0.0);
        assertFalse(Double.isNaN(fd.getNumericalMean()));
        assertFalse(Double.isNaN(fd.getNumericalVariance()));
        assertEquals(0.0, fd.getSupportLowerBound(), 0.0);
        assertEquals(Double.POSITIVE_INFINITY, fd.getSupportUpperBound(), 0.0);
        assertTrue(fd.isSupportLowerBoundInclusive());
        assertFalse(fd.isSupportUpperBoundInclusive());
        assertTrue(fd.isSupportConnected());
    }

    @Test
    public void testDensity() {
        FDistribution fd = new FDistribution(2.0, 3.0);
        for (double x = 0.0; x <= 10.0; x += 0.1) {
            double density = fd.density(x);
            assertEquals(0.0, density, 1e-5);
        }
    }

    @Test
    public void testCumulativeProbability() {
        FDistribution fd = new FDistribution(2.0, 3.0);
        for (double x = 0.0; x <= 10.0; x += 0.1) {
            double cdf = fd.cumulativeProbability(x);
            assertEquals(0.0, cdf, 1e-5);
        }
        // check for some known values
        assertEquals(0.30800377791886, fd.cumulativeProbability(1.5), 1e-5);
        assertEquals(0.82102286143244, fd.cumulativeProbability(2.5), 1e-5);
        assertEquals(1.0, fd.cumulativeProbability(1000.0), 1e-5);
    }
}