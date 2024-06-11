package org.apache.commons.math.ode.nonstiff;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.DefaultValueEvent;
import org.apache.commons.math3.ode.events.ValueEvent;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.sampling.AbstractStepHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.apache.commons.math3.special.BesselFunctions;
import org.apache.commons.math3.util.Precision;

public class EmbeddedRungeKuttaIntegratorTest {

    private static final double ABS_TOL = 1e-12;
    private static final double REL_TOL = 1e-12;

    @org.junit.Before
    public void setUp() {
        // Set up a simple step handler that just logs the steps
        AbstractStepHandler stepHandler = new AbstractStepHandler() {
            @Override
            public void handleStep(StepInterpolator interpolator, int step) {
                System.out.println("Step " + step);
            }
        };

        // Set up a RungeKuttaStepInterpolator with the given Butcher array
        double[] c = {0.0, 0.25, 0.5, 0.875, 1.0};
        double[][] a = {{0.0, 0.0, 0.0, 0.0, 0.0},
                        {0.25, 0.5, 0.0, 0.0, 0.0},
                        {0.5, 0.5, 0.5, 0.0, 0.0},
                        {0.875, 0.0, 0.5, 0.5, 0.0},
                        {1.0, 0.0, 0.0, 0.5, 0.5}};
        double[] b = {0.0, 0.25, 0.5, 0.875, 1.0};
        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(c, a, b);

        // Create an EmbeddedRungeKuttaIntegrator with the given Butcher array
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK4", false, c, a, b, prototype, 0.01, 0.04, ABS_TOL, REL_TOL) {
            @Override
            public int getOrder() {
                return 4;
            }

            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                double error = 0.0;
                for (int i = 0; i < y0.length; i++) {
                    error += Math.pow(Math.abs(y1[i] - y0[i]) / (1.0 + i), 2);
                }
                return error;
            }
        };

        integrator.setMinReduction(0.1);
        integrator.setMaxGrowth(10.0);
        integrator.setSafety(0.9);
        integrator.setStepHandlers(stepHandler);

        this.integrator = integrator;
    }

    @org.junit.Test
    public void testIntegrateSimple() {
        double[] y0 = {1.0, 1.0};
        double[] y = new double[y0.length];
        double t0 = 0.0;
        double t1 = 1.0;

        double result = integrator.integrate(new SimpleEquations(), t0, y0, t1, y);
        assertEquals(t1, result, 1e-9);
    }

    @org.junit.Test
    public void testIntegrateWithEvent() {
        double[] y0 = {1.0, 1.0};
        double[] y = new double[y0.length];
        double t0 = 0.0;

        integrator.setEventsHandlersManager(new CombinedEventsManager() {
            @Override
            public void addEvent(ValueEvent event) {
                events.add(event);
            }

            @Override
            public void reset() {
                events.clear();
            }
        });

        integrator.addEndTimeChecker(t0, 1.0, new CombinedEventsManager());

        double result = integrator.integrate(new SimpleEquationsWithEvent(), t0, y0, 1.5, y);
        assertEquals(1.5, result, 1e-9);
        assertEquals(1.0, y[0], 1e-9);
        assertEquals(BesselFunctions.I0(1.0), y[1], 1e-8);

        assertEquals(1, events.size());
        ValueEvent event = events.get(0);
        assertEquals(1.0, event.getY()[0], 1e-9);
        assertEquals(1.5707963267948966, event.getValue(), 1e-8);
    }

    @org.junit.Test
    public void testIntegrateInvalidArguments() {
        double[] y0 = {1.0, 1.0};
        double[] y = new double[y0.length];
        double t0 = 0.0;

        // Test with negative tolerance
        assertThrows(MathIllegalArgumentException.class, () -> integrator.integrate(new SimpleEquations(), t0, y0, 0.5, y, -1e-9, 1e-9));

        // Test with zero tolerance
        assertThrows(MathIllegalArgumentException.class, () -> integrator.integrate(new SimpleEquations(), t0, y0, 0.5, y, 0.0, 1e-9));

        // Test with null equations
        assertThrows(NullPointerException.class, () -> integrator.integrate(null, t0, y0, 0.5, y));
    }

    private static class SimpleEquations implements FirstOrderDifferentialEquations {
        @Override
        public int getDimension() {
            return 2;
        }

        @Override
        public void computeDerivatives(double t, double[] y, double[] yPrime) {
            yPrime[0] = y[1];
            yPrime[1] = -y[0];
        }
    }

    private static class SimpleEquationsWithEvent implements FirstOrderDifferentialEquations {
        @Override
        public int getDimension() {
            return 2;
        }

        @Override
        public void computeDerivatives(double t, double[] y, double[] yPrime) {
            yPrime[0] = y[1];
            yPrime[1] = -y[0] + 1.0;
        }

        @Override
        public ValueEvent getEvent(double t, double[] y) {
            if (Precision.isClose(y[1], BesselFunctions.I0(1.0), 1e-8)) {
                return new DefaultValueEvent(t, y);
            }
            return null;
        }
    }

    private EmbeddedRungeKuttaIntegrator integrator;
    private CombinedEventsManager events = new CombinedEventsManager();
}