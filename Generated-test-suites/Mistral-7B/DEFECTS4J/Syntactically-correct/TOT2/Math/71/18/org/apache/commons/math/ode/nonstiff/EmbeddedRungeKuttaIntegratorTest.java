package org.apache.commons.math.ode.nonstiff.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.Polynomials;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.ShiftEvent;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.apache.commons.math3.ode.test.AbstractODETest;
import org.junit.Test;

/**
 * Test cases for {@link EmbeddedRungeKuttaIntegrator}.
 */
public class EmbeddedRungeKuttaIntegratorTest extends AbstractODETest {

    /**
     * Test the EmbeddedRungeKuttaIntegrator with a simple differential equation.
     */
    @Test
    public void testSimpleExample() {
        final double[][] coefficients = {
            {0.0, 0.0, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5},
            {0.0, 0.236926885, 0.275, 0.473629545, 0.546547954, 0.689696125, 0.751912715,
                0.823708332, 0.860583842, 0.921827058},
            {0.0, 0.0, 0.0, 0.45, 0.55, 0.55, 0.55, 0.55, 0.55, 0.55}
        };
        final double[] initialValues = {1.0, 1.0};
        final double minStep = 1.0e-10;
        final double maxStep = 1.0e10;
        final double[] absoluteTolerance = {1.0e-12, 1.0e-12};
        final double[] relativeTolerance = {1.0e-13, 1.0e-13};
        final EmbeddedRungeKuttaIntegrator integrator =
            new EmbeddedRungeKuttaIntegrator("RKF45", true, coefficients[0], coefficients[1], coefficients[2],
                                            new RungeKuttaStepInterpolator(), minStep, maxStep,
                                            absoluteTolerance, relativeTolerance);

        // Test a simple differential equation with no events
        final FirstOrderDifferentialEquations equations =
            new FirstOrderDifferentialEquations() {
                @Override
                public int getDimension() {
                    return 2;
                }

                @Override
                public void computeDerivatives(double t, double[] y, double[] yDot) {
                    yDot[0] = y[1];
                    yDot[1] = -y[0];
                }
            };

        final double[][] testPoints = {
            {0.0, 1.0, 1.0},
            {1.0, 0.0, 1.0},
            {2.0, Math.exp(-2.0), Math.exp(-2.0)}
        };
        for (double[] testPoint : testPoints) {
            double t = testPoint[0];
            final double[] y = new double[initialValues.length];
            System.arraycopy(initialValues, 0, y, 0, initialValues.length);
            double result = integrator.integrate(equations, 0.0, y, t, null) / (t - 0.0);
            double expected = Math.exp(-t) / Math.sqrt(2.0);
            assertEquals(expected, result, 1e-10);
        }

        // Test with an event
        final FirstOrderDifferentialEquations equationsWithEvent =
            new FirstOrderDifferentialEquations() {
                @Override
                public int getDimension() {
                    return 1;
                }

                @Override
                public void computeDerivatives(double t, double[] y, double[] yDot) {
                    yDot[0] = y[0];
                }

                @Override
                public Event[] getEvents(double t) {
                    final Event event = new ShiftEvent(1.0, 1.0);
                    return new Event[] {event};
                }
            };

        final double[] testPointWithEvent = {1.0};
        double tWithEvent = testPointWithEvent[0];
        final double[] yWithEvent = new double[initialValues.length];
        System.arraycopy(initialValues, 0, yWithEvent, 0, initialValues.length);
        integrator.integrate(equationsWithEvent, 0.0, yWithEvent, tWithEvent, null);
        assertEquals(1.0, yWithEvent[0], 1e-10);
    }

    /**
     * Test the EmbeddedRungeKuttaIntegrator with a differential equation that causes
     * a MathIllegalArgumentException.
     */
    @Test
    public void testIllegalArgumentException() {
        final double[][] coefficients = {
            {0.0, 0.0, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5},
            {0.0, 0.236926885, 0.275, 0.473629545, 0.546547954, 0.689696125, 0.751912715,
                0.823708332, 0.860583842, 0.921827058},
            {0.0, 0.0, 0.0, 0.45, 0.55, 0.55, 0.55, 0.55, 0.55, 0.55}
        };
        final double[] initialValues = {1.0, 1.0};
        final double minStep = 1.0e-10;
        final double maxStep = 1.0e10;
        final double[] absoluteTolerance = {1.0e-12, 1.0e-12};
        final double[] relativeTolerance = {1.0e-13, 1.0e-13};
        final EmbeddedRungeKuttaIntegrator integrator =
            new EmbeddedRungeKuttaIntegrator("RKF45", true, coefficients[0], coefficients[1], coefficients[2],
                                            new RungeKuttaStepInterpolator(), minStep, maxStep,
                                            absoluteTolerance, relativeTolerance);

        final FirstOrderDifferentialEquations equations =
            new FirstOrderDifferentialEquations() {
                @Override
                public int getDimension() {
                    return 1;
                }

                @Override
                public void computeDerivatives(double t, double[] y, double[] yDot) {
                    yDot[0] = 1.0 / (y[0] * y[0]);
                }
            };

        final double[] testPoint = {0.0};
        double t = testPoint[0];
        final double[] y = new double[initialValues.length];
        System.arraycopy(initialValues, 0, y, 0, initialValues.length);
        assertThrows(MathIllegalArgumentException.class, () -> integrator.integrate(equations,
                                                                                    0.0, y, t, null));
    }

}