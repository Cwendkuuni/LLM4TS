package org.apache.commons.math3.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.PrecisionUtils;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.ShiftNotPositiveException;
import org.junit.Test;

public class SchurTransformerTest {

    @Test(expected = NonSquareMatrixException.class)
    public void testConstructorWithNonSquareMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {3, 4, 5}});
        new SchurTransformer(matrix);
    }

    @Test
    public void testGetP() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {2, 3}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix expectedP = new Array2DRowRealMatrix(new double[][]{{0.342020142555704, -0.615422834050112},
                {-0.615422834050112, 0.342020142555704}});
        RealMatrix actualP = transformer.getP();
        PrecisionUtils.assertEquals(expectedP, actualP, 1e-10);
    }

    @Test
    public void testGetPT() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {2, 3}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix expectedPT = new Array2DRowRealMatrix(new double[][]{{0.342020142555704, 0.615422834050112},
                {-0.615422834050112, 0.342020142555704}});
        RealMatrix actualPT = transformer.getPT();
        PrecisionUtils.assertEquals(expectedPT, actualPT, 1e-10);
    }

    @Test
    public void testGetT() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {2, 3}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix expectedT = new Array2DRowRealMatrix(new double[][]{{-0.618034194102436, 0.387420489757166},
                {-0.387420489757166, -0.618034194102436},
                {0.387420489757166, -0.618034194102436},
                {0.618034194102436, 0.387420489757166}});
        RealMatrix actualT = transformer.getT();
        PrecisionUtils.assertEquals(expectedT, actualT, 1e-10);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetTBeforeTransform() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {2, 3}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        transformer.getT();
    }

    @Test
    public void testTransform() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {2, 3}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        transformer.transform();
        RealMatrix expectedP = new Array2DRowRealMatrix(new double[][]{{0.342020142555704, -0.615422834050112},
                {-0.615422834050112, 0.342020142555704}});
        RealMatrix expectedT = new Array2DRowRealMatrix(new double[][]{{-0.618034194102436, 0.387420489757166},
                {-0.387420489757166, -0.618034194102436},
                {0.387420489757166, -0.618034194102436},
                {0.618034194102436, 0.387420489757166}});
        RealMatrix actualP = transformer.getP();
        PrecisionUtils.assertEquals(expectedP, actualP, 1e-10);
        RealMatrix actualT = transformer.getT();
        PrecisionUtils.assertEquals(expectedT, actualT, 1e-10);
    }

    @Test(expected = ShiftNotPositiveException.class)
    public void testComputeShiftWhenShiftIsNegative() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {2, 3}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        ShiftInfo shift = new SchurTransformer.ShiftInfo();
        transformer.computeShift(-1, 1, 1, shift);
    }

    @Test
    public void testFindSmallSubDiagonalElement() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2, 3, 4}, {2, 4, 5, 6}, {3, 5, 6, 7}, {4, 6, 7, 8}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        int expectedIndex = 10;
        int actualIndex = transformer.findSmallSubDiagonalElement(10, 4.0);
        assertEquals(expectedIndex, actualIndex);
    }

    @Test
    public void testInitQRStep() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2, 3, 4}, {2, 4, 5, 6}, {3, 5, 6, 7}, {4, 6, 7, 8}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        double[] hVec = new double[3];
        int expectedIndex = 7;
        int actualIndex = transformer.initQRStep(7, 9, new SchurTransformer.ShiftInfo(), hVec);
        assertEquals(expectedIndex, actualIndex);
    }

    @Test
    public void testPerformDoubleQRStep() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2, 3, 4}, {2, 4, 5, 6}, {3, 5, 6, 7}, {4, 6, 7, 8}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        SchurTransformer.ShiftInfo shift = new SchurTransformer.ShiftInfo();
        double[] hVec = new double[3];
        int il = 7, im = 9, iu = 12;
        transformer.initQRStep(il, iu, shift, hVec);
        transformer.performDoubleQRStep(il, im, iu, shift, hVec);
        double[][] expectedMatrix = new double[][]{{-0.00320843151810426, 0.00177957782718482, -0.0013744436314673, 0.00142184048764423,
                0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
                {-0.0013744436314673, -0.00463978170616164, -0.742786803259311, 0.126344231641735, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
                {0.00142184048764423, -0.126344231641735, -0.744226855587835, -0.48866585317041, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
                {-0.00177957782718482, -0.742786803259311, -0.48866585317041, -1.22989114068328, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
                {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
                {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
                {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
                {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
                {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
                {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
                {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
                {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
                {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
                {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
                {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0}};
        RealMatrix actualMatrix = transformer.getT();
        for (int i = 0; i < 14; ++i) {
            for (int j = 0; j < 14; ++j) {
                assertEquals(expectedMatrix[i][j], actualMatrix.getEntry(i, j), 1e-10);
            }
        }
    }

    @Test(expected = MaxCountExceededException.class)
    public void testTransformWhenMaxIterationsExceeded() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {2, 3}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        transformer.transform();
        transformer.transform();
    }
}