package org.apache.commons.math3.stat.inference.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.stat.inference.MannWhitneyUTest;
import org.apache.commons.math3.stat.ranking.NaturalRanking;
import org.apache.commons.math3.stat.ranking.TiesStrategy;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;

/**
 * Test class for {@link MannWhitneyUTest}.
 */
public class MannWhitneyUTestTest {

    /**
     * Test method for {@link MannWhitneyUTest#MannWhitneyUTest()}.
     */
    @Test
    public void testConstructor() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        assertNotNull(mannWhitneyUTest);
    }

    /**
     * Test method for {@link MannWhitneyUTest#mannWhitneyU(double[], double[])}.
     */
    @Test
    public void testMannWhitneyU() throws NullArgumentException, NoDataException {
        double[] x = new double[] { 1, 2, 3 };
        double[] y = new double[] { 3, 2, 1 };
        double mannWhitneyU = new MannWhitneyUTest().mannWhitneyU(x, y);
        assertEquals(3.0, mannWhitneyU, 0.0);
    }

    /**
     * Test method for {@link MannWhitneyUTest#mannWhitneyU(double[], double[])}.
     */
    @Test
    public void testMannWhitneyUWithTies() throws NullArgumentException, NoDataException {
        double[] x = new double[] { 1, 2, 2 };
        double[] y = new double[] { 2, 2, 3 };
        double mannWhitneyU = new MannWhitneyUTest().mannWhitneyU(x, y);
        assertEquals(3.5, mannWhitneyU, 0.0);
    }

    /**
     * Test method for {@link MannWhitneyUTest#mannWhitneyUTest(NaNStrategy, TiesStrategy)}.
     */
    @Test
    public void testConstructorWithStrategies() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest(
                NaNStrategy.REMOVE, TiesStrategy.MIN);
        assertNotNull(mannWhitneyUTest);
    }

    /**
     * Test method for {@link MannWhitneyUTest#mannWhitneyUTest(double[], double[])}.
     */
    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUWithNullArguments() {
        new MannWhitneyUTest().mannWhitneyU(null, null);
    }

    /**
     * Test method for {@link MannWhitneyUTest#mannWhitneyUTest(double[], double[])}.
     */
    @Test(expected = NoDataException.class)
    public void testMannWhitneyUWithEmptySamples() {
        new MannWhitneyUTest().mannWhitneyU(new double[] {}, new double[] {});
    }

    /**
     * Test method for {@link MannWhitneyUTest#mannWhitneyUTest(NaNStrategy, TiesStrategy)}.
     */
    @Test
    public void testMannWhitneyUTest() throws NullArgumentException,
            NoDataException, ConvergenceException, MaxCountExceededException {
        double[] x = new double[] { 1, 2, 3 };
        double[] y = new double[] { 3, 2, 1 };
        double mannWhitneyU = new MannWhitneyUTest().mannWhitneyUTest(x, y);
        assertEquals(0.1, mannWhitneyU, 0.01);
    }

    /**
     * Test method for {@link MannWhitneyUTest#mannWhitneyUTest(NaNStrategy, TiesStrategy)}.
     */
    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUTestWithNullArguments() {
        double[] x = null;
        double[] y = new double[] { 3, 2, 1 };
        new MannWhitneyUTest().mannWhitneyUTest(x, y);
    }

    /**
     * Test method for {@link MannWhitneyUTest#mannWhitneyUTest(double[], double[])}.
     */
    @Test(expected = NoDataException.class)
    public void testMannWhitneyUTestWithEmptySamples() {
        double[] x = new double[] {};
        double[] y = new double[] { 3, 2, 1 };
        new MannWhitneyUTest().mannWhitneyUTest(x, y);
    }

    /**
     * Test method for {@link MannWhitneyUTest#mannWhitneyUTest(NaNStrategy, TiesStrategy)}.
     */
    @Test(expected = ConvergenceException.class)
    public void testMannWhitneyUTestWithConvergenceException()
            throws NullArgumentException, NoDataException {
        double[] x = new double[] { 1, 2, 3, 4, 5 };
        double[] y = new double[] { 1, 2, 3, 4, 5 };
        new MannWhitneyUTest().mannWhitneyUTest(x, y);
    }

    /**
     * Test method for {@link MannWhitneyUTest#mannWhitneyUTest(NaNStrategy, TiesStrategy)}.
     */
    @Test(expected = MaxCountExceededException.class)
    public void testMannWhitneyUTestWithMaxCountExceededException()
            throws NullArgumentException, NoDataException {
        double[] x = new double[Integer.MAX_VALUE];
        double[] y = new double[Integer.MIN_VALUE];
        new MannWhitneyUTest().mannWhitneyUTest(x, y);
    }
}