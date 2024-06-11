import org.apache.commons.math3.linear.CholeskyDecomposition;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealVectorImpl;
import org.apache.commons.math3.linear.MatrixUtils;
import org.junit.Assert;

import java.util.Arrays;

public class CholeskyDecompositionImplTest {

    @org.junit.Test
    public void testIdentityMatrix() {
        RealMatrix matrix = MatrixUtils.createRealIdentityMatrix(3);
        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);
        RealMatrix l = decomposition.getL();
        RealMatrix lt = decomposition.getLT();
        RealMatrix expectedL = MatrixUtils.createRealIdentityMatrix(3);
        RealMatrix expectedLT = MatrixUtils.createRealIdentityMatrix(3);
        Assert.assertEquals(expectedL, l);
        Assert.assertEquals(expectedLT, lt);
        DecompositionSolver solver = decomposition.getSolver();
        RealVector b = new RealVectorImpl(new double[]{1, 2, 3}, false);
        RealVector x = solver.solve(b);
        Assert.assertArrayEquals(new double[]{1, 2, 3}, x.toArray(), 0.0);
    }

    @org.junit.Test(expected = org.apache.commons.math3.NonSquareMatrixException.class)
    public void testNonSquareMatrix() {
        RealMatrix matrix = MatrixUtils.createRealIdentityMatrix(2);
        matrix = MatrixUtils.appendColumn(matrix, new double[]{1, 1});
        new CholeskyDecompositionImpl(matrix);
    }

    @org.junit.Test(expected = org.apache.commons.math3.NotSymmetricMatrixException.class)
    public void testNotSymmetricMatrix() {
        RealMatrix matrix = new RealMatrix(new double[][]{
                {1, 1},
                {0, 1}
        });
        new CholeskyDecompositionImpl(matrix);
    }

    @org.junit.Test(expected = org.apache.commons.math3.NotPositiveDefiniteMatrixException.class)
    public void testNotPositiveDefiniteMatrix() {
        RealMatrix matrix = new RealMatrix(new double[][]{
                {1, 2},
                {2, 1}
        });
        new CholeskyDecompositionImpl(matrix);
    }

    @org.junit.Test
    public void testSymmetricPositiveDefiniteMatrix() {
        RealMatrix matrix = new RealMatrix(new double[][]{
                {1, 2},
                {2, 5}
        });
        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);
        RealMatrix l = decomposition.getL();
        RealMatrix lt = decomposition.getLT();
        RealMatrix expectedL = new RealMatrix(new double[][]{
                {1, 0},
                {2 / Math.sqrt(5), Math.sqrt(5)},
                {0, Math.sqrt(5)}
        });
        RealMatrix expectedLT = new RealMatrix(new double[][]{
                {1, 2 / Math.sqrt(5)},
                {2 / Math.sqrt(5), 1},
                {Math.sqrt(5), Math.sqrt(5)},
                {0, 0},
                {0, 0},
                {0, 0}
        });
        Assert.assertEquals(expectedL, l);
        Assert.assertEquals(expectedLT, lt);
        DecompositionSolver solver = decomposition.getSolver();
        RealVector b = new RealVectorImpl(new double[]{1, 1}, false);
        RealVector x = solver.solve(b);
        Assert.assertArrayEquals(new double[]{0.64280945, 0.74165926}, x.toArray(), 0.0);
    }

    @org.junit.Test(expected = org.apache.commons.math3.NotPositiveDefiniteMatrixException.class)
    public void testSingularMatrix() {
        RealMatrix matrix = new RealMatrix(new double[][]{
                {1, 0},
                {0, 0}
        });
        new CholeskyDecompositionImpl(matrix);
    }
}