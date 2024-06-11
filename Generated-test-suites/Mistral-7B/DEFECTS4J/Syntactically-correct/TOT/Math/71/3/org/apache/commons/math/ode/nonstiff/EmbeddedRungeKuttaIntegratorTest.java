package org.apache.commons.math.ode.nonstiff;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.DefaultValueEvent;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.EventException;
import org.apache.commons.math3.ode.events.RelativisticStepHandler;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link EmbeddedRungeKuttaIntegrator}.
 */
public class EmbeddedRungeKuttaIntegratorTest {

    private static final double EPSILON = 1e-12;
    private static final double ABSOLUTE_TOLERANCE = 1e-12;
    private static final double RELATIVE_TOLERANCE = 1e-13;
    private static final double MIN_STEP = 1e-10;
    private static final double MAX_STEP = 1e10;
    private static final double[] INITIAL_STATE = new double[] { 1.0, 1.0 };
    private static final double[][] COEFFICIENTS = new double[][] { { 0.0, 0.3, 0.6, 1.0 }, { 0.3, 0.4, 0.8, 1.0 } };
    private static final double[] WEIGHTS = new double[] { 0.3, 0.6, 1.0 };
    private static final double[][] INITIAL_DERIVATIVES = new double[][] { { 1.0, 1.0 }, { 2.0, 2.0 } };
    private static final double[][] VALUES = new double[][] { { 1.5, 1.5 }, { 2.0, 2.5 } };
    private static final double[] INITIAL_TIME = new double[] { 0.0 };

    private EmbeddedRungeKuttaIntegrator integrator;
    private FirstOrderDifferentialEquations equations;

    @Before
    public void setUp() {
        integrator = new RungeKuttaFehlberg78Method(false, COEFFICIENTS, WEIGHTS, 5,
                new RungeKuttaStepInterpolator(), MIN_STEP, MAX_STEP, ABSOLUTE_TOLERANCE, RELATIVE_TOLERANCE);
        equations = new DummyDifferentialEquations();
        equations.setEquations(new DummyEquations());
    }

    @Test
    public void testIntegration() throws EventException {
        double[] y = INITIAL_STATE;
        double t = INITIAL_TIME[0];
        double[] result = new double[y.length];
        for (int i = 0; i < 10; ++i) {
            double nextStep = integrator.integrate(equations, t, y, t + 1.0, result);
            assertEquals("Unexpected step size.", (t + 1.0), nextStep, EPSILON);
            assertArrayEquals("Unexpected result.", VALUES[i], result, EPSILON);
            System.arraycopy(result, 0, y, 0, y.length);
            t = nextStep;
        }
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testNegativeMinStep() {
        integrator = new RungeKuttaFehlberg78Method(false, COEFFICIENTS, WEIGHTS, 5,
                new RungeKuttaStepInterpolator(), -MIN_STEP, MAX_STEP, ABSOLUTE_TOLERANCE, RELATIVE_TOLERANCE);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testZeroMinStep() {
        integrator = new RungeKuttaFehlberg78Method(false, COEFFICIENTS, WEIGHTS, 5,
                new RungeKuttaStepInterpolator(), 0.0, MAX_STEP, ABSOLUTE_TOLERANCE, RELATIVE_TOLERANCE);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testNegativeMaxStep() {
        integrator = new RungeKuttaFehlberg78Method(false, COEFFICIENTS, WEIGHTS, 5,
                new RungeKuttaStepInterpolator(), MIN_STEP, -MAX_STEP, ABSOLUTE_TOLERANCE, RELATIVE_TOLERANCE);
    }

    @Test
    public void testIntegrationWithEvents() throws EventException {
        double[] y = INITIAL_STATE;
        double t = INITIAL_TIME[0];
        double[] result = new double[y.length];
        RelativisticStepHandler handler = new RelativisticStepHandler(new DefaultValueEvent(0.5, 1.0, 1.0));
        integrator.addStepHandler(handler);
        for (int i = 0; i < 10; ++i) {
            double nextStep = integrator.integrate(equations, t, y, t + 1.0, result);
            assertEquals("Unexpected step size.", (t + 1.0), nextStep, EPSILON);
            assertArrayEquals("Unexpected result.", VALUES[i], result, EPSILON);
            System.arraycopy(result, 0, y, 0, y.length);
            t = nextStep;
        }
        handler.reset();
        assertEquals("Event not triggered.", 1.0, handler.getValue(), EPSILON);
    }

    @Test
    public void testIntegrationWithEventsRejected() throws EventException {
        double[] y = INITIAL_STATE;
        double t = INITIAL_TIME[0];
        double[] result = new double[y.length];
        RelativisticStepHandler handler = new RelativisticStepHandler(new DefaultValueEvent(0.25, 1.0, 1.0));
        integrator.addStepHandler(handler);
        for (int i = 0; i < 10; ++i) {
            double nextStep = integrator.integrate(equations, t, y, t + 1.0, result);
            assertEquals("Unexpected step size.", (t + 1.0), nextStep, EPSILON);
            assertArrayEquals("Unexpected result.", VALUES[i], result, EPSILON);
            System.arraycopy(result, 0, y, 0, y.length);
            t = nextStep;
        }
        handler.reset();
        assertEquals("Event not triggered.", 1.0, handler.getValue(), EPSILON);

        integrator.setMinStep(0.1);
        t = INITIAL_TIME[0];
        for (int i = 0; i < 5; ++i) {
            double nextStep = integrator.integrate(equations, t, y, t + 1.0, result);
            assertEquals("Unexpected step size.", (t + 1.0), nextStep, EPSILON);
            assertArrayEquals("Unexpected result.", VALUES[i], result, EPSILON);
            System.arraycopy(result, 0, y, 0, y.length);
            t = nextStep;
        }
        handler.reset();
        assertEquals("Event not triggered.", 1.0, handler.getValue(), EPSILON);

        integrator.setMaxStep(0.5);
        t = INITIAL_TIME[0];
        for (int i = 5; i < 10; ++i) {
            double nextStep = integrator.integrate(equations, t, y, t + 1.0, result);
            assertEquals("Unexpected step size.", (t + 0.5), nextStep, EPSILON);
            assertArrayEquals("Unexpected result.", VALUES[i], result, EPSILON);
            System.arraycopy(result, 0, y, 0, y.length);
            t = nextStep;
        }
        handler.reset();
        assertEquals("Event triggered.", 0.5, handler.getValue(), EPSILON);
    }

}