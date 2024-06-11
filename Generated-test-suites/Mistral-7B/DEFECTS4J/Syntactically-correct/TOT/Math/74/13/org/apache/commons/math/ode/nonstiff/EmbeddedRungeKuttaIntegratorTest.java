package org.apache.commons.math.ode.nonstiff;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.When;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.step.RungeKuttaStepHandler;
import org.apache.commons.math3.ode.step.RungeKuttaStepInterpolator;
import org.apache.commons.math3.special.BesselFunctions;
import org.junit.Test;

/**
 * Test case for {@link EmbeddedRungeKuttaIntegrator}.
 */
public class EmbeddedRungeKuttaIntegratorTest {

    /**
     * Tests the EmbeddedRungeKuttaIntegrator with a simple ODE.
     */
    @Test
    public void testSimpleODE() {
        final double[][] a = { { 0.0, 0.0, 0.5, 0.5 }, { 0.0, 0.5, 0.5, 0.0 } };
        final double[] b = { 0.5, 0.5 };
        final double minStep = 0.01;
        final double maxStep = 1.0;
        final double scalAbsoluteTolerance = 1e-9;
        final double scalRelativeTolerance = 1e-6;

        final EmbeddedRungeKuttaIntegrator integrator =
            new EmbeddedRungeKutta54Integrator(
                "Embedded Runge-Kutta method of order 5(4)", true, a, b,
                new RungeKuttaStepInterpolator(), minStep, maxStep,
                scalAbsoluteTolerance, scalRelativeTolerance);

        final double t0 = 0.0;
        final double y0[] = { 1.0, 0.0 };
        final double[] y = new double[2];

        final UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double t) {
                double y1 = y0[1];
                return y1 * (1.0 - y1);
            }
        };

        final FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 2;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = y[1];
                yPrime[1] = -f.value(t) - y[1] * y[1];
            }
        };

        // Integration of a simple ODE from t0 = 0 to tFinal = 1
        double tFinal = 1.0;
        integrator.integrate(equations, t0, y0, tFinal, y);
        assertArrayEquals(y0, y0); // check initial conditions
        assertEquals(tFinal, tFinal, 1e-12); // check final time

        // Test a step handler
        final StepHandler stepHandler = new RungeKuttaStepHandler() {
            @Override
            public void handleStep(double time, int step, double[] y, double[] yDot) {
                System.out.println(String.format(
                        "time = %10.6f, step = %2d, y = %s, yDot = %s",
                        time, step, java.util.Arrays.toString(y),
                        java.util.Arrays.toString(yDot)));
            }
        };
        integrator.addStepHandler(stepHandler);
        integrator.integrate(equations, t0, y0, tFinal, y);

        // Test an event handler
        final Event event = new When(1.0);
        final StepHandler eventHandler = new RungeKuttaStepHandler() {
            @Override
            public void handleStep(double time, int step, double[] y, double[] yDot) {
                System.out.println("Event triggered at time = " + time);
            }
        };
        integrator.setEventsManager(new CombinedEventsManager(eventHandler));
        integrator.integrate(equations, t0, y0, 2.0, y);

    }

    /**
     * Tests the EmbeddedRungeKuttaIntegrator with a simple ODE with a singular point.
     */
    @Test
    public void testSingularODE() {
        final double[][] a = { { 0.0, 0.0, 0.5, 0.5 }, { 0.0, 0.5, 0.5, 0.0 } };
        final double[] b = { 0.5, 0.5 };
        final double minStep = 0.01;
        final double maxStep = 1.0;
        final double scalAbsoluteTolerance = 1e-9;
        final double scalRelativeTolerance = 1e-6;

        final EmbeddedRungeKuttaIntegrator integrator =
            new EmbeddedRungeKutta54Integrator(
                "Embedded Runge-Kutta method of order 5(4)", true, a, b,
                new RungeKuttaStepInterpolator(), minStep, maxStep,
                scalAbsoluteTolerance, scalRelativeTolerance);

        final double t0 = 0.0;
        final double y0[] = { 1.0, 0.0 };
        final double[] y = new double[2];

        final UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double t) {
                double y1 = y0[1];
                return y1 * (1.0 - y1) / (y1 * y1 + 1.0);
            }
        };

        final FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 2;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = y[1];
                yPrime[1] = -f.value(t) - y[1] * y[1];
            }
        };

        // Integration of a simple ODE from t0 = 0 to tFinal = 1
        double tFinal = 1.0;
        integrator.integrate(equations, t0, y0, tFinal, y);

        // Test a singular point
        final double tSingular = Math.PI;
        integrator.reset();
        integrator.integrate(equations, tSingular - 0.1, y0, tSingular, y);
        assertEquals(BesselFunctions.besselJ0(1.0, tSingular), y[0],
                     1e-9);
        assertEquals(0.0, y[1], 1e-9);

        integrator.reset();
        integrator.integrate(equations, tSingular, y0, tSingular + 0.1, y);
        assertEquals(BesselFunctions.besselJ0(1.0, tSingular), y[0],
                     1e-9);
        assertEquals(0.0, y[1], 1e-9);

    }

}