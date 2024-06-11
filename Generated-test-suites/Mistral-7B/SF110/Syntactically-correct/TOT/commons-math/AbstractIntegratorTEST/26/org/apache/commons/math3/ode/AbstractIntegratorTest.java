package org.apache.commons.math3.ode.test;

import junit.framework.TestCase;
import org.apache.commons.math3.ode.AbstractIntegrator;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.EventRate;
import org.apache.commons.math3.ode.events.ImpulsiveStepHandler;
import org.apache.commons.math3.ode.events.MoreThanEvent;
import org.apache.commons.math3.ode.events.WhenReachedEvent;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.apache.commons.math3.ode.sampling.StepRequest;
import org.apache.commons.math3.ode.sampling.StepResponse;
import org.apache.commons.math3.ode.sampling.StepSizeRule;
import org.apache.commons.math3.ode.util.ExpandableStatefulODE;
import org.apache.commons.math3.ode.util.ODEEventMultistepIntegrator;
import org.apache.commons.math3.ode.util.ODEIntegrator;
import org.apache.commons.math3.ode.util.ODEStateAndDerivative;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.PrecisionException;
import org.apache.commons.math3.util.UnivariateSolver;

public class AbstractIntegratorTest extends TestCase {

    private static final int MAX_EVALUATIONS = 100;
    private static final int MAX_ITERATION_COUNT = 10;
    private static final double MAX_CHECK_INTERVAL = 0.01;
    private static final double CONVERGENCE = 1e-12;

    public void testIntegration() throws Exception {
        final String integratorName = "RKF45Integrator";
        final double t0 = 0.0;
        final double[] y0 = {1.0, 2.0};
        final double tEnd = 10.0;
        final FirstOrderDifferentialEquations equations = new MyDifferentialEquations();

        // Create integrator
        AbstractIntegrator integrator = new ODEIntegrator(integratorName);

        // Set initial conditions and rules
        integrator.initIntegration(t0, y0, tEnd);
        integrator.setMaxEvaluations(MAX_EVALUATIONS);
        integrator.setStepSizeRule(new StepSizeRule() {
            public double[] getStepSize(final double t, final double[] y, final double[] yDot) {
                return new double[]{0.1};
            }
        });

        // Set event handlers
        integrator.addEventHandler(new WhenReachedEvent(5.0), MAX_CHECK_INTERVAL, CONVERGENCE, MAX_ITERATION_COUNT, new BracketingNthOrderBrentSolver(CONVERGENCE, 5));
        integrator.addEventHandler(new ImpulsiveEvent(1.0), new ExpungeStateHandler());
        integrator.addEventHandler(new MoreThanEvent(5.0, 3.0), MAX_CHECK_INTERVAL, CONVERGENCE, MAX_ITERATION_COUNT, new BracketingNthOrderBrentSolver(CONVERGENCE, 5));

        // Integrate
        double t = integrator.integrate(equations, t0, y0, tEnd, new double[equations.getDimension()]);
        assertEquals(tEnd, t, 1e-6);

        // Test event handling
        assertEquals(3, integrator.getEventHandlers().size());
        for (EventHandler eventHandler : integrator.getEventHandlers()) {
            if (eventHandler instanceof WhenReachedEvent) {
                WhenReachedEvent whenReachedEvent = (WhenReachedEvent) eventHandler;
                assertEquals(5.0, whenReachedEvent.getEventTime(), 1e-6);
            }
            if (eventHandler instanceof ImpulsiveEvent) {
                ImpulsiveEvent impulsiveEvent = (ImpulsiveEvent) eventHandler;
                assertEquals(1.0, impulsiveEvent.getEventTime(), 1e-6);
            }
            if (eventHandler instanceof MoreThanEvent) {
                MoreThanEvent moreThanEvent = (MoreThanEvent) eventHandler;
                assertEquals(5.0, moreThanEvent.getLowerLimit(), 1e-6);
                assertEquals(3.0, moreThanEvent.getUpperLimit(), 1e-6);
            }
        }
    }

