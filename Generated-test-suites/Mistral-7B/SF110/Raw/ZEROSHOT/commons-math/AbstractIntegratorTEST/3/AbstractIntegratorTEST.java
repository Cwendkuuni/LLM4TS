package org.apache.commons.math3.ode.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.analysis.solvers.BracketingNthOrderBrentSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateSolver;
import org.apache.commons.math3.ode.AbstractEvent;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.sampling.AbstractStepInterpolator;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.events.StepHandler;
import org.apache.commons.math3.ode.stephandlers.PrintStepHandler;
import org.apache.commons.math3.ode.stephandlers.StepMonitor;
import org.apache.commons.math3.ode.stephandlers.StepMonitorAdapter;
import org.apache.commons.math3.ode.stephandlers.DefaultStepHandler;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link AbstractIntegrator} class.
 */
public class AbstractIntegratorTest {

    private AbstractIntegrator integrator;
    private FirstOrderDifferentialEquations equations;
    private ExpandableStatefulODE expandableODE;
    private AbstractStepInterpolator interpolator;
    private StepMonitor monitor;
    private List<Event> events;

    @Before
    public void setUp() {
        integrator = new AbstractIntegrator("TestIntegrator") {
            @Override
            public void integrate(ExpandableStatefulODE p0, double p1) {
                // Empty implementation
            }
        };
        equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public double[] getPrimaryBounds() {
                return new double[] { -1, 1 };
            }

            @Override
            public void computePrimaryDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = Math.sin(y[0]);
            }

