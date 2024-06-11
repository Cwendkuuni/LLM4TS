package org.apache.commons.math3.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.apache.commons.math3.util.*;
import org.junit.Test;

public class SchurTransformerTest {

    @Test(expected = NonSquareMatrixException.class)
    public void testSchurTransformerThrowsExceptionForNonSquareMatrix() {
        RealMatrix matrix = Array2DRandomGenerator.getSubMatrix(new double[5][7], 2, 3, 4, 5);
        new SchurTransformer(matrix);
    }

    @Test
    public void testSchurTransformerGetMatrixT() {
        RealMatrix matrix = Array2DRandomGenerator.getRandomMatrix(5, 5);
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix matrixT = transformer.getT();
        assertEquals(matrix.getRowDimension(), matrixT.getRowDimension());
        assertEquals(matrix.getColumnDimension(), matrixT.getColumnDimension());
        assertArrayEquals(matrix.getData(), transformer.getP().transpose().getData());
    }

    @Test
    public void testSchurTransformerTransform() {
        RealMatrix matrix = Array2DRandomGenerator.getRandomMatrix(5, 5);
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix matrixT = transformer.getT();
        RealMatrix matrixP = transformer.getP();
        double norm = transformer.getNorm();

        // Check that the transformed matrix is Hessenberg
        for (int i = 0; i < matrixT.getRowDimension() - 1; i++) {
            for (int j = i + 1; j < matrixT.getColumnDimension(); j++) {
                if (i < matrixT.getRowDimension() - 2) {
                    assertTrue(FastMath.abs(matrixT.getEntry(i, j)) < FastMath.abs(matrixT.getEntry(i, i - 1)));
                }
                assertEquals(0.0, matrixT.getEntry(i, j), 1e-12);
            }
            assertEquals(matrixT.getEntry(i, i), matrixT.getEntry(i + 1, i), 1e-12);
            assertEquals(0.0, matrixT.getEntry(i, i + 1), 1e-12);
        }

        // Check that the transformed matrix has the correct diagonal elements
        for (int i = 0; i < matrixT.getRowDimension(); i++) {
            assertEquals(FastMath.sqrt(FastMath.abs(matrixT.getEntry(i, i))), matrixT.getEntry(i, i), 1e-12);
        }

        // Check that the Schur decomposition satisfies the invariance property: PT * P = T
        RealMatrix PT = MatrixUtils.createRealMatrix(transformer.getPT().getData());
        RealMatrix PP = MatrixUtils.createRealMatrix(MatrixUtils.multiply(transformer.getP(), transformer.getP()).getData());
        assertTrue(MatrixUtils.isEqual(PT, PP, 1e-12));

        // Check that the Schur decomposition satisfies the unitary property: P' * P = I
        RealMatrix PTranspose = MatrixUtils.createRealMatrix(transformer.getP().transpose().getData());
        RealMatrix PPTranspose = MatrixUtils.createRealMatrix(MatrixUtils.multiply(PTranspose, transformer.getP()).getData());
        assertTrue(MatrixUtils.isIdentityMatrix(PPTranspose, 1e-12));

        // Check that the norm of the transformed matrix is less than or equal to the original matrix
        assertTrue(norm <= matrix.normF());
    }

    @Test(expected = MaxCountExceededException.class)
    public void testSchurTransformerThrowsExceptionForMaxCountExceeded() {
        RealMatrix matrix = Array2DRandomGenerator.getRandomMatrix(5, 5);
        SchurTransformer transformer = new SchurTransformer(matrix);
        transformer.transform();
        transformer.transform();
    }
}