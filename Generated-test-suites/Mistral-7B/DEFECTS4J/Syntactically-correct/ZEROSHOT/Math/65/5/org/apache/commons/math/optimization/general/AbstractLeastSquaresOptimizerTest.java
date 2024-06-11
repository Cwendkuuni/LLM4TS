import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealArray2DSparseMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class AbstractLeastSquaresOptimizerTest {

    @Test
    public void testDefaultConstructor() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        assertEquals(AbstractLeastSquaresOptimizer.DEFAULT_MAX_ITERATIONS, optimizer.getMaxIterations());
        assertEquals(new SimpleVectorialValueChecker(), optimizer.getConvergenceChecker());
        assertEquals(Integer.MAX_VALUE, optimizer.getMaxEvaluations());
    }

    @Test
    public void testSettersAndGetters() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        optimizer.setMaxIterations(5);
        optimizer.setConvergenceChecker(new MyVectorialConvergenceChecker());
        optimizer.setMaxEvaluations(10);

        assertEquals(5, optimizer.getMaxIterations());
        assertEquals(new MyVectorialConvergenceChecker(), optimizer.getConvergenceChecker());
        assertEquals(10, optimizer.getMaxEvaluations());
    }

    @Test
    public void testMinimization() throws Exception {
        AbstractLeastSquaresOptimizer optimizer = new MyLeastSquaresOptimizer();

        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return (x[0] - 1) * (x[0] - 1) + (x[1] - 1) * (x[1] - 1);
            }

            @Override
            public double[] gradient(double[] x) {
                double[] gradient = new double[2];
                gradient[0] = -2 * (x[0] - 1);
                gradient[1] = -2 * (x[1] - 1);
                return gradient;
            }
        };

        double[] target = new double[]{1.0, 1.0};
        double[] weights = new double[]{1.0, 1.0};
        double[] startPoint = new double[]{2.0, 2.0};

        VectorialPointValuePair result = optimizer.optimize(function, target, weights, startPoint);

        // Check that optimization was successful
        Assert.assertEquals(0.0, result.getPoint()[0] - 1.0, 0.001); // x
        Assert.assertEquals(0.0, result.getPoint()[1] - 1.0, 0.001); // y
        Assert.assertEquals(0.0, result.getValue() - 0.0, 0.001); // f(x, y)

        // Check that convergence was detected
        assertTrue(optimizer.isConverged());

        // Check that iterations and evaluations were within limits
        assertEquals(AbstractLeastSquaresOptimizer.DEFAULT_MAX_ITERATIONS, optimizer.getIterations());
        assertEquals(Integer.MAX_VALUE, optimizer.getEvaluations());
    }

    @Test
    public void testMaxIterationsExceededException() throws Exception {
        AbstractLeastSquaresOptimizer optimizer = new MyLeastSquaresOptimizer();
        optimizer.setMaxIterations(0);

        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return 0.0;
            }

            @Override
            public double[] gradient(double[] x) {
                return new double[0];
            }
        };

        double[] target = new double[]{0.0};
        double[] weights = new double[]{1.0};
        double[] startPoint = new double[]{0.0};

        try {
            optimizer.optimize(function, target, weights, startPoint);
            fail("Expected MaxIterationsExceededException");
        } catch (OptimizationException e) {
            assertTrue(e.getCause() instanceof MaxIterationsExceededException);
        }
    }

    @Test
    public void testMaxEvaluationsExceededException() throws Exception {
        AbstractLeastSquaresOptimizer optimizer = new MyLeastSquaresOptimizer();

        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return 0.0;
            }

            @Override
            public double[] gradient(double[] x) {
                return new double[0];
            }
        };

        double[] target = new double[]{0.0};
        double[] weights = new double[]{1.0};
        double[] startPoint = new double[]{0.0};

        optimizer.setMaxEvaluations(0);

        try {
            optimizer.optimize(function, target, weights, startPoint);
            fail("Expected MaxEvaluationsExceededException");
        } catch (OptimizationException e) {
            assertTrue(e.getCause() instanceof MaxEvaluationsExceededException);
        }
    }

    @Test
    public void testFunctionEvaluationException() throws Exception {
        AbstractLeastSquaresOptimizer optimizer = new MyLeastSquaresOptimizer();

        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                throw new FunctionEvaluationException("Function failed");
            }

            @Override
            public double[] gradient(double[] x) {
                return new double[0];
            }
        };

        double[] target = new double[]{0.0};
        double[] weights = new double[]{1.0};
        double[] startPoint = new double[]{0.0};

        try {
            optimizer.optimize(function, target, weights, startPoint);
            fail("Expected FunctionEvaluationException");
        } catch (OptimizationException e) {
            assertTrue(e.getCause() instanceof FunctionEvaluationException);
        }
    }

    @Test
    public void testIllegalArgumentException() throws Exception {
        AbstractLeastSquaresOptimizer optimizer = new MyLeastSquaresOptimizer();

        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return 0.0;
            }

            @Override
            public double[] gradient(double[] x) {
                return new double[0];
            }
        };

        double[] target = new double[]{0.0};
        double[] weights = null;
        double[] startPoint = new double[]{0.0};

        try {
            optimizer.optimize(function, target, weights, startPoint);
            fail("Expected IllegalArgumentException");
        } catch (OptimizationException e) {
            assertTrue(e.getCause() instanceof IllegalArgumentException);
        }
    }

    private static class MyVectorialConvergenceChecker implements VectorialConvergenceChecker {
        @Override
        public boolean isConverged(double[] point, double[] objective, double[] gradient, double error) {
            return false;
        }
    }

    private static class MyLeastSquaresOptimizer extends AbstractLeastSquaresOptimizer {
        @Override
        protected VectorialPointValuePair doOptimize()
                throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {

            double[] currentPoint = point.clone();
            double[] currentObjective = objective.clone();
            double[] currentGradient = new double[0];

            double error = 0.001;

            while (!isConverged(currentPoint, currentObjective, currentGradient, error)) {
                incrementIterationsCounter();
                updateResidualsAndCost();
                updateJacobian();
                move(currentPoint, currentGradient);
            }

            return new VectorialPointValuePair(currentPoint, currentObjective);
        }

        @Override
        protected void move(double[] currentPoint, double[] gradient) {
            double[] newPoint = currentPoint.clone();
            for (int i = 0; i < point.length; ++i) {
                newPoint[i] -= 0.01 * gradient[i];
            }
            setPoint(newPoint);
        }
    }
}