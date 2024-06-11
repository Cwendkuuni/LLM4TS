import org.apache.commons.math3.linear.*;
import org.junit.Assert.*;
import org.junit.Test;

public class CholeskyDecompositionImplTest {

    @Test
    public void testIdentityMatrix() throws Exception {
        RealMatrix matrix = MatrixUtils.createRealIdentityMatrix(5);
        CholeskyDecomposition decomposition = new CholeskyDecompositionImpl(matrix);
        RealMatrix l = decomposition.getL();
        RealMatrix lt = decomposition.getLT();
        RealMatrix inverse = decomposition.getSolver().getInverse();
        RealMatrix expectedIdentity = MatrixUtils.createRealIdentityMatrix(5);
        assertEquals(expectedIdentity, l.multiply(lt));
        assertEquals(expectedIdentity, lt.multiply(l));
        assertEquals(expectedIdentity, l.multiply(inverse));
        assertEquals(expectedIdentity, inverse.multiply(l));
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testNonSquareMatrix() throws Exception {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {3, 4, 5}});
        new CholeskyDecompositionImpl(matrix);
    }

    @Test(expected = NotSymmetricMatrixException.class)
    public void testNotSymmetricMatrix() throws Exception {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {3, 4}});
        new CholeskyDecompositionImpl(matrix, 0.001, 0.001);
    }

    @Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testNotPositiveDefiniteMatrix() throws Exception {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {2, -1}});
        new CholeskyDecompositionImpl(matrix, 0.001, 0.001);
    }

    @Test
    public void testDeterminant() throws Exception {
        RealMatrix matrix = MatrixUtils.createRealIdentityMatrix(5);
        CholeskyDecomposition decomposition = new CholeskyDecompositionImpl(matrix);
        Assert.assertEquals(1.0, decomposition.getDeterminant(), 1e-10);
    }

    @Test
    public void testSolve() throws Exception {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {2, 3}});
        CholeskyDecomposition decomposition = new CholeskyDecompositionImpl(matrix, 0.001, 0.001);
        RealVector b = new ArrayRealVector(new double[]{1, 2});
        RealVector x = decomposition.getSolver().solve(b);
        RealVector expectedX = new ArrayRealVector(new double[]{3.0 / Math.sqrt(2.0), 1.0 / Math.sqrt(2.0)});
        assertEquals(expectedX, x);
    }

    @Test
    public void testSolveMatrix() throws Exception {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {2, 3}});
        CholeskyDecomposition decomposition = new CholeskyDecompositionImpl(matrix, 0.001, 0.001);
        RealMatrix b = MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {2, 3}});
        RealMatrix x = decomposition.getSolver().solve(b);
        RealMatrix expectedX = MatrixUtils.createRealMatrix(new double[][]{{3.0 / Math.sqrt(2.0), 1.0 / Math.sqrt(2.0)}, {-1.0 / Math.sqrt(2.0), 3.0 / Math.sqrt(2.0)}});
        assertEquals(expectedX, x);
    }

}