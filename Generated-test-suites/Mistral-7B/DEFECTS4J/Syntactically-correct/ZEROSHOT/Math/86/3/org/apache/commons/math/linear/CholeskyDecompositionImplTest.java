import org.apache.commons.math3.linear.CholeskyDecomposition;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealVectorImpl;
import org.apache.commons.math3.linear.MatrixUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class CholeskyDecompositionImplTest {

    @Test
    public void testCholeskyDecomposition() {
        double[][] data = new double[][]{{1.0, 2.0, 2.0}, {2.0, 5.0, 3.0}, {2.0, 3.0, 7.0}};
        RealMatrix matrix = new Array2DRowRealMatrix(data);

        CholeskyDecomposition decomposition = new CholeskyDecompositionImpl(matrix);
        RealMatrix l = decomposition.getL();
        RealMatrix lt = decomposition.getLT();

        assertEquals("L is not equal to LT.transpose()", l.transpose().multiply(lt), MatrixUtils.createIdentityMatrix(3));
    }

    @Test(expected = NotSquareMatrixException.class)
    public void testNonSquareMatrixException() {
        double[][] data = new double[][]{{1.0, 2.0}, {2.0, 3.0}};
        RealMatrix matrix = new Array2DRowRealMatrix(data);
        new CholeskyDecompositionImpl(matrix);
    }

    @Test(expected = NotSymmetricMatrixException.class)
    public void testNotSymmetricMatrixException() {
        double[][] data = new double[][]{{1.0, 2.0, 0.1}, {2.0, 5.0, 3.0}, {0.1, 3.0, 7.0}};
        RealMatrix matrix = new Array2DRowRealMatrix(data);
        new CholeskyDecompositionImpl(matrix, 0.0001, 0.0001);
    }

    @Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testNotPositiveDefiniteMatrixException() {
        double[][] data = new double[][]{{1.0, 2.0, 2.0}, {2.0, -1.0, 3.0}, {2.0, 3.0, -2.0}};
        RealMatrix matrix = new Array2DRowRealMatrix(data);
        new CholeskyDecompositionImpl(matrix, 0.0001, 0.0001);
    }

    @Test
    public void testDeterminant() {
        double[][] data = new double[][]{{1.0, 2.0, 2.0}, {2.0, 5.0, 3.0}, {2.0, 3.0, 7.0}};
        RealMatrix matrix = new Array2DRowRealMatrix(data);
        CholeskyDecomposition decomposition = new CholeskyDecompositionImpl(matrix);
        assertEquals("Determinant not correct", decomposition.getDeterminant(), 61.0, 0.0);
    }

    @Test
    public void testSolver() {
        double[][] data = new double[][]{{1.0, 2.0, 2.0}, {2.0, 5.0, 3.0}, {2.0, 3.0, 7.0}};
        RealMatrix matrix = new Array2DRowRealMatrix(data);
        CholeskyDecomposition decomposition = new CholeskyDecompositionImpl(matrix);

        double[] b = new double[]{1.0, 2.0, 2.0};
        double[] x = decomposition.getSolver().solve(b);

        RealVector reference = new ArrayRealVector(new double[]{1.0 / 3.0, 1.0, 1.0 / 2.0});
        assertEquals("Solution not correct", reference, new ArrayRealVector(x));
    }
}