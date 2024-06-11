import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.junit.Test;
import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testDecompositionWithPositiveDefiniteMatrix() {
        RealMatrix matrix = MatrixUtils.createRealSymmetricMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        try {
            RectangularCholeskyDecomposition decomposer = new RectangularCholeskyDecomposition(matrix, 0.0);
            RealMatrix root = decomposer.getRootMatrix();
            assertArrayEquals(new double[][]{
                    {FastMath.SQRT(1.0), 0.0},
                    {FastMath.SQRT(2.0), FastMath.SQRT(2.0)}}, root.getData(), 0.0);
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testDecompositionWithNegativeDefiniteMatrix() {
        RealMatrix matrix = MatrixUtils.createRealSymmetricMatrix(new double[][]{{1.0, -2.0}, {-2.0, 1.0}});
        new RectangularCholeskyDecomposition(matrix, 0.0);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testDecompositionWithSingularMatrix() {
        RealMatrix matrix = MatrixUtils.createRealSymmetricMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});
        new RectangularCholeskyDecomposition(matrix, 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDecompositionWithNullMatrix() {
        new RectangularCholeskyDecomposition(null, 0.0);
    }

    @Test
    public void testGetRank() {
        RealMatrix matrix = MatrixUtils.createRealSymmetricMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        RectangularCholeskyDecomposition decomposer = null;
        try {
            decomposer = new RectangularCholeskyDecomposition(matrix, 0.0);
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
        assertEquals(2, decomposer.getRank());
    }

}