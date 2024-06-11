package org.apache.commons.math3.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.ArrayUtils;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testConstructorWithPositiveDefiniteMatrix() {
        RealMatrix matrix = MatrixUtils.createRealSymmetricMatrix(new double[][]{{1.0, 2.5}, {2.5, 3.0}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        RealMatrix expectedResult = MatrixUtils.createRealSymmetricMatrix(new double[][]{{1.0, 1.5}, {1.5, 1.7320508075688772}});
        RealMatrix result = decomposition.getRootMatrix();
        assertArrayEquals(expectedResult.getData(), result.getData(), 0.001);
        assertEquals(2, decomposition.getRank());
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructorWithNegativeDefiniteMatrix() {
        RealMatrix matrix = MatrixUtils.createRealSymmetricMatrix(new double[][]{{-1.0, 2.0}, {2.0, -3.0}});
        new RectangularCholeskyDecomposition(matrix);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructorWithSingularMatrix() {
        RealMatrix matrix = MatrixUtils.createRealSymmetricMatrix(new double[][]{{0.0, 1.0}, {1.0, 0.0}});
        new RectangularCholeskyDecomposition(matrix, 1e-12);
    }

    @Test
    public void testConstructorWithSmallMatrix() {
        RealMatrix matrix = MatrixUtils.createRealSymmetricMatrix(new double[][]{{1.0, 2.5}, {2.5, 0.5}});
        try {
            new RectangularCholeskyDecomposition(matrix, 0.0);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            assertTrue(Precision.isClose(e.getDeterminant(), -0.524259312261323, 0.001));
            assertEquals(1, e.getFlattenedIndex());
            assertEquals(0.0, e.getSmallValue(), 0.001);
        }
    }

    @Test
    public void testConstructorWithSwappedMatrix() {
        RealMatrix matrix = MatrixUtils.createRealSymmetricMatrix(new double[][]{{2.5, 1.0}, {1.0, 2.5}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        RealMatrix expectedResult = MatrixUtils.createRealSymmetricMatrix(new double[][]{{1.5, 1.0}, {1.0, 1.7320508075688772}});
        RealMatrix result = decomposition.getRootMatrix();
        assertArrayEquals(expectedResult.getData(), result.getData(), 0.001);
        assertEquals(2, decomposition.getRank());
    }

    @Test
    public void testGetRank() {
        RealMatrix matrix = MatrixUtils.createRealSymmetricMatrix(new double[][]{{1.0, 2.5}, {2.5, 3.0}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        assertEquals(2, decomposition.getRank());

        matrix = MatrixUtils.createRealSymmetricMatrix(new double[][]{{-1.0, 2.0}, {2.0, -3.0}});
        try {
            new RectangularCholeskyDecomposition(matrix);
        } catch (NonPositiveDefiniteMatrixException e) {
            decomposition = new RectangularCholeskyDecomposition(matrix.subtract(ArrayUtils.scalarMultiply(matrix, 1e-10)));
        }
        assertEquals(0, decomposition.getRank());

        matrix = MatrixUtils.createRealSymmetricMatrix(new double[][]{{0.0, 1.0}, {1.0, 0.0}});
        try {
            new RectangularCholeskyDecomposition(matrix, 1e-12);
        } catch (NonPositiveDefiniteMatrixException e) {
            decomposition = new RectangularCholeskyDecomposition(matrix);
        }
        assertEquals(1, decomposition.getRank());
    }

    @Test
    public void testGetRootMatrix() {
        RealMatrix matrix = MatrixUtils.createRealSymmetricMatrix(new double[][]{{1.0, 2.5}, {2.5, 3.0}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        RealMatrix expectedResult = MatrixUtils.createRealSymmetricMatrix(new double[][]{{1.0, 1.5}, {1.5, 1.7320508075688772}});
        RealMatrix result = decomposition.getRootMatrix();
        assertArrayEquals(expectedResult.getData(), result.getData(), 0.001);

        matrix = MatrixUtils.createRealSymmetricMatrix(new double[][]{{-1.0, 2.0}, {2.0, -3.0}});
        try {
            new RectangularCholeskyDecomposition(matrix);
        } catch (NonPositiveDefiniteMatrixException e) {
            decomposition = new RectangularCholeskyDecomposition(matrix.subtract(ArrayUtils.scalarMultiply(matrix, 1e-10)));
        }
        RealMatrix resultSingular = decomposition.getRootMatrix();
        assertTrue(resultSingular.isIdentityMatrix());

        matrix = MatrixUtils.createRealSymmetricMatrix(new double[][]{{0.0, 1.0}, {1.0, 0.0}});
        try {
            new RectangularCholeskyDecomposition(matrix, 1e-12);
        } catch (NonPositiveDefiniteMatrixException e) {
            decomposition = new RectangularCholeskyDecomposition(matrix);
        }
        RealMatrix resultZero = decomposition.getRootMatrix();
        assertTrue(ArrayUtils.isSame(ArrayUtils.ZERO_VECTOR, resultZero.getColumnVector(0)));
    }
}