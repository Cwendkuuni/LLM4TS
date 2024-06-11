package org.apache.commons.math3.stat.inference.test;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.apache.commons.math3.stat.inference.MannWhitneyUTest;

/**
 * Test cases for {@link MannWhitneyUTest}.
 */
public class MannWhitneyUTestTest {

    /**
     * Test mannWhitneyU method with two identical samples.
     */
    @Test
    public void testMannWhitneyUIdenticalSamples() {
        final double[] sample1 = new double[] { 1.0, 1.5, 1.7, 2.0 };
        final double[] sample2 = new double[] { 1.0, 1.5, 1.7, 2.0 };

        final MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        final double mannWhitneyU = mannWhitneyUTest.mannWhitneyU(sample1, sample2);
        final double expectedMannWhitneyU = 6.0; // sum of ranks for each sample is 6 * 4 = 24, U = 24 / 2 = 12, and U1 + U2 = n1 * n2 = 4 * 4 = 16, so U1 = 16 - 12 = 4, U2 = 16 - 4 = 12, Umin = 16 - (4 * 4 / 2) = 12

        assertEquals(expectedMannWhitneyU, mannWhitneyU, 0.0);
    }

    /**
     * Test mannWhitneyU method with two different samples.
     */
    @Test
    public void testMannWhitneyUDifferentSamples() {
        final double[] sample1 = new double[] { 1.0, 1.5, 1.7, 2.0 };
        final double[] sample2 = new double[] { 2.5, 3.0, 3.5, 4.0 };

        final MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        final double mannWhitneyU = mannWhitneyUTest.mannWhitneyU(sample1, sample2);
        final double expectedMannWhitneyU = 3.0; // rank of 1.0 in sample1: 1, 3, 4, 5 (sum rank = 15), rank of 2.5 in sample2: 4, 5, 6, 7 (sum rank = 21), U1 = 15 - (4 * 4 / 2) = 11, U2 = 21 - 11 = 10, so Umin = 11 + 10 = 21 - (5 * 4) = 3

        assertEquals(expectedMannWhitneyU, mannWhitneyU, 0.0);
    }

    /**
     * Test mannWhitneyUTest method with two identical samples.
     */
    @Test
    public void testMannWhitneyUTestIdenticalSamples() {
        final double[] sample1 = new double[] { 1.0, 1.5, 1.7, 2.0 };
        final double[] sample2 = new double[] { 1.0, 1.5, 1.7, 2.0 };

        final MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        final double pValue = mannWhitneyUTest.mannWhitneyUTest(sample1, sample2);
        final double expectedPValue = 1.0; // since identical samples have the same mean, the p-value should be 1.0

        assertEquals(expectedPValue, pValue, 0.0);
    }

    /**
     * Test mannWhitneyUTest method with two different samples.
     */
    @Test
    public void testMannWhitneyUTestDifferentSamples() {
        final double[] sample1 = new double[] { 1.0, 1.5, 1.7, 2.0 };
        final double[] sample2 = new double[] { 2.5, 3.0, 3.5, 4.0 };

        final MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        final double pValue = mannWhitneyUTest.mannWhitneyUTest(sample1, sample2);
        final double expectedPValue = 0.05; // assuming n1 = 4, n2 = 4, EU = 18, VarU = 3.375, z = (Umin - EU) / sqrt(VarU) = (3 - 18) / sqrt(3.375) = -3.14, and the standard normal cumulative probability of -3.14 is approximately 0.05

        assertEquals(expectedPValue, pValue, 0.01);
    }

    /**
     * Test mannWhitneyUTest method with empty samples.
     */
    @Test(expected = NoDataException.class)
    public void testMannWhitneyUTestEmptySamples() {
        final MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        mannWhitneyUTest.mannWhitneyUTest(new double[] {}, new double[] {});
    }

    /**
     * Test mannWhitneyUTest method with null samples.
     */
    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUTestNullSamples() {
        final MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        mannWhitneyUTest.mannWhitneyUTest(null, null);
    }
}