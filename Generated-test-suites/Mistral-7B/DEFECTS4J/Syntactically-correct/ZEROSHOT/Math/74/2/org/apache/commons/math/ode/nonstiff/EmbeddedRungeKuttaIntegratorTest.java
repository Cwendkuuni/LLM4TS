package org.apache.commons.math.ode.nonstiff.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.When;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.test.AbstractStepSizeTest;
import org.apache.commons.math3.ode.test.DenseOutputManager;
import org.apache.commons.math3.ode.test.NoEventsManager;
import org.apache.commons.math3.ode.test.StepHandlerCounting;
import org.apache.commons.math3.ode.test.StepHandlerPrinting;
import org.apache.commons.math3.ode.test.TestODE;
import org.apache.commons.math3.ode.test.TestODEs;
import org.apache.commons.math3.ode.test.TestStepSizeControl;
import org.apache.commons.math3.ode.util.RungeKuttaStepInterpolator;

import org.apache.commons.math.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math.ode.nonstiff.EmbeddedRungeKuttaIntegratorF42;
import org.apache.commons.math.ode.nonstiff.EmbeddedRungeKuttaIntegratorF54;

/**
 * Test cases for {@link EmbeddedRungeKuttaIntegrator}.
 */
public class EmbeddedRungeKuttaIntegratorTest extends AbstractStepSizeTest {

    /**
     * Tests the {@link EmbeddedRungeKuttaIntegratorF42} integrator.
     */
    @Test
    public void testEmbeddedRungeKuttaIntegratorF42() {
        final EmbeddedRungeKuttaIntegrator integrator =
            new EmbeddedRungeKuttaIntegratorF42("RKF42", true,
                                              new double[]{ 0.25, 0.25, 0.5 },
                                              new double[][]{{ 0.25, 0 },
                                                              { 0.25, 0.25 },
                                                              { 0.5,  0.25 }},
                                              new double[]{ 0.25, 0.25 },
                                              new RungeKuttaStepInterpolator(),
                                              1e-12, 1e-12);
        final TestStepSizeControl control = new TestStepSizeControl();
        integrator.setStepHandler(control);
        final FirstOrderDifferentialEquations equations = new TestODEs().ode42();
        final double tEnd = 10.0;
        double t = 0.0;
        double[] y = new double[]{ 0.0, 1.0 };
        assertEquals(0.0, integrator.integrate(equations, t, y, tEnd, new double[2]), 1e-12);
        assertEquals(10.0, control.getStepCount(), 1);
        assertEquals(10.0, control.getTotalStepSize(), 1e-12);
    }

    /**
     * Tests the {@link EmbeddedRungeKuttaIntegratorF54} integrator.
     */
    @Test
    public void testEmbeddedRungeKuttaIntegratorF54() {
        final EmbeddedRungeKuttaIntegrator integrator =
            new EmbeddedRungeKuttaIntegratorF54("RKF54", false,
                                              new double[]{ 0.25, 0.25, 0.25, 0.5, 0.5 },
                                              new double[][]{{ 0.25, 0 },
                                                              { 0.25, 0.25 },
                                                              { 0.25, 0.25 },
                                                              { 0.5, 0.4286 },
                                                              { 0.5, 0.4286 }},
                                              new double[]{ 0.25, 0.25, 0.25, 0.5, 0.5 },
                                              new RungeKuttaStepInterpolator(),
                                              1e-12, 1e-12);
        final TestStepSizeControl control = new TestStepSizeControl();
        integrator.setStepHandler(control);
        final FirstOrderDifferentialEquations equations = new TestODEs().ode54();
        final double tEnd = 10.0;
        double t = 0.0;
        double[] y = new double[]{ 0.0, 1.0 };
        assertEquals(0.0, integrator.integrate(equations, t, y, tEnd, new double[2]), 1e-12);
        assertEquals(10.0, control.getStepCount(), 1);
        assertEquals(10.0, control.getTotalStepSize(), 1e-12);
    }

