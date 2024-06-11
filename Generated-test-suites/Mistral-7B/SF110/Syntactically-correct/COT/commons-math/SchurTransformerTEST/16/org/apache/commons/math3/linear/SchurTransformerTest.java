package org.apache.commons.math3.linear;

import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.PrecisionUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class SchurTransformerTest {

    private SchurTransformer schurTransformer;
    private RealMatrix sourceMatrix;

    @Before
    public void setUp() {
        double[][] matrixData = {
                {1.0, 2.0, 3.0},
                {2.0, 4.0, 6.0},
                {3.0, 6.0, 9.0}
        };
        sourceMatrix = MatrixUtils.createRealMatrix(matrixData);
    }

    @Test
    public void testSchurTransformerConstruction() {
        schurTransformer = new SchurTransformer(sourceMatrix);
        assertNotNull(schurTransformer);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testSchurTransformerConstructionWithNonSquareMatrix() {
        double[][] matrixData = {
                {1.0, 2.0},
                {3.0}
        };
        RealMatrix nonSquareMatrix = MatrixUtils.createRealMatrix(matrixData);
        new SchurTransformer(nonSquareMatrix);
    }

    @Test
    public void testGetP() {
        schurTransformer = new SchurTransformer(sourceMatrix);
        RealMatrix expectedP = MatrixUtils.createRealMatrix(new double[][]{{0.82018919, 0.35057574, -0.04943094},
                {-0.35057574, 0.90583318, 0.14479433},
                {-0.04943094, 0.14479433, 0.95105651}});
        RealMatrix actualP = schurTransformer.getP();
        assertEquals(expectedP, actualP);
    }

    @Test
    public void testGetPT() {
        schurTransformer = new SchurTransformer(sourceMatrix);
        RealMatrix expectedPT = MatrixUtils.createRealMatrix(new double[][]{{0.82018919, -0.35057574, -0.04943094},
                {0.35057574, 0.90583318, 0.14479433},
                {-0.04943094, 0.14479433, 0.95105651}});
        RealMatrix actualPT = schurTransformer.getPT();
        assertEquals(expectedPT, actualPT);
    }

    @Test
    public void testGetT() {
        schurTransformer = new SchurTransformer(sourceMatrix);
        RealMatrix expectedT = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0},
                {2.0, 4.32874275, 6.0},
                {3.0, 6.0, 9.0}});
        RealMatrix actualT = schurTransformer.getT();
        assertEquals(expectedT, actualT, 1e-9);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetTBeforeTransform() {
        schurTransformer = new SchurTransformer(sourceMatrix);
        schurTransformer.getT();
        schurTransformer = new SchurTransformer(sourceMatrix);
        assertThrows(IllegalStateException.class, () -> schurTransformer.getT());
    }

    @Test
    public void testTransform() {
        schurTransformer = new SchurTransformer(sourceMatrix);
        schurTransformer.transform();
        RealMatrix expectedT = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0},
                {2.0, 4.32874275, 6.0},
                {3.0, 6.0, 9.0}});
        RealMatrix actualT = schurTransformer.getT();
        assertEquals(expectedT, actualT, 1e-9);

        RealMatrix expectedP = MatrixUtils.createRealMatrix(new double[][]{{0.82018919, 0.35057574, -0.04943094},
                {-0.35057574, 0.90583318, 0.14479433},
                {-0.04943094, 0.14479433, 0.95105651}});
        RealMatrix actualP = schurTransformer.getP();
        assertEquals(expectedP, actualP);

        RealMatrix expectedPT = MatrixUtils.createRealMatrix(new double[][]{{0.82018919, -0.35057574, -0.04943094},
                {0.35057574, 0.90583318, 0.14479433},
                {-0.04943094, 0.14479433, 0.95105651}});
        RealMatrix actualPT = schurTransformer.getPT();
        assertEquals(expectedPT, actualPT);
    }

    @Test
    public void testGetNorm() {
        schurTransformer = new SchurTransformer(sourceMatrix);
        double expectedNorm = 15.0;
        double actualNorm = schurTransformer.getNorm();
        assertEquals(expectedNorm, actualNorm, 1e-9);
    }

    @Test
    public void testFindSmallSubDiagonalElement() {
        double[][] matrixData = {
                {1.0, 1.0, 0.0},
                {0.1, 2.0, 1.0},
                {0.1, 0.1, 3.0}
        };
        SchurTransformer schurTransformer = new SchurTransformer(MatrixUtils.createRealMatrix(matrixData));
        int expectedResult = 1;
        int actualResult = schurTransformer.findSmallSubDiagonalElement(0, 0.0);
        assertEquals(expectedResult, actualResult);

        expectedResult = 1;
        schurTransformer = new SchurTransformer(MatrixUtils.createRealMatrix(matrixData));
        actualResult = schurTransformer.findSmallSubDiagonalElement(1, 0.0);
        assertEquals(expectedResult, actualResult);

        expectedResult = 2;
        schurTransformer = new SchurTransformer(MatrixUtils.createRealMatrix(matrixData));
        actualResult = schurTransformer.findSmallSubDiagonalElement(2, 0.0);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testComputeShift() {
        double[][] matrixData = {
                {1.0, 1.0, 0.0},
                {0.1, 2.0, 1.0},
                {0.1, 0.1, 3.0}
        };
        SchurTransformer schurTransformer = new SchurTransformer(MatrixUtils.createRealMatrix(matrixData));

        SchurTransformer.ShiftInfo shiftInfo = new SchurTransformer.ShiftInfo();
        schurTransformer.computeShift(0, 0, 0, shiftInfo);
        double expectedX = 1.0;
        double expectedW = 0.0;
        double expectedY = 0.0;
        double expectedExShift = 1.0;

        double actualX = shiftInfo.x;
        double actualW = shiftInfo.w;
        double actualY = shiftInfo.y;
        double actualExShift = shiftInfo.exShift;

        assertEquals(expectedX, actualX, 1e-9);
        assertEquals(expectedW, actualW, 1e-9);
        assertEquals(expectedY, actualY, 1e-9);
        assertEquals(expectedExShift, actualExShift, 1e-9);
    }

    @Test
    public void testInitQRStep() {
        double[][] matrixData = {
                {1.0, 2.0, 3.0},
                {2.0, 4.0, 6.0},
                {3.0, 6.0, 9.0}
        };
        SchurTransformer schurTransformer = new SchurTransformer(MatrixUtils.createRealMatrix(matrixData));

        double[] hVec = new double[3];
        int expectedIm = 3;
        int actualIm = schurTransformer.initQRStep(0, 3, new SchurTransformer.ShiftInfo(), hVec);

        assertEquals(expectedIm, actualIm);
        assertArrayEquals(new double[]{1.0, -1.0, 3.0}, hVec, 1e-9);
    }

    @Test
    public void testPerformDoubleQRStep() {
        double[][] matrixData = {
                {1.0, 2.0, 3.0},
                {2.0, 4.0, 6.0},
                {3.0, 6.0, 9.0}
        };
        SchurTransformer schurTransformer = new SchurTransformer(MatrixUtils.createRealMatrix(matrixData));

        double[] hVec = new double[3];
        int il = 0, im = 2, iu = 3;
        SchurTransformer.ShiftInfo shiftInfo = new SchurTransformer.ShiftInfo();
        schurTransformer.initQRStep(il, iu, shiftInfo, hVec);

        schurTransformer.performDoubleQRStep(il, im, iu, shiftInfo, hVec);

        double[][] expectedMatrixData = {
                {1.0, 2.0, 3.0},
                {0.0, 5.41942927, 5.41942927},
                {0.0, 5.41942927, 9.0}
        };
        RealMatrix expectedMatrix = MatrixUtils.createRealMatrix(expectedMatrixData);

        RealMatrix actualMatrix = schurTransformer.getT();
        assertEquals(expectedMatrix, actualMatrix);

        expectedMatrixData = new double[][]{{0.82018919, 0.35057574, -0.04943094},
                {-0.35057574, 0.90583318, 0.14479433},
                {-0.04943094, 0.14479433, 0.95105651}};
        expectedMatrix = MatrixUtils.createRealMatrix(expectedMatrixData);

        actualMatrix = schurTransformer.getP();
        assertEquals(expectedMatrix, actualMatrix);

        expectedMatrixData = new double[][]{{0.82018919, 0.35057574, -0.04943094},
                {-0.35057574, 0.90583318, 0.14479433},
                {-0.04943094, 0.14479433, 0.95105651}};
        expectedMatrix = MatrixUtils.createRealMatrix(expectedMatrixData);

        actualMatrix = schurTransformer.getPT();
        assertEquals(expectedMatrix, actualMatrix);
    }

    @Test
    public void testMaxCountExceededException() {
        double[][] matrixData = {
                {1.0, 2.0, 3.0},
                {2.0, 4.0, 6.0},
                {3.0, 6.0, 9.0}
        };
        SchurTransformer schurTransformer = new SchurTransformer(MatrixUtils.createRealMatrix(matrixData));

        assertThrows(org.apache.commons.math3.exception.MaxCountExceededException.class, () -> schurTransformer.transform());
    }
}