package org.apache.commons.math.ode.nonstiff;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NoRootFoundException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.RateEvent;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.sampling.StepHandlerAdapter;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testBasicIntegration() throws MaxCountExceededException, NoRootFoundException, MathIllegalArgumentException, IntegratorException {
        final double[] initialConditions = {0.0, 1.0};
        final double startTime = 0.0;
        final double endTime = 1.0;
        final double toleranceAbs = 1e-8;
        final double toleranceRel = 1e-8;

        // create an integrator
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("rk4", false,
                new double[] {0.25, 0.25, 0.5, 0.5},
                new double[][] {{0.25, 0.25, 0.5, 0.5}, {0.6923066488492137, 0.3376933594386068, 1.0, 1.0}},
                new double[] {0.25, 0.25, 0.5, 0.5},
                new RungeKuttaStepInterpolator(), 1e-8, 1e-8) {
            @Override
            public int getOrder() {
                return 4;
            }
        };

        // create a differential equations system
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return initialConditions.length;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] dy) {
                for (int i = 0; i < initialConditions.length; ++i) {
                    dy[i] = y[i] * (1.0 + t);
                }
            }
        };

        // define a step handler
        StepHandler handler = new StepHandlerAdapter() {
            @Override
            public void handleStep(StepHandlerContext context, int stepNumber) {
                double t = context.getStepTime();
                double[] y = context.getLocalValues();
                assertEquals(Math.exp(t), y[0], toleranceAbs);
                assertEquals(Math.exp(t), y[1], toleranceAbs);
            }
        };

        // integrate the ODE system
        double result = integrator.integrate(equations, startTime, initialConditions, endTime, null, handler);
        assertEquals(endTime, result, toleranceAbs);

        // reset integrator state
        integrator.resetInternalState();
    }

    @Test
    public void testStepSizeControl() throws MaxCountExceededException, NoRootFoundException, MathIllegalArgumentException, IntegratorException {
        final double[] initialConditions = {0.0, 1.0};
        final double startTime = 0.0;
        final double endTime = 1.0;
        final double toleranceAbs = 1e-8;
        final double toleranceRel = 1e-8;

        // create an integrator
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("rk4", false,
                new double[] {0.25, 0.25, 0.5, 0.5},
                new double[][] {{0.25, 0.25, 0.5, 0.5}, {0.6923066488492137, 0.3376933594386068, 1.0, 1.0}},
                new double[] {0.25, 0.25, 0.5, 0.5},
                new RungeKuttaStepInterpolator(), 1e-8, 1e-8) {
            @Override
            public int getOrder() {
                return 4;
            }
        };

        // create a differential equations system with a singular point
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return initialConditions.length;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] dy) {
                for (int i = 0; i < initialConditions.length; ++i) {
                    dy[i] = 1.0 / (y[i] + 1.0);
                }
            }
        };

        // define a step handler
        StepHandler handler = new StepHandlerAdapter() {
            @Override
            public void handleStep(StepHandlerContext context, int stepNumber) {
                double t = context.getStepTime();
                double[] y = context.getLocalValues();
                assertEquals(1.0 / (y[0] + 1.0), y[1], toleranceAbs);
            }
        };

        // integrate the ODE system with large initial conditions
        integrator.setSafety(0.1);
        integrator.setMinReduction(0.1);
        integrator.setMaxGrowth(10.0);
        integrator.integrate(equations, startTime, initialConditions, endTime, null, handler);
        // check that the stepsize was reduced
        assertTrue(integrator.getStepSize() < 1.0);

        // reset integrator state
        integrator.resetInternalState();

        // integrate the ODE system with small initial conditions
        integrator.setSafety(0.9);
        integrator.setMinReduction(0.2);
        integrator.setMaxGrowth(10.0);
        integrator.integrate(equations, startTime, initialConditions, endTime, null, handler);
        // check that the stepsize was not modified
        assertEquals(1.0, integrator.getStepSize(), toleranceAbs);

        // reset integrator state
        integrator.resetInternalState();
    }

    @Test
    public void testStepHandler() throws MaxCountExceededException, NoRootFoundException, MathIllegalArgumentException, IntegratorException {
        final double[] initialConditions = {0.0, 1.0};
        final double startTime = 0.0;
        final double endTime = 1.0;
        final double toleranceAbs = 1e-8;
        final double toleranceRel = 1e-8;

        // create an integrator
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("rk4", false,
                new double[] {0.25, 0.25, 0.5, 0.5},
                new double[][] {{0.25, 0.25, 0.5, 0.5}, {0.6923066488492137, 0.3376933594386068, 1.0, 1.0}},
                new double[] {0.25, 0.25, 0.5, 0.5},
                new RungeKuttaStepInterpolator(), 1e-8, 1e-8) {
            @Override
            public int getOrder() {
                return 4;
            }
        };

        // create a differential equations system with a root
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return initialConditions.length;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] dy) {
                for (int i = 0; i < initialConditions.length; ++i) {
                    dy[i] = y[i] * (1.0 - y[i]);
                }
            }
        };

        // define a step handler raising an exception when the root is reached
        StepHandler handler = new StepHandlerAdapter() {
            @Override
            public void handleStep(StepHandlerContext context, int stepNumber) {
                double t = context.getStepTime();
                double[] y = context.getLocalValues();
                assertFalse(y[0] >= 1.0);
                throw new RuntimeException("Test exception");
            }
        };

        // integrate the ODE system up to the root
        integrator.integrate(equations, startTime, initialConditions, 1.0, null, handler);
        // check that an exception was thrown
        assertThrows(RuntimeException.class, () -> integrator.getLastStepStatus());

        // reset integrator state
        integrator.resetInternalState();
    }

    @Test
    public void testRateEvent() throws MaxCountExceededException, NoRootFoundException, MathIllegalArgumentException, IntegratorException {
        final double[] initialConditions = {0.0, 1.0};
        final double startTime = 0.0;
        final double endTime = 1.0;
        final double toleranceAbs = 1e-8;
        final double toleranceRel = 1e-8;

        // create an integrator
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("rk4", false,
                new double[] {0.25, 0.25, 0.5, 0.5},
                new double[][] {{0.25, 0.25, 0.5, 0.5}, {0.6923066488492137, 0.3376933594386068, 1.0, 1.0}},
                new double[] {0.25, 0.25, 0.5, 0.5},
                new RungeKuttaStepInterpolator(), 1e-8, 1e-8) {
            @Override
            public int getOrder() {
                return 4;
            }
        };

        // create an event handler
        StepHandler handler = new StepHandlerAdapter() {
            @Override
            public void handleStep(StepHandlerContext context, int stepNumber) {
                double t = context.getStepTime();
                double[] y = context.getLocalValues();
                if (t > 0.5) {
                    throw new RateEvent(t, 1.0);
                }
            }
        };

        // create an event manager with one event
        CombinedEventsManager eventsManager = new CombinedEventsManager();
        eventsManager.addEvent(new RateEvent(0.5, 1.0));

        // integrate the ODE system
        integrator.integrate(new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return initialConditions.length;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] dy) {
                for (int i = 0; i < initialConditions.length; ++i) {
                    dy[i] = y[i] * (1.0 + t);
                }
            }
        }, startTime, initialConditions, endTime, integrator, handler, eventsManager);

        // check that the event was detected
        assertNotNull(integrator.getLastEvent());
        assertEquals(0.5, integrator.getLastEvent().getTime(), toleranceAbs);

        // reset integrator state
        integrator.resetInternalState();
    }

}