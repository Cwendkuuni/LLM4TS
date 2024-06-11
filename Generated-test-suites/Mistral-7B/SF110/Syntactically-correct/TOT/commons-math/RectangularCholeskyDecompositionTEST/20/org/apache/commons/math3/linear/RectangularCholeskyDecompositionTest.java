import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testRectangularCholeskyDecomposition() {
        RealMatrix inputMatrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 5.0}, {3.0, 5.0, 6.0}});
        RectangularCholeskyDecomposition matrixDecomposition = new RectangularCholeskyDecomposition(inputMatrix);
        RealMatrix expectedResult = MatrixUtils.createRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.6, 1.0, 0.0}, {0.346, 0.732, 1.0}});
        RealMatrix result = matrixDecomposition.getRootMatrix();
        assertArrayEquals(expectedResult.getData(), result.getData(), 0.001);
        assertEquals(3, matrixDecomposition.getRank());
    }
}