import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Test;
import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testRectangularCholeskyDecompositionPositive() throws NonPositiveDefiniteMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 5.0}, {3.0, 5.0, 6.0}});
        RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix);
        RealMatrix expectedRoot = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.7071067811865476, 0.7071067811865476, 0.0}, {0.0, 0.7071067811865476, 0.8164965809277261}});
        RealMatrix actualRoot = rcd.getRootMatrix();
        assertEquals(expectedRoot, actualRoot);
        assertEquals(3, rcd.getRank());
    }
}