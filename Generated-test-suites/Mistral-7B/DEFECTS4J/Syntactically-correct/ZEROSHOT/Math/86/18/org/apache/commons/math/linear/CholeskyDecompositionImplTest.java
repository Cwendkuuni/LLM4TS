import org.apache.commons.math3.linear.Array2DRowRealMatrix;
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
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {3.0, 2.0, 1.0},
                {2.0, 5.0, 2.0},
                {1.0, 2.0, 8.0}
        });

        try {
            CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);
            RealMatrix l = decomposition.getL();
            RealMatrix lt = decomposition.getLT();
            RealMatrix expectedL = new Array2DRowRealMatrix(new double[][]{
                    {1.0, 0.0, 0.0},
                    {0.7416573867739413, 1.0, 0.0},
                    {0.4828421140884507, 0.4828421140884507, 1.0}
            });
            RealMatrix expectedLT = new Array2DRowRealMatrix(new double[][]{
                    {1.0, 2.0, 1.0},
                    {2.0, 3.7416573867739413, 1.4142137413795918},
                    {1.0, 1.4142137413795918, 3.44948974278317}
            });
            assertEquals(expectedL, l);
            assertEquals(expectedLT, lt);
        } catch (IllegalArgumentException | NotPositiveDefiniteMatrixException | NotSymmetricMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testSolveVector() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {3.0, 2.0, 1.0},
                {2.0, 5.0, 2.0},
                {1.0, 2.0, 8.0}
        });

        try {
            CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);
            RealVector b = new ArrayRealVector(new double[]{3.0, 4.0, 5.0});
            RealVector x = decomposition.getSolver().solve(b);
            RealVector expectedX = new ArrayRealVector(new double[]{1.0, 0.3827951134321275, -0.1224744871391549});
            assertEquals(expectedX, x);
        } catch (IllegalArgumentException | NotPositiveDefiniteMatrixException | NotSymmetricMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testSolveMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {3.0, 2.0, 1.0},
                {2.0, 5.0, 2.0},
                {1.0, 2.0, 8.0}
        });

        try {
            CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);
            RealMatrix b = MatrixUtils.createRealMatrix(new double[][]{
                    {1.0, 2.0},
                    {3.0, 4.0}
            });
            RealMatrix x = decomposition.getSolver().solve(b);
            RealMatrix expectedX = new Array2DRowRealMatrix(new double[][]{
                    {-0.5384693101051351, 1.1752062323918284},
                    {0.8314696125857283, -0.2797936543242553}
            });
            assertEquals(expectedX, x);
        } catch (IllegalArgumentException | NotPositiveDefiniteMatrixException | NotSymmetricMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testGetDeterminant() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {3.0, 2.0, 1.0},
                {2.0, 5.0, 2.0},
                {1.0, 2.0, 8.0}
        });

        try {
            CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);
            double determinant = decomposition.getDeterminant();
            assertEquals(35.0, determinant, 1e-14);
        } catch (IllegalArgumentException | NotPositiveDefiniteMatrixException | NotSymmetricMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNonSquareMatrixException() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {3.0, 2.0},
                {2.0, 5.0},
                {1.0, 2.0}
        });
        new CholeskyDecompositionImpl(matrix);
    }

    @Test(expected = NotSymmetricMatrixException.class)
    public void testNotSymmetricMatrixException() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {3.0, 2.0, 1.0},
                {2.0, 5.0, 2.0},
                {1.0, 2.0, 8.0},
                {1.0, 2.0, 8.0}
        });
        new CholeskyDecompositionImpl(matrix);
    }

    @Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testNotPositiveDefiniteMatrixException() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {3.0, 2.0, 1.0},
                {2.0, 2.0, 2.0},
                {1.0, 2.0, 8.0}
        });
        new CholeskyDecompositionImpl(matrix);
    }

}