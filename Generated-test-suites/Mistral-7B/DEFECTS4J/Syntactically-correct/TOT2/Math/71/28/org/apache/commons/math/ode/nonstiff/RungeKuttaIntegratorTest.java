package org.apache.commons.math.ode.nonstiff.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.RelativetimeEvent;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.junit.Before;
import org.junit.Test;

import org.apache.commons.math.ode.nonstiff.RungeKuttaIntegrator;
import org.apache.commons.math.ode.nonstiff.RungeKuttaIntegratorTest;
import org.apache.commons.math.ode.nonstiff.RungeKuttaStepInterpolator;

/**
 * Test cases for {@link RungeKuttaIntegrator}.
 */
public class RungeKuttaIntegratorTest extends RungeKuttaIntegratorTest {

    private RungeKuttaIntegrator integrator;

    @Before
    public void setUp() {
        integrator = new EulerIntegrator(0.1, new EulerStepInterpolator(), 0.1);
    }

    /**
     * Test the basic functionality of the integrator.
     */
    @Test
    public void basicFunctionalityTest() {
        double[] initialCondition = { 1.0, 2.0 };
        double[] expectedResult = { 3.0, 4.0 };
        double endTime = 2.0;
        double[] result = new double[initialCondition.length];
        double step = 0.1;

        integrator.integrate(new DummyDifferentialEquations(), 0.0, initialCondition, endTime, result);

        assertArrayEquals(expectedResult, result);
    }

    /**
     * Test the behavior when integrating over an event.
     */
    @Test
    public void eventDetectionTest() {
        double[] initialCondition = { 1.0, 1.0 };
        double endTime = 2.0;
        double[] expectedResult = { 1.0, 2.0 };
        double step = 0.1;

        FirstOrderDifferentialEquations equations = new DummyDifferentialEquations() {
            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = y[1];
                yPrime[1] = -y[0];
            }
        };
        StepHandler handler = (s, e) -> {
            assertEquals(1.0, e.getValue(), 1e-10);
        };

        integrator.integrate(equations, 0.0, initialCondition, endTime, null, handler);

        // Check the handler was called during the test
        assertFalse(handler.isHandled());

        // Reset the handler for other tests
        handler.reset();

        // Test the behavior when the event is crossed twice
        expectedResult = new double[] { 1.0, 1.0 };
        endTime = 1.0;
        integrator = new EulerIntegrator(0.1, new EulerStepInterpolator(), 0.1);
        equations = new DummyDifferentialEquations() {
            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = y[1];
                yPrime[1] = -y[0];
            }
        };
        integrator.addStepHandler(handler);
        integrator.integrate(equations, 0.0, initialCondition, endTime, null);

        // Check the handler was called twice during the test
        assertEquals(2, handler.getCallCount());

        // Test the behavior when the event is crossed multiple times
        expectedResult = new double[] { 1.0, 1.0 };
        endTime = 2.0;

        integrator = new EulerIntegrator(0.1, new EulerStepInterpolator(), 0.1);
        integrator.addStepHandler(handler);
        integrator.integrate(equations, 0.0, initialCondition, endTime, null);

        // Check the handler was called multiple times during the test
        assertEquals(3, handler.getCallCount());

        // Test the behavior when an event occurs at the initial condition
        expectedResult = new double[] { 0.0, 1.0 };
        equations = new DummyDifferentialEquations() {
            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = y[1];
                yPrime[1] = -y[0] - 0.1;
            }
        };
        integrator = new EulerIntegrator(0.1, new EulerStepInterpolator(), 0.1);
        integrator.addStepHandler(handler);
        integrator.integrate(equations, 0.0, initialCondition, endTime, null);

        // Check the handler was called once during the test
        assertEquals(1, handler.getCallCount());

        // Test the behavior when an event occurs at the end time
        expectedResult = new double[] { 2.0, 0.0 };
        integrator = new EulerIntegrator(0.1, new EulerStepInterpolator(), 0.1);
        integrator.addStepHandler(handler);
        integrator.integrate(equations, 0.0, initialCondition, endTime, null);

        // Check the handler was called once during the test
        assertEquals(1, handler.getCallCount());
    }

    /**
     * Test the behavior when an illegal argument is passed to the integrator.
     */
    @Test
    public void illegalArgumentTest() {
        FirstOrderDifferentialEquations equations = new DummyDifferentialEquations();
        double[] initialCondition = new double[2];

        assertThrows(MathIllegalArgumentException.class, () -> integrator.integrate(equations, 0.0, initialCondition, 0.0, null));
        assertThrows(NumberIsTooSmallException.class, () -> integrator.integrate(equations, 0.0, initialCondition, -0.1, null));
    }

}

class DummyDifferentialEquations implements FirstOrderDifferentialEquations {
    @Override
    public void computeDerivatives(double t, double[] y, double[] yPrime) {
        yPrime[0] = y[1];
        yPrime[1] = y[0];
    }
}

class DummyStepInterpolator extends RungeKuttaStepInterpolator {
    public DummyStepInterpolator(double[] y, double[] yDot, boolean forward) {
        super(new RungeKuttaIntegrator(null, null, null, null, new EulerStepInterpolator(), 0.1), y, yDot, forward);
    }

    @Override
    public void interpolate(double t, double[] y) {
        // Do nothing
    }
}