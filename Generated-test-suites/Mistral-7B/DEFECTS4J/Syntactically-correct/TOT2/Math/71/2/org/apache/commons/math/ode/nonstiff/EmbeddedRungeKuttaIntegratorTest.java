package org.apache.commons.math.ode.nonstiff;

import org.apache.commons.math3.exception.MathRuntimeException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.When;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Test cases for {@link EmbeddedRungeKuttaIntegrator}.
 */
public class EmbeddedRungeKuttaIntegratorTest {

    /**
     * Test the basic functionality of the integrator.
     */
    @Test
    public void testBasicFunctionality() {
        final String name = "EmbeddedRungeKuttaIntegratorTest";
        double[] initialCondition = new double[] { 1.0, 1.0 };
        double timeStart = 0.0;
        double timeEnd = 5.0;
        double stepSize = 0.1;
        double absTol = 1e-12;
        double relTol = 1e-12;

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKutta45Integrator(name, true,
                new double[] { 0.25, 0.45, 0.6, 1.0 },
                new double[][] { { 0.25, 0.45 }, { 0.25, 0.45, 0.6, 1.0 } },
                new double[] { 0.25, 0.45, 0.6, 1.0 },
                new RungeKuttaStepInterpolator(), 0.01, 1.0, absTol, relTol);

        // Integrate the system
        double[] y = integrator.integrate(new MyDifferentialEquations(initialCondition), timeStart, initialCondition, timeEnd, null);

        // Check the result
        double[] expected = new double[] { 2.718281828, 2.718281828 };
        assertEquals(expected, y, 1e-12);
    }

    /**
     * Test the behavior when the integrator encounters an event.
     */
    @Test
    public void testEventHandling() {
        final String name = "EmbeddedRungeKuttaIntegratorTest";
        double[] initialCondition = new double[] { 1.0, 1.0 };
        double timeStart = 0.0;
        double timeEnd = 2.0;
        double stepSize = 0.1;
        double absTol = 1e-12;
        double relTol = 1e-12;

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKutta45Integrator(name, true,
                new double[] { 0.25, 0.45, 0.6, 1.0 },
                new double[][] { { 0.25, 0.45 }, { 0.25, 0.45, 0.6, 1.0 } },
                new double[] { 0.25, 0.45, 0.6, 1.0 },
                new RungeKuttaStepInterpolator(), 0.01, 1.0, absTol, relTol);

        // Define an event handler
        StepHandler eventHandler = new StepHandler() {
            @Override
            public void handleStep(AbstractStepInterpolator interpolator, int stepNumber) {
                double time = interpolator.getTime();
                if (time >= 1.2) {
                    throw new IntegratorException("Test event handled.");
                }
            }
        };

        // Add the event handler
        integrator.addStepHandler(eventHandler);

        // Integrate the system
        try {
            integrator.integrate(new MyDifferentialEquations(initialCondition), timeStart, initialCondition, timeEnd, null);
            fail("Expected IntegratorException.");
        } catch (IntegratorException e) {
            // Expected
        }
    }

    /**
     * Test the behavior when the integrator encounters an exception in the differential equations.
     */
    @Test
    public void testExceptionHandling() {
        final String name = "EmbeddedRungeKuttaIntegratorTest";
        double[] initialCondition = new double[] { 1.0, 1.0 };
        double timeStart = 0.0;
        double timeEnd = 1.0;
        double stepSize = 0.1;
        double absTol = 1e-12;
        double relTol = 1e-12;

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKutta45Integrator(name, true,
                new double[] { 0.25, 0.45, 0.6, 1.0 },
                new double[][] { { 0.25, 0.45 }, { 0.25, 0.45, 0.6, 1.0 } },
                new double[] { 0.25, 0.45, 0.6, 1.0 },
                new RungeKuttaStepInterpolator(), 0.01, 1.0, absTol, relTol) {

            @Override
            protected double estimateError(double[][] yDotK, double[] y, double[] y1, double h) {
                throw new MathRuntimeException("Test exception handled.");
            }

        };

        // Integrate the system
        try {
            integrator.integrate(new MyDifferentialEquations(initialCondition), timeStart, initialCondition, timeEnd, null);
            fail("Expected MathRuntimeException.");
        } catch (MathRuntimeException e) {
            // Expected
        }
    }

