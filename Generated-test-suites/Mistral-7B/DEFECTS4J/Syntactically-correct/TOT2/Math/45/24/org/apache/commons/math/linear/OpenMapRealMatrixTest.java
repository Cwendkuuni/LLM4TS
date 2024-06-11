package org.apache.commons.math.linear;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class OpenMapRealMatrixTest {

    @Test
    public void testConstructor() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        Assert.assertEquals(2, matrix.getRowDimension());
        Assert.assertEquals(3, matrix.getColumnDimension());
        Assert.assertEquals(0, matrix.getSize());
    }

    @Test
    public void testCopyConstructor() {
        OpenMapRealMatrix original = new OpenMapRealMatrix(2, 3);
        OpenMapRealMatrix copy = new OpenMapRealMatrix(original);
        Assert.assertEquals(original.getRowDimension(), copy.getRowDimension());
        Assert.assertEquals(original.getColumnDimension(), copy.getColumnDimension());
        Assert.assertEquals(original.getSize(), copy.getSize());
    }

    @Test(expected = DimensionMismatchException.class)
    public void testAddWithIncompatibleMatrix() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(1, 1);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);
        matrix1.add(matrix2);
    }

    @Test
    public void testAdd() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(0, 1, 2.0);
        matrix2.setEntry(0, 0, 3.0);
        matrix2.setEntry(0, 1, 4.0);
        matrix1.add(matrix2);
        Assert.assertEquals(1.0 + 3.0, matrix1.getEntry(0, 0), 1e-13);
        Assert.assertEquals(2.0 + 4.0, matrix1.getEntry(0, 1), 1e-13);
    }

    @Test
    public void testSubtract() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(0, 1, 2.0);
        matrix2.setEntry(0, 0, 3.0);
        matrix2.setEntry(0, 1, 4.0);
        matrix1.subtract(matrix2);
        Assert.assertEquals(-2.0, matrix1.getEntry(0, 0), 1e-13);
        Assert.assertEquals(-2.0, matrix1.getEntry(0, 1), 1e-13);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testMultiplyWithIncompatibleMatrix() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(1, 2);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 3);
        matrix1.multiply(matrix2);
    }

    @Test
    public void testMultiply() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 3);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(0, 1, 2.0);
        matrix2.setEntry(0, 0, 3.0);
        matrix2.setEntry(0, 1, 4.0);
        matrix2.setEntry(0, 2, 5.0);
        OpenMapRealMatrix result = matrix1.multiply(matrix2);
        Assert.assertEquals(3.0 * 1.0, result.getEntry(0, 0), 1e-13);
        Assert.assertEquals(3.0 * 2.0, result.getEntry(0, 1), 1e-13);
        Assert.assertEquals(3.0 * 5.0, result.getEntry(0, 2), 1e-13);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testSetEntryWithTooLargeValue() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(1, 1);
        matrix.setEntry(0, 0, Double.POSITIVE_INFINITY);
    }
}