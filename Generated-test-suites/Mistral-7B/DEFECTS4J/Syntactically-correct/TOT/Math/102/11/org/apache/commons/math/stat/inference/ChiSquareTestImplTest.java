package org.apache.commons.math.stat.inference;

import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.stat.inference.ChiSquareTestImpl;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for {@link ChiSquareTestImpl} class.
 */
public class ChiSquareTestImplTest {

    /**
     * Test chiSquare method with valid inputs.
     */
    @Test
    public void testChiSquareValid() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0};
        long[] observed = {2, 3};
        double result = test.chiSquare(expected, observed);
        double expectedResult = 2.9166666025323827;
        Assert.assertEquals(expectedResult, result, 1e-12);
    }

    /**
     * Test chiSquare method with invalid inputs - wrong array lengths.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testChiSquareInvalidLengths() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = {1.0};
        long[] observed = {2};
        test.chiSquare(expected, observed);
    }

    /**
     * Test chiSquare method with invalid inputs - negative expected frequencies.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testChiSquareNegativeExpected() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = {-1.0, 2.0};
        long[] observed = {2, 3};
        test.chiSquare(expected, observed);
    }

    /**
     * Test chiSquare method with invalid inputs - non-positive observed frequencies.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testChiSquareNegativeObserved() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0};
        long[] observed = {-2, 3};
        test.chiSquare(expected, observed);
    }

    /**
     * Test chiSquareTest method with valid inputs.
     */
    @Test
    public void testChiSquareTestValid() throws MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0};
        long[] observed = {2, 3};
        double pValue = test.chiSquareTest(expected, observed);
        double expectedPValue = 0.9973423452408421;
        Assert.assertEquals(expectedPValue, pValue, 1e-12);
    }

    /**
     * Test chiSquareTest method with invalid inputs - wrong array lengths.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testChiSquareTestInvalidLengths() throws MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = {1.0};
        long[] observed = {2};
        test.chiSquareTest(expected, observed);
    }

    /**
     * Test chiSquareTest method with invalid inputs - negative expected frequencies.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testChiSquareTestNegativeExpected() throws MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = {-1.0, 2.0};
        long[] observed = {2, 3};
        test.chiSquareTest(expected, observed);
    }

    /**
     * Test chiSquareTest method with invalid inputs - non-positive observed frequencies.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testChiSquareTestNegativeObserved() throws MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0};
        long[] observed = {-2, 3};
        test.chiSquareTest(expected, observed);
    }

    /**
     * Test chiSquareTest method with alpha = 1.
     */
    @Test
    public void testChiSquareTestAlphaOne() throws MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0};
        long[] observed = {2, 3};
        boolean result = test.chiSquareTest(expected, observed, 1.0);
        Assert.assertFalse(result);
    }

    /**
     * Test chiSquareTest method with alpha = 0.
     */
    @Test
    public void testChiSquareTestAlphaZero() throws MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0};
        long[] observed = {2, 3};
        boolean result = test.chiSquareTest(expected, observed, 0.0);
        Assert.assertTrue(result);
    }

    /**
     * Test chiSquareTest method with alpha > 0.5.
     */
    @Test
    public void testChiSquareTestAlphaGreaterThanHalf() throws MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0};
        long[] observed = {2, 3};
        boolean result = test.chiSquareTest(expected, observed, 0.6);
        Assert.assertTrue(result);
    }

    /**
     * Test chiSquare method with 2-way table.
     */
    @Test
    public void testChiSquareTable() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[][] counts = {{1, 2, 3}, {2, 3, 4}, {3, 4, 5}};
        double result = test.chiSquare(counts);
        double expectedResult = 7.819612643633231;
        Assert.assertEquals(expectedResult, result, 1e-12);
    }

    /**
     * Test chiSquareTest method with 2-way table and alpha = 0.05.
     */
    @Test
    public void testChiSquareTestTable() throws MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[][] counts = {{1, 2, 3}, {2, 3, 4}, {3, 4, 5}};
        double pValue = test.chiSquareTest(counts);
        double expectedPValue = 0.015398282526031885;
        Assert.assertEquals(expectedPValue, pValue, 1e-12);
    }

    /**
     * Test chiSquareTest method with 2-way table and alpha = 0.5.
     */
    @Test
    public void testChiSquareTestTableAlphaHalf() throws MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[][] counts = {{1, 2, 3}, {2, 3, 4}, {3, 4, 5}};
        boolean result = test.chiSquareTest(counts, 0.5);
        Assert.assertTrue(result);
    }

    /**
     * Test chiSquareTest method with 2-way table and alpha = 1.2.
     */
    @Test
    public void testChiSquareTestTableAlphaGreater() throws MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[][] counts = {{1, 2, 3}, {2, 3, 4}, {3, 4, 5}};
        boolean result = test.chiSquareTest(counts, 1.2);
        Assert.assertTrue(result);
    }

    /**
     * Test chiSquareDataSetsComparison method with valid inputs.
     */
    @Test
    public void testChiSquareDataSetsComparisonValid() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {2, 2, 3};
        double result = test.chiSquareDataSetsComparison(observed1, observed2);
        double expectedResult = 2.7066482540309286;
        Assert.assertEquals(expectedResult, result, 1e-12);
    }

    /**
     * Test chiSquareTestDataSetsComparison method with valid inputs.
     */
    @Test
    public void testChiSquareTestDataSetsComparisonValid() throws MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {2, 2, 3};
        double pValue = test.chiSquareTestDataSetsComparison(observed1, observed2);
        double expectedPValue = 0.07582691415438824;
        Assert.assertEquals(expectedPValue, pValue, 1e-12);
    }

    /**
     * Test chiSquareTestDataSetsComparison method with invalid inputs - different lengths.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testChiSquareTestDataSetsComparisonDifferentLengths() throws MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {1, 2};
        test.chiSquareTestDataSetsComparison(observed1, observed2);
    }

    /**
     * Test chiSquareTestDataSetsComparison method with alpha = 1.
     */
    @Test
    public void testChiSquareTestDataSetsComparisonAlphaOne() throws MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {2, 2, 3};
        boolean result = test.chiSquareTestDataSetsComparison(observed1, observed2, 1.0);
        Assert.assertFalse(result);
    }

    /**
     * Test chiSquareTestDataSetsComparison method with alpha = 0.
     */
    @Test
    public void testChiSquareTestDataSetsComparisonAlphaZero() throws MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {2, 2, 3};
        boolean result = test.chiSquareTestDataSetsComparison(observed1, observed2, 0.0);
        Assert.assertTrue(result);
    }

    /**
     * Test chiSquareTestDataSetsComparison method with alpha = 0.5.
     */
    @Test
    public void testChiSquareTestDataSetsComparisonAlphaHalf() throws MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {2, 2, 3};
        boolean result = test.chiSquareTestDataSetsComparison(observed1, observed2, 0.5);
        Assert.assertTrue(result);
    }

    /**
     * Test chiSquareTestDataSetsComparison method with alpha = 1.2.
     */
    @Test
    public void testChiSquareTestDataSetsComparisonAlphaGreater() throws MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {2, 2, 3};
        boolean result = test.chiSquareTestDataSetsComparison(observed1, observed2, 1.2);
        Assert.assertTrue(result);
    }
}