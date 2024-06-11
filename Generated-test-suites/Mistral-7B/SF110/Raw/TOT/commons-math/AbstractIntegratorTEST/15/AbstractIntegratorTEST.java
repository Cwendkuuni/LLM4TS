package org.apache.commons.math3.ode.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.ode.AbstractIntegrator;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.AbstractEvent;
import org.apache.commons.math3.ode.events.ContinuousEvent;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.EventRateFunction;
import org.apache.commons.math3.ode.events.InitialValueEvent;
import org.apache.commons.math3.ode.events.StatefulEvent;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.apache.commons.math3.ode.sampling.StepInterpolator.StepHandler;
import org.apache.commons.math3.ode.util.ExpandableStatefulODE;
import org.apache.commons.math3.ode.util.ODETestUtils;
import org.apache.commons.math3.ode.util.UnivariateSolver;
import org.apache.commons.math3.util.Precision;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link AbstractIntegrator}.
 */
public class AbstractIntegratorTest {

    /** The integrator under test. */
    private AbstractIntegrator integrator;

    /** The differential equations to be integrated. */
    private FirstOrderDifferentialEquations equations;

    /** The continuous event to be triggered. */
    private final ContinuousEvent event = new ContinuousEvent() {
        @Override
        public boolean isSatisfied(double t, double[] y) {
            return t > 1.0;
        }
    };

    /** The event handler for the continuous event. */
    private final StepHandler eventHandler = new StepHandler() {
        @Override
        public void handleStep(StepInterpolator interpolator, boolean last) {
            System.out.println("Event triggered at t = " + interpolator.getInterpolatedTime());
        }
    };

    /**
     * Sets up the test environment.
     */
    @Before
    public void setUp() {
        integrator = new AbstractIntegrator() {
            @Override
            public void integrate(ExpandableStatefulODE ode, double t) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
                // Empty implementation
            }
        };
        integrator.addEventHandler(event, 1.0, 0.001, 10);
        integrator.addStepHandler(eventHandler);
        equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[0] * 0.1;
            }
        };
    }

    /**
     * Test integration of simple differential equation without events.
     */
    @Test
    public void testIntegration() {
        final double[] y0 = { 1.0 };
        final double tEnd = 2.0;
        integrator.initIntegration(0.0, y0, tEnd);
        integrator.setExpandable(new ExpandableStatefulODE(equations));
        double y[] = new double[1];
        integrator.integrate(equations, 0.0, y0, tEnd, y);
        assertEquals(tEnd, y[0], 1.0e-8);
    }

    /**
     * Test integration with continuous event.
     */
    @Test
    public void testEvent() {
        final double[] y0 = { 0.5 };
        integrator.initIntegration(0.0, y0, 3.0);
        integrator.setExpandable(new ExpandableStatefulODE(equations));
        double y[] = new double[1];
        integrator.integrate(equations, 0.0, y0, 3.0, y);
        // Event should have been triggered at least once
        assertTrue(eventHandler.isCalled());
    }

    /**
     * Test integration with initial value event.
     */
    @Test
    public void testInitialValueEvent() {
        final double[] y0 = { 1.0 };
        integrator.initIntegration(0.0, y0, 1.0);
        integrator.addEventHandler(new InitialValueEvent(1.0, 1.0), 0.0, 0.0, new UnivariateSolver() {
            @Override
            public double solve(double t, double x) {
                return x * 2.0;
            }
        });
        integrator.setExpandable(new ExpandableStatefulODE(equations));
        double[] y = new double[1];
        integrator.integrate(equations, 0.0, y0, 1.0, y);
        assertEquals(1.0, y[0], 1.0e-8);
    }

    /**
     * Test integration with stateful event.
     */
    @Test
    public void testStatefulEvent() {
        final double[] y0 = { 0.5 };
        integrator.initIntegration(0.0, y0, 3.0);
        integrator.addEventHandler(new StatefulEvent(new EventRateFunction() {
            @Override
            public double value(double t, double[] y) {
                return 1.0;
            }
        }, 1.0), 0.0, 0.0, new UnivariateSolver() {
            @Override
            public double solve(double t, double x) {
                return x * 2.0;
            }
        });
        integrator.setExpandable(new ExpandableStatefulODE(equations));
        double y[] = new double[1];
        integrator.integrate(equations, 0.0, y0, 3.0, y);
        // Event should have been triggered at least once
        assertTrue(((AbstractEvent) integrator.getEvents().get(0)).isTriggered());
    }

    /**
     * Test integration with no bracketing.
     */
    @Test(expected = NoBracketingException.class)
    public void testNoBracketing() {
        final double[] y0 = { 0.5 };
        integrator.initIntegration(0.0, y0, 1.0);
        integrator.addEventHandler(event, 0.0, 0.0, 1);
        integrator.setExpandable(new ExpandableStatefulODE(equations));
        integrator.integrate(equations, 0.0, y0, 0.0, new double[1]);
    }

    /**
     * Test integration with max count exceeded.
     */
    @Test(expected = MaxCountExceededException.class)
    public void testMaxCountExceeded() {
        final double[] y0 = { 0.5 };
        integrator.initIntegration(0.0, y0, 1.0);
        integrator.addEventHandler(event, 0.0, 0.0, 1);
        integrator.setExpandable(new ExpandableStatefulODE(equations));
        integrator.setMaxEvaluations(0);
        integrator.integrate(equations, 0.0, y0, 1.0, new double[1]);
    }

    /**
     * Test integration with dimension mismatch.
     */
    @Test(expected = DimensionMismatchException.class)
    public void testDimensionMismatch() {
        final double[] y0 = { 0.5, 0.5 };
        integrator.initIntegration(0.0, y0, 1.0);
        integrator.setExpandable(new ExpandableStatefulODE(new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }
        }));
        integrator.integrate(equations, 0.0, y0, 1.0, new double[1]);
    }

    /**
     * Test integration with number too small.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testNumberTooSmall() {
        final double[] y0 = { 0.5 };
        integrator.initIntegration(0.0, y0, 1.0e-100);
        integrator.setExpandable(new ExpandableStatefulODE(equations));
        integrator.integrate(equations, 0.0, y0, 1.0e-100, new double[1]);
    }

}