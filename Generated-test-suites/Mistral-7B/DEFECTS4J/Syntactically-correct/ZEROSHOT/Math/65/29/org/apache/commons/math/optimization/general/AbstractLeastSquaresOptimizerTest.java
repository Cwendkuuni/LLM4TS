import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxEvaluationsExceededException;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.NonLinearConstraint;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.SimpleGoalType;
import org.apache.commons.math3.optim.SimpleLinearObjectiveFunction;
import org.apache.commons.math3.optim.SimpleConstraints;
import org.apache.commons.math3.optim.nonlinear.levenbergmarquardt.LevenbergMarquardtOptimizer;
import org.apache.commons.math3.optim.nonlinear.levenbergmarquardt.LevenbergMarquardtOptimizer.MaxIterationsExceededException;
import org.apache.commons.math3.optim.nonlinear.levenbergmarquardt.LevenbergMarquardtOptimizer.MinimizationData;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class AbstractLeastSquaresOptimizerTest {

    @Test
    public void testAbstractLeastSquaresOptimizer() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected PointValuePair doOptimize()
                    throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
                OptimizationWorker worker = new OptimizationWorker(optimizer);
                return worker.optimize(new LinearFunction(new double[]{-1, 1}));
            }
        };

        // Test default settings
        optimizer.setMaxIterations(AbstractLeastSquaresOptimizer.DEFAULT_MAX_ITERATIONS);
        optimizer.setMaxEvaluations(Integer.MAX_VALUE);

        // Test optimization
        double[] startPoint = {1.0, 1.0};
        double[] target = {2.0, 2.0};
        double[] weights = {1.0, 1.0};
        PointValuePair result = optimizer.optimize(new LinearFunction(target), startPoint, weights, startPoint);

        assertArrayEquals(new double[]{0.0, 0.0}, result.getPoint(), 0.01);
        assertEquals(0.0, result.getValue(), 0.01);

        // Test optimization with constraints
        LinearFunctionWithConstraints functionWithConstraints = new LinearFunctionWithConstraints(new double[]{-2.0, 2.0});
        double[] startPointWithConstraints = {1.0, 1.0};
        PointValuePair resultWithConstraints = optimizer.optimize(functionWithConstraints, startPointWithConstraints, weights, startPointWithConstraints);

        assertArrayEquals(new double[]{1.0, 1.0}, resultWithConstraints.getPoint(), 0.01);
        assertEquals(3.0, resultWithConstraints.getValue(), 0.01);

        // Test optimization with bounds
        LinearFunctionWithBounds functionWithBounds = new LinearFunctionWithBounds(new double[]{-3.0, 3.0});
        double[] startPointWithBounds = {1.0, 1.0};
        PointValuePair resultWithBounds = optimizer.optimize(functionWithBounds, startPointWithBounds, weights, startPointWithBounds);

        assertArrayEquals(new double[]{1.0, 1.0}, resultWithBounds.getPoint(), 0.01);
        assertEquals(3.0, resultWithBounds.getValue(), 0.01);

        // Test optimization with goal type
        LinearFunctionWithGoalType functionWithGoalType = new LinearFunctionWithGoalType(new double[]{-4.0, 4.0}, SimpleGoalType.MINIMIZE);
        double[] startPointWithGoalType = {1.0, 1.0};
        PointValuePair resultWithGoalType = optimizer.optimize(functionWithGoalType, startPointWithGoalType, weights, startPointWithGoalType);

        assertArrayEquals(new double[]{1.0, 1.0}, resultWithGoalType.getPoint(), 0.01);
        assertEquals(-1.0, resultWithGoalType.getValue(), 0.01);

        // Test optimization with max iterations
        optimizer.setMaxIterations(1);
        PointValuePair resultWithMaxIterations = optimizer.optimize(new LinearFunction(target), startPoint, weights, startPoint);

        assertArrayEquals(startPoint, resultWithMaxIterations.getPoint(), 0.01);
        assertEquals(Double.POSITIVE_INFINITY, resultWithMaxIterations.getValue(), 0.01);

        // Test optimization with max evaluations
        optimizer.setMaxEvaluations(1);
        PointValuePair resultWithMaxEvaluations = optimizer.optimize(new LinearFunction(target), startPoint, weights, startPoint);

        assertArrayEquals(startPoint, resultWithMaxEvaluations.getPoint(), 0.01);
        assertEquals(Double.POSITIVE_INFINITY, resultWithMaxEvaluations.getValue(), 0.01);
    }

    private static class OptimizationWorker {
        private final AbstractLeastSquaresOptimizer optimizer;
        private final LinearFunction linearFunction;

        public OptimizationWorker(AbstractLeastSquaresOptimizer optimizer) {
            this.optimizer = optimizer;
            this.linearFunction = new LinearFunction(new double[]{});
        }

        public PointValuePair optimize(DifferentiableMultivariateVectorialFunction function)
                throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
            MinimizationData data = new MinimizationData(optimizer, function);
            LevenbergMarquardtOptimizer optimizer = new LevenbergMarquardtOptimizer();
            return optimizer.optimize(data);
        }
    }

    private static class LinearFunction implements MultivariateFunction {
        private final double[] target;

        public LinearFunction(double[] target) {
            this.target = target;
        }

        @Override
        public double value(RealVector x)
                throws FunctionEvaluationException {
            double[] xValues = x.toArray();
            double sum = 0.0;
            for (int i = 0; i < target.length; i++) {
                double xValue = xValues[i];
                double targetValue = target[i];
                sum += Math.pow(targetValue - (xValue * i), 2);
            }
            return sum;
        }

        @Override
        public RealVector gradient(RealVector x)
                throws FunctionEvaluationException {
            double[] xValues = x.toArray();
            double[] result = new double[xValues.length];
            for (int i = 0; i < xValues.length; i++) {
                double xValue = xValues[i];
                double sum = 0.0;
                for (int j = 0; j < target.length; j++) {
                    double targetValue = target[j];
                    sum += 2 * (targetValue - (xValue * j)) * j;
                }
                result[i] = sum;
            }
            return new Array2DRowRealMatrix(result).asColumnVector();
        }
    }

    private static class LinearFunctionWithConstraints implements MultivariateFunction, NonLinearConstraint {
        private final double[] target;
        private final double[] bounds;

        public LinearFunctionWithConstraints(double[] target) {
            this.target = target;
            this.bounds = new double[target.length];
            for (int i = 0; i < bounds.length; i++) {
                bounds[i] = Double.NEGATIVE_INFINITY;
            }
        }

        public LinearFunctionWithConstraints(double[] target, double[] bounds) {
            this.target = target;
            this.bounds = bounds;
        }

        @Override
        public double value(RealVector x)
                throws FunctionEvaluationException {
            double[] xValues = x.toArray();
            double sum = 0.0;
            for (int i = 0; i < target.length; i++) {
                double xValue = xValues[i];
                double targetValue = target[i];
                sum += Math.pow(targetValue - (xValue * i), 2);
            }
            return sum;
        }

        @Override
        public RealVector gradient(RealVector x)
                throws FunctionEvaluationException {
            double[] xValues = x.toArray();
            double[] result = new double[xValues.length];
            for (int i = 0; i < xValues.length; i++) {
                double xValue = xValues[i];
                double sum = 0.0;
                for (int j = 0; j < target.length; j++) {
                    double targetValue = target[j];
                    sum += 2 * (targetValue - (xValue * j)) * j;
                }
                result[i] = sum;
            }
            return new Array2DRowRealMatrix(result).asColumnVector();
        }

        @Override
        public void constraint(RealVector x, RealVector gradient, RealVector lagrangeMultipliers)
                throws FunctionEvaluationException {
            double[] xValues = x.toArray();
            double[] boundsValues = lagrangeMultipliers.toArray();
            for (int i = 0; i < boundsValues.length; i++) {
                double xValue = xValues[i];
                double lowerBound = bounds[i];
                double upperBound = bounds[i + bounds.length];
                if (xValue < lowerBound) {
                    gradient.setEntry(i, -Math.signum(xValue - lowerBound));
                    lagrangeMultipliers.setEntry(i, Math.max(0.0, boundsValues[i] + gradient.getEntry(i)));
                    throw new NumberIsTooSmallException(LocalizedFormats.BOUND_VIOLATED, i, lowerBound, xValue);
                }
                if (xValue > upperBound) {
                    gradient.setEntry(i, Math.signum(xValue - upperBound));
                    lagrangeMultipliers.setEntry(i + bounds.length, Math.max(0.0, boundsValues[i] + gradient.getEntry(i)));
                    throw new NumberIsTooSmallException(LocalizedFormats.BOUND_VIOLATED, i + bounds.length, upperBound, xValue);
                }
            }
        }

        @Override
        public SimpleLinearObjectiveFunction getObjectiveFunction() {
            return new SimpleLinearObjectiveFunction(this);
        }

        @Override
        public SimpleConstraints getConstraints() {
            return new SimpleConstraints(new SimpleBounds(bounds), new SimpleBounds(bounds));
        }
    }

    private static class LinearFunctionWithBounds implements MultivariateFunction {
        private final double[] target;
        private final double[] bounds;

        public LinearFunctionWithBounds(double[] target) {
            this.target = target;
            this.bounds = new double[target.length];
            for (int i = 0; i < bounds.length; i++) {
                bounds[i] = 0.0;
            }
        }

        @Override
        public double value(RealVector x)
                throws FunctionEvaluationException {
            double[] xValues = x.toArray();
            double sum = 0.0;
            for (int i = 0; i < target.length; i++) {
                double xValue = xValues[i];
                double targetValue = target[i];
                sum += Math.pow(targetValue - (xValue * i), 2);
            }
            return sum;
        }

        @Override
        public RealVector gradient(RealVector x)
                throws FunctionEvaluationException {
            double[] xValues = x.toArray();
            double[] result = new double[xValues.length];
            for (int i = 0; i < xValues.length; i++) {
                double xValue = xValues[i];
                double sum = 0.0;
                for (int j = 0; j < target.length; j++) {
                    double targetValue = target[j];
                    sum += 2 * (targetValue - (xValue * j)) * j;
                }
                result[i] = sum;
            }
            return new Array2DRowRealMatrix(result).asColumnVector();
        }

        @Override
        public SimpleBounds getBounds() {
            return new SimpleBounds(bounds, bounds);
        }
    }

    private static class LinearFunctionWithGoalType implements MultivariateFunction {
        private final double[] target;
        private final SimpleGoalType goalType;

        public LinearFunctionWithGoalType(double[] target, SimpleGoalType goalType) {
            this.target = target;
            this.goalType = goalType;
        }

        @Override
        public double value(RealVector x)
                throws FunctionEvaluationException {
            double[] xValues = x.toArray();
            double sum = 0.0;
            for (int i = 0; i < target.length; i++) {
                double xValue = xValues[i];
                double targetValue = target[i];
                sum += Math.pow(targetValue - (xValue * i), 2);
            }
            return sum;
        }

        @Override
        public RealVector gradient(RealVector x)
                throws FunctionEvaluationException {
            double[] xValues = x.toArray();
            double[] result = new double[xValues.length];
            for (int i = 0; i < xValues.length; i++) {
                double xValue = xValues[i];
                double sum = 0.0;
                for (int j = 0; j < target.length; j++) {
                    double targetValue = target[j];
                    sum += 2 * (targetValue - (xValue * j)) * j;
                }
                result[i] = sum;
            }
            return new Array2DRowRealMatrix(result).asColumnVector();
        }

        @Override
        public SimpleGoalType getGoalType() {
            return goalType;
        }
    }
}