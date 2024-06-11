import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class RectangularCholeskyDecompositionTest {

    @org.junit.Test
    public void testValidDecomposition() {
        double small = 1e-6;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{3.0, 1.0, 1.0}, {1.0, 3.0, 1.0}, {1.0, 1.0, 2.0}});
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
            RealMatrix root = decomposition.getRootMatrix();
            int rank = decomposition.getRank();
            assertNotNull(root);
            assertEquals(3, rank);
            assertEquals(3, root.getRowDimension());
            assertEquals(rank, root.getColumnDimension());
            for (int i = 0; i < rank; i++) {
                for (int j = 0; j < rank; j++) {
                    assertEquals(0.0, root.getEntry(i, j) - Math.sqrt(matrix.getEntry(i, i)), 1e-6);
                    assertEquals(0.0, root.getEntry(j, i) - Math.sqrt(matrix.getEntry(j, j)), 1e-6);
                }
            }
        } catch (NonPositiveDefiniteMatrixException e) {
            throw new RuntimeException(e);
        }
    }

    @org.junit.Test
    public void testInvalidDecomposition() {
        double small = 1e-6;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, -1.0}});
        try {
            new RectangularCholeskyDecomposition(matrix, small);
            assertFalse(true);
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
    }

    @org.junit.Test
    public void testRankOne() {
        double small = 1e-6;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 0.1}});
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
            RealMatrix root = decomposition.getRootMatrix();
            int rank = decomposition.getRank();
            assertNotNull(root);
            assertEquals(2, rank);
            assertEquals(3, root.getRowDimension());
            assertEquals(rank, root.getColumnDimension());
            for (int i = 0; i < rank; i++) {
                for (int j = 0; j < rank; j++) {
                    assertEquals(0.0, root.getEntry(i, j) - Math.sqrt(matrix.getEntry(i, i)), 1e-6);
                    assertEquals(0.0, root.getEntry(j, i) - Math.sqrt(matrix.getEntry(j, j)), 1e-6);
                }
            }
            assertNull(root.getEntry(2, 0));
            assertNull(root.getEntry(0, 2));
        } catch (NonPositiveDefiniteMatrixException e) {
            throw new RuntimeException(e);
        }
    }

    @org.junit.Test
    public void testRankZero() {
        double small = 1e-6;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{-1.0, 0.0, 0.0}, {0.0, -1.0, 0.0}, {0.0, 0.0, -1.0}});
        try {
            new RectangularCholeskyDecomposition(matrix, small);
            assertFalse(true);
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
    }

}