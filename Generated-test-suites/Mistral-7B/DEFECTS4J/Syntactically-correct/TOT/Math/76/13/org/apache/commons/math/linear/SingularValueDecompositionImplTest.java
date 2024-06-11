package org.apache.commons.math.linear;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.Array2DRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for SingularValueDecompositionImpl.
 */
public class SingularValueDecompositionImplTest {

    /**
     * Test the Singular Value Decomposition with a 2x2 matrix.
     */
    @Test
    public void test2x2() {
        // Given
        RealMatrix matrix = new Array2DRealMatrix(new double[][]{
            {1.0, 1.0},
            {1.0, 1.0}
        });
        SingularValueDecompositionImpl svd = null;
        try {
            svd = new SingularValueDecompositionImpl(matrix);
        } catch (InvalidMatrixException e) {
            // Should not happen
            Assert.fail(e.getMessage());
        }

        // When
        RealMatrix u = svd.getU();
        double[] singularValues = svd.getSingularValues();
        RealMatrix v = svd.getV();

        // Then
        Assert.assertEquals(2, u.getRowDimension());
        Assert.assertEquals(2, u.getColumnDimension());
        Assert.assertEquals(2, v.getRowDimension());
        Assert.assertEquals(2, v.getColumnDimension());
        Assert.assertArrayEquals(new double[]{2.449489742783178, 0.0},
                                singularValues, 0.001);

        // Check orthogonality of U
        RealMatrix uUT = u.transpose();
        RealMatrix uU = u.multiply(uUT);
        Assert.assertTrue(uU.isIdentityMatrix());

        // Check orthogonality of V
        RealMatrix vVT = v.transpose();
        RealMatrix vV = v.multiply(vVT);
        Assert.assertTrue(vV.isIdentityMatrix());

        // Check U &times; S &times; V = A
        RealMatrix product = u.multiply(new Array2DRowRealMatrix(singularValues, false))
                             .multiply(v);
        RealMatrix a = matrix.copy();
        Assert.assertEquals(a, product);

        // Check computation of pseudo-inverse
        DecompositionSolver solver = svd.getSolver();
        RealVector b = new Array2DRealMatrix(new double[][]{
            {1.0, 1.0},
            {0.0, 1.0}
        }).transpose().getColumnVector(0);
        RealVector solution = solver.solve(b);
        Assert.assertEquals(new Array2DRealMatrix(new double[][]{
            {0.5, 0.5},
            {0.5, 0.5}
        }).getColumnVector(0), solution, 0.001);
    }

    /**
     * Test the Singular Value Decomposition with a 2x3 matrix.
     */
    @Test
    public void test2x3() {
        // Given
        RealMatrix matrix = new Array2DRealMatrix(new double[][]{
            {1.0, 2.0, 3.0},
            {4.0, 5.0, 6.0}
        });
        SingularValueDecompositionImpl svd = null;
        try {
            svd = new SingularValueDecompositionImpl(matrix, 2);
        } catch (InvalidMatrixException e) {
            // Should not happen
            Assert.fail(e.getMessage());
        }

        // When
        RealMatrix u = svd.getU();
        double[] singularValues = svd.getSingularValues();
        RealMatrix v = svd.getV();

        // Then
        Assert.assertEquals(2, u.getRowDimension());
        Assert.assertEquals(2, u.getColumnDimension());
        Assert.assertEquals(3, v.getRowDimension());
        Assert.assertEquals(2, v.getColumnDimension());
        Assert.assertArrayEquals(new double[]{2.5811388300841897, 0.0, 0.0},
                                singularValues, 0.001);

        // Check orthogonality of U
        RealMatrix uUT = u.transpose();
        RealMatrix uU = u.multiply(uUT);
        Assert.assertTrue(uU.isIdentityMatrix());

        // Check orthogonality of V
        RealMatrix vVT = v.transpose();
        RealMatrix vV = v.multiply(vVT);
        Assert.assertTrue(vV.isIdentityMatrix());

        // Check U &times; S &times; V = A
        RealMatrix product = u.multiply(new Array2DRowRealMatrix(singularValues, false))
                             .multiply(v);
        RealMatrix a = matrix.copy();
        Assert.assertEquals(a, product, 0.001);

        // Check computation of pseudo-inverse
        DecompositionSolver solver = svd.getSolver();
        RealVector b = new Array2DRealMatrix(new double[][]{
            {1.0, 0.0},
            {0.0, 1.0}
        }).transpose().getColumnVector(0);
        RealVector solution = solver.solve(b);
        Assert.assertEquals(new Array2DRealMatrix(new double[][]{
            {-0.538460747334157, 0.8164966331322919},
            {0.8164966331322919, -0.538460747334157}
        }).getColumnVector(0), solution, 0.01);

        // Check computation of pseudo-inverse with a singular matrix
        RealVector bSingular = new Array2DRealMatrix(new double[][]{
            {1.0, 0.0},
            {0.0, 0.0}
        }).transpose().getColumnVector(0);
        try {
            solver.solve(bSingular);
            // Should not happen
            Assert.fail();
        } catch (Exception e) {
            // Expected
        }
    }