    /**
     * Tests the {@link EmbeddedRungeKuttaIntegrator} class with negative
     * step size.
     */
    @Test
    public void testNegativeStepSize() {
        final EmbeddedRungeKuttaIntegrator integrator =
            new EmbeddedRungeKuttaIntegrator("RKF", false,
                                            new double[]{ 0.5 },
                                            new double[][]{{ 0.5 }},
                                            new double[]{ 0.5 },
                                            new RungeKuttaStepInterpolator(),
                                            1e-12, 1e-12);
        final StepHandlerPrinting handler = new StepHandlerPrinting();
        integrator.setStepHandler(handler);
        final FirstOrderDifferentialEquations equations = new TestODE() {
            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = y[0];
            }
        };
        final double tEnd = 1.0;
        double t = 0.0;
        double[] y = new double[]{ 1.0 };
        assertThrows(IllegalArgumentException.class, () -> integrator.integrate(equations, t, y, tEnd, new double[1]));
        assertEquals(0.0, handler.getSteps().get(0).getStep(), 0.0);
    }

    /**
     * Tests the {@link EmbeddedRungeKuttaIntegrator} class with zero step size.
     */
    @Test
    public void testZeroStepSize() {
        final EmbeddedRungeKuttaIntegrator integrator =
            new EmbeddedRungeKuttaIntegrator("RKF", false,
                                            new double[]{ 0.5 },
                                            new double[][]{{ 0.5 }},
                                            new double[]{ 0.5 },
                                            new RungeKuttaStepInterpolator(),
                                            1e-12, 1e-12);
        final StepHandlerPrinting handler = new StepHandlerPrinting();
        integrator.setStepHandler(handler);
        final FirstOrderDifferentialEquations equations = new TestODE() {
            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = y[0];
            }
        };
        final double tEnd = 1.0;
        double t = 0.0;
        double[] y = new double[]{ 1.0 };
        assertThrows(IllegalArgumentException.class, () -> integrator.integrate(equations, t, y, tEnd, new double[1]));
        assertEquals(0.0, handler.getSteps().get(0).getStep(), 0.0);
    }

    /**
     * Tests the {@link EmbeddedRungeKuttaIntegrator} class with too large step size.
     */
    @Test
    public void testTooLargeStepSize() {
        final EmbeddedRungeKuttaIntegrator integrator =
            new EmbeddedRungeKuttaIntegrator("RKF", false,
                                            new double[]{ 0.5 },
                                            new double[][]{{ 0.5 }},
                                            new double[]{ 0.5 },
                                            new RungeKuttaStepInterpolator(),
                                            1e-12, 1e-12);
        final StepHandlerPrinting handler = new StepHandlerPrinting();
        integrator.setStepHandler(handler);
        final FirstOrderDifferentialEquations equations = new TestODE() {
            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = y[0];
            }
        };
        final double tEnd = 1.0;
        double t = 0.0;
        double[] y = new double[]{ 1.0 };
        integrator.setMinStep(1e20);
        assertThrows(IllegalArgumentException.class, () -> integrator.integrate(equations, t, y, tEnd, new double[1]));
        assertEquals(0.0, handler.getSteps().get(0).getStep(), 0.0);
    }

    /**
     * Tests the {@link EmbeddedRungeKuttaIntegrator} class with events.
     */
    @Test
    public void testEvents() {
        final EmbeddedRungeKuttaIntegrator integrator =
            new EmbeddedRungeKuttaIntegrator("RKF", false,
                                            new double[]{ 0.5 },
                                            new double[][]{{ 0.5 }},
                                            new double[]{ 0.5 },
                                            new RungeKuttaStepInterpolator(),
                                            1e-12, 1e-12);
        final DenseOutputManager output = new DenseOutputManager();
        integrator.setStepHandler(output);
        final CombinedEventsManager events = new CombinedEventsManager();
        integrator.setEventsHandlerManager(events);
        final FirstOrderDifferentialEquations equations = new TestODE() {
            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = y[0];
            }

            @Override
            public Event[] getEvents(double t, double[] y, double[] yDot) {
                if (y[0] > 1.0) {
                    return new Event[]{ new When(t) };
                }
                return new Event[0];
            }
        };
        final double tEnd = 2.0;
        double t = 0.0;
        double[] y = new double[]{ 0.1 };
        integrator.integrate(equations, t, y, tEnd, new double[1]);
        assertEquals(1, output.getSize());
        assertEquals(1.0, output.getValue(0, 0), 1e-12);
        assertEquals(1.0, output.getValue(0, 1), 1e-12);
    }

    /**
     * Tests the {@link EmbeddedRungeKuttaIntegrator} class with no events.
     */
    @Test
    public void testNoEvents() {
        final EmbeddedRungeKuttaIntegrator integrator =
            new EmbeddedRungeKuttaIntegrator("RKF", false,
                                            new double[]{ 0.5 },
                                            new double[][]{{ 0.5 }},
                                            new double[]{ 0.5 },
                                            new RungeKuttaStepInterpolator(),
                                            1e-12, 1e-12);
        final NoEventsManager output = new NoEventsManager();
        integrator.setStepHandler(output);
        final FirstOrderDifferentialEquations equations = new TestODE() {
            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = y[0];
            }
        };
        final double tEnd = 2.0;
        double t = 0.0;
        double[] y = new double[]{ 0.1 };
        integrator.integrate(equations, t, y, tEnd, new double[1]);
    }

}