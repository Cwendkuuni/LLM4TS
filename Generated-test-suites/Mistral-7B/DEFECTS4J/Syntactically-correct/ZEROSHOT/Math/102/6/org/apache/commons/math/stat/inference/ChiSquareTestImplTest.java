package org.apache.commons.math.stat.inference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.distribution.ChiSquaredDistributionImpl;
import org.apache.commons.math3.stat.inference.UnknownDistributionChiSquareTest;
import org.junit.Test;

/**
 * Tests for the {@link ChiSquareTestImpl} class.
 */
public class ChiSquareTestImplTest {

    /** The constant EPSILON. */
    private static final double EPSILON = 1e-12;

    /** The constant ALPHA. */
    private static final double ALPHA = 0.05;

    /** The constant SIGMA_SQUARED. */
    private static final double SIGMA_SQUARED = 2.0;

    /** The constant N. */
    private static final int N = 5;

    /** The constant NUM_ITERATIONS. */
    private static final int NUM_ITERATIONS = 1000;

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        assertNotNull(test.getDistribution());
    }

    /**
     * Test constructor with custom distribution.
     */
    @Test
    public void testConstructorWithCustomDistribution() {
        ChiSquaredDistributionImpl dist = new ChiSquaredDistributionImpl(N);
        ChiSquareTestImpl test = new ChiSquareTestImpl(dist);
        assertNotNull(test.getDistribution());
        assertEquals(dist, test.getDistribution());
    }

    /**
     * Test chiSquare method with single observation.
     */
    @Test
    public void testChiSquareSingleObservation() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] observed = {1};
        double[] expected = {1};
        try {
            double result = test.chiSquare(expected, observed);
            assertEquals(0.0, result, EPSILON);
        } catch (IllegalArgumentException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test chiSquare method with multiple observations.
     */
    @Test
    public void testChiSquareMultipleObservations() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] observed = {1, 2, 3, 4, 5};
        double[] expected = {3, 2, 1, 2, 1};
        try {
            double result = test.chiSquare(expected, observed);
            assertEquals(5.991464798855, result, EPSILON);
        } catch (IllegalArgumentException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test chiSquareTest method with single observation.
     */
    @Test
    public void testChiSquareTestSingleObservation() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] observed = {1};
        double[] expected = {1};
        try {
            double pValue = test.chiSquareTest(expected, observed);
            assertEquals(1.0, pValue, EPSILON);
        } catch (IllegalArgumentException | MathException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test chiSquareTest method with multiple observations.
     */
    @Test
    public void testChiSquareTestMultipleObservations() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] observed = {1, 2, 3, 4, 5};
        double[] expected = {3, 2, 1, 2, 1};
        try {
            double pValue = test.chiSquareTest(expected, observed);
            assertEquals(0.045195138553, pValue, EPSILON);
        } catch (IllegalArgumentException | MathException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test chiSquareTest method with large expected values.
     */
    @Test
    public void testChiSquareTestLargeExpectedValues() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed = {1000, 1000, 1000, 1000, 1000};
        long[] expected = {1100, 900, 950, 1050, 850};
        try {
            double pValue = test.chiSquareTest(expected, observed);
            assertTrue(pValue > ALPHA);
        } catch (IllegalArgumentException | MathException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test chiSquareTest method with small p-value.
     */
    @Test
    public void testChiSquareTestSmallPValue() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed = {1, 2, 3, 4, 5};
        long[] expected = {2, 3, 3, 3, 3};
        try {
            double pValue = test.chiSquareTest(expected, observed);
            assertTrue(pValue < ALPHA);
        } catch (IllegalArgumentException | MathException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test chiSquareTestDataSetsComparison method with single observation in each
     * dataset.
     */
    @Test
    public void testChiSquareTestDataSetsComparisonSingleObservation() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {1};
        long[] observed2 = {1};
        try {
            double pValue = test.chiSquareTestDataSetsComparison(observed1, observed2);
            assertEquals(1.0, pValue, EPSILON);
        } catch (IllegalArgumentException | MathException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test chiSquareTestDataSetsComparison method with multiple observations in
     * each dataset.
     */
    @Test
    public void testChiSquareTestDataSetsComparisonMultipleObservations() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3, 4, 5};
        long[] observed2 = {1, 2, 3, 4, 5};
        try {
            double pValue = test.chiSquareTestDataSetsComparison(observed1, observed2);
            assertEquals(1.0, pValue, EPSILON);
        } catch (IllegalArgumentException | MathException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test chiSquareTestDataSetsComparison method with different observations in
     * each dataset.
     */
    @Test
    public void testChiSquareTestDataSetsComparisonDifferentObservations() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3, 4, 5};
        long[] observed2 = {2, 1, 3, 4, 5};
        try {
            double pValue = test.chiSquareTestDataSetsComparison(observed1, observed2);
            assertTrue(pValue > ALPHA);
        } catch (IllegalArgumentException | MathException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test chiSquareDataSetsComparison method with small p-value.
     */
    @Test
    public void testChiSquareDataSetsComparisonSmallPValue() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3, 4, 5};
        long[] observed2 = {2, 1, 3, 4, 5};
        try {
            double pValue = test.chiSquareDataSetsComparison(observed1, observed2);
            assertTrue(pValue < ALPHA);
        } catch (IllegalArgumentException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test chiSquareDataSetsComparison method with equal observations in each
     * dataset.
     */
    @Test
    public void testChiSquareDataSetsComparisonEqualObservations() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3, 4, 5};
        long[] observed2 = {1, 2, 3, 4, 5};
        try {
            double pValue = test.chiSquareDataSetsComparison(observed1, observed2);
            assertEquals(1.0, pValue, EPSILON);
        } catch (IllegalArgumentException | MathException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test checkArray method.
     */
    @Test
    public void testCheckArray() {
        long[][] in = new long[1][1];
        try {
            new ChiSquareTestImpl().checkArray(in);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Input table must have at least two rows", e.getMessage());
        }

        in = new long[1][2];
        try {
            new ChiSquareTestImpl().checkArray(in);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Input table must have at least two columns", e.getMessage());
        }

        in = new long[2][];
        try {
            new ChiSquareTestImpl().checkArray(in);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Input table must be rectangular", e.getMessage());
        }

        in = new long[2][1];
        try {
            new ChiSquareTestImpl().checkArray(in);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Input table must have at least two columns", e.getMessage());
        }

        in = new long[2][2];
        try {
            new ChiSquareTestImpl().checkArray(in);
        } catch (IllegalArgumentException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        long[][] emptyIn = new long[2][2];
        try {
            new ChiSquareTestImpl().checkArray(emptyIn);
        } catch (IllegalArgumentException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        in[0][0] = -1;
        try {
            new ChiSquareTestImpl().checkArray(in);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("All entries in input 2-way table must be non-negative", e.getMessage());
        }

        in[0][1] = -1;
        try {
            new ChiSquareTestImpl().checkArray(in);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("All entries in input 2-way table must be non-negative", e.getMessage());
        }

        in[1][0] = -1;
        try {
            new ChiSquareTestImpl().checkArray(in);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("All entries in input 2-way table must be non-negative", e.getMessage());
        }

        in[1][1] = -1;
        try {
            new ChiSquareTestImpl().checkArray(in);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("All entries in input 2-way table must be non-negative", e.getMessage());
        }
    }
}