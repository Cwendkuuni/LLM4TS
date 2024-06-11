import org.apache.commons.math3.linear.*;
import org.junit.Assert;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testRectangularCholeskyDecomposition() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 1.0, 1.0}, {3.0, 1.0, 1.0}});
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, 0.0);
        RealMatrix expectedResult = MatrixUtils.createRealMatrix(new double[][]{{1.0, 1.0, 1.0}, {1.0, 1.0, 1.0}, {1.0, 1.0, 1.0}});
        Assert.assertEquals(expectedResult, decomp.getRootMatrix());
    }

    @Test
    public void testRectangularCholeskyDecompositionWithSmallDiagonalElements() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 0.5, 2.0}, {0.5, 1.0, 1.0}, {2.0, 1.0, 1.0}});
        try {
            new RectangularCholeskyDecomposition(matrix, 0.5);
            Assert.fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
    }

    @Test
    public void testGetRank() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 1.0, 1.0}, {3.0, 1.0, 1.0}});
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, 0.0);
        Assert.assertEquals(3, decomp.getRank());
    }

    @Test
    public void testGetRankWithSmallDiagonalElements() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 0.5, 2.0}, {0.5, 1.0, 1.0}, {2.0, 1.0, 1.0}});
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, 0.5);
        Assert.assertEquals(2, decomp.getRank());
    }

}