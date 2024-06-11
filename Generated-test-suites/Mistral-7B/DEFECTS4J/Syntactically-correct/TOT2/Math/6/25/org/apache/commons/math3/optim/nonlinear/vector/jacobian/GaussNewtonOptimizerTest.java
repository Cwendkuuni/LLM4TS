import org.apache.commons.math3.*;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.nonlinear.functions.*;
import org.apache.commons.math3.optim.nonlinear.gradient.*;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class GaussNewtonOptimizerTest {

    @Test
    public void testGaussNewtonOptimizerWithSimpleFunction() {
        Function f = new QuadraticFunction(new double[]{-1.0, 2.0}, new double[]{4.0, 8.0});
        OptimizationProblem problem = new OptimizationProblem(f, new PointValuePair(new double[]{0.0, 0.0}));

        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker(1e-6, 1e-6));

        PointValuePair solution = optimizer.optimize(problem);

        assertArrayEquals(new double[]{1.0, 1.0}, solution.getPoint(), 1e-6);
        assertEquals(0.0, solution.getValue(), 1e-6);
    }

    @Test(expected = NullArgumentException.class)
    public void testNullArgumentExceptionForNullFunction() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker(1e-6, 1e-6));
        optimizer.optimize(null);
    }

    @Test(expected = NullArgumentException.class)
    public void testNullArgumentExceptionForNullStartPoint() {
        Function f = new QuadraticFunction(new double[]{-1.0, 2.0}, new double[]{4.0, 8.0});
        OptimizationProblem problem = new OptimizationProblem(f, null);

        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker(1e-6, 1e-6));

        optimizer.optimize(problem);
    }

    @Test(expected = MathInternalError.class)
    public void testMathInternalErrorForSingularProblem() {
        Function f = new SingularFunction();
        OptimizationProblem problem = new OptimizationProblem(f, new PointValuePair(new double[]{0.0, 0.0}));

        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker(1e-6, 1e-6));

        optimizer.optimize(problem);
    }

    @Test
    public void testGaussNewtonOptimizerWithLargeScaleFunction() {
        Function f = new LargeScaleFunction();
        OptimizationProblem problem = new OptimizationProblem(f, new PointValuePair(new double[]{0.0, 0.0, 0.0}));

        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker(1e-6, 1e-6));

        PointValuePair solution = optimizer.optimize(problem);

        assertArrayEquals(new double[]{0.0, 0.0, 0.0}, solution.getPoint(), 1e-6);
        assertEquals(0.0, solution.getValue(), 1e-6);
    }

    @Test
    public void testGaussNewtonOptimizerWithQRDecomposition() {
        Function f = new QuadraticFunction(new double[]{-1.0, 2.0}, new double[]{4.0, 8.0});
        OptimizationProblem problem = new OptimizationProblem(f, new PointValuePair(new double[]{0.0, 0.0}));

        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false, new SimpleVectorValueChecker(1e-6, 1e-6));

        PointValuePair solution = optimizer.optimize(problem);

        assertArrayEquals(new double[]{1.0, 1.0}, solution.getPoint(), 1e-6);
        assertEquals(0.0, solution.getValue(), 1e-6);
    }
}