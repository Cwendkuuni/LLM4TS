package org.apache.commons.math3.linear;

import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.util.ArrayUtils;
import org.apache.commons.math3.util.FastMath;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testEmptyMatrix() {
        try {
            RealMatrix matrix = MatrixUtils.createRealMatrix(new int[]{0, 0});
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected
        }
    }

    @Test
    public void testIdentityMatrix() {
        RealMatrix matrix = MatrixUtils.createIdentityMatrix(3);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        RealMatrix rootMatrix = decomposition.getRootMatrix();
        assertEquals(rootMatrix.getRowDimension(), rootMatrix.getColumnDimension(), 3);
        Assert.assertEquals(MatrixUtils.createIdentityMatrix(3), rootMatrix);
        assertEquals(3, decomposition.getRank());
    }

    @Test
    public void testSingularMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 0.0}});
        try {
            new RectangularCholeskyDecomposition(matrix);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected
        }
    }

    @Test
    public void testPositiveDefiniteMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 5.0, 6.0}, {3.0, 6.0, 10.0}});
        RectangularCholeskyDecomposition decomposition = null;
        try {
            decomposition = new RectangularCholeskyDecomposition(matrix);
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Expected positive definite matrix");
        }
        RealMatrix rootMatrix = decomposition.getRootMatrix();
        assertNotNull(rootMatrix);
        assertEquals(3, rootMatrix.getRowDimension());
        assertEquals(3, rootMatrix.getColumnDimension());
        assertEquals(3, decomposition.getRank());
        Assert.assertArrayEquals(new double[][]{
                {0.4472136024993864, 0.1450032341561114, 0.7354164281629674},
                {-0.1450032341561114, 1.552368963133551, 1.2595592188190996},
                {-0.7354164281629674, 1.2595592188190996, 2.632843038851425}},
                rootMatrix.getData(), 0.001);
    }

    @Test
    public void testNegativeDefiniteMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{-1.0, 2.0, 3.0}, {2.0, -5.0, 6.0}, {3.0, 6.0, -10.0}});
        try {
            new RectangularCholeskyDecomposition(matrix, 1.0E-12);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected
        }
    }

    @Test
    public void testNonPositiveDefiniteMatrixWithSmallDiagonal() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 6.0}, {3.0, 6.0, 1.0}});
        try {
            new RectangularCholeskyDecomposition(matrix, 1.0E-15);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected
        }
    }

    @Test
    public void testNonPositiveDefiniteMatrixWithNegativeSubdiagonal() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 5.0, -6.0}, {3.0, -6.0, 10.0}});
        try {
            new RectangularCholeskyDecomposition(matrix, 1.0E-12);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected
        }
    }

    @Test
    public void testReordering() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{3.0, 2.0, 1.0}, {2.0, 1.0, 3.0}, {1.0, 3.0, 2.0}});
        try {
            new RectangularCholeskyDecomposition(matrix);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullMatrix() {
        new RectangularCholeskyDecomposition(null);
    }
}