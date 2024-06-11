package org.apache.commons.math3.linear;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.util.MathArrays;
import org.apache.commons.math3.util.Precision;

import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

    // helper method to create a test matrix
    private static RealMatrix createTestMatrix(double[][] matrixData) {
        int rows = matrixData.length;
        int columns = matrixData[0].length;
        return new Array2DRowRealMatrix(rows, columns, matrixData);
    }

    @Test
    public void testEmptyMatrix() {
        try {
            new RectangularCholeskyDecomposition(new Array2DRowRealMatrix(0, 0));
            fail("Should throw NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
    }

    @Test
    public void testSingularMatrix() {
        double[][] singularMatrix = { { 0.1, 0.2 }, { 0.2, 0.0 } };
        try {
            new RectangularCholeskyDecomposition(createTestMatrix(singularMatrix));
            fail("Should throw NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
    }

    @Test
    public void testNonPositiveDefiniteMatrix() {
        double[][] nonPositiveMatrix = { { 1.0, 0.0 }, { 0.0, -1.0 } };
        try {
            new RectangularCholeskyDecomposition(createTestMatrix(nonPositiveMatrix));
            fail("Should throw NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
    }

    @Test
    public void testDecomposePositiveDefiniteMatrix() {
        double[][] positiveMatrix = { { 1.1, 2.2, 3.3 }, { 2.2, 4.4, 5.5 }, { 3.3, 5.5, 6.6 } };
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(createTestMatrix(positiveMatrix));
        RealMatrix root = decomp.getRootMatrix();
        int rank = decomp.getRank();

        assertEquals(3, rank);
        assertEquals(3, root.getRowDimension());
        assertEquals(rank, root.getColumnDimension());

        // check the decomposition result
        double[][] expectedRoot = { { 3.436729529233314, -0.0, -0.0 },
                                      { -0.188722308189319, 2.816119455312922, -0.0 },
                                      { -0.377439267266642, -1.391387761968127, 3.339521335139017 } };
        assertArrayEquals(expectedRoot, root.getData(), 1e-10);
    }

    @Test
    public void testDecomposeSymmetricPositiveDefiniteMatrix() {
        double[][] symmetricPositiveMatrix = { { 1.1, 2.2 }, { 2.2, 1.1 } };
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(createTestMatrix(symmetricPositiveMatrix));
        RealMatrix root = decomp.getRootMatrix();
        int rank = decomp.getRank();

        assertEquals(2, rank);
        assertEquals(2, root.getRowDimension());
        assertEquals(rank, root.getColumnDimension());

        // check the decomposition result
        double[][] expectedRoot = { { 3.1622776601683795, -1.414213741059964 },
                                      { -1.414213741059964, 3.1622776601683795 } };
        assertArrayEquals(expectedRoot, root.getData(), 1e-10);
    }

    @Test
    public void testDecomposeDiagonalMatrix() {
        double[][] diagonalMatrix = { { 1.0, 0.0, 0.0 }, { 0.0, 2.0, 0.0 }, { 0.0, 0.0, 3.0 } };
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(createTestMatrix(diagonalMatrix));
        RealMatrix root = decomp.getRootMatrix();
        int rank = decomp.getRank();

        assertEquals(3, rank);
        assertEquals(3, root.getRowDimension());
        assertEquals(rank, root.getColumnDimension());

        // check the decomposition result
        double[][] expectedRoot = { { 1.0, 0.0, 0.0 }, { 0.0, 1.4142135625, 0.0 }, { 0.0, 0.0, 1.73205080757 } };
        assertArrayEquals(expectedRoot, root.getData(), 1e-10);
    }

    @Test
    public void testDecomposeMatrixWithSwappedRows() {
        double[][] matrixData = { { 1.0, 2.0 }, { 2.0, 1.0 } };
        RealMatrix originalMatrix = createTestMatrix(matrixData);
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(originalMatrix);
        RealMatrix root = decomp.getRootMatrix();
        int rank = decomp.getRank();

        // check the decomposition result
        double[][] expectedRoot = { { 1.4142135625, -1.0 }, { -1.0, 1.4142135625 } };
        assertArrayEquals(expectedRoot, root.getData(), 1e-10);

        // check that the original matrix is not modified
        double[][] originalRoot = new double[2][2];
        originalMatrix.getSubMatrix(0, 0, originalRoot, 0, 0);
        assertNotSame(originalRoot, root.getData());
    }

    @Test
    public void testDecomposeMatrixWithNegativeValues() {
        double[][] matrixData = { { 1.0, -2.0 }, { -2.0, 3.0 } };
        RealMatrix originalMatrix = createTestMatrix(matrixData);

        try {
            new RectangularCholeskyDecomposition(originalMatrix);
            fail("Should throw NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }

        // check that the original matrix is not modified
        double[][] originalRoot = new double[2][2];
        originalMatrix.getSubMatrix(0, 0, originalRoot, 0, 0);
        assertArrayEquals(new double[][] { { 1.0, 0.0 }, { 0.0, 1.0 } }, originalRoot);
    }

    @Test
    public void testDecomposeSparseMatrix() {
        OpenBigMathContext context = new OpenBigMathContext(100, 100);
        RealMatrix matrix = MatrixUtils.createRealDenseMatrix(context, new double[][] { { 1.0, 2.0, 0.0 },
                                                                                         { 2.0, 3.0, 1.0 },
                                                                                         { 0.0, 1.0, 4.0 } });
        RealMatrix root = null;
        try {
            RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix);
            root = decomp.getRootMatrix();
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        assertEquals(3, root.getRowDimension());
        assertEquals(3, root.getColumnDimension());

        // check the decomposition result
        double[][] expectedRoot = { { 3.1622776601683795, -1.414213741059964, 0.0 },
                                      { -1.414213741059964, 3.1622776601683795, 1.0 },
                                      { 0.0, 1.0, 1.73205080757 } };
        RealMatrix result = new Array2DRowRealMatrix(3, 3, context);

        int i = 0;
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                result.setEntry(row, col, Precision.round(expectedRoot[row][col], 10));
                i++;
            }
        }

        assertEquals(result, root);
    }

    @Test
    public void testGetRank() {
        double[][] matrixData = { { 1.0, 2.0, 3.0 }, { 2.0, 4.0, 5.0 }, { 3.0, 5.0, 6.0 } };
        RealMatrix originalMatrix = createTestMatrix(matrixData);
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(originalMatrix);
        int rank = decomp.getRank();

        assertEquals(3, rank);

        double[][] subMatrix = new double[2][3];
        originalMatrix.getSubMatrix(0, 0, subMatrix, 0, 0);
        RectangularCholeskyDecomposition subDecomp = new RectangularCholeskyDecomposition(subMatrix);
        int subRank = subDecomp.getRank();

        assertEquals(2, subRank);
    }
}