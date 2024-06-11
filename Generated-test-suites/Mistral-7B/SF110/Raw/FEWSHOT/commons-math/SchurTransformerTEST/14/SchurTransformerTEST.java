package org.apache.commons.math3.linear;

import static org.junit.Assert.*;
import org.junit.Test;

import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.PrecisionUtils;
import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

public class SchurTransformerTest {

    @Test
    public void testSchurTransformer_Init() {
        final RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {{1.0, 2.0}, {2.0, 1.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        assertNotNull(transformer.getP());
        assertNotNull(transformer.getPT());
        assertNotNull(transformer.getT());
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testSchurTransformer_NonSquareMatrixException() {
        final RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {{1.0, 2.0}, {2.0}});
        new SchurTransformer(matrix);
    }

    @Test
    public void testGetP() {
        final RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {{1.0, 2.0}, {2.0, 1.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix p = transformer.getP();
        assertEquals(new double[][] {{1.0, 0.0}, {0.0, 1.0}}, p.getData());
    }

    @Test
    public void testGetT() {
        final RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {{1.0, 2.0}, {2.0, 1.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix t = transformer.getT();
        assertEquals(new double[][] {{1.0, 2.0}, {2.0, 3.0}}, t.getData());
    }

    @Test
    public void testGetPT() {
        final RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {{1.0, 2.0}, {2.0, 1.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix pt = transformer.getPT();
        assertEquals(new double[][] {{1.0, 2.0}, {2.0, 1.0}}, pt.getData());
    }

    @Test
    public void testTransform() {
        final RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {{1.0, 2.0}, {2.0, 1.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix expectedP = new Array2DRowRealMatrix(new double[][] {{1.0 / Math.sqrt(2.0), -1.0 / Math.sqrt(2.0)}, {1.0 / Math.sqrt(2.0), 1.0 / Math.sqrt(2.0)}});
        RealMatrix expectedT = new Array2DRowRealMatrix(new double[][] {{1.0, 0.0}, {-1.0 / Math.sqrt(2.0), Math.sqrt(3.0 / 2.0)}});
        RealMatrix p = transformer.getP();
        RealMatrix t = transformer.getT();
        RealMatrix expectedPt = expectedP.transpose();

        PrecisionUtils.assertEquals(expectedP, p, 1e-15);
        PrecisionUtils.assertEquals(expectedT, t, 1e-15);
        PrecisionUtils.assertEquals(expectedPt, transformer.getPT(), 1e-15);
    }

    @Test
    public void testFindSmallSubDiagonalElement() {
        final double[][] matrix = {{1.0, 2.0, 3.0, 4.0}, {2.0, 5.0, 6.0, 7.0}, {3.0, 6.0, 8.0, 9.0}, {4.0, 7.0, 9.0, 10.0}};
        SchurTransformer transformer = new SchurTransformer(new Array2DRowRealMatrix(matrix));
        int expectedIndex = 3; // The smallest sub-diagonal element is in the last column
        int index = transformer.findSmallSubDiagonalElement(expectedIndex, transformer.getNorm());
        assertEquals(expectedIndex, index);

        // Test case where the smallest sub-diagonal element is not found
        double[][] matrix2 = {{1.0, 2.0, 3.0}, {2.0, 1.0, 4.0}, {3.0, 4.0, 5.0}};
        SchurTransformer transformer2 = new SchurTransformer(new Array2DRowRealMatrix(matrix2));
        index = transformer2.findSmallSubDiagonalElement(0, transformer2.getNorm());
        assertEquals(-1, index);
    }

    @Test
    public void testComputeShift() {
        final SchurTransformer transformer = new SchurTransformer(new Array2DRowRealMatrix(new double[][] {{1.0, 2.0}, {2.0, 1.0}}));
        ShiftInfo shift = new ShiftInfo();
        transformer.computeShift(1, 1, 0, shift);

        // Shift values for iteration 1
        assertEquals(1.0, shift.x, 0.0);
        assertEquals(0.0, shift.y, 0.0);
        assertEquals(0.0, shift.w, 0.0);
        assertEquals(0.0, shift.exShift, 0.0);

        transformer.computeShift(1, 1, 10, shift);

        // Shift values for iteration 10
        assertEquals(3.75, shift.x, 0.0);
        assertEquals(3.75, shift.y, 0.0);
        assertEquals(-1.4375, shift.w, 0.0);
        assertEquals(3.75, shift.exShift, 0.0);

        transformer.computeShift(1, 1, 30, shift);

        // Shift values for iteration 30
        assertEquals(-0.4375, shift.x, 0.0);
        assertEquals(-0.4375, shift.y, 0.0);
        assertEquals(-0.4375, shift.w, 0.0);
        assertEquals(-0.4375, shift.exShift, 0.0);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testTransform_MaxCountExceededException() {
        final RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {{1.0, 2.0}, {2.0, 1.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        transformer.transform();
        transformer.transform(); // This should throw MaxCountExceededException
    }
}