package org.apache.commons.math.stat.inference;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.distribution.ChiSquaredDistributionImpl;
import org.apache.commons.math3.exception.MathException;
import org.apache.commons.math3.stat.inference.ChiSquareTestImpl;
import org.junit.Test;

/**
 * Test class for ChiSquareTestImpl.
 */
public class ChiSquareTestImplTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        assertNotNull(test.getDistribution());
    }

    /**
     * Test constructor with specified distribution.
     */
    @Test
    public void testConstructorWithDistribution() {
        ChiSquaredDistribution distribution = new ChiSquaredDistributionImpl(5);
        ChiSquareTestImpl test = new ChiSquareTestImpl(distribution);
        assertEquals(distribution, test.getDistribution());
    }

    /**
     * Test chiSquare method with single data set.
     */
    @Test
    public void testChiSquareSingleDataSet() throws MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed = {10, 15};
        long[] expected = {12, 13};
        double chiSquare = test.chiSquare(expected, observed);
        double pValue = test.chiSquareTest(expected, observed);
        assertEquals(18.071524745892418, chiSquare, 0.001);
        assertEquals(0.02454193208079421, pValue, 0.001);
    }

    /**
     * Test chiSquare method with two data sets.
     */
    @Test
    public void testChiSquareTwoDataSets() throws MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {10, 15};
        long[] observed2 = {8, 12};
        double chiSquare = test.chiSquareDataSetsComparison(observed1, observed2);
        double pValue = test.chiSquareTestDataSetsComparison(observed1, observed2);
        assertEquals(3.403128435582235, chiSquare, 0.001);
        assertEquals(0.06246054938202158, pValue, 0.001);
    }

    /**
     * Test chiSquare method with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareInvalidInput() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[][] counts = {{1, 2}, {3}};
        test.chiSquare(counts);
    }

    /**
     * Test getDistribution method.
     */
    @Test
    public void testGetDistribution() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        ChiSquaredDistribution distribution = test.getDistribution();
        assertEquals(ChiSquaredDistribution.class, distribution.getClass());
    }

    /**
     * Test setDistribution method.
     */
    @Test
    public void testSetDistribution() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        ChiSquaredDistribution distribution = new ChiSquaredDistributionImpl(5);
        test.setDistribution(distribution);
        assertEquals(distribution, test.getDistribution());
    }
}