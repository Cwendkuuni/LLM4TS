import org.apache.commons.math3.stat.inference.MannWhitneyUTest;
import org.apache.commons.math3.stat.ranking.NaturalRanking;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test class for MannWhitneyUTest.
 */
public class MannWhitneyUTestTest {

    @Test
    public void testMannWhitneyU() {
        // Test with empty arrays
        double[] x = new double[0];
        double[] y = new double[0];
        MannWhitneyUTest test = new MannWhitneyUTest();
        try {
            test.mannWhitneyU(x, y);
            fail("Expected NoDataException");
        } catch (NoDataException e) {
            // Expected
        }

        // Test with null arrays
        x = null;
        test = new MannWhitneyUTest();
        try {
            test.mannWhitneyU(x, y);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // Expected
        }

        y = new double[]{1.0};
        x = new double[]{2.0};
        test = new MannWhitneyUTest();
        double result = test.mannWhitneyU(x, y);
        double expected = 2.0;
        assertEquals(expected, result, 0.0);

        x = new double[]{1.0, 2.0, 3.0};
        y = new double[]{4.0, 5.0, 6.0};
        test = new MannWhitneyUTest();
        result = test.mannWhitneyU(x, y);
        expected = 15.0;
        assertEquals(expected, result, 0.0);

        // Test with the same array for both samples
        x = y = new double[]{1.0, 1.0, 1.0, 1.0, 1.0};
        test = new MannWhitneyUTest();
        result = test.mannWhitneyU(x, y);
        expected = 6.0;
        assertEquals(expected, result, 0.0);

        // Test with different lengths but same elements
        x = new double[]{1.0, 1.0, 1.0};
        y = new double[]{1.0};
        test = new MannWhitneyUTest();
        result = test.mannWhitneyU(x, y);
        expected = 2.5;
        assertEquals(expected, result, 0.0);

        // Test with different lengths and different elements
        x = new double[]{1.0, 2.0, 3.0};
        y = new double[]{4.0, 5.0, 6.0, 7.0};
        test = new MannWhitneyUTest();
        result = test.mannWhitneyU(x, y);
        expected = 18.0;
        assertEquals(expected, result, 0.0);

        // Test withNaN's
        x = new double[]{Double.NaN, 1.0, 2.0};
        y = new double[]{Double.NaN, 4.0, 5.0};
        test = new MannWhitneyUTest();
        try {
            test.mannWhitneyU(x, y);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // Expected
        }

        x = new double[]{Double.NaN, Double.NaN, 1.0, 2.0};
        y = new double[]{Double.NaN, Double.NaN, 4.0, 5.0};
        test = new MannWhitneyUTest(NaturalRanking.MIN_MAX, TiesStrategy.MIN);
        result = test.mannWhitneyU(x, y);
        expected = 5.5;
        assertEquals(expected, result, 0.0);

        test = new MannWhitneyUTest(NaturalRanking.MIN_MAX, TiesStrategy.MAX);
        result = test.mannWhitneyU(x, y);
        expected = 2.5;
        assertEquals(expected, result, 0.0);
    }

    @Test
    public void testMannWhitneyUTest() {
        // Test with empty arrays
        double[] x = new double[0];
        double[] y = new double[0];
        MannWhitneyUTest test = new MannWhitneyUTest();
        try {
            test.mannWhitneyUTest(x, y);
            fail("Expected NoDataException");
        } catch (NoDataException e) {
            // Expected
        }

        // Test with null arrays
        x = null;
        test = new MannWhitneyUTest();
        try {
            test.mannWhitneyUTest(x, y);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // Expected
        }

        y = new double[]{1.0};
        x = new double[]{2.0};
        test = new MannWhitneyUTest();
        double result = test.mannWhitneyUTest(x, y);
        double expected = 0.05;
        assertEquals(expected, result, 0.001);

        x = new double[]{1.0, 2.0, 3.0};
        y = new double[]{4.0, 5.0, 6.0};
        test = new MannWhitneyUTest();
        result = test.mannWhitneyUTest(x, y);
        expected = 0.0045;
        assertEquals(expected, result, 0.0001);

        // Test with the same array for both samples
        x = y = new double[]{1.0, 1.0, 1.0, 1.0, 1.0};
        test = new MannWhitneyUTest();
        result = test.mannWhitneyUTest(x, y);
        expected = 0.5;
        assertEquals(expected, result, 0.001);

        // Test with different lengths but same elements
        x = new double[]{1.0, 1.0, 1.0};
        y = new double[]{1.0};
        test = new MannWhitneyUTest();
        result = test.mannWhitneyUTest(x, y);
        expected = 0.1;
        assertEquals(expected, result, 0.001);

        // Test with different lengths and different elements
        x = new double[]{1.0, 2.0, 3.0};
        y = new double[]{4.0, 5.0, 6.0, 7.0};
        test = new MannWhitneyUTest();
        result = test.mannWhitneyUTest(x, y);
        expected = 0.003;
        assertEquals(expected, result, 0.0001);

        // Test withNaN's
        x = new double[]{Double.NaN, 1.0, 2.0};
        y = new double[]{Double.NaN, 4.0, 5.0};
        test = new MannWhitneyUTest();
        try {
            test.mannWhitneyUTest(x, y);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // Expected
        }

        x = new double[]{Double.NaN, Double.NaN, 1.0, 2.0};
        y = new double[]{Double.NaN, Double.NaN, 4.0, 5.0};
        test = new MannWhitneyUTest(NaturalRanking.MIN_MAX, TiesStrategy.MIN);
        result = test.mannWhitneyUTest(x, y);
        expected = 0.125;
        assertEquals(expected, result, 0.001);

        test = new MannWhitneyUTest(NaturalRanking.MIN_MAX, TiesStrategy.MAX);
        result = test.mannWhitneyUTest(x, y);
        expected = 0.0625;
        assertEquals(expected, result, 0.0001);
    }
}