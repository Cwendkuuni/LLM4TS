import org.apache.commons.math3.linear.CholeskyDecomposition;
import org.apache.commons.math3.linear.CholeskyDecompositionImpl;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealVectorImpl;
import org.apache.commons.math3.linear.MatrixUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

public class CholeskyDecompositionImplTest {

    @Test
    public void testDefaultConstructor() throws Exception {
        RealMatrix matrix = new org.apache.commons.math3.linear.Array2DRowRealMatrix(new double[][]{{1.0, 1.0},{1.0, 1.0}});
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);

        RealMatrix L = choleskyDecomposition.getL();
        RealMatrix LT = choleskyDecomposition.getLT();
        assertEquals(1.0, L.getEntry(0, 0), 1e-15);
        assertEquals(1.0, L.getEntry(1, 1), 1e-15);
        assertEquals(1.0 / Math.sqrt(1.0), L.getEntry(0, 1), 1e-15);
        assertEquals(1.0 / Math.sqrt(1.0), L.getEntry(1, 0), 1e-15);
        assertArrayEquals(new double[]{1.0 / Math.sqrt(1.0), 0.0}, LT.getColumnVector(0).toArray(), 1e-15);
        assertArrayEquals(new double[]{0.0, 1.0 / Math.sqrt(1.0)}, LT.getColumnVector(1).toArray(), 1e-15);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testNonSquareMatrixException() throws Exception {
        RealMatrix matrix = new org.apache.commons.math3.linear.Array2DRowRealMatrix(new double[][]{{1.0, 1.0},{1.0, 1.0, 1.0}});
        new CholeskyDecompositionImpl(matrix);
    }

    @Test(expected = NotSymmetricMatrixException.class)
    public void testNotSymmetricMatrixException() throws Exception {
        RealMatrix matrix = new org.apache.commons.math3.linear.Array2DRowRealMatrix(new double[][]{{1.0, 1.0},{1.0, 2.0}});
        new CholeskyDecompositionImpl(matrix, 1e-12, 1e-12);
    }

    @Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testNotPositiveDefiniteMatrixException() throws Exception {
        RealMatrix matrix = new org.apache.commons.math3.linear.Array2DRowRealMatrix(new double[][]{{1.0, 1.0},{1.0, -1.0}});
        new CholeskyDecompositionImpl(matrix, 1e-12, 1e-12);
    }

    @Test
    public void testCustomConstructor() throws Exception {
        RealMatrix matrix = new org.apache.commons.math3.linear.Array2DRowRealMatrix(new double[][]{{3.0, 2.0},{2.0, 2.0}});
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix, 1e-12, 1e-12);

        RealMatrix L = choleskyDecomposition.getL();
        RealMatrix LT = choleskyDecomposition.getLT();
        assertEquals(1.0 / Math.sqrt(3.0), L.getEntry(0, 0), 1e-15);
        assertEquals(1.0 / Math.sqrt(2.0), L.getEntry(0, 1), 1e-15);
        assertEquals(1.0 / Math.sqrt(2.0), L.getEntry(1, 0), 1e-15);
        assertEquals(1.0 / (2.0 * Math.sqrt(2.0)), L.getEntry(1, 1), 1e-15);
        assertArrayEquals(new double[]{1.0 / Math.sqrt(3.0), 1.0 / (2.0 * Math.sqrt(2.0))}, LT.getColumnVector(0).toArray(), 1e-15);
        assertArrayEquals(new double[]{-1.0 / (2.0 * Math.sqrt(2.0)), 1.0 / Math.sqrt(2.0)}, LT.getColumnVector(1).toArray(), 1e-15);
    }

    @Test
    public void testGetDeterminant() throws Exception {
        RealMatrix matrix = new org.apache.commons.math3.linear.Array2DRowRealMatrix(new double[][]{{2.0, 1.0},{1.0, 2.0}});
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix, 1e-12, 1e-12);
        assertEquals(3.0, choleskyDecomposition.getDeterminant(), 1e-15);
    }

    @Test
    public void testGetSolver() throws Exception {
        RealMatrix matrix = new org.apache.commons.math3.linear.Array2DRowRealMatrix(new double[][]{{3.0, 2.0},{2.0, 2.0}});
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix, 1e-12, 1e-12);

        RealVector b = new RealVectorImpl(new double[]{1.0, 1.0});
        RealVector x = choleskyDecomposition.getSolver().solve(b);
        assertArrayEquals(new double[]{1.0 / Math.sqrt(3.0), 1.0 / (2.0 * Math.sqrt(2.0))}, x.toArray(), 1e-15);

        RealMatrix B = MatrixUtils.createRealMatrix(new double[][]{{1.0, 1.0},{1.0, 1.0}});
        RealMatrix X = choleskyDecomposition.getSolver().solve(B);
        assertArrayEquals(new double[][]{{1.0 / Math.sqrt(3.0), 1.0 / (2.0 * Math.sqrt(2.0))}, {-1.0 / (2.0 * Math.sqrt(2.0)), 1.0 / Math.sqrt(2.0)}}, X.getData(), 1e-15);
    }

}