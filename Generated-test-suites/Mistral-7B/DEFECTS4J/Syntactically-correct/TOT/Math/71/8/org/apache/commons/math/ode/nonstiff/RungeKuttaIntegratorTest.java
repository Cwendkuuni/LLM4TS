package org.apache.commons.math.ode.nonstiff;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.PrecisionException;
import org.apache.commons.math3.exception.ZeroException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.When;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

/**
 * Test for {@link RungeKuttaIntegrator}.
 */
public class RungeKuttaIntegratorTest {

    /**
     * Test for {@link RungeKuttaIntegrator#integrate(FirstOrderDifferentialEquations, double, double[], double, double[])}.
     */
    @Test
    public void testIntegrate() {
        final FirstOrderDifferentialEquations equations = new TestEquations();
        final RungeKuttaIntegrator integrator = new EulerIntegrator(0.1, new EulerStepInterpolator(), 0.1);
        final double[] result = new double[equations.getDimension()];
        final double[] initialValue = new double[]{1.0, 1.0};

        // test with a simple equation
        final double tEnd = 10.0;
        final double stepSize = 1.0;
        assertEquals(tEnd, integrator.integrate(equations, 0.0, initialValue, tEnd, result), 1.e-6);

        // test with an equation having a singularity at t = 1
        final double[] singularPoint = new double[]{1.0, 1.0};
        try {
            integrator.integrate(equations, 0.0, initialValue, 1.0, result);
            fail("Expected an IntegratorException");
        } catch (IntegratorException e) {
            // expected
        }

        // test with an equation having a singularity at t = 1, but with a smaller step size
        final double stepSizeSmaller = 0.5;
        try {
            integrator.integrate(equations, 0.0, initialValue, 1.0, result, stepSizeSmaller);
            fail("Expected an IntegratorException");
        } catch (IntegratorException e) {
            // expected
        }

        // test with an equation having a singularity at t = 1, but with a larger step size
        final double stepSizeLarger = 2.0;
        try {
            integrator.integrate(equations, 0.0, initialValue, 1.0, result, stepSizeLarger);
            fail("Expected an IntegratorException");
        } catch (IntegratorException e) {
            // expected
        }

        // test with an equation having an event at t = 2
        final StepHandler stepHandler = new StepHandler() {
            @Override
            public void handleStep(StepContext context) {
                assertNotNull(context.getY());
                assertEquals(2.0, context.getStepSize(), 1.e-6);
            }
        };
        final CombinedEventsManager eventsManager = new CombinedEventsManager();
        eventsManager.addEvent(new When(2.0, new Event() {
            @Override
            public int getCode() {
                return 0;
            }
        }));
        final RungeKuttaIntegrator integratorWithEvents = new MidpointIntegrator(0.1, new MidpointStepInterpolator(), 0.1, eventsManager);
        integratorWithEvents.addStepHandler(stepHandler);
        integratorWithEvents.integrate(equations, 0.0, initialValue, 3.0, result);

        // test with an equation having an event at t = 2, but with a smaller step size
        final RungeKuttaIntegrator integratorWithEventsSmaller = new MidpointIntegrator(0.1, new MidpointStepInterpolator(), 0.1, eventsManager);
        integratorWithEventsSmaller.setStepSize(0.5);
        integratorWithEventsSmaller.addStepHandler(stepHandler);
        integratorWithEventsSmaller.integrate(equations, 0.0, initialValue, 3.0, result);

        // test with an equation having an event at t = 2, but with a larger step size
        final RungeKuttaIntegrator integratorWithEventsLarger = new MidpointIntegrator(0.1, new MidpointStepInterpolator(), 0.1, eventsManager);
        integratorWithEventsLarger.setStepSize(2.0);
        integratorWithEventsLarger.addStepHandler(stepHandler);
        try {
            integratorWithEventsLarger.integrate(equations, 0.0, initialValue, 3.0, result);
            fail("Expected an IntegratorException");
        } catch (IntegratorException e) {
            // expected
        }

        // test with an equation having an event at t = 1, but with a step size that will trigger the event at the next step
        final double stepSizeEvent = 1.1;
        try {
            integratorWithEvents.setStepSize(stepSizeEvent);
            integratorWithEvents.integrate(equations, 0.0, initialValue, 2.0, result);
            fail("Expected an IntegratorException");
        } catch (IntegratorException e) {
            // expected
        }

        // test with an equation having an event at t = 1, but with a step size that will trigger the event in the middle of the step
        final double stepSizeEventMiddle = 1.5;
        try {
            integratorWithEvents.setStepSize(stepSizeEventMiddle);
            integratorWithEvents.integrate(equations, 0.0, initialValue, 2.0, result);
            fail("Expected an IntegratorException");
        } catch (IntegratorException e) {
            // expected
        }

        // test with an equation having an event at t = 1, but with a step size that will not trigger the event during the step
        final double stepSizeEventFar = 3.0;
        integratorWithEvents.setStepSize(stepSizeEventFar);
        integratorWithEvents.integrate(equations, 0.0, initialValue, 2.0, result);
        assertFalse(eventsManager.hasBeenTriggered());

        // test with an equation having a singularity at t = 0, but with a non-zero initial value
        final double[] initialValueSingularity = new double[]{1.0 / 3.0, 1.0};
        try {
            integrator.integrate(equations, 0.0, initialValueSingularity, 1.0, result);
            fail("Expected a PrecisionException or a MaxCountExceededException");
        } catch (PrecisionException | MaxCountExceededException e) {
            // expected
        }

        // test with an equation having a singularity at t = 0, but with a zero initial value
        final double[] initialValueSingularityZero = new double[]{0.0, 1.0};
        try {
            integrator.integrate(equations, 0.0, initialValueSingularityZero, 1.0, result);
            fail("Expected a ZeroException");
        } catch (ZeroException e) {
            // expected
        }

        // test with an equation having a singularity at t = 0, but with a very small initial value
        final double[] initialValueSingularitySmall = new double[]{1.e-10, 1.0};
        try {
            integrator.integrate(equations, 0.0, initialValueSingularitySmall, 1.0, result);
            fail("Expected a PrecisionException or a MaxCountExceededException");
        } catch (PrecisionException | MaxCountExceededException e) {
            // expected
        }

        // test with an equation having a singularity at t = 0, but with a very large initial value
        final double[] initialValueSingularityLarge = new double[]{1.e10, 1.0};
        try {
            integrator.integrate(equations, 0.0, initialValueSingularityLarge, 1.0, result);
            fail("Expected a PrecisionException or a MaxCountExceededException");
        } catch (PrecisionException | MaxCountExceededException e) {
            // expected
        }

        // test with an equation having an infinite solution at t = 0
        final double[] initialValueInfinite = new double[]{1.0, -1.0};
        try {
            integrator.integrate(equations, 0.0, initialValueInfinite, 1.0, result);
            fail("Expected an IntegratorException.");
        } catch (IntegratorException e) {
            // expected
        }

        // test with an equation having a solution that is not defined at t = 0
        final double[] initialValueUndefined = new double[]{1.0, Math.sqrt(-1.0)};
        try {
            integrator.integrate(equations, 0.0, initialValueUndefined, 1.0, result);
            fail("Expected an IntegratorException.");
        } catch (IntegratorException e) {
            // expected
        }

        // test with an equation having a solution that is not differentiable at t = 0
        final double[] initialValueNotDifferentiable = new double[]{1.0, Math.abs(Math.PI)};
        try {
            integrator.integrate(equations, 0.0, initialValueNotDifferentiable, 1.0, result);
            fail("Expected an IntegratorException.");
        } catch (IntegratorException e) {
            // expected
        }

        // test with an equation having a singularity at t = 2
        final double[] singularPoint2 = new double[]{1.0, 1.0};
        try {
            integrator.integrate(equations, 0.0, initialValue, 2.0, result);
            fail("Expected an IntegratorException");
        } catch (IntegratorException e) {
            // expected
        }

        // test with an equation having a singularity at t = 2, but with a smaller step size
        try {
            integrator.integrate(equations, 0.0, initialValue, 2.0, result, 0.5);
            fail("Expected an IntegratorException");
        } catch (IntegratorException e) {
            // expected
        }

        // test with an equation having a singularity at t = 2, but with a larger step size
        try {
            integrator.integrate(equations, 0.0, initialValue, 2.0, result, 2.0);
            fail("Expected an IntegratorException");
        } catch (IntegratorException e) {
            // expected
        }

        // test with an equation having a singularity at t = 2, but with a very small step size
        integrator.setStepSize(0.1);
        try {
            integrator.integrate(equations, 0.0, initialValue, 2.0, result);
            fail("Expected an IntegratorException");
        } catch (IntegratorException e) {
            // expected
        }

        // test with an equation having a singularity at t = 2, but with a very large step size
        integrator.setStepSize(10.0);
        try {
            integrator.integrate(equations, 0.0, initialValue, 2.0, result);
            fail("Expected an IntegratorException");
        } catch (IntegratorException e) {
            // expected
        }

        // test with an equation having a singularity at t = 2, but with a step size that will trigger the singularity at the next step
        integrator.setStepSize(1.5);
        try {
            integrator.integrate(equations, 0.0, initialValue, 2.0, result);
            fail("Expected an IntegratorException");
        } catch (IntegratorException e) {
            // expected
        }

        // test with an equation having a singularity at t = 2, but with a step size that will trigger the singularity in the middle of the step
        integrator.setStepSize(3.5);
        try {
            integrator.integrate(equations, 0.0, initialValue, 2.0, result);
            fail("Expected an IntegratorException");
        } catch (IntegratorException e) {
            // expected
        }

        // test with an equation having a singularity at t = 2, but with a step size that will not trigger the singularity during the step
        integrator.setStepSize(4.0);
        integrator.integrate(equations, 0.0, initialValue, 2.0, result);
        assertFalse(integrator.hasCompletedIntegration());

    }

}

class TestEquations implements FirstOrderDifferentialEquations {

    @Override
    public int getDimension() {
        return 2;
    }

    @Override
    public void computeDerivatives(double t, double[] y, double[] yPrime) {
        final double factor = Math.exp(-t);
        yPrime[0] = y[1] * factor;
        yPrime[1] = -y[0] * factor;
    }

}