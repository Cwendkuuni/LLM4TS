import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.noCLUDINGinterpolators.ClassicRK4Integrator;
import org.apache.commons.math3.ode.noCLUDpropagators.EulerPropagator;
import org.apache.commons.math3.ode.sampling.StepHandlerAdapter;
import org.apache.commons.math3.ode.sampling.StepHandlerList;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test class for {@link EmbeddedRungeKuttaIntegrator}.
 */
public class EmbeddedRungeKuttaIntegratorTest {

    /**
     * Test method for {@link EmbeddedRungeKuttaIntegrator#EmbeddedRungeKuttaIntegrator(String, boolean, double[], double[][], double[], RungeKuttaStepInterpolator, double, double, double[], double[])}.
     */
    @Test
    public void testEmbeddedRungeKuttaIntegrator() {
        final double[][] a = new double[][]{{0.0, 0.5, 0.5}, {0.0, 0.5, 0.5, 1.0}, {0.0, 1.0, 0.5, 0.5, 1.0}};
        final double[] c = new double[]{0.5, 0.5, 1.0};
        final double[] b = new double[]{1.0 / 6.0, 1.2 / 6.0, 1.0 / 2.0};

        final EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("Test", false, c, a, b,
                new ClassicRK4Integrator(), 1e-10, 1e-10, null, null) {
            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                return Precision.abs(Precision.sub(y1, y0)[0]) / (2.0 * h);
            }
        };

        final ExpandableStatefulODE equations = new ExpandableStatefulODE() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public int getStateDimension() {
                return getDimension();
            }

            @Override
            public int getPrimaryDimension() {
                return getDimension();
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[0] + 0.5 * y[0];
            }

            @Override
            public void initialize(double t0, double[] y0) {
                setTime(t0);
                setCompleteState(y0);
            }
        };

        final double tFinal = 10.0;

        final StepHandlerList stepHandlers = new StepHandlerList();
        stepHandlers.addHandler(new StepHandlerAdapter() {
            @Override
            public void handleStep(double t, double[] y, double[] yDot) {
                System.out.println("t = " + t + ", y = " + y[0]);
            }
        });

        integrator.integrate(equations, tFinal, stepHandlers);

        // check the final state
        final double[] y = equations.getCompleteState();
        assertEquals(Precision.round(3.0 * Math.exp(1.5) - 0.5, 4), y[0], 1e-7);

        // check intermediate states
        final double[] yInt = new double[1];
        final double[] yDotInt = new double[1];
        equations.setTime(equations.getTime() - c[0]);
        integrator.integrate(equations, 0.0, null, yInt, yDotInt);
        assertEquals(Precision.round(1.0, 4), yInt[0], 1e-7);
        assertEquals(Precision.round(1.5, 4), yDotInt[0], 1e-7);

        equations.setTime(equations.getTime() + c[1]);
        integrator.integrate(equations, 0.0, null, yInt, yDotInt);
        assertEquals(Precision.round(2.9166666666666665, 14), yInt[0], 1e-12);
        assertEquals(Precision.round(3.434949494949495, 14), yDotInt[0], 1e-12);

        equations.setTime(equations.getTime() + c[2]);
        integrator.integrate(equations, 0.0, null, yInt, yDotInt);
        assertEquals(Precision.round(5.899009787398816, 14), yInt[0], 1e-12);
        assertEquals(Precision.round(6.666666666666666, 14), yDotInt[0], 1e-12);
    }

    /**
     * Test method for {@link EmbeddedRungeKuttaIntegrator#EmbeddedRungeKuttaIntegrator(String, boolean, double[], double[][], double[], RungeKuttaStepInterpolator, double, double, double[], double[])}.
     */
    @Test
    public void testEmbeddedRungeKuttaIntegratorWithRelativeTolerance() {
        final double[][] a = new double[][]{{0.0, 0.5, 0.5}, {0.0, 0.5, 0.5, 1.0}, {0.0, 1.0, 0.5, 0.5, 1.0}};
        final double[] c = new double[]{0.5, 0.5, 1.0};
        final double[] b = new double[]{1.0 / 6.0, 1.2 / 6.0, 1.0 / 2.0};

        final double[] relTolerance = new double[]{1e-3, 1e-5};

        final EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("Test", false, c, a, b,
                new EulerPropagator(), 0.1, 0.1, relTolerance, null) {
            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                return Precision.abs(Precision.sub(y1, y0)[0]) / h;
            }
        };

        final ExpandableStatefulODE equations = new ExpandableStatefulODE() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public int getStateDimension() {
                return getDimension();
            }

            @Override
            public int getPrimaryDimension() {
                return getDimension();
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = Math.sin(y[0]);
            }

            @Override
            public void initialize(double t0, double[] y0) {
                setTime(t0);
                setCompleteState(y0);
            }
        };

        final double tFinal = 2 * Math.PI;

        integrator.integrate(equations, tFinal);

        // check the final state
        final double[] y = equations.getCompleteState();
        assertEquals(Precision.round(Math.sin(Precision.round(2 * Math.PI, 4)), 4), y[0], 1e-7);

        // check intermediate states
        final double[] yInt = new double[1];
        final double[] yDotInt = new double[1];
        equations.setTime(equations.getTime() - c[0]);
        integrator.integrate(equations, 0.0, null, yInt, yDotInt);
        assertEquals(Precision.round(Math.sin(Precision.round(Math.PI, 4)), 4), yInt[0], 1e-7);

        equations.setTime(equations.getTime() + c[1]);
        integrator.integrate(equations, 0.0, null, yInt, yDotInt);
        assertEquals(Precision.round(Math.sin(Precision.round(Math.PI + Math.PI / 2, 4)), 4), yInt[0], 1e-7);

        equations.setTime(equations.getTime() + c[2]);
        integrator.integrate(equations, 0.0, null, yInt, yDotInt);
        assertEquals(Precision.round(Math.sin(Precision.round(Math.PI + Math.PI, 4)), 4), yInt[0], 1e-7);

    }

}