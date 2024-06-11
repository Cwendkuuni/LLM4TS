package org.apache.commons.math.linear;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.EigenvalueDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.linear.Solver;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for the SingularValueDecompositionImpl class.
 */
public class SingularValueDecompositionImplTest {

    /**
     * Test calcSVD with a simple 2x2 matrix.
     * @throws Exception if an error occurs
     */
    @Test
    public void test2x2()
        throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix u = svd.getU();
        RealMatrix s = svd.getS();
        RealMatrix v = svd.getV();
        Assert.assertEquals(u.getRowDimension(), matrix.getRowDimension());
        Assert.assertEquals(u.getColumnDimension(), matrix.getColumnDimension());
        Assert.assertEquals(s.getRowDimension(), s.getColumnDimension());
        Assert.assertEquals(s.getColumnDimension(), Math.min(matrix.getRowDimension(), matrix.getColumnDimension()));
        Assert.assertEquals(v.getRowDimension(), matrix.getColumnDimension());
        Assert.assertEquals(v.getColumnDimension(), matrix.getColumnDimension());
        Assert.assertTrue(Precision.equals(u.multiply(s).multiply(v.transpose()), matrix, 1e-12));
    }

    /**
     * Test calcSVD with a simple 3x3 matrix.
     * @throws Exception if an error occurs
     */
    @Test
    public void test3x3()
        throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix u = svd.getU();
        RealMatrix s = svd.getS();
        RealMatrix v = svd.getV();
        Assert.assertEquals(u.getRowDimension(), matrix.getRowDimension());
        Assert.assertEquals(u.getColumnDimension(), matrix.getColumnDimension());
        Assert.assertEquals(s.getRowDimension(), s.getColumnDimension());
        Assert.assertEquals(s.getColumnDimension(), Math.min(matrix.getRowDimension(), matrix.getColumnDimension()));
        Assert.assertEquals(v.getRowDimension(), matrix.getColumnDimension());
        Assert.assertEquals(v.getColumnDimension(), matrix.getColumnDimension());
        Assert.assertTrue(Precision.equals(u.multiply(s).multiply(v.transpose()), matrix, 1e-12));
    }

    /**
     * Test calcSVD with a singular 2x2 matrix.
     * @throws Exception if an error occurs
     */
    @Test
    public void testSingular2x2()
        throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{0.0, 1.0}, {1.0, 0.0}});
        SingularValueDecompositionImpl svd = null;
        try {
            svd = new SingularValueDecompositionImpl(matrix);
        } catch (InvalidMatrixException ime) {
            // expected
        }
        Assert.assertNull(svd);
    }

    /**
     * Test getSolver method with a simple 2x2 matrix.
     * @throws Exception if an error occurs
     */
    @Test
    public void test2x2Solver()
        throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        DecompositionSolver solver = svd.getSolver();
        RealVector b = new ArrayRealVector(new double[]{2.0, 4.0});
        RealVector x = solver.solve(b);
        RealVector expected = new ArrayRealVector(new double[]{1.0 / (singularValue(matrix, 0.0) + 1.0),
                                                              1.0 / (singularValue(matrix, 1.0) + 1.0)});
        Assert.assertEquals(expected, x, 1e-12);
    }

    /**
     * Test getSolver method with a singular 2x2 matrix.
     * @throws Exception if an error occurs
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSingular2x2Solver()
        throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{0.0, 1.0}, {1.0, 0.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        DecompositionSolver solver = svd.getSolver();
        RealVector b = new ArrayRealVector(new double[]{0.0, 1.0});
        solver.solve(b);
    }

    /**
     * Test getSolver method with a 3x3 matrix.
     * @throws Exception if an error occurs
     */
    @Test
    public void test3x3Solver()
        throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        DecompositionSolver solver = svd.getSolver();
        RealVector b = new ArrayRealVector(new double[]{2.0, 4.0, 6.0});
        RealVector x = solver.solve(b);
        RealVector expected = new ArrayRealVector(new double[]{1.0 / (singularValue(matrix, 0.0) + 1.0),
                                                              1.0 / (singularValue(matrix, 1.0) + 1.0),
                                                              1.0 / (singularValue(matrix, 2.0) + 1.0)});
        Assert.assertEquals(expected, x, 1e-12);
    }

    /**
     * Helper method to get the singular value for a given matrix and cutoff.
     * @param matrix matrix
     * @param cutoff cutoff
     * @return singular value
     */
    private double singularValue(RealMatrix matrix, double cutoff) {
        SingularValueDecompositionImpl svd = null;
        try {
            svd = new SingularValueDecompositionImpl(matrix, (int) Math.ceil(matrix.getRowDimension() * 0.95));
        } catch (InvalidMatrixException ime) {
            // expected
            return 0.0;
        }
        return svd.getSingularValues()[0];
    }

}