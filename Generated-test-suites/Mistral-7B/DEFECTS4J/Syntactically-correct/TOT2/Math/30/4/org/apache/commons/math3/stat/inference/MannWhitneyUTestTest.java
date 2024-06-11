package org.apache.commons.math3.stat.inference;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.stat.ranking.NaNStrategy;
import org.apache.commons.math3.stat.ranking.NaturalRanking;
import org.apache.commons.math3.stat.ranking.TiesStrategy;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for MannWhitneyUTest.
 */
public class MannWhitneyUTestTest {

    /**
     * Test for method mannWhitneyU with no NaN values and no ties.
     */
    @Test
    public void testMannWhitneyU_noNaN_noTies() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = new double[]{1.0, 2.0, 3.0};
        double[] y = new double[]{4.0, 5.0, 6.0};
        double expected = 6.0;
        double actual = test.mannWhitneyU(x, y);
        assertEquals(expected, actual, 0.0);
    }

    /**
     * Test for method mannWhitneyU with NaN values in one sample.
     */
    @Test(expected = NoDataException.class)
    public void testMannWhitneyU_NaN_inSample() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = new double[]{1.0, 2.0, Double.NaN};
        double[] y = new double[]{4.0, 5.0, 6.0};
        test.mannWhitneyU(x, y);
    }

    /**
     * Test for method mannWhitneyU with NaN values in both samples.
     */
    @Test(expected = NoDataException.class)
    public void testMannWhitneyU_NaN_inBothSamples() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = new double[]{Double.NaN, Double.NaN};
        double[] y = new double[]{Double.NaN, Double.NaN};
        test.mannWhitneyU(x, y);
    }

    /**
     * Test for method mannWhitneyU with ties.
     */
    @Test
    public void testMannWhitneyU_ties() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = new double[]{1.0, 2.0, 2.0, 3.0};
        double[] y = new double[]{3.0, 3.0, 3.0, 4.0};
        double expected = 4.5;
        double actual = test.mannWhitneyU(x, y);
        assertEquals(expected, actual, 0.0);
    }

    /**
     * Test for method mannWhitneyUTest with no NaN values and no ties.
     */
    @Test
    public void testMannWhitneyUTest_noNaN_noTies() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = new double[]{1.0, 2.0, 3.0};
        double[] y = new double[]{4.0, 5.0, 6.0};
        double expected = 0.05;
        double actual = test.mannWhitneyUTest(x, y);
        assertEquals(expected, actual, 0.001);
    }

    /**
     * Test for method mannWhitneyUTest with NaN values in one sample.
     */
    @Test(expected = NoDataException.class)
    public void testMannWhitneyUTest_NaN_inSample() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = new double[]{1.0, 2.0, Double.NaN};
        double[] y = new double[]{4.0, 5.0, 6.0};
        test.mannWhitneyUTest(x, y);
    }

    /**
     * Test for method mannWhitneyUTest with NaN values in both samples.
     */
    @Test(expected = NoDataException.class)
    public void testMannWhitneyUTest_NaN_inBothSamples() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = new double[]{Double.NaN, Double.NaN};
        double[] y = new double[]{Double.NaN, Double.NaN};
        test.mannWhitneyUTest(x, y);
    }

    /**
     * Test for method mannWhitneyUTest with different sample sizes.
     */
    @Test
    public void testMannWhitneyUTest_differentSampleSizes() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = new double[]{1.0, 2.0, 3.0};
        double[] y = new double[]{4.0};
        double expected = 0.025;
        double actual = test.mannWhitneyUTest(x, y);
        assertEquals(expected, actual, 0.001);
    }

    /**
     * Test for method mannWhitneyUTest with equal sample sizes and identical samples.
     */
    @Test
    public void testMannWhitneyUTest_identicalSamples() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = new double[]{1.0, 2.0, 3.0};
        double[] y = new double[]{1.0, 2.0, 3.0};
        double expected = 1.0;
        double actual = test.mannWhitneyUTest(x, y);
        assertEquals(expected, actual, 0.0);
    }

    /**
     * Test for method mannWhitneyUTest with large samples.
     */
    @Test
    public void testMannWhitneyUTest_largeSamples() throws ConvergenceException, MaxCountExceededException {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = new double[10000];
        double[] y = new double[10000];
        for (int i = 0; i < x.length; i++) {
            x[i] = Math.random();
            y[i] = Math.random();
        }
        double expected = test.calculateAsymptoticPValue(test.mannWhitneyU(x, y), x.length, y.length);
        double actual = test.mannWhitneyUTest(x, y);
        assertEquals(expected, actual, 0.001);
    }
}