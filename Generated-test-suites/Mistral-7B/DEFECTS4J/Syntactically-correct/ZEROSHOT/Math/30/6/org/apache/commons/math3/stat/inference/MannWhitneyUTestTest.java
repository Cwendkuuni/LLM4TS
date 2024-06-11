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
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for {@link MannWhitneyUTest} class.
 */
public class MannWhitneyUTestTest {

    /**
     * Test method for {@link MannWhitneyUTest#mannWhitneyU(double[], double[])}.
     */
    @Test
    public void testMannWhitneyU() {
        MannWhitneyUTest test = new MannWhitneyUTest();

        // Test with two empty arrays
        Assert.assertThrows(NoDataException.class, () -> test.mannWhitneyU(new double[0], new double[0]));

        // Test with two arrays of length 1
        double[] x = {1.0};
        double[] y = {2.0};
        double result = test.mannWhitneyU(x, y);
        Assert.assertEquals(0.0, result, 0.0);

        // Test with two arrays of length 2
        x = new double[]{1.0, 3.0};
        y = new double[]{2.0, 4.0};
        result = test.mannWhitneyU(x, y);
        Assert.assertEquals(3.0, result, 0.0);

        // Test with two arrays of length 3 and same elements
        x = new double[]{1.0, 1.0, 1.0};
        y = new double[]{1.0, 1.0, 1.0};
        result = test.mannWhitneyU(x, y);
        Assert.assertEquals(3.0 * 3.0, result, 0.0);

        // Test with two arrays of length 3 and different elements
        x = new double[]{1.0, 2.0, 3.0};
        y = new double[]{4.0, 5.0, 6.0};
        result = test.mannWhitneyU(x, y);
        Assert.assertEquals(15.0, result, 0.0);

        // Test with two arrays of length 4 and same elements
        x = new double[]{1.0, 1.0, 1.0, 1.0};
        y = new double[]{1.0, 1.0, 1.0, 1.0};
        result = test.mannWhitneyU(x, y);
        Assert.assertEquals(6.0 * 6.0, result, 0.0);

        // Test with two arrays of length 4 and different elements
        x = new double[]{1.0, 2.0, 3.0, 4.0};
        y = new double[]{5.0, 6.0, 7.0, 8.0};
        result = test.mannWhitneyU(x, y);
        Assert.assertEquals(24.0, result, 0.0);
    }

    /**
     * Test method for {@link MannWhitneyUTest#mannWhitneyUTest()}.
     */
    @Test
    public void testMannWhitneyUTest() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0, 6.0};
        try {
            double result = test.mannWhitneyUTest(x, y);
            Assert.assertNotNull(result);
        } catch (NoDataException | NullArgumentException | ConvergenceException | MaxCountExceededException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test method for {@link MannWhitneyUTest#mannWhitneyUTest(NaNStrategy, TiesStrategy)}.
     */
    @Test
    public void testMannWhitneyUTestWithStrategies() {
        MannWhitneyUTest test = new MannWhitneyUTest(NAStrategy.SKIP, TiesStrategy.LARGEST);
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0, 6.0};
        try {
            double result = test.mannWhitneyUTest(x, y);
            Assert.assertNotNull(result);
        } catch (NoDataException | NullArgumentException | ConvergenceException | MaxCountExceededException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }
}