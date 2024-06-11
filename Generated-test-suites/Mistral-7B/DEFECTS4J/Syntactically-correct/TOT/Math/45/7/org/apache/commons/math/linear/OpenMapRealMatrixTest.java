package org.apache.commons.math.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.PrecisionUtils;
import org.apache.commons.math3.util.OpenIntToDoubleHashMap;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

/**
 * Tests for {@link OpenMapRealMatrix}.
 */
public class OpenMapRealMatrixTest {

    /**
     * Test construction with correct dimensions.
     */
    @Test
    public void testConstruction() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        assertEquals(2, matrix.getRowDimension());
        assertEquals(3, matrix.getColumnDimension());
    }

    /**
     * Test construction with incorrect dimensions.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructionInvalidDimensions() {
        new OpenMapRealMatrix(-1, 3);
    }

    /**
     * Test creating a matrix by copying another one.
     */
    @Test
    public void testCopy() {
        final double[][] inputData = { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 } };
        OpenMapRealMatrix input = new OpenMapRealMatrix(new Array2DRowRealMatrix(inputData));
        OpenMapRealMatrix copy = input.copy();
        assertEquals(input, copy);
    }

    /**
     * Test addition with a matrix of the same size.
     */
    @Test
    public void testAddition() {
        final double[][] inputData1 = { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 } };
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(new Array2DRowRealMatrix(inputData1));
        final double[][] inputData2 = { { 0.5, -1.5, 2.5 }, { -2.0, 1.0, 4.0 } };
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(new Array2DRowRealMatrix(inputData2));
        OpenMapRealMatrix sum = matrix1.add(matrix2);
        double[][] expectedSumData = { { 1.5, 0.5, 5.5 }, { 2.0, 4.0, 10.0 } };
        RealMatrix expectedSum = new Array2DRowRealMatrix(expectedSumData);
        assertEquals(expectedSum, sum);
    }

    /**
     * Test addition with a matrix of different size.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdditionDifferentSizes() {
        final double[][] inputData1 = { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 } };
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(new Array2DRowRealMatrix(inputData1));
        final double[][] inputData2 = { { 1.0, 2.0 } };
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(new Array2DRowRealMatrix(inputData2));
        OpenMapRealMatrix sum = matrix1.add(matrix2);
        fail("Addition with matrices of different sizes should not be possible");
    }

    /**
     * Test subtraction with a matrix of the same size.
     */
    @Test
    public void testSubtraction() {
        final double[][] inputData1 = { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 } };
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(new Array2DRowRealMatrix(inputData1));
        final double[][] inputData2 = { { 0.5, -1.5, 2.5 }, { -2.0, 1.0, 4.0 } };
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(new Array2DRowRealMatrix(inputData2));
        OpenMapRealMatrix difference = matrix1.subtract(matrix2);
        double[][] expectedDifferenceData = { { 0.5, 3.5, 0.5 }, { 6.0, 4.0, 2.0 } };
        RealMatrix expectedDifference = new Array2DRowRealMatrix(expectedDifferenceData);
        assertEquals(expectedDifference, difference);
    }

    /**
     * Test subtraction with a matrix of different size.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSubtractionDifferentSizes() {
        final double[][] inputData1 = { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 } };
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(new Array2DRowRealMatrix(inputData1));
        final double[][] inputData2 = { { 1.0, 2.0 } };
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(new Array2DRowRealMatrix(inputData2));
        OpenMapRealMatrix difference = matrix1.subtract(matrix2);
        fail("Subtraction with matrices of different sizes should not be possible");
    }

    /**
     * Test multiplication with a matrix of the same size.
     */
    @Test
    public void testMultiplication() {
        final double[][] inputData1 = { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 } };
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(new Array2DRowRealMatrix(inputData1));
        final double[][] inputData2 = { { 1.0, 1.0, 1.0 }, { 2.0, 2.0, 2.0 }, { 3.0, 3.0, 3.0 } };
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(new Array2DRowRealMatrix(inputData2));
        OpenMapRealMatrix product = matrix1.multiply(matrix2);
        double[][] expectedProductData = { { 11.0, 13.0, 15.0 }, { 24.0, 29.0, 34.0 }, { 37.0, 45.0, 53.0 } };
        RealMatrix expectedProduct = new Array2DRowRealMatrix(expectedProductData);
        assertEquals(expectedProduct, product);
    }

    /**
     * Test multiplication with a matrix of different size.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMultiplicationDifferentSizes() {
        final double[][] inputData1 = { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 } };
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(new Array2DRowRealMatrix(inputData1));
        final double[][] inputData2 = { { 1.0, 1.0 } };
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(new Array2DRowRealMatrix(inputData2));
        OpenMapRealMatrix product = matrix1.multiply(matrix2);
        fail("Multiplication with matrices of different sizes should not be possible");
    }

    /**
     * Test setting an entry.
     */
    @Test
    public void testSettingEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(1, 1, 2.0);
        assertEquals(1.0, matrix.getEntry(0, 0), Precision.SAFE_EPSILON);
        assertEquals(2.0, matrix.getEntry(1, 1), Precision.SAFE_EPSILON);
    }

    /**
     * Test setting an entry to zero.
     */
    @Test
    public void testSettingEntryToZero() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(1, 1, 2.0);
        matrix.setEntry(0, 0, 0.0);
        assertEquals(0.0, matrix.getEntry(0, 0), Precision.SAFE_EPSILON);
        assertNotEquals(1.0, matrix.getEntry(0, 0), Precision.SAFE_EPSILON);
    }

    /**
     * Test adding an entry.
     */
    @Test
    public void testAddingEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        matrix.addToEntry(0, 0, 1.0);
        matrix.addToEntry(1, 1, 2.0);
        assertEquals(1.0, matrix.getEntry(0, 0), Precision.SAFE_EPSILON);
        assertEquals(2.0, matrix.getEntry(1, 1), Precision.SAFE_EPSILON);
    }

    /**
     * Test adding an entry with a negative value.
     */
    @Test
    public void testAddingEntryNegative() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        matrix.addToEntry(0, 0, 1.0);
        matrix.addToEntry(1, 1, -2.0);
        assertEquals(0.0, matrix.getEntry(0, 0), Precision.SAFE_EPSILON);
        assertEquals(0.0, matrix.getEntry(1, 1), Precision.SAFE_EPSILON);
    }

    /**
     * Test multiplying an entry.
     */
    @Test
    public void testMultiplyingEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        matrix.multiplyEntry(0, 0, 2.0);
        matrix.multiplyEntry(1, 1, 3.0);
        assertEquals(2.0, matrix.getEntry(0, 0), Precision.SAFE_EPSILON);
        assertEquals(3.0, matrix.getEntry(1, 1), Precision.SAFE_EPSILON);
    }

    /**
     * Test multiplying an entry with zero.
     */
    @Test
    public void testMultiplyingEntryZero() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        matrix.multiplyEntry(0, 0, 0.0);
        matrix.multiplyEntry(1, 1, 0.0);
        assertEquals(0.0, matrix.getEntry(0, 0), Precision.SAFE_EPSILON);
        assertEquals(0.0, matrix.getEntry(1, 1), Precision.SAFE_EPSILON);
    }

    /**
     * Test exception when adding an entry with a too large number.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testAddingEntryTooLarge() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        long maxLong = Long.MAX_VALUE;
        matrix.addToEntry(0, 0, maxLong + 1);
    }

    /**
     * Test exception when multiplying an entry with a too large number.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testMultiplyingEntryTooLarge() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        long maxLong = Long.MAX_VALUE;
        matrix.multiplyEntry(0, 0, maxLong + 1);
    }
}