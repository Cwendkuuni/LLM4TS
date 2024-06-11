package org.apache.commons.math.ode.nonstiff.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.When;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.test.ODETestUtils;
import org.apache.commons.math3.special.BesselFunctions;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class EmbeddedRungeKuttaIntegratorTest {

    @org.junit.Test
    public void testEmbeddedRungeKuttaIntegrator() {
        double[] coefficients = {0.0, 0.3, 0.6, 1.0};
        double[][] internalWeights = {
            {0.0, 0.3, 0.6},
            {0.3, 0.48, 0.27},
            {0.45, 0.6, 0.055}
        };
        double[] externalWeights = {0.25, 0.643, 0.0};

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK3", true, coefficients, internalWeights, externalWeights, new RungeKuttaStepInterpolator(), 0.0, Double.POSITIVE_INFINITY, null, null) {
            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                double error = 0.0;
                for (int i = 0; i < y0.length; i++) {
                    error += Math.pow(Math.abs(y1[i] - y0[i]), 2);
                }
                return Math.sqrt(error);
            }
        };

        integrator.setSafety(0.95);
        integrator.setMinReduction(0.5);
        integrator.setMaxGrowth(5.0);

        FirstOrderDifferentialEquations equations = (firstOrderDifferentialEquations, y, t) -> {
            double y0 = y[0];
            double y1 = y[1];
            double dy0dt = 0.1 * y0 - 0.1 * y1;
            double dy1dt = 0.1 * y1;
            firstOrderDifferentialEquations.setValue(y, new double[]{dy0dt, dy1dt});
        };

        double t0 = 0.0;
        double[] y0 = {1.0, 1.0};
        double t1 = 1.0;
        double[] y1 = {1.5, 2.0};

        double result = integrator.integrate(equations, t0, y0, t1, new double[y0.length]);
        assertEquals(t1, result, 1e-6);
        assertArrayEquals(y1, y, 1e-6);

        // test events handling
        StepHandler eventHandler = new StepHandler() {
            @Override
            public void handleStep(final StepHandlerEvent event) {
                if (event.getStepNumber() == 1) {
                    assertTrue(event.isFirstStep());
                    assertTrue(event.isLastStep());
                } else if (event.getStepNumber() == 2) {
                    assertTrue(event.isFirstStep());
                    assertFalse(event.isLastStep());
                } else if (event.getStepNumber() == 3) {
                    assertTrue(event.isLastStep());
                    assertFalse(event.isFirstStep());
                }
            }
        };

        List<Event> events = Collections.singletonList(
            new When(1.5, new Event() {
                @Override
                public boolean occurs(double t, double[] y) {
                    return true;
                }
            }));

        integrator.setEquations(equations);
        integrator.setStepHandlers(eventHandler);
        integrator.setEventsHandlersManager(new CombinedEventsManager(events));

        result = integrator.integrate(equations, t0, y0, t1, new double[y0.length]);
        assertEquals(t1, result, 1e-6);
        assertArrayEquals(y1, y, 1e-6);

        // test events handling with errors
        double[] y2 = new double[]{1.0, 1.1};
        result = integrator.integrate(equations, t0, y2, t1, new double[y0.length]);
        assertEquals(t1, result, 1e-6);
        assertArrayEquals(y1, y2, 1e-6);
        eventHandler.reset();
        int stepNumber = 0;
        for (StepHandlerEvent event : integrator.getStepHandlers().getEvents()) {
            stepNumber++;
            eventHandler.handleStep(event);
        }
        assertEquals(3, stepNumber);

        // test non-fsal method
        integrator = new EmbeddedRungeKuttaIntegrator("RK4", false, coefficients, internalWeights, externalWeights, new RungeKuttaStepInterpolator(), 0.0, Double.POSITIVE_INFINITY, null, null) {
            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                double error = 0.0;
                for (int i = 0; i < y0.length; i++) {
                    error += Math.pow(Math.abs(y1[i] - y0[i]), 2);
                }
                return Math.sqrt(error);
            }
        };

        integrator.setSafety(0.95);
        integrator.setMinReduction(0.5);
        integrator.setMaxGrowth(5.0);

        StepHandler eventHandler2 = new StepHandler() {
            int stepNumber = 0;
            @Override
            public void handleStep(final StepHandlerEvent event) {
                stepNumber++;
            }
        };

        integrator.setStepHandlers(eventHandler2);
        integrator.setMinStep(0.001);
        integrator.setMaxStep(1.0);

        double[] bessel = new double[2];
        for (int i = 0; i < bessel.length; i++) {
            bessel[i] = BesselFunctions.besselI(i, 1.0);
        }

        double[] y3 = Arrays.copyOf(bessel, bessel.length);
        result = integrator.integrate(equations, 0.0, y3, 3.0, new double[y0.length]);
        assertEquals(3.0, result, 1e-6);
        assertArrayEquals(BesselFunctions.besselI(2, 3.0), y3, 1e-6);

        // test bessel functions with events
        integrator.setEventsHandlersManager(new CombinedEventsManager(
            Arrays.asList(
                new When(1.5, new Event() {
                    @Override
                    public boolean occurs(double t, double[] y) {
                        return Arrays.equals(y, bessel);
                    }
                }),
                new When(2.5, new Event() {
                    @Override
                    public boolean occurs(double t, double[] y) {
                        return Arrays.equals(y, new double[]{BesselFunctions.besselI(2, 3.0), BesselFunctions.besselI(3, 3.0)});
                    }
                })
            )));

        result = integrator.integrate(equations, 0.0, bessel, 4.0, new double[bessel.length]);
        assertEquals(4.0, result, 1e-6);
        assertArrayEquals(Arrays.copyOf(new double[]{BesselFunctions.besselI(2, 4.0), BesselFunctions.besselI(3, 4.0)}, bessel.length), bessel, 1e-6);

        eventHandler2.reset();
        stepNumber = 0;
        for (StepHandlerEvent event : integrator.getStepHandlers().getEvents()) {
            stepNumber++;
            eventHandler2.handleStep(event);
        }
        assertEquals(5, stepNumber);

        // test bessel functions with non-fsal method
        integrator = new EmbeddedRungeKuttaIntegrator("RKF45", false, coefficients, internalWeights, externalWeights, new RungeKuttaStepInterpolator(), 0.0, Double.POSITIVE_INFINITY, null, null) {
            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                double error = 0.0;
                for (int i = 0; i < y0.length; i++) {
                    error += Math.pow(Math.abs(y1[i] - y0[i]), 2);
                }
                return Math.sqrt(error);
            }
        };

        integrator.setSafety(0.95);
        integrator.setMinReduction(0.5);
        integrator.setMaxGrowth(5.0);

        integrator.setMinStep(0.001);
        integrator.setMaxStep(1.0);

        eventHandler2 = new StepHandler() {
            int stepNumber = 0;
            @Override
            public void handleStep(final StepHandlerEvent event) {
                stepNumber++;
            }
        };

        integrator.setStepHandlers(eventHandler2);

        result = integrator.integrate(equations, 0.0, bessel, 4.0, new double[bessel.length]);
        assertEquals(4.0, result, 1e-6);
        assertArrayEquals(Arrays.copyOf(new double[]{BesselFunctions.besselI(2, 4.0), BesselFunctions.besselI(3, 4.0)}, bessel.length), bessel, 1e-6);

        eventHandler2.reset();
        stepNumber = 0;
        for (StepHandlerEvent event : integrator.getStepHandlers().getEvents()) {
            stepNumber++;
            eventHandler2.handleStep(event);
        }
        assertEquals(5, stepNumber);

    }

    @org.junit.Test(expected = IntegratorException.class)
    public void testIntegratorException() {
        double[] coefficients = {0.0, 0.3, 0.6, 1.0};
        double[][] internalWeights = {
            {0.0, 0.3, 0.6},
            {0.3, 0.48, 0.27},
            {0.45, 0.6, 0.055}
        };
        double[] externalWeights = {0.25, 0.643, 0.0};

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK3", true, coefficients, internalWeights, externalWeights, new RungeKuttaStepInterpolator(), 0.0, Double.POSITIVE_INFINITY, null, null) {
            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                throw new IntegratorException("An error has occurred");
            }
        };

        integrator.setSafety(0.95);
        integrator.setMinReduction(0.5);
        integrator.setMaxGrowth(5.0);

        FirstOrderDifferentialEquations equations = (firstOrderDifferentialEquations, y, t) -> {
            throw new DerivativeException("A derivative error has occurred");
        };

        double[] y0 = {1.0, 1.0};
        double t1 = 1.0;

        integrator.integrate(equations, 0.0, y0, t1, new double[y0.length]);

    }

    @org.junit.Test(expected = DerivativeException.class)
    public void testDerivativeException() {
        double[] coefficients = {0.0, 0.3, 0.6, 1.0};
        double[][] internalWeights = {
            {0.0, 0.3, 0.6},
            {0.3, 0.48, 0.27},
            {0.45, 0.6, 0.055}
        };
        double[] externalWeights = {0.25, 0.643, 0.0};

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK3", true, coefficients, internalWeights, externalWeights, new RungeKuttaStepInterpolator(), 0.0, Double.POSITIVE_INFINITY, null, null) {
            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                throw new DerivativeException("A derivative error has occurred");
            }
        };

        integrator.setSafety(0.95);
        integrator.setMinReduction(0.5);
        integrator.setMaxGrowth(5.0);

        FirstOrderDifferentialEquations equations = (firstOrderDifferentialEquations, y, t) -> {
            // empty
        };

        double[] y0 = {1.0, 1.0};
        double t1 = 1.0;

        integrator.integrate(equations, 0.0, y0, t1, new double[y0.length]);

    }

}