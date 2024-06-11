package org.apache.commons.math3.stat.inference.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.stat.inference.MannWhitneyUTest;
import org.apache.commons.math3.stat.ranking.NonNullStrategy;
import org.apache.commons.math3.stat.ranking.TiesStrategy;
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
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {2.0, 3.0, 4.0};
        assertEquals(6.0, test.mannWhitneyU(x, y), 0.001);

        // Test with empty arrays
        assertThrows(NoDataException.class, () -> test.mannWhitneyU(new double[0], new double[0]));
        assertThrows(NoDataException.class, () -> test.mannWhitneyU(new double[]{1.0}, new double[]{}));
        assertThrows(NoDataException.class, () -> test.mannWhitneyU(new double[]{}, new double[1.0]));

        // Test with null arrays
        assertThrows(NullArgumentException.class, () -> test.mannWhitneyU(null, y));
        assertThrows(NullArgumentException.class, () -> test.mannWhitneyU(x, null));

        // Test with arrays of different sizes
        double[] x1 = {1.0, 2.0, 3.0, 4.0};
        double[] y1 = {1.0, 2.0, 3.0};
        double[] x2 = {1.0, 2.0};
        double[] y2 = {1.0, 2.0, 3.0, 4.0, 5.0};
        assertEquals(3.5, test.mannWhitneyU(x1, y1), 0.001);
        assertEquals(7.0, test.mannWhitneyU(x2, y2), 0.001);

        // Test with NaNs
        MannWhitneyUTest testNaN = new MannWhitneyUTest(NonNullStrategy.REPLACE, TiesStrategy.MIN);
        double[] xNaN = {1.0, 2.0, Double.NaN};
        double[] yNaN = {2.0, Double.NaN, 3.0};
        assertEquals(5.0, testNaN.mannWhitneyU(xNaN, yNaN), 0.001);
    }

    /**
     * Test method for {@link MannWhitneyUTest#mannWhitneyUTest()}.
     */
    @Test
    public void testMannWhitneyUTest() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {2.0, 3.0, 4.0};
        double pValue = test.mannWhitneyUTest(x, y);
        assertTrue(pValue > 0.0 && pValue < 0.1);

        // Test with empty arrays
        assertThrows(NoDataException.class, () -> test.mannWhitneyUTest(new double[0], new double[0]));
        assertThrows(NoDataException.class, () -> test.mannWhitneyUTest(new double[]{1.0}, new double[]{}));
        assertThrows(NoDataException.class, () -> test.mannWhitneyUTest(new double[]{}, new double[1.0]));

        // Test with null arrays
        assertThrows(NullArgumentException.class, () -> test.mannWhitneyUTest(null, y));
        assertThrows(NullArgumentException.class, () -> test.mannWhitneyUTest(x, null));

        // Test with arrays of different sizes
        double[] x1 = {1.0, 2.0, 3.0, 4.0};
        double[] y1 = {1.0, 2.0, 3.0};
        MannWhitneyUTest test1 = new MannWhitneyUTest();
        try {
            test1.mannWhitneyUTest(x1, y1);
            fail("Expected NoDataException");
        } catch (NoDataException e) {
            // Expected
        }
        MannWhitneyUTest test2 = new MannWhitneyUTest(NaNStrategy.REPLACE, TiesStrategy.MIN);
        double[] xNaN = {1.0, 2.0, Double.NaN};
        double[] yNaN = {2.0, Double.NaN, 3.0};
        double pValueNaN = test2.mannWhitneyUTest(xNaN, yNaN);
        assertTrue(pValueNaN > 0.0 && pValueNaN < 0.1);

        // Test with same arrays
        double[] xSame = {1.0, 2.0, 3.0};
        double[] ySame = {1.0, 2.0, 3.0};
        MannWhitneyUTest testSame = new MannWhitneyUTest();
        try {
            testSame.mannWhitneyUTest(xSame, ySame);
            fail("Expected NoDataException");
        } catch (NoDataException e) {
            // Expected
        }

        // Test with arrays containing NaNs
        double[] xNaN2 = {1.0, 2.0, Double.NaN};
        double[] yNaN2 = {2.0, Double.NaN, 3.0};
        MannWhitneyUTest testNaN2 = new MannWhitneyUTest(NonNullStrategy.REPLACE, TiesStrategy.MIN);
        try {
            testNaN2.mannWhitneyUTest(xNaN2, yNaN2);
            fail("Expected ConvergenceException or MaxCountExceededException");
        } catch (ConvergenceException e) {
            // Expected
        } catch (MaxCountExceededException e) {
            // Expected
        }
    }

    /**
     * Test method for {@link MannWhitneyUTest#mannWhitneyUTest(NaNStrategy, TiesStrategy)}.
     */
    @Test
    public void testMannWhitneyUTestWithStrategies() {
        MannWhitneyUTest test = new MannWhitneyUTest(NaNStrategy.SKIP, TiesStrategy.LOWER);
        double[] x = {1.0, 2.0, 3.0, Double.NaN};
        double[] y = {2.0, 3.0, 4.0, Double.NaN};
        double pValue = test.mannWhitneyUTest(x, y);
        assertTrue(pValue > 0.0 && pValue < 0.1);

        // Test with empty arrays
        assertThrows(NoDataException.class, () -> test.mannWhitneyUTest(new double[0], new double[0]));
        assertThrows(NoDataException.class, () -> test.mannWhitneyUTest(new double[]{1.0}, new double[]{}));
        assertThrows(NoDataException.class, () -> test.mannWhitneyUTest(new double[]{}, new double[1.0]));

        // Test with null arrays
        assertThrows(NullArgumentException.class, () -> test.mannWhitneyUTest(null, y));
        assertThrows(NullArgumentException.class, () -> test.mannWhitneyUTest(x, null));

        // Test with arrays of different sizes
        double[] x1 = {1.0, 2.0, 3.0, 4.0};
        double[] y1 = {1.0, 2.0, 3.0};
        MannWhitneyUTest test1 = new MannWhitneyUTest(NaNStrategy.REPLACE, TiesStrategy.MIN);
        try {
            test1.mannWhitneyUTest(x1, y1);
            fail("Expected NoDataException");
        } catch (NoDataException e) {
            // Expected
        }
        MannWhitneyUTest test2 = new MannWhitneyUTest(NaNStrategy.SKIP, TiesStrategy.MEDIAN);
        double[] xNaN = {1.0, 2.0, Double.NaN};
        double[] yNaN = {2.0, Double.NaN, 3.0};
        double pValueNaN = test2.mannWhitneyUTest(xNaN, yNaN);
        assertTrue(pValueNaN > 0.0 && pValueNaN < 0.1);

        // Test with same arrays
        double[] xSame = {1.0, 2.0, 3.0};
        double[] ySame = {1.0, 2.0, 3.0};
        MannWhitneyUTest testSame = new MannWhitneyUTest(NaNStrategy.SKIP, TiesStrategy.LOWER);
        try {
            testSame.mannWhitneyUTest(xSame, ySame);
            fail("Expected NoDataException");
        } catch (NoDataException e) {
            // Expected
        }

        // Test with arrays containing NaNs
        double[] xNaN2 = {1.0, 2.0, Double.NaN};
        double[] yNaN2 = {2.0, Double.NaN, 3.0};
        MannWhitneyUTest testNaN2 = new MannWhitneyUTest(NaNStrategy.SKIP, TiesStrategy.MIN);
        try {
            testNaN2.mannWhitneyUTest(xNaN2, yNaN2);
            fail("Expected ConvergenceException or MaxCountExceededException");
        } catch (ConvergenceException e) {
            // Expected
        } catch (MaxCountExceededException e) {
            // Expected
        }
    }

    /**
     * Test method for {@link MannWhitneyUTest#calculateAsymptoticPValue(double, int, int)}.
     */
    @Test
    public void testCalculateAsymptoticPValue() throws ConvergenceException, MaxCountExceededException {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double Umin = 3.5;
        int n1 = 5;
        int n2 = 4;
        double pValue = test.calculateAsymptoticPValue(Umin, n1, n2);
        assertEquals(0.006462278853224225, pValue, 0.00000001);

        // Test with negative Umin
        int n1Neg = 5;
        int n2Neg = 4;
        try {
            test.calculateAsymptoticPValue(-3.5, n1Neg, n2Neg);
            fail("Expected ConvergenceException or MaxCountExceededException");
        } catch (ConvergenceException e) {
            // Expected
        } catch (MaxCountExceededException e) {
            // Expected
        }

        // Test with NaNs
        MannWhitneyUTest testNaN = new MannWhitneyUTest(NaNStrategy.REPLACE, TiesStrategy.MIN);
        double UminNaN = 3.5;
        int n1NaN = 5;
        int n2NaN = 4;
        try {
            testNaN.calculateAsymptoticPValue(UminNaN, n1NaN, n2NaN);
            fail("Expected ConvergenceException or MaxCountExceededException");
        } catch (ConvergenceException e) {
            // Expected
        } catch (MaxCountExceededException e) {
            // Expected
        }
    }

}