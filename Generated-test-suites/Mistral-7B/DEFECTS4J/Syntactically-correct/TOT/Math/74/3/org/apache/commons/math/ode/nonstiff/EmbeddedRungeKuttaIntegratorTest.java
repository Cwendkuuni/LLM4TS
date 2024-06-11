package org.apache.commons.math.ode.nonstiff.test;

import junit.framework.TestCase;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.When;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.apache.commons.math3.ode.testing.TestODE;
import org.apache.commons.math3.ode.util.ODETestUtils;
import org.apache.commons.math3.special.BesselFunctions;

/**
 * Tests for {@link EmbeddedRungeKuttaIntegrator} class.
 */
public class EmbeddedRungeKuttaIntegratorTest extends TestCase {

    /**
     * Test RK4 integrator with simple ODE.
     * @throws Exception if test fails
     */
    public void testRK4() throws Exception {
        final StepInterpolator interpolator = new RungeKuttaStepInterpolator(4, 0.01);
        final EmbeddedRungeKuttaIntegrator rk4 = new DormandPrince54Integrator(4, interpolator);
        final double[] y0 = { 0.0, 1.0 };
        final double t0 = 0.0;
        final double tEnd = 2.0;

        final FirstOrderDifferentialEquations equations = new DummyDifferentialEquations();
        rk4.integrate(equations, t0, y0, tEnd, null);

        // check solution
        final double[] expected = { 1.8627468919152501, 0.6346557658179562 };
        ODETestUtils.compareDoubles(expected, rk4.getSolutions(), 1.e-12);
    }

    /**
     * Test RKF45 integrator with simple ODE.
     * @throws Exception if test fails
     */
    public void testRKF45() throws Exception {
        final StepInterpolator interpolator = new RungeKuttaStepInterpolator(4, 0.01);
        final EmbeddedRungeKuttaIntegrator rkf45 = new DormandPrince853Integrator(5, interpolator);
        final double[] y0 = { 0.0, 1.0 };
        final double t0 = 0.0;
        final double tEnd = 2.0;

        final FirstOrderDifferentialEquations equations = new DummyDifferentialEquations();
        rkf45.integrate(equations, t0, y0, tEnd, null);

        // check solution
        final double[] expected = { 1.8627468919152501, 0.6346557658179562 };
        ODETestUtils.compareDoubles(expected, rkf45.getSolutions(), 1.e-12);
    }

    /**
     * Test RKF45 integrator with simple ODE and events.
     * @throws Exception if test fails
     */
    public void testRKF45_events() throws Exception {
        final double[] y0 = { 1.0 };
        final double t0 = 0.0;
        final double tEnd = 2.0;
        final EmbeddedRungeKuttaIntegrator rkf45 = new DormandPrince853Integrator(5, new DummyStepInterpolator());

        final StepHandler eventHandler = new StepHandler() {

            @Override
            public void handleStep(StepInterpolator interpolator, int step) {
                final double t = interpolator.getTime();
                if (t > 1.5 && t < 1.6) {
                    interpolator.addEvent(new When(1.55), new Event() {
                        @Override
                        public void evaluateEvent(double t, double[] y) {
                            throw new RuntimeException("Test event thrown");
                        }
                    });
                }
            }
        };

        final FirstOrderDifferentialEquations equations = new DummyDifferentialEquations() {

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = y[0];
            }
        };

        try {
            rkf45.integrate(equations, t0, y0, tEnd, eventHandler);
            fail("Expected IntegratorException to be thrown");
        } catch (IntegratorException e) {
            // expected
        }
    }

    /**
     * Test Bessel ODE RKF45.
     * @throws Exception if test fails
     */
    public void testBesselODE() throws Exception {
        final double[] y0 = { 1.0 };
        final double t0 = 0.0;
        final double tEnd = 1.5;
        final EmbeddedRungeKuttaIntegrator rkf45 = new RungeKuttaFehlberg87Integrator(7, new DummyStepInterpolator());

        final FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = -y[0] / (2.0 * t) + BesselFunctions.besselI(0, t);
            }
        };

        rkf45.integrate(equations, t0, y0, tEnd, null);

        final double[] expected = { 1.0, 0.7978845648574415 };
        ODETestUtils.compareDoubles(expected, rkf45.getSolutions(), 1.e-8);
    }

    /**
     * Test Bessel ODE RKF45 with events.
     * @throws Exception if test fails
     */
    public void testBesselODE_events() throws Exception {
        final double[] y0 = { 1.0 };
        final double t0 = 0.0;
        final double tEnd = 1.5;
        final EmbeddedRungeKuttaIntegrator rkf45 = new RungeKuttaFehlberg87Integrator(7, new DummyStepInterpolator());

        final StepHandler eventHandler = new StepHandler() {

            @Override
            public void handleStep(StepInterpolator interpolator, int step) {
                if (interpolator.getTime() > 1.2) {
                    interpolator.addEvent(new When(1.3), new Event() {
                        @Override
                        public void evaluateEvent(double t, double[] y) {
                            throw new RuntimeException("Test event thrown");
                        }
                    });
                }
            }
        };

        final FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = -y[0] / (2.0 * t) + BesselFunctions.besselI(0, t);
            }
        };

        try {
            rkf45.integrate(equations, t0, y0, tEnd, eventHandler);
            fail("Expected IntegratorException to be thrown");
        } catch (IntegratorException e) {
            // expected
        }
    }

    /**
     * Dummy ODE implementation.
     */
    static class DummyDifferentialEquations implements FirstOrderDifferentialEquations {

        @Override
        public void computeDerivatives(double t, double[] y, double[] yPrime) {
            yPrime[0] = y[0];
        }
    }
}