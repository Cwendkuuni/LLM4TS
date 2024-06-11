import org.junit.Test;
import org.junit.Assert.*;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;

public class RectangularCholeskyDecompositionTest {

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructorWithNullMatrix() {
        new RectangularCholeskyDecomposition(null);
    }

    @Test
    public void testConstructorWithEmptyMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(0, 0);
        try {
            new RectangularCholeskyDecomposition(matrix);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected
        }
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructorWithNonPositiveDefiniteMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{-1.0, 1.0}, {1.0, -1.0}});
        new RectangularCholeskyDecomposition(matrix);
    }

    @Test
    public void testConstructorWithPositiveDefiniteMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 1.0}, {1.0, 1.0}});
        RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix);
        RealMatrix expected = MatrixUtils.createRealMatrix(new double[][]{{1.0, 1.0 / Math.sqrt(2.0)}, {0.0, 1.0 / Math.sqrt(2.0)}});
        Assert.assertEquals(expected, rcd.getRootMatrix());
        Assert.assertEquals(2, rcd.getRank());
    }
}