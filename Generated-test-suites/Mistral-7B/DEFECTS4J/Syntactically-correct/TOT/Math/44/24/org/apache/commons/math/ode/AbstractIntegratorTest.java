package org.apache.commons.math.ode.test;

import junit.framework.TestCase;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.events.IntegratorEvent;
import org.apache.commons.math3.ode.events.events.AbstractContinuousEvent;
import org.apache.commons.math3.ode.events.events.GoalType;
import org.apache.commons.math3.ode.events.events.StoppingEvent;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.apache.commons.math3.ode.sampling.Stepper;
import org.apache.commons.math3.ode.test.AbstractODETest;
import org.apache.commons.math3.ode.util.StepperOptions;
import org.apache.commons.math3.util.Precision;

/**
 * Test for {@link AbstractIntegrator}.
 */
public class AbstractIntegratorTest extends AbstractODETest {

    /**
     * Test the integration of a simple ODE: <br>
     * <code>y' = -y</code>.
     */
    public void testSimpleODE() {
        final double tStart = 0.0;
        final double tEnd = 10.0;
        final double accuracy = 1.0e-10;

        final UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double y) {
                return -y;
            }
        };

        final FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations(1, f);
        final StepperOptions options = new StepperOptions();
        final Stepper integrator = new RungeKuttaFehlberg78Stepper(options);

        // Set up the event handler
        final double eventTime = 1.0;
        final StoppingEvent event = new StoppingEvent(eventTime, new GoalType() {
            @Override
            public boolean goalTypeReached(double t, double y, double[] yDot) {
                return Precision.equals(t, eventTime, accuracy);
            }
        });
        integrator.addEventHandler(event);

        // Set up the step handlers
        final List<StepHandler> stepHandlers = new ArrayList<>();
        integrator.addStepHandler(new StepHandler() {
            @Override
            public void handleStep(StepInterpolator interpolator, boolean isLastStep) {
                final double t = interpolator.getGlobalCurrentTime();
                final double y = interpolator.getInterpolatedState()[0];
                if (isLastStep) {
                    System.out.println("Reached the target time: " + t);
                }
            }
        });

        // Perform the integration
        try {
            integrator.integrate(equations, tStart, new double[] { 1.0 }, tEnd);
        } catch (final Exception e) {
            fail("Integration failed with exception:\n" + e.getMessage());
        }

        // Check the results
        final double[] y = integrator.getInterpolatedState();
        final double t = integrator.getTime();
        assertEquals("Error in the integration result", -1.0, y[0], accuracy);
        assertEquals("Error in the integration time", tEnd, t, accuracy);
    }

    /**
     * Test the integration of a simple ODE with an event that stops the integration.
     */
    public void testStoppingEvent() {
        final double tStart = 0.0;
        final double tEnd = 2.0;
        final double eventTime = 1.0;
        final double accuracy = 1.0e-8;

        final UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double y) {
                return -y;
            }
        };

        final FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations(1, f);

        // Set up the event handler
        final StoppingEvent event = new StoppingEvent(eventTime, new GoalType() {
            @Override
            public boolean goalTypeReached(double t, double y, double[] yDot) {
                return Precision.equals(t, eventTime, accuracy);
            }
        });
        event.setStopState(Stepper.STOP_STATE_STOP);
        event.setResetState(Stepper.RESET_STATE_RESET_STATE_ALL);

        // Set up the step handlers
        final List<StepHandler> stepHandlers = new ArrayList<>();
        stepHandlers.add(new StepHandler() {
            @Override
            public void handleStep(StepInterpolator interpolator, boolean isLastStep) {
                final double t = interpolator.getGlobalCurrentTime();
                final double y = interpolator.getInterpolatedState()[0];
                if (isLastStep) {
                    System.out.println("Reached the target time: " + t);
                }
            }
        });
        stepHandlers.add(new StepHandler() {
            @Override
            public void handleStep(StepInterpolator interpolator, boolean isLastStep) {
                final double t = interpolator.getGlobalCurrentTime();
                final double y = interpolator.getInterpolatedState()[0];
                if (y < 0.5 && !isLastStep) {
                    event.reset();
                    interpolator.reset();
                }
            }
        });

        // Perform the integration
        try {
            integrator.integrate(equations, tStart, new double[] { 1.0 }, tEnd);
        } catch (final Exception e) {
            fail("Integration failed with exception:\n" + e.getMessage());
        }

        // Check the results
        final double[] y = integrator.getInterpolatedState();
        final double t = integrator.getTime();
        assertEquals("Error in the integration result", 0.5, y[0], accuracy);
        assertEquals("Error in the integration time", tEnd, t, accuracy);
    }

    /**
     * Test the integration of a simple ODE with an event that resets the state.
     */
    public void testResettingEvent() {
        final double tStart = 0.0;
        final double tEnd = 2.0;
        final double eventTime = 1.0;
        final double accuracy = 1.0e-8;

        final UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double y) {
                return -y;
            }
        };

        final FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations(1, f);

        // Set up the event handler
        final AbstractContinuousEvent event = new AbstractContinuousEvent() {
            @Override
            public EventState evaluateEvent(double t, double[] y, double[] yDot) {
                return new EventState() {
                    @Override
                    public void stepAccepted(double eventTime, double[] eventY) {
                        reset();
                    }

                    @Override
                    public boolean stop() {
                        return false;
                    }
                };
            }

            @Override
            public double eventTime() {
                return eventTime;
            }
        };
        event.setType(IntegratorEvent.Type.CONTINUOUS);
        event.setGoalType(new GoalType() {
            @Override
            public boolean goalTypeReached(double t, double y, double[] yDot) {
                return Precision.equals(t, eventTime, accuracy);
            }
        });

        // Set up the step handlers
        final List<StepHandler> stepHandlers = new ArrayList<>();
        stepHandlers.add(new StepHandler() {
            @Override
            public void handleStep(StepInterpolator interpolator, boolean isLastStep) {
                final double t = interpolator.getGlobalCurrentTime();
                final double y = interpolator.getInterpolatedState()[0];
                if (y < 0.5 && !isLastStep) {
                    event.reset();
                    interpolator.reset();
                }
            }
        });

        // Perform the integration
        try {
            integrator.integrate(equations, tStart, new double[] { 1.0 }, tEnd);
        } catch (final Exception e) {
            fail("Integration failed with exception:\n" + e.getMessage());
        }

        // Check the results
        final double[] y = integrator.getInterpolatedState();
        final double t = integrator.getTime();
        assertEquals("Error in the integration result", 1.0, y[0], accuracy);
        assertEquals("Error in the integration time", tEnd, t, accuracy);
    }

}