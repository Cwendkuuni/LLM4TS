package org.apache.commons.math.ode.nonstiff.test;

import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NoBracketException;
import org.apache.commons.math3.exception.SingularMatrixException;
import org.apache.commons.math3.exception.TooManyIntervalsException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.When;
import org.apache.commons.math3.ode.integrators.RungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaIntegrator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for {@link RungeKuttaIntegrator}.
 */
public class RungeKuttaIntegratorTest {

    /** Sample differential equation. */
    private static final FirstOrderDifferentialEquations sampleProblem = new SampleDifferentialEquations();

    /** Sample Runge-Kutta integrator. */
    private RungeKuttaIntegrator integrator;

    /**
     * Sets up the test.
     */
    @Before
    public void setUp() {
        integrator = new EulerIntegrator(0.1, new EulerStepInterpolator(), 0.1);
    }

    /**
     * Test integration of a simple differential equation with no events.
     * @throws Exception if an error occurs during integration
     */
    @Test
    public void testSimpleIntegration() throws Exception {
        final double[] y0 = {1.0, 0.0};
        final double t0 = 0.0;
        final double t1 = 3.0;
        final double[] y1 = {2.0, 2.0};

        integrator.integrate(sampleProblem, t0, y0, t1, y1);

        Assert.assertArrayEquals("Incorrect final state", y1, new double[]{2.0, 2.0});
    }

    /**
     * Test integration of a differential equation with a single event.
     * @throws Exception if an error occurs during integration
     */
    @Test
    public void testIntegrationWithEvent() throws Exception {
        final double[] y0 = {1.0, 0.0};
        final double t0 = 0.0;
        final double t1 = 3.0;
        final double[] y1 = {2.0, 2.0};

        final Event event = new When(1.5, new SampleEvent());
        integrator = new RungeKuttaIntegrator(1, new RungeKuttaStepInterpolator(), new double[]{0.0, 0.5, 1.0, 1.0}, new double[]{0.5, 0.5, 0.5, 0.5}, new RungeKuttaStepInterpolator(), 0.1) {
            @Override
            protected void addEventHandlers(CombinedEventsManager manager) {
                manager.addEventHandler(event);
            }
        };

        integrator.integrate(sampleProblem, t0, y0, t1, y1);

        Assert.assertArrayEquals("Incorrect final state", y1, new double[]{2.0, 1.5});
    }

    /**
     * Test integration of a differential equation with multiple events.
     * @throws Exception if an error occurs during integration
     */
    @Test
    public void testIntegrationWithMultipleEvents() throws Exception {
        final double[] y0 = {1.0, 0.0};
        final double t0 = 0.0;
        final double t1 = 3.0;
        final double[] y1 = {2.0, 2.0};

        final Event event1 = new When(1.5, new SampleEvent());
        final Event event2 = new When(2.5, new SampleEvent());
        integrator = new RungeKuttaIntegrator(1, new RungeKuttaStepInterpolator(), new double[]{0.0, 0.5, 1.0, 1.0}, new double[]{0.5, 0.5, 0.5, 0.5},
                new RungeKuttaStepInterpolator(), 0.1) {
            @Override
            protected void addEventHandlers(CombinedEventsManager manager) {
                manager.addEventHandler(event1);
                manager.addEventHandler(event2);
            }
        };

        integrator.integrate(sampleProblem, t0, y0, t1, y1);

        Assert.assertArrayEquals("Incorrect final state", y1, new double[]{2.0, 1.0});
    }

    /**
     * Test integration with an exception.
     * @throws Exception if an error occurs during integration
     */
    @Test(expected = MaxIterationsExceededException.class)
    public void testException() throws Exception {
        final double[] y0 = {1.0, 0.0};
        final double t0 = 0.0;
        final double t1 = 1.0;
        final RungeKuttaIntegrator integrator = new RungeKuttaIntegrator(1, new RungeKuttaStepInterpolator(), new double[]{0.0, 0.5, 1.0, 1.0},
                new double[]{0.5, 0.5, 0.5, 0.5}, new RungeKuttaStepInterpolator(), 0.1) {
            @Override
            protected void computeDerivatives(double t, double[] y, double[] dy) throws SingularMatrixException {
                throw new MaxIterationsExceededException("Maximum number of iterations exceeded");
            }
        };

        integrator.integrate(sampleProblem, t0, y0, t1, y0);
    }

    /**
     * Test integration with an exception with events.
     * @throws Exception if an error occurs during integration
     */
    @Test(expected = NoBracketException.class)
    public void testExceptionWithEvent() throws Exception {
        final double[] y0 = {1.0, 0.0};
        final double t0 = 0.0;
        final double t1 = 3.0;
        final double[] y1 = {2.0, 2.0};

        final Event event = new When(1.5, new SampleEvent());
        integrator = new RungeKuttaIntegrator(1, new RungeKuttaStepInterpolator(), new double[]{0.0, 0.5, 1.0, 1.0}, new double[]{0.5, 0.5, 0.5, 0.5},
                new RungeKuttaStepInterpolator(), 0.1) {
            @Override
            protected void computeDerivatives(double t, double[] y, double[] dy) throws SingularMatrixException {
                throw new NoBracketException("No brackets found");
            }

            @Override
            protected void addEventHandlers(CombinedEventsManager manager) {
                manager.addEventHandler(event);
            }
        };

        integrator.integrate(sampleProblem, t0, y0, t1, y0);
    }

    /**
     * Test integration with an exception with multiple events.
     * @throws Exception if an error occurs during integration
     */
    @Test(expected = TooManyIntervalsException.class)
    public void testExceptionWithMultipleEvents() throws Exception {
        final double[] y0 = {1.0, 0.0};
        final double t0 = 0.0;
        final double t1 = 3.0;
        final double[] y1 = {2.0, 2.0};

        final Event event1 = new When(1.5, new SampleEvent());
        final Event event2 = new When(2.5, new SampleEvent());
        integrator = new RungeKuttaIntegrator(1, new RungeKuttaStepInterpolator(), new double[]{0.0, 0.5, 1.0, 1.0}, new double[]{0.5, 0.5, 0.5, 0.5},
                new RungeKuttaStepInterpolator(), 0.1) {
            @Override
            protected void computeDerivatives(double t, double[] y, double[] dy) throws SingularMatrixException {
                throw new TooManyIntervalsException("Too many intervals found");
            }

            @Override
            protected void addEventHandlers(CombinedEventsManager manager) {
                manager.addEventHandler(event1);
                manager.addEventHandler(event2);
            }
        };

        integrator.integrate(sampleProblem, t0, y0, t1, y0);
    }

}

/**
 * Sample differential equation.
 */
class SampleDifferentialEquations implements FirstOrderDifferentialEquations {

    @Override
    public int getNumberOfEquations() {
        return 2;
    }

    @Override
    public void computeDerivatives(double t, double[] y, double[] dy) {
        dy[0] = y[1];
        dy[1] = -y[0];
    }

}

/**
 * Sample event.
 */
class SampleEvent extends Event {

    @Override
    public double getTime() {
        return Double.POSITIVE_INFINITY;
    }

    @Override
    public double[] evaluateEventCondition(double t, double[] y) {
        return new double[]{y[0], y[1]};
    }

    @Override
    public void evaluateEventAction(double t, double[] y, double[] dy) {
        dy[0] += 1.0;
        dy[1] += 1.0;
    }

}