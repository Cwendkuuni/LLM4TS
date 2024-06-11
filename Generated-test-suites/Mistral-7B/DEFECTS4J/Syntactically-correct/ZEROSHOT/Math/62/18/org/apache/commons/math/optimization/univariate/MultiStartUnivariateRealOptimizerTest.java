import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.point.PointValuePairComparator;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Test;

import static org.apache.commons.math3.optim.GoalType.MINIMIZE;

public class MultiStartUnivariateRealOptimizerTest {

    @Test
    public void testMultiStart() throws FunctionEvaluationException, ConvergenceException, MaxEvalException, MathIllegalStateException {
        UnivariateRealFunction f = new AckleyFunction();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(
                new FibonacciSearchOptimizer(),
                5,
                new TestRandomGenerator());
        optimizer.setMaxEvaluations(100);

        PointValuePair solution = optimizer.optimize(f, MINIMIZE, -32.0, 30.0);
        Assert.assertNotNull(solution);
        Assert.assertEquals(0.0, Precision.abs(solution.getPoint().getValue() - 1.89657256384), 1e-8);

        UnivariateRealPointValuePair[] allSolutions = optimizer.getOptima();
        Assert.assertArrayEquals(5, allSolutions.length);
        PointValuePairComparator comparator = new PointValuePairComparator();
        Assert.assertTrue(comparator.compare(solution, allSolutions[0]) >= 0);
    }

    private static class AckleyFunction implements UnivariateRealFunction {
        @Override
        public double value(double x) {
            double sum = Math.pow(Math.sin(Math.PI * x), 2) + Math.pow(Math.cos(2 * Math.PI * x), 2);
            return -Math.exp(-0.2 * x) - Math.exp(Math.sqrt(0.001 * sum) / 0.1) + Math.exp(1.0) + 20.0;
        }

        @Override
        public double derive(double x) {
            double sum = Math.pow(Math.sin(Math.PI * x), 2) + Math.pow(Math.cos(2 * Math.PI * x), 2);
            double fx = -Math.exp(-0.2 * x) - Math.exp(Math.sqrt(0.001 * sum) / 0.1) + Math.exp(1.0) + 20.0;
            return -0.4 * x * Math.exp(-0.2 * x) - Math.exp(-Math.sqrt(0.001 * sum) / 0.1) * Math.sin(Math.PI * x);
        }
    }

    private static class TestRandomGenerator implements RandomGenerator {
        @Override
        public double nextDouble() {
            return Math.random();
        }

        @Override
        public void setSeed(long seed) {
            // Not used in this test.
        }
    }

    private static class FibonacciSearchOptimizer implements UnivariateOptimizer {
        @Override
        public PointValuePair optimize(UnivariateRealFunction f, GoalType goal, double min, double max)
                throws FunctionEvaluationException, ConvergenceException, MaxEvalException {
            return new FibonacciSearch(f, goal, min, max).findMinimum();
        }

        @Override
        public void setMaxEvaluations(int maxEvaluations) {
            // Not used in this test.
        }

        @Override
        public void setConvergenceChecker(ConvergenceChecker<? super PointValuePair> convergenceChecker) {
            // Not used in this test.
        }

        @Override
        public ConvergenceChecker<? super PointValuePair> getConvergenceChecker() {
            // Not used in this test.
            return null;
        }
    }

    private static class FibonacciSearch {
        private static final double EPSILON = 1e-9;
        private static final double FIB_GOLDEN_RATIO = 1.61803398875;
        private static final double FIB_SILVER_RATIO = FIB_GOLDEN_RATIO - 1.0;
        private static final double FIB_FIRST = 1.0;
        private static final double FIB_SECOND = 1.0;

        private UnivariateRealFunction function;
        private GoalType goal;
        private double min;
        private double max;
        private int maxEvaluations;

        public FibonacciSearch(UnivariateRealFunction function, GoalType goal, double min, double max) {
            this.function = function;
            this.goal = goal;
            this.min = min;
            this.max = max;
            this.maxEvaluations = 0;
        }

        public PointValuePair findMinimum() throws FunctionEvaluationException, ConvergenceException, MaxEvalException {
            double a = min;
            double b = max;
            double c;
            int n = 0;

            while (true) {
                n++;
                if (n > maxEvaluations) {
                    throw new MaxEvalException("Function evaluations limit reached.");
                }

                if (Precision.abs(b - a) <= EPSILON) {
                    return new PointValuePair(a, function.value(a));
                }

                c = a + FIB_FIRST * (b - a);

                maxEvaluations++;
                double fc = function.value(c);

                if (goal == MINIMIZE && fc < function.value(a)) {
                    a = c;
                } else {
                    b = b - (b - a) * FIB_SILVER_RATIO;
                }
            }
        }
    }
}