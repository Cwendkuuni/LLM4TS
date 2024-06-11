import org.apache.commons.math3.stat.inference.ChiSquareTestImpl;
import org.apache.commons.math3.stat.inference.UnknownDistributionChiSquareTest;
import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class ChiSquareTestImplTest {

    @Test
    public void testChiSquare() {
        // Test with valid inputs
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0, 1.0, 2.0};
        long[] observed = {2, 3, 1, 3};
        double chiSquare = test.chiSquare(expected, observed);
        assertEquals(4.302585092994, chiSquare, 0.001);

        // Test with edge cases
        expected = new double[0];
        observed = new long[0];
        try {
            test.chiSquare(expected, observed);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        expected = new double[]{1.0, 1.0, 1.0, 1.0};
        observed = new long[]{1, 1, 1, 1};
        try {
            test.chiSquare(expected, observed);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test with null inputs
        expected = null;
        observed = null;
        try {
            test.chiSquare(expected, observed);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }

        // Test with invalid inputs (different lengths)
        expected = new double[]{1.0, 2.0, 1.0, 2.0};
        observed = new long[]{2, 3, 3, 3};
        try {
            test.chiSquare(expected, observed);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testChiSquareTest() throws MathException, IllegalArgumentException {
        ChiSquareTestImpl test = new ChiSquareTestImpl(new ChiSquaredDistributionImpl(2.0));
        double[] expected = {1.0, 2.0, 1.0, 2.0};
        long[] observed = {2, 3, 1, 3};
        double pValue = test.chiSquareTest(expected, observed);
        assertEquals(0.445175790432, pValue, 0.001);

        // Test with edge cases
        test = new ChiSquareTestImpl(new ChiSquaredDistributionImpl(1.0));
        expected = new double[]{1.0, 1.0};
        observed = new long[]{1, 1};
        pValue = test.chiSquareTest(expected, observed);
        assertEquals(0.0, pValue, 0.001);

        test = new ChiSquareTestImpl(new ChiSquaredDistributionImpl(0.0));
        expected = new double[]{1.0};
        observed = new long[]{1};
        try {
            pValue = test.chiSquareTest(expected, observed);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test with null inputs
        expected = null;
        observed = null;
        try {
            test.chiSquareTest(expected, observed);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }

        // Test with invalid inputs (different lengths)
        expected = new double[]{1.0, 2.0, 1.0, 2.0};
        observed = new long[]{2, 3, 3, 4};
        try {
            test.chiSquareTest(expected, observed);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test with invalid degrees of freedom
        test = new ChiSquareTestImpl(new ChiSquaredDistributionImpl(-1.0));
        try {
            test.chiSquareTest(expected, observed);
            fail("Expected MathException");
        } catch (MathException e) {
            // Expected
        }
    }

    @Test
    public void testChiSquareTestWithAlpha() throws MathException, IllegalArgumentException {
        ChiSquareTestImpl test = new ChiSquareTestImpl(new ChiSquaredDistributionImpl(2.0));
        double[] expected = {1.0, 2.0, 1.0, 2.0};
        long[] observed = {2, 3, 1, 3};
        double alpha = 0.05;
        boolean rejected = test.chiSquareTest(expected, observed, alpha);
        assertTrue(rejected);

        // Test with edge cases
        test = new ChiSquareTestImpl(new ChiSquaredDistributionImpl(1.0));
        expected = new double[]{1.0, 1.0};
        observed = new long[]{1, 1};
        alpha = 1.0;
        rejected = test.chiSquareTest(expected, observed, alpha);
        assertFalse(rejected);

        test = new ChiSquareTestImpl(new ChiSquaredDistributionImpl(0.0));
        expected = new double[]{1.0};
        observed = new long[]{1};
        try {
            rejected = test.chiSquareTest(expected, observed, 0.0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test with null inputs
        expected = null;
        observed = null;
        try {
            rejected = test.chiSquareTest(expected, observed, 0.0);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }

        // Test with invalid inputs (different lengths)
        expected = new double[]{1.0, 2.0, 1.0, 2.0};
        observed = new long[]{2, 3, 3, 4};
        try {
            rejected = test.chiSquareTest(expected, observed, 0.0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test with invalid alpha
        test = new ChiSquareTestImpl(new ChiSquaredDistributionImpl(2.0));
        rejected = test.chiSquareTest(expected, observed, -1.0);
        assertFalse(rejected);
    }

    @Test
    public void testChiSquareDataSetsComparison() throws IllegalArgumentException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 1, 2};
        long[] observed2 = {2, 1, 1, 2};
        double chiSquare = test.chiSquareDataSetsComparison(observed1, observed2);
        assertEquals(1.404578134786, chiSquare, 0.001);

        // Test with edge cases
        observed1 = new long[]{1};
        observed2 = new long[]{1};
        try {
            test.chiSquareDataSetsComparison(observed1, observed2);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test with null inputs
        observed1 = null;
        observed2 = null;
        try {
            test.chiSquareDataSetsComparison(observed1, observed2);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }

        // Test with invalid inputs (different lengths)
        observed1 = new long[]{1, 2, 1, 2};
        observed2 = new long[]{2, 3, 3, 4};
        try {
            test.chiSquareDataSetsComparison(observed1, observed2);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testChiSquareTestDataSetsComparison() throws MathException, IllegalArgumentException {
        ChiSquareTestImpl test = new ChiSquareTestImpl(new ChiSquaredDistributionImpl(2.0));
        long[] observed1 = {1, 2, 1, 2};
        long[] observed2 = {2, 1, 1, 2};
        double pValue = test.chiSquareTestDataSetsComparison(observed1, observed2);
        assertEquals(0.253721812205, pValue, 0.001);

        // Test with edge cases
        test = new ChiSquareTestImpl(new ChiSquaredDistributionImpl(1.0));
        observed1 = new long[]{1, 1};
        observed2 = new long[]{1, 1};
        pValue = test.chiSquareTestDataSetsComparison(observed1, observed2);
        assertEquals(1.0, pValue, 0.001);

        test = new ChiSquareTestImpl(new ChiSquaredDistributionImpl(0.0));
        observed1 = new long[]{1};
        observed2 = new long[]{1};
        try {
            pValue = test.chiSquareTestDataSetsComparison(observed1, observed2);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test with null inputs
        observed1 = null;
        observed2 = null;
        try {
            test.chiSquareTestDataSetsComparison(observed1, observed2);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }

        // Test with invalid inputs (different lengths)
        observed1 = new long[]{1, 2, 1, 2};
        observed2 = new long[]{2, 3, 3, 4};
        try {
            test.chiSquareTestDataSetsComparison(observed1, observed2);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test with invalid degrees of freedom
        test = new ChiSquareTestImpl(new ChiSquaredDistributionImpl(-1.0));
        try {
            test.chiSquareTestDataSetsComparison(observed1, observed2);
            fail("Expected MathException");
        } catch (MathException e) {
            // Expected
        }
    }

    @Test
    public void testChiSquareTestDataSetsComparisonWithAlpha()
            throws MathException, IllegalArgumentException {
        ChiSquareTestImpl test = new ChiSquareTestImpl(new ChiSquaredDistributionImpl(2.0));
        long[] observed1 = {1, 2, 1, 2};
        long[] observed2 = {2, 1, 1, 2};
        double alpha = 0.05;
        boolean rejected = test.chiSquareTestDataSetsComparison(observed1, observed2, alpha);
        assertTrue(rejected);

        // Test with edge cases
        test = new ChiSquareTestImpl(new ChiSquaredDistributionImpl(1.0));
        observed1 = new long[]{1, 1};
        observed2 = new long[]{1, 1};
        alpha = 1.0;
        rejected = test.chiSquareTestDataSetsComparison(observed1, observed2, alpha);
        assertFalse(rejected);

        test = new ChiSquareTestImpl(new ChiSquaredDistributionImpl(0.0));
        observed1 = new long[]{1};
        observed2 = new long[]{1};
        try {
            rejected = test.chiSquareTestDataSetsComparison(observed1, observed2, 0.0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test with null inputs
        observed1 = null;
        observed2 = null;
        try {
            rejected = test.chiSquareTestDataSetsComparison(observed1, observed2, 0.0);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }

        // Test with invalid inputs (different lengths)
        observed1 = new long[]{1, 2, 1, 2};
        observed2 = new long[]{2, 3, 3, 4};
        try {
            rejected = test.chiSquareTestDataSetsComparison(observed1, observed2, 0.0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test with invalid alpha
        test = new ChiSquareTestImpl(new ChiSquaredDistributionImpl(2.0));
        rejected = test.chiSquareTestDataSetsComparison(observed1, observed2, -1.0);
        assertFalse(rejected);
    }
}