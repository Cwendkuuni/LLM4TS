package org.apache.commons.math3.linear;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.RandomUtils;
import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.PrecisionUtils;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructorWithNonPositiveDefiniteMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{-1.0, 1.0}, {1.0, -1.0}});
        new RectangularCholeskyDecomposition(matrix);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructorWithNonPositiveDefiniteMatrixSmall() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{-1.0, 1.0}, {1.0, -1.0}});
        new RectangularCholeskyDecomposition(matrix, 1e-15);
    }

    @Test
    public void testConstructor() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{4.0, 1.0, 1.0}, {1.0, 2.0, 1.0}, {1.0, 1.0, 3.0}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1.0 / Precision.sqrt(4.0), 0.0, -1.0 / Precision.sqrt(3.0)},
                {0.0, 1.0 / Precision.sqrt(2.0), 1.0 / Precision.sqrt(2.0)},
                {-1.0 / Precision.sqrt(3.0), 1.0 / Precision.sqrt(2.0), 1.0 / Precision.sqrt(3.0)}});
        RealMatrix actual = decomposition.getRootMatrix();
        assertArrayEquals(expected.getData(), actual.getData(), 1e-15);
    }

    @Test
    public void testGetRank() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{4.0, 1.0, 1.0}, {1.0, 2.0, 1.0}, {1.0, 1.0, 3.0}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        int actual = decomposition.getRank();
        int expected = 3;
        assertTrue(expected == actual);
    }

    @Test
    public void testGetRootMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{4.0, 1.0, 1.0}, {1.0, 2.0, 1.0}, {1.0, 1.0, 3.0}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        RealMatrix actual = decomposition.getRootMatrix();
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1.0 / Precision.sqrt(4.0), 0.0, -1.0 / Precision.sqrt(3.0)},
                {0.0, 1.0 / Precision.sqrt(2.0), 1.0 / Precision.sqrt(2.0)},
                {-1.0 / Precision.sqrt(3.0), 1.0 / Precision.sqrt(2.0), 1.0 / Precision.sqrt(3.0)}});
        assertArrayEquals(expected.getData(), actual.getData(), 1e-15);
    }

    @Test
    public void testConstructorWithIdentityMatrix() {
        RealMatrix matrix = MatrixUtils.createIdentityMatrix(5);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        RealMatrix expected = matrix;
        RealMatrix actual = decomposition.getRootMatrix();
        assertArrayEquals(expected.getData(), actual.getData(), 1e-15);
    }

    @Test
    public void testConstructorWithSymmetricMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{2.0, 1.0}, {1.0, 2.0}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        RealMatrix expected = MatrixUtils.createRealSymmetricMatrix(new double[][]{{Precision.sqrt(2.0), Precision.sqrt(2.0)/2.0},
                {Precision.sqrt(2.0)/2.0, Precision.sqrt(2.0)}});
        RealMatrix actual = decomposition.getRootMatrix();
        assertArrayEquals(expected.getData(), actual.getData(), 1e-15);
    }

    @Test
    public void testConstructorWithSingularMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 0.0}});
        try {
            new RectangularCholeskyDecomposition(matrix);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected
        }
    }

    @Test
    public void testGetRankWithSingularMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 0.0}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        int actual = decomposition.getRank();
        int expected = 0;
        assertTrue(expected == actual);
    }
}