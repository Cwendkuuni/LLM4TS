import org.apache.commons.math3.stat.inference.MannWhitneyUTest;
import org.apache.commons.math3.stat.ranking.NaturalRanking;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MannWhitneyUTestTest {

    private final double[] x = {1.0, 2.0, 3.0, 4.0};
    private final double[] y = {1.5, 2.5, 3.5, 4.5};
    private final MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();

    @Test
    public void testMannWhitneyU() throws Exception {
        double mannWhitneyU = mannWhitneyUTest.mannWhitneyU(x, y);
        assertEquals(10.0, mannWhitneyU, Precision.EPSILON);
    }

    @Test(expected = NullArgumentException.class)
    public void testNullArgumentExceptionX() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        mannWhitneyUTest.mannWhitneyU(null, y);
    }

    @Test(expected = NullArgumentException.class)
    public void testNullArgumentExceptionY() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        mannWhitneyUTest.mannWhitneyU(x, null);
    }

    @Test(expected = NoDataException.class)
    public void testNoDataExceptionX() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        mannWhitneyUTest.mannWhitneyU(new double[0], y);
    }

    @Test(expected = NoDataException.class)
    public void testNoDataExceptionY() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        mannWhitneyUTest.mannWhitneyU(x, new double[0]);
    }

    @Test
    public void testMannWhitneyUTest() throws Exception {
        double asymptoticPValue = mannWhitneyUTest.mannnWhitneyUTest(x, y);
        assertEquals(0.0123, asymptoticPValue, Precision.EPSILON);
    }

    @Test(expected = ConvergenceException.class)
    public void testConvergenceException() throws Exception {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        mannWhitneyUTest.mannWhitneyUTest(new double[10000], new double[10000]);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testMaxCountExceededException() throws Exception {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        mannWhitneyUTest.mannWhitneyUTest(new double[Integer.MAX_VALUE], new double[Integer.MAX_VALUE]);
    }

    @Test
    public void testMannWhitneyUTestWithCustomRanking() throws Exception {
        NaturalRanking ranking = new NaturalRanking(NaNStrategy.REMOVE, TiesStrategy.DENSE);
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest(ranking);
        double[] xCustom = {1.0, 1.5, 2.0, 3.0, 4.0, 4.5};
        double[] yCustom = {1.5, 2.0, 2.5, 3.0, 4.0, 4.5};
        double asymptoticPValue = mannWhitneyUTest.mannnWhitneyUTest(xCustom, yCustom);
        assertEquals(0.0123, asymptoticPValue, Precision.EPSILON);
    }
}