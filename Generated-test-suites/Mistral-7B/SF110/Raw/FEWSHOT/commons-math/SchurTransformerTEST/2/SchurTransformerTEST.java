package org.apache.commons.math3.linear;

import static org.junit.Assert.*;

import org.junit.Test;

import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.apache.commons.math3.exception.MaxCountExceededException;

public class SchurTransformerTest {
    @Test(expected = NonSquareMatrixException.class)
    public void testInvalidMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][] { {1.0}, {2.0, 3.0} });
        new SchurTransformer(matrix);
    }

    @Test
    public void testTransform() {
        double[][] matrix = { {1.0, 2.0}, {2.0, 3.0} };
        RealMatrix inputMatrix = MatrixUtils.createRealMatrix(matrix);
        SchurTransformer transformer = new SchurTransformer(inputMatrix);
        RealMatrix expectedP = MatrixUtils.createRealMatrix(new double[][] { {0.632455512754651, 0.787397898322639}, {-0.787397898322639, 0.632455512754651} });
        RealMatrix expectedT = MatrixUtils.createRealMatrix(new double[][] { {3.0, 2.0}, {2.0, 1.0} });
        RealMatrix p = transformer.getP();
        RealMatrix t = transformer.getT();
        assertEquals(expectedP, p);
        assertEquals(expectedT, t);
    }

    @Test
    public void testGetNorm() {
        double[][] matrix = { {1.0, 2.0}, {2.0, 3.0} };
        RealMatrix inputMatrix = MatrixUtils.createRealMatrix(matrix);
        SchurTransformer transformer = new SchurTransformer(inputMatrix);
        double expectedNorm = 6.283185307179586;
        double norm = transformer.getNorm();
        assertEquals(expectedNorm, norm, 0.0000001);
    }

    @Test
    public void testFindSmallSubDiagonalElement() {
        double[][] matrix = { {1.0, 2.0, 3.0, 4.0}, {2.0, 3.0, 4.0, 5.0}, {3.0, 4.0, 5.0, 6.0}, {4.0, 5.0, 6.0, 7.0} };
        RealMatrix inputMatrix = MatrixUtils.createRealMatrix(matrix);
        SchurTransformer transformer = new SchurTransformer(inputMatrix);
        int expectedIndex = 2;
        int index = transformer.findSmallSubDiagonalElement(expectedIndex, transformer.getNorm());
        assertEquals(expectedIndex, index);
    }

    @Test
    public void testComputeShift() {
        double[][] matrix = { {1.0, 2.0, 3.0, 4.0}, {2.0, 3.0, 4.0, 5.0}, {3.0, 4.0, 5.0, 6.0}, {4.0, 5.0, 6.0, 7.0} };
        RealMatrix inputMatrix = MatrixUtils.createRealMatrix(matrix);
        SchurTransformer transformer = new SchurTransformer(inputMatrix);
        double expectedX = 5.0;
        double x = transformer.matrixT[2][2];
        assertEquals(expectedX, x, 0.0000001);

        double shiftX = transformer.shift.x;
        assertEquals(expectedX, shiftX, 0.0000001);
    }

    @Test
    public void testInitQRStep() {
        double[][] matrix = { {1.0, 2.0, 3.0, 4.0}, {2.0, 3.0, 4.0, 5.0}, {3.0, 4.0, 5.0, 6.0}, {4.0, 5.0, 6.0, 7.0} };
        RealMatrix inputMatrix = MatrixUtils.createRealMatrix(matrix);
        SchurTransformer transformer = new SchurTransformer(inputMatrix);
        int expectedIndex = 1;
        int index = transformer.initQRStep(expectedIndex, transformer.matrixT.length - 1, transformer.shift, new double[3]);
        assertEquals(expectedIndex, index);
    }

    @Test
    public void testPerformDoubleQRStep() {
        double[][] matrix = { {1.0, 2.0, 3.0, 4.0}, {2.0, 3.0, 4.0, 5.0}, {3.0, 4.0, 5.0, 6.0}, {4.0, 5.0, 6.0, 7.0} };
        RealMatrix inputMatrix = MatrixUtils.createRealMatrix(matrix);
        SchurTransformer transformer = new SchurTransformer(inputMatrix);
        int expectedIndex = 1;
        int index = transformer.initQRStep(expectedIndex, transformer.matrixT.length - 1, transformer.shift, new double[3]);
        transformer.performDoubleQRStep(expectedIndex, index, transformer.matrixT.length - 1, transformer.shift, new double[3]);

        double[][] expectedMatrix = { {1.0, 2.0, 0.0, 0.0}, {2.0, 0.5, 1.0, 0.0}, {0.0, 1.0, 0.5, 1.0}, {0.0, 0.0, 1.0, 0.632455512754651} };
        RealMatrix p = transformer.getP();
        RealMatrix t = transformer.getT();

        for (int i = 0; i < p.getRowDimension(); i++) {
            for (int j = 0; j < p.getColumnDimension(); j++) {
                assertEquals(expectedMatrix[i][j], p.getEntry(i, j), 0.0000001);
            }
        }

        for (int i = 0; i < t.getRowDimension(); i++) {
            for (int j = 0; j < t.getColumnDimension(); j++) {
                assertEquals(expectedMatrix[i][j], t.getEntry(i, j), 0.0000001);
            }
        }
    }

    @Test(expected = MaxCountExceededException.class)
    public void testMaxIterations() {
        double[][] matrix = { {1.0, 2.0, 3.0, 4.0}, {2.0, 3.0, 4.0, 5.0}, {3.0, 4.0, 5.0, 6.0}, {4.0, 5.0, 6.0, 7.0} };
        RealMatrix inputMatrix = MatrixUtils.createRealMatrix(matrix);
        SchurTransformer transformer = new SchurTransformer(inputMatrix);
        transformer.transform();
        transformer.transform();
        transformer.transform();
        transformer.transform();
        transformer.transform();
        transformer.transform();
        transformer.transform();
        transformer.transform();
        transformer.transform();
        transformer.transform();
        transformer.transform();
    }
}