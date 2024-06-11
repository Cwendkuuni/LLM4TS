
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Assert;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testInvalidMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{-1.0, 1.0}, {1.0, -1.0}});
        assertThrows(NonPositiveDefiniteMatrixException.class, () -> new RectangularCholeskyDecomposition(matrix));
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testInvalidMatrixWithSmall() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{-1.0, 1.0}, {1.0, -1.0}});
        assertThrows(NonPositiveDefiniteMatrixException.class, () -> new RectangularCholeskyDecomposition(matrix, 0.01));
    }

    @Test
    public void testGetRootMatrixValid() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 5.0}, {3.0, 5.0, 6.0}});
        RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix);
        RealMatrix expected = MatrixUtils.createRealMatrix(new double[][]{{1.0, 1.0, 0.0}, {1.0, 2.1644336122547195, 0.7071067811865476}, {0.0, 0.7071067811865476, 1.7320508075688772}});
        Assert.assertEquals(expected, rcd.getRootMatrix());
    }

    @Test
    public void testGetRankValid() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{3.0, 2.0, 1.0}, {2.0, 3.0, 1.0}, {1.0, 1.0, 3.0}});
        RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix);
        Assert.assertEquals(3, rcd.getRank());
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNegativeMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{-1.0, 1.0}, {1.0, -1.0}});
        assertThrows(NonPositiveDefiniteMatrixException.class, () -> new RectangularCholeskyDecomposition(matrix));
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testSmallMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 1.0}});
        assertThrows(NonPositiveDefiniteMatrixException.class, () -> new RectangularCholeskyDecomposition(matrix, 0.01));
    }

    @Test
    public void testExceptionHandlingWithNonPositiveDefiniteMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{-1.0, 1.0}, {1.0, -1.0}});
        try {
            new RectangularCholeskyDecomposition(matrix);
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
    }
}