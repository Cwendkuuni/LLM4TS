import org.apache.commons.math3.linear.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testRectangularCholeskyDecomposition() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 1.0, 1.0}, {1.0, 2.0, 2.0}, {1.0, 2.0, 3.0}});
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, 0.0);

        assertEquals(2, decomp.getRank());
        RealMatrix expectedRoot = MatrixUtils.createRealMatrix(new double[][]{{1.0, 1.0, 1.0 / Math.sqrt(2.0)}, {1.0 / Math.sqrt(2.0), 1.0, 1.0 / Math.sqrt(2.0)}, {1.0 / Math.sqrt(3.0), 1.0 / Math.sqrt(2.0), 1.0}});
        MatrixUtils.assertEquals(expectedRoot, decomp.getRootMatrix());
    }
}