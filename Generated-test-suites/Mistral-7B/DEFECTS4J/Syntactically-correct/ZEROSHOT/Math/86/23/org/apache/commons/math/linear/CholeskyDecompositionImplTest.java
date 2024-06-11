import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.CholeskyDecomposition;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealVectorImpl;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Assert;
import org.junit.Test;

public class CholeskyDecompositionImplTest {

    @Test
    public void testCholeskyDecomposition() {
        double[][] inputMatrix = {
                {4.0, 1.0, 1.0},
                {1.0, 4.0, 1.0},
                {1.0, 1.0, 4.0}
        };

        RealMatrix matrix = new Array2DRowRealMatrix(inputMatrix);

        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        RealMatrix l = choleskyDecomposition.getL();
        RealMatrix lT = choleskyDecomposition.getLT();

        RealMatrix expectedL = new Array2DRowRealMatrix(new double[][]{
                {2.0, 1.0, 1.0},
                {1.0, 2.0, 1.0},
                {1.0, 1.0, Math.sqrt(3.0)}
        });

        RealMatrix expectedLT = expectedL.transpose();

        Assert.assertEquals(expectedL, l);
        Assert.assertEquals(expectedLT, lT);

        RealMatrix determinantCheck = l.multiply(l.transpose());
        Assert.assertEquals(MatrixUtils.createRealIdentityMatrix(3), determinantCheck);

        DecompositionSolver solver = choleskyDecomposition.getSolver();
        RealVector b = new Array2DRowRealVector(new double[][]{
                {1.0},
                {1.0},
                {1.0}
        });

        RealVector x = solver.solve(b);
        RealVector expectedX = new Array2DRowRealVector(new double[][]{
                {0.5},
                {0.5},
                {1.0 / Math.sqrt(3.0)}
        });

        Assert.assertEquals(expectedX, x);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testNonSquareMatrixException() {
        double[][] inputMatrix = {
                {4.0, 1.0},
                {1.0, 4.0},
                {1.0, 1.0}
        };

        RealMatrix matrix = new Array2DRowRealMatrix(inputMatrix);

        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);

    }

    @Test(expected = NotSymmetricMatrixException.class)
    public void testNotSymmetricMatrixException() {
        double[][] inputMatrix = {
                {4.0, 1.0, 1.0},
                {1.0, 4.0, 1.0},
                {1.0, 1.0, 5.0}
        };

        RealMatrix matrix = new Array2DRowRealMatrix(inputMatrix);

        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);

    }

    @Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testNotPositiveDefiniteMatrixException() {
        double[][] inputMatrix = {
                {1.0, 1.0},
                {1.0, -1.0}
        };

        RealMatrix matrix = new Array2DRowRealMatrix(inputMatrix);

        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);

    }

}