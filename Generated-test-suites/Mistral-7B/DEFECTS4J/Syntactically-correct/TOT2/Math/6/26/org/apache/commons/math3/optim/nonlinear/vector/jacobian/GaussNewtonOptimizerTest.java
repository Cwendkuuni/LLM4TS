import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleVectorValueChecker;
import org.apache.commons.math3.optim.UnconstrainedOptimizationTest;
import org.apache.commons.math3.optimization.leastsq.GaussNewtonOptimizer;
import org.apache.commons.math3.optimization.nonlinear.function.LinearFunction;
import org.apache.commons.math3.optimization.nonlinear.function.PolynomialFunction;
import org.apache.commons.math3.optimization.nonlinear.functions.GriewankFunction;
import org.apache.commons.math3.optimization.nonlinear.functions.RosenbrockFunction;
import org.apache.commons.math3.optimization.nonlinear.functions.RosenbrockFunction2;
import org.apache.commons.math3.optimization.nonlinear.functions.SphereFunction;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class GaussNewtonOptimizerTest extends UnconstrainedOptimizationTest {

    private GaussNewtonOptimizer optimizer;

    @Before
    public void setUp() {
        optimizer = new GaussNewtonOptimizer(new SimpleVectorValueChecker(1e-8));
    }

    @Test
    public void testGaussNewtonOptimizer_LinearFunction() {
        LinearFunction f = new LinearFunction(new double[]{-1.0, -1.0}, new double[]{1.0, 1.0});
        PointValuePair solution = optimizer.optimize(f);
        Assert.assertEquals(new double[]{0.0, 0.0}, solution.getPoint(), 1e-9);
    }

    @Test
    public void testGaussNewtonOptimizer_GriewankFunction() {
        FunctionsTest.checkGradient(new GriewankFunction(), 1e-4, 1e-4);
        PointValuePair solution = optimizer.optimize(new GriewankFunction());
        Assert.assertEquals(new double[]{0.0, 0.0}, solution.getPoint(), 1e-6);
    }

    @Test
    public void testGaussNewtonOptimizer_RosenbrockFunction() {
        FunctionsTest.checkGradient(new RosenbrockFunction(), 1e-4, 1e-4);
        PointValuePair solution = optimizer.optimize(new RosenbrockFunction());
        Assert.assertEquals(new double[]{1.0, 1.0}, solution.getPoint(), 1e-6);
    }

    @Test
    public void testGaussNewtonOptimizer_RosenbrockFunction2() {
        FunctionsTest.checkGradient(new RosenbrockFunction2(), 1e-4, 1e-4);
        PointValuePair solution = optimizer.optimize(new RosenbrockFunction2());
        Assert.assertEquals(new double[]{1.0, 1.0, 1.0, 1.0}, solution.getPoint(), 1e-6);
    }

    @Test
    public void testGaussNewtonOptimizer_SphereFunction() {
        FunctionsTest.checkGradient(new SphereFunction(), 1e-4, 1e-4);
        PointValuePair solution = optimizer.optimize(new SphereFunction());
        Assert.assertEquals(new double[]{0.0, 0.0}, solution.getPoint(), 1e-6);
    }

    @Test(expected = NullArgumentException.class)
    public void testGaussNewtonOptimizer_NullStartPoint() {
        optimizer.optimize(null);
    }

    @Test(expected = NullArgumentException.class)
    public void testGaussNewtonOptimizer_NullTarget() {
        optimizer.optimize(new LinearFunction(null, null));
    }

    @Test(expected = NullArgumentException.class)
    public void testGaussNewtonOptimizer_NullWeight() {
        optimizer.optimize(new LinearFunction(new double[]{-1.0, -1.0}, null));
    }

    @Test(expected = ConvergenceException.class)
    public void testGaussNewtonOptimizer_Unconverged() {
        optimizer = new GaussNewtonOptimizer(null);
        optimizer.optimize(new LinearFunction(new double[]{-1.0, -1.0}, new double[]{1.0, 1.0}));
    }
}