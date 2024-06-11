package org.apache.commons.math3.linear;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testRectangularCholeskyDecompositionValidInput() {
        RealMatrix matrix = RealMatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 5.0}, {3.0, 5.0, 6.0}});
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, 0.0);
        assertEquals(3, decomp.getRank());
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testRectangularCholeskyDecompositionNonPositiveDefiniteMatrix() {
        RealMatrix matrix = RealMatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, -1.0, 5.0}, {3.0, 5.0, 6.0}});
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, 0.0);
    }

    @Test
    public void testRectangularCholeskyDecompositionSmallThresholdGreaterThanSmallestElement() {
        RealMatrix matrix = RealMatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 5.0}, {3.0, 5.0, 9.9e-9}});
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, 9.9e-9);
        assertEquals(3, decomp.getRank());
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testRectangularCholeskyDecompositionSmallThresholdLessThanSmallestElement() {
        RealMatrix matrix = RealMatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 5.0}, {3.0, 5.0, 9.9e-10}});
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, 9.9e-10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRectangularCholeskyDecompositionZeroThreshold() {
        RealMatrix matrix = RealMatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 5.0}, {3.0, 5.0, 6.0}});
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, 0.0d);
    }

    @Test
    public void testGetRankValidDecomposition() {
        RealMatrix matrix = RealMatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 5.0}, {3.0, 5.0, 6.0}});
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, 0.0);
        assertEquals(3, decomp.getRank());
    }

    @Test
    public void testGetRankDifferentRowOrder() {
        RealMatrix matrix = RealMatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {3.0, 2.0, 1.0}, {2.0, 4.0, 5.0}});
        RealMatrix expectedResult = RealMatrixUtils.createRealMatrix(new double[][]{{1.0, 1.0, 1.0}, {0.0, 1.4142135625, 0.7071067811}, {3.0, 0.7071067811, 1.3228317143}});
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, 0.0);
        MatrixUtils.assertEquals(expectedResult, decomp.getRootMatrix());
        assertEquals(3, decomp.getRank());
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testGetRankNonPositiveDefiniteMatrix() {
        RealMatrix matrix = RealMatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, -1.0, 5.0}, {3.0, 5.0, 6.0}});
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, 0.0);
    }

    @Test
    public void testGetRootMatrixValidDecomposition() {
        RealMatrix matrix = RealMatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 5.0}, {3.0, 5.0, 6.0}});
        RealMatrix expectedResult = RealMatrixUtils.createRealMatrix(new double[][]{{1.0, 1.0, 1.0}, {2.0, 1.4142135625, 0.7071067811}, {3.0, 0.7071067811, 1.3228317143}});
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, 0.0);
        MatrixUtils.assertEquals(expectedResult, decomp.getRootMatrix());
    }

    @Test
    public void testGetRootMatrixWithTolerance() {
        RealMatrix matrix = RealMatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 5.0}, {3.0, 5.0, 6.0}});
        RealMatrix expectedResult = RealMatrixUtils.createRealMatrix(new double[][]{{1.0, 1.0, 1.0}, {2.0, 1.4142135625, 0.7071067811}, {3.0, 0.7071067811, 1.3228317143}});
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, 1e-6);
        MatrixUtils.assertEquals(expectedResult, decomp.getRootMatrix(), 1e-6);
    }
}