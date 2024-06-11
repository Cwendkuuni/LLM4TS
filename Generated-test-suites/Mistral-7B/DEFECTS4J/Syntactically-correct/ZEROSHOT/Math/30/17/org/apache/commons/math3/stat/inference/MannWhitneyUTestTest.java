import org.apache.commons.math3.stat.inference.MannWhitneyUTest;
import org.junit.Assert;
import org.junit.Test;

public class MannWhitneyUTestTest {

    @Test
    public void testMannWhitneyU() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double[] x = {1.0, 1.5, 2.0, 2.5};
        double[] y = {2.5, 3.0, 3.5};
        double mannWhitneyU = mannWhitneyUTest.mannWhitneyU(x, y);
        double expectedMannWhitneyU = 6.0;
        Assert.assertEquals("mannWhitneyU(x, y) should return " + expectedMannWhitneyU,
                expectedMannWhitneyU, mannWhitneyU, 0.001);
    }

    @Test
    public void testMannWhitneyUTest() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double[] x = {1.0, 1.5, 2.0, 2.5};
        double[] y = {2.5, 3.0, 3.5};
        double asymptoticPValue = mannWhitneyUTest.mannWhitneyUTest(x, y);
        double expectedAsymptoticPValue = 0.054; // Can be verified using statistical tables or calculators
        Assert.assertEquals("mannWhitneyUTest(x, y) should return " + expectedAsymptoticPValue,
                expectedAsymptoticPValue, asymptoticPValue, 0.001);
    }

    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUTestNullArgumentException() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double[] x = null;
        double[] y = {2.5, 3.0, 3.5};
        mannWhitneyUTest.mannWhitneyUTest(x, y);
    }

    @Test(expected = NoDataException.class)
    public void testMannWhitneyUTestNoDataException() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double[] x = {};
        double[] y = {2.5, 3.0, 3.5};
        mannWhitneyUTest.mannWhitneyUTest(x, y);
    }

    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUTestNullDataException() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double[] x = null;
        double[] y = null;
        mannWhitneyUTest.mannWhitneyUTest(x, y);
    }

    @Test(expected = ConvergenceException.class)
    public void testMannWhitneyUTestConvergenceException() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double[] x = {Double.NaN, Double.NaN, 2.5, 3.0, 3.5};
        double[] y = {2.5, 3.0, 3.5, Double.NaN, Double.NaN};
        mannWhitneyUTest.mannWhitneyUTest(x, y);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testMannWhitneyUTestMaxCountExceededException() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double[] x = new double[Integer.MAX_VALUE];
        double[] y = new double[Integer.MAX_VALUE];
        mannWhitneyUTest.mannWhitneyUTest(x, y);
    }
}