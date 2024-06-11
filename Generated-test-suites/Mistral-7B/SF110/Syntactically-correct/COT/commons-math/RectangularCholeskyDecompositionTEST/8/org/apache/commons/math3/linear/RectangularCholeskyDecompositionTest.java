package org.apache.commons.math3.linear;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.PrecisionUtils;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testExceptionWhenMatrixIsNotPositiveDefinite() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, -1.0, 4.0}, {3.0, 4.0, -1.0}});
        new RectangularCholeskyDecomposition(matrix);
    }

    @Test
    public void testConstructorWithSmallValue() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 4.0}, {3.0, 4.0, 5.0}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-9);
        RealMatrix rootMatrix = decomposition.getRootMatrix();
        int rank = decomposition.getRank();
        assertEquals(3, rank);
        assertNotNull(rootMatrix);
        assertEquals(3, rootMatrix.getRowDimension());
        assertEquals(rank, rootMatrix.getColumnDimension());
    }

    @Test
    public void testConstructorWithoutSmallValue() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 4.0}, {3.0, 4.0, 5.0}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        RealMatrix rootMatrix = decomposition.getRootMatrix();
        int rank = decomposition.getRank();
        assertEquals(3, rank);
        assertNotNull(rootMatrix);
        assertEquals(3, rootMatrix.getRowDimension());
        assertEquals(rank, rootMatrix.getColumnDimension());

        double[][] expected = {{0.40824829, 0.0, 0.0}, {-0.63245065, 0.81649658, 0.0}, {-1.26491063, -1.63299216, 0.81649658}};
        assertArrayEquals(expected, rootMatrix.getData(), 1e-14);
    }

    @Test
    public void testGetRank() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 4.0}, {3.0, 4.0, 5.0}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        assertEquals(3, decomposition.getRank());

        RealMatrix singularMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 2.0}});
        RectangularCholeskyDecomposition singularDecomposition = new RectangularCholeskyDecomposition(singularMatrix);
        assertEquals(2, singularDecomposition.getRank());
    }
}