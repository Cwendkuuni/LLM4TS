package org.apache.commons.math3.stat.inference.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.stat.inference.MannWhitneyUTest;
import org.apache.commons.math3.stat.ranking.NaturalRanking;
import org.junit.Test;

/**
 * Tests for {@link MannWhitneyUTest}.
 */
public class MannWhitneyUTestTest {

    @Test(expected = NullArgumentException.class)
    public void testNullArgumentException() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        test.mannWhitneyUTest(null, new double[0]);
    }

    @Test(expected = NoDataException.class)
    public void testNoDataException() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        test.mannWhitneyUTest(new double[0], new double[0]);
    }

    @Test
    public void testMannWhitneyU() {
        MannWhitneyUTest test = new MannWhitneyUTest();

        double[] x = {1.0, 2.0, 3.0};
        double[] y = {1.5, 2.5, 3.5};
        double expectedU = 6.0;
        double actualU = test.mannWhitneyU(x, y);
        assertEquals(expectedU, actualU, 0.0);
    }

    @Test(expected = ConvergenceException.class)
    public void testConvergenceException() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = new double[100_000];
        double[] y = new double[100_001];
        test.mannWhitneyUTest(x, y);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testMaxCountExceededException() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = new double[Integer.MAX_VALUE];
        double[] y = new double[Integer.MAX_VALUE];
        test.mannWhitneyUTest(x, y);
    }

    @Test
    public void testCalculateAsymptoticPValue() throws ConvergenceException,
            MaxCountExceededException {
        MannWhitneyUTest test = new MannWhitneyUTest();

        double[] x = {1.0, 2.0, 3.0};
        double[] y = {1.5, 2.5, 3.5};

        double expectedPValue = 0.024;
        double actualPValue = test.mannWhitneyUTest(x, y);
        assertEquals(expectedPValue, actualPValue, 0.0);
    }

    @Test
    public void testNaNStrategy() {
        MannWhitneyUTest test = new MannWhitneyUTest(NaturalRanking.MISSING,
                NaturalRanking.MIN_RANK);

        double[] x = {1.0, 2.0, Double.NaN, 3.0};
        double[] y = {1.5, 2.5, 3.5, Double.NaN};

        double expectedU = 4.5;
        double actualU = test.mannWhitneyU(x, y);
        assertEquals(expectedU, actualU, 0.0);
    }

    @Test
    public void testTiesStrategy() {
        MannWhitneyUTest test = new MannWhitneyUTest(NaturalRanking.MIN_RANK,
                NaturalRanking.LOWEST_TIE);

        double[] x = {1.0, 1.5, 2.0, 2.5, 3.0};
        double[] y = {1.0, 1.5, 2.0, 2.5, 3.0};

        double expectedU = 5.0;
        double actualU = test.mannWhitneyU(x, y);
        assertEquals(expectedU, actualU, 0.0);
    }
}