import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testEmptyMatrix() {
        try {
            new RectangularCholeskyDecomposition(new Array2DRowRealMatrix(new double[0][0]), 0.0);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
    }

    @Test
    public void testIdentityMatrix() {
        RealMatrix matrix = RealMatrixUtils.createIdentityMatrix(5);
        try {
            RectangularCholeskyDecomposition decompositon = new RectangularCholeskyDecomposition(matrix, 0.0);
            RealMatrix root = decompositon.getRootMatrix();
            assertEquals(matrix, root.transpose().multiply(root));
            assertEquals(5, decompositon.getRank());
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Unexpected NonPositiveDefiniteMatrixException");
        }
    }

    @Test
    public void testSingularMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.0, 0.0, 0.0}, {0.0, 0.0, -1.0}});
        try {
            new RectangularCholeskyDecomposition(matrix, 0.0);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
    }

    @Test
    public void testPositiveDefiniteMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{2.0, 1.0}, {1.0, 2.0}});
        try {
            RectangularCholeskyDecomposition decompositon = new RectangularCholeskyDecomposition(matrix, 0.0);
            RealMatrix root = decompositon.getRootMatrix();
            assertEquals(matrix, root.transpose().multiply(root));
            assertEquals(2, decompositon.getRank());
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Unexpected NonPositiveDefiniteMatrixException");
        }
    }

    @Test
    public void testSmallDiagonalElements() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 1e-6}, {0.0, 1.0, 1e-6}, {1e-6, 1e-6, 1.0}});
        try {
            RectangularCholeskyDecomposition decompositon = new RectangularCholeskyDecomposition(matrix, 1e-6);
            RealMatrix root = decompositon.getRootMatrix();
            assertEquals(matrix, root.transpose().multiply(root), 1e-6);
            assertEquals(3, decompositon.getRank());
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Unexpected NonPositiveDefiniteMatrixException");
        }
    }

    @Test
    public void testNegativeDiagonalElements() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, -1.0}, {0.0, 1.0, 0.0}, {-1.0, 0.0, 1.0}});
        try {
            new RectangularCholeskyDecomposition(matrix, 0.0);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeThreshold() {
        new RectangularCholeskyDecomposition(new Array2DRowRealMatrix(new double[0][0]), -1.0);
        fail("Unexpected exception not thrown");
    }

}