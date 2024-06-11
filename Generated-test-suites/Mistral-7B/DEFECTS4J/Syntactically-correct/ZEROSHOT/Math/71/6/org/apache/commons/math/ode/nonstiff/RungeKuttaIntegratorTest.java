package org.apache.commons.math.ode.nonstiff.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NoBracketException;
import org.apache.commons.math3.exception.SingularMatrixException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.ZeroFindEvent;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.apache.commons.math3.ode.test.EquationsTestUtils;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaIntegratorTest;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator.RungeKuttaMethod;
import org.apache.commons.math3.special.BesselFunction;
import org.junit.Test;

/**
 * Test class for {@link RungeKuttaIntegrator}.
 */
public class RungeKuttaIntegratorTest extends RungeKuttaIntegratorTest {

    /**
     * Test the Runge-Kutta integrator for the simple example given in the
     * documentation.
     * @throws Exception if an error occurs
     */
    @Test
    public void testSimpleExample() throws Exception {
        final double a = 0.1;
        final double b = 1.0;
        final double m = 1.0;
        final double[] y0 = new double[] { 0.0, 1.0 };
        final double tStart = 0.0;
        final double tEnd = 10.0;
        final int maxSteps = 1000;
        final double step = 0.1;

        final RungeKuttaMethod method = RungeKuttaMethod.RK4;
        final RungeKuttaIntegrator integrator =
                new RungeKuttaIntegrator(method.getName(), method.getC(), method.getA(), method.getB(),
                                        new RungeKuttaStepInterpolator(method), step) {
                    @Override
                    protected boolean handleZeroCrossing(double t, double[] y, double[] yDot,
                                                           FirstOrderDifferentialEquations equations,
                                                           StepInterpolator interpolator,
                                                           StepHandler stepHandler)
                            throws SingularMatrixException, MaxIterationsExceededException,
                                   NoBracketException, DerivativeException, IntegratorException {
                        final ZeroFindEvent event = new ZeroFindEvent(t, y[0]);
                        final double[] y2 = new double[y.length];
                        System.arraycopy(y, 0, y2, 0, y.length);
                        equations.computeDerivatives(t, y2);
                        stepHandler.handleZeroCrossing(event, yDot);
                        return true;
                    }
                };

        integrator.setMaxSteps(maxSteps);

        final FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public void computeDerivatives(double t, double[] y) {
                final double z = y[1];
                y[0] = a * y[0] + b * (m * Math.sin(z) - z);
                y[1] = z;
            }
        };

        EquationsTestUtils.testRungeKutta(integrator, equations, tStart, y0, tEnd, maxSteps, step);
    }

    /**
     * Test the Runge-Kutta integrator for the simple example given in the
     * documentation, with a step size function.
     * @throws Exception if an error occurs
     */
    @Test
    public void testSimpleExampleWithStepSizeFunction() throws Exception {
        final double a = 0.1;
        final double b = 1.0;
        final double m = 1.0;
        final double[] y0 = new double[] { 0.0, 1.0 };
        final double tStart = 0.0;
        final double tEnd = 10.0;
        final int maxSteps = 1000;

        final RungeKuttaMethod method = RungeKuttaMethod.RK4;
        final RungeKuttaIntegrator integrator =
                new RungeKuttaIntegrator(method.getName(), method.getC(), method.getA(), method.getB(),
                                        new RungeKuttaStepInterpolator(method), 0.1) {
                    @Override
                    protected boolean handleZeroCrossing(double t, double[] y, double[] yDot,
                                                           FirstOrderDifferentialEquations equations,
                                                           StepInterpolator interpolator,
                                                           StepHandler stepHandler)
                            throws SingularMatrixException, MaxIterationsExceededException,
                                   NoBracketException, DerivativeException, IntegratorException {
                        final ZeroFindEvent event = new ZeroFindEvent(t, y[0]);
                        final double[] y2 = new double[y.length];
                        System.arraycopy(y, 0, y2, 0, y.length);
                        equations.computeDerivatives(t, y2);
                        stepHandler.handleZeroCrossing(event, yDot);
                        return true;
                    }

                    @Override
                    public double getStepSize(double t, double[] y, double[] yDot) {
                        final double z = y[1];
                        return Math.min(0.2, Math.abs(BesselFunction.sin(z)));
                    }
                };

        integrator.setMaxSteps(maxSteps);

        final FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public void computeDerivatives(double t, double[] y) {
                final double z = y[1];
                y[0] = a * y[0] + b * (m * Math.sin(z) - z);
                y[1] = z;
            }
        };

        EquationsTestUtils.testRungeKutta(integrator, equations, tStart, y0, tEnd, maxSteps, null);
    }

    /**
     * Test the Runge-Kutta integrator for the Bessel differential equation.
     * @throws Exception if an error occurs
     */
    @Test
    public void testBessel() throws Exception {
        final double[] y0 = new double[] { 1.0 };
        final double tStart = 0.0;
        final double tEnd = 2.0 * Math.PI;
        final int maxSteps = 1000;

        final RungeKuttaMethod method = RungeKuttaMethod.RKF45;
        final RungeKuttaIntegrator integrator =
                new RungeKuttaIntegrator(method.getName(), method.getC(), method.getA(), method.getB(),
                                        new RungeKuttaStepInterpolator(method), 0.1) {
                    @Override
                    protected boolean handleZeroCrossing(double t, double[] y, double[] yDot,
                                                           FirstOrderDifferentialEquations equations,
                                                           StepInterpolator interpolator,
                                                           StepHandler stepHandler)
                            throws SingularMatrixException, MaxIterationsExceededException,
                                   NoBracketException, DerivativeException, IntegratorException {
                        // Do nothing, the zeroFindEvent handler will be triggered by
                        // the default action of the integrator.
                        return true;
                    }
                };

        integrator.setMaxSteps(maxSteps);

        final FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public void computeDerivatives(double t, double[] y) {
                final double z = y[0];
                y[0] = z - z * z;
            }
        };

        EquationsTestUtils.testRungeKutta(integrator, equations, tStart, y0, tEnd, maxSteps, null);
    }

}