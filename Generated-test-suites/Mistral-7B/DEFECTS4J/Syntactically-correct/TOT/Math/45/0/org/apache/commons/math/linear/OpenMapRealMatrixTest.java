package org.apache.commons.math.linear;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MatrixDimensionMismatchException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.junit.Test;
import static org.junit.Assert.*;

public class OpenMapRealMatrixTest {

    @Test
    public void testCreation() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        assertEquals(3, matrix.getRowDimension());
        assertEquals(3, matrix.getColumnDimension());
        assertEquals(0, matrix.getNumberElements());
    }

    @Test
    public void testSettingValues() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(1, 1, 2.0);
        matrix.setEntry(2, 2, 3.0);
        assertEquals(3, matrix.getNumberElements());
        assertEquals(1.0, matrix.getEntry(0, 0), 0.001);
        assertEquals(2.0, matrix.getEntry(1, 1), 0.001);
        assertEquals(3.0, matrix.getEntry(2, 2), 0.001);
    }

    @Test(expected = MatrixDimensionMismatchException.class)
    public void testSettingValuesWrongDimension() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(3, 0, 1.0);
    }

    @Test
    public void testAddition() {
        OpenMapRealMatrix matrixA = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrixB = new OpenMapRealMatrix(3, 3);

        matrixA.setEntry(0, 0, 1.0);
        matrixA.setEntry(0, 1, 2.0);
        matrixA.setEntry(1, 0, 3.0);
        matrixA.setEntry(1, 1, 4.0);

        matrixB.setEntry(0, 0, 5.0);
        matrixB.setEntry(0, 1, 6.0);
        matrixB.setEntry(1, 0, 7.0);
        matrixB.setEntry(1, 1, 8.0);

        OpenMapRealMatrix matrixC = matrixA.add(matrixB);

        assertEquals(3, matrixC.getRowDimension());
        assertEquals(3, matrixC.getColumnDimension());
        assertEquals(6, matrixC.getNumberElements());

        assertEquals(6.0, matrixC.getEntry(0, 0), 0.001);
        assertEquals(8.0, matrixC.getEntry(0, 1), 0.001);
        assertEquals(10.0, matrixC.getEntry(1, 0), 0.001);
        assertEquals(12.0, matrixC.getEntry(1, 1), 0.001);
        assertEquals(1.0, matrixC.getEntry(2, 0), 0.001);
        assertEquals(4.0, matrixC.getEntry(2, 1), 0.001);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testAdditionDifferentDimensions() {
        OpenMapRealMatrix matrixA = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrixB = new OpenMapRealMatrix(2, 3);
        matrixA.add(matrixB);
    }

    @Test
    public void testMultiply() {
        OpenMapRealMatrix matrixA = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrixB = new OpenMapRealMatrix(3, 3);

        matrixA.setEntry(0, 0, 1.0);
        matrixA.setEntry(0, 1, 2.0);
        matrixA.setEntry(1, 0, 3.0);
        matrixA.setEntry(1, 1, 4.0);

        matrixB.setEntry(0, 0, 5.0);
        matrixB.setEntry(0, 1, 6.0);
        matrixB.setEntry(1, 0, 7.0);
        matrixB.setEntry(1, 1, 8.0);

        OpenMapRealMatrix matrixC = matrixA.multiply(matrixB);

        assertEquals(3, matrixC.getRowDimension());
        assertEquals(3, matrixC.getColumnDimension());
        assertEquals(9, matrixC.getNumberElements());

        assertEquals(35.0, matrixC.getEntry(0, 0), 0.001);
        assertEquals(43.0, matrixC.getEntry(0, 1), 0.001);
        assertEquals(41.0, matrixC.getEntry(0, 2), 0.001);
        assertEquals(53.0, matrixC.getEntry(1, 0), 0.001);
        assertEquals(68.0, matrixC.getEntry(1, 1), 0.001);
        assertEquals(66.0, matrixC.getEntry(1, 2), 0.001);
        assertEquals(39.0, matrixC.getEntry(2, 0), 0.001);
        assertEquals(50.0, matrixC.getEntry(2, 1), 0.001);
        assertEquals(48.0, matrixC.getEntry(2, 2), 0.001);
    }

    @Test(expected = MatrixDimensionMismatchException.class)
    public void testMultiplyDifferentDimensions() {
        OpenMapRealMatrix matrixA = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrixB = new OpenMapRealMatrix(2, 2);
        matrixA.multiply(matrixB);
    }

    @Test
    public void testExceptionHandling() {
        OpenMapRealMatrix matrixA = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrixB = new OpenMapRealMatrix(3, 4);

        try {
            matrixA.add(matrixB);
        } catch (MatrixDimensionMismatchException e) {
            assertEquals("Arguments for add must have same dimensions", e.getMessage());
            return;
        }

        fail("Expected MatrixDimensionMismatchException");
    }

    @Test
    public void testGetNumberElements() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        assertEquals(0, matrix.getNumberElements());

        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(0, 1, 2.0);
        matrix.setEntry(1, 0, 3.0);
        matrix.setEntry(1, 1, 4.0);

        assertEquals(4, matrix.getNumberElements());
    }

    @Test
    public void testCopyConstructor() {
        OpenMapRealMatrix original = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix copy = new OpenMapRealMatrix(original);

        original.setEntry(0, 0, 1.0);
        original.setEntry(0, 1, 2.0);
        original.setEntry(1, 0, 3.0);
        original.setEntry(1, 1, 4.0);

        assertEquals(original.getEntry(0, 0), copy.getEntry(0, 0));
        assertEquals(original.getEntry(0, 1), copy.getEntry(0, 1));
        assertEquals(original.getEntry(1, 0), copy.getEntry(1, 0));
        assertEquals(original.getEntry(1, 1), copy.getEntry(1, 1));
    }

    @Test
    public void testSubtraction() {
        OpenMapRealMatrix matrixA = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrixB = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrixC = new OpenMapRealMatrix(3, 3);

        matrixA.setEntry(0, 0, 1.0);
        matrixA.setEntry(0, 1, 2.0);
        matrixA.setEntry(1, 0, 3.0);
        matrixA.setEntry(1, 1, 4.0);

        matrixB.setEntry(0, 0, 1.0);
        matrixB.setEntry(0, 1, 2.0);
        matrixB.setEntry(1, 0, 3.0);
        matrixB.setEntry(1, 1, 4.0);

        matrixC = matrixA.subtract(matrixB);

        assertEquals(3, matrixC.getRowDimension());
        assertEquals(3, matrixC.getColumnDimension());
        assertEquals(0, matrixC.getNumberElements());

        assertEquals(0.0, matrixC.getEntry(0, 0), 0.001);
        assertEquals(0.0, matrixC.getEntry(0, 1), 0.001);
        assertEquals(0.0, matrixC.getEntry(1, 0), 0.001);
        assertEquals(0.0, matrixC.getEntry(1, 1), 0.001);
    }

    @Test
    public void testNegativeMatrix() {
        OpenMapRealMatrix original = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix negative = new OpenMapRealMatrix(original);

        original.setEntry(0, 0, 1.0);
        original.setEntry(0, 1, 2.0);
        original.setEntry(1, 0, 3.0);
        original.setEntry(1, 1, 4.0);

        negative.negateAll();

        assertEquals(-1.0, negative.getEntry(0, 0), 0.001);
        assertEquals(-2.0, negative.getEntry(0, 1), 0.001);
        assertEquals(-3.0, negative.getEntry(1, 0), 0.001);
        assertEquals(-4.0, negative.getEntry(1, 1), 0.001);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testSettingTooLargeValue() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(0, 0, Double.MAX_VALUE + 1);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testMultiplyingTooLargeValues() {
        OpenMapRealMatrix matrixA = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrixB = new OpenMapRealMatrix(3, 3);

        matrixA.setEntry(0, 0, Double.MAX_VALUE);
        matrixB.setEntry(0, 0, Double.MAX_VALUE);
        matrixA.multiply(matrixB);
    }
}