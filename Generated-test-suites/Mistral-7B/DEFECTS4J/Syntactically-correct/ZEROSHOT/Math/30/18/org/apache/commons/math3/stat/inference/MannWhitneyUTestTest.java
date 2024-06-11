package org.apache.commons.math3.stat.inference.test;

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
 * Test class for MannWhitneyUTest.
 */
public class MannWhitneyUTestTest {

    /**
     * Test method for {@link MannWhitneyUTest#MannWhitneyUTest()}.
     */
    @Test
    public void testMannWhitneyUTestConstructor() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        assertEquals(NaturalRanking.FIXED, test.getNanStrategy());
        assertEquals(TiesStrategy.AVERAGE, test.getTiesStrategy());
    }

    /**
     * Test method for {@link MannWhitneyUTest#MannWhitneyUTest(NaNStrategy, TiesStrategy)}.
     */
    @Test
    public void testMannWhitneyUTestConstructor2() {
        MannWhitneyUTest test = new MannWhitneyUTest(NaNStrategy.EXCLUSIVE,
                TiesStrategy.MIN);
        assertEquals(NaNStrategy.EXCLUSIVE, test.getNanStrategy());
        assertEquals(TiesStrategy.MIN, test.getTiesStrategy());
    }

    /**
     * Test method for {@link MannWhitneyUTest#ensureDataConformance(double[], double[])}.
     */
    @Test(expected = NullArgumentException.class)
    public void testEnsureDataConformanceNullArgumentException() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        test.ensureDataConformance(null, new double[0]);
    }

    /**
     * Test method for {@link MannWhitneyUTest#ensureDataConformance(double[], double[])}.
     */
    @Test(expected = NoDataException.class)
    public void testEnsureDataConformanceNoDataException() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        test.ensureDataConformance(new double[0], new double[0]);
    }

    /**
     * Test method for {@link MannWhitneyUTest#concatenateSamples(double[], double[])}.
     */
    @Test
    public void testConcatenateSamples() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        final double[] x = {1, 2, 3};
        final double[] y = {4, 5, 6};
        final double[] z = test.concatenateSamples(x, y);
        assertEquals(6, z.length);
        for (int i = 0; i < x.length; ++i) {
            assertEquals(i + x.length, z[i + x.length]);
            assertEquals(i + x.length, z[i]);
        }
        for (int i = 0; i < y.length; ++i) {
            assertEquals(x.length + i, z[x.length + i]);
            assertEquals(x.length + i, z[x.length + y.length - i]);
        }
    }

    /**
     * Test method for {@link MannWhitneyUTest#mannWhitneyU(double[], double[])}.
     */
    @Test
    public void testMannWhitneyU() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        final double[] x = {1, 2, 3};
        final double[] y = {4, 5, 6};
        final double U = test.mannWhitneyU(x, y);
        assertEquals(13.0, U, 0.0);
    }

    /**
     * Test method for {@link MannWhitneyUTest#mannWhitneyUTest(double[], double[])}.
     */
    @Test
    public void testMannWhitneyUTest()
        throws NullArgumentException, NoDataException,
        ConvergenceException, MaxCountExceededException {
        MannWhitneyUTest test = new MannWhitneyUTest();
        final double[] x = {1, 2, 3};
        final double[] y = {4, 5, 6};
        final double pValue = test.mannWhitneyUTest(x, y);
        assertEquals(0.054, pValue, 0.001);
    }

}