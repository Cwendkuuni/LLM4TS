package org.apache.commons.math.ode.nonstiff;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.Polynomials;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.When;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.sampling.StepHandlerAdapter;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.junit.Test;

/**
 * Test for {@link EmbeddedRungeKuttaIntegrator}.
 */
public class EmbeddedRungeKuttaIntegratorTest {

    /**
     * Test for {@link EmbeddedRungeKuttaIntegrator#EmbeddedRungeKuttaIntegrator(String, boolean, double[], double[][], double[], RungeKuttaStepInterpolator, double, double, double[], double[])}.
     */
    @Test
    public void testFehlberg87() {
        final String name = "Fehlberg87";
        final boolean fsal = false;
        final double[] c = {0.0, 0.3, 0.4, 0.0, 0.0, 0.2, 0.0};
        final double[][] a = {
            {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
            {0.3, 0.48, 0.0, 0.0, 0.0, 0.0, 0.0},
            {0.4, 0.0, 0.55556, 0.0, 0.0, 0.0, 0.0},
            {0.0, 0.0, 0.55556, 0.55556, 0.0, 0.0, 0.0},
            {0.0, 0.449898, 0.499988, 0.412345, 0.0, 0.0, 0.0},
            {0.2, 0.0, 0.35687, 0.689885, 0.35687, 0.0, 0.0},
            {0.0, 0.0, 0.833333, 0.396825, 1.11022, 0.35687, 0.0}
        };
        final double[] b = {1.0 / 3.0, 1.0 / 4.0, 1.0 / 6.0, 1.0 / 8.0, 1.0 / 12.0, 1.0 / 24.0, 1.0 / 48.0};
        final RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(8);
        final double minStep = 1e-12;
        final double maxStep = 1e10;
        final double[] scalAbsoluteTolerance = {1e-12, 1e-12};
        final double[] scalRelativeTolerance = {1e-6, 1e-6};

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator(
                name, fsal, c, a, b, prototype, minStep, maxStep,
                scalAbsoluteTolerance, scalRelativeTolerance);

        UnivariateFunction function = new Polynomials.Quartic(1, 0.1, -0.1, 0.2, 0.1, 0);

        // Test forward integration
        double t0 = 0.0;
        double y0[] = {1.0, 1.0};
        double t1 = 1.0;
        double y1[] = {2.0, 1.5};
        assertEquals(y1, integrator.integrate(function, t0, y0, t1, y1));

        // Test backward integration
        t1 = 1.0;
        y1 = new double[]{2.0, 1.5};
        t0 = 0.0;
        y0 = new double[]{1.0, 1.0};
        assertEquals(y0, integrator.integrate(function, t1, y1, t0, y0));

        // Test integration with events
        t0 = 0.0;
        y0 = new double[]{1.0, 1.0};
        StepHandler handler = new StepHandlerAdapter() {
            private int count = 0;

            @Override
            public void handleStep(StepInterpolator interpolator, int step) {
                if (step % 2 == 0) {
                    assertEquals(interpolator.getY()[0], 2.0, 1e-12);
                    assertEquals(interpolator.getY()[1], 1.5, 1e-12);
                    count++;
                }
            }
        };
        integrator.addStepHandler(handler);
        t1 = 1.0;
        double[] y2 = {2.0, 1.5};
        integrator.integrate(function, t0, y0, t1, y2);
        assertTrue(count >= 2);

        // Test integration with a discontinuous ODE
        final FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 2;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = 1.0;
                if (y[1] > 0.5) {
                    yPrime[1] = -1.0;
                } else {
                    yPrime[1] = 1.0;
                }
            }
        };

        t0 = 0.0;
        y0 = new double[]{1.0, 1.0};
        t1 = 1.0;
        y1 = new double[]{2.0, 1.5};
        assertEquals(y1, integrator.integrate(equations, t0, y0, t1, y1));

        // Test integration with an event at the initial time
        final double eventTime = 0.5;
        final Event event = new When(eventTime, y -> y[1] > 0.5);
        integrator.addEventsManager(new CombinedEventsManager(event));
        integrator.addStepHandler(new StepHandlerAdapter() {
            private int count = 0;

            @Override
            public void handleStep(StepInterpolator interpolator, int step) {
                if (step > 0) {
                    assertEquals(interpolator.getY()[0], 1.0, 1e-12);
                    assertEquals(interpolator.getY()[1], 0.5, 1e-12);
                    count++;
                }
            }
        });
        integrator.integrate(function, t0, y0, t1, y1);
        assertTrue(count > 0);

        // Test integration with an event at the final time
        integrator.clearStepHandlers();
        integrator.clearEventsManager();
        integrator.addStepHandler(new StepHandlerAdapter() {
            private int count = 0;

            @Override
            public void handleStep(StepInterpolator interpolator, int step) {
                if (step > 0) {
                    assertEquals(interpolator.getY()[0], 1.0, 1e-12);
                    assertEquals(interpolator.getY()[1], 1.5, 1e-12);
                    count++;
                }
            }
        });
        integrator.addEventsManager(new CombinedEventsManager(new When(t1, y -> y[1] < 1.5)));
        integrator.integrate(function, t0, y0, t1, y1);
        assertTrue(count > 0);

        integrator.dispose();
    }

}