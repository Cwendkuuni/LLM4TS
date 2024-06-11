package org.apache.commons.math3.stat.inference.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.stat.inference.MannWhitneyUTest;
import org.junit.Test;

public class MannWhitneyUTestTest {

    @Test
    public void testMannWhitneyU() {
        // Test empty arrays
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = new double[0];
        double[] y = new double[0];
        assertThrows(NoDataException.class, () -> test.mannWhitneyU(x, y));

        // Test null arrays
        assertThrows(NullArgumentException.class, () -> test.mannWhitneyU(null, y));
        assertThrows(NullArgumentException.class, () -> test.mannWhitneyU(x, null));

        // Test samples with same elements
        double[] x1 = new double[]{1.0, 2.0, 3.0, 4.0};
        double[] y1 = new double[]{1.0, 2.0, 3.0, 4.0};
        assertEquals(6.0, test.mannWhitneyU(x1, y1), 0.001);

        // Test samples with different lengths
        double[] x2 = new double[]{1.0, 2.0, 3.0};
        double[] y2 = new double[]{1.0, 2.0, 3.0, 4.0};
        assertEquals(4.5, test.mannWhitneyU(x2, y2), 0.001);

        // Test samples with different elements
        double[] x3 = new double[]{1.0, 2.0, 3.0};
        double[] y3 = new double[]{4.0, 5.0, 6.0};
        assertEquals(9.0, test.mannWhitneyU(x3, y3), 0.001);

        // Test samples with NaN value
        double[] x4 = new double[]{1.0, 2.0, Double.NaN};
        double[] y4 = new double[]{1.0, 2.0, 3.0};
        assertThrows(NoDataException.class, () -> test.mannWhitneyU(x4, y4));

        // Test samples with NaN strategy and ties strategy fixed
        MannWhitneyUTest testNaNFix = new MannWhitneyUTest(NaNStrategy.FIXED, TiesStrategy.FIXED);
        double[] x5 = new double[]{1.0, 2.0, Double.NaN};
        double[] y5 = new double[]{1.0, 2.0, 3.0};
        assertEquals(3.0, testNaNFix.mannWhitneyU(x5, y5), 0.001);

        // Test convergence error
        double[] x6 = new double[]{Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY};
        double[] y6 = new double[]{Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY};
        assertThrows(ConvergenceException.class, () -> test.mannWhitneyU(x6, y6));

        // Test max count exceeded error
        double[] x7 = new double[]{Double.POSITIVE_INFINITY};
        double[] y7 = new double[]{Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY};
        assertThrows(MaxCountExceededException.class, () -> test.mannWhitneyU(x7, y7));
    }

    @Test
    public void testMannWhitneyUTest() throws ConvergenceException, MaxCountExceededException, NoDataException, NullArgumentException {
        // Test empty arrays
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = new double[0];
        double[] y = new double[0];
        assertThrows(NoDataException.class, () -> test.mannWhitneyUTest(x, y));

        // Test null arrays
        assertThrows(NullArgumentException.class, () -> test.mannWhitneyUTest(null, y));
        assertThrows(NullArgumentException.class, () -> test.mannWhitneyUTest(x, null));

        // Test samples with same elements
        double[] x1 = new double[]{1.0, 2.0, 3.0, 4.0};
        double[] y1 = new double[]{1.0, 2.0, 3.0, 4.0};
        assertEquals(0.0, test.mannWhitneyUTest(x1, y1), 0.001);

        // Test samples with different lengths
        double[] x2 = new double[]{1.0, 2.0, 3.0};
        double[] y2 = new double[]{1.0, 2.0, 3.0, 4.0};
        assertEquals(1.0 / 3.0, test.mannWhitneyUTest(x2, y2), 0.001);

        // Test samples with different elements
        double[] x3 = new double[]{1.0, 2.0, 3.0};
        double[] y3 = new double[]{4.0, 5.0, 6.0};
        assertEquals(1.0, test.mannWhitneyUTest(x3, y3), 0.001);

        // Test samples with NaN value
        double[] x4 = new double[]{1.0, 2.0, Double.NaN};
        double[] y4 = new double[]{1.0, 2.0, 3.0};
        assertThrows(NoDataException.class, () -> test.mannWhitneyUTest(x4, y4));

        // Test convergence error
        double[] x5 = new double[]{Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY};
        double[] y5 = new double[]{Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY};
        assertThrows(ConvergenceException.class, () -> test.mannWhitneyUTest(x5, y5));

        // Test max count exceeded error
        double[] x6 = new double[]{Double.POSITIVE_INFINITY};
        double[] y6 = new double[]{Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY};
        assertThrows(MaxCountExceededException.class, () -> test.mannWhitneyUTest(x6, y6));
    }
}