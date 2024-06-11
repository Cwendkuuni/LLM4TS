import org.apache.commons.math3.stat.inference.MannWhitneyUTest;
import org.apache.commons.math3.stat.ranking.NaturalRanking;
import org.junit.Assert;
import org.junit.Test;

import java.util.Random;

public class MannWhitneyUTestTest {

    @Test
    public void testMannWhitneyU_SameData() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double[] x = new double[]{1.0, 2.0, 3.0, 4.0, 5.0};
        double[] y = x.clone();
        double result = mannWhitneyUTest.mannWhitneyU(x, y);
        Assert.assertEquals(10.0, result, 0.001);
    }

    @Test
    public void testMannWhitneyU_DifferentDataLengths() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double[] x = new double[]{1.0, 2.0, 3.0};
        double[] y = new double[]{4.0, 5.0, 6.0, 7.0};
        try {
            double result = mannWhitneyUTest.mannWhitneyU(x, y);
            Assert.fail();
        } catch (NullArgumentException | NoDataException e) {
            // Expected, as data length mismatch is an error
        }
    }

    @Test
    public void testMannWhitneyU_NullInput() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        try {
            mannWhitneyUTest.mannWhitneyU(null, null);
            Assert.fail();
        } catch (NullArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testMannWhitneyU_EmptyData() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        try {
            mannWhitneyUTest.mannWhitneyU(new double[0], new double[0]);
            Assert.fail();
        } catch (NoDataException e) {
            // Expected
        }
    }

    @Test
    public void testMannWhitneyU_NaNValues() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double[] x = new double[]{1.0, Double.NaN, 3.0, 4.0, 5.0};
        double[] y = new double[]{1.0, 2.0, 3.0, Double.NaN, 5.0};
        try {
            mannWhitneyUTest.mannWhitneyU(x, y);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // Expected, as NaN values are not supported in the current test setup
        }
    }

    @Test
    public void testMannWhitneyUTest_SameData() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double[] x = new double[]{1.0, 2.0, 3.0, 4.0, 5.0};
        double[] y = x.clone();
        double expectedPValue = 0.5;
        double result = mannWhitneyUTest.mannWhitneyUTest(x, y);
        Assert.assertEquals(expectedPValue, result, 0.01);
    }

    @Test
    public void testMannWhitneyUTest_DifferentData() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double[] x = new double[]{1.0, 2.0, 3.0};
        double[] y = new double[]{4.0, 5.0, 6.0, 7.0};
        double expectedPValue = 0.5; // This is just a rough estimate
        double result = mannWhitneyUTest.mannWhitneyUTest(x, y);
        Assert.assertTrue(Math.abs(expectedPValue - result) < 0.05);
    }

    @Test
    public void testMannWhitneyUTest_NullInput() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        try {
            mannWhitneyUTest.mannWhitneyUTest(null, null);
            Assert.fail();
        } catch (NullArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testMannWhitneyUTest_EmptyData() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        try {
            mannWhitneyUTest.mannWhitneyUTest(new double[0], new double[0]);
            Assert.fail();
        } catch (NoDataException e) {
            // Expected
        }
    }

    @Test
    public void testMannWhitneyUTest_ErrorHandling() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double[] x = new double[]{1.0, 2.0, 3.0};
        double[] y = new double[]{4.0, 5.0, 6.0, 7.0, 8.0}; // One element too many
        try {
            mannWhitneyUTest.mannWhitneyUTest(x, y);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}