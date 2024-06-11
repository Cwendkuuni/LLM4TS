package org.apache.commons.math.ode.nonstiff.test;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.NonstiffIntegrator;
import org.apache.commons.math3.ode.events.RelativetimeEvent;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class RungeKuttaIntegratorTest {

    private RungeKuttaIntegrator integrator;
    private FirstOrderDifferentialEquations equations;
    private double[] initialCondition;

    @Before
    public void setUp() {
        double[] c = {0.0, 0.25, 0.5, 0.875};
        double[][] a = {{0.0, 0.0}, {0.25, 0.5}, {0.5, 0.5}, {0.875, 0.25}};
        double[] b = {0.6, 0.2, 0.2, 0.1};
        double step = 0.1;
        integrator = new RK2Integrator("RK2Integrator", c, a, b, new RK2StepInterpolator(), step);
        equations = new TestDifferentialEquations();
        initialCondition = new double[] {1.0, 1.0};
    }

    @After
    public void tearDown() {
        integrator = null;
        equations = null;
        initialCondition = null;
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testConstructorWithZeroStepSize() {
        new RungeKuttaIntegrator("RK2Integrator", new double[] {0.0, 0.25}, new double[][] {{0.0, 0.0}, {0.25, 0.5}}, new double[] {0.6, 0.2}, new RK2StepInterpolator(), 0.0);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testConstructorWithNegativeStepSize() {
        new RungeKuttaIntegrator("RK2Integrator", new double[] {0.0, 0.25}, new double[][] {{0.0, 0.0}, {0.25, 0.5}}, new double[] {0.6, 0.2}, new RK2StepInterpolator(), -0.1);
    }

    @Test
    public void testIntegrateSimpleCase() throws Exception {
        double t = 2.0;
        double[] y = integrator.integrate(equations, 0.0, initialCondition, t, new double[initialCondition.length]);
        assertEquals(t, integrator.getStepStart(), 1e-12);
        assertEquals(t, integrator.getStepSize(), 1e-12);
        assertArrayEquals(new double[] {2.06617231, 2.12252523}, y, 1e-6);
    }

    @Test
    public void testIntegrateWithEvent() throws Exception {
        double tEvent = 1.2;
        double[] yEvent = new double[] {1.5, 1.5};
        StepHandler stepHandler = new StepHandler() {
            @Override
            public void handleStep(AbstractStepInterpolator interpolator, boolean lastStep) {
                if (lastStep) {
                    assertTrue(interpolator.getTime() > tEvent - 1e-6 && interpolator.getTime() < tEvent + 1e-6);
                }
            }
        };
        integrator.addStepHandler(stepHandler);
        double t = 2.0;
        double[] y = integrator.integrate(equations, 0.0, initialCondition, t, new double[initialCondition.length]);
        assertEquals(t, integrator.getStepStart(), 1e-12);
        assertEquals(t, integrator.getStepSize(), 1e-12);
        assertArrayEquals(new double[] {2.06617231, 2.12252523}, y, 1e-6);
        assertNotNull(integrator.getLastStepHandlerEvent());
        assertArrayEquals(yEvent, integrator.getLastStepHandlerEvent().getData(), 1e-6);
        assertFalse(integrator.hasEvents());
    }

    @Test
    public void testIntegrateWithRelativetimeEvent() throws Exception {
        double t0 = 1.0;
        double tEnd = 3.0;
        double[] y0 = initialCondition;
        double[] yEnd = new double[] {2.0, 1.5};
        RelativetimeEvent event = new RelativetimeEvent(tEnd, 0.5);
        integrator.addEvent(event);
        double[] output = new double[yEnd.length];
        integrator.integrate(equations, t0, y0, tEnd, output);
        assertEquals(tEnd, integrator.getStepStart(), 1e-12);
        assertArrayEquals(yEnd, output, 1e-6);
        assertEquals(1, integrator.getEventCount());
        assertArrayEquals(event.getData(), integrator.getLastEvent().getData(), 1e-6);
    }

    @Test(expected = IntegratorException.class)
    public void testIntegrateWithTooSmallStepSize() throws Exception {
        integrator = new RK4Integrator("RK4Integrator", new double[] {0.0, 0.25, 0.25, 0.5, 0.25, 0.25}, new double[][] {{0.0, 0.0}, {0.25, 0.5}, {0.25, 0.5}, {0.5, 0.5}, {0.25, 0.5}, {0.25, 0.5}}, new double[] {0.6, 0.2, 0.2, 0.2, 0.2, 0.2}, new RK4StepInterpolator(), 1e-11);
        integrator.integrate(equations, 0.0, initialCondition, 1.0, new double[initialCondition.length]);
    }

    @Test(expected = IntegratorException.class)
    public void testIntegrateWithTooLargeStepSize() throws Exception {
        integrator = new RK2Integrator("RK2Integrator", new double[] {0.0, 0.25}, new double[][] {{0.0, 0.0}, {0.25, 0.5}}, new double[] {0.6, 0.2}, new RK2StepInterpolator(), 10.0);
        integrator.integrate(equations, 0.0, initialCondition, 1.0, new double[initialCondition.length]);
    }

}

class TestDifferentialEquations implements FirstOrderDifferentialEquations {

    @Override
    public int getDimension() {
        return 2;
    }

    @Override
    public void computeDerivatives(double t, double[] y, double[] yPrime) {
        yPrime[0] = y[1];
        yPrime[1] = -y[0] + y[1] * y[1];
    }

}