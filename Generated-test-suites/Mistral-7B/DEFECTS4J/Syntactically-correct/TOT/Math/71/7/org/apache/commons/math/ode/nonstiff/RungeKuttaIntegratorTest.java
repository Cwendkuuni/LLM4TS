package org.apache.commons.math.ode.nonstiff.test;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NoRootsException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.AbsEvent;
import org.apache.commons.math3.ode.events.RelEvent;
import org.apache.commons.math3.ode.events.RootFunction;
import org.apache.commons.math3.ode.events.events.AbstractEvent;
import org.apache.commons.math3.ode.events.manager.CombinedEventsManager;
import org.apache.commons.math3.ode.events.manager.StepHandlersManager;
import org.apache.commons.math3.ode.sampling.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.stepper.RungeKuttaIntegrator;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for {@link RungeKuttaIntegrator}.
 */
public class RungeKuttaIntegratorTest {

    /**
     * Test for {@link RungeKuttaIntegrator#integrate(FirstOrderDifferentialEquations, double, double[], double, double[])}.
     */
    @Test
    public void testIntegration() {
        final double step = 0.01;
        final double[] y0 = new double[] { 1.0, 1.0 };
        final double[] y1 = new double[] { 2.0, 1.0 };
        final double t0 = 0.0;
        final double t1 = 1.0;
        final RungeKuttaIntegrator rk = new EulerIntegrator("Euler", new double[] {}, new double[] {}, new double[] { 1.0 }, new RungeKuttaStepInterpolator());

        // test integration with no events
        double[] result = rk.integrate(new TestDifferentialEquations(), t0, y0, t1, y1);
        assertEquals(y1[0], result[0]);
        assertEquals(y1[1], result[1]);

        // test integration with a single event
        CombinedEventsManager manager = new CombinedEventsManager();
        manager.addStepHandler(new AbstractEvent.StepHandler() {
            @Override
            public void handleStep(double time, double[] state) {
                assertEquals(1.5, state[0], 1e-12);
                assertEquals(0.5, state[1], 1e-12);
            }
        });
        manager.addEvent(new RelEvent(1.0, new RootFunction() {
            @Override
            public double value(double time, double[] state) {
                return state[0] - 1.5;
            }

            @Override
            public double root1(double time, double[] state) {
                return 1.5;
            }

            @Override
            public double root2(double time, double[] state) {
                return -1.5;
            }
        }));
        result = rk.integrate(new TestDifferentialEquations(), t0, y0, t1, y1, manager);
        assertEquals(y1[0], result[0]);
        assertEquals(y1[1], result[1]);

        // test integration with multiple events
        manager = new CombinedEventsManager();
        manager.addStepHandler(new AbstractEvent.StepHandler() {
            @Override
            public void handleStep(double time, double[] state) {
                assertEquals(1.5, state[0], 1e-12);
                assertEquals(0.5, state[1], 1e-12);
            }
        });
        manager.addEvent(new AbsEvent(0.5, new RootFunction() {
            @Override
            public double value(double time, double[] state) {
                return Math.abs(state[0] - 1.0);
            }

            @Override
            public double root1(double time, double[] state) {
                return 1.0;
            }

            @Override
            public double root2(double time, double[] state) {
                return -1.0;
            }
        }));
        manager.addEvent(new RelEvent(1.0, new RootFunction() {
            @Override
            public double value(double time, double[] state) {
                return state[0] - 1.5;
            }

            @Override
            public double root1(double time, double[] state) {
                return 1.5;
            }

            @Override
            public double root2(double time, double[] state) {
                return -1.5;
            }
        }));
        result = rk.integrate(new TestDifferentialEquations(), t0, y0, t1, y1, manager);
        assertEquals(y1[0], result[0], 1e-12);
        assertEquals(y1[1], result[1], 1e-12);

        // test integration with a step size too small
        rk = new EulerIntegrator("Euler", new double[] {}, new double[] {}, new double[] { 0.001 }, new RungeKuttaStepInterpolator());
        try {
            rk.integrate(new TestDifferentialEquations(), t0, y0, t1, y1);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        // test integration with events that cannot be reached
        manager = new CombinedEventsManager();
        manager.addStepHandler(new AbstractEvent.StepHandler() {
            @Override
            public void handleStep(double time, double[] state) {
                fail("Should not be reached");
            }
        });
        manager.addEvent(new RelEvent(1.0, new RootFunction() {
            @Override
            public double value(double time, double[] state) {
                return state[0] - 1.5;
            }

            @Override
            public double root1(double time, double[] state) {
                return 1.5;
            }

            @Override
            public double root2(double time, double[] state) {
                return -1.5;
            }
        }));
        result = rk.integrate(new TestDifferentialEquations(), t0, y0, t1, y1, manager);
        assertEquals(y0[0], result[0]);
        assertEquals(y0[1], result[1]);

        // test integration with events that cannot be reached because of a non-monotonic time sequence
        manager = new CombinedEventsManager();
        manager.addStepHandler(new AbstractEvent.StepHandler() {
            @Override
            public void handleStep(double time, double[] state) {
                fail("Should not be reached");
            }
        });
        manager.addEvent(new AbsEvent(0.5, new RootFunction() {
            @Override
            public double value(double time, double[] state) {
                return Math.abs(state[0] - 1.0);
            }

            @Override
            public double root1(double time, double[] state) {
                return 1.0;
            }

            @Override
            public double root2(double time, double[] state) {
                return -1.0;
            }
        }));
        manager.addEvent(new RelEvent(1.0, new RootFunction() {
            @Override
            public double value(double time, double[] state) {
                return state[0] - 1.5;
            }

            @Override
            public double root1(double time, double[] state) {
                return 1.5;
            }

            @Override
            public double root2(double time, double[] state) {
                return -1.5;
            }
        }));
        try {
            rk.integrate(new TestDifferentialEquations(), t0, y0, 0.1, y1, manager);
            fail();
        } catch (NoRootsException e) {
            // expected
        } catch (IllegalArgumentException e) {
            fail("Should not be thrown");
        }

    }

}

class TestDifferentialEquations implements FirstOrderDifferentialEquations {

    @Override
    public int getDimension() {
        return 2;
    }

    @Override
    public void computeDerivatives(double t, double[] y, double[] yDot) {
        yDot[0] = y[1];
        yDot[1] = -0.1 * y[1];
    }

}