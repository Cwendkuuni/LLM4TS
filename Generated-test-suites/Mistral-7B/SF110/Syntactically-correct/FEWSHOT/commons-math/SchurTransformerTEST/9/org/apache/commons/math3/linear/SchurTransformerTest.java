package org.apache.commons.math3.linear;

import static org.junit.Assert.*;
import org.junit.Test;

import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.PrecisionUtils;
import org.apache.commons.math3.util.MathUtils;

public class SchurTransformerTest {

    @Test(expected = NonSquareMatrixException.class)
    public void testSchurTransformer_NonSquareMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0}, {2.0, 3.0}});
        new SchurTransformer(matrix);
    }

    @Test
    public void testGetP() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 1.0, 4.0}, {3.0, 4.0, 1.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix expectedP = MatrixUtils.createRealMatrix(new double[][]{{0.725452651246563, -0.622053347253823, 0.323762836535862},
                {-0.622053347253823, 0.725452651246563, -0.323762836535862},
                {0.323762836535862, -0.323762836535862, 0.816496580927726}});
        RealMatrix p = transformer.getP();
        assertEquals(expectedP, p);
    }

    @Test
    public void testGetPT() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 1.0, 4.0}, {3.0, 4.0, 1.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix expectedPT = MatrixUtils.createRealMatrix(new double[][]{{0.725452651246563, -0.622053347253823, 0.323762836535862},
                {-0.622053347253823, 0.725452651246563, -0.323762836535862},
                {0.323762836535862, -0.323762836535862, 0.816496580927726}});
        RealMatrix pt = transformer.getPT();
        assertEquals(expectedPT, pt);
    }

    @Test
    public void testGetT() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 1.0, 4.0}, {3.0, 4.0, 1.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix expectedT = MatrixUtils.createRealMatrix(new double[][]{{-0.564188835836729, 0.816496580927726, 0.323762836535862},
                {-0.816496580927726, -0.564188835836729, -0.323762836535862},
                {-0.323762836535862, -0.323762836535862, 0.564188835836729}});
        RealMatrix t = transformer.getT();
        assertEquals(expectedT, t);
    }

    @Test
    public void testTransform() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{4.0, 1.0, 1.0}, {1.0, 4.0, 1.0}, {1.0, 1.0, 4.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix expectedP = MatrixUtils.createRealMatrix(new double[][]{{0.707106781186548, -0.707106781186548, 0.0},
                {-0.707106781186548, 0.707106781186548, 0.0},
                {0.0, 0.0, 1.0}});
        RealMatrix expectedT = MatrixUtils.createRealMatrix(new double[][]{{-1.0, 0.0, 0.0}, {0.0, 3.0, 0.0}, {0.0, 0.0, 1.0}});
        RealMatrix expectedPT = MatrixUtils.createRealMatrix(new double[][]{{0.707106781186548, 0.707106781186548, 0.0},
                {-0.707106781186548, -0.707106781186548, 0.0},
                {0.0, 0.0, 1.0}});

        RealMatrix p = transformer.getP();
        RealMatrix t = transformer.getT();
        RealMatrix pt = transformer.getPT();

        assertEquals(expectedP, p);
        assertEquals(expectedT, t);
        assertEquals(expectedPT, pt);
    }

    @Test
    public void testGetNorm() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{4.0, 1.0, 1.0}, {1.0, 4.0, 1.0}, {1.0, 1.0, 4.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        double expectedNorm = 23.0353352176063;
        double norm = transformer.getNorm();
        assertEquals(expectedNorm, norm, 1e-13);
    }

    @Test
    public void testFindSmallSubDiagonalElement() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{4.0, 1.0, 1.0}, {1.0, 4.0, 1.0}, {1.0, 1.0, 4.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        int expectedIndex = 2;
        int index = transformer.findSmallSubDiagonalElement(2, transformer.getNorm());
        assertEquals(expectedIndex, index);
    }

    @Test
    public void testComputeShift() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{4.0, 1.0, 1.0}, {1.0, 4.0, 1.0}, {1.0, 1.0, 4.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        ShiftInfo shift = new ShiftInfo();
        int iteration = 1;
        transformer.computeShift(5, 5, iteration, shift);
        double expectedX = 3.4355969064721;
        double expectedY = 3.4355969064721;
        double expectedW = -1.3271952129326;
        double expectedExShift = 3.4355969064721;
        assertEquals(expectedX, shift.x, 1e-13);
        assertEquals(expectedY, shift.y, 1e-13);
        assertEquals(expectedW, shift.w, 1e-13);
        assertEquals(expectedExShift, shift.exShift, 1e-13);

        iteration = 10;
        transformer.computeShift(5, 5, iteration, shift);
        double expectedShiftX = 0.964;
        double expectedShiftY = 0.964;
        double expectedShiftW = -0.4375 * Math.pow(0.964, 2.0);
        assertEquals(expectedShiftX, shift.x, 1e-13);
        assertEquals(expectedShiftY, shift.y, 1e-13);
        assertEquals(expectedShiftW, shift.w, 1e-13);

        iteration = 30;
        transformer.computeShift(5, 5, iteration, shift);
        double expectedS = Math.sqrt(1.1324 * 1.1324 + 1.1324 * 1.1324);
        double expectedZ = 1.0 / (2.0 * Math.sqrt(1.1324 * 1.1324 + 1.1324 * 1.1324) + 0.964);
        assertEquals(expectedS, shift.x, 1e-13);
        assertEquals(expectedZ, shift.y, 1e-13);
        assertEquals(expectedShiftW, shift.w, 1e-13);
        assertEquals(expectedExShift + shift.x, shift.exShift, 1e-13);
    }

    @Test
    public void testInitQRStep() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{4.0, 1.0, 1.0}, {1.0, 4.0, 1.0}, {1.0, 1.0, 4.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        double[] hVec = new double[3];
        int expectedIndex = 2;
        int index = transformer.initQRStep(5, 5, new ShiftInfo(), hVec);
        assertEquals(expectedIndex, index);
    }

    @Test
    public void testPerformDoubleQRStep() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{4.0, 1.0, 1.0}, {1.0, 4.0, 1.0}, {1.0, 1.0, 4.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        double[] hVec = new double[3];
        int il = 5, im = 5, iu = 5;
        ShiftInfo shift = new ShiftInfo();
        transformer.initQRStep(5, 5, shift, hVec);
        transformer.performDoubleQRStep(il, im, iu, shift, hVec);

        double[] expectedMatrixT = new double[]{
                {-0.564188835836729, 0.816496580927726, 0.323762836535862},
                {-0.816496580927726, -0.564188835836729, -0.323762836535862},
                {-0.323762836535862, -0.323762836535862, 0.564188835836729}};
        for (int i = 0; i < transformer.matrixT.length; i++) {
            assertArrayEquals(expectedMatrixT[i], transformer.matrixT[i], 1e-13);
        }
    }
}