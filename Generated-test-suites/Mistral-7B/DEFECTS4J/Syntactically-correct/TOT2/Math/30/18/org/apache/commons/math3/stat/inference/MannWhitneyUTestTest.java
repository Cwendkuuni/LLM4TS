import org.apache.commons.math3.stat.inference.MannWhitneyUTest;
import org.apache.commons.math3.stat.ranking.NaturalRanking;
import org.apache.commons.math3.stat.ranking.TiesStrategy;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test cases for {@link MannWhitneyUTest} class.
 */
public class MannWhitneyUTestTest {

    /**
     * Test method for {@link MannWhitneyUTest#MannWhitneyUTest()}.
     */
    @Test
    public void testDefaultConstructor() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        assertEquals(NaturalRanking.DEFAULT_STRATEGY, test.getNaturalRanking().getNaNStrategy());
        assertEquals(TiesStrategy.AVERAGE, test.getNaturalRanking().getTiesStrategy());
    }

    /**
     * Test method for {@link MannWhitneyUTest#MannWhitneyUTest(org.apache.commons.math3.stat.ranking.NaNStrategy, org.apache.commons.math3.stat.ranking.TiesStrategy)}.
     */
    @Test
    public void testCustomConstructor() {
        NaNStrategy nanStrategy = new NaNStrategy() {
            @Override
            public double rank(double d) {
                if (Double.isNaN(d)) {
                    return Double.POSITIVE_INFINITY;
                }
                return 0.0;
            }
        };

        TiesStrategy tiesStrategy = new TiesStrategy() {
            @Override
            public int tieValue(int rank, double value) {
                return rank;
            }
        };

        MannWhitneyUTest test = new MannWhitneyUTest(nanStrategy, tiesStrategy);
        assertEquals(nanStrategy, test.getNaturalRanking().getNaNStrategy());
        assertEquals(tiesStrategy, test.getNaturalRanking().getTiesStrategy());
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
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0, 6.0};
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] expected = {1.0, 2.0, 3.0, 4.0, 5.0, 6.0};
        double[] actual = test.concatenateSamples(x, y);
        Assert.assertArrayEquals(expected, actual);
    }

    /**
     * Test method for {@link MannWhitneyUTest#mannWhitneyU(double[], double[])}.
     */
    @Test
    public void testMannWhitneyU() {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0, 6.0};
        MannWhitneyUTest test = new MannWhitneyUTest();
        double expected = 10.0;
        double actual = test.mannWhitneyU(x, y);
        assertEquals(expected, actual, 0.001);
    }

    /**
     * Test method for {@link MannWhitneyUTest#mannWhitneyUTest(double[], double[])}.
     */
    @Test
    public void testMannWhitneyUTest() throws NoDataException, ConvergenceException, MaxCountExceededException {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0, 6.0};
        MannWhitneyUTest test = new MannWhitneyUTest();
        double expected = 0.1;
        double actual = test.mannWhitneyUTest(x, y);
        assertEquals(expected, actual, 0.001);
    }

    /**
     * Test method for edge case: {@link MannWhitneyUTest#mannWhitneyUTest(double[], double[])}.
     * The samples have the same value.
     */
    @Test
    public void testMannWhitneyUTestSameValues() throws NoDataException, ConvergenceException, MaxCountExceededException {
        double[] x = {1.0, 1.0};
        double[] y = {1.0, 1.0};
        MannWhitneyUTest test = new MannWhitneyUTest();
        double expected = 1.0;
        double actual = test.mannWhitneyUTest(x, y);
        assertEquals(expected, actual, 0.001);
    }

    /**
     * Test method for edge case: {@link MannWhitneyUTest#mannWhitneyU(double[], double[])}.
     * The samples are empty.
     */
    @Test(expected = NoDataException.class)
    public void testMannWhitneyUEmptySamples() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        test.mannWhitneyU(new double[0], new double[0]);
    }

    /**
     * Test method for edge case: {@link MannWhitneyUTest#mannWhitneyU(double[], double[])}.
     * The samples have different lengths.
     */
    @Test
    public void testMannWhitneyUUnequalLengthSamples() {
        double[] x = {1.0};
        double[] y = {1.0, 2.0};
        MannWhitneyUTest test = new MannWhitneyUTest();
        double expected = 1.0;
        double actual = test.mannWhitneyU(x, y);
        assertEquals(expected, actual, 0.001);
    }
}