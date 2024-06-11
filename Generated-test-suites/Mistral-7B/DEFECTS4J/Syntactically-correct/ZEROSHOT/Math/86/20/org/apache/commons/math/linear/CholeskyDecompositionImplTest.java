import org.apache.commons.math3.linear.CholeskyDecomposition;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CholeskyDecompositionImplTest {

    @Test
    public void testDefaultConstructor() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.1, 0.2}, {0.1, 1.0, 0.3}, {0.2, 0.3, 1.0}});
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
        assertNotNull(cholesky.getL());
        assertNotNull(cholesky.getLT());
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testNonSquareMatrixException() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.1}, {0.1, 1.0}, {0.1, 1.0}});
        new CholeskyDecompositionImpl(matrix);
    }

    @Test(expected = NotSymmetricMatrixException.class)
    public void testNotSymmetricMatrixException() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.1, 0.2}, {0.1, 1.0, 0.3}, {0.2, 0.3, 1.0}, {0.3, 0.5, 0.4}});
        new CholeskyDecompositionImpl(matrix);
    }

    @Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testNotPositiveDefiniteMatrixException() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.1, 0.2}, {0.1, -1.0, 0.3}, {0.2, 0.3, -1.0}});
        new CholeskyDecompositionImpl(matrix);
    }

    @Test
    public void testGetSolver() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.1, 0.2}, {0.1, 1.0, 0.3}, {0.2, 0.3, 1.0}});
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
        DecompositionSolver solver = cholesky.getSolver();

        RealVector b = new ArrayRealVector(new double[]{1.0, 2.0, 3.0});
        RealVector x = solver.solve(b);
        assertEquals(new ArrayRealVector(new double[]{-0.242535629411, 0.539470638141, 1.264910236221}), x);
    }

    @Test
    public void testGetDeterminant() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.1, 0.2}, {0.1, 1.0, 0.3}, {0.2, 0.3, 1.0}});
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
        assertEquals(1.0, cholesky.getDeterminant(), 1.0e-15);
    }

}