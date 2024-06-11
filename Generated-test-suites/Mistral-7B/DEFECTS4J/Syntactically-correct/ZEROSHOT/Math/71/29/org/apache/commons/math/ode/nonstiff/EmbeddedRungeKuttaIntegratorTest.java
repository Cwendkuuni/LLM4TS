package org.apache.commons.math.ode.nonstiff.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.When;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.apache.commons.math3.ode.test.TestUtils;
import org.apache.commons.math3.special.BesselFunctions;

/**
 * Test suite for EmbeddedRungeKuttaIntegrator class.
 */
public class EmbeddedRungeKuttaIntegratorTest {

    /**
     * Test method for {@link EmbeddedRungeKuttaIntegrator#EmbeddedRungeKuttaIntegrator(String, boolean, double[], double[][], double[], RungeKuttaStepInterpolator, double, double, double[], double[])}.
     */
    @org.junit.Test
    public void testEmbeddedRungeKuttaIntegratorConstruction() {
        // Create a simple ODE system
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getNumberOfEquations() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double y[], double[] yPrime) {
                yPrime[0] = BesselFunctions.bessel(0, 2 * Math.PI * t) * Math.exp(-Math.sqrt(2) * t);
            }
        };

        // Define the test tolerance
        double[] tolerances = { 1.0e-12, 1.0e-13 };

        // Create the RK2 method
        double[] cRk2 = { 0.0, 0.5 };
        double[] aRk2 = { { 0.0, 0.5 }, { 0.5, 0.5 } };
        double[] bRk2 = { 0.25, 0.25 };
        EmbeddedRungeKuttaIntegrator rk2 = new EmbeddedRungeKuttaIntegrator("RK2(2)", false, cRk2, aRk2, bRk2, new RungeKuttaStepInterpolator(), 1.0, 10.0, tolerances) {
            @Override
            public int getOrder() {
                return 2;
            }
        };

        // Create the RK3 method
        double[] cRk3 = { 0.0, 0.3, 0.6 };
        double[] aRk3 = { { 0.0, 0.3, 0.0 }, { 0.3, 0.6, 0.3 }, { 0.6, 0.72, 0.45 } };
        double[] bRk3 = { 1.0 / 6.0, 2.0 / 3.0, 1.0 / 3.0 };
        EmbeddedRungeKuttaIntegrator rk3 = new EmbeddedRungeKuttaIntegrator("RK3(3)", true, cRk3, aRk3, bRk3, new RungeKuttaStepInterpolator(), 1.0, 10.0, tolerances) {
            @Override
            public int getOrder() {
                return 3;
            }
        };

        // Test RK2 method
        double[] y0Rk2 = { 1.0 };
        double   tStartRk2 = 0.0;
        double   tEndRk2 = 5.0;
        double[] yEndRk2 = { BesselFunctions.bessel(0, 2 * Math.PI * 5.0) * Math.exp(-Math.sqrt(2) * 5.0) };
        double[] expectedYRk2 = TestUtils.createExpectedY(equations, tStartRk2, y0Rk2, tEndRk2, yEndRk2, tolerances[0], tolerances[1]);

        StepInterpolator interpolatorRk2 = rk2.getStepInterpolator();
        StepHandler       stepHandlerRk2 = new StepHandler() {
            @Override
            public void handleStep(StepInterpolator interpolator, int step) {
                // Dummy handler
            }
        };
        double t = rk2.integrate(equations, tStartRk2, y0Rk2, tEndRk2, new double[y0Rk2.length]);
        double[] y = interpolatorRk2.getStepY();
        assertTrue(TestUtils.isCloseEnough(y, expectedYRk2, tolerances));

        // Test RK3 method
        double[] y0Rk3 = { 1.0 };
        double   tStartRk3 = 0.0;
        double   tEndRk3 = 5.0;
        double[] yEndRk3 = { BesselFunctions.bessel(0, 2 * Math.PI * 5.0) * Math.exp(-Math.sqrt(2) * 5.0) };
        double[] expectedYRk3 = TestUtils.createExpectedY(equations, tStartRk3, y0Rk3, tEndRk3, yEndRk3, tolerances[0], tolerances[1]);

        StepInterpolator interpolatorRk3 = rk3.getStepInterpolator();
        StepHandler       stepHandlerRk3 = new StepHandler() {
            @Override
            public void handleStep(StepInterpolator interpolator, int step) {
                // Dummy handler
            }
        };
        rk3.setStepHandler(stepHandlerRk3);
        t = rk3.integrate(equations, tStartRk3, y0Rk3, tEndRk3, new double[y0Rk3.length]);
        y = interpolatorRk3.getStepY();
        assertTrue(TestUtils.isCloseEnough(y, expectedYRk3, tolerances));

        // Test RK2 method with events
        double[] yStartRk2 = { 1.0 };
        double   tStartEventRk2 = 1.0;
        double[] yEndEventRk2 = { 0.0 };
        Event eventRk2 = new When(tStartEventRk2, yStartRk2, yEndEventRk2);
        equations.addEvent(eventRk2);

        StepInterpolator interpolatorRk2WithEvents = rk2.getStepInterpolator();
        StepHandler       stepHandlerRk2WithEvents = new StepHandler() {
            @Override
            public void handleStep(StepInterpolator interpolator, int step) {
                // Dummy handler
            }
        };
        rk2.setStepHandler(stepHandlerRk2WithEvents);
        t = rk2.integrate(equations, tStartRk2, y0Rk2, tEndRk2, new double[y0Rk2.length]);
        y = interpolatorRk2.getStepY();
        assertTrue(TestUtils.isCloseEnough(y, expectedYRk2, tolerances));
        assertEquals(1, interpolatorRk2.getNumberOfSteps());
        assertEquals(tStartEventRk2, interpolatorRk2.getStepTimeAt(0));

        // Test RK3 method with events
        double[] yStartRk3 = { 1.0 };
        double   tStartEventRk3 = 1.0;
        double[] yEndEventRk3 = { 0.0 };
        Event eventRk3 = new When(tStartEventRk3, yStartRk3, yEndEventRk3);
        equations.addEvent(eventRk3);

        StepInterpolator interpolatorRk3WithEvents = rk3.getStepInterpolator();
        StepHandler       stepHandlerRk3WithEvents = new StepHandler() {
            @Override
            public void handleStep(StepInterpolator interpolator, int step) {
                // Dummy handler
            }
        };
        rk3.setStepHandler(stepHandlerRk3WithEvents);
        t = rk3.integrate(equations, tStartRk3, y0Rk3, tEndRk3, new double[y0Rk3.length]);
        y = interpolatorRk3.getStepY();
        assertTrue(TestUtils.isCloseEnough(y, expectedYRk3, tolerances));
        assertEquals(2, interpolatorRk3.getNumberOfSteps());
        assertEquals(tStartEventRk3, interpolatorRk3.getStepTimeAt(0));

    }

}