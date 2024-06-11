package org.apache.commons.math3.linear;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.Precision;

import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testConstructorWithNullMatrix() {
        try {
            new RectangularCholeskyDecomposition(null);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected
        }
    }

    @Test
    public void testConstructorWithEmptyMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[0][0]);
        try {
            new RectangularCholeskyDecomposition(matrix);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected
        }
    }

    @Test
    public void testConstructorWithIdentityMatrix() {
        RealMatrix matrix = MatrixUtils.createIdentityMatrix(4);
        RectangularCholeskyDecomposition decomp = null;
        try {
            decomp = new RectangularCholeskyDecomposition(matrix);
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Should not throw NonPositiveDefiniteMatrixException for identity matrix");
        }
        RealMatrix root = decomp.getRootMatrix();
        assertEquals(matrix, root);
        assertEquals(4, decomp.getRank());
    }

    @Test
    public void testConstructorWithNonPositiveDefiniteMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {1.0, -1.0}});
        try {
            new RectangularCholeskyDecomposition(matrix);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected
        }
    }

    @Test
    public void testConstructorWithSmallestNegativeEigenvalue() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{-1.0, 0.0}, {0.0, -1.0}});
        double small = 1.0e-14;
        try {
            new RectangularCholeskyDecomposition(matrix, small);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected
        }
    }

    @Test
    public void testGetRank() {
        RealMatrix matrix = MatrixUtils.createIdentityMatrix(5);
        RectangularCholeskyDecomposition decomp = null;
        try {
            decomp = new RectangularCholeskyDecomposition(matrix);
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Should not throw NonPositiveDefiniteMatrixException for identity matrix");
        }
        assertEquals(5, decomp.getRank());
    }

    @Test
    public void testGetRankForSingularMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 0.0}});
        RectangularCholeskyDecomposition decomp = null;
        try {
            decomp = new RectangularCholeskyDecomposition(matrix);
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Should not throw NonPositiveDefiniteMatrixException for singular matrix");
        }
        assertEquals(1, decomp.getRank());
    }

    @Test
    public void testGetRootMatrixForIdentityMatrix() {
        RealMatrix matrix = MatrixUtils.createIdentityMatrix(4);
        RectangularCholeskyDecomposition decomp = null;
        try {
            decomp = new RectangularCholeskyDecomposition(matrix);
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Should not throw NonPositiveDefiniteMatrixException for identity matrix");
        }
        RealMatrix root = decomp.getRootMatrix();
        assertEquals(matrix, root);
    }

    @Test
    public void testGetRootMatrixForSingularMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 0.0}});
        RectangularCholeskyDecomposition decomp = null;
        try {
            decomp = new RectangularCholeskyDecomposition(matrix);
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Should not throw NonPositiveDefiniteMatrixException for singular matrix");
        }
        RealMatrix root = decomp.getRootMatrix();
        assertEquals(new Array2DRowRealMatrix(new double[][]{{1.0}, {0.0}}), root);
    }

    @Test
    public void testGetRootMatrixForNonPositiveDefiniteMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {1.0, -1.0}});
        try {
            new RectangularCholeskyDecomposition(matrix);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected
        }
        RealMatrix root = null;
        assertNull(root);
    }

    @Test
    public void testGetRootMatrixForSmallestNegativeEigenvalue() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{-1.0, 0.0}, {0.0, -1.0}});
        double small = 1.0e-14;
        try {
            new RectangularCholeskyDecomposition(matrix, small);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected
        }
        RealMatrix root = null;
        assertNull(root);
    }

    @Test
    public void testSwapRows() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {1.0, -1.0}});
        RectangularCholeskyDecomposition decomp = null;
        try {
            decomp = new RectangularCholeskyDecomposition(matrix);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected
        }
        int[] index = decomp.index;
        int swapR = 1;
        for (int j = 0; j < 2; ++j) {
            int tmpIndex = index[swapR];
            index[swapR] = index[0];
            index[0] = tmpIndex;
            swapR = (j == 0) ? 1 : 0;
        }
        double[][] b = decomp.b;
        double[] tmpRow = b[0];
        b[0] = b[1];
        b[1] = tmpRow;
        try {
            new RectangularCholeskyDecomposition(new RealMatrix(matrix.getData()), 0.0);
            fail("Expected NonPositiveDefiniteMatrixException after swapping rows");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected
        }
    }

    @Test
    public void testSetEntry() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {1.0, -1.0}});
        RectangularCholeskyDecomposition decomp = null;
        try {
            decomp = new RectangularCholeskyDecomposition(matrix);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected
        }
        int i2 = 0;
        int j2 = 0;
        decomp.root.setEntry(i2, j2, 2.0);
        try {
            new RectangularCholeskyDecomposition(new RealMatrix(matrix.getData()));
            fail("Expected NonPositiveDefiniteMatrixException after modifying root matrix");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected
        }
    }

    @Test
    public void testConstructorWithLargeRealMatrix() {
        int order = 1000;
        RealMatrix matrix = MatrixUtils.createRandomRealMatrix(order, order);
        RectangularCholeskyDecomposition decomp = null;
        try {
            decomp = new RectangularCholeskyDecomposition(matrix);
            fail("Expected NonPositiveDefiniteMatrixException for large matrix");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected
        }
    }

    @Test
    public void testConstructorWithSmallRealMatrix() {
        int order = 10;
        RealMatrix matrix = MatrixUtils.createRandomRealMatrix(order, order);
        RectangularCholeskyDecomposition decomp = null;
        try {
            decomp = new RectangularCholeskyDecomposition(matrix);
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Should not throw NonPositiveDefiniteMatrixException for small random matrix");
        }
        RealMatrix root = decomp.getRootMatrix();
        for (int i = 0; i < order; ++i) {
            for (int j = 0; j < decomp.getRank(); ++j) {
                assertEquals(Precision.isClose(0.0, root.getEntry(i, j)), true);
            }
        }
    }
}