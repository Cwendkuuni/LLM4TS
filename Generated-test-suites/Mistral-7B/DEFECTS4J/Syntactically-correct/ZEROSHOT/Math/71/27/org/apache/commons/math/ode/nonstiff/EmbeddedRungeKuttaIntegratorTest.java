package org.apache.commons.math.ode.nonstiff.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.BasicIntervalEvent;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.EventsManager;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaMethod;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.special.Besselfunctions;

/**
 * Tests for {@link EmbeddedRungeKuttaIntegrator}.
 */
public class EmbeddedRungeKuttaIntegratorTest {

    /**
     * Test method for {@link EmbeddedRungeKuttaIntegrator#EmbeddedRungeKuttaIntegrator(String, boolean, double[], double[][], double[], RungeKuttaStepInterpolator, double, double, double[], double[])}.
     */
    @org.junit.Test
    public void testEmbeddedRungeKuttaIntegrator_fsal() {
        final double[] y0 = { 1.0 };
        final double[][] a = { { 0.0, 0.5 }, { 0.5, 0.5 } };
        final double[] b = { 0.5, 0.5 };
        final double minStep = 0.0001;
        final double maxStep = 0.1;
        final double scalAbsoluteTolerance = 1e-8;
        final double scalRelativeTolerance = 1e-8;

        final RungeKuttaMethod integrator = new EmbeddedRungeKuttaIntegrator("RK2(fsal)", true, a, b,
                new RungeKuttaStepInterpolator(new RungeKuttaMethod.RK2Interpolator()), minStep, maxStep,
                scalAbsoluteTolerance, scalRelativeTolerance) {
            @Override
            public int getOrder() {
                return 2;
            }

            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                return Math.abs(y1[0] - y0[0]) / (h + 1.0e-12);
            }
        };

        final FirstOrderDifferentialEquations equations = (firstOrderDifferentialEquations)
                (d -> { d[0] = Besselfunctions.i0(d[0]); });
        final double t0 = 0.0;
        final double t1 = 1.0;
        final double[] result = new double[] { 1.1685799999999999, 1.1685799999999999 };

        final EventsManager eventsManager = new EventsManager();
        eventsManager.addEqualityEvent(new BasicIntervalEvent(1.0, 1.0, 1.0));

        final StepHandler stepHandler = new StepHandler() {
            @Override
            public void handleStep(StepHandlerContext context, int step) {
                final double[] y = context.getY();
                assertEquals(result[step], y, 1e-8);
            }
        };

        integrator.setStepHandlers(stepHandler);
        integrator.setEventsManager(eventsManager);

        final double[] y = integrator.integrate(equations, t0, y0, t1);

        assertEquals(result[1], y[0], 1e-8);
    }

    /**
     * Test method for {@link EmbeddedRungeKuttaIntegrator#EmbeddedRungeKuttaIntegrator(String, boolean, double[], double[][], double[], RungeKuttaStepInterpolator, double, double, double[], double[])}.
     */
    @org.junit.Test
    public void testEmbeddedRungeKuttaIntegrator_not_fsal() {
        final double[] y0 = { 1.0 };
        final double[][] a = { { 0.0, 0.3, 0.4 }, { 0.3, 0.3, 0.3 }, { 0.4, 0.4, 0.1 } };
        final double[] b = { 0.3, 0.3, 0.4 };
        final double minStep = 0.0001;
        final double maxStep = 0.1;
        final double scalAbsoluteTolerance = 1e-8;
        final double scalRelativeTolerance = 1e-8;

        final RungeKuttaMethod integrator = new EmbeddedRungeKuttaIntegrator("RK3", false, a, b,
                new RungeKuttaStepInterpolator(new RungeKuttaMethod.RK3Interpolator()), minStep, maxStep,
                scalAbsoluteTolerance, scalRelativeTolerance) {
            @Override
            public int getOrder() {
                return 3;
            }

            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                return Math.abs(y1[0] - y0[0]) / (h + 1.0e-12);
            }
        };

        final FirstOrderDifferentialEquations equations = (firstOrderDifferentialEquations)
                (d -> { d[0] = Math.exp(d[0]); });
        final double t0 = 0.0;
        final double t1 = 2.0;
        final double[] result = new double[] { 2.7182818284590451, 2.7182818284590451 };

        final EventsManager eventsManager = new EventsManager();
        final StepHandler stepHandler = new StepHandler() {
            @Override
            public void handleStep(StepHandlerContext context, int step) {
                final double[] y = context.getY();
                assertEquals(result[step], y, 1e-8);
            }
        };

        integrator.setStepHandlers(stepHandler);
        integrator.setEventsManager(eventsManager);

        final double[] y = integrator.integrate(equations, t0, y0, t1);

        assertEquals(result[1], y[0], 1e-8);
    }

}