import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import org.junit.Assert;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testConstructorWithPositiveDefiniteMatrix() {
        RealMatrix matrix = MatrixUtils.createRealSymmetricMatrix(new double[][]{{1.0, 2.0, 2.0}, {2.0, 3.0, 3.0}, {2.0, 3.0, 4.0}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        RealMatrix rootMatrix = decomposition.getRootMatrix();
        int rank = decomposition.getRank();
        Assert.assertEquals(3, rank);
        Assert.assertArrayEquals(new double[][]{
                {0.44948974278317, 0.0, 0.0},
                {0.0, 1.7320508075689, 0.0},
                {0.0, 0.0, 2.0}
        }, rootMatrix.getData(), 0.001);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructorWithNegativeDefiniteMatrix() {
        RealMatrix matrix = MatrixUtils.createRealSymmetricMatrix(new double[][]{{-1.0, 2.0}, {2.0, -3.0}});
        new RectangularCholeskyDecomposition(matrix);
    }

    @Test
    public void testConstructorWithSmallMatrix() {
        RealMatrix matrix = MatrixUtils.createRealSymmetricMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.001);
        RealMatrix rootMatrix = decomposition.getRootMatrix();
        int rank = decomposition.getRank();
        Assert.assertEquals(2, rank);
        Assert.assertArrayEquals(new double[][]{
                {1.4142135625, 0.0},
                {0.0, 1.4142135625}
        }, rootMatrix.getData(), 0.001);
    }

    @Test
    public void testConstructorWithSingularMatrix() {
        RealMatrix matrix = MatrixUtils.createRealSymmetricMatrix(new double[][]{{0.0, 0.0}, {0.0, 0.0}});
        RectangularCholeskyDecomposition decomposition = null;
        try {
            decomposition = new RectangularCholeskyDecomposition(matrix);
        } catch (NonPositiveDefiniteMatrixException e) {
            Assert.assertEquals(0, decomposition.getRank());
        }
    }
}