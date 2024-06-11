package org.apache.commons.math3.stat.inference;

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
import org.junit.Test;

/**
 * Tests {@link MannWhitneyUTest}.
 */
public class MannWhitneyUTestTest {

    /**
     * Test method for {@link MannWhitneyUTest# MannWhitneyUTest()}.
     */
    @Test
    public void testDefaultConstructor() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        assertNotNull(test);
    }

    /**
     * Test method for {@link MannWhitneyUTest# MannWhitneyUTest(NaNStrategy, TiesStrategy)}.
     */
    @Test
    public void testCustomConstructor() {
        MannWhitneyUTest test = new MannWhitneyUTest(NaNStrategy.LOWER_MEAN,
                TiesStrategy.LOWER_TIE);
        assertNotNull(test);
    }

    /**
     * Test method for {@link MannWhitneyUTest# ensureDataConformance(double[], double[])}.
     */
    @Test(expected = NullArgumentException.class)
    public void testEnsureDataConformanceNullArgumentException() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        test.ensureDataConformance(null, new double[0]);
    }

    @Test(expected = NoDataException.class)
    public void testEnsureDataConformanceNoDataException() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        test.ensureDataConformance(new double[0], new double[0]);
    }

    /**
     * Test method for {@link MannWhitneyUTest# mannWhitneyU(double[], double[])}.
     */
    @Test
    public void testMannWhitneyU() throws NoDataException, NullArgumentException {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {2.0, 3.0, 1.0};
        double result = test.mannWhitneyU(x, y);
        assertEquals(6.0, result, 1e-12);
    }

    /**
     * Test method for {@link MannWhitneyUTest# mannWhitneyUTest(double[], double[])}.
     */
    @Test
    public void testMannWhitneyUTest() throws NoDataException,
            NullArgumentException, ConvergenceException,
            MaxCountExceededException {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {2.0, 3.0, 1.0};
        double pValue = test.mannWhitneyUTest(x, y);
        assertEquals(0.125, pValue, 1e-6);
    }

    /**
     * Test method for {@link MannWhitneyUTest# mannWhitneyUTest(double[], double[])}.
     */
    @Test
    public void testMannWhitneyUTestTwoZeroLength()
            throws NoDataException, NullArgumentException,
            ConvergenceException, MaxCountExceededException {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = new double[0];
        double[] y = new double[0];
        double pValue = test.mannWhitneyUTest(x, y);
        assertEquals(1.0, pValue, 1e-6);
    }

    /**
     * Test method for {@link MannWhitneyUTest# mannWhitneyUTest(double[], double[])}.
     */
    @Test
    public void testMannWhitneyUTestOneSampleZeroLength()
            throws NoDataException, NullArgumentException,
            ConvergenceException, MaxCountExceededException {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = new double[0];
        double[] y = {1.0};
        double pValue = test.mannWhitneyUTest(x, y);
        assertEquals(1.0, pValue, 1e-6);
    }

    /**
     * Test method for {@link MannWhitneyUTest# mannWhitneyUTest(double[], double[])}.
     */
    @Test
    public void testMannWhitneyUTestOneSample()
            throws NoDataException, NullArgumentException,
            ConvergenceException, MaxCountExceededException {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1.0};
        double[] y = {2.0, 3.0};
        double pValue = test.mannWhitneyUTest(x, y);
        assertEquals(1.0, pValue, 1e-6);
    }

    /**
     * Test method for {@link MannWhitneyUTest# mannWhitneyUTest(double[], double[])}.
     */
    @Test
    public void testMannWhitneyUTestEqualSamples()
            throws NoDataException, NullArgumentException,
            ConvergenceException, MaxCountExceededException {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1.0, 1.0, 1.0};
        double[] y = {1.0, 1.0, 1.0};
        double pValue = test.mannWhitneyUTest(x, y);
        assertEquals(1.0, pValue, 1e-6);
    }

    /**
     * Test method for {@link MannWhitneyUTest# mannWhitneyUTest(double[], double[])}.
     */
    @Test
    public void testMannWhitneyUTestDifferentSamples()
            throws NoDataException, NullArgumentException,
            ConvergenceException, MaxCountExceededException {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0, 6.0};
        double pValue = test.mannWhitneyUTest(x, y);
        assertEquals(0.042, pValue, 1e-6);
    }

    /**
     * Test method for {@link MannWhitneyUTest# mannWhitneyUTest(double[], double[])}.
     */
    @Test
    public void testMannWhitneyUTestLargeSamples()
            throws NoDataException, NullArgumentException,
            ConvergenceException, MaxCountExceededException {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = new double[10000];
        double[] y = new double[10000];
        for (int i = 0; i < x.length; ++i) {
            x[i] = Math.random();
            y[i] = Math.random();
        }
        double pValue = test.mannWhitneyUTest(x, y);
        assertEquals(1.0 / (x.length + y.length), pValue, 1e-6);
    }

}