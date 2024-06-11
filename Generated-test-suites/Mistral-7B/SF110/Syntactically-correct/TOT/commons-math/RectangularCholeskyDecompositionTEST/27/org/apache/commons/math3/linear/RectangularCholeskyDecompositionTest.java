import org.apache.commons.math3.linear.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {
    @Test
    public void testConstructorWithPositiveDefiniteMatrix() throws NonPositiveDefiniteMatrixException {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{2.0, 1.0}, {1.0, 2.0}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        RealMatrix expectedResult = MatrixUtils.createRealMatrix(new double[][]{{1.4142135625, 0.7071067812}, {0.7071067812, 1.4142135625}});
        RealMatrix actualResult = decomposition.getRootMatrix();
        Assert.assertEquals(expectedResult, actualResult);
    }
}