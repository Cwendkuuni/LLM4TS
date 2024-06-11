package org.apache.commons.math.ode.nonstiff.test;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.DefaultEventHandler;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.EventRate;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegratorTest;
import org.apache.commons.math3.ode.sampling.RungeKuttaStepInterpolator;
import org.junit.Assert;
import org.junit.Test;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testEmbeddedRungeKuttaIntegrator() throws Exception {
        final double minStep = 0.001;
        final double maxStep = 10.0;
        final double scalAbsoluteTolerance = 1e-6;
        final double scalRelativeTolerance = 1e-6;

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaRK23Integrator(
                "RK23", false, new double[]{0.25, 0.5, 0.875, 0.875, 1.0},
                new double[][]{{0.25, 0.25}, {0.5, 0.25}, {0.875, 0.5}, {0.875, 0.5}, {1.0, 1.0}},
                new double[]{0.25, 0.45, 0.729, 0.87}, new RungeKuttaStepInterpolator());
        integrator.setMinStep(minStep);
        integrator.setMaxStep(maxStep);
        integrator.setScaledAbsoluteTolerance(scalAbsoluteTolerance);
        integrator.setScaledRelativeTolerance(scalRelativeTolerance);

        FirstOrderDifferentialEquations equations = new SimpleDifferentialEquations();

        double t0 = 0.0;
        double[] y0 = new double[]{1.0, 1.0};
        double t = 2.0;
        double[] y = new double[]{2.0, 2.0};

        integrator.integrate(equations, t0, y0, t, y);

        double expectedY1 = 2.32698283515625;
        double expectedY2 = 2.32698283515625;

        Assert.assertEquals("y1 is not correct.", expectedY1, y[0], 1e-6);
        Assert.assertEquals("y2 is not correct.", expectedY2, y[1], 1e-6);

        // Test with an event
        double eventTime = 1.5;
        DefaultEventHandler eventHandler = new DefaultEventHandler(new Event(
                new SimpleDifferentialEquations(), EventRate.UNLIMITED, eventTime, new Object()));
        integrator.setEventsHandlerManager(eventHandler);

        integrator.integrate(equations, t0, y0, t, y);

        Assert.assertEquals("Event has not been triggered.", 1, eventHandler.getNumberEventsFired());

        // Test with a maximum step
        integrator.setMaxStep(1.0);
        integrator.integrate(equations, t0, y0, t, y);

        Assert.assertArrayEquals("Integration failed.", new double[]{2.0, 2.0}, y);

        // Test with a small step
        integrator.setMinStep(0.01);
        integrator.integrate(equations, t0, y0, t, y);

        Assert.assertArrayEquals("Integration failed.", new double[]{2.00198917399527, 2.00198917399527}, y);

        // Test with a large step
        integrator.setMaxStep(100.0);
        integrator.integrate(equations, t0, y0, t, y);

        Assert.assertArrayEquals("Integration failed.", new double[]{2.32698283515625, 2.32698283515625}, y);

        // Test with a small absolute tolerance
        integrator.setScaledAbsoluteTolerance(1e-10);
        integrator.integrate(equations, t0, y0, t, y);

        Assert.assertArrayEquals("Integration failed.", new double[]{2.32698283515625, 2.32698283515625}, y);

        // Test with a small relative tolerance
        integrator.setScaledRelativeTolerance(1e-10);
        integrator.integrate(equations, t0, y0, t, y);

        Assert.assertArrayEquals("Integration failed.", new double[]{2.32698283515625, 2.32698283515625}, y);

        // Test with a small step size
        integrator.setMinStep(0.0001);
        integrator.integrate(equations, t0, y0, t, y);

        Assert.assertArrayEquals("Integration failed.", new double[]{2.32698283515625, 2.32698283515625}, y);

        // Test with a large step size
        integrator.setMaxStep(1000.0);
        integrator.integrate(equations, t0, y0, t, y);

        Assert.assertArrayEquals("Integration failed.", new double[]{2.32698283515625, 2.32698283515625}, y);

        // Test with a large error
        integrator.setScaledRelativeTolerance(1.0);
        integrator.integrate(equations, t0, y0, t, y);

        MaxCountExceededException maxCountExceededException = null;
        try {
            integrator.integrate(equations, t0, y0, t, y);
        } catch (MaxCountExceededException e) {
            maxCountExceededException = e;
        }

        Assert.assertNotNull("MaxCountExceededException not thrown.", maxCountExceededException);

        // Test with a negative step size
        integrator.integrate(equations, t0, y0, t, y);
        integrator.setStepSize(-1.0);
        integrator.integrate(equations, t0, y0, t, y);

        NumberIsTooSmallException numberIsTooSmallException = null;
        try {
            integrator.integrate(equations, t0, y0, t, y);
        } catch (NumberIsTooSmallException e) {
            numberIsTooSmallException = e;
        }

        Assert.assertNotNull("NumberIsTooSmallException not thrown.", numberIsTooSmallException);

    }

}

class SimpleDifferentialEquations implements FirstOrderDifferentialEquations {

    @Override
    public int getDimension() {
        return 2;
    }

    @Override
    public void computeDerivatives(double t, double[] y, double[] yPrime) {
        yPrime[0] = y[1];
        yPrime[1] = y[1];
    }

}