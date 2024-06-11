package org.apache.commons.math3.stat.inference.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.stat.inference.MannWhitneyUTest;
import org.apache.commons.math3.stat.ranking.NaturalRanking;
import org.junit.Test;

/**
 * Test for {@link MannWhitneyUTest}.
 */
public class MannWhitneyUTestTest {

    /**
     * Test method for {@link MannWhitneyUTest# MannWhitneyU(double[], double[])}.
     */
    @Test
    public void testMannWhitneyU() {
        MannWhitneyUTest test = new MannWhitneyUTest();

        // Test with two empty arrays
        try {
            test.mannWhitneyU(new double[0], new double[0]);
            fail("Expected NoDataException");
        } catch (NoDataException e) {
            // Expected
        }

        // Test with two arrays of the same size and same elements
        double[] x = { 1.0, 2.0, 3.0, 3.0 };
        double[] y = { 1.0, 2.0, 3.0, 3.0 };
        assertEquals(6.0, test.mannWhitneyU(x, y), 0.0);

        // Test with two arrays of the same size but different elements
        x = new double[] { 1.0, 2.0, 3.0, 4.0 };
        y = new double[] { 2.0, 1.0, 3.0, 4.0 };
        assertEquals(4.0, test.mannWhitneyU(x, y), 0.0);

        // Test with two arrays of different sizes
        x = new double[] { 1.0, 2.0, 3.0 };
        y = new double[] { 2.0, 3.0, 4.0 };
        assertEquals(3.0, test.mannWhitneyU(x, y), 0.0);

        // Test with arrays containing NaN values
        x = new double[] { 1.0, Double.NaN, 3.0, Double.NaN };
        y = new double[] { 2.0, 3.0, 4.0, Double.NaN };
        NaturalRanking naturalRanking = new NaturalRanking(
                MannWhitneyUTest.NaNStrategy.LOWER,
                MannWhitneyUTest.TiesStrategy.AVERAGE);
        MannWhitneyUTest testWithNaN = new MannWhitneyUTest(
                naturalRanking, naturalRanking);
        assertEquals(6.0, testWithNaN.mannWhitneyU(x, y), 0.0);
    }

    /**
     * Test method for {@link MannWhitneyUTest#mannWhitneyUTest(double[], double[])}.
     */
    @Test
    public void testMannWhitneyUTest() {
        MannWhitneyUTest test = new MannWhitneyUTest();

        // Test with two empty arrays
        try {
            test.mannWhitneyUTest(new double[0], new double[0]);
            fail("Expected NoDataException");
        } catch (NoDataException e) {
            // Expected
        }

        // Test with two arrays of the same size and same elements
        double[] x = { 1.0, 2.0, 3.0, 3.0 };
        double[] y = { 1.0, 2.0, 3.0, 3.0 };
        double pValue = test.mannWhitneyUTest(x, y);
        assertTrue(pValue > 0.0 && pValue < 0.1);

        // Test with two arrays of the same size but different elements
        x = new double[] { 1.0, 2.0, 3.0, 4.0 };
        y = new double[] { 2.0, 1.0, 3.0, 4.0 };
        pValue = test.mannWhitneyUTest(x, y);
        assertTrue(pValue > 0.1 && pValue < 0.2);

        // Test with two arrays of different sizes
        x = new double[] { 1.0, 2.0, 3.0 };
        y = new double[] { 2.0, 3.0, 4.0 };
        pValue = test.mannWhitneyUTest(x, y);
        assertTrue(pValue > 0.2 && pValue < 0.3);

        // Test with arrays containing NaN values
        x = new double[] { 1.0, Double.NaN, 3.0, Double.NaN };
        y = new double[] { 2.0, 3.0, 4.0, Double.NaN };
        NaturalRanking naturalRanking = new NaturalRanking(
                MannWhitneyUTest.NaNStrategy.LOWER,
                MannWhitneyUTest.TiesStrategy.AVERAGE);
        MannWhitneyUTest testWithNaN = new MannWhitneyUTest(
                naturalRanking, naturalRanking);
        pValue = testWithNaN.mannWhitneyUTest(x, y);
        assertTrue(pValue > 0.3 && pValue < 0.4);
    }
}