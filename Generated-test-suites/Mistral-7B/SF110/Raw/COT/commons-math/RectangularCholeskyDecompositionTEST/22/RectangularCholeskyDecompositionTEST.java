package org.apache.commons.math3.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructorWithNonPositiveDefiniteMatrix() throws NonPositiveDefiniteMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{-1.0, 2.0}, {2.0, -1.0}});
        new RectangularCholeskyDecomposition(matrix);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructorWithSmallNegativeDiagonal() throws NonPositiveDefiniteMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {2.0, -0.001}});
        new RectangularCholeskyDecomposition(matrix, 0.0);
    }

    @Test
    public void testGetRootMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{4.0, 1.0, 1.0}, {1.0, 2.0, 1.0}, {1.0, 1.0, 3.0}});
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix);
        RealMatrix expectedRoot = RealMatrixUtils.createRealMatrix(3, 3)
                .setEntry(0, 0, 2.0)
                .setEntry(0, 1, -1.0)
                .setEntry(0, 2, -1.0)
                .setEntry(1, 0, -1.0)
                .setEntry(1, 1, 2.0)
                .setEntry(1, 2, -1.0)
                .setEntry(2, 0, -1.0)
                .setEntry(2, 1, 1.0)
                .setEntry(2, 2, 1.5);

        RealMatrix root = decomp.getRootMatrix();
        assertEquals(expectedRoot, root);
    }

    @Test
    public void testGetRank() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{4.0, 1.0, 1.0}, {1.0, 2.0, 1.0}, {1.0, 1.0, 3.0}});
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix);
        int rank = decomp.getRank();
        assertEquals(3, rank);
    }

    @Test
    public void testConstructorWithIdentityMatrix() throws NonPositiveDefiniteMatrixException {
        RealMatrix matrix = RealMatrixUtils.createIdentityMatrix(3);
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix);
        RealMatrix expectedRoot = RealMatrixUtils.createIdentityMatrix(3);
        RealMatrix root = decomp.getRootMatrix();
        assertEquals(expectedRoot, root);
        int rank = decomp.getRank();
        assertEquals(3, rank);
    }

    @Test
    public void testConstructorWithSymmetricPositiveDefiniteMatrix() throws NonPositiveDefiniteMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{2.0, 1.0, 1.0}, {1.0, 2.0, 1.0}, {1.0, 1.0, 2.0}});
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix);
        RealMatrix expectedRoot = RealMatrixUtils.createRealMatrix(3, 3)
                .setEntry(0, 0, 1.4142135625)
                .setEntry(0, 1, 0.707106781186548)
                .setEntry(0, 2, 0.707106781186548)
                .setEntry(1, 0, 0.707106781186548)
                .setEntry(1, 1, 1.4142135625)
                .setEntry(1, 2, 0.707106781186548)
                .setEntry(2, 0, 0.707106781186548)
                .setEntry(2, 1, 0.707106781186548)
                .setEntry(2, 2, 1.4142135625);

        RealMatrix root = decomp.getRootMatrix();
        assertEquals(expectedRoot, root, 0.00001);
        int rank = decomp.getRank();
        assertEquals(3, rank);
    }

    @Test
    public void testConstructorWithDiagonalMatrix() throws NonPositiveDefiniteMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 2.0}, {0.0, 0.0}});
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix);
        RealMatrix expectedRoot = RealMatrixUtils.createRealMatrix(3, 2)
                .setEntry(0, 0, 1.0)
                .setEntry(1, 1, 1.4142135625)
                .setEntry(2, 2, 1.4142135625);

        RealMatrix root = decomp.getRootMatrix();
        assertEquals(expectedRoot, root, 0.00001);
        int rank = decomp.getRank();
        assertEquals(2, rank);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRankAfterSetRootMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{4.0, 1.0, 1.0}, {1.0, 2.0, 1.0}, {1.0, 1.0, 3.0}});
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix);
        RealMatrix root = decomp.getRootMatrix();
        decomp.setRootMatrix(new Array2DRowRealMatrix(new double[3][3]));
        int rank = decomp.getRank();
    }

    @Test
    public void testSetRootMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{4.0, 1.0, 1.0}, {1.0, 2.0, 1.0}, {1.0, 1.0, 3.0}});
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix);
        RealMatrix root = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}, {0.0, 0.0}});
        decomp.setRootMatrix(root);
        RealMatrix expectedRoot = root;
        RealMatrix rootMatrix = decomp.getRootMatrix();
        assertEquals(expectedRoot, rootMatrix);
    }
}