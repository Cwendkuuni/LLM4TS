package org.apache.commons.math3.stat.inference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.stat.ranking.NaNStrategy;
import org.apache.commons.math3.stat.ranking.NaturalRanking;
import org.apache.commons.math3.stat.ranking.TiesStrategy;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;

/**
 * JUnit test file for MannWhitneyUTest class.
 */
public class MannWhitneyUTestTest {

    /**
     * Test constructor MannWhitneyUTest().
     */
    @Test
    public void testMannWhitneyUTestConstruction() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        MannWhitneyUTest mannWhitneyUTestWithCustomStrategies = new MannWhitneyUTest(
                new NaNStrategy.Median(), new TiesStrategy.Minimum());
    }

    /**
     * Test method mannWhitneyU().
     */
    @Test
    public void testMannWhitneyU() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();

        // Test with empty arrays
        double[] x = {};
        double[] y = {};
        try {
            mannWhitneyUTest.mannWhitneyU(x, y);
            fail("NoDataException expected");
        } catch (NoDataException e) {
            // Expected
        }

        // Test with null arrays
        double[] xNull = null;
        double[] yNull = null;
        try {
            mannWhitneyUTest.mannWhitneyU(xNull, yNull);
            fail("NullArgumentException expected");
        } catch (NullArgumentException e) {
            // Expected
        }

        // Test with arrays of same length and equal data
        double[] xSameLengthAndEqualData = {1.0, 1.0};
        double[] ySameLengthAndEqualData = {1.0, 1.0};
        double mannWhitneyUResultSameLengthAndEqualData = mannWhitneyUTest
                .mannWhitneyU(xSameLengthAndEqualData, ySameLengthAndEqualData);
        assertEquals(1.0, mannWhitneyUResultSameLengthAndEqualData, 0.0);

        // Test with arrays of same length and different data
        double[] xSameLengthAndDifferentData = {1.0, 2.0};
        double[] ySameLengthAndDifferentData = {2.0, 1.0};
        double mannWhitneyUResultSameLengthAndDifferentData = mannWhitneyUTest
                .mannWhitneyU(xSameLengthAndDifferentData, ySameLengthAndDifferentData);
        assertEquals(1.0, mannWhitneyUResultSameLengthAndDifferentData, 0.0);

        // Test with arrays of different lengths
        double[] xDifferentLengths = {1.0, 2.0, 3.0};
        double[] yDifferentLengths = {2.0, 1.0, 3.0, 4.0};
        double mannWhitneyUResultDifferentLengths = mannWhitneyUTest
                .mannWhitneyU(xDifferentLengths, yDifferentLengths);
        assertEquals(6.0, mannWhitneyUResultDifferentLengths, 0.0);

        // Test negative numbers
        double[] xNegativeNumbers = {-1.0, -2.0, -3.0};
        double[] yNegativeNumbers = {-2.0, -1.0, -3.0, -4.0};
        double mannWhitneyUResultNegativeNumbers = mannWhitneyUTest
                .mannWhitneyU(xNegativeNumbers, yNegativeNumbers);
        assertEquals(6.0, mannWhitneyUResultNegativeNumbers, 0.0);

        // Test NaN values
        double[] xNaNValues = {1.0, Double.NaN, 3.0};
        double[] yNaNValues = {2.0, Double.NaN, 3.0, 4.0};
        try {
            mannWhitneyUTest.mannWhitneyU(xNaNValues, yNaNValues);
            fail("NullPointerException or NoDataException expected");
        } catch (NullPointerException | NoDataException e) {
            // Expected
        }

        // Test custom strategies
        MannWhitneyUTest mannWhitneyUTestCustomStrategies = new MannWhitneyUTest(
                new NaNStrategy.Median(), new TiesStrategy.Continuous());
        double[] xCustomStrategies = {1.0, Double.NaN, 2.0, Double.NaN};
        double[] yCustomStrategies = {2.0, 1.0, 3.0, 4.0};
        try {
            mannWhitneyUTestCustomStrategies
                    .mannWhitneyU(xCustomStrategies, yCustomStrategies);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        double mannWhitneyUResultCustomStrategies = mannWhitneyUTestCustomStrategies
                .mannWhitneyU(new double[] {}, new double[]{1.0});
        assertEquals(0.0, mannWhitneyUResultCustomStrategies, 0.0);

        mannWhitneyUResultCustomStrategies = mannWhitneyUTestCustomStrategies
                .mannWhitneyU(new double[]{1.0}, new double[]{1.0});
        assertEquals(1.0, mannWhitneyUResultCustomStrategies, 0.0);

        mannWhitneyUResultCustomStrategies = mannWhitneyUTestCustomStrategies
                .mannWhitneyU(new double[]{1.0, 1.0, 2.0},
                        new double[]{1.0, 2.0, 2.0, 3.0});
        assertEquals(3.0, mannWhitneyUResultCustomStrategies, 0.0);

        mannWhitneyUResultCustomStrategies = mannWhitneyUTestCustomStrategies
                .mannWhitneyU(new double[]{1.0, 1.0, 2.0, Double.NaN},
                        new double[]{1.0, 2.0, 2.0, 3.0, 4.0});
        assertEquals(4.5, mannWhitneyUResultCustomStrategies, 0.0);

        mannWhitneyUResultCustomStrategies = mannWhitneyUTestCustomStrategies
                .mannWhitneyU(new double[]{1.0, 1.0, 2.0, 2.0, Double.NaN},
                        new double[]{1.0, 2.0, 2.0, 3.0, 3.0, 4.0});
        assertEquals(5.5, mannWhitneyUResultCustomStrategies, 0.0);
    }

    /**
     * Test method mannWhitneyUTest().
     */
    @Test
    public void testMannWhitneyUTest() throws ConvergenceException,
            MaxCountExceededException {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();

        // Test with empty arrays
        double[] x = {};
        double[] y = {};
        try {
            mannWhitneyUTest.mannWhitneyUTest(x, y);
            fail("NoDataException expected");
        } catch (NoDataException e) {
            // Expected
        }

        // Test with null arrays
        double[] xNull = null;
        double[] yNull = null;
        try {
            mannWhitneyUTest.mannWhitneyUTest(xNull, yNull);
            fail("NullArgumentException expected");
        } catch (NullArgumentException e) {
            // Expected
        }

        // Test with arrays of same length and equal data
        double[] xSameLengthAndEqualData = {1.0, 1.0};
        double[] ySameLengthAndEqualData = {1.0, 1.0};
        double pValueSameLengthAndEqualData = mannWhitneyUTest
                .mannWhitneyUTest(xSameLengthAndEqualData, ySameLengthAndEqualData);
        assertEquals(0.0, pValueSameLengthAndEqualData, 0.01);

        // Test with arrays of same length and different data
        double[] xSameLengthAndDifferentData = {1.0, 2.0};
        double[] ySameLengthAndDifferentData = {2.0, 1.0};
        double pValueSameLengthAndDifferentData = mannWhitneyUTest
                .mannWhitneyUTest(xSameLengthAndDifferentData, ySameLengthAndDifferentData);
        assertTrue(pValueSameLengthAndDifferentData > 0.05);

        // Test with arrays of different lengths
        double[] xDifferentLengths = {1.0, 2.0, 3.0};
        double[] yDifferentLengths = {2.0, 1.0, 3.0, 4.0};
        double pValueDifferentLengths = mannWhitneyUTest
                .mannWhitneyUTest(xDifferentLengths, yDifferentLengths);
        assertTrue(pValueDifferentLengths > 0.05);

        // Test negative numbers
        double[] xNegativeNumbers = {-1.0, -2.0, -3.0};
        double[] yNegativeNumbers = {-2.0, -1.0, -3.0, -4.0};
        double pValueNegativeNumbers = mannWhitneyUTest
                .mannWhitneyUTest(xNegativeNumbers, yNegativeNumbers);
        assertTrue(pValueNegativeNumbers > 0.05);

        // Test NaN values
        double[] xNaNValues = {1.0, Double.NaN, 3.0};
        double[] yNaNValues = {2.0, 1.0, 3.0, 4.0};
        try {
            mannWhitneyUTest.mannWhitneyUTest(xNaNValues, yNaNValues);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test custom strategies
        MannWhitneyUTest mannWhitneyUTestCustomStrategies = new MannWhitneyUTest(
                new NaNStrategy.Median(), new TiesStrategy.Continuous());
        double[] xCustomStrategies = {1.0, Double.NaN, 2.0, Double.NaN};
        double[] yCustomStrategies = {2.0, 1.0, 3.0, 4.0};
        try {
            mannWhitneyUTestCustomStrategies
                    .mannWhitneyUTest(xCustomStrategies, yCustomStrategies);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        double[] xCustomStrategies2 = {1.0, 1.0, 2.0, Double.NaN};
        double[] yCustomStrategies2 = {1.0, 2.0, 2.0, 3.0, 4.0};
        double pValueCustomStrategies = mannWhitneyUTestCustomStrategies
                .mannWhitneyUTest(xCustomStrategies2, yCustomStrategies2);
        assertEquals(0.25, pValueCustomStrategies, 0.01);
    }
}