package org.apache.commons.math.ode.nonstiff;

import org.apache.commons.math3.exception.MaxAbsRelativeErrorExceededException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.RelativeEvent;
import org.apache.commons.math3.ode.noautodiff.NoAutodiffMethodIntegrator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for {@link RungeKuttaIntegrator} class.
 */
public class RungeKuttaIntegratorTest {

    private RungeKuttaIntegrator integrator;
    private FirstOrderDifferentialEquations equations;
    private double[] initialConditions;
    private double startTime;
    private double endTime;

    /**
     * Initialize test data.
     */
    @Before
    public void setUp() {
        integrator = new EulerIntegrator(0.1, new RungeKuttaStepInterpolator(1), 1.0);
        equations = new DummyDifferentialEquations();
        initialConditions = new double[] {0.0, 1.0};
        startTime = 0.0;
        endTime = 5.0;
    }

    /**
     * Test method {@link RungeKuttaIntegrator#integrate(FirstOrderDifferentialEquations, double, double[], double, double[])}.
     * Checks if the integrator returns correct results for simple test case.
     */
    @Test
    public void testSimpleIntegration() {
        double[] y = integrator.integrate(equations, startTime, initialConditions, endTime, new double[initialConditions.length]);
        assertEquals("The result should be [2.718281828, 2.718281828]", "[" + Math.E + "," + Math.E + "]", Arrays.toString(y));
    }

    /**
     * Test method {@link RungeKuttaIntegrator#integrate(FirstOrderDifferentialEquations, double, double[], double, double[])}.
     * Checks if an exception is thrown when the equations are null.
     */
    @Test(expected = NullArgumentException.class)
    public void testNullEquations() {
        integrator.integrate(null, startTime, initialConditions, endTime, new double[initialConditions.length]);
    }

    /**
     * Test method {@link RungeKuttaIntegrator#integrate(FirstOrderDifferentialEquations, double, double[], double, double[])}.
     * Checks if an exception is thrown when the initial conditions are null.
     */
    @Test(expected = NullArgumentException.class)
    public void testNullInitialConditions() {
        integrator.integrate(equations, startTime, null, endTime, new double[initialConditions.length]);
    }

    /**
     * Test method {@link RungeKuttaIntegrator#integrate(FirstOrderDifferentialEquations, double, double[], double, double[])}.
     * Checks if an exception is thrown when the start time is less than zero.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNegativeStartTime() {
        integrator.integrate(equations, -1.0, initialConditions, endTime, new double[initialConditions.length]);
    }

    /**
     * Test method {@link RungeKuttaIntegrator#integrate(FirstOrderDifferentialEquations, double, double[], double, double[])}.
     * Checks if an exception is thrown when the end time is less than the start time.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testEndTimeLessThanStartTime() {
        integrator.integrate(equations, startTime, initialConditions, -1.0, new double[initialConditions.length]);
    }

    /**
     * Test method {@link RungeKuttaIntegrator#integrate(FirstOrderDifferentialEquations, double, double[], double, double[])}.
     * Checks if an exception is thrown when the step size is too small.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testStepSizeTooSmall() {
        integrator = new RungeKuttaIntegrator("RK4", new double[] {0.25, 0.25, 0.25, 0.25}, 
                new double[][] { {0.0, 0.5, 0.5, 0.5}, {0.25, 0.42, 0.42, 0.1}, {0.25, 0.35, 0.45, 0.1}, {0.25, 0.2, 0.3, 0.3} },
                new double[] {0.25, 0.25, 0.25, 0.25}, new RungeKuttaStepInterpolator(1), 0.00001) {

            @Override
            protected double getStepSize() {
                return 0.0001;
            }

        };
        integrator.integrate(equations, startTime, initialConditions, endTime, new double[initialConditions.length]);
    }

    /**
     * Test method {@link RungeKuttaIntegrator#integrate(FirstOrderDifferentialEquations, double, double[], double, double[])}.
     * Checks if the integrator handles an event correctly.
     */
    @Test
    public void testEventHandling() {
        final double switchTime = 1.5;
        equations = new DummyDifferentialEquationsWithEvents();
        StepHandler stepHandler = new DummyStepHandler();
        final int eventCount = 2;
        integrator = new RungeKuttaIntegrator("RK4", new double[] {0.25, 0.25, 0.25, 0.25}, 
                new double[][] { {0.0, 0.5, 0.5, 0.5}, {0.25, 0.42, 0.42, 0.1}, {0.25, 0.35, 0.45, 0.1}, {0.25, 0.2, 0.3, 0.3} },
                new double[] {0.25, 0.25, 0.25, 0.25}, new RungeKuttaStepInterpolator(1), 0.1) {

            @Override
            protected void addStepHandlers(List<StepHandler> handlers) {
                handlers.add(stepHandler);
            }

            @Override
            protected AbstractStepInterpolator createStepInterpolator() {
                return new RungeKuttaStepInterpolator(1) {

                    @Override
                    public void storeTime(double time) {
                        super.storeTime(time);
                        if (getStep() % 10 == 0) {
                            stepHandler.handleStep(this, false);
                        }
                    }

                };
            }

        };
        integrator.integrate(equations, startTime, initialConditions, endTime, new double[initialConditions.length]);
        assertEquals("The number of events handled should be " + eventCount, eventCount, stepHandler.getStepCount());
    }

