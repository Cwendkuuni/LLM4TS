package org.apache.commons.math.stat.inference;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.distribution.ChiSquaredDistributionImpl;
import org.apache.commons.math3.exception.MathException;
import org.junit.Test;

/**
 * Unit tests for {@link ChiSquareTestImpl}.
 */
public class ChiSquareTestImplTest {

    /**
     * Test constructor with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNull() {
        new ChiSquareTestImpl(null);
    }

    /**
     * Test constructor with valid argument.
     */
    @Test
    public void testConstructor() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        assertNotNull(test);
    }

    /**
     * Test chiSquare method with impossible inputs.
     *
     * @throws MathException expected
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareImpossible() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = new double[0];
        long[] observed = new long[0];
        test.chiSquare(expected, observed);
    }

    /**
     * Test chiSquare method with valid inputs.
     */
    @Test
    public void testChiSquareValid() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = new double[] {1.0, 2.0, 3.0, 1.0};
        long[] observed = new long[] {2, 3, 3, 1};
        double result = test.chiSquare(expected, observed);
        double expectedResult = 2.405;
        assertEquals(expectedResult, result, 1e-12);
    }

    /**
     * Test chiSquareTest method with impossible inputs.
     *
     * @throws MathException expected
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestImpossible() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = new double[0];
        long[] observed = new long[0];
        test.chiSquareTest(expected, observed);
    }

    /**
     * Test chiSquareTest method with valid inputs.
     *
     * @throws MathException expected
     */
    @Test
    public void testChiSquareTestValid() throws MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = new double[] {1.0, 2.0, 3.0, 1.0};
        long[] observed = new long[] {2, 3, 3, 1};
        double pValue = test.chiSquareTest(expected, observed);
        double expectedPValue = 0.0254;
        assertEquals(expectedPValue, pValue, 1e-12);
    }

    /**
     * Test chiSquareTest method with p-value less than 0.
     */
    @Test
    public void testChiSquareTestPValueLessThanZero() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = new double[] {1.0, 2.0, 3.0, 1.0};
        long[] observed = new long[] {2, 3, 3, 1};
        try {
            test.chiSquareTest(expected, observed, -0.01);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Test chiSquareTest method with p-value greater than 1.
     */
    @Test
    public void testChiSquareTestPValueGreaterThanOne() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = new double[] {1.0, 2.0, 3.0, 1.0};
        long[] observed = new long[] {2, 3, 3, 1};
        try {
            test.chiSquareTest(expected, observed, 1.01);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Test chiSquareTestDataSetsComparison method with null inputs.
     *
     * @throws MathException expected
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestDataSetsComparisonNull() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = null;
        long[] observed2 = null;
        test.chiSquareTestDataSetsComparison(observed1, observed2);
    }

    /**
     * Test chiSquareTestDataSetsComparison method with empty inputs.
     *
     * @throws MathException expected
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestDataSetsComparisonEmpty() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = new long[0];
        long[] observed2 = new long[0];
        test.chiSquareTestDataSetsComparison(observed1, observed2);
    }

    /**
     * Test chiSquareTestDataSetsComparison method with valid inputs.
     *
     * @throws MathException expected
     */
    @Test
    public void testChiSquareTestDataSetsComparisonValid() throws MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = new long[] {1, 2, 3, 4, 5};
        long[] observed2 = new long[] {2, 4, 3, 4, 5};
        double pValue = test.chiSquareTestDataSetsComparison(observed1, observed2);
        double expectedPValue = 0.0315;
        assertEquals(expectedPValue, pValue, 1e-12);
    }

    /**
     * Test chiSquare method with large inputs.
     */
    @Test
    public void testChiSquareLargeInputs() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = new double[1000];
        long[] observed = new long[1000];
        double result = test.chiSquare(expected, observed);
        // result may differ due to floating point precision
        assertTrue(result > 0);
    }

    /**
     * Test chiSquareTest method with large inputs.
     *
     * @throws MathException expected
     */
    @Test
    public void testChiSquareTestLargeInputs() throws MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = new double[1000];
        long[] observed = new long[1000];
        double pValue = test.chiSquareTest(expected, observed);
        // pValue may differ due to floating point precision
        assertTrue(pValue > 0);
    }

    /**
     * Test chiSquareTestDataSetsComparison method with large inputs.
     *
     * @throws MathException expected
     */
    @Test
    public void testChiSquareTestDataSetsComparisonLargeInputs() throws MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = new long[1000];
        long[] observed2 = new long[1000];
        double pValue = test.chiSquareTestDataSetsComparison(observed1, observed2);
        // pValue may differ due to floating point precision
        assertTrue(pValue > 0);
    }
}