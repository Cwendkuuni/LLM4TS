import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RectangularCholeskyDecompositionTest {

    @org.junit.Test
    public void testValidDecomposition() {
        double small = 1e-10;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.5, 0.5}, {0.5, 1.0, 0.5}, {0.5, 0.5, 1.0}});
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
            RealMatrix root = decomposition.getRootMatrix();
            int rank = decomposition.getRank();
            assertEquals(3, rank);
            assertEquals(3, root.getRowDimension());
            assertEquals(rank, root.getColumnDimension());
            assertArrayEquals(new double[][]{
                    {FastMath.SQRT(1.0), 0.0, 0.0},
                    {0.5, FastMath.SQRT(0.5), 0.0},
                    {0.5, 0.5, FastMath.SQRT(0.5)}
            }, root.getData(), 0.0);
        } catch (NonPositiveDefiniteMatrixException e) {
            throw new RuntimeException("Unexpected NonPositiveDefiniteMatrixException: " + e.getMessage());
        }
    }

    @org.junit.Test
    public void testInvalidDecomposition() {
        double small = 1e-10;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.5, 0.5}, {0.5, 1.0, 0.5}, {0.5, 0.5, -1.0}});
        try {
            new RectangularCholeskyDecomposition(matrix, small);
            assertFalse("NonPositiveDefiniteMatrixException expected");
        } catch (NonPositiveDefiniteMatrixException e) {
            assertEquals(-1.0, e.getDiagonalValue(), 0.0);
            assertEquals(2, e.getRowIndex());
            assertEquals(small, e.getThreshold(), 0.0);
        }
    }

    @org.junit.Test
    public void testIdentityMatrixDecomposition() {
        double small = 1e-12;
        RealMatrix matrix = MatrixUtils.IDENTITY_MATRIX_3;
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
            RealMatrix root = decomposition.getRootMatrix();
            int rank = decomposition.getRank();
            assertEquals(3, rank);
            assertEquals(3, root.getRowDimension());
            assertEquals(rank, root.getColumnDimension());
            assertArrayEquals(new double[][]{
                    {1.0, 0.0, 0.0},
                    {0.0, 1.0, 0.0},
                    {0.0, 0.0, 1.0}
            }, root.getData(), 0.0);
        } catch (NonPositiveDefiniteMatrixException e) {
            throw new RuntimeException("Unexpected NonPositiveDefiniteMatrixException: " + e.getMessage());
        }
    }

    @org.junit.Test
    public void testSingularValueDecompositionComparison() {
        double small = 1e-12;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.5, 0.5}, {0.5, 1.0, 0.5}, {0.5, 0.5, 1.0}});
        SingularValueDecomposition svd = new SingularValueDecomposition(matrix);
        RealMatrix root = svd.getU();
        int rank = svd.getRank();
        try {
            RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix, small);
            RealMatrix rcr = rcd.getRootMatrix();
            assertEquals(rank, rcd.getRank());
            for (int i = 0; i < rank; ++i) {
                for (int j = 0; j < rank; ++j) {
                    assertEquals(root.getEntry(i, j), rcr.getEntry(i, j), 1e-12);
                }
            }
        } catch (NonPositiveDefiniteMatrixException e) {
            throw new RuntimeException("Unexpected NonPositiveDefiniteMatrixException: " + e.getMessage());
        }
    }
}