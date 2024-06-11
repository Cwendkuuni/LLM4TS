import org.apache.commons.math3.linear.*;
import org.junit.Assert;
import org.junit.Test;

public class SingularValueDecompositionImplTest {

    @Test
    public void testSingularValueDecomposition() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {3, 4}});
        try {
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
            RealMatrix u = svd.getU();
            RealMatrix s = svd.getS();
            RealMatrix v = svd.getV();
            RealMatrix product = u.multiply(s).multiply(v.transpose());
            Assert.assertEquals(matrix, product, 0.001);
        } catch (InvalidMatrixException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testSingularValueDecompositionIdentity() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 0}, {0, 1}});
        try {
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
            RealMatrix u = svd.getU();
            RealMatrix s = svd.getS();
            RealMatrix v = svd.getV();
            RealMatrix product = u.multiply(s).multiply(v.transpose());
            Assert.assertEquals(MatrixUtils.IDENTITY_MATRIX, product, 0.001);
        } catch (InvalidMatrixException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testSingularValueDecompositionZeroDiagonal() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 0}, {0, 0}});
        try {
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
            RealMatrix u = svd.getU();
            RealMatrix s = svd.getS();
            RealMatrix v = svd.getV();
            RealMatrix product = u.multiply(s).multiply(v.transpose());
            Assert.assertEquals(new Array2DRowRealMatrix(new double[][]{{1, 0}, {0, 0}}), product, 0.001);
        } catch (SingularMatrixException e) {
            Assert.assertTrue(true); // expected singular value decomposition failure
        }
    }

    @Test
    public void testSingularValueDecompositionNegativeDiagonal() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {-3, -4}});
        try {
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
            RealMatrix u = svd.getU();
            RealMatrix s = svd.getS();
            RealMatrix v = svd.getV();
            RealMatrix product = u.multiply(s).multiply(v.transpose());
            Assert.assertEquals(matrix, product, 0.001);
        } catch (InvalidMatrixException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSingularValueDecompositionWithWrongDimensions() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {3, 4}, {5, 6}});
        new SingularValueDecompositionImpl(matrix);
    }

    @Test(expected = InvalidMatrixException.class)
    public void testSingularValueDecompositionWithSingularMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2, 1}, {2, 1, 1}, {1, 1, 0}});
        new SingularValueDecompositionImpl(matrix);
    }

    @Test
    public void testGetRank() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {3, 4}});
        try {
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
            int rank = svd.getRank();
            Assert.assertEquals(2, rank);
        } catch (InvalidMatrixException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testGetConditionNumber() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {3, 4}});
        try {
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
            double conditionNumber = svd.getConditionNumber();
            Assert.assertTrue(conditionNumber > 0);
        } catch (InvalidMatrixException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testGetNorm() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {3, 4}});
        try {
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
            double norm = svd.getNorm();
            Assert.assertTrue(norm > 0);
        } catch (InvalidMatrixException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testGetCovariance() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2, 3}, {3, 4, 5}, {5, 6, 7}});
        try {
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
            RealMatrix covariance = svd.getCovariance(2);
            RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1.29155257, 2.4141453}, {2.4141453, 3.42857143}});
            Assert.assertEquals(expected, covariance, 0.001);
        } catch (InvalidMatrixException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

}