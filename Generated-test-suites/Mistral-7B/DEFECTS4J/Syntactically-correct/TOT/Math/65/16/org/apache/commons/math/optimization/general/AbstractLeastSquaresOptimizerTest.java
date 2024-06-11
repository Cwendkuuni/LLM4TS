package org.apache.commons.math.optimization.general;

import org.apache.commons.math3.exception.MaxEvaluationsExceededException;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.PrecisionExceededException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealArray2DSolution;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.NonlinearConstraint;
import org.apache.commons.math3.optim.nonlinear.SimpleGoalType;
import org.apache.commons.math3.optim.nonlinear.convergence.IterationNumber;
import org.apache.commons.math3.optim.nonlinear.gradient.Gradient;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for AbstractLeastSquaresOptimizer class
 */
public class AbstractLeastSquaresOptimizerTest {
    protected AbstractLeastSquaresOptimizer optimizer;

    @Before
    public void setUp() {
        optimizer = new LevenbergMarquardtOptimizer();
    }

    /**
     * Test optimization with a simple function
     * @throws Exception if an error occurs during optimization
     */
    @Test
    public void testSimpleFunction() throws Exception {
        final double[] x = {1.0, 2.0};
        final double[] y = {3.0, 4.0};
        final double[] startPoint = {1.5, 2.5};
        final RealVector target = new Array2DRowRealMatrix(y).transpose();
        final RealMatrix weightMatrix = new Array2DRowRealMatrix(new double[][]{{0.1, 0}, {0, 0.1}});
        final RealVector initialGuess = new Array2DRowRealMatrix(x).transpose();
        final GoalType goalType = new SimpleGoalType(GoalType.MINIMIZE, 0);

        PointValuePair result = optimizer.optimize(new SumFunction(x), target.toVector(),
                weightMatrix.getColumnVector(0).toArray(), startPoint, null,
                new SimpleBounds(new Double[]{-10.0, -10.0}, new Double[]{10.0, 10.0}),
                new MaxIterationsExceededException(1), new MaxEval(10), goalType,
                new InitialGuess(initialGuess), new IterationNumber(10), new Gradient(), null);

        Assert.assertTrue("Optimization failed", result != null);
        Assert.assertArrayEquals("Incorrect results", x, result.getPoint(), 1e-6);
        Assert.assertEquals("Incorrect objective function value", 0.0, result.getValue(), 1e-6);
    }

    /**
     * Test optimization with a nonlinear constraint
     * @throws Exception if an error occurs during optimization
     */
    @Test
    public void testNonlinearConstraint() throws Exception {
        final double[] x = {1.0, 2.0, 3.0};
        final double[] y = {1.0, 4.0, 9.0};
        final double[] startPoint = {1.5, 2.5, 3.5};
        final RealVector target = new Array2DRowRealMatrix(y).transpose();
        final RealMatrix weightMatrix = new Array2DRowRealMatrix(new double[][]{{0.1, 0, 0}, {0, 0.1, 0}});
        final RealVector initialGuess = new Array2DRowRealMatrix(x).transpose();
        final GoalType goalType = new SimpleGoalType(GoalType.MINIMIZE, 0);

        NonlinearConstraint constraint = (RealVector p) -> {
            double sum = 0.0;
            for (int i = 0; i < p.getDimension(); i++) {
                sum += Math.pow(p.getEntry(i), 2);
            }
            return sum - 10;
        };

        PointValuePair result = optimizer.optimize(new SumFunction(x), target.toVector(),
                weightMatrix.getColumnVector(0).toArray(), startPoint, constraint,
                new SimpleBounds(new Double[]{-10.0, -10.0, -10.0}, new Double[]{10.0, 10.0, 10.0}),
                new MaxIterationsExceededException(1), new MaxEval(10), goalType,
                new InitialGuess(initialGuess), new IterationNumber(10), new Gradient(), null);

        Assert.assertTrue("Optimization failed", result != null);
        Assert.assertArrayEquals("Incorrect results", x, result.getPoint(), 1e-6);
        Assert.assertEquals("Incorrect objective function value", 0.0, result.getValue(), 1e-6);
    }

    /**
     * Test optimization with a non-differentiable function
     * @throws Exception if an error occurs during optimization
     */
    @Test
    public void testNonDifferentiableFunction() throws Exception {
        final double[] x = {1.0, 2.0};
        final double[] y = {3.0, 4.0};
        final double[] startPoint = {1.5, 2.5};
        final RealVector target = new Array2DRowRealMatrix(y).transpose();
        final RealMatrix weightMatrix = new Array2DRowRealMatrix(new double[][]{{0.1, 0}, {0, 0.1}});
        final RealVector initialGuess = new Array2DRowRealMatrix(x).transpose();
        final GoalType goalType = new SimpleGoalType(GoalType.MINIMIZE, 0);

        PointValuePair result = optimizer.optimize(new AbsFunction(x), target.toVector(),
                weightMatrix.getColumnVector(0).toArray(), startPoint, null,
                new SimpleBounds(new Double[]{-10.0, -10.0}, new Double[]{10.0, 10.0}),
                new MaxIterationsExceededException(1), new MaxEval(10), goalType,
                new InitialGuess(initialGuess), new IterationNumber(10), null, null);

        Assert.assertTrue("Optimization failed", result != null);
        Assert.assertArrayEquals("Incorrect results", x, result.getPoint(), 1e-6);
        Assert.assertEquals("Incorrect objective function value", 0.0, result.getValue(), 1e-6);
    }

