package org.apache.commons.math.ode.nonstiff.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.When;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.apache.commons.math3.special.BesselFunctions;
import org.apache.commons.math3.special.BesselFunctions.BesselFunctionOfFirstKind;
import org.apache.commons.math3.special.BesselFunctions.BesselFunctionOfSecondKind;
import org.apache.commons.math3.special.MathConstants;
import org.junit.Before;
import org.junit.Test;

import org.apache.commons.math.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math.ode.nonstiff.EmbeddedRungeKuttaIntegratorTest;
import org.apache.commons.math.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math.ode.nonstiff.nonlinear.RK23;

/**
 * Tests for {@link EmbeddedRungeKuttaIntegrator}.
 */
public class EmbeddedRungeKuttaIntegratorTest {

    private static final double EPSILON = 1e-14;
    private static final double TOLERANCE = 1e-12;

    private EmbeddedRungeKuttaIntegrator integrator;
    private FirstOrderDifferentialEquations equations;

    @Before
    public void setUp() {
        integrator = new RK23();
        equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getNumberOfEquations() {
                return 1;
            }

            @Override
            public int getOrder() {
                return 2;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = y[1];
            }
        };
    }

    @Test
    public void testConstructor() {
        assertNotNull(integrator);
        assertTrue(integrator instanceof EmbeddedRungeKuttaIntegrator);
        assertEquals(2, integrator.getOrder());
        assertEquals(0.9, integrator.getSafety(), EPSILON);
        assertEquals(0.2, integrator.getMinReduction(), EPSILON);
        assertEquals(10.0, integrator.getMaxGrowth(), EPSILON);
    }

    @Test
    public void testIntegrate() throws Exception {
        double[] y0 = new double[] { 1.0 };
        double  t0  = 0.0;
        double  t1  = Math.PI;

        integrator.setMinStep(1e-5);
        integrator.setMaxStep(1e5);

        double result = integrator.integrate(equations, t0, y0, t1, new double[1]);
        assertTrue(result >= t1 - TOLERANCE);
        assertTrue(result <= t1 + TOLERANCE);
    }

    @Test
    public void testIntegrateStepHandlers() throws Exception {
        double[] y0 = new double[] { 1.0 };
        double  t0  = 0.0;
        double  t1  = MathConstants.PI;

        integrator.setMinStep(1e-5);
        integrator.setMaxStep(1e5);

        // test step handler that checks if step size is greater than 1
        StepInterpolator interpolator = integrator.getInterpolator();
        integrator.addStepHandler((stepHandler) -> {
            if (interpolator.getStepSize() > 1) {
                throw new AssertionError("Step size is greater than 1");
            }
        });

        try {
            integrator.integrate(equations, t0, y0, t1, new double[1]);
            assertFalse(true);

        } catch (Exception e) {
            // expected
        }

        // remove the step handler
        integrator.removeStepHandler(0);

        // test step handler that checks if current time is equal to PI
        integrator.addStepHandler((stepHandler) -> {
            if (interpolator.getTime() != t1) {
                throw new AssertionError("Current time is not " + t1);
            }
        });

        double result = integrator.integrate(equations, t0, y0, t1, new double[1]);
        assertTrue(result >= t1 - TOLERANCE);
        assertTrue(result <= t1 + TOLERANCE);

        // remove the step handler
        integrator.removeStepHandler(0);

        // test step handler that checks if current time is less than PI
        integrator.addStepHandler((stepHandler) -> {
            if (interpolator.getTime() >= t1) {
                throw new AssertionError("Current time is greater than or equal to " + t1);
            }
        });

        try {
            integrator.integrate(equations, t0, y0, t1, new double[1]);
            assertFalse(true);

        } catch (Exception e) {
            // expected
        }

        // remove the step handler
        integrator.removeStepHandler(0);

        // test step handler that checks if current time is equal to PI, and
        // that accepts the step if the time is not greater than PI
        integrator.addStepHandler((stepHandler) -> {
            if (interpolator.getTime() >= t1) {
                throw new AssertionError("Current time is greater than or equal to " + t1);
            }
        });
        integrator.addStepHandler((stepHandler) -> {
            if (interpolator.getTime() != t1) {
                throw new AssertionError("Current time is not " + t1);
            }
        });

        result = integrator.integrate(equations, t0, y0, t1, new double[1]);
        assertTrue(result >= t1 - TOLERANCE);
        assertTrue(result <= t1 + TOLERANCE);

        // remove the step handlers
        integrator.removeStepHandler(0);
        integrator.removeStepHandler(0);

        // test event handler that checks if current time is equal to PI
        double[] y = new double[1];
        integrator.addEventHandler(new When(t1) {
            @Override
            public void handleEvent(double time, double[] eventValue) {
                assertEquals(t1, time, EPSILON);
                assertEquals(1.0, y[0], EPSILON);
            }
        });

        integrator.integrate(equations, t0, y0, t1, new double[1]);

    }

    @Test
    public void testIntegrateEventHandlers() throws Exception {
        double[] y0 = new double[] { 1.0 };
        double  t0  = 0.0;
        double  t1  = MathConstants.PI;

        integrator.setMinStep(1e-5);
        integrator.setMaxStep(1e5);

        // test event handler that throws an exception when integrating
        integrator.addEventHandler(new When(MathConstants.PI / 2) {
            @Override
            public void handleEvent(double time, double[] eventValue) {
                throw new AssertionError("Should not reach this point");
            }
        });

        try {
            integrator.integrate(equations, t0, y0, t1, new double[1]);
            assertFalse(true);

        } catch (Exception e) {
            // expected
        }

        // remove the event handler
        integrator.removeEventHandler(0);

        // test event handler that checks if current time is equal to PI, and
        // that accepts the event if the time is not greater than PI
        integrator.addEventHandler(new When(MathConstants.PI) {
            @Override
            public void handleEvent(double time, double[] eventValue) {
                assertEquals(t1, time, EPSILON);
                assertEquals(1.0, y[0], EPSILON);
            }
        });

        integrator.integrate(equations, t0, y0, t1, new double[1]);

    }

    @Test
    public void testIntegrateBesselFunction() throws Exception {
        double[] y0 = new double[] { 1.0 };
        double  t0  = 0.0;
        double  t1  = Math.PI;

        integrator.setMinStep(1e-5);
        integrator.setMaxStep(1e5);

        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getNumberOfEquations() {
                return 1;
            }

            @Override
            public int getOrder() {
                return 2;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = -y[0] / (t * t) + BesselFunctionOfSecondKind(0, t);
            }
        };

        integrator.addEventHandler(new When(MathConstants.PI) {
            @Override
            public void handleEvent(double time, double[] eventValue) {
                assertEquals(BesselFunctionOfSecondKind(0, time), y[0], EPSILON);
            }
        });

        integrator.integrate(equations, t0, y0, t1, new double[1]);

    }

    @Test
    public void testIntegrateBesselFunctionWithEvent() throws Exception {
        double[] y0 = new double[] { 1.0 };
        double  t0  = 0.0;
        double  t1  = Math.PI;

        integrator.setMinStep(1e-5);
        integrator.setMaxStep(1e5);

        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getNumberOfEquations() {
                return 1;
            }

            @Override
            public int getOrder() {
                return 2;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = -y[0] / (t * t) + BesselFunctionOfSecondKind(0, t);
            }
        };

        integrator.addEvent(new Event() {
            @Override
            public boolean isReached(double t, double[] y) {
                return BesselFunctionOfFirstKind(0, t) >= 0.5;
            }

            @Override
            public double getValue(double t, double[] y) {
                return BesselFunctionOfFirstKind(0, t);
            }
        });

        integrator.integrate(equations, t0, y0, t1, new double[1]);

    }

    @Test
    public void testIntegrateBesselFunctionWithStepHandlers() throws Exception {
        double[] y0 = new double[] { 1.0 };
        double  t0  = 0.0;
        double  t1  = Math.PI;

        integrator.setMinStep(1e-5);
        integrator.setMaxStep(1e5);

        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getNumberOfEquations() {
                return 1;
            }

            @Override
            public int getOrder() {
                return 2;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = -y[0] / (t * t) + BesselFunctionOfSecondKind(0, t);
            }
        };

        integrator.addStepHandler((stepHandler) -> {
            double time = integrator.getInterpolator().getTime();
            double value = BesselFunctionOfFirstKind(0, time);
            if (value >= 0.5) {
                stepHandler.rejectStep();

            }
        });

        integrator.integrate(equations, t0, y0, t1, new double[1]);

    }

}