import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.linear.Vector3;
import org.junit.Assert;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testEmptyMatrix() {
        try {
            new RectangularCholeskyDecomposition(RealMatrixUtils.createRealMatrix(0, 0), 0.0);
            Assert.fail();
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
    }

    @Test
    public void testIdentityMatrix() {
        RealMatrix matrix = RealMatrixUtils.createIdentityMatrix(5);
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.0);
            RealMatrix root = decomposition.getRootMatrix();
            SingularValueDecomposition svd = new SingularValueDecomposition(matrix);
            RealMatrix expected = RealMatrixUtils.createRealMatrix(5, 5).subtract(MatrixUtils.multiply(svd.getU(), svd.getVT()));
            Assert.assertEquals(expected, root);
            Assert.assertEquals(5, decomposition.getRank());
        } catch (NonPositiveDefiniteMatrixException e) {
            Assert.fail();
        }
    }

    @Test
    public void testNaN() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, Double.NaN}, {Double.NaN, 2.0}});
        try {
            new RectangularCholeskyDecomposition(matrix, 0.0);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testSingularMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {1.0, 1.0}});
        try {
            new RectangularCholeskyDecomposition(matrix, 0.0);
            Assert.fail();
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
    }

    @Test
    public void testRectangularCholeskyDecomposition() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 5.0}, {3.0, 5.0, 6.0}});
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-4);
            RealMatrix root = decomposition.getRootMatrix();
            RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0, 1.0}, {1.0, 2.0, 2.0}, {1.0, 2.0, 3.0}});
            Assert.assertEquals(expected, root);
            Assert.assertEquals(3, decomposition.getRank());

            // check that we can calculate the original matrix with the root matrix
            RealMatrix calculatedMatrix = MatrixUtils.multiply(MatrixUtils.multiply(root, root.transpose()), matrix);
            Assert.assertEquals(matrix, calculatedMatrix, 1e-6);

        } catch (NonPositiveDefiniteMatrixException e) {
            Assert.fail();
        }
    }

    @Test
    public void testCorrelatedRandomVectorGenerator() {
        RealMatrix covarianceMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.5, 0.5}, {0.5, 2.0, 0.5}, {0.5, 0.5, 2.0}});
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(covarianceMatrix, 1e-6);
            RealMatrix root = decomposition.getRootMatrix();
            Vector3 v1 = new Vector3(new double[]{1.0, 0.0, 0.0});
            Vector3 v2 = new Vector3(new double[]{0.0, 1.0, 0.0});
            Vector3 v3 = new Vector3(new double[]{0.0, 0.0, 1.0});
            Vector3 sample1 = new Vector3(MatrixUtils.multiply(root, new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0}}).getColumnVector(0)));
            Vector3 sample2 = new Vector3(MatrixUtils.multiply(root, new Array2DRowRealMatrix(new double[][]{{0.0, 1.0, 0.0}}).getColumnVector(0)));
            Vector3 sample3 = new Vector3(MatrixUtils.multiply(root, new Array2DRowRealMatrix(new double[][]{{0.0, 0.0, 1.0}}).getColumnVector(0)));
            Assert.assertEquals(v1, sample1, 1e-6);
            Assert.assertEquals(v2, sample2, 1e-6);
            Assert.assertEquals(v3, sample3, 1e-6);
            Assert.assertEquals(v1, new Vector3(covarianceMatrix.operate(new Vector3(v1.toArray()))), 1e-6);
            Assert.assertEquals(v2, new Vector3(covarianceMatrix.operate(new Vector3(v2.toArray()))), 1e-6);
            Assert.assertEquals(v3, new Vector3(covarianceMatrix.operate(new Vector3(v3.toArray()))), 1e-6);

        } catch (NonPositiveDefiniteMatrixException e) {
            Assert.fail();
        }
    }

}