    /**
     * Test the Singular Value Decomposition with a 3x3 matrix.
     */
    @Test
    public void test3x3() {
        // Given
        RealMatrix matrix = new Array2DRealMatrix(new double[][]{
            {1.0, 1.0, 1.0},
            {1.0, 1.0, 1.0},
            {1.0, 1.0, 1.0}
        });
        SingularValueDecompositionImpl svd = null;
        try {
            svd = new SingularValueDecompositionImpl(matrix);
        } catch (InvalidMatrixException e) {
            // Should not happen
            Assert.fail(e.getMessage());
        }

        // When
        RealMatrix u = svd.getU();
        double[] singularValues = svd.getSingularValues();
        RealMatrix v = svd.getV();

        // Then
        Assert.assertEquals(3, u.getRowDimension());
        Assert.assertEquals(3, u.getColumnDimension());
        Assert.assertEquals(3, v.getRowDimension());
        Assert.assertEquals(3, v.getColumnDimension());
        Assert.assertArrayEquals(new double[]{3.7416573867739413, 0.0, 0.0},
                                singularValues, 0.001);

        // Check orthogonality of U
        RealMatrix uUT = u.transpose();
        RealMatrix uU = u.multiply(uUT);
        Assert.assertTrue(uU.isIdentityMatrix());

        // Check orthogonality of V
        RealMatrix vVT = v.transpose();
        RealMatrix vV = v.multiply(vVT);
        Assert.assertTrue(vV.isIdentityMatrix());

        // Check U &times; S &times; V = A
        RealMatrix product = u.multiply(new Array2DRowRealMatrix(singularValues, false))
                             .multiply(v);
        RealMatrix a = matrix.copy();
        Assert.assertEquals(a, product, 0.001);

        // Check computation of pseudo-inverse
        DecompositionSolver solver = svd.getSolver();
        RealVector b = new Array2DRealMatrix(new double[][]{
            {1.0, 0.0, 0.0},
            {0.0, 1.0, 0.0},
            {0.0, 0.0, 1.0}
        }).transpose().getColumnVector(0);
        RealVector solution = solver.solve(b);
        Assert.assertEquals(new Array2DRealMatrix(new double[][]{
            {0.3659558223397293, -0.4999999999999997, 0.816496633132291},
            {-0.4999999999999997, 0.3659558223397293, -0.816496633132291},
            {0.816496633132291, -0.816496633132291, 0.3659558223397293}
        }).getColumnVector(0), solution, 0.01);

        // Check computation of pseudo-inverse with a singular matrix
        RealVector bSingular = new Array2DRealMatrix(new double[][]{
            {1.0, 0.0, 0.0},
            {0.0, 1.0, 0.0},
            {0.0, 0.0, 0.0}
        }).transpose().getColumnVector(0);
        try {
            solver.solve(bSingular);
            // Should not happen
            Assert.fail();
        } catch (Exception e) {
            // Expected
        }
    }

    /**
     * Test the Singular Value Decomposition with a 3x2 matrix.
     */
    @Test
    public void test3x2() {
        // Given
        RealMatrix matrix = new Array2DRealMatrix(new double[][]{
            {1.0, 2.0},
            {1.0, 2.0},
            {1.0, 2.0}
        });
        SingularValueDecompositionImpl svd = null;
        try {
            svd = new SingularValueDecompositionImpl(matrix, 1);
        } catch (InvalidMatrixException e) {
            // Should not happen
            Assert.fail(e.getMessage());
        }

        // When
        RealMatrix u = svd.getU();
        double[] singularValues = svd.getSingularValues();
        RealMatrix v = svd.getV();

        // Then
        Assert.assertEquals(3, u.getRowDimension());
        Assert.assertEquals(2, u.getColumnDimension());
        Assert.assertEquals(2, v.getRowDimension());
        Assert.assertEquals(2, v.getColumnDimension());
        Assert.assertArrayEquals(new double[]{3.7416573867739413, 0.0},
                                singularValues, 0.001);

        // Check orthogonality of U
        RealMatrix uUT = u.transpose();
        RealMatrix uU = u.multiply(uUT);
        Assert.assertTrue(uU.isIdentityMatrix());

        // Check orthogonality of V
        RealMatrix vVT = v.transpose();
        RealMatrix vV = v.multiply(vVT);
        Assert.assertTrue(vV.isIdentityMatrix());

        // Check U &times; S &times; V = A
        RealMatrix product = u.multiply(new Array2DRowRealMatrix(singularValues, false))
                             .multiply(v);
        RealMatrix a = matrix.copy();
        Assert.assertEquals(a, product, 0.001);

        // Check computation of pseudo-inverse
        DecompositionSolver solver = svd.getSolver();
        RealVector b = new Array2DRealMatrix(new double[][]{
            {1.0, 0.0},
            {0.0, 1.0}
        }).transpose().getColumnVector(0);
        RealVector solution = solver.solve(b);
        Assert.assertEquals(new Array2DRealMatrix(new double[][]{
            {0.525425354158533, -0.816496633132291},
            {-0.816496633132291, 0.525425354158533},
            {0.525425354158533, -0.816496633132291}
        }).getColumnVector(0), solution, 0.01);

        // Check computation of pseudo-inverse with a singular matrix
        RealVector bSingular = new Array2DRealMatrix(new double[][]{
            {1.0, 0.0},
            {0.0, 0.0}
        }).transpose().getColumnVector(0);
        try {
            solver.solve(bSingular);
            // Should not happen
            Assert.fail();
        } catch (Exception e) {
            // Expected
        }
    }

}