    /**
     * Test optimization with an unbounded function
     * @throws Exception if an error occurs during optimization
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testUnboundedFunction() throws Exception {
        final double[] x = {1.0, 2.0};
        final double[] startPoint = {1.5, 2.5};
        optimizer.optimize(new UnboundedFunction(x), new RealVector(new double[0]), null, startPoint, null, null, null, null);
    }

    /**
     * Test optimization with a discontinuous function
     * @throws Exception if an error occurs during optimization
     */
    @Test(expected = PrecisionExceededException.class)
    public void testDiscontinuousFunction() throws Exception {
        final double[] x = {1.0, 2.0};
        final double[] startPoint = {1.5, 2.5};
        optimizer.optimize(new DiscontinuousFunction(x), new RealVector(new double[0]), null, startPoint, null, null, null, null);
    }
}

abstract class TestFunction implements DifferentiableMultivariateVectorialFunction {
    protected final double[] x;

    public TestFunction(double[] x) {
        this.x = x;
    }

    @Override
    public RealVector value(RealVector v) {
        throw new UnsupportedOperationException();
    }

    @Override
    public RealMatrix jacobian() {
        throw new UnsupportedOperationException();
    }

    @Override
    public RealVector gradient(RealVector v) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void checkDerivative(RealVector v, RealVector w) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void checkHessian(RealVector v, RealMatrix h) {
        throw new UnsupportedOperationException();
    }
}

class SumFunction extends TestFunction {
    public SumFunction(double[] x) {
        super(x);
    }

    @Override
    public RealVector value(RealVector v) {
        RealVector result = new Array2DRowRealMatrix(x).transpose().mapMultiply(v);
        return result;
    }

    @Override
    public RealMatrix jacobian() {
        RealMatrix result = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {1.0, 1.0}});
        return result;
    }

    @Override
    public RealVector gradient(RealVector v) {
        RealVector result = new Array2DRowRealMatrix(x).transpose();
        return result;
    }

    @Override
    public void checkDerivative(RealVector v, RealVector w) {
        RealVector result = new Array2DRowRealMatrix(x).transpose().mapMultiply(w);
        Assert.assertArrayEquals("Incorrect derivative", result.toArray(), jacobian().getColumnVector(0).toArray(), 1e-6);
    }

    @Override
    public void checkHessian(RealVector v, RealMatrix h) {
        RealMatrix result = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {1.0, 1.0}});
        Assert.assertEquals("Incorrect hessian", result, h);
    }
}

class AbsFunction extends TestFunction {
    public AbsFunction(double[] x) {
        super(x);
    }

    @Override
    public RealVector value(RealVector v) {
        return new Array2DRowRealMatrix(x).mapMultiply(Math.abs(v)).toVector();
    }

    @Override
    public RealMatrix jacobian() {
        RealMatrix result = new Array2DRowRealMatrix(new double[][]{{Math.abs(x[0]), 0.0}, {0.0, Math.abs(x[1])}});
        return result;
    }

    @Override
    public RealVector gradient(RealVector v) {
        RealVector result = new Array2DRowRealMatrix(x).mapMultiply(new RealVector(new double[]{-Math.signum(x[0]), -Math.signum(x[1])}));
        return result;
    }

    @Override
    public void checkDerivative(RealVector v, RealVector w) {
        RealVector result = new Array2DRowRealMatrix(x).mapMultiply(new RealVector(new double[]{-Math.signum(x[0]), -Math.signum(x[1])}));
        Assert.assertArrayEquals("Incorrect derivative", result.toArray(), jacobian().getColumnVector(0).toArray(), 1e-6);
    }

    @Override
    public void checkHessian(RealVector v, RealMatrix h) {
        throw new UnsupportedOperationException();
    }
}

class UnboundedFunction extends TestFunction {
    public UnboundedFunction(double[] x) {
        super(x);
    }

    @Override
    public RealVector value(RealVector v) {
        double sum = 0;
        for (int i = 0; i < x.length; i++) {
            sum += Math.abs(v.getEntry(i)) * Math.pow(Math.abs(v.getEntry(i)), 2);
        }
        return new Array2DRowRealMatrix(x).transpose().mapMultiply(v).mapMultiply(new Array2DRowRealMatrix(new double[][]{{1.0 / 3.0, 1.0 / 3.0}, {1.0 / 3.0, 1.0 / 3.0}}).transpose()).mapSubtract(new Array2DRowRealMatrix(new double[]{sum, sum}));
    }
}

class DiscontinuousFunction extends TestFunction {
    public DiscontinuousFunction(double[] x) {
        super(x);
    }

    @Override
    public RealVector value(RealVector v) {
        RealVector result = new Array2DRowRealMatrix(x).mapMultiply(Math.abs(v));
        return result;
    }

    @Override
    public RealMatrix jacobian() {
        RealMatrix result = new Array2DRowRealMatrix(new double[][]{{Math.abs(x[0]), 0.0}, {0.0, Math.abs(x[1])}});
        return result;
    }

    @Override
    public RealVector gradient(RealVector v) {
        return new Array2DRowRealMatrix(x).mapMultiply(new RealVector(new double[]{-Math.signum(x[0]), -Math.signum(x[1])}));
    }

    @Override
    public void checkDerivative(RealVector v, RealVector w) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void checkHessian(RealVector v, RealMatrix h) {
        throw new UnsupportedOperationException();
    }
}