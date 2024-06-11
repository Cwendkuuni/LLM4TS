package org.apache.commons.math3.linear;

import static org.junit.Assert.*;
import org.junit.Test;

import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.PrecisionUtils;
import org.apache.commons.math3.util.RealMatrix;
import org.apache.commons.math3.util.MatrixUtils;
import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.util.LocalizedFormats;

public class SchurTransformerTest {

    @Test(expected = NonSquareMatrixException.class)
    public void testSchurTransformerWithNonSquareMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][] { { 1, 2 }, { 3, 4, 5 } });
        new SchurTransformer(matrix);
    }

    @Test
    public void testGetP() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][] { { 1, 0 }, { 0, 1 } });
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix expectedP = MatrixUtils.createRealMatrix(new double[][] { { 0.7465844945873259, -0.6928203230275508 }, { -0.6928203230275508, 0.7465844945873259 } });
        RealMatrix actualP = transformer.getP();
        assertEquals(expectedP, actualP);
    }

    @Test
    public void testGetPT() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][] { { 1, 0 }, { 0, 1 } });
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix expectedPT = MatrixUtils.createRealMatrix(new double[][] { { 0.7465844945873259, 0.6928203230275508 }, { -0.6928203230275508, 0.7465844945873259 } });
        RealMatrix actualPT = transformer.getPT();
        assertEquals(expectedPT, actualPT);
    }

    @Test
    public void testGetT() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][] { { 3, 2, 1 }, { 2, -1, 2 }, { 1, 2, -1 } });
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix expectedT = MatrixUtils.createRealMatrix(new double[][] { { 1.2914529552099015, -0.471404579460849, -1.7231140169395969 }, { -0.471404579460849, 2.212832824630368, -0.7673338727104613 }, { -1.7231140169395969, -0.7673338727104613, 1.486490150328842 } });
        RealMatrix actualT = transformer.getT();
        assertEquals(expectedT, actualT);
    }

    @Test
    public void testTransform() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][] { { 3, 2, 1 }, { 2, -1, 2 }, { 1, 2, -1 } });
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix expectedP = MatrixUtils.createRealMatrix(new double[][] { { 0.3535533905954278, -0.8573845550624183, -0.3826834322596267 }, { -0.8573845550624183, 0.3535533905954278, 0.5325730503792525 }, { -0.3826834322596267, 0.5325730503792525, -0.8164966532291779 } });
        RealMatrix expectedT = MatrixUtils.createRealMatrix(new double[][] { { 1.2665329576919255, -0.4719683652958414, -1.7231140169395969 }, { -0.4719683652958414, 2.213642673032973, -0.7673338727104613 }, { -1.7231140169395969, -0.7673338727104613, 1.486490150328842 } });
        RealMatrix expectedPt = MatrixUtils.createRealMatrix(new double[][] { { 0.3535533905954278, 0.8573845550624183, -0.3826834322596267 }, { -0.8573845550624183, 0.3535533905954278, -0.5325730503792525 }, { -0.3826834322596267, -0.5325730503792525, 0.8164966532291779 } });
        RealMatrix actualP = transformer.getP();
        RealMatrix actualT = transformer.getT();
        RealMatrix actualPt = transformer.getPT();
        assertEquals(expectedP, actualP);
        assertEquals(expectedT, actualT);
        assertEquals(expectedPt, actualPt);
    }

    @Test
    public void testFindSmallSubDiagonalElement() {
        double[][] matrix = { { 1, 0, 1 }, { 0, 2, 0 }, { 1, 0, 3 } };
        SchurTransformer transformer = new SchurTransformer(MatrixUtils.createRealMatrix(matrix));
        int[] expectedIndices = { 1, 1, 2 };
        int[] actualIndices = new int[3];
        for (int i = 0; i < 3; ++i) {
            actualIndices[i] = transformer.findSmallSubDiagonalElement(i, 3);
        }
        assertArrayEquals(expectedIndices, actualIndices);
    }

    @Test
    public void testComputeShift() {
        double[][] matrix = { { 1, 0, 1 }, { 0, 2, 0 }, { 1, 0, 3 } };
        SchurTransformer transformer = new SchurTransformer(MatrixUtils.createRealMatrix(matrix));
        ShiftInfo[] expectedShifts = new ShiftInfo[] {
            new ShiftInfo(),
            new ShiftInfo() {{
                x = 2.0;
                w = -0.4375;
                y = 2.0;
            }},
            new ShiftInfo() {{
                x = 3.0;
                w = -0.4375;
                y = 3.0;
            }}
        };
        ShiftInfo[] actualShifts = new ShiftInfo[3];
        for (int i = 0; i < 3; ++i) {
            actualShifts[i] = transformer.computeShift(i, 2, 0, new ShiftInfo());
        }
        for (int i = 0; i < 3; ++i) {
            assertEquals(expectedShifts[i].x, actualShifts[i].x, Precision.SAFE_EPSILON);
            assertEquals(expectedShifts[i].w, actualShifts[i].w, Precision.SAFE_EPSILON);
            assertEquals(expectedShifts[i].y, actualShifts[i].y, Precision.SAFE_EPSILON);
        }
    }

    @Test
    public void testInitQRStep() {
        double[][] matrix = { { 1, 0, 1 }, { 0, 2, 0 }, { 1, 0, 3 } };
        SchurTransformer transformer = new SchurTransformer(MatrixUtils.createRealMatrix(matrix));
        double[][] expectedHVecs = new double[][] { { 1.4142137410599687, -0.7071067811865476, 0.7071067811865476 }, { 1.0, 2.0, 1.0 }, { 0.7071067811865476, -1.4142137410599687, 0.7071067811865476 } };
        int[] expectedIm = { 1, 1, 2 };
        for (int i = 0; i < 3; ++i) {
            double[] hVec = new double[3];
            int im = transformer.initQRStep(i, 2, new ShiftInfo(), hVec);
            assertArrayEquals(expectedHVecs[i], hVec, PrecisionUtils.DEFAULT_EPSILON);
            assertEquals(expectedIm[i], im);
        }
    }

    @Test
    public void testPerformDoubleQRStep() {
        double[][] matrix = { { 1, 0, 1 }, { 0, 2, 0 }, { 1, 0, 3 } };
        SchurTransformer transformer = new SchurTransformer(MatrixUtils.createRealMatrix(matrix));
        ShiftInfo shift = new ShiftInfo();
        double[] hVec = new double[3];
        int im = 1;
        transformer.performDoubleQRStep(0, im, 2, shift, hVec);
        double[][] expectedT = new double[][] { { 1.2665329576919255, -0.4719683652958414, -1.7231140169395969 }, { -0.4719683652958414, 2.213642673032973, -0.7673338727104613 }, { -1.7231140169395969, -0.7673338727104613, 1.486490150328842 } };
        double[][] expectedTSubDiagonal = new double[][] { { 0, 0.7413235436398922, 0 }, { 0.7413235436398922, 0, 0.5325730503792525 }, { 0, 0.5325730503792525, 0 } };
        double[][] expectedMatrixP = new double[][] { { 0.3535533905954278, -0.8573845550624183, -0.3826834322596267 }, { -0.8573845550624183, 0.3535533905954278, -0.5325730503792525 }, { -0.3826834322596267, -0.5325730503792525, 0.8164966532291779 } };
        RealMatrix actualT = transformer.getT();
        double[][] actualTSubDiagonal = new double[3][3];
        for (int i = 0; i < 3; ++i) {
            System.arraycopy(transformer.matrixT[i], i, actualTSubDiagonal[i], 0, 3 - i);
        }
        RealMatrix actualMatrixP = transformer.getP();
        for (int i = 0; i < 3; ++i) {
            System.arraycopy(transformer.matrixP[i], 0, actualMatrixP.getData()[i], 0, 3 * i);
        }
        assertEquals(expectedT, actualT);
        assertEquals(expectedTSubDiagonal, actualTSubDiagonal);
        assertEquals(expectedMatrixP, actualMatrixP);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testTransformWithMaxIterationsExceeded() {
        double[][] matrix = new double[5][5];
        for (int i = 0; i < 5; ++i) {
            for (int j = 0; j < 5; ++j) {
                matrix[i][j] = Precision.random();
            }
        }
        SchurTransformer transformer = new SchurTransformer(MatrixUtils.createRealMatrix(matrix));
        transformer.maxIterations = 0;
        transformer.transform();
    }

}