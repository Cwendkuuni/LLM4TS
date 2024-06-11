package org.apache.commons.math3.linear;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.MatrixUtils;
import org.junit.Test;
import org.junit.Assert;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testRectangularCholeskyDecomposition() {
        // Given: A positive definite matrix
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 6.0}, {3.0, 6.0, 9.0}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);

        // When: Getting the root matrix
        RealMatrix rootMatrix = decomposition.getRootMatrix();

        // Then: The root matrix should be correctly computed
        RealMatrix expectedRootMatrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.6, 1.0, 0.0}, {0.3, 1.5, 1.0}});
        Assert.assertEquals(expectedRootMatrix, rootMatrix);

        // Given: A non-positive definite matrix
        RealMatrix nonPositiveDefiniteMatrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, -1.0, 6.0}, {3.0, 6.0, -9.0}});

        // Then: An exception should be thrown
        try {
            new RectangularCholeskyDecomposition(nonPositiveDefiniteMatrix);
            Assert.fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected
        }
    }
}