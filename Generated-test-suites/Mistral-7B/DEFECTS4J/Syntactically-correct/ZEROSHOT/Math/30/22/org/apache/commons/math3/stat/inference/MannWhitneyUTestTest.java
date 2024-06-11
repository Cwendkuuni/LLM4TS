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
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * JUnit test file for MannWhitneyUTest class.
 */
public class MannWhitneyUTestTest {

    /**
     * Test constructor MannWhitneyUTest().
     */
    @Test
    public void testMannWhitneyUTestConstruction() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        assertEquals(NaturalRanking.DEFAULT_STRATEGY, mannWhitneyUTest.getNaturalRanking().getNaNStrategy());
        assertEquals(TiesStrategy.AVERAGE, mannWhitneyUTest.getNaturalRanking().getTiesStrategy());
    }

    /**
     * Test constructor MannWhitneyUTest(NaNStrategy nanStrategy, TiesStrategy tiesStrategy).
     */
    @Test
    public void testMannWhitneyUTestConstructionWithStrategies() {
        NaNStrategy nanStrategy = new NaNStrategy() {
            @Override
            public double rank(double d) {
                if (Double.isNaN(d)) {
                    return Double.NaN;
                }
                return Double.POSITIVE_INFINITY;
            }
        };
        TiesStrategy tiesStrategy = new TiesStrategy() {
            @Override
            public int tie(int r1, int r2) {
                return (r1 > r2) ? r2 : r1;
            }
        };
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest(nanStrategy, tiesStrategy);
        assertEquals(nanStrategy, mannWhitneyUTest.getNaturalRanking().getNaNStrategy());
        assertEquals(tiesStrategy, mannWhitneyUTest.getNaturalRanking().getTiesStrategy());
    }

    /**
     * Test method mannWhitneyU().
     */
    @Test
    public void testMannWhitneyU() {
        double[] x = {1.0, 2.5, 3.5};
        double[] y = {2.0, 3.0, 4.0, 5.0};
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double mannWhitneyU = mannWhitneyUTest.mannWhitneyU(x, y);
        assertEquals(12.0, mannWhitneyU, 0.0);
    }

    /**
     * Test method mannWhitneyUTest().
     */
    @Test
    public void testMannWhitneyUTest() throws NoDataException, ConvergenceException, MaxCountExceededException {
        double[] x = {1.0, 2.5, 3.5};
        double[] y = {2.0, 3.0, 4.0, 5.0};
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double pValue = mannWhitneyUTest.mannWhitneyUTest(x, y);
        assertEquals(0.0566527365428788, pValue, 0.00001);
    }

}