package org.example;

import org.junit.Test;
import org.junit.Assert.*;
import org.apache.commons.math3.linear.*;

public class RectangularCholeskyDecompositionTest {
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructorNoArgumentsThrowsException() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{-1.0, 0.0}, {0.0, -1.0}});
        new RectangularCholeskyDecomposition(matrix);
    }

    @Test
    public void testConstructorNoArguments() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        RealMatrix root = decomposition.getRootMatrix();
        Assert.assertEquals(new double[][]{ {1.0, 0.0}, {0.0, 1.0} }, root.getData());
        Assert.assertEquals(2, decomposition.getRank());
    }

    @Test
    public void testConstructorSmallArgument() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-12);
        RealMatrix root = decomposition.getRootMatrix();
        Assert.assertEquals(new double[][]{ {1.0, 0.0}, {0.0, 1.0} }, root.getData());
        Assert.assertEquals(2, decomposition.getRank());
    }

    @Test
    public void testGetRootMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        RealMatrix root = decomposition.getRootMatrix();
        Assert.assertEquals(new double[][]{ {1.0, 0.0}, {0.0, 1.0} }, root.getData());
    }

    @Test
    public void testGetRank() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        Assert.assertEquals(2, decomposition.getRank());
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructorSingularMatrixThrowsException() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 0.0}});
        new RectangularCholeskyDecomposition(matrix);
    }

    @Test
    public void testConstructorNegativeDiagonalMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{-1.0, 0.0}, {0.0, -1.0}});
        RectangularCholeskyDecomposition decomposition = null;
        try {
            decomposition = new RectangularCholeskyDecomposition(matrix);
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected exception
        }
        Assert.assertNull(decomposition);
    }

    @Test
    public void testConstructorSmallDiagonalMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 0.5}});
        RectangularCholeskyDecomposition decomposition = null;
        try {
            decomposition = new RectangularCholeskyDecomposition(matrix, 1e-5);
        } catch (NonPositiveDefiniteMatrixException e) {
            Assert.assertTrue(e.getMessage().contains("near singular"));
        }
        Assert.assertNull(decomposition);
    }

    @Test
    public void testGetRankSingularMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 0.0}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        Assert.assertEquals(0, decomposition.getRank());
    }

    @Test
    public void testGetRankNegativeDiagonalMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{-1.0, 0.0}, {0.0, -1.0}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        Assert.assertEquals(-1, decomposition.getRank());
    }

    @Test
    public void testGetRankSmallDiagonalMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 0.5}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        Assert.assertEquals(-1, decomposition.getRank());
    }
}