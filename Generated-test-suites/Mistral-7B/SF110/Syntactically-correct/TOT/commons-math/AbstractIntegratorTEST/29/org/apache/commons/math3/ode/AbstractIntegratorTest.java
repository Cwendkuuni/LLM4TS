package org.apache.commons.math3.ode.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.ode.events.*;
import org.apache.commons.math3.ode.sampling.*;
import org.apache.commons.math3.exception.*;
import org.junit.Before;
import org.junit.Test;

public class AbstractIntegratorTest {

    private AbstractIntegrator integrator;
    private FirstOrderDifferentialEquations equations;

    @Before
    public void setUp() {
        integrator = new MyAbstractIntegrator("MyIntegrator");
        equations = new MyDifferentialEquations();
    }

    @Test
    public void testIntegration() throws Exception {
        double t0 = 0.0;
        double[] y0 = {1.0, 2.0};
        double t = 5.0;
        double[] y = new double[y0.length];

        integrator.integrate(equations, t0, y0, t, y);

        assertEquals(t, integrator.getCurrentStepStart(), 1e-10);
        assertArrayEquals(y, integrator.getExpandable().getPrimaryState(), 1e-10);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testDimensionMismatch() throws Exception {
        double t0 = 0.0;
        double[] y0 = {1.0};
        double[] y = new double[2];

        integrator.integrate(equations, t0, y0, 1.0, y);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testMaxEvaluations() throws Exception {
        integrator.setMaxEvaluations(0);
        integrator.integrate(equations, 0.0, new double[2], 1.0, new double[2]);
    }

    @Test(expected = NoBracketingException.class)
    public void testNoBracketing() throws Exception {
        integrator.setMaxEvaluations(-1);
        integrator.integrate(equations, 0.0, new double[2], 1.0, new double[2]);
    }

    @Test
    public void testEventHandler() throws Exception {
        MyEventHandler handler = new MyEventHandler();
        integrator.addEventHandler(handler, 1.0, 1e-6, 10);

        double t0 = 0.0;
        double[] y0 = {1.0, 2.0};
        double t = 2.0;

        integrator.integrate(equations, t0, y0, t, new double[y0.length]);

        assertTrue(handler.isTriggered());
    }

    @Test
    public void testClearEventHandlers() throws Exception {
        MyEventHandler handler = new MyEventHandler();
        integrator.addEventHandler(handler, 1.0, 1e-6, 10);

        integrator.clearEventHandlers();

        MyEventHandler handler2 = new MyEventHandler();
        integrator.addEventHandler(handler2, 1.0, 1e-6, 10);

        double t0 = 0.0;
        double[] y0 = {1.0, 2.0};
        double t = 2.0;

        integrator.integrate(equations, t0, y0, t, new double[y0.length]);

        assertFalse(handler.isTriggered());
        assertFalse(handler2.isTriggered());
    }

    @Test
    public void testStepHandlers() throws Exception {
        MyStepHandler handler = new MyStepHandler();
        integrator.addStepHandler(handler);

        double t0 = 0.0;
        double[] y0 = {1.0, 2.0};
        double t = 2.0;

        integrator.integrate(equations, t0, y0, t, new double[y0.length]);

        assertTrue(handler.isCalled());
    }

    @Test
    public void testClearStepHandlers() throws Exception {
        MyStepHandler handler = new MyStepHandler();
        integrator.addStepHandler(handler);

        integrator.clearStepHandlers();

        MyStepHandler handler2 = new MyStepHandler();
        integrator.addStepHandler(handler2);

        double t0 = 0.0;
        double[] y0 = {1.0, 2.0};
        double t = 2.0;

        integrator.integrate(equations, t0, y0, t, new double[y0.length]);

        assertFalse(handler.isCalled());
        assertFalse(handler2.isCalled());
    }

    private static class MyAbstractIntegrator extends AbstractIntegrator {
        public MyAbstractIntegrator(String name) {
            super(name);
        }

        @Override
        public void integrate(ExpandableStatefulODE equations, double t) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
            // Override the integration method for testing
            super.integrate(equations, t);
        }
    }

    private static class MyDifferentialEquations implements FirstOrderDifferentialEquations {
        public double[] derivative(double t, double[] y) {
            return new double[] {y[0] * 2.0, y[1] * 2.0};
        }

        public int getDimension() {
            return 2;
        }
    }

    private static class MyEventHandler implements EventHandler {
        private boolean triggered = false;

        public void init(double t0, double[] y0, double t) {
            // Empty implementation
        }

        public boolean evaluateStep(AbstractStepInterpolator interpolator) {
            return t > 1.0;
        }

        public void stepAccepted(double t, double[] y) {
            triggered = true;
        }

        public void reset(double t, double[] y) {
            // Empty implementation
        }

        public boolean stop() {
            return false;
        }

        public boolean isTriggered() {
            return triggered;
        }
    }

    private static class MyStepHandler implements StepHandler {
        private boolean called = false;

        public void init(double t0, double[] y0, double t) {
            // Empty implementation
        }

        public void handleStep(AbstractIntegrator integrator, boolean last) {
            called = true;
        }

        public boolean isCalled() {
            return called;
        }
    }
}