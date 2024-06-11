package org.apache.commons.math3.stat.inference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.stat.inference.MannWhitneyUTest;
import org.apache.commons.math3.stat.ranking.NaturalRanking;
import org.apache.commons.math3.stat.ranking.TiesStrategy;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.exception.NoSuchAlgorithmException;
import org.junit.Test;

/**
 * Test class for {@link MannWhitneyUTest}.
 */
public class MannWhitneyUTestTest {

    /**
     * Test method for {@link MannWhitneyUTest#mannWhitneyU(double[], double[])}.
     */
    @Test
    public void testMannWhitneyU() {
        MannWhitneyUTest mwu = new MannWhitneyUTest();

        // Test with two empty arrays
        assertThrows(NoDataException.class, () -> mwu.mannWhitneyU(new double[]{}, new double[]{}));

        // Test with two arrays of same length and containing the same elements
        double[] x = { 1.0, 2.0, 2.0, 3.0 };
        double[] y = { 1.0, 2.0, 2.0, 3.0 };
        double u = mwu.mannWhitneyU(x, y);
        assertEquals(6.0, u, 0.0);

        // Test with two arrays of different length and containing different elements
        double[] x2 = { 1.0, 2.0, 2.0, 3.0 };
        double[] y2 = { 4.0, 5.0, 6.0 };
        double u2 = mwu.mannWhitneyU(x2, y2);
        assertEquals(12.0, u2, 0.0);

        // Test with arrays containing NaN values
        double[] xNaN = { 1.0, 2.0, Double.NaN, 3.0 };
        double[] yNaN = { 1.0, 2.0, Double.NaN, 3.0, 4.0 };
        double uNaN = mwu.mannWhitneyU(xNaN, yNaN);
        assertEquals(10.0, uNaN, 0.0);

        // Test with arrays containing ties
        double[] xTies = { 1.0, 2.0, 2.0, 3.0, 3.0 };
        double[] yTies = { 1.0, 2.0, 2.0, 3.0 };
        double uTies = mwu.mannWhitneyU(xTies, yTies);
        assertEquals(7.5, uTies, 0.1);
    }

    /**
     * Test method for {@link MannWhitneyUTest#mannWhitneyUTest()}.
     */
    @Test
    public void testMannWhitneyUTest() throws NoSuchAlgorithmException {
        MannWhitneyUTest mwu = new MannWhitneyUTest();
        double[] x = { 1.0, 2.0, 2.0, 3.0 };
        double[] y = { 4.0, 5.0, 6.0 };
        double pValue = mwu.mannWhitneyUTest(x, y);
        assertEquals(0.01477779258233898, pValue, Precision.EPSILON);
    }

    /**
     * Test method for {@link MannWhitneyUTest#mannWhitneyUTest(NaNStrategy, TiesStrategy)}.
     */
    @Test
    public void testMannWhitneyUTestCustom() {
        MannWhitneyUTest mwu = new MannWhitneyUTest(NaturalRanking.MIN_RANK, TiesStrategy.MIN);

        // Test with two arrays of same length and containing the same elements
        double[] x = { 1.0, 2.0, 2.0, 3.0 };
        double[] y = { 1.0, 2.0, 2.0, 3.0 };
        double u = mwu.mannWhitneyU(x, y);
        assertEquals(6.0, u, 0.0);

        // Test with arrays containing NaN values
        double[] xNaN = { 1.0, 2.0, Double.NaN, 3.0 };
        double[] yNaN = { 1.0, 2.0, Double.NaN, 3.0, 4.0 };
        double uNaN = mwu.mannWhitneyU(xNaN, yNaN);
        assertEquals(8.0, uNaN, 0.0);

        // Test with arrays containing ties
        double[] xTies = { 1.0, 2.0, 2.0, 3.0, 3.0 };
        double[] yTies = { 1.0, 2.0, 2.0, 3.0 };
        double uTies = mwu.mannWhitneyU(xTies, yTies);
        assertEquals(6.5, uTies, 0.1);
    }

    /**
     * Test method for {@link MannWhitneyUTest#mannWhitneyUTest(NaNStrategy, TiesStrategy)}
     * with illegal arguments.
     */
    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUTestIllegalArgument() {
        new MannWhitneyUTest(null, TiesStrategy.MIN);
    }

    /**
     * Test method for {@link MannWhitneyUTest#mannWhitneyUTest(NaNStrategy, TiesStrategy)}
     * with NaNStrategy.RANDOM.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMannWhitneyUTestIllegalStrategy() {
        new MannWhitneyUTest(NaturalRanking.RANDOM, TiesStrategy.MIN);
    }

    /**
     * Test method for {@link MannWhitneyUTest#calculateAsymptoticPValue(double, int, int)}
     * with illegal arguments.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCalculateAsymptoticPValueIllegalArguments() {
        new MannWhitneyUTest().calculateAsymptoticPValue(-1.0, 1, 1);
    }
}