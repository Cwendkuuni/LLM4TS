package org.apache.commons.math3.linear;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.Pair;
import org.junit.Test;
import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testIdentityMatrix() {
        double small = 1e-6;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
            RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1.0 / FastMath.sqrt(1.0), 0.0}, {0.0, 1.0 / FastMath.sqrt(1.0)}});
            assertEquals(expected, decomposition.getRootMatrix());
            assertEquals(2, decomposition.getRank());
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testSymmetricPositiveDefiniteMatrix() {
        double small = 1e-6;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 2.0, 1.0},
                {2.0, 5.0, 5.0, 2.0},
                {2.0, 5.0, 10.0, 5.0},
                {1.0, 2.0, 5.0, 1.0}});
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
            RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1.0 / FastMath.sqrt(1.0), 1.0 / FastMath.sqrt(2.0), 0.0, 1.0 / FastMath.sqrt(2.0)},
                    {0.0, (1.0 / FastMath.sqrt(2.0) + 1.0 / FastMath.sqrt(5.0)) / 2.0, 1.0 / FastMath.sqrt(5.0), (1.0 / FastMath.sqrt(2.0) - 1.0 / FastMath.sqrt(5.0)) / 2.0},
                    {0.0, (1.0 / FastMath.sqrt(5.0) + 1.0 / FastMath.sqrt(10.0)) / 2.0, 1.0 / FastMath.sqrt(10.0), (1.0 / FastMath.sqrt(5.0) - 1.0 / FastMath.sqrt(10.0)) / 2.0},
                    {1.0 / FastMath.sqrt(2.0), (1.0 / FastMath.sqrt(2.0) - 1.0 / FastMath.sqrt(5.0)) / 2.0, (1.0 / FastMath.sqrt(5.0) - 1.0 / FastMath.sqrt(10.0)) / 2.0, 1.0 / FastMath.sqrt(1.0)}});
            assertEquals(expected, decomposition.getRootMatrix());
            assertEquals(4, decomposition.getRank());
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testSymmetricPositiveSemidefiniteMatrix() {
        double small = 1e-6;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 2.0, 1.0},
                {2.0, 5.0, 5.0, 2.0},
                {2.0, 5.0, 10.0, 5.0},
                {1.0, 2.0, 5.0, 1.0},
                {1.0, 2.0, 5.0, 1.0}});
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
            RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1.0 / FastMath.sqrt(1.0), 1.0 / FastMath.sqrt(2.0), 0.0, 1.0 / FastMath.sqrt(2.0)},
                    {0.0, (1.0 / FastMath.sqrt(2.0) + 1.0 / FastMath.sqrt(5.0)) / 2.0, 1.0 / FastMath.sqrt(5.0), (1.0 / FastMath.sqrt(2.0) - 1.0 / FastMath.sqrt(5.0)) / 2.0},
                    {0.0, (1.0 / FastMath.sqrt(5.0) + 1.0 / FastMath.sqrt(10.0)) / 2.0, 1.0 / FastMath.sqrt(10.0), (1.0 / FastMath.sqrt(5.0) - 1.0 / FastMath.sqrt(10.0)) / 2.0},
                    {1.0 / FastMath.sqrt(2.0), (1.0 / FastMath.sqrt(2.0) - 1.0 / FastMath.sqrt(5.0)) / 2.0, (1.0 / FastMath.sqrt(5.0) - 1.0 / FastMath.sqrt(10.0)) / 2.0, (1.0 + 1.0) / 2.0},
                    {1.0 / FastMath.sqrt(2.0), (1.0 / FastMath.sqrt(2.0) - 1.0 / FastMath.sqrt(5.0)) / 2.0, (1.0 / FastMath.sqrt(5.0) - 1.0 / FastMath.sqrt(10.0)) / 2.0, (1.0 + 1.0) / 2.0}});
            assertEquals(expected, decomposition.getRootMatrix());
            assertEquals(5, decomposition.getRank());
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testNonPositiveDefiniteMatrix() {
        double small = 1e-6;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {1.0, -1.0}});
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            assertEquals(-1.0, e.getDiagonalElement(), Precision.EPSILON);
            assertEquals(0, e.getRow());
        }
    }

    @Test
    public void testVerySmallDiagonalElements() {
        double small = 1e-16;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0, 1.0, 1.0},
                {1.0, 1e-6, 1e-6, 1e-6},
                {1.0, 1e-6, 1e-6, 1e-6},
                {1.0, 1e-6, 1e-6, 1.0}});
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
    }

    @Test
    public void testEqualColumns() {
        double small = 1e-6;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0, 1.0},
                {1.0, 1.0, 1.0},
                {1.0, 1.0, 1.0}});
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
            RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1.0 / FastMath.sqrt(3.0), 1.0 / FastMath.sqrt(3.0), 1.0 / FastMath.sqrt(3.0)},
                    {1.0 / FastMath.sqrt(3.0), 1.0 / FastMath.sqrt(3.0), 1.0 / FastMath.sqrt(3.0)},
                    {1.0 / FastMath.sqrt(3.0), 1.0 / FastMath.sqrt(3.0), 1.0 / FastMath.sqrt(3.0)}});
            assertEquals(expected, decomposition.getRootMatrix());
            assertEquals(3, decomposition.getRank());
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testZeroMatrix() {
        double small = 1e-6;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[0][0]);
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

}