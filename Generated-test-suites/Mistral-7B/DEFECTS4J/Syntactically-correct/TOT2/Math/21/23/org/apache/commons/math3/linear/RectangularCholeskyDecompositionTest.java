import static org.junit.Assert.*;
import org.apache.commons.math3.linear.*;

public class RectangularCholeskyDecompositionTest {

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteMatrixException() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {2, -1}});
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, 0.0);
        fail("NonPositiveDefiniteMatrixException expected but not thrown.");
    }

    @Test
    public void testConstructWithPositiveDefiniteMatrix() {
        RealMatrix matrix = MatrixUtils.createRealSymmetricPositiveDefiniteMatrix(2);
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, 0.0);
        assertEquals(2, decomp.getRank());
        RealMatrix expectedRoot = MatrixUtils.createRealMatrix(new double[][]{{0.447213595499958, 0.894427190999911}, {-0.894427190999911, 1.788854381999823}});
        assertEquals(expectedRoot, decomp.getRootMatrix());
    }
}