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

public class MannWhitneyUTestTest {

    @Test
    public void testMannWhitneyUTest() {
        MannWhitneyUTest test = new MannWhitneyUTest();

        double[] sample1 = {1.0, 1.5, 2.0, 2.5};
        double[] sample2 = {2.5, 3.0, 3.5};

        try {
            double pValue = test.mannWhitneyUTest(sample1, sample2);
            assertEquals(0.05, pValue, 0.001);
        } catch (NullArgumentException | NoDataException | ConvergenceException | MaxCountExceededException e) {
            // Tests should not throw exceptions
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = NullArgumentException.class)
    public void testNullArgumentException() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] nullSample = null;
        test.mannWhitneyUTest(nullSample, new double[0]);
    }

    @Test(expected = NoDataException.class)
    public void testNoDataException() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] emptySample = new double[0];
        test.mannWhitneyUTest(emptySample, new double[0]);
    }

    @Test(expected = ConvergenceException.class)
    public void testConvergenceException() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] sample1 = new double[100000];
        double[] sample2 = new double[100000];
        test.mannWhitneyUTest(sample1, sample2);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testMaxCountExceededException() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] sample1 = new double[Integer.MAX_VALUE];
        double[] sample2 = new double[Integer.MAX_VALUE];
        test.mannWhitneyUTest(sample1, sample2);
    }

    @Test
    public void testMannWhitneyUTestWithCustomStrategies() {
        MannWhitneyUTest test = new MannWhitneyUTest(
                new NaNStrategy() {
                    @Override
                    public double nanrank1d(double d) {
                        return Double.NaN;
                    }
                },
                new TiesStrategy() {
                    @Override
                    public int calculateRank(double value, int n) {
                        int rank = 1;
                        for (int i = 0; i < n; i++) {
                            if (Double.isNaN(value) || value < getNthValue(i + 1)) {
                                rank++;
                            } else {
                                break;
                            }
                        }
                        return rank;
                    }

                    private double getNthValue(int index) {
                        throw new UnsupportedOperationException();
                    }
                }
        );

        double[] sample1 = {1.0, 1.5, 2.0, 2.5, Double.NaN};
        double[] sample2 = {2.5, 3.0, 3.5, Double.NaN};

        try {
            double pValue = test.mannWhitneyUTest(sample1, sample2);
            assertEquals(0.1, pValue, 0.01);
        } catch (NullArgumentException | NoDataException | ConvergenceException | MaxCountExceededException e) {
            // Tests should not throw exceptions
            fail("Unexpected exception: " + e.getMessage());
        }
    }
}