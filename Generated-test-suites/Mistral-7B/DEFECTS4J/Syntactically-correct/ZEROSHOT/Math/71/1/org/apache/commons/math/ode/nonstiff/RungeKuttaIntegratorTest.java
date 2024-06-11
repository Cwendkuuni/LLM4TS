package org.apache.commons.math.ode.nonstiff.test;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NoSuchElementException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.When;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class RungeKuttaIntegratorTest {

    private RungeKuttaIntegrator integrator;
    private FirstOrderDifferentialEquations equations;
    private double[] initialValues;

    @Before
    public void setUp() {
        // Initialize the differential equations to be integrated
        equations = new MyDifferentialEquations();
        initialValues = new double[]{1.0, 1.0};

        // Create a test integrator with a fixed step size
        integrator = new MyRungeKuttaIntegrator(0.1, 0.2, 0.3, 0.4, 0.1, new MyRungeKuttaStepInterpolator());
    }

    @Test
    public void testIntegrate() throws Exception {
        double tStart = 0.0;
        double tEnd = 10.0;

        // Integrate the differential equations over the given time range
        double[] y = integrator.integrate(equations, tStart, initialValues, tEnd);

        // Check that the integration was successful
        assertNotNull(y);
        assertEquals(y.length, 2);

        // Check the results against the analytical solution
        double[] analyticalSolution = new double[]{3.732050807568877, 2.716923398550511};
        for (int i = 0; i < y.length; i++) {
            assertEquals(analyticalSolution[i], y[i], 1e-10);
        }
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testNegativeStepSize() {
        integrator = new MyRungeKuttaIntegrator(0.1, 0.2, 0.3, 0.4, 0.1, new MyRungeKuttaStepInterpolator());
        integrator.setStep(0.1 * -1.0);
    }

    @Test(expected = IntegratorException.class)
    public void testDerivativeException() {
        equations = new DerivativeExceptionDifferentialEquations();
        integrator = new MyRungeKuttaIntegrator(0.1, 0.2, 0.3, 0.4, 0.1, new MyRungeKuttaStepInterpolator());
        integrator.integrate(equations, 0.0, new double[]{1.0, 1.0}, 1.0, new double[2]);
    }

    @Test(expected = IntegratorException.class)
    public void testIntegratorException() {
        equations = new IntegratorExceptionDifferentialEquations();
        integrator = new MyRungeKuttaIntegrator(0.1, 0.2, 0.3, 0.4, 0.1, new MyRungeKuttaStepInterpolator());
        integrator.integrate(equations, 0.0, new double[]{1.0, 1.0}, 1.0, new double[2]);
    }

    @Test(expected = NoSuchElementException.class)
    public void testEndTimeReached() {
        equations = new EndTimeReachedDifferentialEquations();
        integrator = new MyRungeKuttaIntegrator(0.1, 0.2, 0.3, 0.4, 0.1, new MyRungeKuttaStepInterpolator());
        integrator.integrate(equations, 0.0, new double[]{1.0, 1.0}, 10.1, new double[2]);
    }

    @Test(expected = NoSuchElementException.class)
    public void testEndTimeReachedWithZeroStep() {
        equations = new EndTimeReachedDifferentialEquations();
        integrator = new MyRungeKuttaIntegrator(0.1, 0.2, 0.3, 0.4, 0.1, new MyRungeKuttaStepInterpolator());
        integrator.setStep(0.0);
        integrator.integrate(equations, 0.0, new double[]{1.0, 1.0}, 1.0, new double[2]);
    }

    @Test(expected = When.class)
    public void testEventHandling() throws Exception {
        final double eventTime = 1.5;
        equations = new EventDifferentialEquations(new When(eventTime));
        integrator = new MyRungeKuttaIntegrator(0.1, 0.2, 0.3, 0.4, 0.1, new MyRungeKuttaStepInterpolator());

        double[] y = integrator.integrate(equations, 0.0, new double[]{1.0, 1.0}, 2.0, new double[2]);

        // Check the number of event notifications
        assertEquals(1, integrator.getNumEventNotifications());

        // Check the event time and state
        Event event = integrator.getEvent(0);
        assertEquals(eventTime, event.getTime(), 1e-10);
        assertEquals(y[0], event.getValue()[0], 1e-10);
        assertEquals(y[1], event.getValue()[1], 1e-10);
    }

    static class MyDifferentialEquations implements FirstOrderDifferentialEquations {
        @Override
        public int getDimension() {
            return 2;
        }

        @Override
        public void computeDerivatives(double t, double[] y, double[] yPrime) {
            yPrime[0] = y[1];
            yPrime[1] = 2.0 * y[0];
        }
    }

    static class MyRungeKuttaIntegrator extends RungeKuttaIntegrator {
        public MyRungeKuttaIntegrator(double step, double[] c, double[][] a, double[] b,
                                      RungeKuttaStepInterpolator prototype, double stepSize) {
            super(RungeKuttaIntegratorTest.class.getSimpleName(), c, a, b, prototype, stepSize);
            this.step = step;
        }
    }

    static class MyRungeKuttaStepInterpolator extends RungeKuttaStepInterpolator {

    }

    static class DerivativeExceptionDifferentialEquations implements FirstOrderDifferentialEquations {
        @Override
        public int getDimension() {
            return 2;
        }

        @Override
        public void computeDerivatives(double t, double[] y, double[] yPrime) {
            throw new DerivativeException("Derivatives not defined.");
        }
    }

    static class IntegratorExceptionDifferentialEquations implements FirstOrderDifferentialEquations {
        @Override
        public int getDimension() {
            return 2;
        }

        @Override
        public void computeDerivatives(double t, double[] y, double[] yPrime) {
            throw new IntegratorException("Integration error.");
        }
    }

    static class EndTimeReachedDifferentialEquations implements FirstOrderDifferentialEquations {
        @Override
        public int getDimension() {
            return 2;
        }

        @Override
        public void computeDerivatives(double t, double[] y, double[] yPrime) {
            // No-op
        }
    }

    static class EventDifferentialEquations implements FirstOrderDifferentialEquations {
        private final When event;

        public EventDifferentialEquations(When event) {
            this.event = event;
        }

        @Override
        public int getDimension() {
            return 2;
        }

        @Override
        public void computeDerivatives(double t, double[] y, double[] yPrime) {
            // No-op
        }

        public Event getEvent(int index) {
            return event;
        }
    }
}