package org.apache.commons.math3.linear;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.Arrays2D;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testEmptyMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[0][0]);
        assertThrows(IllegalArgumentException.class, () -> new RectangularCholeskyDecomposition(matrix));
    }

    @Test
    public void testSingularMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 0.0}, {2.0, 1.0, 0.0}, {0.0, 0.0, 0.0}});
        assertThrows(NonPositiveDefiniteMatrixException.class, () -> new RectangularCholeskyDecomposition(matrix));
    }

    @Test
    public void testDiagonalMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        RealMatrix rootMatrix = decomposition.getRootMatrix();
        assertNotNull(rootMatrix);
        assertEquals(2, rootMatrix.getRowDimension());
        assertEquals(2, rootMatrix.getColumnDimension());
        assertArrayEquals(new double[][]{{1.0, 0.0}, {0.0, 1.0}}, rootMatrix.getData());
        int rank = decomposition.getRank();
        assertEquals(2, rank);
    }

    @Test
    public void testSymmetricMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 2.0}, {2.0, 1.0, 1.0}, {2.0, 1.0, 1.0}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        RealMatrix rootMatrix = decomposition.getRootMatrix();
        assertNotNull(rootMatrix);
        assertEquals(3, rootMatrix.getRowDimension());
        assertEquals(3, rootMatrix.getColumnDimension());
        int rank = decomposition.getRank();
        assertEquals(3, rank);

        double epsilon = 1.0e-15;
        assertArrayEquals(new double[][]{{1.0, 0.0, 0.0}, {0.0, 1.0 - epsilon, 1.0 - epsilon}, {0.0, 1.0 - epsilon, 1.0}},
                rootMatrix.getData(), epsilon);
    }

    @Test
    public void testNonSymmetricMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 2.0}, {2.0, 1.0, 1.0}, {2.0, 1.0, 1.0 + 1.0e-15}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        RealMatrix rootMatrix = decomposition.getRootMatrix();
        assertNotNull(rootMatrix);
        assertEquals(3, rootMatrix.getRowDimension());
        assertEquals(3, rootMatrix.getColumnDimension());
        int rank = decomposition.getRank();
        assertEquals(3, rank);

        double epsilon = 1.0e-15;
        assertArrayEquals(new double[][]{{1.0, 0.0, 0.0}, {0.0, 1.0 - epsilon, 1.0}, {0.0, 1.0 - epsilon, 1.0}},
                rootMatrix.getData(), epsilon);
    }

    @Test
    public void testNonPositiveDefiniteMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{-1.0, 2.0, 2.0}, {2.0, -1.0, 1.0}, {2.0, 1.0, -1.0}});
        assertThrows(NonPositiveDefiniteMatrixException.class, () -> new RectangularCholeskyDecomposition(matrix));
    }

    @Test
    public void testNonPositiveDefiniteMatrixWithSmallValue() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{-1.0, 2.0, 2.0}, {2.0, -1.0, 1.0}, {2.0, 1.0, -1.0 + 1.0e-15}});
        assertThrows(NonPositiveDefiniteMatrixException.class, () -> new RectangularCholeskyDecomposition(matrix, 1.0e-15));
    }
}