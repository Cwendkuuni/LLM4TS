package org.apache.commons.math.ode.nonstiff.test;

import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.SingularMatrixException;
import org.apache.commons.math3.exception.TooManyIterationsException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.When;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.apache.commons.math3.ode.test.AbstractODETest;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.events.DefaultEventHandler;
import org.apache.commons.math3.ode.events.EventManager;
import org.apache.commons.math3.ode.events.SingularMatrixEvent;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test the {@link RungeKuttaIntegrator} class.
 */
public class RungeKuttaIntegratorTest extends AbstractODETest {

    /**
     * Test the Runge-Kutta 4(5) integrator.
     */
    @Test
    public void testRungeKutta45() {
        final RungeKuttaIntegrator rk = new MidpointIntegrator(
                new double[] { 0.5, 0.5, 0.25, 0.25 },
                new double[][] { { 1.0, 0.0 }, { 0.444444444, 0.444444444 }, { 0.277777778, 0.722222222 }, { 0.2, 0.8 }, { 0, 1.0 } },
                new double[] { 1.0 / 6.0, 1.0 / 3.0, 1.0 / 2.0, 1.0 / 6.0 },
                new RungeKuttaStepInterpolator() {
                    @Override
                    public StepInterpolator copy() {
                        return new RungeKuttaStepInterpolator() {};
                    }
                }, 0.01);
        rk.setMaxSteps(100);

        final double[] y0 = { 0.0, 0.0 };
        final double t0 = 0.0;
        final double tEnd = 1.0;

        // test integration of a simple equation
        final double[] y1 = rk.integrate(new MyDifferentialEquations(), t0, y0, tEnd, null);
        assertEquals(new double[] { 0.57735026918962576, 0.57735026918962576 }, y1, 1.e-6);

        // test integration with a singular matrix event
        final EventManager eventsManager = new EventManager();
        eventsManager.addHandler(new DefaultEventHandler() {
            @Override
            public void handleEvent(Event event) {
                if (event instanceof SingularMatrixEvent) {
                    throw new SingularMatrixException();
                }
            }
        });
        rk.setEventsManager(eventsManager);
        final double[] y2 = rk.integrate(new SingularMatrixDifferentialEquations(), t0, y0, tEnd, null);
        fail("An exception should have been thrown");

        // test integration with a maximum number of iterations
        final int maxIter = 10;
        rk.setMaxIterations(maxIter);
        try {
            rk.integrate(new MyDifferentialEquationsWithMaxIter(), t0, y0, tEnd, null);
            fail("An exception should have been thrown");
        } catch (MaxIterationsExceededException e) {
            // expected
        }

    }

    /**
     * Test the Runge-Kutta 2(3) integrator.
     */
    @Test
    public void testRungeKutta23() {
        final RungeKuttaIntegrator rk = new EulerIntegrator(0.1, new RungeKuttaStepInterpolator() {}, 0.01);
        rk.setMaxSteps(100);

        final double[] y0 = { 0.0, 0.0 };
        final double t0 = 0.0;
        final double tEnd = 1.0;

        // test integration of a simple equation
        final double[] y1 = rk.integrate(new MyDifferentialEquations(), t0, y0, tEnd, null);
        assertEquals(new double[] { 0.5, 0.5 }, y1, 1.e-6);

        // test integration with a maximum number of iterations
        final int maxIter = 10;
        rk.setMaxIterations(maxIter);
        try {
            rk.integrate(new MyDifferentialEquationsWithMaxIter(), t0, y0, tEnd, null);
            fail("An exception should have been thrown");
        } catch (MaxIterationsExceededException e) {
            // expected
        }

    }

}

/**
 * Differential equations for testing the integrators.
 */
class MyDifferentialEquations implements FirstOrderDifferentialEquations {

    @Override
    public int getNumberOfEquations() {
        return 1;
    }

    @Override
    public RealVector evaluateDerivative(RealVector state, RealMatrix jacobian)
            throws SingularMatrixException, IllegalStateException,
            InconsistentStateException {
        final double x = state.getEntry(0);
        final double y = state.getEntry(1);

        final RealVector result = new Array2DRowRealMatrix(new double[][] {{ 1.0 }}).transpose().operate(new double[] { 1.0 - x * x });
        if (jacobian != null) {
            jacobian.setEntry(0, 0, 2.0 * x);
        }
        return result;
    }

}

/**
 * Differential equations for testing the integrators with a singular matrix event.
 */
class SingularMatrixDifferentialEquations implements FirstOrderDifferentialEquations {

    @Override
    public int getNumberOfEquations() {
        return 1;
    }

    @Override
    public RealVector evaluateDerivative(RealVector state, RealMatrix jacobian)
            throws SingularMatrixException, IllegalStateException,
            InconsistentStateException {
        final double x = state.getEntry(0);
        final double y = state.getEntry(1);

        if (x * x < 1.e-12) {
            throw new SingularMatrixException();
        }

        final RealVector result = new Array2DRowRealMatrix(new double[][] {{ 1.0 }}).transpose().operate(new double[] { 1.0 - x * x });
        if (jacobian != null) {
            jacobian.setEntry(0, 0, 2.0 * x);
        }
        return result;
    }

}

/**
 * Differential equations for testing the integrators with a maximum number of iterations.
 */
class MyDifferentialEquationsWithMaxIter implements FirstOrderDifferentialEquations {

    @Override
    public int getNumberOfEquations() {
        return 1;
    }

    @Override
    public RealVector evaluateDerivative(RealVector state, RealMatrix jacobian)
            throws SingularMatrixException, IllegalStateException,
            InconsistentStateException {
        final double x = state.getEntry(0);
        final double y = state.getEntry(1);

        for (int i = 0; i < 1000; ++i) {
            final double x2 = x * x;
            final double y2 = y * y;
            if (x2 > y2) {
                throw new TooManyIterationsException();
            }
        }

        final RealVector result = new Array2DRowRealMatrix(new double[][] {{ 1.0 }}).transpose().operate(new double[] { 1.0 - x * x });
        if (jacobian != null) {
            jacobian.setEntry(0, 0, 2.0 * x);
        }
        return result;
    }

}