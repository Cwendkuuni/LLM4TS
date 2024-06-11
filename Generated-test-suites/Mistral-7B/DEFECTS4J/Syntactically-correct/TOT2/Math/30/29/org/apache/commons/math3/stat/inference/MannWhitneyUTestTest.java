import org.apache.commons.math3.stat.inference.MannWhitneyUTest;
import org.apache.commons.math3.stat.ranking.NaturalRanking;
import org.apache.commons.math3.stat.ranking.Ranking;
import org.junit.Assert;
import org.junit.Test;

import java.util.Random;

public class MannWhitneyUTestTest {

    @Test
    public void testMannWhitneyU() {
        // Create test instance using default strategies
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();

        // Test with equal samples
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {3.0, 2.0, 1.0};
        double mannWhitneyU = mannWhitneyUTest.mannWhitneyU(x, y);
        Assert.assertEquals(1.0, mannWhitneyU, 0.0);

        // Test with different length samples
        double[] x1 = {1.0, 2.0, 3.0};
        double[] y1 = {3.0, 4.0, 5.0};
        double mannWhitneyU1 = mannWhitneyUTest.mannWhitneyU(x1, y1);
        Assert.assertEquals(6.0, mannWhitneyU1, 0.0);

        // Test with samples having the same element
        double[] x2 = {2.0, 2.0, 2.0};
        double[] y2 = {2.0, 2.0, 2.0};
        try {
            mannWhitneyUTest.mannWhitneyU(x2, y2);
            Assert.fail("Expected NoDataException");
        } catch (NoDataException e) {
            // Expected
        }

        // Test with null arguments
        try {
            mannWhitneyUTest.mannWhitneyU(null, y1);
            Assert.fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // Expected
        }
        try {
            mannWhitneyUTest.mannWhitneyU(x1, null);
            Assert.fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // Expected
        }
        try {
            mannWhitneyUTest.mannWhitneyU(null, null);
            Assert.fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // Expected
        }

        // Test with empty samples
        try {
            mannWhitneyUTest.mannWhitneyU(new double[0], y1);
            Assert.fail("Expected NoDataException");
        } catch (NoDataException e) {
            // Expected
        }
        try {
            mannWhitneyUTest.mannWhitneyU(x1, new double[0]);
            Assert.fail("Expected NoDataException");
        } catch (NoDataException e) {
            // Expected
        }
        try {
            mannWhitneyUTest.mannWhitneyU(new double[0], new double[0]);
            Assert.fail("Expected NoDataException");
        } catch (NoDataException e) {
            // Expected
        }

        // Test mannWhitneyUTest method
        try {
            mannWhitneyUTest.mannWhitneyUTest(x1, y1);
            Assert.assertEquals(0.01, mannWhitneyUTest.calculateAsymptoticPValue(6.0, x1.length, y1.length), 0.001);
        } catch (ConvergenceException | MaxCountExceededException e) {
            Assert.fail("Expected no exceptions");
        }

        // Test with different strategies
        MannWhitneyUTest mannWhitneyUTest1 = new MannWhitneyUTest(NaturalRanking.MEDIAN, TiesStrategy.MIN);
        double mannWhitneyU2 = mannWhitneyUTest1.mannWhitneyU(x, y);
        Assert.assertEquals(1.0, mannWhitneyU2, 0.0);
    }
}