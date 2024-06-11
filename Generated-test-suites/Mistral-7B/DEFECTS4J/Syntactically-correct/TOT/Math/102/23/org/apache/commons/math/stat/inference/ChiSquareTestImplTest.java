package org.apache.commons.math.stat.inference;

import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for {@link ChiSquareTestImpl} class.
 */
public class ChiSquareTestImplTest {

    /**
     * Test for chiSquare method with given expected and observed counts.
     */
    @Test
    public void testChiSquare() {
        ChiSquareTestImpl test = new ChiSquareTestImpl(new ChiSquaredDistributionImpl(1.0));

        long[] observed = {10, 20, 30, 40};
        double[] expected = {15.0, 25.0, 35.0, 25.0};

        double result = test.chiSquare(expected, observed);
        double expectedResult = 3.84155596923826; // degrees of freedom = 3
        assertEquals("chiSquare: wrong result for given expected and observed counts",
                Precision.round(expectedResult, 6), test.chiSquare(expected, observed), 1e-6);
    }

    /**
     * Test for chiSquareTest method with given expected and observed counts.
     */
    @Test
    public void testChiSquareTest() {
        ChiSquareTestImpl test = new ChiSquareTestImpl(new ChiSquaredDistributionImpl(1.0));

        long[] observed = {10, 20, 30, 40};
        double[] expected = {15.0, 25.0, 35.0, 25.0};

        double pValue = test.chiSquareTest(expected, observed);
        double expectedPValue = 0.0431382581423444; // for 3 degrees of freedom and p-value = 0.05
        assertEquals("chiSquareTest: wrong pValue for given expected and observed counts",
                Precision.round(expectedPValue, 6), pValue, 1e-6);
    }

    /**
     * Test for chiSquareTest(double[], double[], double) method with given expected and observed counts and alpha value.
     */
    @Test
    public void testChiSquareTestWithAlpha() {
        ChiSquareTestImpl test = new ChiSquareTestImpl(new ChiSquaredDistributionImpl(1.0));

        long[] observed = {10, 20, 30, 40};
        double[] expected = {15.0, 25.0, 35.0, 25.0};

        boolean result = test.chiSquareTest(expected, observed, 0.05);
        boolean expectedResult = true;
        assertEquals("chiSquareTest: wrong result for given expected and observed counts and significance level",
                expectedResult, result);
    }

    /**
     * Test for chiSquare method with given 2-way table.
     */
    @Test
    public void testChiSquareWithTwoWayTable() {
        ChiSquareTestImpl test = new ChiSquareTestImpl(new ChiSquaredDistributionImpl(1.0));

        long[][] counts = {{10, 5}, {15, 10}, {25, 20}, {30, 35}};

        double result = test.chiSquare(counts);
        double expectedResult = 16.2623467545747; // degrees of freedom = (2-1)*(2-1) = 3
        assertEquals("chiSquare: wrong result for given 2-way table",
                Precision.round(expectedResult, 6), test.chiSquare(counts), 1e-6);
    }

    /**
     * Test for chiSquareTest method with given 2-way table.
     */
    @Test
    public void testChiSquareTestWithTwoWayTable() {
        ChiSquareTestImpl test = new ChiSquareTestImpl(new ChiSquaredDistributionImpl(1.0));

        long[][] counts = {{10, 5}, {15, 10}, {25, 20}, {30, 35}};

        double pValue = test.chiSquareTest(counts);
        double expectedPValue = 0.00220836873698322; // for 3 degrees of freedom and p-value = 0.05
        assertEquals("chiSquareTest: wrong pValue for given 2-way table",
                Precision.round(expectedPValue, 6), pValue, 1e-6);
    }

    /**
     * Test for chiSquareTest(long[][], double) method with given 2-way table and alpha value.
     */
    @Test
    public void testChiSquareTestWithTwoWayTableAndAlpha() {
        ChiSquareTestImpl test = new ChiSquareTestImpl(new ChiSquaredDistributionImpl(1.0));

        long[][] counts = {{10, 5}, {15, 10}, {25, 20}, {30, 35}};

        boolean result = test.chiSquareTest(counts, 0.05);
        boolean expectedResult = true;
        assertEquals("chiSquareTest: wrong result for given 2-way table and significance level",
                expectedResult, result);
    }

    /**
     * Test for chiSquareDataSetsComparison method with given observed arrays.
     */
    @Test
    public void testChiSquareDataSetsComparison() {
        ChiSquareTestImpl test = new ChiSquareTestImpl(new ChiSquaredDistributionImpl(1.0));

        long[] observed1 = {10, 20, 30, 40};
        long[] observed2 = {15, 15, 30, 45};

        double result = test.chiSquareDataSetsComparison(observed1, observed2);
        double expectedResult = 6.08251845686139; // degrees of freedom = (2-1)*(2-1) = 3
        assertEquals("chiSquareDataSetsComparison: wrong result for given observed arrays",
                Precision.round(expectedResult, 6), test.chiSquareDataSetsComparison(observed1, observed2), 1e-6);
    }

    /**
     * Test for chiSquareTestDataSetsComparison method with given observed arrays.
     */
    @Test
    public void testChiSquareTestDataSetsComparison() {
        ChiSquareTestImpl test = new ChiSquareTestImpl(new ChiSquaredDistributionImpl(1.0));

        long[] observed1 = {10, 20, 30, 40};
        long[] observed2 = {15, 15, 30, 45};

        double pValue = test.chiSquareTestDataSetsComparison(observed1, observed2);
        double expectedPValue = 0.0131830544792111; // for 3 degrees of freedom and p-value = 0.05
        assertEquals("chiSquareTestDataSetsComparison: wrong pValue for given observed arrays",
                Precision.round(expectedPValue, 6), pValue, 1e-6);
    }

    /**
     * Test for chiSquareTestDataSetsComparison method with given observed arrays and alpha value.
     */
    @Test
    public void testChiSquareTestDataSetsComparisonWithAlpha() {
        ChiSquareTestImpl test = new ChiSquareTestImpl(new ChiSquaredDistributionImpl(1.0));

        long[] observed1 = {10, 20, 30, 40};
        long[] observed2 = {15, 15, 30, 45};

        boolean result = test.chiSquareTestDataSetsComparison(observed1, observed2, 0.05);
        boolean expectedResult = true;
        assertEquals("chiSquareTestDataSetsComparison: wrong result for given observed arrays and significance level",
                expectedResult, result);
    }
}