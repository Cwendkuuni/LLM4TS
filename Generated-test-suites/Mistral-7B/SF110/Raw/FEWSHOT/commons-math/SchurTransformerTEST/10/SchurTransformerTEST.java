package org.apache.commons.math3.linear;

import static org.junit.Assert.*;
import org.junit.Test;

import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.PrecisionUtils;
import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.util.LocalizedFormats;

public class SchurTransformerTest {

    @Test(expected = NonSquareMatrixException.class)
    public void testSchurTransformerWithNonSquareMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{
            {1.0, 2.0},
            {3.0, 4.0, 5.0}});
        new SchurTransformer(matrix);
    }

    @Test
    public void testGetters() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{
            {1.0, 2.0},
            {2.0, 1.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix p = transformer.getP();
        RealMatrix pt = transformer.getPT();
        RealMatrix t = transformer.getT();
        assertNotNull(p);
        assertNotNull(pt);
        assertNotNull(t);
        assertEquals(p.getRowDimension(), p.getColumnDimension());
        assertEquals(p.getRowDimension(), t.getRowDimension());
        assertEquals(p.getRowDimension(), t.getColumnDimension());
        assertEquals(t.getRowDimension(), t.getColumnDimension());
        assertEquals(pt.getRowDimension(), pt.getColumnDimension());
        assertEquals(pt.getRowDimension(), pt.getColumnDimension());
    }

    @Test
    public void testTransform() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{
            {1.0, 2.0},
            {2.0, 1.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix expectedT = MatrixUtils.createRealMatrix(new double[][]{
            {4.1321918803372125E-17, 1.9999999999999995},
            {1.9999999999999995, 4.0},
            {1.9999999999999995, -1.9999999999999995},
            {-1.9999999999999995, 1.9999999999999995},
            {1.0, 0.0},
            {0.0, 0.0}});
        RealMatrix expectedP = MatrixUtils.createRealMatrix(new double[][]{
            {1.0, 0.0},
            {0.0, 1.0}});
        RealMatrix actualT = transformer.getT();
        RealMatrix actualP = transformer.getP();
        assertEquals(expectedT, actualT, 1e-14);
        assertEquals(expectedP, actualP, 1e-14);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testTransformWithMaxCountExceeded() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{
            {1.0, 1.0},
            {1.0, 1.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
    }

    @Test
    public void testGetNorm() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{
            {1.0, 2.0},
            {2.0, 1.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        double actualNorm = transformer.getNorm();
        double expectedNorm = 5.0;
        assertEquals(expectedNorm, actualNorm, 1e-14);
    }

    @Test
    public void testFindSmallSubDiagonalElement() {
        RealMatrix matrix = MatrixUtils
                .createRealMatrix(new double[][]{
                        {1.0, 2.0, 3.0, 4.0, 5.0},
                        {2.0, 1.0, 1.5, 1.0, 0.5},
                        {3.0, 1.5, 1.0, 1.0, 1.0},
                        {4.0, 1.0, 1.0, 1.0, 1.0},
                        {5.0, 0.5, 1.0, 1.0, 1.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        int actualIl = transformer.findSmallSubDiagonalElement(4, transformer.getNorm());
        int expectedIl = 3;
        assertEquals(expectedIl, actualIl);
    }

    @Test
    public void testComputeShift1() {
        double x = 2.0;
        double y = 1.0;
        double w = 0.0;
        int iteration = 10;
        ShiftInfo expectedShiftInfo = new ShiftInfo();
        expectedShiftInfo.x = 1.5;
        expectedShiftInfo.y = 1.5;
        expectedShiftInfo.w = -0.75;
        expectedShiftInfo.exShift = 1.5;
        ShiftInfo actualShiftInfo = new SchurTransformer().computeShift(2, 2, iteration, new ShiftInfo());
        assertEquals(expectedShiftInfo.x, actualShiftInfo.x, 1e-14);
        assertEquals(expectedShiftInfo.y, actualShiftInfo.y, 1e-14);
        assertEquals(expectedShiftInfo.w, actualShiftInfo.w, 1e-14);
        assertEquals(expectedShiftInfo.exShift, actualShiftInfo.exShift, 1e-14);
    }

    @Test
    public void testComputeShift2() {
        double x = 2.0;
        double y = 1.0;
        double w = 0.0;
        int iteration = 30;
        ShiftInfo expectedShiftInfo = new ShiftInfo();
        expectedShiftInfo.x = 1.2;
        expectedShiftInfo.y = 1.2;
        expectedShiftInfo.w = -0.6;
        ShiftInfo actualShiftInfo = new SchurTransformer().computeShift(2, 2, iteration, new ShiftInfo());
        assertEquals(expectedShiftInfo.x, actualShiftInfo.x, 1e-14);
        assertEquals(expectedShiftInfo.y, actualShiftInfo.y, 1e-14);
        assertEquals(expectedShiftInfo.w, actualShiftInfo.w, 1e-14);
    }

    @Test
    public void testInitQRStep() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{
            {1.0, 2.0, 3.0},
            {2.0, 1.0, 1.5},
            {3.0, 1.5, 1.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        double[] hVec = new double[3];
        int actualIm = transformer.initQRStep(2, 4, new ShiftInfo(), hVec);
        int expectedIm = 1;
        assertEquals(expectedIm, actualIm);
        double actualHVec0 = hVec[0];
        double expectedHVec0 = 2.5;
        assertEquals(expectedHVec0, actualHVec0, 1e-14);
        double actualHVec1 = hVec[1];
        double expectedHVec1 = -1.0;
        assertEquals(expectedHVec1, actualHVec1, 1e-14);
        double actualHVec2 = hVec[2];
        double expectedHVec2 = 1.5;
        assertEquals(expectedHVec2, actualHVec2, 1e-14);
    }

    @Test
    public void testPerformDoubleQRStep() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{
            {1.0, 2.0, 3.0},
            {2.0, 1.0, 1.5},
            {3.0, 1.5, 1.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        double[] hVec = new double[3];
        int il = 2;
        int im = 1;
        int iu = 4;
        int iteration = 1;
        ShiftInfo shift = new ShiftInfo();
        transformer.initQRStep(il, iu, shift, hVec);
        transformer.performDoubleQRStep(il, im, iu, shift, hVec);
        RealMatrix expectedT = MatrixUtils.createRealMatrix(new double[][]{
            {1.0, 2.0, 3.0},
            {1.7320508075688772E-17, -0.9999999999999998},
            {1.7320508075688772E-17, 1.5},
            {1.5, -0.9999999999999998},
            {0.0, 0.9999999999999998},
            {0.0, 0.0}});
        RealMatrix actualT = transformer.getT();
        assertEquals(expectedT, actualT, 1e-14);
    }
}