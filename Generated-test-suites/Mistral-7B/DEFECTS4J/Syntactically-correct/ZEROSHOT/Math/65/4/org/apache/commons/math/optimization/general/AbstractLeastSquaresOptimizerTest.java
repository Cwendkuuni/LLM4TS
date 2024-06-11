import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.simple.TestFunctions;
import org.apache.commons.math3.optim.univariate.UnivariateTaylorSeriesModel;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AbstractLeastSquaresOptimizerTest {

    private AbstractLeastSquaresOptimizer optimizer;
    private double[] targetValues;
    private double[] weights;
    private double[] startPoint;

    @Before
    public void setUp() {
        optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize()
                    throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
                // Implementation of the optimization algorithm goes here
                fail("doOptimize should not be called in tests");
                return null;
            }
        };
        targetValues = new double[]{1.0, 2.0, 3.0};
        weights = new double[]{1.0, 2.0, 3.0};
        startPoint = new double[]{0.0, 0.0, 0.0};
    }

    @Test
    public void testOptimize_SingleVariable() throws Exception {
        MultivariateFunction function = new TestFunctions.SphereFunction(2);
        PointValuePair result = optimizer.optimize(function.value(startPoint), targetValues, weights, startPoint);
        double expectedCost = Math.sqrt(6.0);
        assertEquals(expectedCost, result.getValue(), 0.001);
        assertArrayEquals(new double[]{1.0, 1.0}, result.getPoint(), 0.001);
    }

    @Test
    public void testOptimize_MultipleVariables() throws Exception {
        MultivariateFunction function = new TestFunctions.RosenbrockFunction(2);
        PointValuePair result = optimizer.optimize(new UnivariateTaylorSeriesModel(function, startPoint[0]), targetValues, weights, startPoint);
        double expectedCost = 1.0;
        assertEquals(expectedCost, result.getValue(), 0.001);
        assertArrayEquals(new double[]{1.0, 1.0}, result.getPoint(), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptimize_IncorrectStartPointDimension() throws Exception {
        MultivariateFunction function = new TestFunctions.SphereFunction(3);
        optimizer.optimize(function.value(startPoint), targetValues, weights, new double[]{0.0, 0.0});
    }

}