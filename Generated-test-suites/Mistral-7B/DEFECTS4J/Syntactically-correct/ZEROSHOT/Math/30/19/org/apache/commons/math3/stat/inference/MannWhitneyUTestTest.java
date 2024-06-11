package org.apache.commons.math3.stat.inference;

import static org.junit.Assert.assertEquals;

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
 * Tests for {@link MannWhitneyUTest}.
 */
public class MannWhitneyUTestTest {

    /** Ranking algorithm. */
    private NaturalRanking naturalRanking;

    /**
     * Create a test instance using where NaN's are left in place and ties get
     * the average of applicable ranks.
     */
    @SuppressWarnings("deprecation")
    MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();

    /**
     * Create a test instance using the given strategies for NaN's and ties.
     */
    @SuppressWarnings("deprecation")
    MannWhitneyUTest mannWhitneyUTestWithCustomStrategies;

    {
        naturalRanking = mannWhitneyUTest.getNaturalRanking();
        mannWhitneyUTestWithCustomStrategies = new MannWhitneyUTest(
                NANStrategy.MISSING, TiesStrategy.MIN);
    }

    /**
     * Test method for {@link MannWhitneyUTest#mannWhitneyU(double[], double[])}.
     */
    @Test
    public void testMannWhitneyU() {
        double[] x1 = {1.0d, 2.0d, 3.0d, 4.0d};
        double[] y1 = {2.5d, 3.0d, 3.5d, 4.5d};
        double expectedU1 = 6.0d;
        double actualU1 = mannWhitneyUTest.mannWhitneyU(x1, y1);
        assertEquals(expectedU1, actualU1, 0.001);

        double[] x2 = {1.0d, 2.0d, 3.0d, 4.0d, 5.0d};
        double[] y2 = {2.5d, 3.0d, 3.5d, 4.5d, 5.5d};
        double expectedU2 = 10.0d;
        double actualU2 = mannWhitneyUTest.mannWhitneyU(x2, y2);
        assertEquals(expectedU2, actualU2, 0.001);

        double[] x3 = {Double.NaN, 2.0d, 3.0d, 4.0d};
        double[] y3 = {2.5d, 3.0d, 3.5d, 4.5d, Double.NaN};
        double expectedU3 = 6.0d;
        double actualU3 = mannWhitneyUTestWithCustomStrategies.mannWhitneyU(x3, y3);
        assertEquals(expectedU3, actualU3, 0.001);
    }

    /**
     * Test method for {@link MannWhitneyUTest#mannWhitneyUTest(double[], double[])}.
     */
    @Test
    public void testMannWhitneyUTest() throws NoDataException,
            ConvergenceException, MaxCountExceededException {
        double[] x1 = {1.0d, 2.0d, 3.0d, 4.0d};
        double[] y1 = {2.5d, 3.0d, 3.5d, 4.5d};
        double expectedPValue = 0.16666666666666663;
        double actualPValue = mannWhitneyUTest.mannWhitneyUTest(x1, y1);
        assertEquals(expectedPValue, actualPValue, 0.00001);

        double[] x2 = {1.0d, 2.0d, 3.0d, 4.0d, 5.0d};
        double[] y2 = {2.5d, 3.0d, 3.5d, 4.5d, 5.5d};
        double expectedPValue2 = 0.02222222222222222;
        double actualPValue2 = mannWhitneyUTest.mannWhitneyUTest(x2, y2);
        assertEquals(expectedPValue2, actualPValue2, 0.00001);
    }
}