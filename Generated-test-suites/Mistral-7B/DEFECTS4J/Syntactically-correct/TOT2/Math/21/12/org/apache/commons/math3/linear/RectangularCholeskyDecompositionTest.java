import org.junit.Test; import org.apache.commons.math3.linear.RealMatrix; import static org.junit.Assert.assertArrayEquals; import static org.junit.Assert.assertEquals;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testDecomposition() {
        double small = 1e-6;
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{2, 1}, {1, 2}});
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
            RealMatrix expected = MatrixUtils.createRealMatrix(new double[][]{{1.4142135625, 0.7071067812}, {0.7071067812, 1.4142135625}});
            assertEquals(expected, decomposition.getRootMatrix());
            assertEquals(2, decomposition.getRank());
        } catch (NonPositiveDefiniteMatrixException e) {
            // This exception should not be thrown for this input
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteMatrixException() {
        double small = 1e-6;
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 0}, {0, -1}});
        new RectangularCholeskyDecomposition(matrix, small);
    }

}