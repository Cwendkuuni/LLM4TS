import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.initializers.ArrayRealVectorInitializer;
import org.apache.commons.math3.optim.nonlinear.gradient.Gradient;
import org.apache.commons.math3.optim.nonlinear.gradient.LevenbergMarquardtOptimizer;
import org.apache.commons.math3.optim.nonlinear.vector.real.Vector3;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class AbstractLeastSquaresOptimizerTest {

    @Test
    public void testAbstractLeastSquaresOptimizer() throws Exception {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize()
                    throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
                // Your implementation of the Levenberg-Marquardt optimization algorithm goes here
                // For the purpose of testing, we'll use a simple gradient descent algorithm instead
                LevenbergMarquardtOptimizer lmo = new LevenbergMarquardtOptimizer(0.001);
                MultivariateFunction f = new MultivariateFunction() {
                    @Override
                    public double value(double[] x) {
                        // Your objective function implementation goes here
                        return x[0] * x[0] + x[1] * x[1] - 2 * (x[0] + x[1]);
                    }

                    @Override
                    public Gradient gradient(double[] x) {
                        // Your objective function gradient implementation goes here
                        double[] grad = new double[2];
                        grad[0] = 2 * x[0] - 2;
                        grad[1] = 2 * x[1] - 2;
                        return new Gradient() {
                            @Override
                            public double[] values() {
                                return grad;
                            }
                        };
                    }
                };

                PointValuePair result = lmo.optimize(f, new ArrayRealVectorInitializer(new double[]{1.0, 1.0}), new SimpleBounds(new double[]{-10.0, 10.0}, new double[]{10.0, 10.0}));
                Vector3 x = (Vector3) result.getPoint();
                double fval = result.getValue();

                // Your test cases go here
                Assert.assertEquals(0.0, x.getEntry(0) - (-1.0), 1e-6);
                Assert.assertEquals(0.0, x.getEntry(1) - (-1.0), 1e-6);
                Assert.assertEquals(0.0, fval, 1e-6);

                return new PointValuePair(x.toArray(), fval);
            }
        };

        // Your test cases go here
        Double[] testValues = new Double[]{0.0, 0.0};
        Double[] testWeights = new Double[]{1.0, 1.0};
        Double[] testStartPoint = new Double[]{1.0, 1.0};
        PointValuePair result = optimizer.optimize(new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                // Your objective function implementation goes here
                return testValues[0] * x[0] * x[0] + testValues[1] * x[1] * x[1] - 2 * (x[0] + x[1]);
            }

            @Override
            public Gradient gradient(double[] x) {
                // Your objective function gradient implementation goes here
                double[] grad = new double[2];
                grad[0] = 2 * testValues[0] * x[0] - 2;
                grad[1] = 2 * testValues[1] * x[1] - 2;
                return new Gradient() {
                    @Override
                    public double[] values() {
                        return grad;
                    }
                };
            }
        }, testValues, testWeights, new ArrayRealVectorInitializer(testStartPoint));

        Double[] expectedResult = new Double[]{0.0, 0.0};
        Assert.assertArrayEquals(expectedResult, result.getPoint().toArray(), 1e-6);
        Assert.assertEquals(0.0, result.getValue(), 1e-6);
    }

}