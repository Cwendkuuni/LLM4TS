import org.apache.commons.math3.stat.inference.MannWhitneyUTest;
import org.apache.commons.math3.stat.ranking.NaturalRanking;
import org.apache.commons.math3.stat.ranking.TiesStrategy;
import org.apache.commons.math3.util.FastMath;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Tests for {@link MannWhitneyUTest} class.
 */
public class MannWhitneyUTestTest {

    private MannWhitneyUTest mannWhitneyUTest;
    private double[] x;
    private double[] y;

    @Before
    public void setUp() {
        mannWhitneyUTest = new MannWhitneyUTest();
        x = new double[]{1.0, 2.0, 3.0, 1.0, 4.0};
        y = new double[]{2.0, 3.0, 1.0, 5.0, 4.0};
    }

    /**
     * Test method for {@link MannWhitneyUTest#mannWhitneyU(double[], double[])}.
     */
    @Test
    public void testMannWhitneyU() {
        double result = mannWhitneyUTest.mannWhitneyU(x, y);
        assertEquals(3.0, result, 0.001);
    }

    /**
     * Test method for {@link MannWhitneyUTest#mannWhitneyUTest()} constructor.
     */
    @Test
    public void testDefaultConstructor() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        assertEquals(NaturalRanking.NATURAL, mannWhitneyUTest.naturalRanking.getStrategy());
        assertEquals(TiesStrategy.AVERAGE, mannWhitneyUTest.naturalRanking.getTiesStrategy());
    }

    /**
     * Test method for {@link MannWhitneyUTest#mannWhitneyUTest(NaNStrategy, TiesStrategy)} constructor.
     */
    @Test
    public void testCustomConstructor() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest(NaNStrategy.MISSING, TiesStrategy.DITHER);
        assertEquals(NaNStrategy.MISSING, mannWhitneyUTest.naturalRanking.getNaNStrategy());
        assertEquals(TiesStrategy.DITHER, mannWhitneyUTest.naturalRanking.getTiesStrategy());
    }

    /**
     * Test method for {@link MannWhitneyUTest#ensureDataConformance(double[], double[])}.
     */
    @Test
    public void testEnsureDataConformanceNullArgumentException() {
       assertThrows(NullArgumentException.class, () -> mannWhitneyUTest.ensureDataConformance(null, x));
        assertThrows(NullArgumentException.class, () -> mannWhitneyUTest.ensureDataConformance(x, null));
    }

    /**
     * Test method for {@link MannWhitneyUTest#ensureDataConformance(double[], double[])}.
     */
    @Test
    public void testEnsureDataConformanceNoDataException() {
        assertThrows(NoDataException.class, () -> mannWhitneyUTest.ensureDataConformance(new double[0], x));
        assertThrows(NoDataException.class, () -> mannWhitneyUTest.ensureDataConformance(x, new double[0]));
    }

    /**
     * Test method for {@link MannWhitneyUTest#calculateAsymptoticPValue(double, int, int)} method.
     */
    @Test
    public void testCalculateAsymptoticPValue() {
        double Umin = mannWhitneyUTest.mannWhitneyU(x, y);
        double pValue = mannWhitneyUTest.calculateAsymptoticPValue(Umin, x.length, y.length);
        assertEquals(0.0442, pValue, 0.001);
    }

    /**
     * Test method for {@link MannWhitneyUTest#mannWhitneyUTest(double[], double[])} method with equal data.
     */
    @Test
    public void testEqualData() {
        double[] xEqua = new double[]{1.0, 2.0, 2.0, 2.0, 2.0};
        double[] yEqua = new double[]{2.0, 2.0, 2.0, 2.0, 2.0};
        assertThrows(IllegalArgumentException.class, () -> mannWhitneyUTest.mannWhitneyUTest(xEqua, yEqua));
    }

    /**
     * Test method for {@link MannWhitneyUTest#mannWhitneyUTest(double[], double[])} method with empty data.
     */
    @Test
    public void testEmptyData() {
        double[] xEmpty = new double[0];
        double[] yEmpty = new double[0];
        assertThrows(IllegalArgumentException.class, () -> mannWhitneyUTest.mannWhitneyUTest(xEmpty, yEmpty));
    }

    /**
     * Test method for {@link MannWhitneyUTest#mannWhitneyUTest(double[], double[])} method with one data point in each array.
     */
    @Test
    public void testOneDataPointPerArray() {
        double[] xOne = new double[]{1.0};
        double[] yOne = new double[]{2.0};
        assertThrows(IllegalArgumentException.class, () -> mannWhitneyUTest.mannWhitneyUTest(xOne, yOne));
    }
}