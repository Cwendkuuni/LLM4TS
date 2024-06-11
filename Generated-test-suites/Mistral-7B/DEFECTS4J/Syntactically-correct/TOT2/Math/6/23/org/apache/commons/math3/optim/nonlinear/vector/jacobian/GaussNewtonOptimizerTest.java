import org.apache.commons.math3.optim.PointsValuePair;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.SimpleValuesChecker;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.SingularMatrixException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.optimization.general.GaussNewtonOptimizer;
import org.apache.commons.math3.optimization.general.AbstractLeastSquaresOptimizer;
import org.apache.commons.math3.optimization.initializers.ArrayInitializer;
import org.apache.commons.math3.optimization.convergers.IterativeConverger;
import org.apache.commons.math3.optimization.convergers.ConvergenceChecker;

public class GaussNewtonOptimizerTest {

    // Test for constructor with default settings
    @Test
    public void testGaussNewtonOptimizerDefaults() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        assertNotNull(optimizer);
        assertTrue(optimizer.isUseLU());
        ConvergenceChecker<? extends PointsValuePair> checker = optimizer.getConvergenceChecker();
        assertNotNull(checker);
        assertTrue(checker instanceof SimpleValuesChecker);
    }

    // Test for constructor with custom settings
    @Test
    public void testGaussNewtonOptimizerCustomSettings() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false, null);
        assertNotNull(optimizer);
        assertFalse(optimizer.isUseLU());
        ConvergenceChecker<? extends PointsValuePair> checker = optimizer.getConvergenceChecker();
        assertNull(checker);
    }

    // Test for optimize method with convergence checker
    @Test
    public void testGaussNewtonOptimizerConvergenceChecker() {
        double[] targetValues = {1.0, 2.0};
        RealMatrix weightMatrix = MatrixUtils.dense2DIdentity(targetValues.length);
        RealVector initialPoint = new ArrayRealVector(new double[]{0.0, 0.0});
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleValuesChecker(1e-4));
        PointsValuePair solution = optimizer.optimize(new ArrayInitializer(initialPoint), new MaxEval(100));
        assertArrayEquals(new double[]{1.0, 1.0}, solution.getPoint().toArray(), 1e-4);
        assertEquals(0.0, optimizer.getCost(), 1e-4);
    }

    // Test for optimize method without convergence checker
    @Test(expected = NullArgumentException.class)
    public void testGaussNewtonOptimizerNoConvergenceChecker() {
        double[] targetValues = {1.0, 2.0};
        RealMatrix weightMatrix = MatrixUtils.dense2DIdentity(targetValues.length);
        RealVector initialPoint = new ArrayRealVector(new double[]{0.0, 0.0});
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        PointsValuePair solution = optimizer.optimize(new ArrayInitializer(initialPoint), new MaxEval(100));
    }

    // Test for optimize method with invalid input
    @Test(expected = NullArgumentException.class)
    public void testGaussNewtonOptimizerNullArguments() {
        GaussNewterOptimizer optimizer = new GaussNewtonOptimizer();
        optimizer.optimize(null, new MaxEval(100));
    }

    // Test for optimize method with Singular MatrixException
    @Test(expected = ConvergenceException.class)
    public void testGaussNewtonOptimizerSingularMatrixException() {
        double[] targetValues = {1.0, 2.0};
        RealMatrix weightMatrix = MatrixUtils.dense2DIdentity(targetValues.length);
        RealVector initialPoint = new ArrayRealVector(new double[]{0.0, 0.0});
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleValuesChecker(1e-4));
        weightMatrix.setEntry(1, 1, 0);
        PointsValuePair solution = optimizer.optimize(new ArrayInitializer(initialPoint), new MaxEval(100));
    }

    // Test for optimize method with MaxEvalException
    @Test(expected = MaxEvalException.class)
    public void testGaussNewtonOptimizerMaxEvalException() {
        double[] targetValues = {1.0, 2.0};
        RealMatrix weightMatrix = MatrixUtils.dense2DIdentity(targetValues.length);
        RealVector initialPoint = new ArrayRealVector(new double[]{0.0, 0.0});
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleValuesChecker(1e-4));
        PointsValuePair solution = optimizer.optimize(new ArrayInitializer(initialPoint), new MaxEval(0));
    }
}