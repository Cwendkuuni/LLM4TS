package org.apache.commons.math.ode.nonstiff.test;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.EventManager;
import org.apache.commons.math3.ode.events.MockEvent;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link EmbeddedRungeKuttaIntegrator} class.
 */
public class EmbeddedRungeKuttaIntegratorTest {

    private static final double ACCURACY = 1e-12;
    private static final double RELATIVE_TOLERANCE = 1e-8;
    private static final double ABSOLUTE_TOLERANCE = 1e-12;

    private static final int INTEGRATOR_ORDER = 5;

    private static EmbeddedRungeKuttaIntegrator integrator;

    @BeforeAll
    public static void setup() {
        integrator = new RungeKuttaFehlberg78Integrator(INTEGRATOR_ORDER, new RungeKuttaStepInterpolator());
        integrator.setMinStep(0.0001);
        integrator.setMaxStep(1.0);
        integrator.setSafety(0.95);
        integrator.setMinReduction(0.4);
        integrator.setMaxGrowth(1.5);
    }

    @Test
    public void testEmbeddedRungeKuttaIntegrator_example1() {
        double[] y0 = new double[]{0.0, 0.1};
        double tEnd = 1.0;
        FirstOrderDifferentialEquations equations = new RK4Equations();

        double t = integrator.integrate(equations, 0.0, y0, tEnd, new double[y0.length]);
        assertArrayEquals(new double[]{1.0, 1.0}, y0, ACCURACY);
        assertEquals(tEnd, t, ACCURACY);
    }

    @Test
    public void testEmbeddedRungeKuttaIntegrator_example2() {
        double[] y0 = new double[]{0.0, 1.0};
        double tEnd = 1.0;
        FirstOrderDifferentialEquations equations = new RK4Equations();

        integrator.setMinStep(0.01);
        integrator.setMaxStep(1.0);

        double t = integrator.integrate(equations, 0.0, y0, tEnd, new double[y0.length]);
        assertArrayEquals(new double[]{0.5, 0.5}, y0, ACCURACY);
        assertEquals(tEnd, t, ACCURACY);
    }

    @Test
    public void testEmbeddedRungeKuttaIntegrator_edgeCase_stepSize_minimal() {
        double[] y0 = new double[]{0.0, 0.1};
        double tEnd = 1.0;
        FirstOrderDifferentialEquations equations = new RK4Equations();

        integrator.setMinStep(0.0);

        assertThrows(IllegalArgumentException.class, () -> {
            integrator.integrate(equations, 0.0, y0, tEnd, new double[y0.length]);
        });
    }

    @Test
    public void testEmbeddedRungeKuttaIntegrator_edgeCase_stepSize_maximal() {
        double[] y0 = new double[]{0.0, 0.1};
        double tEnd = 1.0;
        FirstOrderDifferentialEquations equations = new RK4Equations();

        integrator.setMaxStep(0.0);

        assertThrows(IllegalArgumentException.class, () -> {
            integrator.integrate(equations, 0.0, y0, tEnd, new double[y0.length]);
        });
    }

    @Test
    public void testEmbeddedRungeKuttaIntegrator_exception_derivative() {
        double[] y0 = new double[]{0.0, 0.1};
        double tEnd = 1.0;
        FirstOrderDifferentialEquations equations = new DerivativeExceptionDifferentialEquations();

        assertThrows(DerivativeException.class, () -> {
            integrator.integrate(equations, 0.0, y0, tEnd, new double[y0.length]);
        });
    }

    @Test
    public void testEmbeddedRungeKuttaIntegrator_edgeCase_event() {
        double[] y0 = new double[]{0.0, 0.1};
        double tEnd = 1.5;
        FirstOrderDifferentialEquations equations = new EventDifferentialEquations();

        EventManager eventManager = new EventManager();
        eventManager.addEvent(new MockEvent(1.2));
        equations.setEventsManager(eventManager);

        integrator.setMinStep(0.01);
        integrator.setMaxStep(1.0);

        double t = 0.0;
        double[] y = new double[y0.length];
        double[] yPrime = new double[y0.length];

        try {
            integrator.integrate(equations, t, y0, tEnd, y);
            fail();
        } catch (IntegratorException e) {
            // expected
        }
        assertArrayEquals(y0, y);
        assertEquals(t, integrator.getStepStart(), ACCURACY);

        eventManager.clear();

        integrator.reset();
        integrator.setMinStep(0.01);
        integrator.setMaxStep(1.0);
        integrator.setSafety(0.9);
        integrator.setMinReduction(0.2);
        integrator.setMaxGrowth(1.5);

        y = new double[y0.length];
        yPrime = new double[y0.length];

        integrator.integrate(equations, t, y0, tEnd, y);
        assertArrayEquals(y, yPrime, RELATIVE_TOLERANCE);
    }

