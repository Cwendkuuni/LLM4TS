import org.apache.commons.math3.linear.CholeskyDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.apache.commons.math3.special.Epsilon;
import org.junit.Assert;

public class CholeskyDecompositionImplTest {

    @org.junit.Test
    public void testIdentityMatrix() {
        RealMatrix matrix = RealMatrixUtils.createIdentityMatrix(5);
        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);
        RealMatrix l = decomposition.getL();
        RealMatrix lt = decomposition.getLT();
        RealMatrix inverse = decomposition.getSolver().getInverse();
        RealMatrix product = l.multiply(lt);
        RealMatrix productInverse = lt.multiply(l);
        RealMatrix expectedProduct = RealMatrixUtils.createIdentityMatrix(5);
        RealMatrix expectedProductInverse = RealMatrixUtils.createIdentityMatrix(5);

        Assert.assertEquals("Product of L and LT should be identity", expectedProduct, product, Epsilon.getDoublesEpsilon(1e-13));
        Assert.assertEquals("Product of LT and L should be identity", expectedProductInverse, productInverse, Epsilon.getDoublesEpsilon(1e-13));
        Assert.assertEquals("Determinant of identity matrix should be 1", 1.0, decomposition.getDeterminant(), Epsilon.getDoublesEpsilon(1e-13));
    }

    @org.junit.Test
    public void testSymmetricMatrix() {
        double[][] data = {
                {2.0, 1.0, 1.0, 1.0, 1.0},
                {1.0, 2.0, 1.0, 1.0, 1.0},
                {1.0, 1.0, 2.0, 1.0, 1.0},
                {1.0, 1.0, 1.0, 2.0, 1.0},
                {1.0, 1.0, 1.0, 1.0, 2.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(data, false);
        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);
        RealMatrix l = decomposition.getL();
        RealMatrix lt = decomposition.getLT();

        Assert.assertEquals("Symmetry of LT", l, lt.transpose(), Epsilon.getDoublesEpsilon(1e-13));
    }

    @org.junit.Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testNotPositiveDefiniteMatrix() {
        double[][] data = {
                {1.0, 2.0, 2.0},
                {2.0, -1.0, 1.0},
                {2.0, 1.0, -1.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(data, false);
        new CholeskyDecompositionImpl(matrix);
    }

    @org.junit.Test(expected = NotSymmetricMatrixException.class)
    public void testNotSymmetricMatrix() {
        double[][] data = {
                {2.0, 1.0, 1.0},
                {1.0, 2.0, 1.0},
                {1.0, 1.0, -2.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(data, false);
        new CholeskyDecompositionImpl(matrix);
    }

    @org.junit.Test
    public void testSolveVector() {
        double[][] data = {
                {2.0, 1.0, 1.0},
                {1.0, 2.0, 1.0},
                {1.0, 1.0, 2.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(data, false);
        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);
        RealVector b = new ArrayRealVector(new double[]{1.0, 2.0, 3.0});
        RealVector x = decomposition.getSolver().solve(b);
        double[] expected = {-0.3333333333333333, -0.6666666666666666, 1.0};

        Assert.assertArrayEquals("Solution for vector", expected, x.toArray(), Epsilon.getDoublesEpsilon(1e-13));
    }

    @org.junit.Test
    public void testSolveMatrix() {
        double[][] data = {
                {2.0, 1.0, 1.0},
                {1.0, 2.0, 1.0},
                {1.0, 1.0, 2.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(data, false);
        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);
        RealMatrix b = RealMatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        RealMatrix x = decomposition.getSolver().solve(b);
        double[][] expected = {{-0.6, -1.2}, {1.2, 2.4}};

        Assert.assertArrayEquals("Solution for matrix", expected, x.getData(), Epsilon.getDoublesEpsilon(1e-13));
    }

}