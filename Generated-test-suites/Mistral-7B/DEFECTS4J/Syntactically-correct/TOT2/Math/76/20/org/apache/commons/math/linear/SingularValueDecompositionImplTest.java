package org.apache.commons.math.linear;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SingularValueDecompositionImplTest {

    private RealMatrix matrix;
    private SingularValueDecompositionImpl singularValueDecomposition;

    @Before
    public void setUp() {
        double[][] initialData = {
                {1.0, 1.0, 0.0},
                {1.0, 1.0, 1.0},
                {1.0, 0.0, 1.0}
        };
        matrix = new Array2DRowRealMatrix(initialData, false);
        try {
            singularValueDecomposition = new SingularValueDecompositionImpl(matrix);
        } catch (InvalidMatrixException e) {
            Assert.fail("Decomposition should be possible.");
        }
    }

    /**
     * Test getU() method.
     */
    @Test
    public void testGetU() {
        try {
            RealMatrix expected = new Array2DRowRealMatrix(
                    new double[][]{{-0.57882636, -0.57534255, -0.57358136},
                                    {-0.57358136, -0.73581353, 0.57358136},
                                    {-0.57534255, 0.57358136, -0.57534255}},
                    false);
            RealMatrix result = singularValueDecomposition.getU();
            Assert.assertEquals(ExpectedArrays.deepEquals(expected.getData()),
                                ExpectedArrays.deepEquals(result.getData()));
        } catch (InvalidMatrixException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test getUT() method.
     */
    @Test
    public void testGetUT() {
        try {
            RealMatrix expected = new Array2DRowRealMatrix(
                    new double[][]{{-0.57882636, -0.57358136, -0.57534255},
                                    {-0.57358136, 0.57358136, 0.57534255},
                                    {-0.57534255, 0.57534255, -0.57358136}},
                    false);
            RealMatrix result = singularValueDecomposition.getUT();
            Assert.assertEquals(ExpectedArrays.deepEquals(expected.getData()),
                                ExpectedArrays.deepEquals(result.getData()));
        } catch (InvalidMatrixException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test getS() method.
     */
    @Test
    public void testGetS() {
        try {
            double[] expected = {1.41421356, 0.70710678};
            double[] result = singularValueDecomposition.getSingularValues();
            Assert.assertArrayEquals(expected, result);
        } catch (InvalidMatrixException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test getV() method.
     */
    @Test
    public void testGetV() {
        try {
            RealMatrix expected = new Array2DRowRealMatrix(
                    new double[][]{{0.40824829, 0.81649658, 0.40824829},
                                    {-0.81649658, 0.40824829, 0.81649658},
                                    {-0.40824829, 0.81649658, -0.40824829}},
                    false);
            RealMatrix result = singularValueDecomposition.getV();
            Assert.assertEquals(ExpectedArrays.deepEquals(expected.getData()),
                                ExpectedArrays.deepEquals(result.getData()));
        } catch (InvalidMatrixException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test getVT() method.
     */
    @Test
    public void testGetVT() {
        try {
            RealMatrix expected = new Array2DRowRealMatrix(
                    new double[][]{{0.40824829, -0.81649658, -0.40824829},
                                    {0.81649658, 0.40824829, 0.81649658},
                                    {-0.40824829, 0.81649658, -0.40824829}},
                    false);
            RealMatrix result = singularValueDecomposition.getVT();
            Assert.assertEquals(ExpectedArrays.deepEquals(expected.getData()),
                                ExpectedArrays.deepEquals(result.getData()));
        } catch (InvalidMatrixException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test getCovariance() method.
     */
    @Test
    public void testGetCovariance() {
        try {
            double[][] expectedData = {{0.14142136, 0.0}, {0.0, 0.1}};
            RealMatrix expected = new Array2DRowRealMatrix(expectedData, false);
            RealMatrix result = singularValueDecomposition.getCovariance(0.0);
            Assert.assertEquals(ExpectedArrays.deepEquals(expected.getData()),
                                ExpectedArrays.deepEquals(result.getData()));
        } catch (InvalidMatrixException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test getNorm() method.
     */
    @Test
    public void testGetNorm() {
        try {
            double expected = 1.41421356;
            double result = singularValueDecomposition.getNorm();
            Assert.assertEquals(expected, result, 1e-9);
        } catch (InvalidMatrixException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test getConditionNumber() method.
     */
    @Test
    public void testGetConditionNumber() {
        try {
            double expected = 3.0;
            double result = singularValueDecomposition.getConditionNumber();
            Assert.assertEquals(expected, result, 1e-9);
        } catch (InvalidMatrixException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test getRank() method.
     */
    @Test
    public void testGetRank() {
        try {
            int expected = 3;
            int result = singularValueDecomposition.getRank();
            Assert.assertEquals(expected, result);
        } catch (IllegalStateException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test Solver.
     */
    @Test
    public void testSolver() throws Exception {
        try {
            double[][] bData = {{2.0, 1.0}, {1.0, 2.0}};
            RealMatrix b = new Array2DRowRealMatrix(bData, false);
            RealVector x = singularValueDecomposition.getSolver().solve(b);
            double[] expected = {0.57358136, 0.57358136};
            double[] result = x.toArray();
            Assert.assertArrayEquals(expected, result);
        } catch (IllegalArgumentException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test InvalidMatrixException.
     */
    @Test(expected = InvalidMatrixException.class)
    public void testInvalidMatrixException() throws Exception {
        try {
            RealMatrix invalidMatrix = new Array2DRowRealMatrix(
                    new double[][]{{1.0, 1.0}, {1.0, 1.0}, {1.0, 1.0}, {1.0, 1.0}},
                    false);
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(invalidMatrix);
        } catch (MathRuntimeException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test EigenDecompositionImpl exception.
     */
    @Test(expected = org.apache.commons.math3.linear.EigenDecomposition.SingularValueException.class)
    public void testEigenDecompositionException() throws Exception {
        double[][] initialData = {
                {1.0, 1.0, 0.0},
                {1.0, 1.0, 1.0},
                {1.0, 0.0, 0.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(initialData, false);
        SingularValueDecompositionImpl singularValueDecomposition;

        try {
            singularValueDecomposition = new SingularValueDecompositionImpl(matrix);
        } catch (InvalidMatrixException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }

        try {
            SingularValueDecompositionImpl.EigenDecomposition eigenDecomposition =
                    new SingularValueDecompositionImpl.EigenDecompositionImpl(
                            singularValueDecomposition.mainTridiagonal,
                            singularValueDecomposition.secondaryTridiagonal,
                            MathUtils.SAFE_MIN);
            eigenDecomposition.getRealEigenvalues();
        } catch (org.apache.commons.math3.linear.EigenDecomposition.SingularValueException e) {
            // expected
        }
    }

}