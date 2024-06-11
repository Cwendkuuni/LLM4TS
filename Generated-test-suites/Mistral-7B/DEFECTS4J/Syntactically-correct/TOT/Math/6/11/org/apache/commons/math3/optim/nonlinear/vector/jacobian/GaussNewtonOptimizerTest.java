package org.apache.commons.math3.optimization.general;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.QRDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularMatrixException;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.SimpleGoalType;
import org.apache.commons.math3.optim.UnboundedDomainException;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.PointValuePairFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.SimpleObjectiveFunction;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test for {@link GaussNewtonOptimizer}.
 */
public class GaussNewtonOptimizerTest {

    /**
     * Test optimization with identity weights.
     */
    @Test
    public void testIdentityWeights() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        optimizer.setStartPoint(new double[]{1.0, 2.0, 3.0});
        optimizer.setObjectiveFunction(new SimpleObjectiveFunction(new IdentityFunction()));
        optimizer.optimize();
        RealVector expected = new ArrayRealVector(new double[]{1.0, 1.0, 1.0});
        Assert.assertArrayEquals(expected.toArray(), optimizer.getPoint(), 0.0);
        Assert.assertEquals(0.0, optimizer.getCost(), 0.0);
    }

    /**
     * Test optimization with different weights.
     */
    @Test
    public void testDifferentWeights() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        optimizer.setUseLU(false);
        optimizer.setStartPoint(new double[]{1.0, 2.0, 3.0});
        optimizer.setObjectiveFunction(new WeightedSumFunction(new double[]{1.0, 2.0, 3.0}));
        optimizer.optimize();
        RealVector expected = new ArrayRealVector(new double[]{2.0 / 6.0, 2.0 / 3.0, 1.0 / 3.0});
        Assert.assertArrayEquals(expected.toArray(), optimizer.getPoint(), 0.0);
        Assert.assertEquals(0.0, optimizer.getCost(), 0.0);
    }

    /**
     * Test optimization with non-square system.
     */
    @Test(expected = SingularMatrixException.class)
    public void testNonSquareSystem() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        optimizer.setUseLU(false);
        optimizer.setStartPoint(new double[]{1.0, 2.0, 3.0});
        optimizer.setObjectiveFunction(new NonSquareSystemFunction());
        optimizer.optimize();
    }

    /**
     * Test optimization with invalid input.
     */
    @Test(expected = NullArgumentException.class)
    public void testInvalidInput() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        optimizer.setObjectiveFunction(null);
        optimizer.optimize();
    }

    /**
     * Test optimization with non-convergent sequence.
     */
    @Test
    public void testNonConvergentSequence() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        optimizer.setStartPoint(new double[]{1.0, 2.0, 3.0});
        optimizer.setObjectiveFunction(new NonConvergentSequenceFunction());
        optimizer.setConvergenceChecker(null);
        optimizer.optimize();
        Assert.fail("Optimization did not fail.");
    }

    /**
     * Test optimization with unbounded domain.
     */
    @Test(expected = UnboundedDomainException.class)
    public void testUnboundedDomain() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        optimizer.setStartPoint(new double[]{1.0, 2.0, 3.0});
        optimizer.setObjectiveFunction(new UnboundedDomainFunction());
        optimizer.optimize();
    }

    /**
     * Identity function for testing purposes.
     */
    private static class IdentityFunction implements ObjectiveFunction {

        @Override
        public double value(RealVector x) {
            return x.dotProduct(x);
        }

        @Override
        public RealVector gradient(RealVector x) {
            return new ArrayRealVector(x.toArray().clone());
        }

        @Override
        public GoalType getGoalType() {
            return SimpleGoalType.MINIMIZE;
        }
    }

    /**
     * Function for testing with different weights.
     */
    private static class WeightedSumFunction implements ObjectiveFunction {

        private final double[] _weights;

        public WeightedSumFunction(double[] weights) {
            _weights = weights;
        }

        @Override
        public double value(RealVector x) {
            double sum = 0.0;
            for (int i = 0; i < x.getDimension(); ++i) {
                sum += _weights[i] * x.getEntry(i) * x.getEntry(i);
            }
            return sum;
        }

        @Override
        public RealVector gradient(RealVector x) {
            RealVector gradient = new ArrayRealVector(new double[x.getDimension()]);
            for (int i = 0; i < x.getDimension(); ++i) {
                gradient.setEntry(i, 2.0 * _weights[i] * x.getEntry(i));
            }
            return gradient;
        }

        @Override
        public GoalType getGoalType() {
            return SimpleGoalType.MINIMIZE;
        }
    }

    /**
     * Function for testing with non-square system.
     */
    private static class NonSquareSystemFunction implements ObjectiveFunction {

        @Override
        public double value(RealVector x) {
            throw new SingularMatrixException("Non-square system.");
        }

        @Override
        public RealVector gradient(RealVector x) {
            return new ArrayRealVector(new double[x.getDimension()]);
        }

        @Override
        public GoalType getGoalType() {
            return SimpleGoalType.MINIMIZE;
        }
    }

    /**
     * Function for testing with an invalid input.
     */
    private static class InvalidInputFunction implements ObjectiveFunction {

        @Override
        public double value(RealVector x) {
            throw new NullPointerException();
        }

        @Override
        public RealVector gradient(RealVector x) {
            return new ArrayRealVector(new double[x.getDimension()]);
        }

        @Override
        public GoalType getGoalType() {
            return SimpleGoalType.MINIMIZE;
        }
    }

    /**
     * Function for testing with a non-convergent sequence.
     */
    private static class NonConvergentSequenceFunction implements ObjectiveFunction {

        private int _iter = 0;

        @Override
        public double value(RealVector x) {
            _iter++;
            if (_iter % 2 == 0) {
                throw new ConvergenceException("Test non-convergent sequence.");
            }
            return Math.pow(x.getEntry(0), 2.0) + Math.pow(x.getEntry(1), 2.0) + Math.pow(x.getEntry(2), 2.0);
        }

        @Override
        public RealVector gradient(RealVector x) {
            return new ArrayRealVector(new double[]{2.0 * x.getEntry(0), 2.0 * x.getEntry(1), 2.0 * x.getEntry(2)});
        }

        @Override
        public GoalType getGoalType() {
            return SimpleGoalType.MINIMIZE;
        }
    }

    /**
     * Function for testing with an unbounded domain.
     */
    private static class UnboundedDomainFunction implements ObjectiveFunction {

        @Override
        public double value(RealVector x) {
            return Math.log(Math.abs(x.getEntry(0))) + Math.log(Math.abs(x.getEntry(1))) + Math.log(Math.abs(x.getEntry(2)));
        }

        @Override
        public RealVector gradient(RealVector x) {
            return new ArrayRealVector(new double[]{-1.0 / Math.log(Math.abs(x.getEntry(0))) / x.getEntry(0),
                    -1.0 / Math.log(Math.abs(x.getEntry(1))) / x.getEntry(1),
                    -1.0 / Math.log(Math.abs(x.getEntry(2))) / x.getEntry(2)});
        }

        @Override
        public GoalType getGoalType() {
            return SimpleGoalType.MINIMIZE;
        }
    }
}