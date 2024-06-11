import org.apache.commons.math3.optim.AbstractOptimizerTest;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleVectorialFunction;
import org.apache.commons.math3.optim.nonlinear.complex.LeastSquaresOptimizationProblem;
import org.apache.commons.math3.optim.nonlinear.vector.linearregression.LinearRegression;
import org.junit.Assert;

public class AbstractLeastSquaresOptimizerTest extends AbstractOptimizerTest {

    @Override
    protected PointValuePair doOptimizeTest(InitialGuess initialGuess, double[] expected) {
        DifferentiableMultivariateVectorialFunction function = new SimpleVectorialFunction(new LinearRegression(2));
        LeastSquaresOptimizationProblem problem = new LeastSquaresOptimizationProblem(function, new double[]{1.0, 2.0}, new double[]{3.0, 4.0});

        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize()
                    throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
                return super.optimize(function, new double[]{1.0, 2.0}, new double[]{1.0, 1.0}, initialGuess.getPoint());
            }
        };

        optimizer.setMaxIterations(100);
        optimizer.optimize(function, new double[]{1.0, 2.0}, new double[]{1.0, 1.0}, initialGuess.getPoint());

        double[] actual = optimizer.getPoint();
        double rms = optimizer.getRMS();

        Assert.assertArrayEquals(expected, actual, 1e-14);
        Assert.assertEquals(0.0, rms, 1e-14);

        return new PointValuePair(optimizer.getPoint(), optimizer.getFunctionValue(optimizer.getPoint()));
    }

    @Override
    protected InitialGuess getInitialGuess() {
        return new InitialGuess(new double[]{0.0, 0.0});
    }

    @Override
    protected double[] getExpectedResult() {
        return new double[]{1.0/3.0, 2.0/3.0};
    }

}