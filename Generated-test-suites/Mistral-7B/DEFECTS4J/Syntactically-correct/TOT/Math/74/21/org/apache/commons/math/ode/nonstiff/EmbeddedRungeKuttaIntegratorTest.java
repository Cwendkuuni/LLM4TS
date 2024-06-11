package org.apache.commons.math.ode.nonstiff.test;

import junit.framework.TestCase;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.LienardEquations;
import org.apache.commons.math3.ode.events.StepEvent;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.sampling.StepHandler;

public class EmbeddedRungeKuttaIntegratorTest extends TestCase {

    private static final double ABS_TOL = 1e-13;
    private static final double REL_TOL = 1e-14;
    private static final double RK4_MIN_STEP = 0.001;
    private static final double RK4_MAX_STEP = 1000.0;
    private static final double[] RK4_VEC_ABS_TOL = {ABS_TOL, ABS_TOL};
    private static final double[] RK4_VEC_REL_TOL = {REL_TOL, REL_TOL};

    public void testRK4Integrator() throws Exception {
        final String name = "RK4";
        final boolean fsal = false;
        final double[] c = {0.0, 0.5, 0.5, 1.0, 1.0, 1.0};
        final double[][] a = {
            {0.0, 0.0,  0.0,  0.0,  0.0,  0.0},
            {0.25, 0.0,  0.25, 0.0,  0.0,  0.0},
            {0.0,  0.25, 0.0,  0.25, 0.0,  0.0},
            {0.0,  0.0,  0.5,  0.5,  0.5,  0.5},
        };
        final double[] b = {0.25, 0.25, 0.25, 0.25, 0.5, 0.5};
        final RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(name, fsal, c, a, b);

        EmbeddedRungeKuttaIntegrator integrator =
            new EmbeddedRungeKuttaIntegrator(name, fsal, c, a, b, prototype, RK4_MIN_STEP, RK4_MAX_STEP, RK4_VEC_ABS_TOL, RK4_VEC_REL_TOL);

        LienardEquations equations = new LienardEquations();
        double y0[] = {0.0, 1.0};
        double tStart = 0.0;
        double tEnd = 5.0;
        double[] y = new double[2];
        double t = 0.0;

        integrator.initialize(equations, tStart, y0, tEnd, y);

        // standard test
        integrator.integrate(equations, tStart, y0, tEnd, y);
        assertTrue("Test failed: y(tEnd) not close enough to the expected value",
                   isCloseEnough(y, getExpectedValue(tEnd), ABS_TOL, REL_TOL));

        // step size test
        integrator.setMinStep(1e-5);
        integrator.integrate(equations, tStart, y0, tEnd, y);
        assertTrue("Test failed: y(tEnd) not close enough to the expected value",
                   isCloseEnough(y, getExpectedValue(tEnd), ABS_TOL, REL_TOL));
        integrator.setMaxStep(1e-3);
        integrator.integrate(equations, tStart, y0, tEnd, y);
        assertTrue("Test failed: y(tEnd) not close enough to the expected value",
                   isCloseEnough(y, getExpectedValue(tEnd), ABS_TOL, REL_TOL));

        // fail test with too small step size
        integrator.setMinStep(1e-8);
        try {
            integrator.integrate(equations, tStart, y0, tEnd, y);
            fail("Test failed: expected IntegratorException");
        } catch (IntegratorException ie) {
            // expected
        }

        integrator.setMinStep(RK4_MIN_STEP);

        // fail test with too large step size
        integrator.setMaxStep(1e6);
        try {
            integrator.integrator(equations, tStart, y0, tEnd, y);
            fail("Test failed: expected IntegratorException");
        } catch (IntegratorException ie) {
            // expected
        }

        integrator.setMaxStep(RK4_MAX_STEP);

        // event test
        integrator.addEvent(new StepHandler() {
            public void handleStep(StepHandler event, int type) {
                if (type == StepEvent.TYPE_END_INTERVAL) {
                    throw new AssertionFailedError("Test failed: an event should have been triggered before");
                }
            }
        });
        integrator.setMaxStep(1.0);
        integrator.integrate(equations, tStart, y0, tEnd, y);
        assertTrue("Test failed: event was not triggered",
                   eventOccurred(integrator));

        integrator.reset();
        integrator.addStepHandler(new StepHandler() {
            public void handleStep(StepHandler event, int type) {
                if (type == StepEvent.TYPE_END_INTERVAL) {
                    throw new AssertionFailedError("Test failed: an event should not have been triggered");
                }
            }
        });
        integrator.integrate(equations, tStart, y0, tEnd, y);
        assertFalse("Test failed: event was triggered",
                   eventOccurred(integrator));

    }

    private static boolean eventOccurred(EmbeddedRungeKuttaIntegrator integrator) {
        for (StepHandler handler : integrator.getStepHandlers()) {
            if (handler instanceof StepHandlerAdapter) {
                return ((StepHandlerAdapter) handler).isEventOccurred();
            }
        }
        return false;
    }

    private static double[] getExpectedValue(double t) {
        double[] y = new double[2];
        LienardEquations equations = new LienardEquations();
        equations.initialize(t, y);
        return y;
    }

    private static boolean isCloseEnough(double[] a, double[] b, double absTol, double relTol) {
        for (int i = 0; i < a.length; i++) {
            double diff = Math.abs(a[i] - b[i]);
            if (Math.max(Math.abs(a[i]), Math.abs(b[i])) > 0.0 &&
                    (Math.abs(diff) > absTol || diff / Math.max(Math.abs(a[i]), Math.abs(b[i])) > relTol)) {
                return false;
            }
        }
        return true;
    }

    private static class StepHandlerAdapter implements StepHandler {
        private boolean eventOccurred;

        public void handleStep(StepHandler event, int type) {
            if (type == StepEvent.TYPE_END_INTERVAL) {
                eventOccurred = true;
            }
        }

        public boolean isEventOccurred() {
            boolean result = eventOccurred;
            eventOccurred = false;
            return result;
        }
    }

}