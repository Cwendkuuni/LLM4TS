import org.junit.Test;
import org.junit.Assert.*;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.util.Precision;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testConstructFromPositiveDefiniteMatrix() {
        RealMatrix matrix = MatrixUtils.createRealSymmetricMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 5.0, 6.0}, {3.0, 6.0, 9.0}});
        RectangularCholeskyDecomposition cd = new RectangularCholeskyDecomposition(matrix);
        RealMatrix expectedRoot = MatrixUtils.createRealSymmetricMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.0, 2.30255511131, 0.0}, {0.0, 0.0, 3.0}});
        RealMatrix actualRoot = cd.getRootMatrix();
        Assert.assertEquals(expectedRoot, actualRoot);
        Assert.assertEquals(3, cd.getRank());
    }
}