            @Override
            public void computeSecondaryDerivatives(double t, double[] y, int index, double[] ySecondary, double[] ySecondaryDot) {
                ySecondaryDot[0] = -Math.cos(y[0]);
            }
        };
        expandableODE = new ExpandableStatefulODE(equations);
        interpolator = new AbstractStepInterpolator() {
            @Override
            public double integrate(ExpandableStatefulODE equations, double t0, double y0, double[] y, double t1) throws MaxCountExceededException, NumberIsTooSmallException, NoBracketingException {
                integrator.integrate(equations, t0, y0, t1, y);
                return t1;
            }
        };
        monitor = new StepMonitorAdapter();
        integrator.addStepHandler(new PrintStepHandler(monitor));
        integrator.addStepHandler(new DefaultStepHandler(0.01, 0.0));
        integrator.addEventHandler(new TestEventHandler(2.0, 1.0, 1.0e-10, 5), 0.1);
        integrator.addEventHandler(new TestEventHandler(3.0, 2.0, 1.0e-10, 5), 0.2);
        events = new ArrayList<>();
        integrator.setEventHandlers(new ArrayList<>(integrator.getEventHandlers()) {
            @Override
            public Iterator<EventHandler> iterator() {
                return super.iterator();
            }
        });
    }

    @Test
    public void testIntegration() throws Exception {
        integrator.initIntegration(0.0, new double[]{ 0.0 }, 4.0);
        integrator.setExpandable(expandableODE);
        integrator.setEvaluationsCounter(integrator.getEvaluationsCounter());
        double t = integrator.acceptStep(interpolator, new double[]{ 0.0 }, new double[1], 0.1);
        assertEquals(0.1, t, 1e-14);
        assertEquals(1, integrator.getEvaluations());
        t = integrator.acceptStep(interpolator, new double[]{ 0.1 }, new double[1], 2.0);
        assertEquals(2.0, t, 1e-14);
        assertEquals(3, integrator.getEvaluations());
        integrator.setStateInitialized(true);
        t = integrator.acceptStep(interpolator, new double[]{ 2.0 }, new double[1], 1.0);
        assertEquals(3.0, t, 1e-14);
        assertEquals(4, integrator.getEvaluations());
        t = integrator.acceptStep(interpolator, new double[]{ 3.0 }, new double[1], 0.1);
        assertEquals(3.1, t, 1e-14);
        assertEquals(5, integrator.getEvaluations());
        assertEquals(2, integrator.getEventHandlers().size());
        assertEquals(0, integrator.getStepHandlers().size());
        // Test event handling
        integrator.initIntegration(0.0, new double[]{ 0.0 }, 4.0);
        integrator.setExpandable(expandableODE);
        integrator.setEvaluationsCounter(integrator.getEvaluationsCounter());
        integrator.acceptStep(interpolator, new double[]{ 0.0 }, new double[1], 2.0);
        assertEquals(1, integrator.getEventHandlers().size());
        Event event = integrator.getEventHandlers().get(0);
        assertEquals(2.0, event.getEventTime(), 1e-14);
        assertEquals(TestEventHandler.class, event.getClass());
        events.add(event);
        integrator.acceptStep(interpolator, new double[]{ 2.0 }, new double[1], 1.0);
        assertEquals(2, integrator.getEventHandlers().size());
        event = integrator.getEventHandlers().get(1);
        assertEquals(3.0, event.getEventTime(), 1e-14);
        assertEquals(TestEventHandler.class, event.getClass());
        events.add(event);
        integrator.acceptStep(interpolator, new double[]{ 3.0 }, new double[1], 0.1);
        assertEquals(3, integrator.getEventHandlers().size());
        event = integrator.getEventHandlers().get(2);
        assertEquals(4.0, event.getEventTime(), 1e-14);
        assertEquals(TestEventHandler.class, event.getClass());
        events.add(event);
        // Test step handling
        integrator.clearStepHandlers();
        integrator.addStepHandler(new PrintStepHandler(monitor));
        integrator.addStepHandler(new StepMonitor(monitor));
        integrator.addStepHandler(new StepMonitor(new StepMonitorAdapter() {
            private int count;

            @Override
            public void handleStep(AbstractStepInterpolator interpolator, boolean last) {
                count++;
            }
        }));
        integrator.initIntegration(0.0, new double[]{ 0.0 }, 4.0);
        integrator.setExpandable(expandableODE);
        integrator.setEvaluationsCounter(integrator.getEvaluationsCounter());
        t = integrator.acceptStep(interpolator, new double[]{ 0.0 }, new double[1], 0.1);
        assertEquals(0.1, t, 1e-14);
        assertEquals(4, integrator.getEvaluations());
        assertEquals(1, monitor.getNumberSteps());
        assertEquals(1, ((StepMonitor) monitor).getPreviousStepTime());
        assertEquals(0, ((StepMonitor) monitor).getCurrentStepTime());
        assertEquals(0.1, ((StepMonitor) monitor).getStepSize());
        assertEquals(1, ((StepMonitor) monitor).getLastStep());
        assertEquals(1, integrator.getStepHandlers().size());
        integrator.clearStepHandlers();
    }

    @Test(expected = DimensionMismatchException.class)
    public void testDimensionMismatchException() throws Exception {
        integrator.initIntegration(0.0, new double[]{ 0.0, 0.0 }, 4.0);
        integrator.setExpandable(expandableODE);
        integrator.acceptStep(interpolator, new double[]{ 0.0 }, new double[]{ 1.0, 1.0 }, 0.1);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testNumberIsTooSmallException() throws Exception {
        integrator.initIntegration(0.0, new double[]{ 0.0 }, 1.0e-100);
        integrator.setExpandable(expandableODE);
        integrator.acceptStep(interpolator, new double[]{ 0.0 }, new double[1], 0.1);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testMaxCountExceededException() throws Exception {
        integrator.initIntegration(0.0, new double[]{ 0.0 }, 4.0);
        integrator.setExpandable(expandableODE);
        integrator.setMaxEvaluations(0);
        integrator.acceptStep(interpolator, new double[]{ 0.0 }, new double[1], 0.1);
    }

    @Test(expected = NoBracketingException.class)
    public void testNoBracketingException() throws Exception {
        integrator.initIntegration(0.0, new double[]{ 0.0 }, 4.0);
        integrator.setExpandable(new ExpandableStatefulODE(new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computePrimaryDerivatives(double t, double[] y, double[] yPrime) {
                // No bracketing
            }
        }));
        integrator.acceptStep(interpolator, new double[]{ 0.0 }, new double[1], 0.1);
    }

    public static class TestEventHandler implements EventHandler {

        private final double eventTime;
        private final double convergence;
        private final int maxIterationCount;
        private final UnivariateSolver solver;

        public TestEventHandler(double eventTime, double convergence, int maxIterationCount) {
            this(eventTime, convergence, maxIterationCount, new BracketingNthOrderBrentSolver(convergence, 5));
        }

        public TestEventHandler(double eventTime, double convergence, int maxIterationCount, UnivariateSolver solver) {
            this.eventTime = eventTime;
            this.convergence = convergence;
            this.maxIterationCount = maxIterationCount;
            this.solver = solver;
        }

        @Override
        public void handleEvent(double t, double[] y, double[] yDot) {
            // Empty implementation
        }

        @Override
        public boolean evaluateStep(AbstractStepInterpolator interpolator) {
            return interpolator.getGlobalCurrentTime() >= this.eventTime;
        }

        @Override
        public void reinitializeBegin(AbstractStepInterpolator interpolator) {
            // Empty implementation
        }

        @Override
        public void stepAccepted(double t, double[] yPrimary) {
            // Empty implementation
        }

        @Override
        public void reset(double t, double[] yComplete) {
            // Empty implementation
        }

        public double getEventTime() {
            return eventTime;
        }

        public EventHandler getEventHandler() {
            return this;
        }
    }
}