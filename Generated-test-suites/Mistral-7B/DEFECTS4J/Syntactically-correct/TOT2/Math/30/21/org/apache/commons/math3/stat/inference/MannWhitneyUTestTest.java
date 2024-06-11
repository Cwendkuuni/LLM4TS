package org.apache.commons.math3.stat.inference.test;

import static org.junit.Assert.assertEquals;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.stat.inference.MannWhitneyUTest;
import org.junit.Test;

/**
 * Tests for {@link MannWhitneyUTest}.
 */
public class MannWhitneyUTestTest {

    /**
     * Test method for {@link MannWhitneyUTest#mannWhitneyU(double[], double[])}.
     */
    @Test
    public void testMannWhitneyU() {
        final double[] x = new double[] { 1.0, 2.0, 3.0, 4.0 };
        final double[] y = new double[] { 3.0, 2.0, 1.0, 4.0 };
        final MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        final double mannWhitneyU = mannWhitneyUTest.mannWhitneyU(x, y);
        assertEquals(6.0, mannWhitneyU, 0.0);
    }

    /**
     * Test method for {@link MannWhitneyUTest#mannWhitneyUTest(double[], double[])}.
     */
    @Test
    public void testMannWhitneyUTest() {
        final double[] x = new double[] { 1.0, 2.0, 3.0, 4.0 };
        final double[] y = new double[] { 3.0, 2.0, 1.0, 4.0 };
        final MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        final double mannWhitneyUTestResult = mannWhitneyUTest.mannnWhitneyUTest(x, y);
        final double expected = 0.05; // assuming normal distribution with same mean and standard deviation for both samples
        assertEquals(expected, mannWhitneyUTestResult, 0.01);
    }

    /**
     * Test method for edge case: same sample.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSameSample() {
        final double[] x = new double[] { 1.0, 2.0, 3.0, 4.0 };
        final MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        mannWhitneyUTest.mannWhitneyUTest(x, x);
    }

    /**
     * Test method for edge case: null samples.
     */
    @Test(expected = NullPointerException.class)
    public void testNullSamples() {
        final MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        mannWhitneyUTest.mannWhitneyUTest(null, null);
    }

    /**
     * Test method for edge case: empty samples.
     */
    @Test(expected = NoDataException.class)
    public void testEmptySamples() {
        final MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        mannWhitneyUTest.mannWhitneyUTest(new double[] {}, new double[] {});
    }

    /**
     * Test method for exception handling: null input for ranking algorithm.
     */
    @Test(expected = NullArgumentException.class)
    public void testNullRankingAlgorithm() {
        final MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        mannWhitneyUTest.setNaturalRanking(null);
        mannWhitneyUTest.mannWhitneyUTest(new double[] { 1.0 }, new double[] { 2.0 });
    }
}