    @Test
    public void testEmbeddedRungeKuttaIntegrator_edgeCase_event_maxStep() {
        double[] y0 = new double[]{0.0, 0.1};
        double tEnd = 1.5;
        FirstOrderDifferentialEquations equations = new EventDifferentialEquations();

        EventManager eventManager = new EventManager();
        eventManager.addEvent(new MockEvent(1.2, 1.3));
        equations.setEventsManager(eventManager);

        integrator.setMinStep(0.01);
        integrator.setMaxStep(1.3);

        double t = 0.0;
        double[] y = new double[y0.length];
        double[] yPrime = new double[y0.length];

        try {
            integrator.integrate(equations, t, y0, tEnd, y);
            fail();
        } catch (IntegratorException | NumberIsTooSmallException e) {
            // expected
        }
        assertArrayEquals(y0, y);
        assertEquals(t, integrator.getStepStart(), ACCURACY);

        eventManager.clear();

        integrator.reset();
        integrator.setMinStep(0.01);
        integrator.setMaxStep(1.5);

        y = new double[y0.length];
        yPrime = new double[y0.length];

        integrator.integrate(equations, t, y0, tEnd, y);
        assertArrayEquals(y, yPrime, RELATIVE_TOLERANCE);
    }

    @Test
    public void testEmbeddedRungeKuttaIntegrator_edgeCase_event_maxCount() {
        double[] y0 = new double[]{0.0, 0.1};
        double tEnd = 1.5;
        FirstOrderDifferentialEquations equations = new EventDifferentialEquations();

        EventManager eventManager = new EventManager();
        eventManager.addEvent(new MockEvent(1.2), 1);
        eventManager.addEvent(new MockEvent(1.2), 1);
        equations.setEventsManager(eventManager);

        integrator.setMinStep(0.01);
        integrator.setMaxStep(1.5);

        double t = 0.0;
        double[] y = new double[y0.length];
        double[] yPrime = new double[y0.length];

        try {
            integrator.integrate(equations, t, y0, tEnd, y);
            fail();
        } catch (IntegratorException | MaxCountExceededException e) {
            // expected
        }
        assertArrayEquals(y0, y);
        assertEquals(t, integrator.getStepStart(), ACCURACY);

        eventManager.clear();

        integrator.reset();
        integrator.setMinStep(0.01);
        integrator.setMaxStep(1.5);
        integrator.setMaxSteps(2);

        y = new double[y0.length];
        yPrime = new double[y0.length];

        integrator.integrate(equations, t, y0, tEnd, y);
        assertArrayEquals(y, yPrime, RELATIVE_TOLERANCE);
    }

    @Test
    public void testEmbeddedRungeKuttaIntegrator_example3() {
        double[] y0 = new double[]{1.0, 1.0};
        double tEnd = 1.5;
        FirstOrderDifferentialEquations equations = new VanDerPolEquations();

        double t = integrator.integrate(equations, 0.5, y0, tEnd, new double[y0.length]);
        assertArrayEquals(new double[]{1.26418645986884, 0.96357118612656}, y0, ACCURACY);
        assertEquals(tEnd, t, ACCURACY);
    }

    @Test
    public void testEmbeddedRungeKuttaIntegrator_example4() {
        double[] y0 = new double[]{0.0, 0.5};
        double tEnd = 2.0;
        FirstOrderDifferentialEquations equations = new VanDerPolEquations();

        double t = integrator.integrate(equations, 0.0, y0, tEnd, new double[y0.length]);
        assertArrayEquals(new double[]{0.78325054734588, 0.46867284724982}, y0, ACCURACY);
        assertEquals(tEnd, t, ACCURACY);
    }

    @AfterAll
    public static void teardown() {
        integrator = null;
    }

}