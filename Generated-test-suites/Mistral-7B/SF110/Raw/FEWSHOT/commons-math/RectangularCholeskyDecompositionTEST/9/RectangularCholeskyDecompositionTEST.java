package org.apache.commons.math3.linear;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testCholeskyDecomposition() {
        double[][] matrixData = new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 6.0}, {3.0, 6.0, 9.0}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);

        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
            RealMatrix rootMatrix = decomposition.getRootMatrix();
            int rank = decomposition.getRank();

            assertEquals(3, rank);
            assertEquals(3, rootMatrix.getRowDimension());
            assertEquals(3, rootMatrix.getColumnDimension());

            // Check that the lower triangular matrix is correct
            double[][] rootData = rootMatrix.getData();
            assertEquals(1.0, rootData[0][0], 1e-12);
            assertEquals(0.0, rootData[0][1], 1e-12);
            assertEquals(0.0, rootData[0][2], 1e-12);
            assertEquals(1.0, rootData[1][1], 1e-12);
            assertEquals(1.4142135625, rootData[1][2], 1e-12);
            assertEquals(1.7320508076, rootData[2][2], 1e-12);

            // Check that the matrix can be reconstructed from the decomposition
            RealMatrix reconstructedMatrix = rootMatrix.transpose().multiply(rootMatrix);
            assertEquals(matrix, reconstructedMatrix, 1e-12);

        } catch (NonPositiveDefiniteMatrixException e) {
            // This should not be thrown for a positive definite matrix
            assertFalse(true);
        }
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteMatrixException() {
        double[][] matrixData = new double[][]{{1.0, -1.0}, {-1.0, 1.0}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);

        new RectangularCholeskyDecomposition(matrix, 1e-12);
    }

    @Test
    public void testEmptyMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[0][0]);

        try {
            new RectangularCholeskyDecomposition(matrix);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected
        }
    }

    @Test
    public void testIdentityMatrix() {
        double[][] matrixData = new double[][]{{1.0, 0.0}, {0.0, 1.0}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);

        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
            RealMatrix rootMatrix = decomposition.getRootMatrix();
            int rank = decomposition.getRank();

            assertEquals(2, rank);
            assertEquals(2, rootMatrix.getRowDimension());
            assertEquals(2, rootMatrix.getColumnDimension());

            // Check that the lower triangular matrix is correct
            double[][] rootData = rootMatrix.getData();
            assertEquals(1.0, rootData[0][0], 1e-12);
            assertEquals(0.0, rootData[0][1], 1e-12);
            assertEquals(0.0, rootData[1][0], 1e-12);
            assertEquals(1.0, rootData[1][1], 1e-12);

            // Check that the matrix can be reconstructed from the decomposition
            RealMatrix reconstructedMatrix = rootMatrix.transpose().multiply(rootMatrix);
            assertEquals(matrix, reconstructedMatrix, 1e-12);

        } catch (NonPositiveDefiniteMatrixException e) {
            // This should not be thrown for an identity matrix
            assertFalse(true);
        }
    }

    @Test
    public void testSingularValueDecomposition() {
        double[][] matrixData = new double[][]{{1.0, 2.0}, {2.0, 4.0}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);

        SingularValueDecomposition svd = new SingularValueDecomposition(matrix);
        double[] singularValues = svd.getSingularValues();

        try {
            new RectangularCholeskyDecomposition(matrix);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected
        }

        assertTrue(singularValues[1] <= 0.0);
    }
}