package org.apache.commons.math.ode.events;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.RootNotFoundException;
import org.apache.commons.math3.solvers.BrentSolver;
import org.apache.commons.math3.util.Precision;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link EventState} class.
 */
public class EventStateTest {

    private EventHandler mockEventHandler;
    private EventState eventState;

    @Before
    public void setUp() {
        mockEventHandler = new EventHandler() {
            @Override
            public double g(double t, double[] y) {
                return 0.1 * Math.sin(3 * t);
            }

            @Override
            public void resetState(double t, double[] y) {
                // no-op
            }
        };
        eventState = new EventState(mockEventHandler, 0.01, 1.0e-5, 100);
    }

    @Test
    public void testReinitializeBegin() throws EventException {
        eventState.reinitializeBegin(0.0, new double[4]);
        assertEquals(0.1 * Math.sin(3 * 0.0), eventState.getEventHandler().g(0.0, new double[4]), 1e-5);
        assertEquals(true, eventState.g0Positive);
    }

    @Test
    public void testEvaluateStepWithNoEvent() throws DerivativeException, EventException, ConvergenceException {
        final StepInterpolator interpolator = new StepInterpolator() {
            @Override
            public double getCurrentTime() {
                return 0.1;
            }

            @Override
            public boolean isForward() {
                return true;
            }
        };
        eventState.evaluateStep(interpolator);
        assertFalse(eventState.pendingEvent);
    }

    @Test
    public void testEvaluateStepWithEvent() throws DerivativeException, EventException, ConvergenceException {
        final StepInterpolator interpolator = new StepInterpolator() {
            @Override
            public double getCurrentTime() {
                return 0.2;
            }

            @Override
            public boolean isForward() {
                return true;
            }
        };
        eventState.reinitializeBegin(0.1, new double[4]);
        eventState.evaluateStep(interpolator);
        assertTrue(eventState.pendingEvent);
        assertEquals(0.2, eventState.pendingEventTime, 1e-5);
    }

    @Test(expected = DerivativeException.class)
    public void testEvaluateStepWithDerivativeException() throws EventException, DerivativeException, ConvergenceException {
        final StepInterpolator interpolator = new StepInterpolator() {
            @Override
            public double getCurrentTime() {
                throw new DerivativeException("Derivative error");
            }

            @Override
            public boolean isForward() {
                return true;
            }
        };
        eventState.evaluateStep(interpolator);
    }

    @Test(expected = EventException.class)
    public void testEvaluateStepWithEventException() throws DerivativeException, EventException, ConvergenceException {
        final StepInterpolator interpolator = new StepInterpolator() {
            @Override
            public double getCurrentTime() {
                return 0.2;
            }

            @Override
            public boolean isForward() {
                return true;
            }

            @Override
            public double[] getInterpolatedState() {
                throw new EventException("Event error");
            }
        };
        eventState.evaluateStep(interpolator);
    }

    @Test(expected = ConvergenceException.class)
    public void testEvaluateStepWithConvergenceException() throws DerivativeException, EventException, ConvergenceException {
        final StepInterpolator interpolator = new StepInterpolator() {
            @Override
            public double getCurrentTime() {
                return 0.2;
            }

            @Override
            public boolean isForward() {
                return true;
            }

            @Override
            public double[] getInterpolatedState() {
                return new double[4];
            }
        };
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double t) throws RootNotFoundException, NoBracketingException, MaxIterationsExceededException {
                if (t == 0.2) {
                    throw new ConvergenceException("Convergence error");
                }
                return 0.1 * Math.sin(3 * t);
            }
        };
        BrentSolver solver = new BrentSolver();
        solver.setAbsoluteAccuracy(1.0e-5);
        solver.setMaximalIterationCount(100);
        double root = solver.solve(f, 0.1, 0.3);
        stepAcceptedTest(interpolator, root);
    }

    @Test
    public void testStepAccepted() throws EventException {
        final StepInterpolator interpolator = new StepInterpolator() {
            @Override
            public double getCurrentTime() {
                return 0.2;
            }

            @Override
            public boolean isForward() {
                return true;
            }

            @Override
            public double[] getInterpolatedState() {
                return new double[4];
            }
        };
        double eventTime = 0.21;
        eventState.evaluateStep(interpolator);
        eventState.stepAccepted(0.21, new double[4]);
        assertEquals(0.1 * Math.sin(3 * 0.21), eventState.getEventHandler().g(0.21, new double[4]), 1e-5);
        assertEquals(true, eventState.g0Positive);
    }

    @Test
    public void testStop() throws EventException {
        final StepInterpolator interpolator = new StepInterpolator() {
            @Override
            public double getCurrentTime() {
                return 0.2;
            }

            @Override
            public boolean isForward() {
                return true;
            }

            @Override
            public double[] getInterpolatedState() {
                return new double[4];
            }
        };
        eventState.reinitializeBegin(0.1, new double[4]);
        eventState.evaluateStep(interpolator);
        assertTrue(eventState.pendingEvent);
        eventState.stepAccepted(0.11, new double[4]);
        eventState.stepAccepted(0.12, new double[4]);
        assertTrue(eventState.stop());
    }

    @Test
    public void testReset() throws EventException {
        final StepInterpolator interpolator = new StepInterpolator() {
            @Override
            public double getCurrentTime() {
                return 0.2;
            }

            @Override
            public boolean isForward() {
                return true;
            }

            @Override
            public double[] getInterpolatedState() {
                return new double[4];
            }
        };
        eventState.reinitializeBegin(0.1, new double[4]);
        eventState.evaluateStep(interpolator);
        eventState.stepAccepted(0.11, new double[4]);
        eventState.stepAccepted(0.12, new double[4]);
        eventState.reset(0.13, new double[4]);
        assertFalse(eventState.pendingEvent);
        assertEquals(0.1, eventState.getEventHandler().g(0.13, new double[4]), 1e-5);
    }

    private void stepAcceptedTest(StepInterpolator interpolator, double eventTime) throws EventException {
        eventState.evaluateStep(interpolator);
        eventState.stepAccepted(eventTime, new double[4]);
        assertEquals(0.1 * Math.sin(3 * eventTime), eventState.getEventHandler().g(eventTime, new double[4]), 1e-5);
        assertEquals(true, eventState.g0Positive);
        assertEquals(EventHandler.CONTINUE, eventState.nextAction);
    }

}