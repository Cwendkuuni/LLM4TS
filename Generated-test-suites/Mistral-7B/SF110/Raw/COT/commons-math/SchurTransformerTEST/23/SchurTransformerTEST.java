package org.apache.commons.math3.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.linear.*;
import org.junit.Test;

public class SchurTransformerTest {

    @Test
    public void testSchurTransformer_ValidMatrix() {
        RealMatrix inputMatrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 3.0, 4.0}, {3.0, 4.0, 5.0}});
        SchurTransformer schurTransformer = new SchurTransformer(inputMatrix);
        RealMatrix pMatrix = schurTransformer.getP();
        RealMatrix tMatrix = schurTransformer.getT();
        RealMatrix ptMatrix = schurTransformer.getPT();

        assertEquals("Check dimensions of P matrix", inputMatrix.getRowDimension(), pMatrix.getRowDimension());
        assertEquals("Check dimensions of P matrix", inputMatrix.getColumnDimension(), pMatrix.getColumnDimension());

        assertEquals("Check dimensions of T matrix", inputMatrix.getRowDimension(), tMatrix.getRowDimension());
        assertEquals("Check dimensions of T matrix", inputMatrix.getColumnDimension(), tMatrix.getColumnDimension());

        assertEquals("Check dimensions of PT matrix", inputMatrix.getRowDimension(), ptMatrix.getRowDimension());
        assertEquals("Check dimensions of PT matrix", inputMatrix.getColumnDimension(), ptMatrix.getColumnDimension());

        RealMatrix expectedP = MatrixUtils.createRealMatrix(new double[][]{{0.61332743, 0.7914767, -0.06042333}, {0.7914767, 0.88340625, -0.16622905}, {-0.06042333, -0.16622905, 0.91824924}});
        RealMatrix expectedT = MatrixUtils.createRealMatrix(new double[][]{{-2.15981535, 1.57415297, 0.0}, {1.57415297, -0.38253959, 0.61332743}, {0.0, 0.61332743, 0.91824924}});
        RealMatrix expectedPt = MatrixUtils.createRealMatrix(new double[][]{{0.91824924, 0.61332743, 1.57415297}, {0.91824924, 0.88340625, -1.57415297}, {1.57415297, -0.16622905, 2.15981535}});

        MatrixUtils.assertEquals("Check P matrix", expectedP, pMatrix);
        MatrixUtils.assertEquals("Check T matrix", expectedT, tMatrix);
        MatrixUtils.assertEquals("Check PT matrix", expectedPt, ptMatrix);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testSchurTransformer_NonSquareMatrix() {
        RealMatrix inputMatrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0}});
        new SchurTransformer(inputMatrix);
    }

    @Test
    public void testSchurTransformer_GetMethods() {
        RealMatrix inputMatrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 3.0, 4.0}, {3.0, 4.0, 5.0}});
        SchurTransformer schurTransformer = new SchurTransformer(inputMatrix);

        RealMatrix pMatrix = schurTransformer.getP();
        RealMatrix tMatrix = schurTransformer.getT();
        RealMatrix ptMatrix = schurTransformer.getPT();

        assertEquals("Check P matrix is not null", pMatrix, schurTransformer.getP());
        assertEquals("Check T matrix is not null", tMatrix, schurTransformer.getT());
        assertEquals("Check PT matrix is not null", ptMatrix, schurTransformer.getPT());

        schurTransformer.getP();
        schurTransformer.getT();
        schurTransformer.getPT();
    }

    @Test
    public void testSchurTransformer_Transform() {
        RealMatrix inputMatrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 3.0, 4.0}, {3.0, 4.0, 5.0}});
        SchurTransformer schurTransformer = new SchurTransformer(inputMatrix);

        RealMatrix pMatrix = schurTransformer.getP();
        RealMatrix tMatrix = schurTransformer.getT();
        RealMatrix ptMatrix = schurTransformer.getPT();

        assertEquals("Check dimensions of P matrix", inputMatrix.getRowDimension(), pMatrix.getRowDimension());
        assertEquals("Check dimensions of P matrix", inputMatrix.getColumnDimension(), pMatrix.getColumnDimension());

        assertEquals("Check dimensions of T matrix", inputMatrix.getRowDimension(), tMatrix.getRowDimension());
        assertEquals("Check dimensions of T matrix", inputMatrix.getColumnDimension(), tMatrix.getColumnDimension());

        assertEquals("Check dimensions of PT matrix", inputMatrix.getRowDimension(), ptMatrix.getRowDimension());
        assertEquals("Check dimensions of PT matrix", inputMatrix.getColumnDimension(), ptMatrix.getColumnDimension());

        RealMatrix expectedP = MatrixUtils.createRealMatrix(new double[][]{{0.61332743, 0.7914767, -0.06042333}, {0.7914767, 0.88340625, -0.16622905}, {-0.06042333, -0.16622905, 0.91824924}});
        RealMatrix expectedT = MatrixUtils.createRealMatrix(new double[][]{{-2.15981535, 1.57415297, 0.0}, {1.57415297, -0.38253959, 0.61332743}, {0.0, 0.61332743, 0.91824924}});
        RealMatrix expectedPt = MatrixUtils.createRealMatrix(new double[][]{{0.91824924, 0.61332743, 1.57415297}, {0.91824924, 0.88340625, -1.57415297}, {1.57415297, -0.16622905, 2.15981535}});

        MatrixUtils.assertEquals("Check P matrix", expectedP, pMatrix);
        MatrixUtils.assertEquals("Check T matrix", expectedT, tMatrix);
        MatrixUtils.assertEquals("Check PT matrix", expectedPt, ptMatrix);
    }

    @Test
    public void testSchurTransformer_FindSmallSubDiagonalElement() {
        double[][] inputMatrix = new double[][]{{1.0, 2.0, 3.0, 4.0, 5.0}, {2.0, 3.0, 4.0, 5.0, 6.0}, {3.0, 4.0, 5.0, 6.0, 7.0}, {4.0, 5.0, 6.0, 7.0, 8.0}, {5.0, 6.0, 7.0, 8.0, 9.0}};
        SchurTransformer schurTransformer = new SchurTransformer(RealMatrixFactory2D.RECTANGULAR.getMatrix(5, 5, inputMatrix));

        int expectedIndex = 4;
        int actualIndex = schurTransformer.findSmallSubDiagonalElement(4, schurTransformer.getNorm());

        assertEquals("Check index of small subdiagonal element", expectedIndex, actualIndex);
    }

    @Test
    public void testSchurTransformer_ComputeShift() {
        double[][] inputMatrix = new double[][]{{1.0, 2.0, 3.0}, {2.0, 3.0, 4.0}, {3.0, 4.0, 5.0}};
        SchurTransformer schurTransformer = new SchurTransformer(RealMatrixFactory2D.RECTANGULAR.getMatrix(3, 3, inputMatrix));
        ShiftInfo shiftInfo = new SchurTransformer.ShiftInfo();

        schurTransformer.computeShift(1, 2, 0, shiftInfo);

        double expectedX = 3.75;
        double expectedW = 12.0;
        double expectedY = 3.75;

        assertEquals("Check x value", expectedX, shiftInfo.x, 0.001);
        assertEquals("Check w value", expectedW, shiftInfo.w, 0.001);
        assertEquals("Check y value", expectedY, shiftInfo.y, 0.001);
    }

    @Test
    public void testSchurTransformer_InitQRStep() {
        double[][] inputMatrix = new double[][]{{1.0, 2.0, 3.0}, {2.0, 3.0, 4.0}, {3.0, 4.0, 5.0}};
        SchurTransformer schurTransformer = new SchurTransformer(RealMatrixFactory2D.RECTANGULAR.getMatrix(3, 3, inputMatrix));
        double[] hVec = new double[3];

        int expectedIndex = 1;

        int actualIndex = schurTransformer.initQRStep(1, 2, shiftInfo, hVec);

        assertEquals("Check index of last element in hVec", expectedIndex, actualIndex);
    }

    @Test
    public void testSchurTransformer_PerformDoubleQRStep() {
        double[][] inputMatrix = new double[][]{{1.0, 2.0, 3.0}, {2.0, 3.0, 4.0}, {3.0, 4.0, 5.0}};
        SchurTransformer schurTransformer = new SchurTransformer(RealMatrixFactory2D.RECTANGULAR.getMatrix(3, 3, inputMatrix));
        ShiftInfo shiftInfo = new SchurTransformer.ShiftInfo();
        double[] hVec = new double[3];

        int il = 1;
        int im = 1;
        int iu = 3;

        schurTransformer.performDoubleQRStep(il, im, iu, shiftInfo, hVec);

        double[] expectedHVec = new double[]{-0.57735027, 2.57628865, 0.57735027};

        assertArrayEquals("Check hVec values", expectedHVec, hVec, 0.001);
    }
}