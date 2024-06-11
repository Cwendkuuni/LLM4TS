package org.apache.commons.math.linear;

import org.apache.commons.math3.exception.SingularValueDecompositionException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test for {@link SingularValueDecompositionImpl} class.
 */
public class SingularValueDecompositionImplTest {

    /**
     * Test the SingularValueDecompositionImpl constructor with an identity matrix.
     * @throws SingularValueDecompositionException if the construction fails
     */
    @Test
    public void testIdentityMatrix()
        throws SingularValueDecompositionException {
        final RealMatrix matrix = MatrixUtils.createIdentityMatrix(3);
        final SingularValueDecomposition decomposition =
            new SingularValueDecompositionImpl(matrix);
        Assert.assertArrayEquals(new double[] { 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0 },
                                 decomposition.getSingularValues(), 0.0);
        Assert.assertEquals(matrix, decomposition.getU());
        Assert.assertEquals(MatrixUtils.createIdentityMatrix(3), decomposition.getV());
        Assert.assertEquals(MatrixUtils.createIdentityMatrix(3), decomposition.getVT());
        Assert.assertEquals(MatrixUtils.createIdentityMatrix(3), decomposition.getS());
    }

    /**
     * Test the SingularValueDecompositionImpl constructor with a singular matrix.
     * @throws SingularValueDecompositionException if the construction fails
     */
    @Test
    public void testSingularMatrix()
        throws SingularValueDecompositionException {
        final RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][] { { 0.0, 1.0 }, { 1.0, 0.0 } });
        final SingularValueDecomposition decomposition =
            new SingularValueDecompositionImpl(matrix);
        Assert.assertArrayEquals(new double[] { 1.0, 1.0 }, decomposition.getSingularValues(), 0.0);
        Assert.assertEquals(MatrixUtils.createRealMatrix(new double[][] { { -0.577350269189626, 0.577350269189626 },
                                                                         { 0.577350269189626, -0.577350269189626 } }),
                             decomposition.getU());
        Assert.assertEquals(MatrixUtils.createRealMatrix(new double[][] { { -0.577350269189626, 0.577350269189626 },
                                                                           { 0.577350269189626, -0.577350269189626 } }),
                             decomposition.getVT());
        Assert.assertEquals(MatrixUtils.createRealMatrix(new double[][] { { 1.0, 0.0 }, { 0.0, 1.0 } }),
                             decomposition.getV());
        Assert.assertEquals(MatrixUtils.createRealDiagonalMatrix(new double[] { 1.0, 1.0 }), decomposition.getS());
    }

    /**
     * Test the SingularValueDecompositionImpl constructor with a random matrix.
     * @throws SingularValueDecompositionException if the construction fails
     */
    @Test
    public void testRandomMatrix()
        throws SingularValueDecompositionException {
        final RealMatrix matrix = new Array2DRowRealMatrix(new double[][] { { 0.535328831, 0.33404733, 0.15386245, 0.65933816 },
                                                                         { 0.14625966, 0.82395891, 0.87077036, 0.16194483 },
                                                                         { 0.61450952, -1.04129558, 0.35131782, -0.54873775 },
                                                                         { 1.06056611, 0.77180733, -0.30076424, -0.34655528 } });
        final SingularValueDecomposition decomposition =
            new SingularValueDecompositionImpl(matrix);
        final double[] expectedSingularValues = { 2.128442697264829, 1.32042470418834, 0.0 };
        Assert.assertArrayEquals(expectedSingularValues, decomposition.getSingularValues(), 0.001);
        Assert.assertEquals(MatrixUtils.createRealMatrix(new double[][] { { 0.44182321, 0.80625958, -0.50525337, -0.48192223 },
                                                                         { -0.80625958, -0.44182321, 0.48192223, 0.50525337 },
                                                                         { -0.50525337, 0.48192223, 0.73115257, -0.01243125 },
                                                                         { -0.48192223, 0.50525337, -0.01243125, -0.73115257 } }),
                             decomposition.getU());
        Assert.assertEquals(MatrixUtils.createRealMatrix(new double[][] { { -0.74678698, -0.43957206, 0.43957206, 0.74678698 },
                                                                           { 0.43957206, 0.74678698, -0.74678698, 0.43957206 },
                                                                           { -0.75741954, -0.08163245, 0.08163245, 0.75741954 },
                                                                           { 0.08163245, -0.75741954, 0.75741954, -0.08163245 } }),
                             decomposition.getVT());
        Assert.assertEquals(MatrixUtils.createRealMatrix(new double[][] { { 0.53532883, 0.14625966, 0.61450952, 1.06056611 },
                                                                           { 0.33404733, 0.82395891, -1.04129558, 0.77180733 },
                                                                           { 0.15386245, 0.87077036, 0.35131782, -0.30076424 },
                                                                           { 0.65933816, 0.16194483, -0.54873775, -0.34655528 } }),
                             decomposition.getV());
        Assert.assertEquals(MatrixUtils.createRealDiagonalMatrix(expectedSingularValues), decomposition.getS());
    }

    /**
     * Test the getConditionNumber method with an identity matrix.
     */
    @Test
    public void testIdentityMatrixConditionNumber() {
        final RealMatrix matrix = MatrixUtils.createIdentityMatrix(3);
        try {
            final SingularValueDecomposition decomposition =
                new SingularValueDecompositionImpl(matrix);
            Assert.assertEquals(1.0, decomposition.getConditionNumber(), 0.0);
        } catch (final SingularValueDecompositionException e) {
            Assert.fail("Exception should not be thrown: " + e.getMessage());
        }
    }

    /**
     * Test the getConditionNumber method with a singular matrix.
     */
    @Test
    public void testSingularMatrixConditionNumber() {
        final RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][] { { 0.0, 1.0 }, { 1.0, 0.0 } });
        try {
            final SingularValueDecomposition decomposition =
                new SingularValueDecompositionImpl(matrix);
            Assert.assertEquals(Infinity.doubleValue(), decomposition.getConditionNumber(), 0.0);
        } catch (final SingularValueDecompositionException e) {
            Assert.fail("Exception should not be thrown: " + e.getMessage());
        }
    }

    /**
     * Test the getConditionNumber method with a random matrix.
     */
    @Test
    public void testRandomMatrixConditionNumber() {
        final RealMatrix matrix = new Array2DRowRealMatrix(new double[][] { { 0.53532883, 0.33404733, 0.15386245, 0.65933816 },
                                                                         { 0.14625966, 0.82395891, 0.87077036, 0.16194483 },
                                                                         { 0.61450952, -1.04129558, 0.35131782, -0.54873775 },
                                                                         { 1.06056611, 0.77180733, -0.30076424, -0.34655528 } });
        try {
            final SingularValueDecomposition decomposition =
                new SingularValueDecompositionImpl(matrix);
            Assert.assertEquals(4.367964218004552, decomposition.getConditionNumber(), 0.01);
        } catch (final SingularValueDecompositionException e) {
            Assert.fail("Exception should not be thrown: " + e.getMessage());
        }
    }

    /**
     * Test the getSolver method with a random matrix.
     */
    @Test
    public void testRandomMatrixSolver() {
        final RealMatrix matrix = new Array2DRowRealMatrix(new double[][] { { 0.53532883, 0.33404733, 0.15386245, 0.65933816 },
                                                                         { 0.14625966, 0.82395891, 0.87077036, 0.16194483 },
                                                                         { 0.61450952, -1.04129558, 0.35131782, -0.54873775 },
                                                                         { 1.06056611, 0.77180733, -0.30076424, -0.34655528 } });
        try {
            final SingularValueDecomposition decomposition =
                new SingularValueDecompositionImpl(matrix);
            final DecompositionSolver solver = decomposition.getSolver();
            final RealMatrix b = new Array2DRowRealMatrix(new double[][] { { 1.0, 2.0, 3.0, 4.0 }, { 5.0, 6.0, 7.0, 8.0 } });
            RealVector x = solver.solve(new Array2DRowRealMatrix(new double[][] { { 1.0, 5.0 }, { 2.0, 6.0 } }));
            RealVector expectedX = new Array2DRowRealVector(new double[] { -0.02848850821869613, 0.16650712825354363 });
            Assert.assertEquals(expectedX, x, 0.001);
        } catch (final SingularValueDecompositionException e) {
            Assert.fail("Exception should not be thrown: " + e.getMessage());
        }
    }

}