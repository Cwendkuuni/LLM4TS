package org.apache.commons.math.linear;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.special.EigenDecomposition;
import org.apache.commons.math3.special.MatrixUtils;
import org.apache.commons.math3.special.SingularValueDecomposition;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for {@link SingularValueDecompositionImpl} class.
 */
public class SingularValueDecompositionImplTest {

    /**
     * Test the construction and basic method calls of the SVD implementation.
     */
    @Test
    public void testSVD() {
        // create a test matrix
        double[][] data = {
            {1.0, 2.0, 3.0},
            {4.0, 5.0, 6.0},
            {7.0, 8.0, 9.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(data, false);

        try {
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);

            // check the U, S and V matrices
            RealMatrix u = svd.getU();
            RealMatrix s = svd.getS();
            RealMatrix v = svd.getV();
            RealMatrix expectedU = new SingularValueDecomposition(matrix).getU();
            RealMatrix expectedS = new SingularValueDecomposition(matrix).getSingularValues();
            RealMatrix expectedV = new SingularValueDecomposition(matrix).getV();

            Assert.assertEquals(expectedU, u);
            Assert.assertEquals(expectedS, s);
            Assert.assertEquals(expectedV, v);

            // check the singular values
            double[] singularValues = svd.getSingularValues();
            double[] expectedSingularValues = expectedS.getData()[0];
            Assert.assertArrayEquals(expectedSingularValues, singularValues, 0.0);

            // check the eigen values
            double[] eigenvalues = eigenDecomposition(s).getRealEigenvalues();
            Assert.assertArrayEquals(singularValues, eigenvalues, 0.0);

            // check the solution of the linear system Ax = b with b = [1, 0, 0]
            double[] x = svd.getSolver().solve(new ArrayRealVector(new double[]{1.0, 0.0, 0.0}));
            RealVector b = new ArrayRealVector(new double[]{1.0, 0.0, 0.0});
            RealVector expectedX = new SingularValueDecomposition(matrix).solve(b);
            Assert.assertArrayEquals(expectedX.toArray(), x, 0.0);

            // check the condition number and rank
            double conditionNumber = svd.getConditionNumber();
            int rank = svd.getRank();
            double[] expectedSingularValuesSquared =
                new SingularValueDecomposition(matrix).getSingularValuesSquared().getData()[0];
            double expectedConditionNumber = Math.sqrt(Math.abs(Math.pow(expectedSingularValuesSquared[0], 2) *
                Math.pow(expectedSingularValuesSquared[1], 2) * Math.pow(expectedSingularValuesSquared[2], 2)));
            Assert.assertEquals(expectedConditionNumber, conditionNumber, 0.0);
            Assert.assertEquals(3, rank);

        } catch (InvalidMatrixException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test the construction with a singular matrix.
     */
    @Test
    public void testSingularMatrix() {
        // create a singular matrix
        double[][] data = {
            {1.0, 2.0, 0.0},
            {4.0, 5.0, 0.0},
            {7.0, 8.0, 0.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(data, false);

        try {
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);

            // check the rank
            int rank = svd.getRank();
            Assert.assertEquals(2, rank);

            // check all other operations throw an exception
            try {
                svd.getU();
                Assert.fail();
            } catch (InvalidMatrixException e) {
                // expected
            }

            try {
                svd.getS();
                Assert.fail();
            } catch (InvalidMatrixException e) {
                // expected
            }

            try {
                svd.getV();
                Assert.fail();
            } catch (InvalidMatrixException e) {
                // expected
            }

            try {
                svd.getSingularValues();
                Assert.fail();
            } catch (InvalidMatrixException e) {
                // expected
            }

            try {
                svd.getUT();
                Assert.fail();
            } catch (InvalidMatrixException e) {
                // expected
            }

            try {
                svd.getSolver();
                Assert.fail();
            } catch (InvalidMatrixException e) {
                // expected
            }

            try {
                svd.getCovariance(1.0);
                Assert.fail();
            } catch (InvalidMatrixException e) {
                // expected
            }

            try {
                svd.getNorm();
                Assert.fail();
            } catch (InvalidMatrixException e) {
                // expected
            }

            try {
                svd.getConditionNumber();
                Assert.fail();
            } catch (InvalidMatrixException e) {
                // expected
            }

        } catch (InvalidMatrixException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test the construction with a matrix with non-square dimensions.
     */
    @Test
    public void testNonSquareMatrix() {
        // create a non-square matrix
        double[][] data = {
            {1.0, 2.0},
            {3.0, 4.0, 5.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(data, false);

        try {
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);

            // check the rank
            int rank = svd.getRank();
            Assert.assertEquals(1, rank);

            // check all other operations throw an exception
            try {
                svd.getU();
                Assert.fail();
            } catch (InvalidMatrixException e) {
                // expected
            }

            try {
                svd.getS();
                Assert.fail();
            } catch (InvalidMatrixException e) {
                // expected
            }

            try {
                svd.getV();
                Assert.fail();
            } catch (InvalidMatrixException e) {
                // expected
            }

            try {
                svd.getSingularValues();
                Assert.fail();
            } catch (InvalidMatrixException e) {
                // expected
            }

            try {
                svd.getUT();
                Assert.fail();
            } catch (InvalidMatrixException e) {
                // expected
            }

            try {
                svd.getSolver();
                Assert.fail();
            } catch (InvalidMatrixException e) {
                // expected
            }

            try {
                svd.getCovariance(1.0);
                Assert.fail();
            } catch (InvalidMatrixException e) {
                // expected
            }

            try {
                svd.getNorm();
                Assert.fail();
            } catch (InvalidMatrixException e) {
                // expected
            }

            try {
                svd.getConditionNumber();
                Assert.fail();
            } catch (InvalidMatrixException e) {
                // expected
            }

        } catch (InvalidMatrixException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Helper method to compute the eigen values of a matrix.
     * @param matrix the matrix
     * @return the eigen values
     */
    private EigenDecomposition eigenDecomposition(RealMatrix matrix) {
        return new EigenDecompositionImpl(matrix);
    }
}