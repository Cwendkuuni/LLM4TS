package org.apache.commons.math3.linear;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.util.Arrays2D;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testConstructor_PositiveDefiniteMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 5.0, 6.0}, {3.0, 6.0, 10.0}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        RealMatrix root = decomposition.getRootMatrix();
        int rank = decomposition.getRank();
        assertEquals(3, rank);
        RealVector vector1 = root.operate(new ArrayRealVector(new double[]{1.0, 0.0, 0.0}));
        RealVector vector2 = root.operate(new ArrayRealVector(new double[]{0.0, 1.0, 0.0}));
        RealVector vector3 = root.operate(new ArrayRealVector(new double[]{0.0, 0.0, 1.0}));
        assertEquals(new ArrayRealVector(new double[]{1.0, 1.4142135625, 1.4142135625}, Precision.SAFE_DOUBLE_EPSILON), vector1);
        assertEquals(new ArrayRealVector(new double[]{-1.4142135625, 1.0, 0.0}, Precision.SAFE_DOUBLE_EPSILON), vector2);
        assertEquals(new ArrayRealVector(new double[]{0.0, -1.4142135625, 1.0}, Precision.SAFE_DOUBLE_EPSILON), vector3);
    }

    @Test
    public void testConstructor_PositiveDefiniteMatrix_Small() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 6.0}, {3.0, 6.0, 10.0}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-8);
        RealMatrix root = decomposition.getRootMatrix();
        int rank = decomposition.getRank();
        assertEquals(3, rank);
        RealVector vector1 = root.operate(new ArrayRealVector(new double[]{1.0, 0.0, 0.0}));
        RealVector vector2 = root.operate(new ArrayRealVector(new double[]{0.0, 1.0, 0.0}));
        RealVector vector3 = root.operate(new ArrayRealVector(new double[]{0.0, 0.0, 1.0}));
        assertEquals(new ArrayRealVector(new double[]{1.0, 1.4142135625, 1.4142135625}, Precision.SAFE_DOUBLE_EPSILON), vector1);
        assertEquals(new ArrayRealVector(new double[]{-1.4142135625, 1.0, 0.0}, Precision.SAFE_DOUBLE_EPSILON), vector2);
        assertEquals(new ArrayRealVector(new double[]{0.0, -1.4142135625, 1.0}, Precision.SAFE_DOUBLE_EPSILON), vector3);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructor_NonPositiveDefiniteMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, -1.0, 6.0}, {3.0, 6.0, -10.0}});
        new RectangularCholeskyDecomposition(matrix);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructor_NonPositiveDefiniteMatrix_Small() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, -1.0, 6.0}, {3.0, 6.0, -10.0}});
        new RectangularCholeskyDecomposition(matrix, 1e-8);
    }
}