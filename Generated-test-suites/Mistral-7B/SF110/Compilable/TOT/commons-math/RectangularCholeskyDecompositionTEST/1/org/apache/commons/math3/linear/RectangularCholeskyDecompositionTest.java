
package org.apache.commons.math3.linear;
import org.junit.Assert;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testRectangularCholeskyDecompositionPositive() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2, 3}, {2, 4, 6}, {3, 6, 10}});
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix);
        RealMatrix expectedRoot = new Array2DRowRealMatrix(new double[][]{{1, 0, 0}, {0.44948974278317, 0.81650635427417, 0}, {0, 0.61232259477711, 2.1655542359281}});
        RealMatrix actualRoot = decomp.getRootMatrix();
        Assert.assertEquals(expectedRoot, actualRoot);
        Assert.assertEquals(3, decomp.getRank());
    }

    @Test
    public void testRectangularCholeskyDecompositionSmall() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2, 3}, {2, 4, 6}, {3, 6, 10}});
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, 0.01);
        RealMatrix expectedRoot = new Array2DRowRealMatrix(new double[][]{{1, 0, 0}, {0.44948974278317, 0.81650635427417, 0}, {0, 0.61232259477711, 2.1655542359281}});
        RealMatrix actualRoot = decomp.getRootMatrix();
        Assert.assertEquals(expectedRoot, actualRoot);
        Assert.assertEquals(3, decomp.getRank());
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteMatrixException() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, -1}, {-1, 1}});
        new RectangularCholeskyDecomposition(matrix);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteMatrixExceptionSmall() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, -1}, {-1, 1}});
        new RectangularCholeskyDecomposition(matrix, 0.0);
    }

    @Test
    public void testSingularMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 0}, {0, 1}});
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix);
        RealMatrix expectedRoot = new Array2DRowRealMatrix(new double[][]{{1, 0}, {0, 1}});
        RealMatrix actualRoot = decomp.getRootMatrix();
        Assert.assertEquals(expectedRoot, actualRoot);
        Assert.assertEquals(2, decomp.getRank());
    }

    @Test
    public void testIdentityMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 0}, {0, 1}});
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix);
        RealMatrix expectedRoot = new Array2DRowRealMatrix(new double[][]{{1, 0}, {0, 1}});
        RealMatrix actualRoot = decomp.getRootMatrix();
        Assert.assertEquals(expectedRoot, actualRoot);
        Assert.assertEquals(2, decomp.getRank());
    }
}