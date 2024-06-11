import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.sampling.*;
import org.junit.Assert;
import org.junit.Test;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testSimpleIntegration() {
        double[] c = {0.3, 0.4, 0.5};
        double[][] a = {{-0.0, 0.44, 0.0, 0.6}, {-0.6, -0.27, 0.6, -0.0}, {0.0, 0.27, -0.8, 0.0}, {0.6, 0.0, 0.27, -0.4}};
        double[] b = {0.0, 0.2, -0.2, 0.0};
        RungeKuttaStepInterpolator prototype = new RungeKutta23FehlbergIntegrator();
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRk45Integrator("RK45", true, c, a, b, prototype, 0.01, 0.01, null, null);

        ExpandableStatefulODE equations = new ExpandableODEFunctionAdapter() {
            @Override
            public int getDimension() {
                return 4;
            }

            @Override
            public void calculateStateDerivatives(double[] y, double t, double[] yDot) {
                yDot[0] = y[1];
                yDot[1] = y[2];
                yDot[2] = y[3];
                yDot[3] = 0.0;
            }
        };

        integrator.integrate(equations, 10.0);
        double[] expected = {1.0, 1.0, 1.0, 0.0};
        Assert.assertArrayEquals(expected, equations.getCompleteState(), 0.001);
    }

    @Test
    public void testErrorInInitialCondition() {
        double[] c = {0.3, 0.4, 0.5};
        double[][] a = {{-0.0, 0.44, 0.0, 0.6}, {-0.6, -0.27, 0.6, -0.0}, {0.0, 0.27, -0.8, 0.0}, {0.6, 0.0, 0.27, -0.4}};
        double[] b = {0.0, 0.2, -0.2, 0.0};
        RungeKuttaStepInterpolator prototype = new RungeKutta23FehlbergIntegrator();
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRk45Integrator("RK45", true, c, a, b, prototype, 0.01, 0.01, null, null);

        ExpandableStatefulODE equations = new ExpandableODEFunctionAdapter() {
            @Override
            public int getDimension() {
                return 4;
            }

            @Override
            public void calculateStateDerivatives(double[] y, double t, double[] yDot) {
                yDot[0] = y[1];
                yDot[1] = y[2];
                yDot[2] = y[3];
                yDot[3] = 0.0;
            }
        };

        equations.setCompleteState(new double[]{1.0, 1.0, 1.0, Double.NaN});
        try {
            integrator.integrate(equations, 10.0);
            Assert.fail("Expected NumberIsTooSmallException or DimensionMismatchException");
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NumberIsTooSmallException || e instanceof DimensionMismatchException);
        }
    }

    @Test
    public void testErrorInTime() {
        double[] c = {0.3, 0.4, 0.5};
        double[][] a = {{-0.0, 0.44, 0.0, 0.6}, {-0.6, -0.27, 0.6, -0.0}, {0.0, 0.27, -0.8, 0.0}, {0.6, 0.0, 0.27, -0.4}};
        double[] b = {0.0, 0.2, -0.2, 0.0};
        RungeKuttaStepInterpolator prototype = new RungeKutta23FehlbergIntegrator();
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRk45Integrator("RK45", true, c, a, b, prototype, 0.01, 0.01, null, null);

        ExpandableStatefulODE equations = new ExpandableODEFunctionAdapter() {
            @Override
            public int getDimension() {
                return 4;
            }

            @Override
            public void calculateStateDerivatives(double[] y, double t, double[] yDot) {
                yDot[0] = y[1];
                yDot[1] = y[2];
                yDot[2] = y[3];
                yDot[3] = 0.0;
            }
        };

        try {
            integrator.integrate(equations, -10.0);
            Assert.fail("Expected NumberIsTooSmallException");
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NumberIsTooSmallException);
        }
    }
}