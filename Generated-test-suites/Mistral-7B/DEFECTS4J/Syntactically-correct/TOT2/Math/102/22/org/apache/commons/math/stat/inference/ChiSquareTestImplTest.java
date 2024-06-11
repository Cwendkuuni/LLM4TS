package org.apache.commons.math.stat.inference;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.distribution.ChiSquaredDistributionImpl;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

/**
 * Tests for the ChiSquareTestImpl class.
 */
public class ChiSquareTestImplTest {

    /**
     * Tests the constructor with no arguments.
     */
    @Test
    public void testEmptyConstructor() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        ChiSquaredDistribution distribution = chiSquareTest.getDistribution();
        assertEquals("Degrees of freedom should be 1 for empty constructor.", 1.0, distribution.getDegreesOfFreedom(), 0.0);
    }

    /**
     * Tests the constructor with a ChiSquaredDistribution argument.
     */
    @Test
    public void testWithChiSquaredDistribution() {
        double df = 5.0;
        ChiSquaredDistributionImpl chiSquareDistribution = new ChiSquaredDistributionImpl(df);
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl(chiSquareDistribution);
        ChiSquaredDistribution distribution = chiSquareTest.getDistribution();
        assertEquals("Degrees of freedom should be the same as the one passed in the constructor.", df, distribution.getDegreesOfFreedom(), 0.0);
    }

    /**
     * Tests the chiSquare method with expected and observed arrays.
     */
    @Test
    public void testChiSquare() {
        long[] expected = {1, 2, 3, 4, 5};
        long[] observed = {2, 3, 2, 4, 6};
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        double result = chiSquareTest.chiSquare(expected, observed);
        double expectedResult = 7.81125692947442;
        assertEquals("chiSquare method should return the expected result.", expectedResult, result, 0.0001);
    }

    /**
     * Tests the chiSquareTest method with expected and observed arrays.
     */
    @Test
    public void testChiSquareTest() throws IllegalArgumentException, MathException {
        long[] expected = {1, 2, 3, 4, 5};
        long[] observed = {2, 3, 2, 4, 6};
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        double pValue = chiSquareTest.chiSquareTest(expected, observed);
        double expectedPValue = 0.0511287212056356;
        assertEquals("chiSquareTest method should return the expected p-value.", expectedPValue, pValue, 0.0001);
    }

    /**
     * Tests the chiSquareTest method with expected and observed arrays and alpha.
     */
    @Test
    public void testChiSquareTestWithAlpha() throws IllegalArgumentException, MathException {
        long[] expected = {1, 2, 3, 4, 5};
        long[] observed = {2, 3, 2, 4, 6};
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        boolean result = chiSquareTest.chiSquareTest(expected, observed, 0.05);
        boolean expectedResult = false;
        assertEquals("chiSquareTest method should return false with alpha = 0.05.", expectedResult, result);
    }

    /**
     * Tests the chiSquare method with a 2-way table.
     */
    @Test
    public void testChiSquareWith2WayTable() {
        long[][] counts = {
            {1, 2},
            {2, 3}
        };
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        double result = chiSquareTest.chiSquare(counts);
        double expectedResult = 2.0;
        assertEquals("chiSquare method should return the expected result.", expectedResult, result, 0.0001);
    }

    /**
     * Tests the chiSquareTest method with a 2-way table.
     */
    @Test
    public void testChiSquareTestWith2WayTable() throws IllegalArgumentException, MathException {
        long[][] counts = {
            {1, 2},
            {2, 3}
        };
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        double pValue = chiSquareTest.chiSquareTest(counts);
        double expectedPValue = 0.117833858057853;
        assertEquals("chiSquareTest method should return the expected p-value.", expectedPValue, pValue, 0.0001);
    }

    /**
     * Tests the chiSquareTestDataSetsComparison method
     * with two arrays of observed frequency counts.
     */
    @Test
    public void testChiSquareTestDataSetsComparison() throws IllegalArgumentException, MathException {
        long[] observed1 = {1, 2, 3, 4, 5};
        long[] observed2 = {2, 3, 2, 4, 6};
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        double pValue = chiSquareTest.chiSquareTestDataSetsComparison(observed1, observed2);
        double expectedPValue = 0.0511287212056356;
        assertEquals("chiSquareTestDataSetsComparison method should return the expected p-value.", expectedPValue, pValue, 0.0001);
    }

    /**
     * Tests the chiSquareTestDataSetsComparison method
     * with two arrays of observed frequency counts and alpha.
     */
    @Test
    public void testChiSquareTestDataSetsComparisonWithAlpha() throws IllegalArgumentException, MathException {
        long[] observed1 = {1, 2, 3, 4, 5};
        long[] observed2 = {2, 3, 2, 4, 6};
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        boolean result = chiSquareTest.chiSquareTestDataSetsComparison(observed1, observed2, 0.05);
        boolean expectedResult = false;
        assertEquals("chiSquareTestDataSetsComparison method should return false with alpha = 0.05.", expectedResult, result);
    }

    /**
     * Tests the checkArray method.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCheckArrayWithInvalidArray() {
        long[][] in = new long[2][];
        in[0] = new long[]{1};
        in[1] = new long[1];
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        chiSquareTest.checkArray(in);
    }

    /**
     * Tests the checkArray method.
     */
    @Test
    public void testCheckArrayWithValidArray() {
        long[][] in = new long[2][2];
        in[0] = new long[]{1, 2};
        in[1] = new long[]{2, 3};
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        chiSquareTest.checkArray(in);
    }
}