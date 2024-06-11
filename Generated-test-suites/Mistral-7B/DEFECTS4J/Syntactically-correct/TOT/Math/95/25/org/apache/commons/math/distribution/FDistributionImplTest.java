package org.apache.commons.math.distribution;

import org.apache.commons.math3.exception.MathException;
import org.apache.commons.math3.special.Beta;
import org.junit.Test;
import static org.junit.Assert.*;

public class FDistributionImplTest {

    @Test
    public void testFDistributionImpl_ValidConstruction() throws MathException {
        FDistributionImpl fDist = new FDistributionImpl(2.0, 4.0);
        assertEquals(2.0, fDist.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(4.0, fDist.getDenominatorDegreesOfFreedom(), 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFDistributionImpl_InvalidConstruction_NegativeDOF() throws MathException {
        new FDistributionImpl(-2.0, 4.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFDistributionImpl_InvalidConstruction_ZeroDOF() throws MathException {
        new FDistributionImpl(0.0, 4.0);
    }

    @Test
    public void testCumulativeProbability_ValidInput() throws MathException {
        FDistributionImpl fDist = new FDistributionImpl(2.0, 4.0);
        double pVal = fDist.cumulativeProbability(1.5);
        assertTrue(pVal > 0.0 && pVal < 1.0);
    }

    @Test
    public void testCumulativeProbability_EdgeCases() throws MathException {
        FDistributionImpl fDist = new FDistributionImpl(2.0, 4.0);
        double pVal = fDist.cumulativeProbability(0.0);
        assertEquals(0.0, pVal, 0.0);

        pVal = fDist.cumulativeProbability(Double.POSITIVE_INFINITY);
        assertEquals(1.0, pVal, 0.0);
    }

    @Test(expected = MathException.class)
    public void testCumulativeProbability_InvalidInput() throws MathException {
        FDistributionImpl fDist = new FDistributionImpl(2.0, 4.0);
        fDist.cumulativeProbability(-1.0);
    }

    @Test
    public void testInverseCumulativeProbability_ValidInput() throws MathException {
        FDistributionImpl fDist = new FDistributionImpl(2.0, 4.0);
        double pVal = 0.95;
        double xVal = fDist.inverseCumulativeProbability(pVal);
        assertTrue(xVal > 0.0);
    }

    @Test(expected = MathException.class)
    public void testInverseCumulativeProbability_InvalidInput_Zero() throws MathException {
        FDistributionImpl fDist = new FDistributionImpl(2.0, 4.0);
        fDist.inverseCumulativeProbability(0.0);
    }

    @Test(expected = MathException.class)
    public void testInverseCumulativeProbability_InvalidInput_One() throws MathException {
        FDistributionImpl fDist = new FDistributionImpl(2.0, 4.0);
        fDist.inverseCumulativeProbability(1.0);
    }
}