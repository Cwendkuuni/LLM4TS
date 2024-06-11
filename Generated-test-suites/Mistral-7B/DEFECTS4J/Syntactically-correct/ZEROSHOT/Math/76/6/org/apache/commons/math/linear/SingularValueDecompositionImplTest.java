package org.apache.commons.math.linear;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.special.EigenDecomposition;
import org.apache.commons.math3.special.SingularValueDecomposition;

import org.junit.Test;

public class SingularValueDecompositionImplTest {

    /**
     * Test the constructor with a valid matrix.
     * @throws Exception if an error occurs
     */
    @Test
    public void testConstructorValidMatrix() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        SingularValueDecompositionImpl singularValueDecomposition =
            new SingularValueDecompositionImpl(matrix);
        assertNotNull(singularValueDecomposition.getU());
        assertNotNull(singularValueDecomposition.getS());
        assertNotNull(singularValueDecomposition.getV());
        assertNotNull(singularValueDecomposition.getVt());

        assertEquals(2, singularValueDecomposition.getRank());
    }

    /**
     * Test the constructor with a matrix with singular values equal to zero.
     * @throws Exception if an error occurs
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorSingularValuesZero() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 0.0}});
        new SingularValueDecompositionImpl(matrix);
    }

    /**
     * Test the constructor with a matrix with negative singular values.
     * @throws Exception if an error occurs
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorSingularValuesNegative() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, -4.0}});
        new SingularValueDecompositionImpl(matrix);
    }

    /**
     * Test the getSolver method.
     * @throws Exception if an error occurs
     */
    @Test
    public void testGetSolver() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        SingularValueDecompositionImpl singularValueDecomposition =
            new SingularValueDecompositionImpl(matrix);
        DecompositionSolver solver = singularValueDecomposition.getSolver();
        RealVector vector = new ArrayRealVector(new double[]{1.0, 1.0});
        double[] result = solver.solve(vector.toArray());
        assertArrayEquals(new double[]{1.13247181, 0.44834185}, result, 0.001);
        assertTrue(solver.isNonSingular());
    }

    /**
     * Test the getSolver method with a singular matrix.
     * @throws Exception if an error occurs
     */
    @Test
    public void testGetSolverSingularMatrix() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 0.0}});
        SingularValueDecompositionImpl singularValueDecomposition =
            new SingularValueDecompositionImpl(matrix);
        DecompositionSolver solver = singularValueDecomposition.getSolver();
        RealVector vector = new ArrayRealVector(new double[]{1.0, 1.0});
        double[] result = solver.solve(vector.toArray());
        assertArrayEquals(new double[]{1.0, 1.0}, result, 0.001);
        assertFalse(solver.isNonSingular());
    }

    /**
     * Test the getSolver method with a matrix that has only one non-zero singular value.
     * @throws Exception if an error occurs
     */
    @Test
    public void testGetSolverOneNonZeroSingularValue() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});
        SingularValueDecompositionImpl singularValueDecomposition =
            new SingularValueDecompositionImpl(matrix);
        DecompositionSolver solver = singularValueDecomposition.getSolver();
        RealVector vector = new ArrayRealVector(new double[]{1.0, 1.0});
        double[] result = solver.solve(vector.toArray());
        assertArrayEquals(new double[]{1.0, 1.0}, result, 0.001);
        assertTrue(solver.isNonSingular());
    }

    /**
     * Test the getCovariance method with a valid matrix.
     * @throws Exception if an error occurs
     */
    @Test
    public void testGetCovarianceValidMatrix() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        SingularValueDecompositionImpl singularValueDecomposition =
            new SingularValueDecompositionImpl(matrix, 1);
        RealMatrix result = singularValueDecomposition.getCovariance(1.0e-10);
        SingularValueDecomposition svd = new SingularValueDecomposition(matrix);
        RealMatrix expected = svd.getMatrixA().transpose().multiply(svd.getMatrixV());
        assertEquals(expected.getEntry(0, 0), result.getEntry(0, 0), 1e-10);
        assertEquals(expected.getEntry(0, 1), result.getEntry(0, 1), 1e-10);
        assertEquals(expected.getEntry(1, 0), result.getEntry(1, 0), 1e-10);
        assertEquals(expected.getEntry(1, 1), result.getEntry(1, 1), 1e-10);
    }

    /**
     * Test the getCovariance method with a singular matrix.
     * @throws Exception if an error occurs
     */
    @Test
    public void testGetCovarianceSingularMatrix() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 0.0}});
        SingularValueDecompositionImpl singularValueDecomposition =
            new SingularValueDecompositionImpl(matrix, 1);
        RealMatrix result = singularValueDecomposition.getCovariance(1.0e-10);
        SingularValueDecomposition svd = new SingularValueDecomposition(matrix);
        RealMatrix expected = svd.getMatrixV().transpose().multiply(svd.getMatrixV());
        assertEquals(expected.getEntry(0, 0), result.getEntry(0, 0), 1e-10);
        assertEquals(expected.getEntry(1, 1), result.getEntry(1, 1), 1e-10);
    }

    /**
     * Test the getCovariance method with a matrix that has only one non-zero singular value.
     * @throws Exception if an error occurs
     */
    @Test
    public void testGetCovarianceOneNonZeroSingularValue() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});
        SingularValueDecompositionImpl singularValueDecomposition =
            new SingularValueDecompositionImpl(matrix, 1);
        RealMatrix result = singularValueDecomposition.getCovariance(1.0e-10);
        SingularValueDecomposition svd = new SingularValueDecomposition(matrix);
        RealMatrix expected = svd.getMatrixV().transpose().multiply(svd.getMatrixV());
        assertEquals(expected.getEntry(0, 0), result.getEntry(0, 0), 1e-10);
        assertEquals(expected.getEntry(1, 1), result.getEntry(1, 1), 1e-10);
    }

    /**
     * Test the getNorm method with a valid matrix.
     * @throws Exception if an error occurs
     */
    @Test
    public void testGetNormValidMatrix() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        SingularValueDecompositionImpl singularValueDecomposition =
            new SingularValueDecompositionImpl(matrix);
        assertEquals(5.196152443339453, singularValueDecomposition.getNorm(), 1e-10);
    }

    /**
     * Test the getNorm method with a singular matrix.
     * @throws Exception if an error occurs
     */
    @Test
    public void testGetNormSingularMatrix() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 0.0}});
        SingularValueDecompositionImpl singularValueDecomposition =
            new SingularValueDecompositionImpl(matrix);
        assertEquals(0.0, singularValueDecomposition.getNorm(), 1e-10);
    }

    /**
     * Test the getConditionNumber method with a valid matrix.
     * @throws Exception if an error occurs
     */
    @Test
    public void testGetConditionNumberValidMatrix() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        SingularValueDecompositionImpl singularValueDecomposition =
            new SingularValueDecompositionImpl(matrix);
        assertEquals(12.890579586972156, singularValueDecomposition.getConditionNumber(), 1e-10);
    }

    /**
     * Test the getConditionNumber method with a singular matrix.
     * @throws Exception if an error occurs
     */
    @Test
    public void testGetConditionNumberSingularMatrix() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 0.0}});
        SingularValueDecompositionImpl singularValueDecomposition =
            new SingularValueDecompositionImpl(matrix);
        assertEquals(0.0, singularValueDecomposition.getConditionNumber(), 1e-10);
    }

    /**
     * Test the getRank method with a valid matrix.
     * @throws Exception if an error occurs
     */
    @Test
    public void testGetRankValidMatrix() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        SingularValueDecompositionImpl singularValueDecomposition =
            new SingularValueDecompositionImpl(matrix);
        assertEquals(2, singularValueDecomposition.getRank());
    }

    /**
     * Test the getRank method with a singular matrix.
     * @throws Exception if an error occurs
     */
    @Test
    public void testGetRankSingularMatrix() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 0.0}});
        SingularValueDecompositionImpl singularValueDecomposition =
            new SingularValueDecompositionImpl(matrix);
        assertEquals(0, singularValueDecomposition.getRank());
    }

}