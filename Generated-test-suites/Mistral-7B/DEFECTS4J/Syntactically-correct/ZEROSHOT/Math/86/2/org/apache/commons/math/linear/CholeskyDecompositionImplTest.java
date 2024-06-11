import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.exception.NotPositiveDefiniteMatrixException;
import org.apache.commons.math3.exception.NotSymmetricMatrixException;
import org.junit.Test;
import static org.junit.Assert.*;

public class CholeskyDecompositionImplTest {

    @Test
    public void testConstructors() {
        RealMatrix matrix = MatrixUtils.createRealSymmetricMatrix(new double[][]{{1.0, 2.0, 2.0}, {2.0, 1.0, 3.0}, {2.0, 3.0, 1.0}});
        CholeskyDecompositionImpl cholesky1 = new CholeskyDecompositionImpl(matrix);
        RealMatrix matrix2 = MatrixUtils.createRealSymmetricMatrix(new double[][]{{4.0, 1.0, 1.0}, {1.0, 4.0, 1.0}, {1.0, 1.0, 4.0}});
        CholeskyDecompositionImpl cholesky2 = new CholeskyDecompositionImpl(matrix2, 1e-12, 1e-12);
        assertNotNull(cholesky1.getL());
        assertNotNull(cholesky1.getLT());
        assertNotNull(cholesky2.getL());
        assertNotNull(cholesky2.getLT());
    }

    @Test(expected = NotSquareMatrixException.class)
    public void testNonSquareMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        new CholeskyDecompositionImpl(matrix);
    }

    @Test(expected = NotSymmetricMatrixException.class)
    public void testNotSymmetricMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        new CholeskyDecompositionImpl(matrix, 1e-12, 1e-12);
    }

    @Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testNotPositiveDefiniteMatrix() {
        RealMatrix matrix = MatrixUtils.createRealSymmetricMatrix(new double[][]{{1.0, -1.0}, {-1.0, 1.0}});
        new CholeskyDecompositionImpl(matrix, 1e-12, 1e-12);
    }

    @Test
    public void testDeterminant() {
        RealMatrix matrix = MatrixUtils.createRealSymmetricMatrix(new double[][]{{1.0, 2.0, 2.0}, {2.0, 1.0, 3.0}, {2.0, 3.0, 1.0}});
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
        assertEquals(1.0, cholesky.getDeterminant(), 1e-12);
    }

    @Test
    public void testSolver() {
        RealMatrix matrix = MatrixUtils.createRealSymmetricMatrix(new double[][]{{5.0, 2.0, 2.0}, {2.0, 5.0, 2.0}, {2.0, 2.0, 5.0}});
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix, 1e-8, 1e-12);

        double[] b = {1.0, 2.0, 3.0};
        double[] x = cholesky.getSolver().solve(b);
        assertArrayEquals(new double[]{0.4611387435458059, 1.2890625, 1.2890625}, x, 1e-8);

        RealVector b2 = new ArrayRealVector(new double[]{1.0, 2.0, 3.0});
        RealVector x2 = cholesky.getSolver().solve(b2);
        assertArrayEquals(x, x2.toArray(), 1e-8);
    }

    @Test
    public void testGetL() {
        RealMatrix matrix = MatrixUtils.createRealSymmetricMatrix(new double[][]{{5.0, 2.0, 2.0}, {2.0, 5.0, 2.0}, {2.0, 2.0, 5.0}});
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix, 1e-8, 1e-12);
        RealMatrix l = cholesky.getL();
        RealMatrix expected = MatrixUtils.createRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {1.0, 2.164853551701852, 0.0}, {1.0, 1.0, 3.316624790355373}});
        assertMatrixEquals(expected, l);
    }

    @Test
    public void testGetLT() {
        RealMatrix matrix = MatrixUtils.createRealSymmetricMatrix(new double[][]{{5.0, 2.0, 2.0}, {2.0, 5.0, 2.0}, {2.0, 2.0, 5.0}});
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix, 1e-8, 1e-12);
        RealMatrix lt = cholesky.getLT();
        RealMatrix expected = MatrixUtils.createRealMatrix(new double[][]{{1.0, 1.0, 1.0}, {2.164853551701852, 2.164853551701852, 2.414213741317694}, {1.0, 1.0, 3.316624790355373}});
        assertMatrixEquals(expected, lt);
    }

}