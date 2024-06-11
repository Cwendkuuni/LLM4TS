
package org.apache.commons.math3.linear;
import org.junit.Test;
import org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testRectangularCholeskyDecomposition() {
        // Given: A positive definite matrix
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{2.0, 1.0}, {1.0, 2.0}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);

        // Then: The decomposition should be successful
        Assert.assertNotNull(decomposition.getRootMatrix());
        Assert.assertEquals(2, decomposition.getRank());

        // And: The resulting root matrix should be the Cholesky decomposition of the input matrix
        RealMatrix expectedRoot = new Array2DRowRealMatrix(new double[][]{{1.4142135625, 0.7071067811}, {0.7071067811, 1.4142135625}});
        Assert.assertEquals(expectedRoot, decomposition.getRootMatrix());
    }

    @Test
    public void testRectangularCholeskyDecompositionWithSmallValue() {
        // Given: A positive definite matrix with a small value
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{2.0, 1.0, 0.1}, {1.0, 2.0, 0.1}, {0.1, 0.1, 2.1}});
        RectangularCholeskyDecomposition decomposition;
        try {
            decomposition = new RectangularCholeskyDecomposition(matrix, 0.01);
            fail("A NonPositiveDefiniteMatrixException should have been thrown");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Then: The exception should be thrown
            Assert.assertNotNull(e.getMessage());
        }
    }

    @Test
    public void testGetRank() {
        // Given: A successful RectangularCholeskyDecomposition
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{2.0, 1.0}, {1.0, 2.0}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);

        // Then: The rank should be equal to the number of positive eigenvalues
        Assert.assertEquals(2, decomposition.getRank());

        // And: If the matrix is singular, the rank should be less than the number of rows
        RealMatrix singularMatrix = new Array2DRowRealMatrix(new double[][]{{2.0, 1.0}, {1.0, 1.0}});
        RectangularCholeskyDecomposition singularDecomposition;
        try {
            singularDecomposition = new RectangularCholeskyDecomposition(singularMatrix);
            fail("A NonPositiveDefiniteMatrixException should have been thrown");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Do nothing, as expected
        }
        RectangularCholeskyDecomposition singularDecomposition2;
        try {
            singularDecomposition2 = new RectangularCholeskyDecomposition(singularMatrix, 0.0);
            Assert.assertEquals(1, singularDecomposition2.getRank());
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("The decomposition should have been successful, but an exception was thrown");
        }
    }
}