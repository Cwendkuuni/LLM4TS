import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.distribution.ChiSquaredDistributionImpl;
import org.apache.commons.math3.stat.inference.UnknownDistributionChiSquareTest;
import org.junit.Test;
import static org.junit.Assert.*;

public class ChiSquareTestImplTest {

    @Test
    public void testChiSquareTestImpl() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        assertNotNull(test);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareNullExpectedOrObserved() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        test.chiSquare(null, new long[0]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareDifferentLengths() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = new double[3];
        long[] observed = new long[2];
        test.chiSquare(expected, observed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareNegativeExpectedOrObserved() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = new double[]{-1, 2};
        long[] observed = new long[]{1, 1};
        test.chiSquare(expected, observed);
    }

    @Test
    public void testChiSquare() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = new double[]{1.0, 2.0};
        long[] observed = new long[]{2, 3};
        double chiSquare = test.chiSquare(expected, observed);
        assertEquals(3.75, chiSquare, 0.001);
    }

    @Test
    public void testChiSquareTest() throws MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = new double[]{1.0, 2.0};
        long[] observed = new long[]{2, 3};
        double pValue = test.chiSquareTest(expected, observed);
        assertEquals(0.084, pValue, 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestInvalidAlpha() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = new double[]{1.0, 2.0};
        long[] observed = new long[]{2, 3};
        test.chiSquareTest(expected, observed, -0.1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestInvalidAlphaGreaterThan05() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = new double[]{1.0, 2.0};
        long[] observed = new long[]{2, 3};
        test.chiSquareTest(expected, observed, 0.6);
    }

    @Test
    public void testChiSquareTestAcceptNullHypothesis() throws MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = new double[]{1.0, 2.0};
        long[] observed = new long[]{2, 3};
        boolean result = test.chiSquareTest(expected, observed, 0.05);
        assertTrue(result);
    }

    @Test
    public void testChiSquareTestRejectNullHypothesis() throws MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = new double[]{1.0, 3.0};
        long[] observed = new long[]{2, 3};
        boolean result = test.chiSquareTest(expected, observed, 0.05);
        assertFalse(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquare2WayTableNull() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[][] counts = null;
        test.chiSquare(counts);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquare2WayTableEmpty() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[][] counts = new long[0][];
        test.chiSquare(counts);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquare2WayTableDifferentRowsLength() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[][] counts = new long[2][3];
        counts[0] = new long[2];
        test.chiSquare(counts);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquare2WayTableDifferentColumnsLength() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[][] counts = new long[3][2];
        test.chiSquare(counts);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquare2WayTableNegativeEntry() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[][] counts = new long[2][2];
        counts[0][0] = -1;
        test.chiSquare(counts);
    }

    @Test
    public void testChiSquare2WayTable() throws MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[][] counts = new long[2][2];
        counts[0][0] = 3;
        counts[0][1] = 5;
        counts[1][0] = 5;
        counts[1][1] = 7;
        double chiSquare = test.chiSquare(counts);
        assertEquals(6.605, chiSquare, 0.001);
    }

    @Test
    public void testChiSquareTest2WayTable() throws MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[][] counts = new long[2][2];
        counts[0][0] = 3;
        counts[0][1] = 5;
        counts[1][0] = 5;
        counts[1][1] = 7;
        double pValue = test.chiSquareTest(counts);
        assertEquals(0.016, pValue, 0.001);
    }

    @Test
    public void testChiSquareTest2WayTableRejectNullHypothesis() throws MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[][] counts = new long[2][2];
        counts[0][0] = 4;
        counts[0][1] = 5;
        counts[1][0] = 5;
        counts[1][1] = 7;
        boolean result = test.chiSquareTest(counts, 0.05);
        assertFalse(result);
    }
}