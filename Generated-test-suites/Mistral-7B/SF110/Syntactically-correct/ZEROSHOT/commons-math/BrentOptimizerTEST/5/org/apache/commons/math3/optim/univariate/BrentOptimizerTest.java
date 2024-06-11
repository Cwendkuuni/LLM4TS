import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.NonlinearOptimizationException;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.UnivariateOptimizer.*;
import org.apache.commons.math3.optim.nonlinear.scalar.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.util.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizer() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, 1.0e-6, null);
        try {
            UnivariateFunction function = new CubicFunction(-1.0);
            double result = optimizer.optimize(function, new InitialGuess(2.0), GoalType.MINIMIZE).getValue();
            assertEquals( result, -1.0, 1.0e-7);
        } catch (NonlinearOptimizationException e) {
            fail("Optimization did not converge: " + e.getMessage());
        }

        try {
            UnivariateFunction function = new QuadraticFunction(1.0);
            double result = optimizer.optimize(function, new InitialGuess(2.0), GoalType.MINIMIZE).getValue();
            assertEquals( result, 1.0, 1.0e-7);
        } catch (NonlinearOptimizationException e) {
            fail("Optimization did not converge: " + e.getMessage());
        }

        try {
            UnivariateFunction function = new SinFunction();
            double result = optimizer.optimize(function, new InitialGuess(2.0), GoalType.MINIMIZE).getValue();
            assertEquals( result, -Math.PI / 2.0, 1.0e-7);
        } catch (NonlinearOptimizationException e) {
            fail("Optimization did not converge: " + e.getMessage());
        }

        try {
            UnivariateFunction function = new AbsFunction();
            double result = optimizer.optimize(function, new InitialGuess(2.0), GoalType.MINIMIZE).getValue();
            fail("Optimization should not converge with the absolute function.");
        } catch (NonlinearOptimizationException e) {
            // Expected
        }

        try {
            UnivariateFunction function = new CustomFunction();
            double result = optimizer.optimize(function, new InitialGuess(2.0), GoalType.MINIMIZE).getValue();
            fail("Optimization should not converge with the custom function.");
        } catch (NonlinearOptimizationException e) {
            // Expected
        }

        try {
            UnivariateFunction function = new LogFunction();
            double result = optimizer.optimize(function, new InitialGuess(2.0), GoalType.MINIMIZE).getValue();
            assertEquals( result, Math.log(Math.E), 1.0e-7);
        } catch (NonlinearOptimizationException e) {
            fail("Optimization did not converge: " + e.getMessage());
        }

        try {
            UnivariateFunction function = new RootFunction(Math.sqrt(2.0));
            double result = optimizer.optimize(function, new InitialGuess(2.0), GoalType.MINIMIZE).getValue();
            assertEquals( result, Math.sqrt(2.0), 1.0e-7);
        } catch (NonlinearOptimizationException e) {
            fail("Optimization did not converge: " + e.getMessage());
        }
    }

    static class CubicFunction implements UnivariateFunction {
        private final double a;

        public CubicFunction(double a) {
            this.a = a;
        }

        @Override
        public double value(double x) {
            return Math.pow(x, 3) + a;
        }

        @Override
        public double derivative(double x) {
            return 3 * Math.pow(x, 2);
        }
    }

    static class QuadraticFunction implements UnivariateFunction {
        private final double a;

        public QuadraticFunction(double a) {
            this.a = a;
        }

        @Override
        public double value(double x) {
            return Math.pow(x, 2) + a;
        }

        @Override
        public double derivative(double x) {
            return 2 * x;
        }
    }

    static class SinFunction implements UnivariateFunction {
        @Override
        public double value(double x) {
            return Math.sin(x);
        }

        @Override
        public double derivative(double x) {
            return Math.cos(x);
        }
    }

    static class AbsFunction implements UnivariateFunction {
        @Override
        public double value(double x) {
            return Math.abs(x);
        }

        @Override
        public double derivative(double x) {
            return Math.signum(x);
        }
    }

    static class CustomFunction implements UnivariateFunction {
        @Override
        public double value(double x) {
            throw new UnsupportedOperationException("Custom function not supported.");
        }

        @Override
        public double derivative(double x) {
            throw new UnsupportedOperationException("Custom function not supported.");
        }
    }

    static class LogFunction implements UnivariateFunction {
        @Override
        public double value(double x) {
            return Math.log(x);
        }

        @Override
        public double derivative(double x) {
            return 1.0 / x;
        }
    }

    static class RootFunction implements UnivariateFunction {
        private final Function function;

        public RootFunction(Function function) {
            this.function = function;
        }

        @Override
        public double value(double x) {
            return function.value(Math.pow(x, 2)) - x;
        }

        @Override
        public double derivative(double x) {
            double fx = function.value(Math.pow(x, 2));
            return 2 * x * (function.derivative(Math.pow(x, 2)) - fx / (2 * x));
        }

        private interface Function {
            double value(double x);

            double derivative(double x);
        }
    }
}