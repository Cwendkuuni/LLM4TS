package org.apache.commons.math.ode.nonstiff;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.When;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test for {@link EmbeddedRungeKuttaIntegrator}.
 */
public class EmbeddedRungeKuttaIntegratorTest {

    private EmbeddedRungeKuttaIntegrator integrator;
    private FirstOrderDifferentialEquations equations;

    @Before
    public void setUp() {
        equations = new RK4IntegratorTestEquations();
        integrator = new RungeKuttaFehlberg78Integrator(
                6, new RungeKuttaStepInterpolator(6, 7, true),
                1.0e-10, 1.0e-10, 1.0);
    }

    /**
     * Test for the integration of a simple differential equation.
     */
    @Test
    public void testSimpleIntegration() {
        final double y0 = 0.0;
        final double t0 = 0.0;
        final double[] y = new double[] { 1.0, 2.0 };
        final double t = 1.0;

        integrator.integrate(equations, t0, y0, t, y);
        assertEquals(1.0, y[0], 1.0e-10);
        assertEquals(2.0, y[1], 1.0e-10);
    }

    /**
     * Test for the integration of a differential equation with an event at the final time.
     */
    @Test(expected = IntegratorException.class)
    public void testEventAtFinalTime() {
        final double y0 = 0.0;
        final double t0 = 0.0;
        final double[] y = new double[] { 1.0, 2.0 };
        final double t = 1.0;

        integrator.addStepHandler(new StepHandler() {
            @Override
            public void handleStep(StepContext context, int step) {
                fail("Unexpected step event");
            }
        });
        integrator.addEvent(new When(1.0), new Event() {
            @Override
            public void evaluateEvent(double t, double[] y) {
                throw new RuntimeException("Event exception");
            }
        });

        try {
            integrator.integrate(equations, t0, y0, t, y);
            fail("Integration should have raised an IntegratorException");
        } catch (IntegratorException e) {
            // expected
        }
    }

    /**
     * Test for the integration of a differential equation with an event at an intermediate time.
     */
    @Test
    public void testEventAtIntermediateTime() {
        final double y0[] = { 0.0, 0.0 };
        final double t0 = 0.0;
        final double t = 2.0;

        integrator.addStepHandler(new StepHandler() {
            private int stepCount = 0;

            @Override
            public void handleStep(StepContext context, int step) {
                stepCount++;
                if (stepCount == 6) {
                    assertEquals(1.0, context.getStepSize(), 1.0e-12);
                    assertEquals(1.0, context.getY()[0], 1.0e-12);
                    assertEquals(1.0, context.getY()[1], 1.0e-12);
                    assertEquals(1.5, context.getT(), 1.0e-12);
                }
            }
        });
        integrator.addEvent(new When(1.5), new Event() {
            @Override
            public void evaluateEvent(double t, double[] y) {
                throw new MaxCountExceededException("Maximum number of steps reached");
            }
        });

        integrator.integrate(equations, t0, y0, t, null);
        assertTrue(integrator.getNumSteps() > 6);
    }

    /**
     * Test for the integration of a differential equation with a very small step size.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testSmallStepSize() {
        final double y0 = 0.0;
        final double t0 = 0.0;
        final double[] y = new double[1];
        final double t = 1.0;
        final double stepSize = 1.0e-20;

        integrator.setMinStep(stepSize);
        try {
            integrator.integrate(equations, t0, y0, t, y);
            fail("Integration should have raised a NumberIsTooSmallException");
        } catch (NumberIsTooSmallException e) {
            // expected
        }
    }

    /**
     * Test for the integration of a differential equation with a negative step size.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testNegativeStepSize() {
        final double y0 = 0.0;
        final double t0 = 0.0;
        final double[] y = new double[1];
        final double t = 1.0;
        final double stepSize = -1.0;

        integrator.setMinStep(stepSize);
        try {
            integrator.integrate(equations, t0, y0, t, y);
            fail("Integration should have raised a MathIllegalArgumentException");
        } catch (MathIllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Test for the integration of a differential equation with a very large step size.
     */
    @Test(expected = IntegratorException.class)
    public void testLargeStepSize() {
        final double y0 = 0.0;
        final double t0 = 0.0;
        final double t = 1.0;

        integrator.setMaxStep(100.0);
        try {
            integrator.integrate(equations, t0, y0, t, null);
            fail("Integration should have raised an IntegratorException");
        } catch (IntegratorException e) {
            // expected
        }
    }

}