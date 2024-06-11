package org.apache.commons.math.linear;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.DefaultRealMatrixPreservingVisitor;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.EigenDecompositionImpl;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.junit.Assert;
import org.junit.Test;

public class SingularValueDecompositionImplTest {

    @Test
    public void testSingularValueDecomposition() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {1.0, 2.0, 3.0},
            {4.0, 5.0, 6.0},
            {7.0, 8.0, 9.0}
        });

        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix u = svd.getU();
        RealMatrix s = svd.getS();
        RealMatrix v = svd.getV();

        RealMatrix uTimesS = u.multiply(s);
        RealMatrix sTimesVt = s.transpose().multiply(v);
        RealMatrix product = uTimesS.add(sTimesVt.negate());

        Assert.assertEquals(Array2DRowRealMatrix.ZERO, product, 1e-9);
    }

    @Test(expected = InvalidMatrixException.class)
    public void testSingularValueDecompositionWithInvalidMatrix() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {1.0, 2.0},
            {4.0, 5.0},
            {7.0, 8.0}
        });

        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
    }

    @Test
    public void testGetSingularValues() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {1.0, 2.0, 3.0},
            {4.0, 5.0, 6.0},
            {7.0, 8.0, 9.0}
        });

        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        double[] singularValues = svd.getSingularValues();

        Assert.assertArrayEquals(new double[]{1.0, 5.98515225556523, 0.0}, singularValues, 1e-9);
    }

    @Test
    public void testGetRank() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {1.0, 2.0, 3.0},
            {4.0, 5.0, 6.0},
            {7.0, 8.0, 9.0}
        });

        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        int rank = svd.getRank();

        Assert.assertEquals(3, rank);
    }

    @Test
    public void testGetCovariance() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {1.0, 2.0, 3.0},
            {4.0, 5.0, 6.0},
            {7.0, 8.0, 9.0}
        });

        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix covariance = svd.getCovariance(1.0);

        Assert.assertEquals(2, covariance.getRowDimension());
        Assert.assertEquals(2, covariance.getColumnDimension());
        Assert.assertTrue(covariance.isSymmetric());
    }

    @Test
    public void testGetNorm() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {1.0, 2.0, 3.0},
            {4.0, 5.0, 6.0},
            {7.0, 8.0, 9.0}
        });

        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        double norm = svd.getNorm();

        Assert.assertEquals(1.0, norm, 1e-9);
    }

    @Test
    public void testGetConditionNumber() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {1.0, 2.0, 3.0},
            {4.0, 5.0, 6.0},
            {7.0, 8.0, 9.0}
        });

        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        double conditionNumber = svd.getConditionNumber();

        Assert.assertTrue(conditionNumber > 1.0);
    }

    @Test
    public void testGetSolver() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {1.0, 2.0, 3.0},
            {4.0, 5.0, 6.0},
            {7.0, 8.0, 9.0}
        });

        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        DecompositionSolver solver = svd.getSolver();

        RealVector b = new ArrayRealVector(new double[]{1.0, 2.0, 3.0});
        RealVector x = solver.solve(b);

        Assert.assertArrayEquals(new double[]{1.0 / 3.0, 2.0 / 3.0, 1.0 / 3.0}, x.toArray(), 1e-9);
    }

}