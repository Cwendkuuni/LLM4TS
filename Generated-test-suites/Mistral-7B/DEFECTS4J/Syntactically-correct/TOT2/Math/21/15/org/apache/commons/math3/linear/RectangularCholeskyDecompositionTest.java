package org.apache.commons.math3.linear.test;

import org.apache.commons.math3.linear.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testConstructor() {
        // test case 1: Symmetric Positive Semidefinite matrix
        double small = 0.001;
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{
                {1.0, 2.0, 3.0},
                {2.0, 4.0, 5.0},
                {3.0, 5.0, 6.0}
        });

        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
        RealMatrix root = decomposition.getRootMatrix();
        int rank = decomposition.getRank();

        // check that the constructor has been executed correctly
        assertEquals(3, root.getRowDimension());
        assertEquals(rank, root.getColumnDimension());
        assertTrue(root instanceof RealMatrix);
        // check the decomposition result
        assertArrayEquals(new double[][]{
                {1.0, 0.0, 0.0},
                {0.63245553203361, 0.422580645161297, 0.0},
                {0.34532941176694, 0.218252733896326, 0.408248290463863}
        }, root.getData(), 0.001);
        // check the rank
        assertEquals(2, rank);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructorWithNonPositiveDefiniteMatrix() {
        // test case 2: non-positive definite matrix
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{
                {1.0, 2.0, -1.0},
                {2.0, -1.0, 3.0},
                {-1.0, 3.0, 1.0}
        });

        double small = 0.0;
        new RectangularCholeskyDecomposition(matrix, small);
        fail("Expected NonPositiveDefiniteMatrixException");
    }

    @Test
    public void testGetRootMatrix() {
        // test case 3: symmetric positive semidefinite matrix
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{
                {1.0, 2.0, 3.0},
                {2.0, 4.0, 5.0},
                {3.0, 5.0, 6.0}
        });
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.0);
        RealMatrix root = decomposition.getRootMatrix();

        // check that the root matrix is as expected
        assertArrayEquals(new double[][]{
                {1.0, 0.0, 0.0},
                {0.63245553203361, 0.422580645161297, 0.0},
                {0.34532941176694, 0.218252733896326, 0.408248290463863}
        }, root.getData(), 0.001);

        // check that the root matrix is correctly transposed
        RealMatrix transposedRoot = root.transpose();
        assertArrayEquals(new double[][]{
                {1.0, 0.63245553203361, 0.34532941176694},
                {0.0, 0.422580645161297, 0.218252733896326},
                {0.0, 0.0, 0.408248290463863}
        }, transposedRoot.getData(), 0.001);
    }

    @Test
    public void testGetRank() {
        // test case 4: symmetric positive semidefinite matrix
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{
                {1.0, 2.0, 3.0},
                {2.0, 4.0, 5.0},
                {3.0, 5.0, 6.0}
        });
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.0);
        int rank = decomposition.getRank();

        // check the rank
        assertEquals(2, rank);
    }

    // ... (previous tests)

    @Test
    public void testConstructorWithSingularMatrix() {
        // test case 5: singular matrix
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{
                {1.0, 2.0, 3.0},
                {2.0, 2.0, 2.0},
                {3.0, 2.0, 2.0}
        });
        double small = 0.0;

        try {
            new RectangularCholeskyDecomposition(matrix, small);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
    }

    @Test
    public void testConstructorWithNonSymmetricMatrix() {
        // test case 6: non-symmetric matrix
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{
                {1.0, 2.0},
                {3.0, 4.0}
        });
        double small = 0.0;

        try {
            new RectangularCholeskyDecomposition(matrix, small);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
    }

    @Test
    public void testGetRankWithDifferentThresholdValues() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{
                {1.0, 2.0, 3.0},
                {2.0, 4.0, 5.0},
                {3.0, 5.0, 6.0}
        });

        // test with small threshold
        RectangularCholeskyDecomposition decompositionSmall = new RectangularCholeskyDecomposition(matrix, 0.001);
        int rankSmall = decompositionSmall.getRank();
        assertEquals(2, rankSmall);

        // test with large threshold
        RectangularCholeskyDecomposition decompositionLarge = new RectangularCholeskyDecomposition(matrix, 0.1);
        int rankLarge = decompositionLarge.getRank();
        assertEquals(3, rankLarge);
    }

}