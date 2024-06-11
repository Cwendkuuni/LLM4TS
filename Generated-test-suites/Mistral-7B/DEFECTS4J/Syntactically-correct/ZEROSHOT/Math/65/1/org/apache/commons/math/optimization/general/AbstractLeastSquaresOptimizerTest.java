import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.SimpleConstraint;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;

public class AbstractLeastSquaresOptimizerTest {

    private AbstractLeastSquaresOptimizer optimizer;
    private DoubleFunction f;
    private double[] target;
    private double[] weights;
    private double[] startPoint;

    @Before
    public void setUp() {
        optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize()
                throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
                // This class doesn't perform actual optimization, it just returns a dummy PointValuePair
                return new PointValuePair(point, getRMS());
            }
        };

        f = new DoubleFunction("f", 2) {
            @Override
            public double value(double[] x) {
                return x[0] * x[0] + x[1] * x[1];
            }

            @Override
            public double[] gradient(double[] x) {
                return new double[] {2 * x[0], 2 * x[1]};
            }
        };

        target = new double[] {1.0, 1.0};
        weights = new double[] {1.0, 1.0};
        startPoint = new double[] {2.0, 2.0};
    }

    @After
    public void tearDown() {
        optimizer = null;
        f = null;
        target = null;
        weights = null;
        startPoint = null;
    }

    @Test
    public void testOptimize() throws Exception {
        optimizer.optimize(f, target, weights, startPoint);
        double rms = optimizer.getRMS();
        assertEquals(Math.sqrt(2.0), rms, 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeBadDimensions() throws Exception {
        optimizer.optimize(f, weights, startPoint);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeBadTargetDimensions() throws Exception {
        optimizer.optimize(f, target, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeBadStartPointDimensions() throws Exception {
        optimizer.optimize(f, target, weights, new double[] {3.0});
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testOptimizeFunctionWrapperException() throws Exception {
        DifferentiableMultivariateVectorialFunction function = new DifferentiableMultivariateVectorialFunction() {
            @Override
            public RealVector value(RealVector x)
                throws FunctionEvaluationException {
                throw new FunctionEvaluationException(x, new RuntimeException("Function evaluation failed"));
            }

            @Override
            public RealMatrix jacobianValue(RealVector x)
                throws FunctionEvaluationException {
                throw new FunctionEvaluationException(x, new RuntimeException("Function evaluation failed"));
            }
        };
        optimizer.optimize(function, target, weights, startPoint);
    }

    @Test(expected = OptimizationException.class)
    public void testOptimizeMaxIterationsExceeded() throws Exception {
        optimizer.setMaxIterations(0);
        optimizer.optimize(f, target, weights, startPoint);
    }

    @Test(expected = OptimizationException.class)
    public void testOptimizeMaxEvaluationsExceeded() throws Exception {
        optimizer.setMaxEvaluations(0);
        optimizer.optimize(f, target, weights, startPoint);
    }

    @Test
    public void testGetRMS() throws Exception {
        optimizer.optimize(f, target, weights, startPoint);
        double rms = optimizer.getRMS();
        assertEquals(Math.sqrt(2.0), rms, 0.001);
    }

    @Test
    public void testGetChiSquare() throws Exception {
        optimizer.optimize(f, target, weights, startPoint);
        double chiSquare = optimizer.getChiSquare();
        assertEquals(4.0, chiSquare, 0.001);
    }

    @Test
    public void testGuessParametersErrors() throws Exception {
        optimizer.optimize(f, target, weights, startPoint);
        double[] errors = optimizer.guessParametersErrors();
        assertEquals(2.0, errors[0], 0.01);
        assertEquals(2.0, errors[1], 0.01);
    }
}

class DoubleFunction implements MultivariateFunction {
    private String name;
    private int numVariables;

    public DoubleFunction(String name, int numVariables) {
        this.name = name;
        this.numVariables = numVariables;
    }

    @Override
    public double value(double[] x) {
        // implementation of the function goes here
    }

    @Override
    public double[] gradient(double[] x) {
        // implementation of the gradient goes here
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getNumberOfVariables() {
        return numVariables;
    }
}