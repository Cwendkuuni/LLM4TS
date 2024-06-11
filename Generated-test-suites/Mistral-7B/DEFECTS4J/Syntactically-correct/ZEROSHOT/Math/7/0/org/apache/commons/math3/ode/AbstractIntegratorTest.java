package org.apache.commons.math3.ode.test;

import org.apache.commons.math3.ode.AbstractIntegrator;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.events.MockEventHandler;
import org.apache.commons.math3.ode.sampling.AbstractStepInterpolator;
import org.apache.commons.math3.ode.test.util.TestODE;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class AbstractIntegratorTest {

    @Test
    public void testIntegrate() throws Exception {
        final String name = "TestIntegrator";
        final FirstOrderDifferentialEquations equations = new TestODE();
        final AbstractIntegrator integrator = new AbstractIntegrator(name);
        integrator.addEventHandler(new MockEventHandler(), 0.01, 1.0e-6, 10);
        integrator.setEquations(equations);
        integrator.initIntegration(0.0, new double[]{}, 2.0);
        integrator.integrate(equations, 2.0);
        assertEquals(2.0, integrator.getCurrentStepStart(), 1e-6);
        assertArrayEquals(new double[]{1.1, 2.2}, integrator.getState(), 1e-6);
        assertEquals(name, integrator.getName());
        assertEquals(1, integrator.getEvaluations());
    }

    @Test(expected = DimensionMismatchException.class)
    public void testDimenionMismatch() throws Exception {
        final String name = "TestIntegrator";
        final AbstractIntegrator integrator = new AbstractIntegrator(name);
        integrator.initIntegration(0.0, new double[]{0.1}, 2.0);
        integrator.integrate(new TestODE(), 2.0);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testNumberTooSmall() throws Exception {
        final String name = "TestIntegrator";
        final AbstractIntegrator integrator = new AbstractIntegrator(name);
        integrator.setMaxEvaluations(0);
        integrator.initIntegration(0.0, new double[]{0.1}, 1e-12);
        integrator.integrate(new TestODE(), 1.0);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testMaxEvaluations() throws Exception {
        final String name = "TestIntegrator";
        final AbstractIntegrator integrator = new AbstractIntegrator(name);
        integrator.setMaxEvaluations(0);
        integrator.initIntegration(0.0, new double[]{0.1}, 1.0);
        integrator.integrate(new TestODE(), 1.0, new double[1], new double[1]);
    }

    @Test(expected = NoBracketingException.class)
    public void testNoBracketing() throws Exception {
        final String name = "TestIntegrator";
        final AbstractIntegrator integrator = new AbstractIntegrator(name);
        integrator.setMaxEvaluations(100);
        final ExpandableStatefulODE equations = new TestODE();
        integrator.initIntegration(0.0, new double[]{0.1}, 1.0);
        integrator.addEventHandler(new MockEventHandler(), 0.01, 1.0e-6, 10);
        integrator.addEventHandler(new MockEventHandler(), 0.03, 1.0e-6, 10);
        integrator.addStepHandler(new MockStepHandler());
        integrator.integrate(equations, 1.0);
    }

    @Test
    public void testAcceptStep() throws Exception {
        final String name = "TestIntegrator";
        final AbstractIntegrator integrator = new AbstractIntegrator(name);
        integrator.setMaxEvaluations(10);
        final ExpandableStatefulODE equations = new TestODE();
        integrator.initIntegration(0.0, new double[]{0.1}, 2.0);
        integrator.addEventHandler(new MockEventHandler(), 0.5, 1.0e-6, 10);
        integrator.addEventHandler(new MockEventHandler(), 1.5, 1.0e-6, 10);
        integrator.addStepHandler(new MockStepHandler());
        integrator.integrate(equations, 2.0);
        assertEquals(2.0, integrator.getCurrentStepStart(), 1e-6);
        assertArrayEquals(new double[]{1.1, 2.2}, integrator.getState(), 1e-6);
        assertTrue(integrator.isLastStep());
        assertFalse(integrator.resetOccurred);
        assertEquals(1, integrator.getEvaluations());
    }

    static class MockEventHandler implements EventHandler {

        private boolean stop;
        private boolean reset;

        @Override
        public void init(double t, double[] y, double tEnd) {
            stop = false;
            reset = false;
        }

        @Override
        public void evaluateState(double t, double[] y, double tEnd, double[] yDot) {
        }

        public boolean evaluateStep(AbstractStepInterpolator interpolator) {
            return false;
        }

        public void stepAccepted(double t, double[] y) {
            this.t = t;
            this.y = y;
            if (stop) {
                throw new RuntimeException("Integration stopped by event.");
            }
            if (reset) {
                Arrays.fill(y, 0.0);
            }
        }

        public void stop() {
            this.stop = true;
        }

        public void reset(double t, double[] y) {
            this.reset = true;
        }

    }

    static class MockStepHandler implements StepHandler {

        @Override
        public void handleStep(AbstractStepInterpolator interpolator, boolean isLastStep) {
            if (isLastStep) {
                throw new RuntimeException("Integration stopped by step handler.");
            }
        }

    }

}