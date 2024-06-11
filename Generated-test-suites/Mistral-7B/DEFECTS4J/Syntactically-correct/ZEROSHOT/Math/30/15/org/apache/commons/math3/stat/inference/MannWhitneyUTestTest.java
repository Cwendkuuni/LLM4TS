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
 * Test for MannWhitneyUTest class.
 */
public class MannWhitneyUTestTest {

    @Test
    public void testMannWhitneyU() throws NullArgumentException, NoDataException,
            ConvergenceException, MaxCountExceededException {
        // Test default constructor
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double[] x = new double[]{1.0, 1.5, 2.0, 2.5};
        double[] y = new double[]{2.5, 3.0, 3.5};
        double result = mannWhitneyU(mannWhitneyUTest, x, y);
        double expected = 5.0;
        assertEquals(expected, result, 0.001);

        // Test constructor with custom strategies
        MannWhitneyUTest mannWhitneyUTestWithCustomStrategies = new MannWhitneyUTest(
                NaNStrategy.SKIP, TiesStrategy.MIN);
        double[] xWithTies = new double[]{1.0, 1.5, 1.5, 2.0, 2.5};
        double[] yWithTies = new double[]{2.5, 2.5, 3.0, 3.5};
        result = mannWhitneyU(mannWhitneyUTestWithCustomStrategies, xWithTies,
                yWithTies);
        expected = 5.5;
        assertEquals(expected, result, 0.001);

        // Test mannWhitneyUTest method
        double pValue = mannWhitneyUTest(mannWhitneyUTestWithCustomStrategies,
                xWithTies, yWithTies);
        NormalDistribution normalDistribution = new NormalDistribution(0, 1);
        double criticalValue = normalDistribution.inverseCumulativeProbability(0.025);
        double expectedPValue = 2 * normalDistribution.cumulativeProbability(
                criticalValue);
        assertEquals(expectedPValue, pValue, 0.001);
    }

    private double mannWhitneyU(MannWhitneyUTest mannWhitneyUTest, double[] x,
            double[] y) throws NullArgumentException, NoDataException,
            ConvergenceException, MaxCountExceededException {
        mannWhitneyUTest.ensureDataConformance(x, y);
        return mannWhitneyUTest.mannWhitneyU(x, y);
    }
}