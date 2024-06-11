package org.apache.commons.math3.optimization.general;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.QRDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SingularMatrixException;
import org.apache.commons.math3.optim.AbstractOptimizationTest;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValuesChecker;
import org.apache.commons.math3.optim.UnboundedDomainException;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for {@link GaussNewtonOptimizer}.
 */
public class GaussNewtonOptimizerTest extends AbstractOptimizationTest {

    /**
     * Test optimization with simple data and LU decomposition.
     */
    @Test
    public void testGaussNewtonOptimizerLU() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleValuesChecker());
        double[] targetValues = {3.0, 2.0};
        double[] xExpected = {1.0, 1.0};
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});
        PointValuePair solution = optimizer.optimize(targetValues, new IdentityInitialGuess(2), weightMatrix);
        Assert.assertArrayEquals(xExpected, solution.getPoint(), 1e-12);
    }

    /**
     * Test optimization with simple data and QR decomposition.
     */
    @Test
    public void testGaussNewtonOptimizerQR() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false, new SimpleValuesChecker());
        double[] targetValues = {3.0, 2.0};
        double[] xExpected = {1.0, 1.0};
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});
        PointValuePair solution = optimizer.optimize(targetValues, new IdentityInitialGuess(2), weightMatrix);
        Assert.assertArrayEquals(xExpected, solution.getPoint(), 1e-12);
    }

    /**
     * Test optimization with nonlinear data and LU decomposition.
     */
    @Test
    public void testGaussNewtonOptimizerLUNonlinear() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleValuesChecker());
        double[] targetValues = {Math.exp(1.0), Math.exp(2.0)};
        double[] xExpected = {0.5, 1.5};
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});
        PointValuePair solution = optimizer.optimize(targetValues, new IdentityInitialGuess(2), weightMatrix);
        Assert.assertArrayEquals(xExpected, solution.getPoint(), 1e-3);
    }

    /**
     * Test optimization with nonlinear data and QR decomposition.
     */
    @Test
    public void testGaussNewtonOptimizerQRNonlinear() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false, new SimpleValuesChecker());
        double[] targetValues = {Math.exp(1.0), Math.exp(2.0)};
        double[] xExpected = {0.5, 1.5};
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});
        PointValuePair solution = optimizer.optimize(targetValues, new IdentityInitialGuess(2), weightMatrix);
        Assert.assertArrayEquals(xExpected, solution.getPoint(), 1e-3);
    }

    /**
     * Test optimization with empty target values.
     */
    @Test(expected = NullArgumentException.class)
    public void testGaussNewtonOptimizerEmptyTarget() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleValuesChecker());
        optimizer.optimize(new double[0], new IdentityInitialGuess(2), new BlockRealMatrix(new double[0][0]));
    }

    /**
     * Test optimization with null target values.
     */
    @Test(expected = NullArgumentException.class)
    public void testGaussNewtonOptimizerNullTarget() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleValuesChecker());
        optimizer.optimize(null, new IdentityInitialGuess(2), new BlockRealMatrix(new double[0][0]));
    }

    /**
     * Test optimization with null weight matrix.
     */
    @Test(expected = NullArgumentException.class)
    public void testGaussNewtonOptimizerNullWeight() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleValuesChecker());
        optimizer.optimize(new double[]{}, new IdentityInitialGuess(2), null);
    }

    /**
     * Test optimization with complex data.
     */
    @Test(expected = UnboundedDomainException.class)
    public void testGaussNewtonOptimizerComplexData() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleValuesChecker());
        double[] targetValues = {1.0, Math.complex(0.0, 1.0)};
        double[] xExpected = {1.0, 1.0};
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});
        optimizer.optimize(targetValues, new IdentityInitialGuess(2), weightMatrix);
    }

    /**
     * Test optimization with inconsistent dimensions.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGaussNewtonOptimizerInconsistentDimensions() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleValuesChecker());
        double[] targetValues = {1.0};
        double[] xExpected = {1.0, 1.0};
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}, {1.0, 2.0}});
        optimizer.optimize(targetValues, new IdentityInitialGuess(2), weightMatrix);
    }
}