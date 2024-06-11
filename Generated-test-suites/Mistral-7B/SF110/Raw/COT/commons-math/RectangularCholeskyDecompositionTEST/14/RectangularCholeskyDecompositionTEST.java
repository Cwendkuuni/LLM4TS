import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testConstructorWithPositiveDefiniteMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 5.0}, {3.0, 5.0, 6.0}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        RealMatrix expectedRoot = MatrixUtils.createRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.44948974278317, 0.81649658092772, 0.0}, {0.0, 1.2732053501516, 0.81649658092772}});
        RealMatrix actualRoot = decomposition.getRootMatrix();
        Assert.assertArrayEquals(expectedRoot.getData(), actualRoot.getData(), 0.0);
        Assert.assertEquals(expectedRoot.getRowDimension(), actualRoot.getRowDimension());
        Assert.assertEquals(expectedRoot.getColumnDimension(), actualRoot.getColumnDimension());
        Assert.assertEquals(3, decomposition.getRank());
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructorWithNonPositiveDefiniteMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{-1.0, 2.0, 3.0}, {2.0, -4.0, 5.0}, {3.0, 5.0, -6.0}});
        new RectangularCholeskyDecomposition(matrix);
    }

    @Test
    public void testConstructorWithSmallEigenvalues() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 1.0, 4.0}, {3.0, 4.0, 1.0}});
        RectangularCholeskyDecomposition decomposition = null;
        try {
            decomposition = new RectangularCholeskyDecomposition(matrix, 1e-8);
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected
        }
        Assert.assertNull(decomposition);
    }

    @Test
    public void testGetRank() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 5.0}, {3.0, 5.0, 6.0}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        Assert.assertEquals(3, decomposition.getRank());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRankAfterExceptionInConstructor() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{-1.0, 2.0, 3.0}, {2.0, -4.0, 5.0}, {3.0, 5.0, -6.0}});
        new RectangularCholeskyDecomposition(matrix); // Throws NonPositiveDefiniteMatrixException
        RectangularCholeskyDecomposition decomposition = null;
        try {
            decomposition = new RectangularCholeskyDecomposition(matrix);
        } catch (Exception e) {
            // Ignore
        }
        Assert.assertNull(decomposition.getRootMatrix());
        Assert.assertThrows(IllegalStateException.class, () -> decomposition.getRank());
    }

    @Test
    public void testGetRootMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 5.0}, {3.0, 5.0, 6.0}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        RealMatrix expectedRoot = MatrixUtils.createRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.44948974278317, 0.81649658092772, 0.0}, {0.0, 1.2732053501516, 0.81649658092772}});
        RealMatrix actualRoot = decomposition.getRootMatrix();
        Assert.assertArrayEquals(expectedRoot.getData(), actualRoot.getData(), 0.0);
        Assert.assertEquals(expectedRoot.getRowDimension(), actualRoot.getRowDimension());
        Assert.assertEquals(expectedRoot.getColumnDimension(), actualRoot.getColumnDimension());
    }

    @Test
    public void testGetRootMatrixWithSmallEigenvalues() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 1.0, 4.0}, {3.0, 4.0, 1.0}});
        RectangularCholeskyDecomposition decomposition = null;
        try {
            decomposition = new RectangularCholeskyDecomposition(matrix, 1e-8);
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected
        }
        Assert.assertNull(decomposition);
        Assert.assertNull(decomposition.getRootMatrix());
    }

    @Test
    public void testGetRootMatrixWithNonSquareMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        RectangularCholeskyDecomposition decomposition = null;
        try {
            decomposition = new RectangularCholeskyDecomposition(matrix);
        } catch (IllegalArgumentException e) {
            // Expected
        }
        Assert.assertNull(decomposition);
        Assert.assertNull(decomposition.getRootMatrix());
    }

    @Test
    public void testGetRootMatrixWithSingularMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 1.0, 3.0}, {3.0, 3.0, 0.0}});
        RectangularCholeskyDecomposition decomposition = null;
        try {
            decomposition = new RectangularCholeskyDecomposition(matrix);
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected
        }
        Assert.assertNull(decomposition);
        Assert.assertNull(decomposition.getRootMatrix());
    }
}