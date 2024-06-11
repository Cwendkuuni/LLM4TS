package org.apache.commons.math.ode.nonstiff.test;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.When;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.utils.ODETestUtils;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for {@link EmbeddedRungeKuttaIntegrator}.
 */
public class EmbeddedRungeKuttaIntegratorTest {

    /**
     * Test a simple Runge-Kutta integrator.
     */
    @Test
    public void testSimpleRungeKutta() {
        final double[] a1 = {0.0, 0.5, 0.5, 0.5};
        final double[] b1 = {0.0, 0.5, 0.5, 0.5};
        final double[] c1 = {0.0, 0.25, 0.5, 0.875};
        final double[] y0 = {1.0, 1.0};

        final FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            public int getDimension() {
                return y0.length;
            }

            public void computeDerivatives(double t, double[] y) {
                for (int i = 0; i < y.length; i++) {
                    y[i] = Math.exp(t) * y[i];
                }
            }
        };

        final EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK4", false, c1, a1, b1,
                new RungeKuttaStepInterpolator(RungeKuttaMethod.RK4), 1.0e-12, 1.0e-12, null, null) {
            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                double error = 0.0;
                for (int i = 0; i < y0.length; i++) {
                    error += Math.abs(yDotK[yDotK.length - 1][i] - y1[i]);
                }
                return error;
            }
        };

        final RungeKuttaStepInterpolator interpolator = (RungeKuttaStepInterpolator) integrator.prototype;
        interpolator.setInterpolatorFactor(0.5);

        StepHandler handler = new StepHandler() {
            int count = 0;
            public void handleStep(AbstractStepInterpolator interpolator, boolean last) {
                count++;
            }
        };
        integrator.setStepHandlers(handler);

        try {
            double t = 1.0;
            double[] y = new double[y0.length];
            System.arraycopy(y0, 0, y, 0, y0.length);
            assertEquals(1.0, integrator.integrate(equations, 0.0, y, t, y), 1e-12);
            assertEquals(3, count);
        } catch (Exception e) {
            fail("Should not throw an exception");
        }

        try {
            double t = 1.1;
            double[] y = new double[y0.length];
            System.arraycopy(y0, 0, y, 0, y0.length);
            integrator.integrate(equations, 0.0, y, t, y);
            fail("Should throw an exception");
        } catch (IntegratorException e) {
            // Expected
        }

        try {
            double t = 1.1;
            double[] y = new double[y0.length];
            System.arraycopy(y0, 0, y, 0, y0.length);
            integrator.integrate(equations, 0.0, y, t, y);
            fail("Should throw an exception");
        } catch (MaxCountExceededException e) {
            // Expected
        }

    }

    /**
     * Test an explicit Runge-Kutta integrator where the last evaluation is the same as the first
     * in the next step, so it can be reused.
     */
    @Test
    public void testExplicitRungeKutta() {
        final double[] a1 = {0.0, 0.5, 0.5, 0.5};
        final double[] b1 = {0.0, 0.5, 0.5, 0.5};
        final double[] c1 = {0.0, 0.0, 1.0, 1.0};
        final double[] y0 = {1.0, 1.0};

        final FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            public int getDimension() {
                return y0.length;
            }

            public void computeDerivatives(double t, double[] y) {
                for (int i = 0; i < y.length; i++) {
                    y[i] = Math.exp(t) * y[i];
                }
            }
        };

        final EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RKF2", true, c1, a1, b1,
                new RungeKuttaStepInterpolator(RungeKuttaMethod.RK2), 1.0e-12, 1.0e-12, null, null) {
            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                double error = 0.0;
                for (int i = 0; i < y0.length; i++) {
                    error += Math.abs(yDotK[0][i] - y1[i]);
                }
                return error;
            }
        };

        final RungeKuttaStepInterpolator interpolator = (RungeKuttaStepInterpolator) integrator.prototype;
        interpolator.setInterpolatorFactor(0.5);

        StepHandler handler = new StepHandler() {
            int count = 0;
            public void handleStep(AbstractStepInterpolator interpolator, boolean last) {
                count++;
            }
        };
        integrator.setStepHandlers(handler);

        try {
            double t = 1.0;
            double[] y = new double[y0.length];
            System.arraycopy(y0, 0, y, 0, y0.length);
            assertEquals(1.0, integrator.integrate(equations, 0.0, y, t, y), 1e-12);
            assertEquals(2, count);
        } catch (Exception e) {
            fail("Should not throw an exception");
        }

        try {
            double t = 1.1;
            double[] y = new double[y0.length];
            System.arraycopy(y0, 0, y, 0, y0.length);
            integrator.integrate(equations, 0.0, y, t, y);
            fail("Should throw an exception");
        } catch (IntegratorException e) {
            // Expected
        }

        try {
            double t = 1.1;
            double[] y = new double[y0.length];
            System.arraycopy(y0, 0, y, 0, y0.length);
            integrator.integrate(equations, 0.0, y, t, y);
            fail("Should throw an exception");
        } catch (MaxCountExceededException e) {
            // Expected
        }

        try {
            double t = 2.0;
            double[] y = new double[y0.length];
            System.arraycopy(y0, 0, y, 0, y0.length);
            integrator.integrate(equations, 0.0, y, t, y);
            fail("Should throw an exception");
        } catch (IllegalArgumentException e) {
            // Expected
        }

    }

    /**
     * Test an implicit Runge-Kutta integrator with events.
     */
    @Test
    public void testImplicitRungeKuttaWithEvent() {
        final double[] a1 = {0.0, 0.5, 0.5, 0.5};
        final double[] b1 = {0.0, 0.5, 0.5, 0.5};
        final double[] c1 = {0.0, 0.0, 1.0, 1.0};
        final double[] y0 = {1.0, 1.0};

        final FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            public int getDimension() {
                return y0.length;
            }

            public void computeDerivatives(double t, double[] y) {
                for (int i = 0; i < y.length; i++) {
                    y[i] = Math.exp(t) * y[i];
                }
            }

            public void addEvent(Event event) {
                if (event instanceof When) {
                    When when = (When) event;
                    if (when.getTime() == 2.0) {
                        when.setNextValue(new double[] { 2.0, 2.0 });
                    }
                }
            }
        };

        final EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RKF2", true, c1, a1, b1,
                new RungeKuttaStepInterpolator(RungeKuttaMethod.RK2), 1.0e-12, 1.0e-12, null, null) {
            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                double error = 0.0;
                for (int i = 0; i < y0.length; i++) {
                    error += Math.abs(yDotK[0][i] - y1[i]);
                }
                return error;
            }
        };

        final RungeKuttaStepInterpolator interpolator = (RungeKuttaStepInterpolator) integrator.prototype;
        interpolator.setInterpolatorFactor(0.5);

        StepHandler handler = new StepHandler() {
            int count = 0;
            public void handleStep(AbstractStepInterpolator interpolator, boolean last) {
                count++;
            }
        };
        integrator.setStepHandlers(handler);

        try {
            double t = 1.0;
            double[] y = new double[y0.length];
            System.arraycopy(y0, 0, y, 0, y0.length);
            assertEquals(1.0, integrator.integrate(equations, 0.0, y, t, y), 1e-12);
            assertEquals(2, count);
        } catch (Exception e) {
            fail("Should not throw an exception");
        }

        try {
            double t = 1.1;
            double[] y = new double[y0.length];
            System.arraycopy(y0, 0, y, 0, y0.length);
            integrator.integrate(equations, 0.0, y, t, y);
            fail("Should throw an exception");
        } catch (IntegratorException e) {
            // Expected
        }

        try {
            double t = 1.1;
            double[] y = new double[y0.length];
            System.arraycopy(y0, 0, y, 0, y0.length);
            integrator.integrate(equations, 0.0, y, t, y);
            fail("Should throw an exception");
        } catch (MaxCountExceededException e) {
            // Expected
        }

        try {
            double t = 2.0;
            double[] y = new double[y0.length];
            System.arraycopy(y0, 0, y, 0, y0.length);
            integrator.integrate(equations, 0.0, y, t, y);
            fail("Should throw an exception");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            double t = 2.1;
            double[] y = new double[y0.length];
            System.arraycopy(y0, 0, y, 0, y0.length);
            integrator.integrate(equations, 0.0, y, t, y);
            // The step should be rejected and the state should not be modified
            assertEquals(2.0, y[0], 1e-12);
            assertEquals(2.0, y[1], 1e-12);
        } catch (IntegratorException e) {
            fail("Should not throw an exception");
        }

    }

}