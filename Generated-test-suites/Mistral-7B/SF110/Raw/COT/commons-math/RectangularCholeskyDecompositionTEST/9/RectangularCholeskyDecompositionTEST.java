import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testConstructorWithPositiveDefiniteMatrix() throws NonPositiveDefiniteMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{2.0, 1.0}, {1.0, 2.0}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        RealMatrix expectedResult = new Array2DRowRealMatrix(new double[][]{{1.4142135625, 0.7071067812}, {0.7071067812, 1.4142135625}});
        Assert.assertEquals(expectedResult, decomposition.getRootMatrix());
        Assert.assertEquals(2, decomposition.getRank());
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructorWithNonPositiveDefiniteMatrix() throws NonPositiveDefiniteMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, -1.0}, {-1.0, 1.0}});
        new RectangularCholeskyDecomposition(matrix);
    }

    @Test
    public void testGetRank() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{2.0, 1.0}, {1.0, 2.0}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        Assert.assertEquals(2, decomposition.getRank());
    }

    @Test
    public void testGetRootMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{2.0, 1.0}, {1.0, 2.0}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        RealMatrix expectedResult = new Array2DRowRealMatrix(new double[][]{{1.4142135625, 0.7071067812}, {0.7071067812, 1.4142135625}});
        Assert.assertEquals(expectedResult, decomposition.getRootMatrix());
    }

    @Test
    public void testConstructorWithSmallValue() throws NonPositiveDefiniteMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{2.0, 1.0}, {1.0, 2.0}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-10);
        RealMatrix expectedResult = new Array2DRowRealMatrix(new double[][]{{1.4142135625, 0.7071067812}, {0.7071067812, 1.4142135625}});
        Assert.assertEquals(expectedResult, decomposition.getRootMatrix());
        Assert.assertEquals(2, decomposition.getRank());
    }

    @Test
    public void testConstructorWithSingularMatrix() throws NonPositiveDefiniteMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{2.0, 0.0}, {0.0, 2.0}});
        try {
            new RectangularCholeskyDecomposition(matrix);
            Assert.fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            Assert.assertEquals(0.0, Precision.abs(e.getDeterminant()), 1e-10);
            Assert.assertEquals(0, e.getRow());
        }
    }

    @Test
    public void testConstructorWithNegativeMatrix() throws NonPositiveDefiniteMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{-1.0, 1.0}, {1.0, -1.0}});
        try {
            new RectangularCholeskyDecomposition(matrix);
            Assert.fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            Assert.assertEquals(-1.0, e.getDeterminant(), 1e-10);
            Assert.assertEquals(0, e.getRow());
        }
    }
}