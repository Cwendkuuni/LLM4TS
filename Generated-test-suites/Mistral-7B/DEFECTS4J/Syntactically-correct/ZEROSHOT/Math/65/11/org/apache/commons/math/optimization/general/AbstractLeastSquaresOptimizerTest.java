import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SingleVariableOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.vector.linear.LinearConstraint;
import org.apache.commons.math3.optim.nonlinear.vector.linear.LinearConstraints;
import org.apache.commons.math3.optim.nonlinear.vector.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.vector.linear.LinearPointValuePair;
import org.apache.commons.math3.optim.nonlinear.vector.linear.LinearSubjectToConstraintMinimizer;
import org.apache.commons.math3.optim.nonlinear.vector.linear.LinearSubjectToConstraintMinimizer.LinearConstraintSet;
import org.apache.commons.math3.optim.nonlinear.vector.linear.LinearSubjectToConstraintMinimizer.LinearObjectiveFunctionAndConstraints;
import org.apache.commons.math3.optim.nonlinear.vector.linear.LinearSubjectToConstraintMinimizer.LinearSubjectToConstraintMinimizerOptions;
import org.apache.commons.math3.optim.nonlinear.vector.linear.LinearVectorConstraint;
import org.apache.commons.math3.optim.nonlinear.vector.linear.LinearVectorConstraints;
import org.apache.commons.math3.optim.nonlinear.vector.linear.LinearVectorPointValuePair;
import org.apache.commons.math3.optim.point.PointValuePair;
import org.apache.commons.math3.optim.points.PointValues;
import org.apache.commons.math3.optim.initializer.StartPointInitializer;
import org.apache.commons.math3.optim.initializer.SimpleStartPointInitializer;
import org.apache.commons.math3.optim.initializer.StartPointIterator;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.POINT_MISMATCH;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class AbstractLeastSquaresOptimizerTest {

    @Test
    public void testAbstractLeastSquaresOptimizer() {
        // Test default constructor
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        assertEquals(DEFAULT_MAX_ITERATIONS, optimizer.getMaxIterations());
        assertNull(optimizer.getConvergenceChecker());
        assertNull(optimizer.jacobian);
        assertNull(optimizer.targetValues);
        assertNull(optimizer.residualsWeights);
        assertNull(optimizer.point);
        assertNull(optimizer.objective);
        assertNull(optimizer.residuals);
        assertNull(optimizer.cost);

        // Test constructor with custom settings
        AbstractLeastSquaresOptimizer optimizerCustom = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize()
                    throws FunctionEvaluationException, OptimizationException {
                // Override the default optimizer with a simple LinearSubjectToConstraintMinimizer
                LinearSubjectToConstraintMinimizerOptions options = new LinearSubjectToConstraintMinimizerOptions();
                options.setMaxIterations(5);
                LinearSubjectToConstraintMinimizer minimizer = new LinearSubjectToConstraintMinimizer(options);

                LinearConstraintSet constraints = LinearConstraints.create();
                LinearObjectiveFunctionAndConstraints objectiveAndConstraints =
                        new LinearObjectiveFunctionAndConstraints(new LinearObjectiveFunction(new UnivariateFunction() {
                            @Override
                            public double value(double x) {
                                return Math.pow(x, 2);
                            }
                        }), constraints);

                PointValuePair optimalSolution = minimizer.optimize(objectiveAndConstraints);
                LinearVectorPointValuePair optimalSolutionVector = (LinearVectorPointValuePair) optimalSolution;
                assertArrayEquals(new double[]{0}, optimalSolutionVector.getPoint(), 0.001);
                assertEquals(0, optimalSolutionVector.getValue(), 0.001);

                return new LinearVectorPointValuePair(new double[]{0}, 0);
            }
        };
        assertEquals(5, optimizerCustom.getMaxIterations());
        assertNotNull(optimizerCustom.getConvergenceChecker());
        assertNull(optimizerCustom.jacobian);
        assertNull(optimizerCustom.targetValues);
        assertNull(optimizerCustom.residualsWeights);
        assertNull(optimizerCustom.point);
        assertNull(optimizerCustom.objective);
        assertNull(optimizerCustom.residuals);
        assertNull(optimizerCustom.cost);

        // Test methods with custom settings
        optimizerCustom.setConvergenceChecker(new SimpleVectorialValueChecker());
        assertNotNull(optimizerCustom.getConvergenceChecker());
        optimizerCustom.updateJacobian();
        assertNull(optimizerCustom.jacobian);
        optimizerCustom.updateResidualsAndCost();
        assertNull(optimizerCustom.cost);

        // Test optimization using custom optimizer
        DifferentiableMultivariateVectorialFunction function = new DifferentiableMultivariateVectorialFunction() {
            @Override
            public double[] value(double[] x) {
                return new double[]{Math.pow(x[0], 2) + Math.pow(x[1], 2)};
            }

            @Override
            public double[] gradient(double[] x) {
                return new double[]{2 * x[0], 2 * x[1]};
            }
        };
        double[] startPoint = new double[]{1, 1};
        double[] target = new double[]{3};
        double[] weights = new double[]{1};
        PointValuePair optimizedSolution = optimizerCustom.optimize(function, target, weights, startPoint);
        LinearVectorPointValuePair optimizedSolutionVector = (LinearVectorPointValuePair) optimizedSolution;
        assertArrayEquals(new double[]{0.5, 0.5}, optimizedSolutionVector.getPoint(), 0.001);
        assertEquals(Math.pow(0.5, 2) + Math.pow(0.5, 2), optimizedSolutionVector.getValue(), 0.001);

        // Test exceptions
        try {
            optimizerCustom.optimize(null, target, weights, startPoint);
            Assert.fail("Expected FunctionEvaluationException");
        } catch (FunctionEvaluationException e) {
            // Expected
        }

        try {
            optimizerCustom.optimize(function, null, weights, startPoint);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            optimizerCustom.optimize(function, target, null, startPoint);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            optimizerCustom.optimize(function, target, weights, null);
            Assert.fail("Expected POINT_MISMATCH");
        } catch (POINT_MISMATCH e) {
            // Expected
        }

        try {
            optimizerCustom.optimize(function, target, weights, startPoint);
            optimizerCustom.incrementIterationsCounter();
            optimizerCustom.incrementIterationsCounter();
            optimizerCustom.incrementIterationsCounter();
            optimizerCustom.incrementIterationsCounter();
            optimizerCustom.incrementIterationsCounter();
            optimizerCustom.incrementIterationsCounter();
            optimizerCustom.updateJacobian();
            optimizerCustom.updateResidualsAndCost();
            optimizerCustom.updateResidualsAndCost();
            optimizerCustom.updateResidualsAndCost();
            optimizerCustom.updateResidualsAndCost();
            optimizerCustom.updateResidualsAndCost();
            optimizerCustom.updateResidualsAndCost();
            assertTrue(optimizerCustom.getIterations() > 7);
            assertFalse(optimizerCustom.getCost() < 1);
            assertFalse(optimizerCustom.getCost() > 2);
        } catch (FunctionEvaluationException e) {
            // Expected
        } catch (OptimizationException e) {
            // Expected
        }

        try {
            optimizerCustom.optimize(function, target, weights, startPoint);
            optimizerCustom.setMaxIterations(0);
            optimizerCustom.optimize(function, target, weights, startPoint);
            Assert.fail("Expected MaxIterationsExceededException");
        } catch (OptimizationException e) {
            assertTrue(e instanceof MaxIterationsExceededException);
        }

        try {
            optimizerCustom.optimize(function, target, weights, startPoint);
            optimizerCustom.setMaxEvaluations(0);
            optimizerCustom.optimize(function, target, weights, startPoint);
            Assert.fail("Expected MaxEvaluationsExceededException");
        } catch (OptimizationException e) {
            assertTrue(e instanceof MaxEvaluationsExceededException);
        }
    }

}