import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.optim.BaseUnivariateRealOptimizer;
import org.apache.commons.math3.optim.ConvergenceChecker;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateRealPointValuePair;
import org.apache.commons.math3.random.RandomGenerator;
import org.junit.Assert;
import org.junit.Test;

public class MultiStartUnivariateRealOptimizerTest {

    @Test
    public void testMultiStartUnivariateRealOptimizer() throws Exception {
        // Test function
        UnivariateRealFunction testFunction = new TestUnivariateRealFunction();

        // Test with a random optimizer
        BaseUnivariateRealOptimizer optimizer = new FibonacciOptimizer();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 5, new MersenneTwisterFastRandom());

        // Test optimization
        GoalType goalType = GoalType.MINIMIZE;
        double start = 0.0;
        double min = Double.POSITIVE_INFINITY;
        double max = Double.NEGATIVE_INFINITY;
        UnivariateRealPointValuePair result = multiStartOptimizer.optimize(testFunction, goalType, start, min, max);
        Assert.assertNotNull(result);
        Assert.assertNotNull(result.getPoint());
        Assert.assertTrue(result.getValue() >= testFunction.value(result.getPoint()));

        // Test getOptima
        UnivariateRealPointValuePair[] optima = multiStartOptimizer.getOptima();
        Assert.assertNotNull(optima);
        Assert.assertEquals(5, optima.length);
        for (UnivariateRealPointValuePair pair : optima) {
            Assert.assertNotNull(pair);
            Assert.assertNotNull(pair.getPoint());
            Assert.assertTrue(pair.getValue() >= testFunction.value(pair.getPoint()));
        }

        // Test maxEvaluations
        maxEvaluationsTest(multiStartOptimizer, testFunction, goalType, start, min, max, 100);

        // Test with a specific optimizer
        optimizer = new GoldenSectionFibonacciOptimizer();
        multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 5, new MersenneTwisterFastRandom());
        result = multiStartOptimizer.optimize(testFunction, goalType, start, min, max);
        Assert.assertNotNull(result);
        Assert.assertNotNull(result.getPoint());
        Assert.assertTrue(result.getValue() >= testFunction.value(result.getPoint()));

        optima = multiStartOptimizer.getOptima();
        Assert.assertNotNull(optima);
        Assert.assertEquals(5, optima.length);
        for (UnivariateRealPointValuePair pair : optima) {
            Assert.assertNotNull(pair);
            Assert.assertNotNull(pair.getPoint());
            Assert.assertTrue(pair.getValue() >= testFunction.value(pair.getPoint()));
        }

        maxEvaluationsTest(multiStartOptimizer, testFunction, goalType, start, min, max, 100);
    }

    private void maxEvaluationsTest(MultiStartUnivariateRealOptimizer optimizer,
                                   UnivariateRealFunction testFunction,
                                   GoalType goalType,
                                   double start,
                                   double min,
                                   double max,
                                   int maxEvaluations) throws Exception {
        optimizer.setMaxEvaluations(maxEvaluations);
        UnivariateRealPointValuePair result = optimizer.optimize(testFunction, goalType, start, min, max);
        Assert.assertNotNull(result);
        Assert.assertNotNull(result.getPoint());
        Assert.assertTrue(result.getValue() >= testFunction.value(result.getPoint()));

        UnivariateRealPointValuePair[] optima = optimizer.getOptima();
        Assert.assertNotNull(optima);
        Assert.assertEquals(optimizer.getStarts(), optima.length);
        int totalEvaluations = optimizer.getEvaluations();
        Assert.assertTrue(totalEvaluations <= maxEvaluations);

        for (UnivariateRealPointValuePair pair : optima) {
            Assert.assertNotNull(pair);
            Assert.assertNotNull(pair.getPoint());
            Assert.assertTrue(pair.getValue() >= testFunction.value(pair.getPoint()));
        }
    }
}

class TestUnivariateRealFunction implements UnivariateRealFunction {
    @Override
    public double value(double x) {
        return Math.pow(x, 2) - 2.0 * x + 1.0;
    }

    @Override
    public double derivative(double x) {
        return 2.0 * x - 2.0;
    }
}