import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testExceptionWhenMatrixIsNotPositiveDefinite() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, -1.0}});
        new RectangularCholeskyDecomposition(matrix);
    }

    @Test
    public void testGetRootMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 2.0, 4.0}, {3.0, 4.0, 5.0}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-13);
        RealMatrix root = decomposition.getRootMatrix();
        assertNotNull(root);
        assertEquals(3, root.getRowDimension());
        assertEquals(3, root.getColumnDimension());
    }

    @Test
    public void testGetRank() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 2.0, 4.0}, {3.0, 4.0, 5.0}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-13);
        int rank = decomposition.getRank();
        assertEquals(3, rank);
    }

    @Test
    public void testCorrectDecomposition() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{4.0, 2.0, 2.0}, {2.0, 3.0, 3.0}, {2.0, 3.0, 5.0}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-13);
        RealMatrix root = decomposition.getRootMatrix();
        assertNotNull(root);
        assertEquals(3, root.getRowDimension());
        assertEquals(3, root.getColumnDimension());
        assertEquals(1.0 / Math.sqrt(4.0), root.getEntry(0, 0), 1e-6);
        assertEquals(-1.0 / Math.sqrt(3.0), root.getEntry(1, 1), 1e-6);
        assertEquals(-1.0 / Math.sqrt(5.0), root.getEntry(2, 2), 1e-6);
        assertEquals(1.0 / 2.0, root.getEntry(0, 1), 1e-6);
        assertEquals(1.0 / 2.0, root.getEntry(0, 2), 1e-6);
        assertEquals(-1.0 / 2.0, root.getEntry(1, 0), 1e-6);
        assertEquals(1.0, root.getEntry(1, 1));
        assertEquals(1.0, root.getEntry(2, 2));
    }

    @Test
    public void testSmallMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 1.0}, {1.0, 1.0}});
        RectangularCholeskyDecomposition decomposition = null;
        try {
            decomposition = new RectangularCholeskyDecomposition(matrix, 1e-6);
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected
        }
        assertNull(decomposition);
    }

    @Test
    public void testIdentityMatrix() {
        RealMatrix matrix = MatrixUtils.createIdentityMatrix(4);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-13);
        RealMatrix root = decomposition.getRootMatrix();
        assertNotNull(root);
        assertEquals(4, root.getRowDimension());
        assertEquals(4, root.getColumnDimension());
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                assertEquals(i == j ? 1.0 : 0.0, root.getEntry(i, j), 1e-13);
            }
        }
    }

    @Test
    public void testSingularMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 0.0}});
        RectangularCholeskyDecomposition decomposition = null;
        try {
            deployment = new RectangularCholeskyDecomposition(matrix, 1e-13);
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected
        }
        assertNull(decomposition);
    }

    @Test
    public void testDiagonalMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 2.0}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-13);
        RealMatrix root = decomposition.getRootMatrix();
        assertNotNull(root);
        assertEquals(2, root.getRowDimension());
        assertEquals(2, root.getColumnDimension());
        assertEquals(1.0 / Math.sqrt(1.0), root.getEntry(0, 0), 1e-6);
        assertEquals(1.0 / Math.sqrt(2.0), root.getEntry(1, 1), 1e-6);
    }
}