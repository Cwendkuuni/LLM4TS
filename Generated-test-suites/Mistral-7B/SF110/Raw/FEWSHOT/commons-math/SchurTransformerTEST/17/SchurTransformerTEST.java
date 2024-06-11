package org.apache.commons.math3.linear;

import static org.junit.Assert.*;
import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

public class SchurTransformerTest {

    @Test(expected = NonSquareMatrixException.class)
    public void testConstructorWithNonSquareMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0}, {2.0, 3.0}});
        new SchurTransformer(matrix);
    }

    @Test
    public void testGetters() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 1.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix p = transformer.getP();
        RealMatrix pt = transformer.getPT();
        RealMatrix t = transformer.getT();
        assertNotNull(p);
        assertNotNull(pt);
        assertNotNull(t);
        assertEquals(p, pt.transpose());
        assertEquals(p.getRowDimension(), p.getColumnDimension());
        assertEquals(t.getRowDimension(), t.getColumnDimension());
        assertEquals(p.getRowDimension(), t.getRowDimension());
    }

    @Test
    public void testTransform() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 1.0, 1.0}, {3.0, 1.0, 1.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix p = transformer.getP();
        RealMatrix pt = transformer.getPT();
        RealMatrix t = transformer.getT();
        assertEquals(p, transformer.getP());
        assertEquals(pt, transformer.getPT());
        assertEquals(t, transformer.getT());
        validateMatrix(p, new double[][]{{0.9999999999999997, -0.009999999999999999, -0.009999999999999999},
                {-0.009999999999999999, 0.9999999999999997, -0.009999999999999999},
                {-0.009999999999999999, -0.009999999999999999, 0.9999999999999997}});
        validateMatrix(pt, new double[][]{{0.9999999999999997, 0.009999999999999999, 0.009999999999999999},
                {0.009999999999999999, 0.9999999999999997, 0.009999999999999999},
                {0.009999999999999999, 0.009999999999999999, 0.9999999999999997}});
        validateMatrix(t, new double[][]{{1.0, -1.9999999999999997, -0.9999999999999998},
                {-1.9999999999999997, 2.9999999999999986, 0.9999999999999998},
                {-0.9999999999999998, 0.9999999999999998, 1.9999999999999993}});
    }

    @Test
    public void testGetNorm() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 1.0, 1.0}, {3.0, 1.0, 1.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        double norm = transformer.getNorm();
        double expectedNorm = 13.25;
        assertEquals(expectedNorm, norm, 1e-10);
    }

    @Test
    public void testFindSmallSubDiagonalElement() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0, 4.0},
                {2.0, 1.0, 1.0, 1.0}, {3.0, 1.0, 1.0, 1.0}, {4.0, 1.0, 1.0, 1.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        int index = transformer.findSmallSubDiagonalElement(3, transformer.getNorm());
        int expectedIndex = 2;
        assertEquals(expectedIndex, index);
    }

    @Test
    public void testComputeShift() {
        SchurTransformer transformer = new SchurTransformer(MatrixUtils.createRealMatrix(new double[][]{{0.1, 0.2}, {0.2, 0.1}}));
        ShiftInfo shift = new ShiftInfo();
        transformer.computeShift(0, 0, 0, shift);
        double expectedX = 0.1;
        double expectedW = 0.0;
        double expectedY = 0.1;
        double expectedExShift = 0.1;
        assertEquals(expectedX, shift.x, 1e-10);
        assertEquals(expectedW, shift.w, 1e-10);
        assertEquals(expectedY, shift.y, 1e-10);
        assertEquals(expectedExShift, shift.exShift, 1e-10);

        transformer.computeShift(0, 0, 10, shift);
        expectedX = 0.75 * (FastMath.abs(transformer.getT()[0][0]) + FastMath.abs(transformer.getT()[1][1]));
        expectedW = -0.4375 * (FastMath.abs(transformer.getT()[0][0]) + FastMath.abs(transformer.getT()[1][1]) * FastMath.abs(transformer.getT()[1][1]));
        expectedY = expectedX;
        assertEquals(expectedX, shift.x, 1e-10);
        assertEquals(expectedW, shift.w, 1e-10);
        assertEquals(expectedY, shift.y, 1e-10);

        transformer.computeShift(0, 0, 30, shift);
        double s = (shift.y - shift.x) / 2.0;
        expectedX = s;
        expectedW = -0.4375 * s * s;
        assertEquals(expectedX, shift.x, 1e-10);
        assertEquals(expectedW, shift.w, 1e-10);
    }

    @Test
    public void testInitQRStep() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 1.0, 1.0}, {3.0, 1.0, 1.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        double[] hVec = new double[3];
        int index = transformer.initQRStep(2, 4, new ShiftInfo(), hVec);
        int expectedIndex = 2;
        assertEquals(expectedIndex, index);
    }

    @Test
    public void testPerformDoubleQRStep() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 1.0, 1.0}, {3.0, 1.0, 1.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        double[] hVec = new double[3];
        int il = 2, im = 2, iu = 4, iteration = 0;
        ShiftInfo shift = new ShiftInfo();
        transformer.performDoubleQRStep(il, im, iu, shift, hVec);
        validateMatrix(transformer.getT(), new double[][]{{1.0, -1.9999999999999997, -0.9999999999999998},
                {-1.9999999999999997, 2.9999999999999986, 0.9999999999999998},
                {-0.9999999999999998, 0.9999999999999998, 1.9999999999999993}});

        transformer.performDoubleQRStep(il, im, iu, shift, hVec);
        validateMatrix(transformer.getT(), new double[][]{{1.0, -1.9999999999999997, -0.9999999999999998},
                {-1.9999999999999997, 2.9999999999999986, 0.9999999999999998},
                {-0.9999999999999998, 0.9999999999999998, 1.9999999999999993}});

        transformer.performDoubleQRStep(il, im, iu, shift, hVec);
        validateMatrix(transformer.getT(), new double[][]{{1.0, -1.9999999999999997, -0.9999999999999998},
                {-1.9999999999999997, 2.9999999999999986, 0.9999999999999998},
                {-0.9999999999999998, 0.9999999999999998, 1.9999999999999993}});
    }

    private void validateMatrix(RealMatrix matrix, double[][] expected) {
        for (int i = 0; i < matrix.getRowDimension(); i++) {
            for (int j = 0; j < matrix.getColumnDimension(); j++) {
                double actual = matrix.getEntry(i, j);
                double expectedValue = expected[i][j];
                assertEquals(expectedValue, actual, 1e-10);
            }
        }
    }
}