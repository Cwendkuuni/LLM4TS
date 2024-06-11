import org.apache.commons.math3.linear.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {
    @Test
    public void testRectangularCholeskyDecomposition() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 6.0}, {3.0, 6.0, 9.0}});
        RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix);
        RealMatrix expectedResult = MatrixUtils.createRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.0, 2.0, 1.0}, {0.0, 1.0, 3.0}});
        RealMatrix actualResult = rcd.getRootMatrix();
        assertTrue(MatrixUtils.isEqual(expectedResult, actualResult, 1e-9));
        assertEquals(3, rcd.getRank());
    }
}