package org.apache.commons.math.linear;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.EigenDecompositionImpl;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.linear.SingularValueDecompositionImpl;
import org.apache.commons.math3.linear.SingularValueDecompositionSolver;
import org.apache.commons.math3.linear.Transformer;
import org.apache.commons.math3.linear.BiDiagonalTransformer;
import org.apache.commons.math3.linear.MatrixUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the {@link SingularValueDecompositionImpl} class.
 */
public class SingularValueDecompositionImplTest {

    private SingularValueDecompositionImpl decomposer;
    private RealMatrix matrix;
    private RealMatrix expectedUT;
    private RealMatrix expectedV;
    private RealMatrix expectedS;
    private RealMatrix expectedSingularValues;

    /**
     * Setup for the tests.
     */
    @Before
    public void setup() {
        double[][] data = { { 3, 2, 1 }, { 2, 2, 1 }, { 1, 1, 2 } };
        matrix = new Array2DRowRealMatrix(data, false);
        expectedUT = new Array2DRowRealMatrix(new double[][] { { 0.57735, 0.57735, 0.57735 },
                                                               { 0.52555, -0.81649, -0.08261 },
                                                               { -0.81649, -0.52555, 0.57735 } }, false);
        expectedV = new Array2DRowRealMatrix(new double[][] { { 0.44721, 0.73437, 0.63246 },
                                                             { -0.7568, 0.08261, 0.61233 },
                                                             { 0.40825, 0.61233, 0.71266 } }, false);
        expectedS = MatrixUtils.createRealDiagonalMatrix(new double[] { 3, 1.5811, 0 });
        expectedSingularValues = new Array2DRowRealMatrix(new double[][] { { 3.0, 1.5811, 0.0 },
                                                                          { 1.5811, 0.7905556, 0.0 },
                                                                          { 0.0, 0.0, 0.0 } }, false);
        decomposer = new SingularValueDecompositionImpl(matrix);
    }

    /**
     * Test the {@link SingularValueDecompositionImpl#getU()} method.
     */
    @Test
    public void testGetU() {
        RealMatrix actualUT = decomposer.getU();
        Assert.assertTrue("UT should be equal to expected UT", expectedUT.equals(actualUT));
    }

    /**
     * Test the {@link SingularValueDecompositionImpl#getUT()} method.
     */
    @Test
    public void testGetUT() {
        RealMatrix actualUT = decomposer.getUT();
        Assert.assertTrue("UT should be equal to expected UT transposed",
                         expectedUT.transpose().equals(actualUT));
    }

    /**
     * Test the {@link SingularValueDecompositionImpl#getS()} method.
     */
    @Test
    public void testGetS() {
        RealMatrix actualS = decomposer.getS();
        Assert.assertTrue("S should be equal to expected S", expectedS.equals(actualS));
    }

    /**
     * Test the {@link SingularValueDecompositionImpl#getSingularValues()} method.
     */
    @Test
    public void testGetSingularValues() {
        double[] actualSingularValues = decomposer.getSingularValues();
        Assert.assertArrayEquals("Singular values should be equal to expected singular values",
                                expectedSingularValues.getData(), actualSingularValues, 0.001);
    }

    /**
     * Test the {@link SingularValueDecompositionImpl#getV()} method.
     */
    @Test
    public void testGetV() {
        RealMatrix actualV = decomposer.getV();
        Assert.assertTrue("V should be equal to expected V", expectedV.equals(actualV));
    }

    /**
     * Test the {@link SingularValueDecompositionImpl#getVT()} method.
     */
    @Test
    public void testGetVT() {
        RealMatrix actualVT = decomposer.getVT();
        Assert.assertTrue("VT should be equal to expected VT", expectedV.transpose().equals(actualVT));
    }

    /**
     * Test the {@link SingularValueDecompositionImpl#getCovariance(double)} method.
     */
    @Test
    public void testCovariance() {
        RealMatrix actualCovariance = decomposer.getCovariance(1);
        RealMatrix expectedCovariance = new Array2DRowRealMatrix(new double[][] { { 1.0, 0.0, 0.0 },
                                                                               { 0.0, 1.0, 0.0 },
                                                                               { 0.0, 0.0, 1.0 } }, false);
        Assert.assertTrue("Covariance should be equal to expected covariance",
                         expectedCovariance.equals(actualCovariance));
    }

    /**
     * Test the {@link SingularValueDecompositionImpl#getNorm()} method.
     */
    @Test
    public void testGetNorm() {
        Assert.assertEquals("Norm should be equal to expected norm", 3.0, decomposer.getNorm(), 0.001);
    }

    /**
     * Test the {@link SingularValueDecompositionImpl#getConditionNumber()} method.
     */
    @Test
    public void testGetConditionNumber() {
        Assert.assertEquals("Condition number should be equal to expected condition number",
                           3.0 / 0.0, decomposer.getConditionNumber(), 0.001);
    }

    /**
     * Test the {@link SingularValueDecompositionImpl#getRank()} method.
     */
    @Test
    public void testGetRank() {
        Assert.assertEquals("Rank should be equal to expected rank", 3, decomposer.getRank());
    }

    /**
     * Test the {@link SingularValueDecompositionImpl#getSolver()} method.
     */
    @Test
    public void testSolver() {
        RealVector b = new ArrayRealVector(new double[] { 1, 1, 1 });
        RealVector x = decomposer.getSolver().solve(b);
        Assert.assertArrayEquals("Solution should be equal to expected solution",
                                new double[] { 0.6, 0.6, 0.6 }, x.toArray(), 0.001);
    }

    /**
     * Test the {@link SingularValueDecompositionImpl#getSolver()} method with a non-square matrix.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testSolverWithNonSquareMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] { { 3, 2 }, { 1, 1 } }, false);
        SingularValueDecompositionImpl decomposer = new SingularValueDecompositionImpl(matrix);
        DecompositionSolver solver = decomposer.getSolver();
        solver.solve(new Array2DRowRealMatrix(new double[][] { { 1, 1, 1 } }, false));
    }

}