package org.apache.commons.math3.stat.inference.test;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.stat.inference.MannWhitneyUTest;
import org.apache.commons.math3.stat.ranking.NaturalRanking;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test class for {@link MannWhitneyUTest}.
 */
public class MannWhitneyUTestTest {

    /**
     * Tests the Mann-Whitney U test with two identical arrays.
     */
    @Test
    public void testIdenticalArrays() {
        final double[] x = new double[] {1.0, 1.0, 1.0};
        final double[] y = new double[] {1.0, 1.0, 1.0};

        try {
            MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
            final double result = mannWhitneyUTest.mannWhitneyUTest(x, y);
            assertEquals(0.0, result, 1e-10);
        } catch (NullArgumentException | NoDataException |
               ConvergenceException | MaxCountExceededException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Tests the Mann-Whitney U test with two different arrays with no common
     * elements.
     */
    @Test
    public void testDifferentArraysNoCommonElements() {
        final double[] x = new double[] {1.0, 2.0, 3.0};
        final double[] y = new double[] {4.0, 5.0, 6.0};

        try {
            MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
            final double result = mannWhitneyUTest.mannWhitneyUTest(x, y);
            assertEquals(6.0, result, 1e-10);
        } catch (NullArgumentException | NoDataException |
               ConvergenceException | MaxCountExceededException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Tests the Mann-Whitney U test with two different arrays with common
     * elements.
     */
    @Test
    public void testDifferentArraysCommonElements() {
        final double[] x = new double[] {1.0, 2.0, 3.0, 4.0};
        final double[] y = new double[] {1.0, 2.0, 3.0, 4.0};

        try {
            MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
            final double result = mannWhitneyUTest.mannWhitneyUTest(x, y);
            assertEquals(10.0, result, 1e-10);
        } catch (NullArgumentException | NoDataException |
               ConvergenceException | MaxCountExceededException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Tests the Mann-Whitney U test with an empty array for the first sample.
     */
    @Test(expected = NoDataException.class)
    public void testEmptyFirstArray() {
        final double[] x = new double[] {};
        final double[] y = new double[] {1.0, 2.0, 3.0};

        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        mannWhitneyUTest.mannWhitneyUTest(x, y);
    }

    /**
     * Tests the Mann-Whitney U test with an empty array for the second sample.
     */
    @Test(expected = NoDataException.class)
    public void testEmptySecondArray() {
        final double[] x = new double[] {1.0, 2.0, 3.0};
        final double[] y = new double[] {};

        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        mannWhitneyUTest.mannWhitneyUTest(x, y);
    }

    /**
     * Tests the Mann-Whitney U test with null arrays.
     */
    @Test(expected = NullArgumentException.class)
    public void testNullArrays() {
        final double[] x = null;
        final double[] y = new double[] {1.0, 2.0, 3.0};

        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        mannWhitneyUTest.mannWhitneyUTest(x, y);
    }

    /**
     * Tests the Mann-Whitney U test with arrays containing NaNs.
     */
    @Test
    public void testArraysWithNaNs() {
        final double[] x = new double[] {1.0, 2.0, Double.NaN};
        final double[] y = new double[] {Double.NaN, 3.0, 4.0};

        try {
            MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest(
                    NaNStrategy.SKIP, TiesStrategy.MIN);
            final double result = mannWhitneyUTest.mannWhitneyUTest(x, y);
            assertEquals(3.0, result, 1e-10);
        } catch (NullArgumentException | NoDataException |
               ConvergenceException | MaxCountExceededException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Tests the Mann-Whitney U test with arrays containing infinite values.
     */
    @Test
    public void testArraysWithInfiniteValues() {
        final double[] x = new double[] {Double.POSITIVE_INFINITY, 2.0, 3.0};
        final double[] y = new double[] {1.0, Double.POSITIVE_INFINITY, 4.0};

        try {
            MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest(
                    NaNStrategy.SKIP, TiesStrategy.MIN);
            final double result = mannWhitneyUTest.mannWhitneyUTest(x, y);
            assertEquals(3.0, result, 1e-10);
        } catch (NullArgumentException | NoDataException |
               ConvergenceException | MaxCountExceededException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Tests the Mann-Whitney U test with arrays having different sizes.
     */
    @Test
    public void testArraysDifferentSizes() {
        final double[] x = new double[] {1.0, 2.0, 3.0};
        final double[] y = new double[] {1.0, 2.0};

        try {
            MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
            final double result = mannWhitneyUTest.mannWhitneyUTest(x, y);
            assertEquals(3.0, result, 1e-10);
        } catch (NullArgumentException | NoDataException |
               ConvergenceException | MaxCountExceededException e) {
            throw new RuntimeException(e);
        }
    }
}