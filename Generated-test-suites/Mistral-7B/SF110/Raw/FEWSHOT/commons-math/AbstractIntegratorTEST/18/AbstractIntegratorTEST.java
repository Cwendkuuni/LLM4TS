package org.apache.commons.math3.ode;

import static org.junit.Assert.*;
import org.apache.commons.math3.ode.events.AbstractEvent;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.events.StopEvent;
import org.apache.commons.math3.ode.sampling.AbstractStepInterpolator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.util.Precision;

import java.util.*;

public class AbstractIntegratorTest {
    @Test
    public void testInitIntegration() {
        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator") {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t) throws DimensionMismatchException, MaxCountExceededException, NoBracketingException {
                // Empty implementation
            }
        };

        double t0 = 0.0;
        double[] y0 = new double[1];
        double t = 1.0;

        integrator.initIntegration(t0, y0, t);

        // Test empty stepHandlers
        assertFalse(integrator.getStepHandlers().isEmpty());
        assertTrue(integrator.getStepHandlers().size() == 0);

        // Test empty eventHandlers
        assertFalse(integrator.getEventHandlers().isEmpty());
        assertTrue(integrator.getEventHandlers().size() == 0);

        // Test getName
        assertEquals("TestIntegrator", integrator.getName());

        // Test getMaxEvaluations
        assertEquals(-1, integrator.getMaxEvaluations());

        // Test getEvaluations
        assertEquals(0, integrator.getEvaluations());

        // Test getStepStart and getCurrentSignedStepsize
        assertEquals(Double.NaN, integrator.getCurrentStepStart(), 0.0);
        assertEquals(Double.NaN, integrator.getCurrentSignedStepsize(), 0.0);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testIntegrateWithDimensionMismatch() {
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 2;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                // Empty implementation
            }
        };

        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator") {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t) throws DimensionMismatchException, MaxCountExceededException, NoBracketingException {
                // Empty implementation
            }
        };

        double t0 = 0.0;
        double[] y0 = new double[1];
        double t1 = 1.0;
        double[] y1 = new double[2];

        integrator.integrate(new ExpandableStatefulODE(equations), t1);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testIntegrateWithMaxCountExceeded() {
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                // Empty implementation
            }
        };

        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator") {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t) throws DimensionMismatchException, MaxCountExceededException, NoBracketingException {
                // Empty implementation
            }
        };

        integrator.setMaxEvaluations(0);

        double t0 = 0.0;
        double[] y0 = new double[1];
        double t1 = 1.0;

        integrator.integrate(new ExpandableStatefulODE(equations), t1);
    }

    @Test(expected = NoBracketingException.class)
    public void testIntegrateWithNoBracketing() {
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                // Empty implementation
            }
        };

        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator") {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t) throws DimensionMismatchException, MaxCountExceededException, NoBracketingException {
                // Empty implementation
            }
        };

        integrator.addEventHandler(new StopEvent(0.5), 0.1, 0.01, 10);

        double t0 = 0.0;
        double[] y0 = new double[1];
        double t1 = 1.0;

        integrator.integrate(new ExpandableStatefulODE(equations), t1);
    }

    @Test
    public void testIntegrateWithEventHandlers() throws Exception {
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[0] * 2.0;
            }
        };

        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator") {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t) throws DimensionMismatchException, MaxCountExceededException, NoBracketingException {
                // Empty implementation
            }
        };

        EventHandler<Double> eventHandler = new AbstractEvent() {
            @Override
            public double getEventTime() {
                return 0.5;
            }

            @Override
            public void handleEvent(double eventTime, double[] eventState) {
                eventState[0] = eventTime;
            }
        };

        integrator.addEventHandler(eventHandler, 0.1, 0.01, 10);

        double t0 = 0.0;
        double[] y0 = new double[1];
        double t1 = 1.0;
        double[] y1 = new double[1];

        integrator.integrate(new ExpandableStatefulODE(equations), t1, y0, y1);

        assertEquals(0.5, y1[0], 0.01);
    }

    @Test
    public void testAcceptStep() throws Exception {
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[0] * 2.0;
            }
        };

        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator") {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t) throws DimensionMismatchException, MaxCountExceededException, NoBracketingException {
                // Empty implementation
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) throws MaxCountExceededException, DimensionMismatchException {
                yDot[0] = y[0] * 3.0;
            }
        };

        EventHandler<Double> eventHandler = new StopEvent(0.5) {
            @Override
            public void handleEvent(double eventTime, double[] eventState) {
                eventState[0] = eventTime;
            }
        };

        StepHandler stepHandler = new StepHandler() {
            @Override
            public void handleStep(StepInterpolator interpolator, boolean lastStep) {
                interpolator.setSoftPreviousTime(interpolator.getGlobalPreviousTime());
                interpolator.setSoftCurrentTime(interpolator.getGlobalCurrentTime());
                interpolator.setInterpolatedTime(interpolator.getGlobalCurrentTime());
                interpolator.setInterpolatedState(new double[]{interpolator.getGlobalCurrentTime(), interpolator.getGlobalCurrentState()[0] * 2.0});
            }
        };

        integrator.addEventHandler(eventHandler, 0.1, 0.01, 10);
        integrator.addStepHandler(stepHandler);

        double t0 = 0.0;
        double[] y0 = new double[1];
        double t1 = 1.0;
        double[] y1 = new double[2];

        integrator.integrate(new ExpandableStatefulODE(equations), t1, y0, y1);

        double eventTime = y1[0];
        assertEquals(0.5, eventTime, 0.01);
        assertEquals(0.5, y1[1], 0.01);
    }

    @Test
    public void testAcceptStepWithReset() throws Exception {
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[0] * 2.0;
            }
        };

        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator") {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t) throws DimensionMismatchException, MaxCountExceededException, NoBracketingException {
                // Empty implementation
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) throws MaxCountExceededException, DimensionMismatchException {
                yDot[0] = y[0] * 3.0;
            }
        };

        EventHandler<Double> eventHandler = new StopEvent(0.5) {
            @Override
            public void handleEvent(double eventTime, double[] eventState) {
                eventState[0] = eventTime;
            }
        };

        StepHandler stepHandler = new StepHandler() {
            @Override
            public void handleStep(StepInterpolator interpolator, boolean lastStep) {
                interpolator.setSoftPreviousTime(interpolator.getGlobalPreviousTime());
                interpolator.setSoftCurrentTime(interpolator.getGlobalCurrentTime());
                interpolator.setInterpolatedTime(interpolator.getGlobalCurrentTime());
                interpolator.setInterpolatedState(new double[]{interpolator.getGlobalCurrentTime(), interpolator.getGlobalCurrentState()[0] * 2.0});
            }

            @Override
            public boolean reset(double interpolatedTime, double[] interpolatedState) {
                return interpolatedTime >= 0.7;
            }
        };

        integrator.addEventHandler(eventHandler, 0.1, 0.01, 10);
        integrator.addStepHandler(stepHandler);

        double t0 = 0.0;
        double[] y0 = new double[1];
        double t1 = 1.0;
        double[] y1 = new double[2];

        integrator.integrate(new ExpandableStatefulODE(equations), t1, y0, y1);

        double eventTime = y1[0];
        assertEquals(0.5, eventTime, 0.01);
        assertEquals(0.5, y1[1], 0.01);

        integrator.clearEventHandlers();
        integrator.clearStepHandlers();

        t0 = 0.0;
        y0 = new double[1];
        t1 = 1.5;
        integrator.integrate(new ExpandableStatefulODE(equations), t1, y0, y1);

        eventTime = y1[0];
        assertEquals(0.7, eventTime, 0.01);
        assertArrayEquals(new double[]{1.1640424884636918, 1.3384135532129722}, y1, 0.01);
    }

    @Test
    public void testSanityChecks() throws Exception {
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[0] * 2.0;
            }
        };

        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator") {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t) throws DimensionMismatchException, MaxCountExceededException, NoBracketingException {
                // Empty implementation
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) throws MaxCountExceededException, DimensionMismatchException {
                yDot[0] = y[0] * 3.0;
            }

            @Override
            protected void sanityChecks(ExpandableStatefulODE equations, double t) throws NumberIsTooSmallException {
                super.sanityChecks(equations, t);
                assertEquals(1.0, equations.getTime(), 0.0);
                assertEquals(1.0, t, 0.0);
            }
        };

        double t0 = 0.0;
        double[] y0 = new double[1];
        double t1 = 0.999999999;

        integrator.integrate(new ExpandableStatefulODE(equations), t1, y0, y0);
    }
}