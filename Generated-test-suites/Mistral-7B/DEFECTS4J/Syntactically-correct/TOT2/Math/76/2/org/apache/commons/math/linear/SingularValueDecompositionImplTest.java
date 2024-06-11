package org.apache.commons.math.linear;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.linear.Solver;
import org.apache.commons.math3.linear.MatrixUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test for SingularValueDecompositionImpl class
 */
public class SingularValueDecompositionImplTest {

    /**
     * Test constructor with valid matrix
     */
    @Test
    public void testValidMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        try {
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
            SingularValueDecomposition svdExpected = new SingularValueDecomposition() {
                @Override
                public RealMatrix getU() {
                    return new Array2DRowRealMatrix(new double[][]{{0.86652753}, {0.45825781}});
                }

                @Override
                public RealMatrix getUT() {
                    return new Array2DRowRealMatrix(new double[][]{{0.86652753}, {0.45825781}});
                }

                @Override
                public RealMatrix getS() {
                    return new Array2DRowRealMatrix(new double[][]{{2.23606798}, {0.0}});
                }

                @Override
                public RealMatrix getV() {
                    return new Array2DRowRealMatrix(new double[][]{{-0.4472136}, {0.89442718}});
                }

                @Override
                public RealMatrix getVT() {
                    return new Array2DRowRealMatrix(new double[][]{{-0.4472136}, {0.89442718}});
                }

                @Override
                public double[] getSingularValues() {
                    return new double[]{2.23606798, 0.0};
                }
            };
            // Test getters
            Assert.assertEquals(svdExpected.getU(), svd.getU());
            Assert.assertEquals(svdExpected.getUT(), svd.getUT());
            Assert.assertEquals(svdExpected.getS(), svd.getS());
            Assert.assertEquals(svdExpected.getV(), svd.getV());
            Assert.assertEquals(svdExpected.getVT(), svd.getVT());
            Assert.assertArrayEquals(svdExpected.getSingularValues(), svd.getSingularValues());
            // Test Solver
            RealVector b = new ArrayRealVector(new double[]{2.0, 2.0});
            DecompositionSolver solver = svd.getSolver();
            RealVector x = solver.solve(b);
            RealVector expectedX = new ArrayRealVector(new double[]{1.16442564, 0.74651826});
            Assert.assertEquals(expectedX, x);
            // Test getRank
            int rank = svd.getRank();
            Assert.assertEquals(rank, 2);
        } catch (InvalidMatrixException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test constructor with invalid matrix
     */
    @Test(expected = InvalidMatrixException.class)
    public void testInvalidMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 1.0}, {2.0, 2.0}});
        new SingularValueDecompositionImpl(matrix);
    }

    /**
     * Test getCovariance with positive minSingularValue
     */
    @Test
    public void testGetCovariancePositive() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        try {
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
            double minSingularValue = 1.0;
            RealMatrix covariance = svd.getCovariance(minSingularValue);
            double[][] covarianceData = covariance.getData();
            double[] expectedCovarianceData = new double[][]{
                    {0.69314718, 0.73439845},
                    {0.73439845, 0.69314718}
            };
            Assert.assertArrayEquals(expectedCovarianceData, covarianceData);
        } catch (InvalidMatrixException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test getCovariance with negative minSingularValue
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetCovarianceNegative() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        try {
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
            double minSingularValue = -1.0;
            svd.getCovariance(minSingularValue);
        } catch (InvalidMatrixException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test getConditionNumber
     */
    @Test
    public void testGetConditionNumber() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        try {
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
            double conditionNumber = svd.getConditionNumber();
            Assert.assertEquals(conditionNumber, 3.0);
        } catch (InvalidMatrixException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test getRank
     */
    @Test
    public void testGetRank() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 3.0, 4.0}});
        try {
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
            int rank = svd.getRank();
            Assert.assertEquals(rank, 3);
        } catch (InvalidMatrixException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test getSolver
     */
    @Test
    public void testGetSolver() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        try {
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
            DecompositionSolver solver = svd.getSolver();
            Solver solverExpected = new Solver(svd.getSingularValues(), svd.getUT(), svd.getV(), true);
            Assert.assertEquals(solverExpected, solver);
        } catch (InvalidMatrixException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test getNorm
     */
    @Test
    public void testGetNorm() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        try {
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
            double norm = svd.getNorm();
            Assert.assertEquals(norm, 3.0);
        } catch (InvalidMatrixException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test Eigenvalue decomposition with small matrix
     */
    @Test
    public void testEigenvalueDecompositionSmallMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 1.0}, {1.0, 1.0}});
        try {
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
            EigenDecomposition eigenDecomposition = svd.getEigenDecomposition();
            double[][] eigenValueData = eigenDecomposition.getData();
            double[] expectedEigenvalueData = new double[][]{
                    {1.41421374, 1.41421374}
            };
            Assert.assertArrayEquals(expectedEigenvalueData, eigenValueData);
        } catch (InvalidMatrixException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test Eigenvalue decomposition with large matrix
     */
    @Test
    public void testEigenvalueDecompositionLargeMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 3.0, 4.0}, {3.0, 4.0, 5.0}});
        try {
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
            EigenDecomposition eigenDecomposition = svd.getEigenDecomposition();
            double[][] eigenValueData = eigenDecomposition.getData();
            double[] expectedEigenvalueData = new double[][]{
                    {12.88937059, 0.0, 0.0},
                    {0.0, 1.61177447, 0.0},
                    {0.0, 0.0, 0.0}
            };
            Assert.assertArrayEquals(expectedEigenvalueData, eigenValueData);
        } catch (InvalidMatrixException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

}