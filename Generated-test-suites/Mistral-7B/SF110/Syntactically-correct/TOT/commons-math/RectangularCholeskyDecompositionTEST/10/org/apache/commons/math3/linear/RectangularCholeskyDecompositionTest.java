import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.linear.*;

public class RectangularCholeskyDecompositionTest {
    private RealMatrix matrix;
    private RealMatrix expectedRoot;
    private RealMatrix actualRoot;

    @Before
    public void setUp() {
        double[][] data = {
            {1.0, 2.0, 3.0},
            {2.0, 4.0, 3.0},
            {3.0, 3.0, 5.0}
        };
        matrix = MatrixUtils.createRealMatrix(3, 3, data);
        expectedRoot = MatrixUtils.createRealMatrix(3, 3, new double[][]{{1.0, 0.0, 0.0}, {0.4495, 0.8165, 0.0}, {-0.4472, -1.2212, 0.9511}});
    }

    @Test
    public void testRectangularCholeskyDecomposition() {
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
            actualRoot = decomposition.getRootMatrix();
            assertEquals(expectedRoot, actualRoot);
            assertEquals(3, decomposition.getRank());
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
}