    public void testIntegrationWithError() throws Exception {
        final String integratorName = "RKF45Integrator";
        final double t0 = 0.0;
        final double[] y0 = {1.0, 2.0};
        final double tEnd = 10.0;
        final FirstOrderDifferentialEquations equations = new MyDifferentialEquationsWithError();

        // Create integrator
        AbstractIntegrator integrator = new ODEIntegrator(integratorName);

        // Set initial conditions and rules
        integrator.initIntegration(t0, y0, tEnd);
        integrator.setMaxEvaluations(MAX_EVALUATIONS);
        integrator.setStepSizeRule(new StepSizeRule() {
            public double[] getStepSize(final double t, final double[] y, final double[] yDot) {
                return new double[]{0.1};
            }
        });

        // Integrate
        try {
            integrator.integrate(equations, t0, y0, tEnd, new double[equations.getDimension()]);
            fail("Expected DimensionMismatchException");
        } catch (DimensionMismatchException e) {
            // Expected
        }

        // Test that error was reported
        assertEquals(1, integrator.getEvaluations());
    }

    private static class MyDifferentialEquations implements FirstOrderDifferentialEquations {

        public int getDimension() {
            return 2;
        }

        public void computeDerivatives(final double t, final double[] y, final double[] yDot) {
            yDot[0] = y[1];
            yDot[1] = -y[0];
        }
    }

    private static class MyDifferentialEquationsWithError implements FirstOrderDifferentialEquations {

        public int getDimension() {
            return 2;
        }

        public void computeDerivatives(final double t, final double[] y, final double[] yDot) {
            if (y[0] < 0) {
                throw new IllegalArgumentException("y[0] cannot be negative");
            }
            yDot[0] = y[1];
            yDot[1] = -y[0];
        }
    }

    private static class ExpungeStateHandler implements EventHandler {

        public void handleEvent(final double t, final double[] y) {
            // Remove all event handlers
            for (int i = AbstractIntegrator.this.getEventHandlers().size() - 1; i >= 0; i--) {
                AbstractIntegrator.this.removeEventHandler(AbstractIntegrator.this.getEventHandlers().get(i));
            }
        }

        public void init(final double t0, final double[] y0, final double t) {}

        public double evaluateStep(final StepInterpolator interpolator) {
            return Double.POSITIVE_INFINITY;
        }

        public void stepAccepted(final double t, final double[] yPrimary) {}

        public void stepRejected(final double t, final double[] yPrimary, final double[] ySecondary) {}
    }

    private static class BracketingNthOrderBrentSolver implements UnivariateSolver {

        private final double convergence;
        private final int maxIterationCount;

        public BracketingNthOrderBrentSolver(final double convergence, final int maxIterationCount) {
            this.convergence = convergence;
            this.maxIterationCount = maxIterationCount;
        }

        public double solve(final UnivariateFunction f, final double bracketEnd, final double initialBracketMin, final double initialBracketMax, final double tolerance) throws PrecisionException {
            throw new UnsupportedOperationException("Not supported by this solver.");
        }

        public double solve(final UnivariateFunction f, final double bracketEnd, final double bracketMid, final double initialBracketMin) throws PrecisionException {
            double[] brent = new double[3];
            brent[0] = initialBracketMin;
            brent[1] = bracketMid;
            brent[2] = bracketEnd;
            double f0 = f.value(brent[0]);
            double f1 = f.value(brent[1]);
            double f2 = f.value(brent[2]);
            int iterations = 0;
            while (true) {
                if (iterations >= maxIterationCount) {
                    throw new PrecisionException("Maximum number of iterations reached.");
                }
                if (f0 * f2 < 0) {
                    double brentTemp = brent[1];
                    brent[1] = brent[2];
                    brent[2] = brentTemp;
                    f0 = f1;
                    f1 = f2;
                }
                double brentMid = brent[1];
                double beta = 2.0 / 3.0;
                if (f0 * f1 > 0) {
                    beta = 1.0 / 3.0;
                }
                double brentNew = brentMid - (f1 * (brent[2] - brent[1]) / (f2 - f1));
                double f3 = f.value(brentNew);
                if (Math.abs(f3) <= convergence) {
                    return brentNew;
                }
                if ((f3 > 0 && f1 < 0) || (f3 < 0 && f1 > 0)) {
                    brent[1] = brentNew;
                    f1 = f3;
                } else {
                    brent[2] = brentNew;
                    f2 = f3;
                }
                iterations++;
            }
        }
    }

    private static interface UnivariateFunction {

        public double value(final double x);
    }
}