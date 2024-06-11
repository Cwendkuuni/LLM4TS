import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.junit.Test;
import static org.junit.Assert.*;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testEmbeddedRungeKuttaIntegrator() {
        double[][] a = {{-0.3, 0.9, 0.6}, {0, 0.27, 0.45}, {0, 0, 0.55}};
        double[] b = {0.238, 0.678, 0.962};
        double[] c = {0, 0.3, 0.6};
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("MyIntegrator", true, c, a, b,
                new RungeKuttaStepInterpolator(1), 0.001, 0.01, null, null) {
            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                double error = 0;
                for (int i = 0; i < y0.length; i++) {
                    error += Math.pow(y1[i] - y0[i], 2);
                }
                return Math.sqrt(error);
            }
        };

        ExpandableStatefulODE equations = new ExpandableStatefulODE("MyODE") {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public int getStateDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double time, double[] y, double[] yDot) {
                yDot[0] = Math.sin(y[0]);
            }
        };

        integrator.integrate(equations, 0.0);
        assertEquals(0.0, equations.getCompleteState()[0], 1e-8);

        integrator.integrate(equations, 2.0);
        double[] y = equations.getCompleteState();
        double expected = 1.1071487177960169935;
        assertEquals(expected, y[0], 1e-6);

        integrator.integrate(equations, 4.0);
        expected = 2.0416925409584582215;
        assertEquals(expected, y[0], 1e-6);

        integrator.integrate(equations, 6.0);
        expected = 3.1313706376238608398;
        assertEquals(expected, y[0], 1e-6);

        integrator.integrate(equations, 8.0);
        expected = 4.2212403155897363727;
        assertEquals(expected, y[0], 1e-6);

        integrator.integrate(equations, 10.0);
        expected = 5.3112963925533166023;
        assertEquals(expected, y[0], 1e-6);
    }
}