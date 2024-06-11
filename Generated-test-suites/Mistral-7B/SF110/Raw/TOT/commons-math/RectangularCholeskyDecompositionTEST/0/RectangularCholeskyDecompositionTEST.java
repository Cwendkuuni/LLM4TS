import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import org.junit.Assert;

public class RectangularCholeskyDecompositionTest {
    @Test
    public void testConstructorWithPositiveDefiniteMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 5.0}, {3.0, 5.0, 6.0}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        RealMatrix rootMatrix = decomposition.getRootMatrix();
        int rank = decomposition.getRank();
        Assert.assertEquals(3, rank);
        // Check the lower triangular matrix
        Assert.assertArrayEquals(new double[][]{{1.0, 0.0, 0.0}, {0.44948974278317, 1.0, 0.0}, {0.18254593559343, 0.46238239933825, 1.0}}, rootMatrix.getData(), 0.0001);
    }
}