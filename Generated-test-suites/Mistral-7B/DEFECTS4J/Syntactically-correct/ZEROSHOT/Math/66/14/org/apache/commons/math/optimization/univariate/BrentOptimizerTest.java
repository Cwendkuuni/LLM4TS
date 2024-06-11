import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxIterationsExceededException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.junit.Assert;
import org.junit.Test;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizer() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2) + Math.pow(Math.cos(x), 2);
            }
        };

        GoalType goalType = GoalType.MINIMIZE;
        double min = -3.0;
        double max = 3.0;
        double initialGuess = 0.0;

        try {
            PointValuePair result = new BrentOptimizer().optimize(function, goalType, min, max, initialGuess);
            double optimum = result.getPoint();
            double objectiveValue = result.getValue();

            Assert.assertEquals("The minimum should be approximately -1.0316", -1.0316, objectiveValue, 0.0001);
            Assert.assertEquals("The optimum should be approximately -1.2759", -1.2759, optimum, 0.0001);
        } catch (MaxIterationsExceededException e) {
            // Maximum iterations exceeded is an expected exception
        } catch (Exception e) {
            throw new RuntimeException("Unexpected exception", e);
        }
    }
}