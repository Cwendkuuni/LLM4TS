package org.apache.commons.math.ode.nonstiff;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.When;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for the {@link RungeKuttaIntegrator} class.
 */
public class RungeKuttaIntegratorTest {

    /**
     * Test of the Runge-Kutta integrator with a simple equation.
     * @throws MathIllegalArgumentException if step size is too small
     * @throws NumberIsTooSmallException if step size is too small
     * @throws DerivativeException if derivative cannot be computed
     * @throws IntegratorException if integration fails
     */
    @Test
    public void testSimpleIntegration() throws MathIllegalArgumentException, NumberIsTooSmallException, DerivativeException, IntegratorException {
        double[] y0 = {1.0};
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return y0.length;
            }

            @Override
            public void computeDerivatives(double t, double[] y) {
                double yDot[] = new double[y0.length];
                for (int i = 0; i < y0.length; ++i) {
                    yDot[i] = y[i] * 2.0;
                }
                System.arraycopy(yDot, 0, this.yDot, 0, this.yDot.length);
            }
        };

        RungeKuttaIntegrator integrator = new RungeKuttaIntegrator("RK4",
                new double[] {0.25, 0.25, 0.25, 0.25},
                new double[][] {
                        {0.25, 0},
                        {0.25, 0.25},
                        {0.25, 0.25},
                        {0.25, 0.25}
                },
                new double[] {0.25, 0.25, 0.25, 0.25},
                new RungeKuttaStepInterpolator(),
                0.1) {
            @Override
            protected StepHandler createStepHandler() {
                return new StepHandler() {
                    @Override
                    public void handleStep(AbstractStepInterpolator interpolator, int stepNumber) {
                        double t = interpolator.getTime();
                        double[] y = interpolator.getY();
                        Assert.assertArrayEquals("Degenerated step", y0, y);
                    }
                };
            }
        };

        double tEnd = 3.0;
        double[] yEnd = {4.0};
        integrator.integrate(equations, 0.0, y0, tEnd, yEnd);
        Assert.assertArrayEquals("Unexpected final state", yEnd, y0);
    }

    /**
     * Test of the Runge-Kutta integrator with a simple event.
     * @throws MathIllegalArgumentException if step size is too small
     * @throws NumberIsTooSmallException if step size is too small
     * @throws DerivativeException if derivative cannot be computed
     * @throws IntegratorException if integration fails
     */
    @Test
    public void testSimpleEvent() throws MathIllegalArgumentException, NumberIsTooSmallException, DerivativeException, IntegratorException {
        double[] y0 = {1.0};
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return y0.length;
            }

            @Override
            public void computeDerivatives(double t, double[] y) {
                double yDot[] = new double[y0.length];
                for (int i = 0; i < y0.length; ++i) {
                    yDot[i] = y[i] * 2.0;
                }
                System.arraycopy(yDot, 0, this.yDot, 0, this.yDot.length);
            }
        };

        RungeKuttaIntegrator integrator = new RungeKuttaIntegrator("RK4",
                new double[] {0.25, 0.25, 0.25, 0.25},
                new double[][] {
                        {0.25, 0},
                        {0.25, 0.25},
                        {0.25, 0.25},
                        {0.25, 0.25}
                },
                new double[] {0.25, 0.25, 0.25, 0.25},
                new RungeKuttaStepInterpolator(),
                0.1) {
            @Override
            protected StepHandler createStepHandler() {
                return new StepHandler() {
                    @Override
                    public void handleStep(AbstractStepInterpolator interpolator, int stepNumber) {
                        double t = interpolator.getTime();
                        double[] y = interpolator.getY();
                        if (t > 1.5) {
                            throw new EventOccurredException(new When(1.5, new Event() {}));
                        }
                    }
                };
            }
        };

        double tEnd = 2.0;
        double[] yEnd = {4.0};
        integrator.integrate(equations, 0.0, y0, tEnd, yEnd);
    }

}