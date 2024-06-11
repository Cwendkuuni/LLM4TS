package org.apache.commons.math3.ode.test;

import static org.junit.Assert.*;
import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.ode.events.*;
import org.apache.commons.math3.ode.sampling.*;
import org.apache.commons.math3.analysis.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.ode.AbstractIntegrator;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.stephandlers.*;
import org.apache.commons.math3.ode.stephandlers.StoppingConditions;
import org.junit.Test;

/**
 * Test for AbstractIntegrator class.
 */
public class AbstractIntegratorTest {

    /**
     * Test the integration of a simple ODE.
     *
     * @throws Exception if an error occurs during testing
     */
    @Test
    public void testSimpleIntegration() throws Exception {
        final String name = "TestIntegrator";
        final double t0 = 0.0;
        final double[] y0 = new double[] { 1.0, 0.0 };
        final double t1 = 2.0;
        final double[] y1 = new double[] { 2.0, 1.0 };
        final FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            public int getDimension() {
                return y0.length;
            }

            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[1];
                yDot[1] = 3.0 * y[0] - y[1];
            }
        };

        AbstractIntegrator integrator = new AbstractIntegrator(name) {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
                sanityChecks(equations, t);
                super.integrate(equations, t);
            }
        };
        integrator.addStepHandler(new StepSizeChecker(0.1, new StoppingConditions() {}));
        integrator.addEventHandler(new Event(t1, new MyEventHandler()));

        integrator.initIntegration(t0, y0, t1);
        integrator.setExpandable(new ExpandableStatefulODE(equations));
        integrator.integrate(t0, y0, y1);

        assertEquals(y1[0], 2.0, 1e-8);
        assertEquals(y1[1], 1.0, 1e-8);
    }

    /**
     * Test the integration of an ODE with an event.
     *
     * @throws Exception if an error occurs during testing
     */
    @Test
    public void testEventIntegration() throws Exception {
        final String name = "TestIntegrator";
        final double t0 = 0.0;
        final double[] y0 = new double[] { 1.0, 0.0 };
        final double t1 = 1.0;
        final double[] y1 = new double[] { 0.0, 1.0 };
        final double eventTime = 0.5;

        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            public int getDimension() {
                return y0.length;
            }

            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[1];
                yDot[1] = -y[0];
            }
        };

        AbstractIntegrator integrator = new AbstractIntegrator(name) {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
                sanityChecks(equations, t);
                super.integrate(equations, t);
            }
        };

        integrator.addStepHandler(new StepSizeChecker(0.1, new StoppingConditions() {}));
        integrator.addEventHandler(new MyEventHandler(eventTime), 0.01, 1e-6, 10);

        integrator.initIntegration(t0, y0, t1);
        integrator.setExpandable(new ExpandableStatefulODE(equations));
        integrator.integrate(t0, y0, y1);

        assertEquals(y1[0], 0.0, 1e-8);
        assertEquals(y1[1], 1.0, 1e-8);
    }

    /**
     * Test the integration of an ODE with an event that stops the integration.
     *
     * @throws Exception if an error occurs during testing
     */
    @Test(expected = MaxCountExceededException.class)
    public void testEventStopIntegration() throws Exception {
        final String name = "TestIntegrator";
        final double t0 = 0.0;
        final double[] y0 = new double[] { 1.0, 0.0 };
        final double t1 = 2.0;
        final double[] y1 = new double[] { 1.0, 0.0 };
        final double eventTime = 0.5;

        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            public int getDimension() {
                return y0.length;
            }

            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[1];
                yDot[1] = y[0];
            }
        };

        AbstractIntegrator integrator = new AbstractIntegrator(name) {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
                sanityChecks(equations, t);
                super.integrate(equations, t);
            }
        };

        integrator.addStepHandler(new StepSizeChecker(0.1, new StoppingConditions() {}));
        integrator.addEventHandler(new MyEventHandler(eventTime), 0.01, 1e-6, 10);

        integrator.initIntegration(t0, y0, t1);
        integrator.setExpandable(new ExpandableStatefulODE(equations));
        integrator.integrate(t0, y0, y1);

        fail("Integration should have been stopped by the event.");
    }

    /**
     * Test the integration of an ODE with a stopping condition.
     *
     * @throws Exception if an error occurs during testing
     */
    @Test
    public void testStoppingCondition() throws Exception {
        final String name = "TestIntegrator";
        final double t0 = 0.0;
        final double[] y0 = new double[] { 1.0, 0.0 };
        final double t1 = 2.0;
        final double[] y1 = new double[] { 1.0, 0.1 };

        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            public int getDimension() {
                return y0.length;
            }

            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[1];
                yDot[1] = y[0];
            }
        };

        AbstractIntegrator integrator = new AbstractIntegrator(name) {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
                sanityChecks(equations, t);
                super.integrate(equations, t);
            }
        };

        integrator.addStepHandler(new StepSizeChecker(0.1, new StoppingConditions() {
            public boolean isStoppingTimeReached(double t, double[] y) {
                return y[1] > 0.1;
            }
        }));

        integrator.initIntegration(t0, y0, t1);
        integrator.setExpandable(new ExpandableStatefulODE(equations));
        integrator.integrate(t0, y0, y1);

        assertEquals(y1[0], 1.0, 1e-8);
        assertEquals(y1[1], 0.1, 1e-8);
    }

    /**
     * Helper class for testing events.
     */
    static class MyEventHandler implements EventHandler {

        private final double eventTime;
        private final double maxCheckInterval;
        private final double convergence;
        private final int maxIterationCount;
        private final UnivariateSolver solver;

        /**
         * Constructor.
         *
         * @param eventTime the event time
         */
        MyEventHandler(double eventTime) {
            this(eventTime, 0.01, 1e-6, 10, new BracketingNthOrderBrentSolver(1e-6, 5));
        }

        /**
         * Constructor.
         *
         * @param eventTime the event time
         * @param maxCheckInterval the maximum check interval
         * @param convergence the convergence criterion
         * @param maxIterationCount the maximum iteration count
         * @param solver the solver to use
         */
        MyEventHandler(double eventTime, double maxCheckInterval, double convergence, int maxIterationCount, UnivariateSolver solver) {
            this.eventTime = eventTime;
            this.maxCheckInterval = maxCheckInterval;
            this.convergence = convergence;
            this.maxIterationCount = maxIterationCount;
            this.solver = solver;
        }

        /**
         * {@inheritDoc}
         */
        public boolean evaluateStep(AbstractStepInterpolator interpolator) {
            return (interpolator.getGlobalCurrentTime() < eventTime);
        }

        /**
         * {@inheritDoc}
         */
        public void stepAccepted(double t, double[] y) {
            // no-op
        }

        /**
         * {@inheritDoc}
         */
        public double getEventTime() {
            return eventTime;
        }
    }
}