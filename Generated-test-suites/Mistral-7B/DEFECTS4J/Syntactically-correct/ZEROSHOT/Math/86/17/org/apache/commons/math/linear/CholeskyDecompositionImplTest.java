import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.CholeskyDecomposition;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.junit.Assert;
import org.junit.Test;

public class CholeskyDecompositionImplTest {

    @Test
    public void testIdentityMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});
        CholeskyDecomposition choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        RealMatrix l = choleskyDecomposition.getL();
        RealMatrix lt = choleskyDecomposition.getLT();
        DecompositionSolver solver = choleskyDecomposition.getSolver();

        Assert.assertEquals(l.getRowDimension(), l.getColumnDimension());
        Assert.assertEquals(lt.getRowDimension(), lt.getColumnDimension());
        Assert.assertEquals(l.getRowDimension(), lt.getRowDimension());

        RealVector b = new Array2DRowRealVector(new double[][]{new double[]{1.0}, new double[]{1.0}});
        RealVector x = solver.solve(b);
        Assert.assertArrayEquals(new double[]{1.0, 1.0}, x.toArray(), 0.0);

        RealMatrix inv = l.getTranspose().multiply(l);
        Assert.assertTrue(inv.isIdentityMatrix());
    }

    @Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{2.0, 1.0}, {1.0, 2.0}});
        new CholeskyDecompositionImpl(matrix);
    }

    @Test(expected = NotSymmetricMatrixException.class)
    public void testNonSymmetricMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {1.0, 2.0}});
        new CholeskyDecompositionImpl(matrix, 0.001, 0.0);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testNonSquareMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {1.0}});
        new CholeskyDecompositionImpl(matrix);
    }

    @Test
    public void testSolve() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{4.0, 1.0, 1.0}, {1.0, 2.0, 1.0}, {1.0, 1.0, 3.0}});
        CholeskyDecomposition choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        RealMatrix l = choleskyDecomposition.getL();
        RealVector b = new Array2DRowRealVector(new double[]{1.0, 2.0, 3.0});
        RealVector x = choleskyDecomposition.getSolver().solve(b);

        RealVector expected = new Array2DRowRealVector(new double[]{1.5, -0.5, 1.0});
        Assert.assertArrayEquals(expected.toArray(), x.toArray(), 0.001);
    }

    @Test
    public void testDeterminant() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{2.0, 1.0}, {1.0, 3.0}});
        CholeskyDecomposition choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        double determinant = choleskyDecomposition.getDeterminant();

        Assert.assertEquals(determinant, 3.0, 0.0);
    }

}