    /**
     * Test method {@link RungeKuttaIntegrator#integrate(FirstOrderDifferentialEquations, double, double[], double, double[])}.
     * Checks if the integrator correctly handles a system with relative events.
     */
    @Test
    public void testRelativeEventHandling() {
        final double switchTime = 1.5;
        equations = new DummyDifferentialEquationsWithRelativeEvents();
        StepHandler stepHandler = new DummyStepHandler();
        integrator = new RungeKuttaIntegrator("RK4", new double[] {0.25, 0.25, 0.25, 0.25}, 
                new double[][] { {0.0, 0.5, 0.5, 0.5}, {0.25, 0.42, 0.42, 0.1}, {0.25, 0.35, 0.45, 0.1}, {0.25, 0.2, 0.3, 0.3} },
                new double[] {0.25, 0.25, 0.25, 0.25}, new RungeKuttaStepInterpolator(1), 0.1) {

            @Override
            protected void addStepHandlers(List<StepHandler> handlers) {
                handlers.add(stepHandler);
            }

            @Override
            protected AbstractStepInterpolator createStepInterpolator() {
                return new RungeKuttaStepInterpolator(1) {

                    @Override
                    public void storeTime(double time) {
                        super.storeTime(time);
                        if (getStep() % 10 == 0) {
                            stepHandler.handleStep(this, false);
                        }
                    }

                    @Override
                    public double getLocalTime(int stage) {
                        return getLocalTime(stage) + (stage > 0 ? 0.1 : 0.0);
                    }

                };
            }

        };
        integrator.integrate(equations, startTime, initialConditions, endTime, new double[initialConditions.length]);
        assertEquals("The number of events handled should be 3", 3, stepHandler.getStepCount());
    }

    /**
     * Test method {@link RungeKuttaIntegrator#integrate(FirstOrderDifferentialEquations, double, double[], double, double[])}.
     * Checks if an exception is thrown when the event handler returns an error flag.
     */
    @Test(expected = MaxAbsRelativeErrorExceededException.class)
    public void testEventError() {
        final double switchTime = 1.5;
        equations = new DummyDifferentialEquationsWithErrorEvents();
        integrator = new RungeKuttaIntegrator("RK4", new double[] {0.25, 0.25, 0.25, 0.25}, 
                new double[][] { {0.0, 0.5, 0.5, 0.5}, {0.25, 0.42, 0.42, 0.1}, {0.25, 0.35, 0.45, 0.1}, {0.25, 0.2, 0.3, 0.3} },
                new double[] {0.25, 0.25, 0.25, 0.25}, new RungeKuttaStepInterpolator(1), 0.1) {

            @Override
            protected void addStepHandlers(List<StepHandler> handlers) {
                handlers.add(new DummyStepHandler() {

                    private int errorCount = 0;

                    @Override
                    public void handleStep(AbstractStepInterpolator interpolator, boolean lastStep) {
                        if (errorCount == 1) {
                            throw new MaxAbsRelativeErrorExceededException("Test error", 1.0);
                        }
                        errorCount++;
                    }

                });
            }

        };
        integrator.integrate(equations, startTime, initialConditions, endTime, new double[initialConditions.length]);
    }

    /**
     * Test method {@link RungeKuttaIntegrator#integrate(FirstOrderDifferentialEquations, double, double[], double, double[])}.
     * Checks if the integrator correctly handles a system with no events.
     */
    @Test
    public void testNoEvents() {
        StepHandler stepHandler = new DummyStepHandler();
        integrator = new RungeKuttaIntegrator("RK4", new double[] {0.25, 0.25, 0.25, 0.25}, 
                new double[][] { {0.0, 0.5, 0.5, 0.5}, {0.25, 0.42, 0.42, 0.1}, {0.25, 0.35, 0.45, 0.1}, {0.25, 0.2, 0.3, 0.3} },
                new double[] {0.25, 0.25, 0.25, 0.25}, new RungeKuttaStepInterpolator(1), 0.1) {

            @Override
            protected void addStepHandlers(List<StepHandler> handlers) {
                handlers.add(stepHandler);
            }

        };
        integrator.integrate(new DummyDifferentialEquations(), startTime, initialConditions, endTime, new double[initialConditions.length]);
        assertEquals("The number of steps should be approximately 50", 50, stepHandler.getStepCount());
    }

    /**
     * Test method {@link RungeKuttaIntegrator#integrate(FirstOrderDifferentialEquations, double, double[], double, double[])}.
     * Checks if the integrator correctly handles a system with auto-differentiated methods.
     */
    @Test
    public void testAutodiff() {
        StepHandler stepHandler = new DummyStepHandler();
        integrator = new NoAutodiffMethodIntegrator("RK4", new double[] {0.25, 0.25, 0.25, 0.25}, 
                new double[][] { {0.0, 0.5, 0.5, 0.5}, {0.25, 0.42, 0.42, 0.1}, {0.25, 0.35, 0.45, 0.1}, {0.25, 0.2, 0.3, 0.3} },
                new double[] {0.25, 0.25, 0.25, 0.25}, new RungeKuttaStepInterpolator(1), 0.1) {

            @Override
            protected void addStepHandlers(List<StepHandler> handlers) {
                handlers.add(stepHandler);
            }

        };
        integrator.integrate(new DummyDifferentialEquationsWithAutodiff(), startTime, initialConditions, endTime, new double[initialConditions.length]);
        assertEquals("The number of steps should be approximately 50", 50, stepHandler.getStepCount());
    }

}