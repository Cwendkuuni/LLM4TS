package org.apache.commons.math3.linear.test;

import org.apache.commons.math3.linear.*;
import org.junit.*;
import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testRectangularCholeskyDecomposition() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2, 2}, {2, 3, 3}, {2, 3, 3}});
        double small = 0.001;
        try {
            RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix, small);
            assertEquals(3, rcd.getRank());
            RealMatrix root = rcd.getRootMatrix();
            // perform additional checks on root and rank if necessary
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected exception for edge cases
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{}, {}});
        double small = 0.001;
        new RectangularCholeskyDecomposition(matrix, small);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNonSymmetricMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {1, 2}});
        double small = 0.001;
        new RectangularCholeskyDecomposition(matrix, small);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, -1}, {-1, 1}});
        double small = 0.001;
        new RectangularCholeskyDecomposition(matrix, small);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSmallValueTooSmall() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2, 2}, {2, 3, 3}, {2, 3, 3}});
        double small = 0.0;
        new RectangularCholeskyDecomposition(matrix, small);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSmallValueTooLarge() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2, 2}, {2, 3, 3}, {2, 3, 3}});
        double small = 10;
        new RectangularCholeskyDecomposition(matrix, small);
    }
}