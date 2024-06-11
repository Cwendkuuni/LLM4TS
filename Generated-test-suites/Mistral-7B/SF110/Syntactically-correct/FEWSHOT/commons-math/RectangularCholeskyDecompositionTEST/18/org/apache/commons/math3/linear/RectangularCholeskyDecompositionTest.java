package org.apache.commons.math3.linear;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testEmptyMatrix() {
        try {
            new RectangularCholeskyDecomposition(MatrixUtils.createRealMatrix(0, 0));
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected
        }
    }

    @Test
    public void testIdentityMatrix() {
        RealMatrix matrix = MatrixUtils.createIdentityMatrix(5);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        RealMatrix root = decomposition.getRootMatrix();
        int rank = decomposition.getRank();
        assertEquals(5, rank);
        assertEquals(5, root.getRowDimension());
        assertEquals(rank, root.getColumnDimension());
        assertArrayEquals(new double[][]{
                {1.0, 0.0, 0.0, 0.0, 0.0},
                {0.0, 1.0, 0.0, 0.0, 0.0},
                {0.0, 0.0, 1.0, 0.0, 0.0},
                {0.0, 0.0, 0.0, 1.0, 0.0},
                {0.0, 0.0, 0.0, 0.0, 1.0}
        }, root.getData());
    }

    @Test
    public void testSingularMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 1.0},
                {1.0, 1.0}
        });
        try {
            new RectangularCholeskyDecomposition(matrix);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected
        }
    }

    @Test
    public void testNonPositiveDefiniteMatrixWithSmallValue() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 1.0},
                {1.0, -1.0}
        });
        try {
            new RectangularCholeskyDecomposition(matrix, 1e-10);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected
        }
    }

    @Test
    public void testPositiveDefiniteMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {4.0, 2.0, 2.0},
                {2.0, 4.0, 2.0},
                {2.0, 2.0, 4.0}
        });
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        RealMatrix root = decomposition.getRootMatrix();
        int rank = decomposition.getRank();
        assertEquals(3, rank);
        assertEquals(3, root.getRowDimension());
        assertEquals(rank, root.getColumnDimension());
        assertArrayEquals(new double[][]{
                {1.0, 0.0, 0.0},
                {0.714285714, 0.428283236, 0.0},
                {0.428283236, 1.0, 0.428283236}
        }, root.getData(), 0.001);
    }

    @Test
    public void testNonPositiveDefiniteMatrixWithNegativeValues() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0, 3.0},
                {2.0, 1.0, 2.0},
                {3.0, 2.0, 1.0}
        });
        try {
            new RectangularCholeskyDecomposition(matrix);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected
        }
    }

    @Test
    public void testNonPositiveDefiniteMatrixWithSmallNegativeValues() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 1.0, 1.0},
                {1.0, 1.0, -1.0},
                {1.0, -1.0, 1.0}
        });
        try {
            new RectangularCholeskyDecomposition(matrix, 1e-9);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected
        }
    }

    @Test
    public void testNonPositiveDefiniteMatrixWithZeroDiagonal() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {0.0, 1.0, 2.0},
                {1.0, 0.0, 3.0},
                {2.0, 3.0, 0.0}
        });
        try {
            new RectangularCholeskyDecomposition(matrix);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected
        }
    }

    @Test
    public void testNonPositiveDefiniteMatrixWithSmallZeroDiagonal() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {0.0, 1.0, 2.0},
                {1.0, 0.0, 3.0},
                {2.0, 3.0, 0.0}
        });
        try {
            new RectangularCholeskyDecomposition(matrix, 1e-12);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected
        }
    }

    @Test
    public void testNonPositiveDefiniteMatrixWithDiagonalBelowSmallValue() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 1.0, 1.0},
                {1.0, 1.0, -2.0},
                {1.0, -2.0, 1.0}
        });
        try {
            new RectangularCholeskyDecomposition(matrix, 1.0);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected
        }
    }

    @Test
    public void testNonPositiveDefiniteMatrixWithDiagonalBelowSmallValueButOnePositive() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 1.0, 1.0},
                {1.0, 1.0, -2.0},
                {1.0, 2.0, 1.0}
        });
        try {
            new RectangularCholeskyDecomposition(matrix, 1.0);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected
        }
    }

    @Test
    public void testNonPositiveDefiniteMatrixWithSmallDiagonal() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 1.0, 1.0},
                {1.0, 1.0, -3.0},
                {1.0, -3.0, 1.0}
        });
        try {
            new RectangularCholeskyDecomposition(matrix, 0.5);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected
        }
    }

    @Test
    public void testNonPositiveDefiniteMatrixWithSmallDiagonalButOnePositive() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 1.0, 1.0},
                {1.0, 1.0, -3.0},
                {1.0, 2.0, 1.0}
        });
        try {
            new RectangularCholeskyDecomposition(matrix, 0.5);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected
        }
    }

    @Test
    public void testPositiveDefiniteMatrixWithSmallNegativeValues() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 1.0, 1.0, 1.0},
                {1.0, 1.0, -2.0, 1.0},
                {1.0, -2.0, 1.0, 1.0},
                {1.0, 1.0, 1.0, 1.0}
        });
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-9);
        RealMatrix root = decomposition.getRootMatrix();
        int rank = decomposition.getRank();
        assertEquals(4, rank);
        assertEquals(4, root.getRowDimension());
        assertEquals(rank, root.getColumnDimension());
        assertArrayEquals(new double[][]{
                {1.0, 0.0, 0.0, 0.0},
                {0.632455532, 0.422580645, 0.0, 0.0},
                {0.335649919, 0.816496581, 0.422580645, 0.0},
                {0.335649919, 0.173131509, 0.816496581, 0.422580645}
        }, root.getData(), 0.001);
    }
}