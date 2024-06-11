import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateRealOptimizer;
import org.apache.commons.math3.optim.goal.GoalType;
import org.apache.commons.math3.random.RandomGenerator;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiStartUnivariateRealOptimizerTest {

    @Test
    public void testMultiStartUnivariateRealOptimizer() throws FunctionEvaluationException, ConvergenceException {
        UnivariateRealFunction function = new MyTestFunction();
        RandomGenerator generator = new org.apache.commons.math3.random.MersenneTwisterFastRandomGenerator();

        MultiStartUnivariateRealOptimizer<MyTestFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(
                new FibonacciSearchUnivariateOptimizer<>(), 3, generator);

        optimizer.setMaxEvaluations(10);

        PointValuePair best = optimizer.optimize(function, GoalType.MINIMIZE, -10.0, 10.0);
        assertNotNull(best);
        assertEquals(-1.2367581420323115, best.getPoint(), 0.001);
        assertEquals(-1.2367581420323115, best.getValue(), 0.001);

        PointValuePair[] allOptima = optimizer.getOptima();
        assertEquals(3, allOptima.length);
        for (PointValuePair pair : allOptima) {
            assertNotNull(pair);
            if (pair != null) {
                assertEquals(-1.2367581420323115, pair.getPoint(), 0.001);
                assertEquals(-1.2367581420323115, pair.getValue(), 0.001);
            }
        }
    }

    @Test (expected = ConvergenceException.class)
    public void testNoConvergence() throws FunctionEvaluationException, ConvergenceException {
        UnivariateRealFunction function = new MyTestFunctionWithNoSolution();
        RandomGenerator generator = new org.apache.commons.math3.random.MersenneTwisterFastRandomGenerator();

        MultiStartUnivariateRealOptimizer<MyTestFunctionWithNoSolution> optimizer = new MultiStartUnivariateRealOptimizer<>(
                new FibonacciSearchUnivariateOptimizer<>(), 3, generator);

        optimizer.setMaxEvaluations(10);

        optimizer.optimize(function, GoalType.MINIMIZE, -10.0, 10.0);
        fail("Should have thrown ConvergenceException");
    }
}

class MyTestFunction implements UnivariateRealFunction {
    @Override
    public double value(double x) {
        return Math.pow(Math.sin(x), 2) + Math.pow(Math.cos(x), 2);
    }

    @Override
    public double derivative(double x) {
        return 2 * (Math.sin(x) * Math.cos(x) + Math.cos(x) * Math.sin(x));
    }
}

class MyTestFunctionWithNoSolution implements UnivariateRealFunction {
    @Override
    public double value(double x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public double derivative(double x) {
        throw new UnsupportedOperationException();
    }
}