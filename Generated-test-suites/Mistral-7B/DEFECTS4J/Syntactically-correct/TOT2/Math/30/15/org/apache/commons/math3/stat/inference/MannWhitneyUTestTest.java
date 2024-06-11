package org.apache.commons.math3.stat.inference;

import static org.junit.Assert.assertEquals;

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

    /**
     * Tests that the Mann-Whitney U test with no data throws the expected
     * exception.
     *
     * @throws Exception if an error occurs
     */
    @Test(expected = NoDataException.class)
    public void testNoDataException() throws Exception {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        mannWhitneyUTest.mannWhitneyUTest(new double[0], new double[0]);
    }

    /**
     * Tests that the Mann-Whitney U test with null data throws the expected
     * exception.
     *
     * @throws Exception if an error occurs
     */
    @Test(expected = NullArgumentException.class)
    public void testNullDataException() throws Exception {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        mannWhitneyUTest.mannWhitneyUTest(null, new double[0]);
        mannWhitneyUTest.mannWhitneyUTest(new double[0], null);
        mannWhitneyUTest.mannWhitneyUTest(null, null);
    }

    /**
     * Tests that the Mann-Whitney U test returns the correct result with two
     * identical samples.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testIdenticalSamples() throws Exception {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double[] x = { 1.0, 1.5, 1.3, 1.2 };
        double[] y = x.clone();
        double expected = mannWhitneyUTest.mannWhitneyU(x, y);
        double result = mannWhitneyUTest.mannWhitneyUTest(x, y);
        assertEquals(expected, result, 0.0);
    }

    /**
     * Tests that the Mann-Whitney U test returns the correct result with two
     * different samples.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testDifferentSamples() throws Exception {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double[] x = { 1.0, 1.5, 1.3, 1.2 };
        double[] y = { 2.5, 2.8, 2.1, 2.9 };
        double expected = 3.0;
        double result = mannWhitneyUTest.mannWhitneyUTest(x, y);
        assertEquals(expected, result, 0.0);
    }

    /**
     * Tests that the Mann-Whitney U test handles NaN values correctly.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testNaNValues() throws Exception {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double[] x = { 1.0, 1.5, 1.3, 1.2, Double.NaN };
        double[] y = { 2.5, 2.8, 2.1, 2.9, Double.NaN };
        double expected = 4.5;
        double result = mannWhitneyUTest.mannWhitneyUTest(x, y);
        assertEquals(expected, result, 0.0);
    }

    /**
     * Tests that the Mann-Whitney U test handles ties correctly.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testTies() throws Exception {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double[] x = { 1.0, 1.5, 1.3, 1.2, 1.1, 1.1 };
        double[] y = { 2.5, 2.8, 2.1, 2.9, 1.1, 1.1 };
        double expected = 6.0;
        double result = mannWhitneyUTest.mannWhitneyUTest(x, y);
        assertEquals(expected, result, 0.0);
    }

    /**
     * Tests that the Mann-Whitney U test handles convergence
     * exception correctly.
     *
     * @throws Exception if an error occurs
     */
    @Test(expected = ConvergenceException.class)
    public void testConvergenceException()
        throws Exception, ConvergenceException, MaxCountExceededException,
        NoDataException, NullArgumentException {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double[] x = { 1.0, 1.5, 1.3, 1.2 };
        double[] y = { Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY,
                Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY };
        mannWhitneyUTest.mannWhitneyUTest(x, y);
    }

    /**
     * Tests that the Mann-Whitney U test handles maximum count exceeded
     * exception correctly.
     *
     * @throws Exception if an error occurs
     */
    @Test(expected = MaxCountExceededException.class)
    public void testMaxCountExceededException()
        throws Exception, ConvergenceException, MaxCountExceededException,
        NoDataException, NullArgumentException {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double[] x = new double[Integer.MAX_VALUE];
        double[] y = new double[Integer.MAX_VALUE];
        mannWhitneyUTest.mannWhitneyUTest(x, y);
    }

}