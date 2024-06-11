package org.apache.commons.math3.linear;

import static org.junit.Assert.*;
import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.PrecisionUtils;
import org.junit.Test;

public class SchurTransformerTest {

    @Test(expected = NonSquareMatrixException.class)
    public void testConstructorWithNonSquareMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1}, {2, 3}});
        new SchurTransformer(matrix);
    }

    @Test
    public void testGetP() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {2, 3}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{0.34685472, 0.8660254}, {-0.8660254, 0.34685472}});
        RealMatrix p = transformer.getP();
        assertEquals(expected, p);
    }

    @Test
    public void testGetPT() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {2, 3}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{0.34685472, -0.8660254}, {0.8660254, 0.34685472}});
        RealMatrix pt = transformer.getPT();
        assertEquals(expected, pt);
    }

    @Test
    public void testGetT() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {2, 3}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1, 2}, {2, 3}});
        RealMatrix t = transformer.getT();
        assertEquals(expected, t);
    }

    @Test
    public void testTransformWithIdentityMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 0}, {0, 1}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1, 0}, {0, 1}});
        RealMatrix p = transformer.getP();
        RealMatrix t = transformer.getT();
        RealMatrix pt = transformer.getPT();
        assertEquals(expected, p);
        assertEquals(expected, pt);
        assertEquals(expected, t);
    }

    @Test
    public void testTransformWithUpperTriangularMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2, 0}, {2, 3, 4}, {0, 4, 5}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{0.49289322, -0.36317515, 0},
                {-0.36317515, 0.49289322, 0}, {0, 0, 1}});
        RealMatrix p = transformer.getP();
        RealMatrix t = transformer.getT();
        RealMatrix pt = transformer.getPT();
        assertEquals(expected, p);
        assertEquals(expected, pt);
        assertEquals(expected, t);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testTransformWithNonConvergentMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 1, 1}, {1, 1, 1}, {1, 1, 1}});
        SchurTransformer transformer = new SchurTransformer(matrix);
    }

    @Test
    public void testGetNorm() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {2, 3}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        double expected = 5.0;
        double norm = transformer.getNorm();
        assertEquals(expected, norm, 1e-9);
    }

    @Test
    public void testFindSmallSubDiagonalElement() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2, 0, 0}, {2, 3, 4, 0}, {0, 4, 5, 6},
                {0, 0, 6, 7}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        int il = transformer.findSmallSubDiagonalElement(3, transformer.getNorm());
        assertEquals(3, il);
    }

    @Test
    public void testComputeShift() {
        double x = 1.0;
        double y = 2.0;
        double w = 3.0;
        double exShift = 4.0;
        SchurTransformer.ShiftInfo shiftInfo = new SchurTransformer.ShiftInfo();
        SchurTransformer transformer = new SchurTransformer();
        transformer.computeShift(1, 1, 0, shiftInfo);
        assertEquals(0.0, shiftInfo.x, 1e-9);
        assertEquals(0.0, shiftInfo.w, 1e-9);
        assertEquals(0.0, shiftInfo.y, 1e-9);
        assertEquals(0.0, shiftInfo.exShift, 1e-9);

        shiftInfo.x = x;
        shiftInfo.w = w;
        transformer.computeShift(1, 1, 10, shiftInfo);
        assertEquals(x + exShift, shiftInfo.x, 1e-9);
        assertEquals(w, shiftInfo.w, 1e-9);
        assertEquals(x + exShift, shiftInfo.y, 1e-9);
        assertEquals(exShift, shiftInfo.exShift, 1e-9);

        shiftInfo.x = x;
        shiftInfo.w = w;
        transformer.computeShift(1, 1, 30, shiftInfo);
        assertEquals(0.964, shiftInfo.x, 1e-9);
        assertEquals(0.964, shiftInfo.w, 1e-9);
        assertEquals(0.964, shiftInfo.y, 1e-9);
        assertEquals(0.0, shiftInfo.exShift, 1e-9);
    }

    @Test
    public void testInitQRStep() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2, 0}, {2, 3, 4}, {0, 4, 5}, {0, 0, 6}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        double[] hVec = new double[3];
        int il = transformer.findSmallSubDiagonalElement(3, transformer.getNorm());
        int im = transformer.initQRStep(il, 3, transformer.getShiftInfo(), hVec);
        assertEquals(2, im);
    }

    @Test
    public void testPerformDoubleQRStep() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2, 0}, {2, 3, 4}, {0, 4, 5}, {0, 0, 6}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        double[] hVec = new double[3];
        int il = transformer.findSmallSubDiagonalElement(3, transformer.getNorm());
        int im = transformer.initQRStep(il, 3, transformer.getShiftInfo(), hVec);
        transformer.performDoubleQRStep(il, im, 3, transformer.getShiftInfo(), hVec);

        RealMatrix expectedP = new Array2DRowRealMatrix(new double[][]{{0.34685472, -0.8660254, 0},
                {-0.8660254, 0.34685472, 0}, {0, 0, 1}});
        RealMatrix p = transformer.getP();
        assertEquals(expectedP, p);

        RealMatrix expectedT = new Array2DRowRealMatrix(new double[][]{{1.00019859, -2.00043963, 0.0},
                {-2.00043963, 1.00019859, 0.0}, {0.0, 0.0, 1.0}});
        RealMatrix t = transformer.getT();
        assertEquals(expectedT, t);

        RealMatrix expectedPt = new Array2DRowRealMatrix(new double[][]{{0.34685472, 2.00043963, 0.0},
                {-2.00043963, 0.34685472, 0.0}, {0.0, 0.0, 1.0}});
        RealMatrix pt = transformer.getPT();
        assertEquals(expectedPt, pt);
    }
}