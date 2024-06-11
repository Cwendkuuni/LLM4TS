package org.apache.commons.math3.linear;

import static org.junit.Assert.*;
import org.junit.Test;

import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.special.FastMath;

public class SchurTransformerTest {

    @Test(expected = NonSquareMatrixException.class)
    public void testInitializeWithNonSquareMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0}, {2.0, 3.0}});
        new SchurTransformer(matrix);
    }

    @Test
    public void testGetters() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix p = transformer.getP();
        RealMatrix pt = transformer.getPT();
        RealMatrix t = transformer.getT();
        assertEquals(p, pt.transpose());
        assertNotSame(p, t);
    }

    @Test
    public void testTransform() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 5.0}, {3.0, 5.0, 6.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix p = transformer.getP();
        RealMatrix t = transformer.getT();
        RealMatrix expectedP = new Array2DRowRealMatrix(new double[][]{{1.0 / FastMath.sqrt(2.0), (3.0 - 4.0) / (2.0 * FastMath.sqrt(2.0)), (5.0 - 3.0) / (2.0 * FastMath.sqrt(6.0))},
                {-(3.0 - 4.0) / (2.0 * FastMath.sqrt(2.0)), 1.0 / FastMath.sqrt(2.0), (2.0 * (5.0 - 3.0) - (3.0 - 4.0) * (5.0 - 3.0)) / (2.0 * FastMath.sqrt(6.0 * 6.0))},
                {-(5.0 - 3.0) / (2.0 * FastMath.sqrt(6.0)), -(2.0 * (5.0 - 3.0) - (3.0 - 4.0) * (5.0 - 3.0)) / (2.0 * FastMath.sqrt(6.0 * 6.0)), 1.0}});
        RealMatrix expectedT = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}});

        assertEquals(expectedP, p);
        assertEquals(expectedT, t);
    }

    @Test
    public void testFindSmallSubDiagonalElement() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0, 4.0}, {2.0, 4.0, 5.0, 6.0}, {3.0, 5.0, 6.0, 7.0}, {4.0, 6.0, 7.0, 8.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);

        int expected = 0;
        for (int i = 0; i < matrix.getRowDimension(); ++i) {
            int actual = transformer.findSmallSubDiagonalElement(i, transformer.getNorm());
            assertEquals(expected, actual);
            ++expected;
        }
    }

    @Test
    public void testComputeShift() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0, 4.0}, {2.0, 4.0, 5.0, 6.0}, {3.0, 5.0, 6.0, 7.0}, {4.0, 6.0, 7.0, 8.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        ShiftInfo shift = new SchurTransformer.ShiftInfo();

        transformer.computeShift(0, 0, 0, shift);
        assertEquals(1.0, shift.x, 0.0);
        assertEquals(0.0, shift.y, 0.0);
        assertEquals(0.0, shift.w, 0.0);
        assertEquals(0.0, shift.exShift, 0.0);

        transformer.computeShift(1, 1, 0, shift);
        assertEquals(5.0, shift.x, 0.0);
        assertEquals(4.0, shift.y, 0.0);
        assertEquals(2.0 * (5.0 * 5.0 + 4.0 * 4.0), shift.w, 0.0);
        assertEquals(0.0, shift.exShift, 0.0);

        transformer.computeShift(1, 1, 10, shift);
        assertEquals(2.0 * FastMath.sqrt(2.0 * 5.0 * 5.0 + 4.0 * 4.0), shift.x, 0.0);
        assertEquals(2.0 * FastMath.sqrt(2.0 * 5.0 * 5.0 + 4.0 * 4.0), shift.y, 0.0);
        assertEquals(-0.4375 * (2.0 * FastMath.sqrt(2.0 * 5.0 * 5.0 + 4.0 * 4.0) * (2.0 * FastMath.sqrt(2.0 * 5.0 * 5.0 + 4.0 * 4.0))), shift.w, 0.0);
        assertEquals(0.964, shift.exShift, 0.0);

        transformer.computeShift(1, 1, 30, shift);
        assertEquals(2.0 * FastMath.sqrt(2.0 * 5.0 * 5.0 + 4.0 * 4.0), shift.x, 0.0);
        assertEquals(2.0 * FastMath.sqrt(2.0 * 5.0 * 5.0 + 4.0 * 4.0), shift.y, 0.0);
        assertEquals(-0.4375 * (2.0 * FastMath.sqrt(2.0 * 5.0 * 5.0 + 4.0 * 4.0) * (2.0 * FastMath.sqrt(2.0 * 5.0 * 5.0 + 4.0 * 4.0))), shift.w, 0.0);
        assertEquals(0.964, shift.exShift, 0.0);

        transformer.computeShift(1, 1, 100, shift);
        fail("Should have thrown MaxCountExceededException");
    }

    @Test
    public void testInitQRStep() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0, 4.0}, {2.0, 4.0, 5.0, 6.0}, {3.0, 5.0, 6.0, 7.0}, {4.0, 6.0, 7.0, 8.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        double[] hVec = new double[3];

        int expected = 1;
        int actual = transformer.initQRStep(0, 3, new SchurTransformer.ShiftInfo(), hVec);
        assertEquals(expected, actual);

        expected = 2;
        actual = transformer.initQRStep(1, 3, new SchurTransformer.ShiftInfo(), hVec);
        assertEquals(expected, actual);

        expected = 2;
        actual = transformer.initQRStep(2, 3, new SchurTransformer.ShiftInfo(), hVec);
        assertEquals(expected, actual);

        expected = 1;
        actual = transformer.initQRStep(3, 3, new SchurTransformer.ShiftInfo(), hVec);
        assertEquals(expected, actual);
    }

    @Test
    public void testPerformDoubleQRStep() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0, 4.0}, {2.0, 4.0, 5.0, 6.0}, {3.0, 5.0, 6.0, 7.0}, {4.0, 6.0, 7.0, 8.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        double[] hVec = new double[3];
        int im = transformer.initQRStep(1, 3, new SchurTransformer.ShiftInfo(), hVec);

        transformer.performDoubleQRStep(1, im, 3, new SchurTransformer.ShiftInfo(), hVec);

        RealMatrix expectedP = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0, 4.0}, {0.0, 1.0 / FastMath.sqrt(2.0), (3.0 - 4.0) / (2.0 * FastMath.sqrt(2.0)), (5.0 - 3.0) / (2.0 * FastMath.sqrt(6.0))},
                {0.0, -(3.0 - 4.0) / (2.0 * FastMath.sqrt(2.0)), 1.0 / FastMath.sqrt(2.0), (2.0 * (5.0 - 3.0) - (3.0 - 4.0) * (5.0 - 3.0)) / (2.0 * FastMath.sqrt(6.0 * 6.0))},
                {0.0, -(5.0 - 3.0) / (2.0 * FastMath.sqrt(6.0)), -(2.0 * (5.0 - 3.0) - (3.0 - 4.0) * (5.0 - 3.0)) / (2.0 * FastMath.sqrt(6.0 * 6.0)), 1.0}});
        RealMatrix expectedT = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}});

        RealMatrix p = transformer.getP();
        RealMatrix t = transformer.getT();

        assertEquals(expectedP, p);
        assertEquals(expectedT, t);
    }
}