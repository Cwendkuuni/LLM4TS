package org.apache.commons.math3.stat.inference.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.stat.inference.MannWhitneyUTest;
import org.apache.commons.math3.stat.ranking.NaNStrategy;
import org.apache.commons.math3.stat.ranking.NaturalRanking;
import org.apache.commons.math3.stat.ranking.TiesStrategy;
import org.junit.Test;

/**
 * Test cases for {@link MannWhitneyUTest}.
 */
public class MannWhitneyUTestTest {

    /**
     * Tests the constructor with default strategies.
     */
    @Test
    public void testDefaultConstructor() {
        final MannWhitneyUTest test = new MannWhitneyUTest();
        final NaturalRanking naturalRanking = test.getRanking();
        assertEquals(NaturalRanking.class, naturalRanking.getClass());
        assertEquals(NaNStrategy.FIXED, naturalRanking.getNaNStrategy());
        assertEquals(TiesStrategy.AVERAGE, naturalRanking.getTiesStrategy());
    }

    /**
     * Tests the constructor with custom strategies.
     */
    @Test
    public void testCustomConstructor() {
        final NaNStrategy nanStrategy = new NaNStrategy() {
            @Override
            public int rank(double d, double[] ps) {
                return ps.length;
            }
        };

        final TiesStrategy tiesStrategy = new TiesStrategy() {
            @Override
            public int rankTie(int i, int j, int maxRank) {
                return maxRank;
            }
        };

        final MannWhitneyUTest test = new MannWhitneyUTest(nanStrategy, tiesStrategy);
        final NaturalRanking naturalRanking = test.getRanking();
        assertEquals(nanStrategy, naturalRanking.getNaNStrategy());
        assertEquals(tiesStrategy, naturalRanking.getTiesStrategy());
    }

    /**
     * Test null arrays.
     */
    @Test(expected = NullPointerException.class)
    public void testNullArrays() {
        final MannWhitneyUTest test = new MannWhitneyUTest();
        test.mannWhitneyU(null, null);
    }

    /**
     * Test empty arrays.
     */
    @Test
     public void testEmptyArrays() {
        final MannWhitneyUTest test = new MannWhitneyUTest();
        assertThrows(NoDataException.class, () -> test.mannWhitneyU(new double[0], new double[0]));
    }

    /**
     * Test valid data.
     */
    @Test
    public void testValidData() {
        final MannWhitneyUTest test = new MannWhitneyUTest();
        final double[] x = {1.0, 1.5, 2.0, 2.5};
        final double[] y = {2.5, 3.0, 3.5};

        final double u = test.mannWhitneyU(x, y);
        final double pValue = test.mannWhitneyUTest(x, y);
        assertTrue(Double.isFinite(u));
        assertTrue(Double.isFinite(pValue));
        assertEquals(0.05, pValue, 0.01);
    }

    /**
     * Test data with same values.
     */
    @Test
    public void testSameValues() {
        final MannWhitneyUTest test = new MannWhitneyUTest();
        final double[] x = {1.0, 1.0, 1.0, 1.0};
        final double[] y = {1.0, 1.0, 1.0, 1.0};

        // Mann-Whitney U test should fail due to NoDataException
        assertThrows(NoDataException.class, () -> test.mannWhitneyUTest(x, y));
    }

    /**
     * Test data with NaN values.
     */
    @Test
    public void testNaNValues() {
        final MannWhitneyUTest test = new MannWhitneyUTest();
        final double[] x = {1.0, 1.0, Double.NaN, Double.NaN};
        final double[] y = {1.0, 1.0, 1.0, 1.0};

        // Mann-Whitney U test should not fail due to NaN values
        test.mannWhitneyU(x, y);

        // Mann-Whitney U test should not return NaN result
        assertFalse(Double.isNaN(test.mannWhitneyUTest(x, y)));
    }

    /**
     * Test data with max count exceeded exception.
     */
    @Test(expected = MaxCountExceededException.class)
    public void testMaxCountExceeded() {
        final MannWhitneyUTest test = new MannWhitneyUTest();
        final double[] x = new double[Integer.MAX_VALUE]; // 2^31
        final double[] y = new double[Integer.MAX_VALUE]; // 2^31

        test.mannWhitneyU(x, y);
    }

    /**
     * Test data with convergence exception.
     */
    @Test(expected = ConvergenceException.class)
    public void testConvergenceException() {
        final MannWhitneyUTest test = new MannWhitneyUTest() {
            @Override
            public double calculateAsymptoticPValue(double Umin, int n1, int n2)
                    throws ConvergenceException, MaxCountExceededException {
                throw new ConvergenceException();
            }
        };

        final double[] x = {1.0, 1.5, 2.0, 2.5};
        final double[] y = {2.5, 3.0, 3.5};

        test.mannWhitneyUTest(x, y);
    }
}