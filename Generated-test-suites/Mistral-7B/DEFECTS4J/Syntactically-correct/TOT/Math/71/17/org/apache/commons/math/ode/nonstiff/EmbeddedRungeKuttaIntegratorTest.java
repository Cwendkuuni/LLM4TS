package org.apache.commons.math.ode.nonstiff.test;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.EventException;
import org.apache.commons.math3.ode.events.WelcomeEvent;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegratorTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

public class EmbeddedRungeKuttaIntegratorTest {

    private EmbeddedRungeKuttaIntegrator integrator;
    private FirstOrderDifferentialEquations equations;
    private double[] initialValues;
    private double startTime, endTime;

    @Before
    public void setUp() {
        integrator = new RungeKuttaFehlberg78Integrator(5, 1.0e-8, 1.0e-6);
        equations = new DummyDifferentialEquations();
        initialValues = new double[]{1.0, 1.0};
        startTime = 0.0;
        endTime = 1.0;
    }

    @Test
    public void testSimpleIntegration() {
        double[] y = new double[]{1.0, 1.0};
        integrator.integrate(equations, startTime, initialValues, endTime, y);
        Assert.assertEquals(2.718281828, y[0], 1.0e-6);
        Assert.assertEquals(2.718281828, y[1], 1.0e-6);
    }

    @Test(expected = DeriveException.class)
    public void testFailureWithDerivativeException() {
        equations = new DummyDifferentialEquationsWithDerivativeException();
        integrator.integrate(equations, startTime, initialValues, endTime, initialValues);
    }

    @Test(expected = IntegratorException.class)
    public void testFailureWithIntegratorException() {
        equations = new DummyDifferentialEquationsWithIntegratorException();
        integrator.integrate(equations, startTime, initialValues, endTime, initialValues);
    }

    @Test
    public void testIntegrationWithEvent() {
        equations = new DummyDifferentialEquationsWithEvent();
        StepHandler handler = new EventDetectionHandler();
        integrator.addStepHandler(handler);
        double[] y = new double[]{1.0, 1.0};
        integrator.integrate(equations, startTime, initialValues, endTime, y);
        Assert.assertEquals(Event.EVENT_REACHED, handler.getEventType());
    }

    @Test
    public void testIntegrationWithEvents() {
        equations = new DummyDifferentialEquationsWithMultipleEvents();
        StepHandler handler = new EventDetectionHandler();
        integrator.addStepHandler(handler);
        double[] y = new double[]{1.0, 1.0};
        integrator.integrate(equations, startTime, initialValues, endTime, y);
        Assert.assertEquals(Event.EVENT_REACHED, handler.getEventType());
    }

    @Test
    public void testIntegrationWithDenseOutput() {
        integrator = new RungeKuttaFehlberg78Integrator(5, 1.0e-8, 1.0e-6, true);
        double[] y = new double[3];
        double time = 0.0;
        for (int i = 0; i < 10; i++) {
            integrator.reset();
            integrator.integrate(equations, time, initialValues, time + 0.1, y);
        }
        StepInterpolator interpolator = integrator.getInterpolator();
        for (int i = 0; i < y.length; i++) {
            Assert.assertTrue(Math.abs(interpolator.interpolate(time, i) - y[i]) < 1.0e-6);
        }
    }

    @Test
    public void testIntegrationWithLargeStep() {
        integrator = new RungeKuttaMidpointIntegrator(1, 1.0e-8, 1.0e-6);
        double[] y = new double[]{1.0, 1.0};
        double start = 0.0;
        double stop  = 10000.0;
        integrator.integrate(equations, start, initialValues, stop, y);
        Assert.assertEquals(1.974641397e+05, y[0], 1.0e-6);
        Assert.assertEquals(1.974641397e+05, y[1], 1.0e-6);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testFailureWithSmallStep() {
        integrator = new RungeKuttaMidpointIntegrator(1, 1.0e-16, 1.0e-6);
        double[] y = new double[]{1.0, 1.0};
        double start = 0.0;
        double stop  = 0.01;
        integrator.integrate(equations, start, initialValues, stop, y);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testFailureWithNegativeStep() {
        integrator = new RungeKuttaMidpointIntegrator(1, 1.0e-6, 1.0e-6);
        double[] y = new double[]{1.0, 1.0};
        double start = 0.0;
        double stop  = -0.01;
        integrator.integrate(equations, start, initialValues, stop, y);
    }

    static class DummyDifferentialEquations implements FirstOrderDifferentialEquations {

        @Override
        public int getDimension() {
            return 2;
        }

        @Override
        public void computeDerivatives(double t, double[] y, double[] yPrime) {
            for (int i = 0; i < 2; i++) {
                yPrime[i] = y[i] * 2.0;
            }
        }

    }

    static class DummyDifferentialEquationsWithDerivativeException
            extends DummyDifferentialEquations {

        @Override
        public void computeDerivatives(double t, double[] y, double[] yPrime) {
            throw new DerivativeException();
        }

    }

    static class DummyDifferentialEquationsWithIntegratorException
            extends DummyDifferentialEquations {

        @Override
        public void computeDerivatives(double t, double[] y, double[] yPrime) {
            throw new IntegratorException();
        }

    }

    static class DummyDifferentialEquationsWithEvent extends DummyDifferentialEquations {

        private double eventTime;

        public DummyDifferentialEquationsWithEvent() {
            eventTime = 2.0;
        }

        @Override
        public void computeDerivatives(double t, double[] y, double[] yPrime) {
            if (t >= eventTime) {
                throw new EventException(new WelcomeEvent());
            }
            for (int i = 0; i < 2; i++) {
                yPrime[i] = y[i] * 2.0;
            }
        }

    }

    static class DummyDifferentialEquationsWithMultipleEvents extends DummyDifferentialEquations {

        private double eventTime1, eventTime2;

        public DummyDifferentialEquationsWithMultipleEvents() {
            eventTime1 = 2.0;
            eventTime2 = 4.0;
        }

        @Override
        public void computeDerivatives(double t, double[] y, double[] yPrime) {
            if (t >= eventTime1 && t <= eventTime2) {
                throw new EventException(new WelcomeEvent());
            }
            for (int i = 0; i < 2; i++) {
                yPrime[i] = y[i] * 2.0;
            }
        }

    }

    static class EventDetectionHandler implements StepHandler {

        private int eventType;

        @Override
        public void handleStep(StepInterpolator interpolator, int stepNumber) {
            double[] y = interpolator.getY();
            if (y[0] > 2.0) {
                eventType = Event.EVENT_REACHED;
            }
        }

        public int getEventType() {
            return eventType;
        }

    }

}