package org.apache.commons.math3.linear.test;

import org.apache.commons.math3.linear.*;
import org.junit.Assert;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testRectangularCholeskyDecomposition() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{3.0, 2.0, 1.0}, {2.0, 3.0, 1.0}, {1.0, 1.0, 3.0}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        RealMatrix expectedRoot = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {-0.3535533905932737, 0.8164965809277261, 0.0}, {-0.7071067811865576, -0.408248290463863, 1.0}});
        RealMatrix actualRoot = decomposition.getRootMatrix();
        Assert.assertEquals(expectedRoot, actualRoot);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteMatrixException() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{-1.0, 1.0}, {1.0, -1.0}});
        try {
            new RectangularCholeskyDecomposition(matrix, 0.0);
        } catch (NonPositiveDefiniteMatrixException e) {
            Assert.assertEquals(-1.0, e.getDeterminant(), 0.0);
            Assert.assertEquals(0, e.getRowIndex());
            Assert.assertEquals(0.0, e.getSmall());
        }
    }

    @Test
    public void testGetRootMatrixAndRank() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{3.0, 2.0, 1.0}, {2.0, 3.0, 1.0}, {1.0, 1.0, 3.0}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        RealMatrix expectedRoot = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {-0.3535533905932737, 0.8164965809277261, 0.0}, {-0.7071067811865576, -0.408248290463863, 1.0}});
        RealMatrix actualRoot = decomposition.getRootMatrix();
        int expectedRank = 3;
        int actualRank = decomposition.getRank();
        Assert.assertEquals(expectedRoot, actualRoot);
        Assert.assertEquals(expectedRank, actualRank);
    }
}