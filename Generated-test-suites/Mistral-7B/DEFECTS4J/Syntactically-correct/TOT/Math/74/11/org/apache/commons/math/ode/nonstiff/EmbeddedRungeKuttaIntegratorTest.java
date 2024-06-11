package org.apache.commons.math.ode.nonstiff;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.When;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/**
 * This class tests the {@link EmbeddedRungeKuttaIntegrator} abstract class.
 */
public class EmbeddedRungeKuttaIntegratorTest {

    private EmbeddedRungeKuttaIntegrator integrator;
    private FirstOrderDifferentialEquations equations;

    @Before
    public void setup() {
        equations = new RK45Equations();
        integrator = new RK45Integrator(
            "RK45",
            false,
            new double[] { 0.23, 0.23, 0.23, 0.23, 0.0 },
            new double[][] { { 0.25, 0.25, 0.5, 0.0, 0.0 },
                              { 0.0, 0.0, 0.25, 0.25, 0.25 },
                              { 0.6834, 0.0, 0.0, 0.6834, 0.0 },
                              { 0.0, 0.6834, 0.0, 0.0, 0.6834 },
                              { 0.25, 0.25, 0.25, 0.25, 0.25 } },
            new double[] { 0.25, 0.25, 0.25, 0.25, 0.25 },
            new RungeKuttaStepInterpolator(),
            1e-8,
            1e-8,
            1e-8,
            1e-8,
            1e-8 );
    }

    @Test
    public void testIntegration() throws Exception {
        double[] y0 = { 2.0, 2.0 };
        double t0 = 0.0;
        double t = 2.5;

        double[] y = integrator.integrate(equations, t0, y0, t);
        assertEquals(y0[0], y[0], 1e-8);
        assertEquals(y0[1], y[1], 1e-8);
    }

    @Test
    public void testIntegrationWithEvents() throws Exception {
        double[] y0 = { 2.0, 2.0 };
        double t0 = 0.0;
        double t = 1.5;

        integrator.setStepHandler(new StepHandler() {
            @Override
            public void handleStep(int stage, double[] interpolatedValues,
                                   double t) {
                if (stage == 1 && t == 1.0) {
                    throw new AssertionError("An event should have occurred");
                }
            }
        });
        integrator.setEventsHandlerManager(new CombinedEventsManager() {
            @Override
            public void addEvent(Event event) {
                if (event instanceof When && ((When) event).getT() == 1.0) {
                    throw new AssertionError("Event should not be added");
                }
                super.addEvent(event);
            }
        });

        try {
            integrator.integrate(equations, t0, y0, t);
            assertTrue(false);
        } catch (MaxCountExceededException e) {
            // expected
        }
    }

    @Test
    public void testIntegrationWithIllegalArguments() {
        assertThrows(MathIllegalArgumentException.class, () -> {
            new EmbeddedRungeKuttaIntegrator("", false, null, null, null, null,
                                             1e-8, 1e-8, 1e-8, 1e-8, 1e-8);
        });
        assertThrows(MathIllegalArgumentException.class, () -> {
            new EmbeddedRungeKuttaIntegrator("", false, new double[] { 0.2, 0.2 },
                                              null, null, null, 1e-8, 1e-8, 1e-8, 1e-8, 1e-8);
        });
        assertThrows(MathIllegalArgumentException.class, () -> {
            new EmbeddedRungeKuttaIntegrator("", false, new double[][] { { 0.2 } },
                                              null, null, null, 1e-8, 1e-8, 1e-8, 1e-8, 1e-8);
        });
        assertThrows(MathIllegalArgumentException.class, () -> {
            new EmbeddedRungeKuttaIntegrator("", false, new double[][] { { 0.2 }, { 0.2 } },
                                              null, null, null, 1e-8, 1e-8, 1e-8, 1e-8, 1e-8);
        });
        assertThrows(MathIllegalArgumentException.class, () -> {
            new EmbeddedRungeKuttaIntegrator("", false, new double[][] { { 0.2 }, { 0.2 } },
                                              new double[] { 0.2 }, null, null, 1e-8, 1e-8, 1e-8, 1e-8, 1e-8);
        });
        assertThrows(MathIllegalArgumentException.class, () -> {
            new EmbeddedRungeKuttaIntegrator("", false, new double[][] { { 0.2 }, { 0.2 } },
                                              new double[] { 0.2 }, new RungeKuttaStepInterpolator(), null, 1e-8, 1e-8, 1e-8, 1e-8, 1e-8);
        });
        assertThrows(MathIllegalArgumentException.class, () -> {
            new EmbeddedRungeKuttaIntegrator("", false, new double[][] { { 0.2 }, { 0.2 } },
                                              new double[] { 0.2 }, new RungeKuttaStepInterpolator(), new CombinedEventsManager() {}, 1e-8, 1e-8, 1e-8, 1e-8, 1e-8);
        });
        assertThrows(MathIllegalArgumentException.class, () -> {
            new EmbeddedRungeKuttaIntegrator("", false, new double[][] { { 0.2 }, { 0.2 } },
                                              new double[] { 0.2 }, new RungeKuttaStepInterpolator(), new CombinedEventsManager() {}, 1e-8, null, 1e-8, 1e-8, 1e-8);
        });
        assertThrows(MathIllegalArgumentException.class, () -> {
            new EmbeddedRungeKuttaIntegrator("", false, new double[][] { { 0.2 }, { 0.2 } },
                                              new double[] { 0.2 }, new RungeKuttaStepInterpolator(), new CombinedEventsManager() {}, 1e-8, 1e-8, null, 1e-8, 1e-8);
        });
        assertThrows(MathIllegalArgumentException.class, () -> {
            new EmbeddedRungeKuttaIntegrator("", false, new double[][] { { 0.2 }, { 0.2 } },
                                              new double[] { 0.2 }, new RungeKuttaStepInterpolator(), new CombinedEventsManager() {}, 1e-8, 1e-8, 1e-8, null, 1e-8);
        });
        assertThrows(MathIllegalArgumentException.class, () -> {
            new EmbeddedRungeKuttaIntegrator("", false, new double[][] { { 0.2 }, { 0.2 } },
                                              new double[] { 0.2 }, new RungeKuttaStepInterpolator(), new CombinedEventsManager() {}, 1e-8, 1e-8, 1e-8, 1e-8, null);
        });
    }

    @Test
    public void testIntegrationWithNoBracketing() {
        double[] y0 = { 2.0, 2.0 };
        double t0 = 0.0;
        double t = 1.0;

        integrator.setEventsHandlerManager(new CombinedEventsManager() {
            @Override
            public void addEvent(Event event) {
                if (event instanceof When && ((When) event).getT() == 0.5) {
                    throw new NoBracketingException("An event can't occur at that time");
                }
                super.addEvent(event);
            }
        });
        try {
            integrator.integrate(equations, t0, y0, t);
            assertTrue(false);
        } catch (NoBracketingException e) {
            // expected
        }
    }

}