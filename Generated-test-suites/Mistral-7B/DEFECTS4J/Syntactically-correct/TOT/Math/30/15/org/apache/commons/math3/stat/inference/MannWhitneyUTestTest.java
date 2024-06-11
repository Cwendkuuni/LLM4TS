package org.apache.commons.math3.stat.inference.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.stat.inference.MannWhitneyUTest;
import org.junit.Test;

/**
 * Test cases for {@link MannWhitneyUTest}.
 */
public class MannWhitneyUTestTest {

    /**
     * Test method for {@link MannWhitneyUTest#mannWhitneyU(double[], double[])}.
     */
    @Test
    public void testMannWhitneyU() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();

        // Test with two empty arrays
        double[] x = new double[0];
        double[] y = new double[0];
        assertEquals(0.0, mannWhitneyUTest.mannWhitneyU(x, y), 0.0);

        // Test with one element arrays
        double[] x1 = { 5.0 };
        double[] y1 = { 6.0 };
        assertEquals(0.0, mannWhitneyUTest.mannWhitneyU(x1, y1), 0.0);

        // Test with same elements arrays
        double[] x2 = { 1.0, 1.0 };
        double[] y2 = { 1.0, 1.0 };
        assertEquals(2.0, mannWhitneyUTest.mannWhitneyU(x2, y2), 0.0);

        // Test with different elements arrays
        double[] x3 = { 1.0, 2.0, 3.0 };
        double[] y3 = { 4.0, 5.0, 6.0 };
        assertEquals(12.0, mannWhitneyUTest.mannWhitneyU(x3, y3), 0.0);

        // Test with null arrays
        assertThrows(NullArgumentException.class, () -> {
            MannWhitneyUTest mannWhitneyUTestNull = new MannWhitneyUTest();
            mannWhitneyUTestNull.mannWhitneyU(null, new double[0]);
        });
        assertThrows(NullArgumentException.class, () -> {
            MannWhitneyUTest mannWhitneyUTestNull = new MannWhitneyUTest();
            mannWhitneyUTestNull.mannWhitneyU(new double[0], null);
        });

