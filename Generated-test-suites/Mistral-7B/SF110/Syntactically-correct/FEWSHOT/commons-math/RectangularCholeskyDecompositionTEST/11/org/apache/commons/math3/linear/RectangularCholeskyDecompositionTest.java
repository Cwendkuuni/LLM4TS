package org.apache.commons.math3.linear;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteMatrixException() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {1.0, -1.0, 1.0}, {3.0, 1.0, -1.0}});
        new RectangularCholeskyDecomposition(matrix, 0.0);
    }

    @Test
    public void testGetRootMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 6.0}, {3.0, 6.0, 9.0}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        RealMatrix rootMatrix = decomposition.getRootMatrix();
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.0, 1.4142135625, 1.4142135625}, {0.0, 1.4142135625, 2.4494897427}});
        assertEquals("The root matrix is not correct.", expected, rootMatrix);
    }

    @Test
    public void testGetRank() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 6.0}, {3.0, 6.0, 9.0}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        assertEquals("The rank is not correct.", 3, decomposition.getRank());
    }

    @Test
    public void testIdentityMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        RealMatrix rootMatrix = decomposition.getRootMatrix();
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});
        assertEquals("The root matrix is not correct.", expected, rootMatrix);
        assertEquals("The rank is not correct.", 2, decomposition.getRank());
    }

    @Test
    public void testSingularMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 0.0}});
        try {
            new RectangularCholeskyDecomposition(matrix, 0.0);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected exception
        }
        assertNull("The root matrix should be null.", decomposition.getRootMatrix());
        assertEquals("The rank is not correct.", 0, decomposition.getRank());
    }

    @Test
    public void testNegativeMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{-1.0, 0.0}, {0.0, 1.0}});
        try {
            new RectangularCholeskyDecomposition(matrix, 0.0);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected exception
        }
        assertNull("The root matrix should be null.", decomposition.getRootMatrix());
        assertEquals("The rank is not correct.", 0, decomposition.getRank());
    }

    @Test
    public void testSymmetricMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 1.0}});
        try {
            new RectangularCholeskyDecomposition(matrix, 0.0);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected exception
        }
        assertNull("The root matrix should be null.", decomposition.getRootMatrix());
        assertEquals("The rank is not correct.", 0, decomposition.getRank());
    }

    @Test
    public void testTriangularMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 0.0}, {1.0, 3.0, 2.0}, {0.0, 2.0, 3.0}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        RealMatrix rootMatrix = decomposition.getRootMatrix();
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.0, 1.4142135625, 1.4142135625}, {0.0, 1.4142135625, 2.4494897427}});
        assertEquals("The root matrix is not correct.", expected, rootMatrix);
        assertEquals("The rank is not correct.", 3, decomposition.getRank());
    }

    @Test
    public void testTriangularMatrixWithZeroOnDiagonal() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{0.0, 0.0, 0.0}, {0.0, 1.0, 1.0}, {0.0, 1.0, 2.0}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        RealMatrix rootMatrix = decomposition.getRootMatrix();
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.0, 1.0, 1.0}, {0.0, 0.0, 1.4142135625}});
        assertEquals("The root matrix is not correct.", expected, rootMatrix);
        assertEquals("The rank is not correct.", 3, decomposition.getRank());
    }

    @Test
    public void testTriangularMatrixWithSmallValues() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 0.0}, {2.0, 3.0, 1.0}, {0.0, 1.0, 0.1}});
        double small = 1e-8;
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
        RealMatrix rootMatrix = decomposition.getRootMatrix();
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.0, 1.5811388301, 1.5811388301}, {0.0, 1.5811388301, 1.6487212766}});
        assertEquals("The root matrix is not correct.", expected, rootMatrix, 1e-8);
        assertEquals("The rank is not correct.", 3, decomposition.getRank());
    }

    @Test
    public void testTriangularMatrixWithNegativeValues() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{-1.0, 2.0, 0.0}, {2.0, -3.0, 1.0}, {0.0, 1.0, -0.1}});
        double small = 1e-8;
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
        RealMatrix rootMatrix = decomposition.getRootMatrix();
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.0, 1.5811388301, 1.5811388301}, {0.0, 1.5811388301, 1.6487212766}});
        assertEquals("The root matrix is not correct.", expected, rootMatrix, 1e-8);
        assertEquals("The rank is not correct.", 3, decomposition.getRank());
    }

    @Test
    public void testTriangularMatrixWithNegativeValuesOnDiagonal() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{-1.0, 0.0, 0.0}, {0.0, 1.0, 1.0}, {0.0, 1.0, -2.0}});
        double small = 1e-8;
        try {
            new RectangularCholeskyDecomposition(matrix, small);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected exception
        }
        assertNull("The root matrix should be null.", decomposition.getRootMatrix());
        assertEquals("The rank is not correct.", 0, decomposition.getRank());
    }
}