package org.apache.commons.math3.stat.inference.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.stat.inference.MannWhitneyUTest;
import org.apache.commons.math3.stat.ranking.NaturalRanking;
import org.apache.commons.math3.stat.ranking.Ranking;
import org.apache.commons.math3.stat.ranking.TiesStrategy;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.datatype.NumberUtils;
import org.junit.Test;

/**
 * Test {@link MannWhitneyUTest} class.
 */
public class MannWhitneyUTestTest {

    /**
     * Test method for {@link MannWhitneyUTest#mannWhitneyU(double[], double[])}.
     */
    @Test
    public void testMannWhitneyU() {
        // Test with default settings
        double[] x = {1.0, 1.5, 2.0, 2.5};
        double[] y = {1.1, 1.6, 2.1, 2.6};
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double mannWhitneyU = mannWhitneyUTest.mannWhitneyU(x, y);
        assertEquals(12.0, mannWhitneyU, Precision.SAFE_EPSILON);

        // Test with custom settings
        MannWhitneyUTest mannWhitneyUTestCustom = new MannWhitneyUTest(
                NaturalRanking.MEDIAN_TIED, TiesStrategy.MIN);
        double mannWhitneyUCustom = mannWhitneyUTestCustom.mannWhitneyU(x, y);
        assertEquals(11.0, mannWhitneyUCustom, Precision.SAFE_EPSILON);
    }

    /**
     * Test method for {@link MannWhitneyUTest#mannWhitneyUTest(double[], double[])}.
     */
    @Test
    public void testMannWhitneyUTest() {
        // Test with default settings
        double[] x = {1.0, 1.5, 2.0, 2.5};
        double[] y = {1.1, 1.6, 2.1, 2.6};
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double pValue = mannWhitneyUTest.mannWhitneyUTest(x, y);
        assertEquals(0.34375, pValue, 0.001);

        // Test with custom settings
        double[] x2 = {1.0, 1.5, 2.0, 2.5};
        double[] y2 = {1.1, 1.6, 1.6, 2.6};
        MannWhitneyUTest mannWhitneyUTestCustom = new MannWhitneyUTest(
                NaturalRanking.MEDIAN_TIED, TiesStrategy.MIN);
        double pValueCustom = mannWhitneyUTestCustom.mannWhitneyUTest(x2, y2);
        assertEquals(0.25, pValueCustom, 0.001);

        // Test with empty arrays
        MannWhitneyUTest mannWhitneyUTestEmpty = new MannWhitneyUTest();
        try {
            mannWhitneyUTestEmpty.mannWhitneyUTest(new double[0], new double[0]);
            fail();
        } catch (Exception e) {
            // expected
        }
    }

    /**
     * Test method for {@link MannWhitneyUTest#ensureDataConformance(double[], double[])}.
     */
    @Test(expected = NoDataException.class)
    public void testEnsureDataConformanceWithNullArrays() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        mannWhitneyUTest.ensureDataConformance(null, null);
    }

    /**
     * Test method for {@link MannWhitneyUTest#ensureDataConformance(double[], double[])}.
     */
    @Test(expected = NoDataException.class)
    public void testEnsureDataConformanceWithEmptyArrays() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        mannWhitneyUTest.ensureDataConformance(new double[0], new double[0]);
    }

    /**
     * Test method for {@link MannWhitneyUTest#ensureDataConformance(double[], double[])}.
     */
    @Test
    public void testEnsureDataConformance() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        mannWhitneyUTest.ensureDataConformance(new double[] {1.0, 1.5, 2.0, 2.5},
                new double[] {1.1, 1.6, 2.1, 2.6});
    }

    /**
     * Test method for {@link MannWhitneyUTest#concatenateSamples(double[], double[])}.
     */
    @Test
    public void testConcatenateSamples() {
        double[] x = {1.0, 1.5, 2.0, 2.5};
        double[] y = {1.1, 1.6, 2.1, 2.6};
        double[] z = MannWhitneyUTest.concatenateSamples(x, y);
        assertEquals(8, z.length);
        for (int i = 0; i < 8; i++) {
            assertEquals(i < 4 ? x[i] : y[i - 4], z[i], 0.0);
        }
    }

    /**
     * Test method for {@link MannWhitneyUTest#calculateAsymptoticPValue(double, int, int)}.
     */
    @Test
    public void testCalculateAsymptoticPValue() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double Umin = 6.0;
        int n1 = 4;
        int n2 = 4;
        double pValue = mannWhitneyUTest.calculateAsymptoticPValue(Umin, n1, n2);
        assertEquals(0.5, pValue, 0.001);

        Umin = 12.0;
        pValue = mannWhitneyUTest.calculateAsymptoticPValue(Umin, n1, n2);
        assertEquals(0.0, pValue, 0.001);
    }

    /**
     * Test method for {@link MannWhitneyUTest#calculateAsymptoticPValue(double, int, int)}.
     */
    @Test(expected = ConvergenceException.class)
    public void testCalculateAsymptoticPValueWithConvergenceError() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double Umin = 0.5;
        int n1 = 1;
        int n2 = 1;
        mannWhitneyUTest.calculateAsymptoticPValue(Umin, n1, n2);
    }
}