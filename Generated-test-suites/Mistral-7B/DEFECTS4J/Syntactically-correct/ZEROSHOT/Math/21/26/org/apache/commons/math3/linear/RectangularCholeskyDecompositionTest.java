import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testRectangularCholeskyDecomposition() {
        // Test case 1: Positive Semidefinite Matrix
        double[][] matrixData = {
                {2.0, 1.0, 1.0},
                {1.0, 2.0, 1.0},
                {1.0, 1.0, 1.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData);
        try {
            RectangularCholeskyDecomposition rectangularCholeskyDecomposition = new RectangularCholeskyDecomposition(matrix, 0.0);
            RealMatrix rootMatrix = rectangularCholeskyDecomposition.getRootMatrix();
            int rank = rectangularCholeskyDecomposition.getRank();
            double[][] rootData = rootMatrix.getData();
            assertEquals(3, rootData.length);
            assertEquals(rank, rootData[0].length);
            assertEquals(3, rootData[0].length);
            assertEquals(1.0 / FastMath.sqrt(2.0), rootData[0][0], 1e-10);
            assertEquals(1.0 / FastMath.sqrt(2.0), rootData[1][0], 1e-10);
            assertEquals(1.0 / FastMath.sqrt(2.0), rootData[2][0], 1e-10);
            assertEquals(1.0 / FastMath.sqrt(2.0), rootData[0][1], 1e-10);
            assertEquals(-1.0 / (2.0 * FastMath.sqrt(2.0)), rootData[0][2], 1e-10);
            assertEquals(1.0 / (2.0 * FastMath.sqrt(2.0)), rootData[1][1], 1e-10);
            assertEquals(-1.0 / (2.0 * FastMath.sqrt(2.0)), rootData[1][2], 1e-10);
            assertEquals(-1.0 / (2.0 * FastMath.sqrt(2.0)), rootData[2][1], 1e-10);
            assertEquals(1.0 / (2.0 * FastMath.sqrt(2.0)), rootData[2][2], 1e-10);
            RealMatrix expectedBTranspose = RealMatrixUtils.createRealMatrix(new double[][]{{1.0 / FastMath.sqrt(2.0), 1.0 / FastMath.sqrt(2.0), 1.0 / FastMath.sqrt(2.0)},
                    {-1.0 / (2.0 * FastMath.sqrt(2.0)), 1.0 / (2.0 * FastMath.sqrt(2.0)), -1.0 / (2.0 * FastMath.sqrt(2.0))},
                    {-1.0 / (2.0 * FastMath.sqrt(2.0)), -1.0 / (2.0 * FastMath.sqrt(2.0)), 1.0 / (2.0 * FastMath.sqrt(2.0))}});
            RealMatrix expectedResult = rootMatrix.transpose().multiply(rootMatrix);
            RealMatrix result = matrix.multiply(expectedResult);
            assertEquals(Precision.safeEqual(1.0, result.getEntry(0, 0), 1e-10), true);
            assertEquals(Precision.safeEqual(1.0, result.getEntry(1, 1), 1e-10), true);
            assertEquals(Precision.safeEqual(1.0, result.getEntry(2, 2), 1e-10), true);
            assertEquals(Precision.safeEqual(0.0, result.getEntry(0, 1), 1e-10), true);
            assertEquals(Precision.safeEqual(0.0, result.getEntry(0, 2), 1e-10), true);
            assertEquals(Precision.safeEqual(0.0, result.getEntry(1, 0), 1e-10), true);
            assertEquals(Precision.safeEqual(0.0, result.getEntry(1, 2), 1e-10), true);
            assertEquals(Precision.safeEqual(0.0, result.getEntry(2, 0), 1e-10), true);
            assertEquals(Precision.safeEqual(0.0, result.getEntry(2, 1), 1e-10), true);
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Unexpected NonPositiveDefiniteMatrixException: " + e.getMessage());
        }

        // Test case 2: Non Positive Definite Matrix
        double[][] nonPositiveDefiniteMatrixData = {
                {2.0, 1.0, 1.0},
                {1.0, -1.0, 1.0},
                {1.0, 1.0, 1.0}
        };
        RealMatrix nonPositiveDefiniteMatrix = new Array2DRowRealMatrix(nonPositiveDefiniteMatrixData);
        try {
            new RectangularCholeskyDecomposition(nonPositiveDefiniteMatrix, 0.0);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected
        }
    }

}