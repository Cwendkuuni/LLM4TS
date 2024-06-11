package org.apache.commons.math3.linear;

import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.junit.Test;
import org.junit.Assert.*;

/**
 * Test for {@link RectangularCholeskyDecomposition} class.
 */
public class RectangularCholeskyDecompositionTest {

    /**
     * Test decomposition of a positive definite matrix.
     */
    @Test
    public void testDecompositionPositiveDefinite() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {1.0, 2.0, 3.0},
            {2.0, 5.0, 6.0},
            {3.0, 6.0, 10.0}
        });

        RectangularCholeskyDecomposition decompositor = new RectangularCholeskyDecomposition(matrix, 0.0);

        RealMatrix expectedRoot = new Array2DRowRealMatrix(new double[][]{
            {0.577350269189626, -0.577350269189626, -0.816496580927726},
            {1.154700538341273, 0.0, -1.322836635294142},
            {1.664928739422511, 1.154700538341273, 1.414213741287123}
        });

        RealMatrix root = decompositor.getRootMatrix();

        Assert.assertEquals(expectedRoot.getRowDimension(), root.getRowDimension());
        Assert.assertEquals(expectedRoot.getColumnDimension(), root.getColumnDimension());
        Assert.assertArrayEquals(expectedRoot.getData(), root.getData(), 1e-14);
    }

    /**
     * Test decomposition of a positive semidefinite matrix with small diagonal values.
     */
    @Test
    public void testDecompositionPositiveSemidefinite() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {1.0, 2.0, 3.0},
            {2.0, 0.1, 4.0},
            {3.0, 4.0, 0.1}
        });

        try {
            RectangularCholeskyDecomposition decompositor = new RectangularCholeskyDecomposition(matrix, 0.1);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }

        double small = 0.01;
        RectangularCholeskyDecomposition decompositor = new RectangularCholeskyDecomposition(matrix, small);

        RealMatrix expectedRoot = new Array2DRowRealMatrix(new double[][]{
            {0.546512232649660, -0.546512232649660, -0.801406522369233},
            {1.130228110243953, 0.0, -1.233240993354506},
            {1.660840345060377, 1.130228110243953, 1.413273182221481}
        });

        RealMatrix root = decompositor.getRootMatrix();

        Assert.assertEquals(expectedRoot.getRowDimension(), root.getRowDimension());
        Assert.assertEquals(expectedRoot.getColumnDimension(), root.getColumnDimension());
        Assert.assertArrayEquals(expectedRoot.getData(), root.getData(), 1e-14);
    }

    /**
     * Test decomposition of a non-positive definite matrix.
     */
    @Test
    public void testDecompositionNonPositiveDefinite() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {1.0, 2.0, 3.0},
            {2.0, -1.0, 4.0},
            {3.0, 4.0, -1.0}
        });

        try {
            RectangularCholeskyDecomposition decompositor = new RectangularCholeskyDecomposition(matrix, 0.0);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
    }

    /**
     * Test getRank() method.
     */
    @Test
    public void testGetRank() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {1.0, 2.0, 3.0},
            {2.0, 5.0, 6.0},
            {3.0, 6.0, 10.0}
        });

        RectangularCholeskyDecomposition decompositor = null;
        try {
            decompositor = new RectangularCholeskyDecomposition(matrix, 0.0);
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Expected positive semidefinite matrix.");
        }

        int expectedRank = 3;
        int rank = decompositor.getRank();

        Assert.assertEquals(expectedRank, rank);

        matrix = new Array2DRowRealMatrix(new double[][]{
            {1.0, 2.0, 3.0},
            {2.0, 0.1, 4.0},
            {3.0, 4.0, 0.1}
        });

        try {
            decompositor = new RectangularCholeskyDecomposition(matrix, 0.1);
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Expected positive semidefinite matrix.");
        }

        SingularValueDecomposition svd = new SingularValueDecomposition(matrix);
        int rankWithSingularValues = svd.getRank();

        expectedRank = rankWithSingularValues;
        rank = decompositor.getRank();

        Assert.assertEquals(expectedRank, rank);
    }

    /**
     * Test getRootMatrix() method.
     */
    @Test
    public void testGetRootMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {1.0, 2.0, 3.0},
            {2.0, 5.0, 6.0},
            {3.0, 6.0, 10.0}
        });

        RectangularCholeskyDecomposition decompositor = null;
        try {
            decompositor = new RectangularCholeskyDecomposition(matrix, 0.0);
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Expected positive semidefinite matrix.");
        }

        RealMatrix expectedRoot = new Array2DRowRealMatrix(new double[][]{
            {0.577350269189626, -0.577350269189626, -0.816496580927726},
            {1.154700538341273, 0.0, -1.322836635294142},
            {1.664928739422511, 1.154700538341273, 1.414213741287123}
        });

        RealMatrix root = decompositor.getRootMatrix();

        Assert.assertEquals(expectedRoot.getRowDimension(), root.getRowDimension());
        Assert.assertEquals(expectedRoot.getColumnDimension(), root.getColumnDimension());
        Assert.assertArrayEquals(expectedRoot.getData(), root.getData(), 1e-14);

        matrix = new Array2DRowRealMatrix(new double[][]{
            {1.0, 2.0, 3.0},
            {2.0, 0.1, 4.0},
            {3.0, 4.0, 0.1}
        });

        try {
            decompositor = new RectangularCholeskyDecomposition(matrix, 0.1);
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Expected positive semidefinite matrix.");
        }

        expectedRoot = new Array2DRowRealMatrix(new double[][]{
            {0.546512232649660, -0.546512232649660, -0.801406522369233},
            {1.130228110243953, 0.0, -1.233240993354506},
            {1.660840345060377, 1.130228110243953, 1.413273182221481}
        });

        root = decompositor.getRootMatrix();

        Assert.assertEquals(expectedRoot.getRowDimension(), root.getRowDimension());
        Assert.assertEquals(expectedRoot.getColumnDimension(), root.getColumnDimension());
        Assert.assertArrayEquals(expectedRoot.getData(), root.getData(), 1e-14);
    }

    /**
     * Test getRootMatrix() method with empty matrix.
     */
    @Test
    public void testGetRootMatrixEmpty() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[0][0]);

        try {
            new RectangularCholeskyDecomposition(matrix, 0.0);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }

        try {
            RectangularCholeskyDecomposition decompositor = new RectangularCholeskyDecomposition(matrix, 0.1);
            RealMatrix root = decompositor.getRootMatrix();
            fail("Expected NonPositiveDefiniteMatrixException with empty matrix.");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
    }

    /**
     * Test getRootMatrix() method with singular matrix.
     */
    @Test
    public void testGetRootMatrixSingular() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {1.0, 0.0},
            {0.0, 0.0}
        });

        try {
            new RectangularCholeskyDecomposition(matrix, 0.0);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }

        try {
            RectangularCholeskyDecomposition decompositor = new RectangularCholeskyDecomposition(matrix, 0.1);
            RealMatrix root = decompositor.getRootMatrix();
            fail("Expected NonPositiveDefiniteMatrixException with singular matrix.");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
    }
}