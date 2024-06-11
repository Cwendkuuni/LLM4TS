package org.apache.commons.math3.stat.inference.test;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.stat.inference.MannWhitneyUTest;
import org.apache.commons.math3.stat.ranking.NaturalRanking;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test for {@link MannWhitneyUTest}.
 */
public class MannWhitneyUTestTest {

    /**
     * Test method for {@link MannWhitneyUTest#MannWhitneyUTest()}.
     */
    @Test
    public void testDefaultConstructor() {
        MannWhitneyUTest mwt = new MannWhitneyUTest();
        assertNotNull(mwt);
        assertEquals(NaturalRanking.FIXED, mwt.getNanStrategy());
        assertEquals(TiesStrategy.AVERAGE, mwt.getTiesStrategy());
    }

    /**
     * Test method for {@link MannWhitneyUTest#MannWhitneyUTest(NaNStrategy, TiesStrategy)}.
     */
    @Test
    public void testCustomConstructor() {
        MannWhitneyUTest mwt = new MannWhitneyUTest(NaNStrategy.MISSING, TiesStrategy.DUPLICATE);
        assertNotNull(mwt);
        assertNotEquals(NaturalRanking.FIXED, mwt.getNanStrategy());
        assertNotEquals(TiesStrategy.AVERAGE, mwt.getTiesStrategy());
    }

    /**
     * Test method for {@link MannWhitneyUTest#ensureDataConformance(double[], double[])}.
     */
    @Test(expected = NullArgumentException.class)
    public void testEnsureDataConformanceNullArgument() {
        MannWhitneyUTest mwt = new MannWhitneyUTest();
        mwt.ensureDataConformance(null, new double[0]);
    }

    @Test(expected = NoDataException.class)
    public void testEnsureDataConformanceNoData() {
        MannWhitneyUTest mwt = new MannWhitneyUTest();
        mwt.ensureDataConformance(new double[0], new double[0]);
    }

    /**
     * Test method for {@link MannWhitneyUTest#mannWhitneyU(double[], double[])}.
     */
    @Test
    public void testMannWhitneyU() {
        MannWhitneyUTest mwt = new MannWhitneyUTest();

        double[] x = {1.5, 2.5, 3.5, 4.5};
        double[] y = {2.0, 3.0, 3.5, 4.0};
        double u = mwt.mannWhitneyU(x, y);
        assertEquals(12.0, u, 0.001);

        x = new double[]{Double.NaN, 2.5, 3.5, 4.5};
        y = new double[]{2.0, 3.0, 3.5, 4.0};
        u = mwt.mannWhitneyU(x, y);
        assertEquals(11.0, u, 0.001);

        x = new double[]{1.5, 2.5, 3.5, 4.5};
        y = new double[]{2.0, 3.0, 3.5, Double.NaN};
        u = mwt.mannWhitneyU(x, y);
        assertEquals(11.0, u, 0.001);

        x = new double[]{1.5, 2.5, 3.5, 4.5};
        y = new double[]{2.0, 3.0, 3.5, 3.5};
        u = mwt.mannWhitneyU(x, y);
        assertEquals(12.5, u, 0.001);

        x = new double[]{1.5, 2.5, 3.5, 4.5};
        y = new double[]{2.0, 3.0, 3.5, 3.5};
        MannWhitneyUTest mwt2 = new MannWhitneyUTest(NaNStrategy.EXCLUDE, TiesStrategy.MIN);
        u = mwt2.mannWhitneyU(x, y);
        assertEquals(12.0, u, 0.001);
    }

    /**
     * Test method for {@link MannWhitneyUTest#mannWhitneyUTest(double[], double[])}.
     */
    @Test
    public void testMannWhitneyUTest() throws ConvergenceException, MaxCountExceededException, NoDataException, NullArgumentException {
        MannWhitneyUTest mwt = new MannWhitneyUTest();

        double[] x = {1.5, 2.5, 3.5, 4.5};
        double[] y = {2.0, 3.0, 3.5, 4.0};
        double pvalue = mwt.mannWhitneyUTest(x, y);
        assertEquals(0.05, pvalue, 0.001);

        x = new double[]{Double.NaN, 2.5, 3.5, 4.5};
        y = new double[]{2.0, 3.0, 3.5, 4.0};
        pvalue = mwt.mannWhitneyUTest(x, y);
        assertEquals(0.045, pvalue, 0.001);

        x = new double[]{1.5, 2.5, 3.5, 4.5};
        y = new double[]{2.0, 3.0, 3.5, Double.NaN};
        pvalue = mwt.mannWhitneyUTest(x, y);
        assertEquals(0.045, pvalue, 0.001);

        x = new double[]{1.5, 2.5, 3.5, 4.5};
        y = new double[]{2.0, 3.0, 3.5, 3.5};
        MannWhitneyUTest mwt2 = new MannWhitneyUTest(NaNStrategy.EXCLUDE, TiesStrategy.MIN);
        pvalue = mwt2.mannWhitneyUTest(x, y);
        assertEquals(0.05, pvalue, 0.001);
    }

}