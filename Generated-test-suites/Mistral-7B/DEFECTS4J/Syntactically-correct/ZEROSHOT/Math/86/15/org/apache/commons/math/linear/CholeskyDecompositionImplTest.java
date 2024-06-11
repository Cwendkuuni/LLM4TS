import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.exception.NotPositiveDefiniteMatrixException;
import org.apache.commons.math3.exception.NotSymmetricMatrixException;
import org.junit.Test;
import static org.junit.Assert.*;

public class CholeskyDecompositionImplTest {

    @Test
    public void testCholeskyDecomposition() throws NotPositiveDefiniteMatrixException, NotSymmetricMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{3.0, 2.0, 2.0}, {2.0, 1.2, 1.5}, {2.0, 1.5, 2.7}});
        CholeskyDecomposition choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        RealMatrix l = choleskyDecomposition.getL();
        RealMatrix lt = choleskyDecomposition.getLT();

        // Check L * L^T = Original Matrix
        RealMatrix product = l.multiply(lt);
        assertEquals(matrix, product);

        // Check L is lower triangular
        for (int i = 0; i < l.getRowDimension(); i++) {
            for (int j = 0; j < i; j++) {
                assertEquals(0.0, l.getEntry(i, j), 1e-15);
            }
        }

        // Check L^T is upper triangular
        for (int i = 0; i < lt.getColumnDimension(); i++) {
            for (int j = i + 1; j < lt.getRowDimension(); j++) {
                assertEquals(0.0, lt.getEntry(i, j), 1e-15);
            }
        }

        // Test Solver
        RealVector b = new ArrayRealVector(new double[]{1.0, 2.0, 3.0});
        RealVector x = choleskyDecomposition.getSolver().solve(b);
        assertArrayEquals(new double[]{1.5, 0.0, -0.5}, x.toArray(), 1e-15);
    }

    @Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testNotPositiveDefiniteException() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 2.0}, {2.0, -1.0, 1.5}, {2.0, 1.5, -2.7}});
        new CholeskyDecompositionImpl(matrix);
    }

    @Test(expected = NotSymmetricMatrixException.class)
    public void testNotSymmetricMatrixException() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{3.0, 2.0, 2.0}, {2.0, 1.2, 1.5}, {2.0, 1.5, 2.7}, {2.0, 1.5, 2.7, 3.0}});
        new CholeskyDecompositionImpl(matrix);
    }

}