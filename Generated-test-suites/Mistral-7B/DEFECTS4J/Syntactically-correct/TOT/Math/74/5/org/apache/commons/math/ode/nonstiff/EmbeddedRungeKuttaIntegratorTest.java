package org.apache.commons.math.ode.nonstiff;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.When;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.step.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.util.ODETestUtils;
import org.apache.commons.math3.special.BesselFunctions;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for {@link EmbeddedRungeKuttaIntegrator} class.
 */
public class EmbeddedRungeKuttaIntegratorTest {

    /** The test integrator. */
    private EmbeddedRungeKuttaIntegrator integrator;

    /** The test equations. */
    private FirstOrderDifferentialEquations equations;

    /** The test step handler. */
    private StepHandler stepHandler;

    /** The test events manager. */
    private CombinedEventsManager eventsManager;

    /** The test tolerance. */
    private final double tolerance = 1e-10;

    /**
     * Set up.
     */
    @Before
    public void setUp() {
        integrator = new DormandPrince853Integrator(1.0, new RungeKuttaStepInterpolator(),
                0.001, 0.01, new double[] { 0.0, 0.03, 0.034720985 },
                new double[][] { { 0.0, 0.235626887, 0.678402009, 0.861921853, 0.319685979,
                        0.356876683, 0.467914214, 0.532744026 },
                  { 0.235626887, 0.800040098, 0.943878047, 0.996596238, 0.642590639,
                        0.674513934, 0.733389129, 0.788672943 } });
        equations = new DummyDifferentialEquations();
        stepHandler = new DummyStepHandler();
        eventsManager = new CombinedEventsManager();
    }

    /**
     * Test the integrator with a simple differential equation.
     */
    @Test
    public void testSimple() {
        double[] y0 = { 1.0, 1.0 };
        double tStart = 0.0;
        double tEnd = 5.0;
        double[] yEnd = { 2.0, 2.0 };
        integrator.integrate(equations, tStart, y0, tEnd, yEnd);
        assertEquals(yEnd[0], y0[0], tolerance);
        assertEquals(yEnd[1], y0[1], tolerance);
    }

    /**
     * Test the integrator with a differential equation that depends on a parameter.
     */
    @Test
    public void testParameter() {
        double[] y0 = { 1.0, 1.0 };
        double tStart = 0.0;
        double tEnd = 5.0;
        double parameter = 1.0;
        double[] yEnd = { BesselFunctions.I0(5.0), BesselFunctions.I0(5.0) };
        integrator.setParameter(parameter);
        integrator.integrate(new BesselDifferentialEquations(parameter), tStart, y0, tEnd, yEnd);
        assertEquals(yEnd[0], y0[0], tolerance);
        assertEquals(yEnd[1], y0[1], tolerance);
    }

    /**
     * Test the integrator with a differential equation with an event.
     */
    @Test
    public void testEvent() {
        double[] y0 = { 1.0, 1.0 };
        double tStart = 0.0;
        double tEnd = 5.0;
        double[] yEnd = { 2.0, 2.0 };
        double stopTime = 2.5;
        eventsManager.addEvent(new When(stopTime) {
            @Override
            public int index(double t, double[] y, double[] yDot, FirstOrderDifferentialEquations equationsContext,
                            CombinedEventsManager eventsContext) {
                throw new NumberIsTooSmallException("Reached the end of the integration period");
            }
        });
        integrator.addStepHandler(stepHandler);
        integrator.integrate(equations, tStart, y0, tEnd, yEnd);
        assertEquals(yEnd[0], y0[0], tolerance);
        assertEquals(yEnd[1], y0[1], tolerance);
        // The step handler should have been called at least once
        assertEquals(1, stepHandler.getNumberOfSteps());
    }

    /**
     * Test the integrator with an illegal step size.
     */
    @Test
    public void testIllegalStepSize() {
        double[] y0 = { 1.0, 1.0 };
        double tStart = 0.0;
        double tEnd = 5.0;
        double stepSize = -0.1;
        assertThrows(MathIllegalArgumentException.class, () -> integrator.integrate(equations, tStart, y0, tEnd, y0, stepSize));
    }

    /**
     * Test the integrator with a too small step size.
     */
    @Test
    public void testTooSmallStepSize() {
        double[] y0 = { 1.0, 1.0 };
        double tStart = 0.0;
        double tEnd = 5.0;
        double stepSize = 0.0;
        assertThrows(NumberIsTooSmallException.class, () -> integrator.integrate(equations, tStart, y0, tEnd, y0, stepSize));
    }

}