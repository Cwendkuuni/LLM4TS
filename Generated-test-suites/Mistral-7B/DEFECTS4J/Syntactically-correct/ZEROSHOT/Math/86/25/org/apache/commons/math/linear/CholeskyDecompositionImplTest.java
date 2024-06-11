import org.apache.commons.math3.linear.CholeskyDecomposition;
import org.apache.commons.math3.linear.CholeskyDecompositionImpl;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealVectorImpl;
import org.apache.commons.math3.linear.util.MatrixUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class CholeskyDecompositionImplTest {

    @Test
    public void testCholeskyDecomposition() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {2.0, 1.0, 1.0},
            {1.0, 2.0, 1.0},
            {1.0, 1.0, 2.0}
        });

        CholeskyDecomposition decomposition = new CholeskyDecompositionImpl(matrix);
        RealMatrix l = decomposition.getL();
        RealMatrix lt = decomposition.getLT();

        assertEquals(matrix, l.multiply(lt));
        assertEquals(MatrixUtils.createRealIdentityMatrix(matrix.getRowDimension()), l.getTranspose().multiply(l));
        assertEquals(1.0, decomposition.getDeterminant(), 1e-12);
    }

    @Test(expected = NotSquareMatrixException.class)
    public void testNotSquareMatrixException() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {2.0, 1.0}
        });
        new CholeskyDecompositionImpl(matrix);
    }

    @Test(expected = NotSymmetricMatrixException.class)
    public void testNotSymmetricMatrixException() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {2.0, 1.0},
            {1.0, 3.0}
        });
        new CholeskyDecompositionImpl(matrix);
    }

    @Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testNotPositiveDefiniteMatrixException() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {-1.0, 1.0},
            {1.0, -1.0}
        });
        new CholeskyDecompositionImpl(matrix);
    }

    @Test
    public void testGetSolver() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {2.0, 1.0, 1.0},
            {1.0, 2.0, 1.0},
            {1.0, 1.0, 2.0}
        });
        CholeskyDecomposition decomposition = new CholeskyDecompositionImpl(matrix);
        DecompositionSolver solver = decomposition.getSolver();

        RealVector vector = new ArrayRealVector(new double[]{1.0, 2.0, 3.0});
        RealVector solution = solver.solve(vector);
        assertArrayEquals(new double[]{0.16666667, 0.33333333, 0.33333333}, solution.toArray(), 0.001);
    }

    @Test
    public void testSolve() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {2.0, 1.0, 1.0},
            {1.0, 2.0, 1.0},
            {1.0, 1.0, 2.0}
        });
        CholeskyDecomposition decomposition = new CholeskyDecompositionImpl(matrix);

        RealVector vector = new ArrayRealVector(new double[]{1.0, 2.0, 3.0});
        RealVector solution = decomposition.getSolver().solve(vector);
        assertArrayEquals(new double[]{0.16666667, 0.33333333, 0.33333333}, solution.toArray(), 0.001);
    }

    @Test
    public void testSolveMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {2.0, 1.0, 1.0},
            {1.0, 2.0, 1.0},
            {1.0, 1.0, 2.0}
        });
        CholeskyDecomposition decomposition = new CholeskyDecompositionImpl(matrix);

        RealMatrix matrixB = new Array2DRowRealMatrix(new double[][]{
            {1.0, 0.0},
            {0.0, 1.0},
            {1.0, 1.0}
        });
        RealMatrix solution = decomposition.getSolver().solve(matrixB);

        RealMatrix expectedSolution = new Array2DRowRealMatrix(new double[][]{
            {0.23214286, -0.46428573},
            {-0.46428573, 0.23214286},
            {0.60653066, 0.60653066}
        });
        assertMatrixEquals(expectedSolution, solution);
    }

}