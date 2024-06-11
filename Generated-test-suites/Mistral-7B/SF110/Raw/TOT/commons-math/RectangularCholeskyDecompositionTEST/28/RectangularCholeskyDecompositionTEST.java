import org.apache.commons.math3.linear.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testRectangularCholeskyDecomposition() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2, 3}, {2, 4, 6}, {3, 6, 10}});
        RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix);
        RealMatrix root = rcd.getRootMatrix();
        int rank = rcd.getRank();

        // Check if the root matrix is correct
        RealMatrix expectedRoot = new Array2DRowRealMatrix(new double[][]{{1/Math.sqrt(1), 0, 0}, {-0.5, 1/Math.sqrt(2), 0}, {-1.5, -0.5*Math.sqrt(2)/2, 1/Math.sqrt(3)}});
        assertEquals("Root matrix is not correct", expectedRoot, root);

        // Check if the rank is correct
        int expectedRank = 3;
        assertEquals("Rank is not correct", expectedRank, rank);
    }
}