    /**
     * Test the behavior when the integrator encounters an illegal argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgument() {
        final String name = "EmbeddedRungeKuttaIntegratorTest";
        double[] initialCondition = new double[] { 1.0, 1.0 };
        double timeStart = 0.0;
        double timeEnd = 1.0;
        double stepSize = 0.1;
        double absTol = 1e-12;
        double relTol = 1e-12;

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKutta45Integrator(name, true,
                new double[] { 0.25, 0.45, 0.6, 1.0 },
                new double[][] { { 0.25, 0.45 }, { 0.25, 0.45, 0.6, 1.0 } },
                new double[] { 0.25, 0.45, 0.6, 1.0 },
                new RungeKuttaStepInterpolator(), -1.0, 1.0, absTol, relTol);
    }

    /**
     * Test the behavior when the integrator encounters a null differential equation.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullDifferentialEquations() {
        final String name = "EmbeddedRungeKuttaIntegratorTest";
        double[] initialCondition = new double[] { 1.0, 1.0 };
        double timeStart = 0.0;
        double timeEnd = 1.0;
        double stepSize = 0.1;
        double absTol = 1e-12;
        double relTol = 1e-12;

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKutta45Integrator(name, true,
                new double[] { 0.25, 0.45, 0.6, 1.0 },
                new double[][] { { 0.25, 0.45 }, { 0.25, 0.45, 0.6, 1.0 } },
                new double[] { 0.25, 0.45, 0.6, 1.0 },
                new RungeKuttaStepInterpolator(), 0.01, 1.0, absTol, relTol);

        integrator.integrate(null, timeStart, initialCondition, timeEnd, null);
    }

    /**
     * Test the behavior when the integrator encounters a system with no state variables.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNoStateVariables() {
        final String name = "EmbeddedRungeKuttaIntegratorTest";
        double[] initialCondition = null;
        double timeStart = 0.0;
        double timeEnd = 1.0;
        double stepSize = 0.1;
        double absTol = 1e-12;
        double relTol = 1e-12;

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKutta45Integrator(name, true,
                new double[] { 0.25, 0.45, 0.6, 1.0 },
                new double[][] { { 0.25, 0.45 }, { 0.25, 0.45, 0.6, 1.0 } },
                new double[] { 0.25, 0.45, 0.6, 1.0 },
                new RungeKuttaStepInterpolator(), 0.01, 1.0, absTol, relTol);

        integrator.integrate(new MyDifferentialEquationsNoStateVariables(), timeStart, initialCondition, timeEnd, null);
    }

    /**
     * Differential equations for test purposes.
     */
    private static class MyDifferentialEquations implements FirstOrderDifferentialEquations {

        private double[] state;

        public MyDifferentialEquations(double[] initialState) {
            this.state = initialState;
        }

        @Override
        public int getDimension() {
            return state.length;
        }

        @Override
        public void computeDerivatives(double t, double[] y, double[] yDot) {
            Arrays.fill(yDot, 1.0);
        }
    }

    /**
     * Differential equations with no state variables for test purposes.
     */
    private static class MyDifferentialEquationsNoStateVariables implements FirstOrderDifferentialEquations {

        @Override
        public int getDimension() {
            return 0;
        }

        @Override
        public void computeDerivatives(double t, double[] y, double[] yDot) {
            // Not reached
        }
    }

    /**
     * Event for test purposes.
     */
    private static class MyEvent extends Event {

        public MyEvent(double t, double[] y, double[] value) {
            super(new When(t), null, value);
        }
    }

    /**
     * Events manager for test purposes.
     */
    private static class MyEventsManager extends CombinedEventsManager {

        @Override
        public boolean evaluateStep(AbstractStepInterpolator interpolator) {
            addEvent(new MyEvent(1.2, null, new double[] { 1.0, 1.0 }));
            return false;
        }

        @Override
        public List<Event> getEvents() {
            return Collections.emptyList();
        }

        @Override
        public void clearEvents() {
            // Not reached
        }

        @Override
        public void stepAccepted(double nextTime, double[] nextState) {
            // Not reached
        }
    }
}