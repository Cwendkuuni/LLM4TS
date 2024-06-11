package org.apache.commons.math3.stat.inference;

import static org.junit.Assert.assertEquals;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.stat.ranking.NaNStrategy;
import org.apache.commons.math3.stat.ranking.NaturalRanking;
import org.apache.commons.math3.stat.ranking.TiesStrategy;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;

/**
 * Test class for MannWhitneyUTest.
 */
public class MannWhitneyUTestTest {

    /**
     * Tests the constructor.
     */
    @Test
    public void testConstructor() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        assertEquals(NaturalRanking.DEFAULT_STRATEGY, mannWhitneyUTest.getNaturalRanking().getNaNStrategy());
        assertEquals(TiesStrategy.AVERAGE, mannWhitneyUTest.getNaturalRanking().getTiesStrategy());
    }

    /**
     * Tests the constructor with custom strategies for NaN's and ties.
     */
    @Test
    public void testConstructorWithCustomStrategies() {
        NaNStrategy nanStrategy = new NaNStrategy() {
            @Override
            public double rankNaN(double value, int length) {
                // Custom strategy for NaN
                return -1;
            }
        };

        TiesStrategy tiesStrategy = new TiesStrategy() {
            @Override
            public int rankTies(int length, int position, double value) {
                // Custom strategy for ties
                return position;
            }
        };

        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest(nanStrategy, tiesStrategy);
        assertEquals(nanStrategy, mannWhitneyUTest.getNaturalRanking().getNaNStrategy());
        assertEquals(tiesStrategy, mannWhitneyUTest.getNaturalRanking().getTiesStrategy());
    }

    /**
     * Tests the method mannWhitneyU().
     */
    @Test
    public void testMannWhitneyU() throws ConvergenceException, MaxCountExceededException, NoDataException, NullArgumentException {
        double[] x = { 1.5, 2.0, 2.5, 3.0 };
        double[] y = { 2.0, 2.5, 3.5, 4.0 };

        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double mannWhitneyU = mannWhitneyUTest.mannWhitneyU(x, y);
        assertEquals(15.0, mannWhitneyU, 1e-10);
    }

    /**
     * Tests the method calculateAsymptoticPValue().
     */
    @Test
    public void testCalculateAsymptoticPValue() throws ConvergenceException, MaxCountExceededException {
        double Umin = 7.0;
        int n1 = 5;
        int n2 = 7;

        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double pValue = mannWhitneyUTest.calculateAsymptoticPValue(Umin, n1, n2);
        assertEquals(0.05, pValue, 1e-6);
    }

    /**
     * Tests the method mannWhitneyUTest().
     */
    @Test
    public void testMannWhitneyUTest() throws ConvergenceException, MaxCountExceededException, NoDataException, NullArgumentException {
        double[] x = { 1.5, 2.0, 2.5, 3.0 };
        double[] y = { 2.0, 2.5, 3.5, 4.0, 4.5, 5.0 };

        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double pValue = mannWhitneyUTest.mannWhitneyUTest(x, y);
        assertEquals(0.05, pValue, 1e-6);
    }
}