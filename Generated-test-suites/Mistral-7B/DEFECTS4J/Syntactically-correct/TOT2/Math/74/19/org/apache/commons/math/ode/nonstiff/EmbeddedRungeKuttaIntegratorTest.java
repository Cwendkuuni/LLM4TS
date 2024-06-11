package org.apache.commons.math.ode.nonstiff;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.When;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link EmbeddedRungeKuttaIntegrator}.
 */
public class EmbeddedRungeKuttaIntegratorTest {

    /** Test equation. */
    static final FirstOrderDifferentialEquations EQUATIONS = new FirstOrderDifferentialEquations() {
        @Override
        public int getDimension() {
            return 1;
        }

        @Override
        public void computeDerivatives(double t, double[] y, double[] yDot) {
            yDot[0] = Math.sin(y[0]);
        }
    };

    /** Test equation with events. */
    static final FirstOrderDifferentialEquations EVENT_EQUATIONS = new FirstOrderDifferentialEquations() {
        @Override
        public int getDimension() {
            return 1;
        }

        @Override
        public void computeDerivatives(double t, double[] y, double[] yDot) {
            yDot[0] = Math.sin(y[0]);
        }

        @Override
        public Event[] getEvents(double t, double[] y, int index) {
            return new Event[] { new When(t + 1.0, 1.0) };
        }
    };

    private EmbeddedRungeKuttaIntegrator integrator;

    @Before
    public void setUp() throws InstantiationException, IllegalAccessException {
        integrator = new EmbeddedRungeKutta45Integrator(
                "Embedded Runge-Kutta 4-5 method", true,
                new double[] { 0.25, 0.25, 0.5, 0.5 },
                new double[][] { { 0.25, 0 }, { 0, 0.25 }, { 0.25, 0.25 }, { 0.5, 0 } },
                new double[] { 0.25, 0.25 },
                new RungeKuttaStepInterpolator(), 0.001, 0.05,
                null, null);
    }

    /**
     * Test normal behavior.
     * @throws DerivativeException
     * @throws IntegratorException
     */
    @Test
    public void testNormalBehavior() throws DerivativeException, IntegratorException {
        final double[] initialCondition = new double[] { 0.0 };
        final double step = 0.5;
        final double start = 0.0;
        final double end = 10.0;
        final double[] result = integrator.integrate(EQUATIONS, start, initialCondition, end,
                new double[initialCondition.length]);
        assertEquals(10.0, result[0], 1e-8);
    }

    /**
     * Test normal behavior with events.
     * @throws DerivativeException
     * @throws IntegratorException
     */
    @Test
    public void testNormalBehaviorWithEvents() throws DerivativeException, IntegratorException {
        final double[] initialCondition = new double[] { 0.0 };
        final double step = 0.1;
        final double start = 0.0;
        final double end = 5.0;
        integrator.addStepHandler(new StepHandler() {
            @Override
            public void handleStep(AbstractStepInterpolator interpolator, int index) {
                System.out.println("Step " + index + " at " + interpolator.getCurrentTime());
            }
        });
        final double[] result = integrator.integrate(EVENT_EQUATIONS, start, initialCondition, end,
                new double[initialCondition.length]);
        assertEquals(5.0, result[0], 1e-6);
    }

    /** Test invalid arguments. */
    @Test(expected = MathIllegalArgumentException.class)
    public void testInvalidArguments() throws InstantiationException, IllegalAccessException {
        new EmbeddedRungeKuttaIntegrator(null, true, null, null, null, null, 0.0, 0.0, null, null);
    }

    /** Test invalid arguments. */
    @Test(expected = MathIllegalArgumentException.class)
    public void testInvalidArguments2() throws InstantiationException, IllegalAccessException {
        new EmbeddedRungeKuttaIntegrator("", true, null, null, null, null, 0.0, 0.0, null, null);
    }

    /** Test invalid arguments. */
    @Test(expected = NumberIsTooSmallException.class)
    public void testInvalidArguments3() throws InstantiationException, IllegalAccessException {
        new EmbeddedRungeKuttaIntegrator("Embedded Runge-Kutta method", true,
                new double[] { 0.25, 0.25, 0.5, 0.5 },
                new double[][] { { 0.25, 0 },
                        { 0, 0.25 }, { 0.25, 0.25 }, { 0.5, 0 } },
                new double[] { 0.25, 0.25 },
                new RungeKuttaStepInterpolator(), -0.001, 0.05,
                null, null);
    }

}