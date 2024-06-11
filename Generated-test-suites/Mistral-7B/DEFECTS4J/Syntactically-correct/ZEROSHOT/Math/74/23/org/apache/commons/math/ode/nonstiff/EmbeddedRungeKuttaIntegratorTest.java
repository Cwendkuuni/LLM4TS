package org.apache.commons.math.ode.nonstiff.EmbeddedRungeKuttaIntegrator;

import static org.junit.Assert.*;

import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.DefaultValueEvent;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.EventManager;
import org.apache.commons.math3.ode.events. continous.WhenXReachesValue;
import org.apache.commons.math3.ode.step.StepHandler;
import org.apache.commons.math3.ode.stepinterpolator.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.time.StepContext;
import org.apache.commons.math3.ode.time.StepHandlerAdapter;
import org.apache.commons.math3.ode.time.StepInterpolator;
import org.apache.commons.math3.ode.utils.ODETestUtils;
import org.apache.commons.math3.special.BesselFunctions;
import org.junit.Before;
import org.junit.Test;

public class EmbeddedRungeKuttaIntegratorTest {

    private static final double RELATIVE_TOLERANCE = 1e-12;
    private static final double ABSOLUTE_TOLERANCE = 1e-12;

    private EmbeddedRungeKuttaIntegrator integrator;
    private FirstOrderDifferentialEquations equations;
    private double[] initialValues;

    @Before
    public void setUp() {
        integrator = new RungeKuttaFehlberg78Method();
        equations = new MyDifferentialEquations();
        initialValues = new double[] {0.0, 1.0};
    }

    @Test
    public void testIntegrateForward() throws Exception {
        double[] result = integrator.integrate(equations, 0.0, initialValues, 2.0, new double[2]);
        double expected[] = {2.6692117348351878, 1.112598032291368};
        assertArrayEquals(expected, result, RELATIVE_TOLERANCE, ABSOLUTE_TOLERANCE);
    }

    @Test
    public void testIntegrateBackward() throws Exception {
        double[] result = integrator.integrate(equations, 2.0, initialValues, 0.0, new double[2]);
        double expected[] = {0.0, 1.0};
        assertArrayEquals(expected, result, RELATIVE_TOLERANCE, ABSOLUTE_TOLERANCE);
    }

    @Test(expected = DerivativeException.class)
    public void testIntegrateWithIllegalStep() throws Exception {
        double[] result = integrator.integrate(equations, 0.0, initialValues, -1.0, new double[2]);
    }

    @Test(expected = IntegratorException.class)
    public void testIntegrateWithEvents() throws Exception {
        final double eventTime = 1.5;
        final double expectedY = 0.5;

        EventManager eventManager = new EventManager();
        eventManager.addEvent(new WhenXReachesValue(new DefaultValueEvent(1.0), new Event(eventTime, expectedY)));
        integrator.setEventHandlers(eventManager);

        double[] result = integrator.integrate(equations, 0.0, initialValues, 2.0, new double[2]);
        double expected[] = {2.6692117348351878, 1.112598032291368};
        assertArrayEquals(expected, result, RELATIVE_TOLERANCE, ABSOLUTE_TOLERANCE);

        double eventY[] = {expectedY};
        StepContext context = new StepContext(eventTime, eventY);
        StepHandler handler = new StepHandlerAdapter() {
            @Override
            public void handleStep(StepInterpolator interpolator, int stepNumber) {
                double y[] = interpolator.getPreviousY();
                assertEquals(expectedY, y[0], RELATIVE_TOLERANCE);
            }
        };
        integrator.addStepHandler(handler);

        integrator.initialize(equations, 0.0, initialValues);
        integrator.stepToNextEvent(context);
    }

    private static class MyDifferentialEquations implements FirstOrderDifferentialEquations {

        @Override
        public int getNumberOfEquations() {
            return 2;
        }

        @Override
        public void computeDerivatives(double t, double[] y, double[] yPrime) {
            double x = y[0];
            double yp = y[1];
            yPrime[0] = yp;
            yPrime[1] = BesselFunctions.besselJ(0, 4 * Math.PI * x) * yp;
        }

    }

}