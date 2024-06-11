package org.apache.commons.math3.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructorWithNonPositiveDefiniteMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{-1.0, 1.0}, {1.0, -1.0}});
        new RectangularCholeskyDecomposition(matrix);
    }

    @Test
    public void testGetRootMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 6.0}, {3.0, 6.0, 9.0}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.001);
        RealMatrix rootMatrix = decomposition.getRootMatrix();

        RealMatrix expected = MatrixUtils.createRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.7071067811865476, 0.7071067811865476, 0.0}, {-0.7071067811865476, 0.7071067811865476, 1.2247448713915895}});
        assertMatrixEquals(expected, rootMatrix);
    }

    @Test
    public void testGetRank() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 6.0}, {3.0, 6.0, 9.0}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.001);

        int expectedRank = 3;
        int actualRank = decomposition.getRank();
        assertEquals(expectedRank, actualRank);
    }

    @Test
    public void testConstructorWithSmallMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 1.0}, {1.0, 1.0}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.0001);

        int expectedRank = 2;
        int actualRank = decomposition.getRank();
        assertEquals(expectedRank, actualRank);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructorWithNegativeMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{-1.0, 1.0}, {1.0, -1.0}});
        new RectangularCholeskyDecomposition(matrix, 0.0);
    }

    @Test
    public void testConstructorWithSingularMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 0.0}});
        try {
            new RectangularCholeskyDecomposition(matrix, 0.0001);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected
        }
    }

    @Test
    public void testConstructorWithIdentityMatrix() {
        RealMatrix matrix = MatrixUtils.createIdentityMatrix(3);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.001);
        RealMatrix rootMatrix = decomposition.getRootMatrix();

        RealMatrix expected = MatrixUtils.createIdentityMatrix(3);
        assertMatrixEquals(expected, rootMatrix);
    }

    private void assertMatrixEquals(RealMatrix expected, RealMatrix actual) {
        for (int i = 0; i < expected.getRowDimension(); i++) {
            for (int j = 0; j < expected.getColumnDimension(); j++) {
                assertEquals(Precision.round(expected.getEntry(i, j), 15), Precision.round(actual.getEntry(i, j), 15), 0.0);
            }
        }
    }
}