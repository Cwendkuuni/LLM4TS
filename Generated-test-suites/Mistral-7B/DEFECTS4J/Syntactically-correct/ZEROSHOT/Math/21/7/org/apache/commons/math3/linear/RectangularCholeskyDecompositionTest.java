import org.apache.commons.math3.LinearAlgebraTests;
import org.apache.commons.math3.MatrixUtils;
import org.apache.commons.math3.RealMatrix;
import org.junit.Test;
import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testRectangularCholeskyDecomposition() {
        // Test with a positive semi-definite matrix
        RealMatrix matrix = MatrixUtils.createRealSymmetricMatrix(new double[][]{{1.0, 2.0, 2.0},
                                                                                  {2.0, 1.0, 1.0},
                                                                                  {2.0, 1.0, 1.0}});
        try {
            RectangularCholeskyDecomposition rectangularCholeskyDecomposition = new RectangularCholeskyDecomposition(matrix, 0.0);
            RealMatrix root = rectangularCholeskyDecomposition.getRootMatrix();
            int rank = rectangularCholeskyDecomposition.getRank();

            // Check the root matrix
            RealMatrix expectedRoot = MatrixUtils.createRealMatrix(new double[][]{{FastMath.SQRT(1.0), 0.0, 0.0},
                                                                                    {FastMath.SQRT(1.0), FastMath.SQRT(0.5), 0.0},
                                                                                    {FastMath.SQRT(1.0), -FastMath.SQRT(0.5), FastMath.SQRT(0.5)}});
            LinearAlgebraTests.assertEquals(expectedRoot, root, 1e-9);

            // Check the rank
            int expectedRank = 3;
            assertEquals(expectedRank, rank);

        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Unexpected NonPositiveDefiniteMatrixException: " + e.getMessage());
        }

        // Test with a non-positive definite matrix
        try {
            RealMatrix matrixNP = MatrixUtils.createRealSymmetricMatrix(new double[][]{{1.0, -1.0},
                                                                                         {-1.0, 1.0}});
            new RectangularCholeskyDecomposition(matrixNP, 0.0);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected
        }
    }
}