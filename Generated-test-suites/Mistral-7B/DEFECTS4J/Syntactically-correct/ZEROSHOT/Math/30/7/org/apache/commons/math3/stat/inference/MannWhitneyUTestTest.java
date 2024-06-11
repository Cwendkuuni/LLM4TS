package org.apache.commons.math3.stat.inference.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.stat.inference.MannWhitneyUTest;
import org.apache.commons.math3.stat.ranking.NaNStrategy;
import org.apache.commons.math3.stat.ranking.NaturalRanking;
import org.apache.commons.math3.stat.ranking.TiesStrategy;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;

/**
 * This class tests the MannWhitneyUTest class.
 */
public class MannWhitneyUTestTest {

    /**
     * Test method for {@link MannWhitneyUTest#MannWhitneyUTest()}.
     */
    @Test
    public void testMannWhitneyUTestDefaultConstructor() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        assertNotNull(mannWhitneyUTest);
    }

    /**
     * Test method for {@link MannWhitneyUTest#MannWhitneyUTest(NaNStrategy, TiesStrategy)}.
     */
    @Test
    public void testMannWhitneyUTestCustomConstructor() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest(
                NaNStrategy.LOWEST, TiesStrategy.DITHER);
        assertNotNull(mannWhitneyUTest);
    }

    /**
     * Test method for {@link MannWhitneyUTest#ensureDataConformance(double[], double[])}.
     */
    @Test(expected = NullArgumentException.class)
    public void testEnsureDataConformanceNullArgumentException() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        mannWhitneyUTest.ensureDataConformance(null, new double[0]);
    }

    /**
     * Test method for {@link MannWhitneyUTest#ensureDataConformance(double[], double[])}.
     */
    @Test(expected = NoDataException.class)
    public void testEnsureDataConformanceNoDataException() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        mannWhitneyUTest.ensureDataConformance(new double[0], new double[0]);
    }

    /**
     * Test method for {@link MannWhitneyUTest#concatenateSamples(double[], double[])}.
     */
    @Test
    public void testConcatenateSamples() {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0, 6.0};
        double[] z = MannWhitneyUTest.this.concatenateSamples(x, y);
        assertArrayEquals(new double[]{1.0, 2.0, 3.0, 4.0, 5.0, 6.0}, z);
    }

    /**
     * Test method for {@link MannWhitneyUTest#mannWhitneyU(double[], double[])}.
     */
    @Test
    public void testMannWhitneyU() throws NoDataException, NullArgumentException {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();

        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0, 6.0};
        double result = mannWhitneyUTest.mannWhitneyU(x, y);
        assertEquals(3.0, result, 0.001);

        x = new double[]{1.0, 1.0, 1.0, 1.0};
        y = new double[]{2.0, 2.0, 2.0, 2.0};
        result = mannWhitneyUTest.mannWhitneyU(x, y);
        assertEquals(6.0, result, 0.001);
    }

    /**
     * Test method for {@link MannWhitneyUTest#mannWhitneyUTest(double[], double[])}.
     */
    @Test
    public void testMannWhitneyUTest() throws NoDataException,
            NullArgumentException, ConvergenceException,
            MaxCountExceededException {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();

        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0, 6.0};
        double pValue = mannWhitneyUTest.mannWhitneyUTest(x, y);
        assertEquals(0.16666666666666663, pValue, 0.0000001);

        x = new double[]{1.0, 1.0, 1.0, 1.0};
        y = new double[]{2.0, 2.0, 2.0, 2.0};
        pValue = mannWhitneyUTest.mannWhitneyUTest(x, y);
        assertEquals(0.0, pValue, 0.0000001);
    }

}