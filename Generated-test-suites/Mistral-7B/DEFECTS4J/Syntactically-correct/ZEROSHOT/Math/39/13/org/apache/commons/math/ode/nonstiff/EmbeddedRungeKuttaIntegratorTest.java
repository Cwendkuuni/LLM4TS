import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testEmbeddedRungeKuttaIntegrator() {
        double[] c = {0.0, 0.3, 0.6, 1.0};
        double[][] a = {
                {0.0, 0.0, 0.0, 0.0},
                {0.3, 0.47360957, 0.0, 0.3},
                {0.6, 0.38233215, 0.618, 0.3},
                {1.0, 0.0, 0.83132528, 0.6}
        };
        double[] b = {0.0, 0.23819423, 0.68160246, 0.83132528};
        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(1, c.length + 1);
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("test", false, c, a, b, prototype, 0.01, 0.01) {
            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                double error = 0;
                for (int i = 0; i < y0.length; i++) {
                    error += Precision.pow(FastMath.abs(y1[i] - y0[i]), 2);
                }
                return Math.sqrt(error);
            }
        };
        ExpandableStatefulODE equations = new ExpandableStatefulODE() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public int getOrder() {
                return integrator.getOrder();
            }

            @Override
            public void initialize(double t, double[] y, double[] yDot) {
                y[0] = 0.0;
                yDot[0] = 0.0;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = 1.0;
            }
        };

        integrator.integrate(equations, 1.0);
        assertEquals(1.0, equations.getTime(), 0.001);
        assertTrue(Precision.abs(equations.getCompleteState()[0] - 1.0) < 1e-8);

        integrator = new EmbeddedRungeKuttaIntegrator("test", false, c, a, b, prototype, 0.01, 0.01, new double[]{1e-8, 1e-8}, new double[]{1e-8, 1e-8});
        integrator.integrate(equations, 1.0);
        assertEquals(1.0, equations.getTime(), 0.001);
        assertTrue(Precision.abs(equations.getCompleteState()[0] - 1.0) < 1e-12);

        integrator = new EmbeddedRungeKuttaIntegrator("test", true, c, a, b, prototype, 0.01, 0.01) {
            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                double error = 0;
                for (int i = 0; i < y0.length; i++) {
                    error += Precision.pow(FastMath.abs(y1[i] - y0[i]), 2);
                }
                return Math.sqrt(error);
            }
        };
        integrator.integrate(equations, 1.0);
        assertEquals(1.0, equations.getTime(), 0.001);
        assertTrue(Precision.abs(equations.getCompleteState()[0] - 1.0) < 1e-8);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmbeddedRungeKuttaIntegratorInvalidArguments() {
        double[] c = {0.0, 0.3};
        double[][] a = {
                {0.0, 0.0},
                {0.3, 0.47360957}
        };
        double[] b = {0.0, 0.23819423};
        new EmbeddedRungeKuttaIntegrator("test", false, c, a, b, null, 0.01, 0.01);
    }

    @Test(expected = IllegalStateException.class)
    public void testEmbeddedRungeKuttaIntegratorInitializationError() {
        double[] c = {0.0, 0.3};
        double[][] a = {
                {0.0, 0.0},
                {0.3, 0.47360957}
        };
        double[] b = {0.0, 0.23819423};
        new EmbeddedRungeKuttaIntegrator("test", false, c, a, b, new RungeKuttaStepInterpolator(1, 2), 0.01, 0.01) {
            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                throw new RuntimeException();
            }
        };
    }
}