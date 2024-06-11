package org.apache.commons.math3.stat.inference.test;

import static org.junit.Assert.assertEquals;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.stat.inference.MannWhitneyUTest;
import org.apache.commons.math3.stat.ranking.NaturalRanking;
import org.junit.Test;

/**
 * Test class for {@link MannWhitneyUTest}.
 */
public class MannWhitneyUTestTest {

    /**
     * Test method for {@link MannWhitneyUTest#MannWhitneyUTest()}.
     */
    @Test
    public void testMannWhitneyUTestDefaultConstructor() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        // no assertions
    }

    /**
     * Test method for {@link MannWhitneyUTest#MannWhitneyUTest(NaNStrategy, TiesStrategy)}.
     */
    @Test
    public void testMannWhitneyUTestCustomConstructor() {
        MannWhitneyUTest test = new MannWhitneyUTest(
                NaNStrategy.REPLACE, TiesStrategy.MIN);
        // no assertions
    }

    /**
     * Test method for {@link MannWhitneyUTest#ensureDataConformance(double[], double[])}.
     */
    @Test(expected = NullArgumentException.class)
    public void testEnsureDataConformanceNullArrays() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        test.ensureDataConformance(null, null);
    }

    @Test(expected = NullArgumentException.class)
    public void testEnsureDataConformanceNullFirstArray() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = null;
        test.ensureDataConformance(x, new double[0]);
    }

    @Test(expected = NullArgumentException.class)
    public void testEnsureDataConformanceNullSecondArray() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] y = null;
        test.ensureDataConformance(new double[0], y);
    }

    @Test(expected = NoDataException.class)
    public void testEnsureDataConformanceEmptyArrays() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        test.ensureDataConformance(new double[0], new double[0]);
    }

    /**
     * Test method for {@link MannWhitneyUTest#concatenateSamples(double[], double[])}.
     */
    @Test
    public void testConcatenateSamples() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = new double[] {1.0, 2.0, 3.0};
        double[] y = new double[] {4.0, 5.0};
        double[] z = test.concatenateSamples(x, y);
        assertEquals(6, z.length);
        for (int i = 0; i < x.length; ++i) {
            assertEquals(i + 1, z[i]);
            assertEquals(i + 1 + x.length, z[i + x.length]);
        }
    }

    /**
     * Test method for {@link MannWhitneyUTest#mannWhitneyU(double[], double[])}.
     */
    @Test
    public void testMannWhitneyU() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = new double[] {1.0, 2.0, 3.0};
        double[] y = new double[] {4.0, 5.0, 5.0};
        double U = test.mannWhitneyU(x, y);
        assertEquals(7.0, U, 1e-12);
    }

    /**
     * Test method for {@link MannWhitneyUTest#calculateAsymptoticPValue(double, int, int)}.
     */
    @Test
    public void testCalculateAsymptoticPValue() throws ConvergenceException,
        MaxCountExceededException {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double Umin = 0.0;
        double Umax = 7.0;
        int n1 = 3;
        int n2 = 3;
        double pValue = test.calculateAsymptoticPValue(Umin, n1, n2);
        assertEquals(0.1264, pValue, 1e-12);
    }

    /**
     * Test method for {@link MannWhitneyUTest#mannWhitneyUTest(double[], double[])}.
     */
    @Test
    public void testMannWhitneyUTest() throws ConvergenceException,
        MaxCountExceededException, NoDataException, NullArgumentException {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = new double[] {1.0, 2.0, 3.0};
        double[] y = new double[] {4.0, 5.0, 5.0};
        double pValue = test.mannWhitneyUTest(x, y);
        assertEquals(0.1264, pValue, 1e-12);
    }

}