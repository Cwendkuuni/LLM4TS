import org.apache.commons.math3.stat.inference.MannWhitneyUTest;
import org.apache.commons.math3.stat.ranking.NaturalRanking;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MannWhitneyUTestTest {

    @Test
    public void testMannWhitneyU_sameData() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1.0, 1.5, 2.0, 2.5};
        double[] y = x; // same data

        double result = 0.0;
        try {
            result = test.mannWhitneyU(x, y);
        } catch (NullArgumentException | NoDataException e) {
            // should not reach here
        }

        assertEquals(3.0, result, 0.001);
    }

    @Test
    public void testMannWhitneyU_emptyData() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {};
        double[] y = {};

        try {
            test.mannWhitneyU(x, y);
            fail("Should have thrown NoDataException");
        } catch (NoDataException e) {
            // expected
        }
    }

    @Test
    public void testMannWhitneyU_nullData() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = null;
        double[] y = null;

        try {
            test.mannWhitneyU(x, y);
            fail("Should have thrown NullArgumentException");
        } catch (NullArgumentException e) {
            // expected
        }
    }

    @Test
    public void testMannWhitneyU_differentLengthData() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1.0, 1.5, 2.0, 2.5};
        double[] y = {1.0, 2.0};

        try {
            double result = test.mannWhitneyU(x, y);
            assertEquals(2.0, result, 0.001);
        } catch (NullArgumentException | NoDataException e) {
            // should not reach here
        }
    }

    @Test
    public void testMannWhitneyUTest_sameData() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1.0, 1.5, 2.0, 2.5};
        double[] y = x; // same data

        double alpha = 0.05;
        double result = 0.0;
        try {
            result = test.mannWhitneyUTest(x, y, alpha);
        } catch (NullArgumentException | NoDataException | ConvergenceException | MaxCountExceededException e) {
            // should not reach here
        }

        assertEquals(alpha, result, 0.01);
    }

    @Test
    public void testMannWhitneyUTest_emptyData() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {};
        double[] y = {};

        try {
            test.mannWhitneyUTest(x, y, 0.05);
            fail("Should have thrown NoDataException");
        } catch (NoDataException e) {
            // expected
        }
    }

    @Test
    public void testMannWhitneyUTest_nullData() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = null;
        double[] y = null;

        try {
            test.mannWhitneyUTest(x, y, 0.05);
            fail("Should have thrown NullArgumentException");
        } catch (NullArgumentException e) {
            // expected
        }
    }

    @Test
    public void testMannWhitneyUTest_differentLengthData() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1.0, 1.5, 2.0, 2.5};
        double[] y = {1.0, 2.0, 3.0};

        try {
            test.mannWhitneyUTest(x, y, 0.05);
            fail("Should have thrown ConvergenceException or MaxCountExceededException");
        } catch (ConvergenceException | MaxCountExceededException e) {
            // expected
        }
    }
}