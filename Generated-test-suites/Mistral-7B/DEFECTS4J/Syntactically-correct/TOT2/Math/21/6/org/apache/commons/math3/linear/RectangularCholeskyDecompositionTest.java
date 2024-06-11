import org.apache.commons.math3.linear.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testRectangularCholeskyDecomposition() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 5.0}, {3.0, 5.0, 6.0}});
        double small = 0.0;
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
        RealMatrix expectedRoot = MatrixUtils.createRealMatrix(new double[][]{{1.0, 1.0, 1.0}, {0.4472, 1.4142, 1.8758}, {-0.7364, -0.3314, 1.7321}});
        assertEquals(expectedRoot, decomposition.getRootMatrix());
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteMatrixException() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, -1.0, 5.0}, {3.0, 5.0, 6.0}});
        double small = 0.0;
        new RectangularCholeskyDecomposition(matrix, small);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteMatrixExceptionSmallDiagonal() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 1.0, 5.0}, {3.0, 5.0, 0.0}});
        double small = 0.0;
        new RectangularCholeskyDecomposition(matrix, small);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteMatrixExceptionNegativeDiagonal() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, -5.0}, {3.0, 5.0, 6.0}});
        double small = 0.0;
        new RectangularCholeskyDecomposition(matrix, small);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteMatrixExceptionZeroRank() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});
        double small = 0.0;
        new RectangularCholeskyDecomposition(matrix, small);
    }

    @Test
    public void testGetRank() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 5.0}, {3.0, 5.0, 6.0}});
        double small = 0.0;
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
        assertEquals(3, decomposition.getRank());
    }

}