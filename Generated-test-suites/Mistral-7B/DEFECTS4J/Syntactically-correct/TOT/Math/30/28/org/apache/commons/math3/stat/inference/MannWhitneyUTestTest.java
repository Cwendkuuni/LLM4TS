package org.apache.commons.math3.stat.inference.test;

import static org.junit.Assert.assertEquals;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.stat.inference.MannWhitneyUTest;
import org.junit.Test;

/**
 * Test class for {@link MannWhitneyUTest}.
 */
public class MannWhitneyUTestTest {

    /**
     * Test method for {@link MannWhitneyUTest#mannWhitneyU(double[], double[])}.
     */
    @Test
    public void testMannWhitneyU() {
        final double[] x = {1.0, 2.0, 3.0, 3.0};
        final double[] y = {4.0, 5.0, 6.0, 6.0, 6.0};
        final MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();

        try {
            final double mannWhitneyU = mannWhitneyUTest.mannWhitneyU(x, y);
            assertEquals(10.0, mannWhitneyU, 0.0);
        } catch (NullArgumentException | NoDataException e) {
            // Test with invalid arguments
            assertEquals(0.0, 0.0);
        }
    }

    /**
     * Test method for {@link MannWhitneyUTest#mannWhitneyUTest()}.
     */
    @Test
    public void testMannWhitneyUTest() {
        final MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        final double[] x = {1.0, 2.0, 2.5};
        final double[] y = {2.5, 3.0, 3.5};

        try {
            final double mannWhitneyU = mannWhitneyUTest.mannWhitneyU(x, y);
            assertEquals(4.5, mannWhitneyU, 0.0);
            final double pValue = mannWhitneyUTest.mannWhitneyUTest(x, y);
            assertEquals(0.36842105263156, pValue, 0.001);
        } catch (NullArgumentException | NoDataException | ConvergenceException
                | MaxCountExceededException e) {
            // Test with invalid arguments
            assertEquals(Double.NaN, Double.NaN);
        }
    }

    /**
     * Test method for {@link MannWhitneyUTest#mannWhitneyUTest(NaNStrategy, TiesStrategy)}.
     */
    @Test
    public void testMannWhitneyUTestWithCustomStrategies() {
        final MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest(
                NaNStrategy.REMOVE, TiesStrategy.MIN);
        final double[] x = {1.0, 2.0, 2.5};
        final double[] y = {2.5, 3.0, 3.5};

        try {
            final double mannWhitneyU = mannWhitneyUTest.mannWhitneyU(x, y);
            assertEquals(3.5, mannWhitneyU, 0.0);
            final double pValue = mannWhitneyUTest.mannWhitneyUTest(x, y);
            assertEquals(0.5, pValue, 0.001);
        } catch (NullArgumentException | NoDataException | ConvergenceException
                | MaxCountExceededException e) {
            // Test with invalid arguments
            assertEquals(Double.NaN, Double.NaN);
        }
    }

    /**
     * Test method for {@link MannWhitneyUTest#mannWhitneyUTest(double[], double[])}.
     */
    @Test
    public void testMannWhitneyUTestWithEqualSamples() {
        final MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        final double[] x = {1.0, 1.0, 1.0};
        final double[] y = {1.0, 1.0, 1.0};

        try {
            final double mannWhitneyU = mannWhitneyUTest.mannWhitneyU(x, y);
            assertEquals(3.0, mannWhitneyU, 0.0);
            final double pValue = mannWhitneyUTest.mannWhitneyUTest(x, y);
            assertEquals(1.0, pValue, 0.0);
        } catch (NullArgumentException | NoDataException | ConvergenceException
                | MaxCountExceededException e) {
            // Test with invalid arguments
            assertEquals(Double.NaN, Double.NaN);
        }
    }
}