        // Test with zero-length arrays
        assertThrows(NoDataException.class, () -> {
            MannWhitneyUTest mannWhitneyUTestZero = new MannWhitneyUTest();
            mannWhitneyUTestZero.mannWhitneyU(new double[0], new double[0]);
        });
    }

    /**
     * Test method for {@link MannWhitneyUTest#mannWhitneyUTest(double[], double[])}.
     */
    @Test
    public void testMannWhitneyUTest() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();

        // Test with two empty arrays
        double[] x = new double[0];
        double[] y = new double[0];
        assertEquals(1.0, mannWhitneyUTest.mannWhitneyUTest(x, y), 0.0);

        // Test with one element arrays
        double[] x1 = { 5.0 };
        double[] y1 = { 6.0 };
        assertFalse(mannWhitneyUTest.mannWhitneyUTest(x1, y1) < 0.5);

        // Test with same elements arrays
        double[] x2 = { 1.0, 1.0 };
        double[] y2 = { 1.0, 1.0 };
        assertEquals(0.0, mannWhitneyUTest.mannWhitneyUTest(x2, y2), 0.0);

        // Test with different elements arrays
        double[] x3 = { 1.0, 2.0, 3.0 };
        double[] y3 = { 4.0, 5.0, 6.0 };
        assertTrue(mannWhitneyUTest.mannWhitneyUTest(x3, y3) > 0.5);

        // Test with null arrays
        assertThrows(NullArgumentException.class, () -> {
            mannWhitneyUTest.mannWhitneyUTest(null, new double[0]);
        });
        assertThrows(NullArgumentException.class, () -> {
            mannWhitneyUTest.mannWhitneyUTest(new double[0], null);
        });

        // Test with zero-length arrays
        assertThrows(NoDataException.class, () -> {
            mannWhitneyUTest.mannWhitneyUTest(new double[0], new double[0]);
        });

        // Test with equal means
        double[] x4 = { 1.5, 1.5, 1.5, 1.5, 1.5 };
        double[] y4 = { 1.5, 1.5, 1.5, 1.5, 1.5 };
        assertEquals(1.0, mannWhitneyUTest.mannWhitneyUTest(x4, y4), 0.0);

        // Test with different means
        double[] x5 = { 1.0, 2.0, 3.0, 4.0, 5.0 };
        double[] y5 = { 6.0, 7.0, 8.0, 9.0, 10.0 };
        assertTrue(mannWhitneyUTest.mannWhitneyUTest(x5, y5) > 0.5);
    }

    /**
     * Test method for edge cases.
     */
    @Test
    public void testEdgeCases() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();

        // Test with arrays with maximum size
        final int maxSize = 10000;
        double[] xMax = new double[maxSize];
        double[] yMax = new double[maxSize];
        for (int i = 0; i < maxSize; i++) {
            xMax[i] = (double) (Math.random() * 10);
            yMax[i] = (double) (Math.random() * 20);
        }
        MannWhitneyUTest mannWhitneyUTestEdge = new MannWhitneyUTest();
        assertEquals(mannWhitneyUTest.mannWhitneyU(xMax, yMax), mannWhitneyUTestEdge.mannWhitneyU(xMax, yMax), 0.0);
        assertEquals(mannWhitneyUTest.mannWhitneyUTest(xMax, yMax), mannWhitneyUTestEdge.mannWhitneyUTest(xMax, yMax), 0.0);

        // Test with arrays with minimum size
        double[] xMin = new double[0];
        double[] yMin = new double[0];
        MannWhitneyUTest mannWhitneyUTestMin = new MannWhitneyUTest();
        assertEquals(mannWhitneyUTestMin.mannWhitneyU(xMin, yMin), mannWhitneyUTestMin.mannWhitneyU(xMin, yMin), 0.0);
        assertEquals(mannWhitneyUTestMin.mannWhitneyUTest(xMin, yMin), mannWhitneyUTestMin.mannWhitneyUTest(xMin, yMin), 0.0);

        // Test with arrays with one element
        double[] xOne = { 1.0 };
        double[] yOne = { 1.0 };
        MannWhitneyUTest mannWhitneyUTestOne = new MannWhitneyUTest();
        assertEquals(mannWhitneyUTestOne.mannWhitneyU(xOne, yOne), mannWhitneyUTestOne.mannWhitneyU(xOne, yOne), 0.0);
        assertEquals(mannWhitneyUTestOne.mannWhitneyUTest(xOne, yOne), mannWhitneyUTestOne.mannWhitneyUTest(xOne, yOne), 0.0);

        // Test with arrays with same elements
        double[] xSame = { 1.0, 1.0 };
        double[] ySame = { 1.0, 1.0 };
        MannWhitneyUTest mannWhitneyUTestSame = new MannWhitneyUTest();
        assertEquals(mannWhitneyUTestSame.mannWhitneyU(xSame, ySame), mannWhitneyUTestSame.mannWhitneyU(xSame, ySame), 0.0);
        assertEquals(mannWhitneyUTestSame.mannWhitneyUTest(xSame, ySame), mannWhitneyUTestSame.mannWhitneyUTest(xSame, ySame), 0.0);
    }

    /**
     * Test method for error scenarios.
     */
    @Test
    public void testErrorScenarios() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();

        // Test with null arrays
        assertThrows(NullArgumentException.class, () -> {
            mannWhitneyUTest.mannWhitneyU(null, new double[0]);
        });
        assertThrows(NullArgumentException.class, () -> {
            mannWhitneyUTest.mannWhitneyU(new double[0], null);
        });

        // Test with zero-length arrays
        assertThrows(NoDataException.class, () -> {
            mannWhitneyUTest.mannWhitneyU(new double[0], new double[0]);
        });

        // Test with arrays with negative elements
        double[] xNeg = { -1.0 };
        double[] yNeg = { 1.0 };
        assertThrows(IllegalArgumentException.class, () -> {
            mannWhitneyUTest.mannWhitneyU(xNeg, yNeg);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            mannWhitneyUTest.mannWhitneyUTest(xNeg, yNeg);
        });

        // Test with arrays with NaN elements
        double[] xNaN = { Double.NaN };
        double[] yNaN = { 1.0 };
        assertThrows(IllegalArgumentException.class, () -> {
            mannWhitneyUTest.mannWhitneyU(xNaN, yNaN);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            mannWhitneyUTest.mannWhitneyUTest(xNaN, yNaN);
        });

        // Test with arrays with infinite elements
        double[] xInf = { Double.POSITIVE_INFINITY };
        double[] yInf = { 1.0 };
        assertThrows(IllegalArgumentException.class, () -> {
            mannWhitneyUTest.mannWhitneyU(xInf, yInf);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            mannWhitneyUTest.mannWhitneyUTest(xInf, yInf);
        });

        // Test with arrays with different data types
        double[] xDouble = { 1.0 };
        int[] yInt = { 1 };
        assertThrows(ClassCastException.class, () -> {
            mannWhitneyUTest.mannWhitneyU(xDouble, yInt);
        });
        assertThrows(ClassCastException.class, () -> {
            mannWhitneyUTest.mannWhitneyUTest(xDouble, yInt);
        });

        // Test with arrays with different lengths
        double[] xDifferentLength = { 1.0, 2.0 };
        double[] yDifferentLength = new double[3];
        assertThrows(IllegalArgumentException.class, () -> {
            mannWhitneyUTest.mannWhitneyU(xDifferentLength, yDifferentLength);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            mannWhitneyUTest.mannWhitneyUTest(xDifferentLength, yDifferentLength);
        });

        // Test with arrays with overlapping elements
        double[] xOverlap = { 1.0, 2.0, 2.0 };
        double[] yOverlap = { 2.0, 3.0, 2.0 };
        assertThrows(IllegalArgumentException.class, () -> {
            mannWhitneyUTest.mannWhitneyU(xOverlap, yOverlap);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            mannWhitneyUTest.mannWhitneyUTest(xOverlap, yOverlap);
        });
    }
}