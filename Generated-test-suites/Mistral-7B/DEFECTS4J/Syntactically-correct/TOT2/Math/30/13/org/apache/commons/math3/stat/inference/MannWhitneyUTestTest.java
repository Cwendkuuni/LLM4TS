package org.apache.commons.math3.stat.inference;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.stat.ranking.NaNStrategy;
import org.apache.commons.math3.stat.ranking.NaturalRanking;
import org.apache.commons.math3.stat.ranking.TiesStrategy;
import org.apache.commons.math3.util.FastMath;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for {@link MannWhitneyUTest} class.
 */
public class MannWhitneyUTestTest {

    /**
     * Test method for {@link MannWhitneyUTest# MannWhitneyUTest()} constructor.
     */
    @Test
    public void testMannWhitneyUTestConstructor() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        Assert.assertNotNull(test);
    }

    /**
     * Test method for {@link MannWhitneyUTest# MannWhitneyUTest(NaNStrategy, TiesStrategy)} constructor.
     */
    @Test
    public void testMannWhitneyUTestConstructorWithStrategies() {
        MannWhitneyUTest test = new MannWhitneyUTest(NaNStrategy.LOWEST, TiesStrategy.ORDERED);
        Assert.assertNotNull(test);
    }

    /**
     * Test method for {@link MannWhitneyUTest# ensureDataConformance(double[], double[])} method.
     */
    @Test(expected = NullArgumentException.class)
    public void testEnsureDataConformanceWithNullArguments() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        test.ensureDataConformance(null, new double[0]);
    }

    /**
     * Test method for {@link MannWhitneyUTest# ensureDataConformance(double[], double[])} method.
     */
    @Test(expected = NoDataException.class)
    public void testEnsureDataConformanceWithEmptyData() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = new double[0];
        double[] y = new double[0];
        test.ensureDataConformance(x, y);
    }

    /**
     * Test method for {@link MannWhitneyUTest# concatenateSamples(double[], double[])} method.
     */
    @Test
    public void testConcatenateSamples() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0, 6.0};
        double[] z = test.concatenateSamples(x, y);
        Assert.assertArrayEquals(new double[]{1.0, 2.0, 3.0, 4.0, 5.0, 6.0}, z);
    }

    /**
     * Test method for {@link MannWhitneyUTest# mannWhitneyU(double[], double[])} method.
     */
    @Test
    public void testMannWhitneyU() throws NoDataException, NullArgumentException {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0, 6.0};
        double U = test.mannWhitneyU(x, y);
        Assert.assertEquals(15.0, U, 0.001);
    }

    /**
     * Test method for {@link MannWhitneyUTest# mannWhitneyUTest(double[], double[])} method.
     */
    @Test
    public void testMannWhitneyUTest()
        throws NoDataException, NullArgumentException,
        ConvergenceException, MaxCountExceededException {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1.5, 2.5, 3.5};
        double[] y = {2.0, 2.0, 4.0};
        double pValue = test.mannWhitneyUTest(x, y);
        Assert.assertEquals(0.453594632281744, pValue, 0.001);
    }
}