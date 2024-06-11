package org.apache.commons.math.ode.nonstiff.test;

import junit.framework.TestCase;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.When;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;

/**
 * Test for the EmbeddedRungeKuttaIntegrator class.
 */
public class EmbeddedRungeKuttaIntegratorTest extends TestCase {

    /**
     * Test method for {@link EmbeddedRungeKuttaIntegrator#integrate(FirstOrderDifferentialEquations, double, double[], double, double[])}.
     */
    public void testIntegrate() {
        // define the differential equation
        class TestDifferentialEquations implements FirstOrderDifferentialEquations {
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = y[1];
                yPrime[1] = -0.1 * y[1];
            }
        }

        // define the test data
        double[] y0 = new double[] { 1.0, 1.0 };
        double  t0  = 0.0;
        double  t   = 2.0;

        // define the integrator
        EmbeddedRungeKuttaIntegrator integrator =
                new EmbeddedRungeKuttaIntegrator("RK4", false,
                        new double[] { 0.25, 0.25, 0.25, 0.25 },
                        new double[][] { { 0.25, 0.0, 0.25, 0.0, 0.25, 0.0, 0.25, 0.0 },
                                        { 0.0, 0.25, 0.0, 0.25, 0.0, 0.25, 0.0, 0.25 } },
                        new double[] { 0.25, 0.25, 0.25, 0.25 },
                        new RungeKuttaStepInterpolator(),
                        0.001, 0.001,
                        new double[] { 1.0e-9, 1.0e-9 }) {

            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                double error = 0.0;
                for (int i = 0; i < y0.length; i++) {
                    error += Math.abs(yDotK[yDotK.length - 1][i] - yDotK[0][i]);
                }
                return error;
            }
        };

        // define the step handler
        StepHandler stepHandler = new StepHandler() {
            public void handleStep(StepInterpolator interpolator, int currentStep) {
                if (currentStep % 10 == 0) {
                    System.out.println("Step " + currentStep + " at "
                            + interpolator.getTime());
                }
            }
        };

        // integrate the differential equation
        integrator.addStepHandler(stepHandler);
        double result = integrator.integrate(new TestDifferentialEquations(), t0, y0, t, null);

        // check the result
        assertEquals("Integration failed", t, result, 1.0e-9);
    }

    /**
     * Test method for
     * {@link EmbeddedRungeKuttaIntegrator#integrate(FirstOrderDifferentialEquations, double, double[], double, double[])}.
     * with an event.
     */
    public void testIntegrateWithEvent() {
        // define the differential equation
        class TestDifferentialEquations implements FirstOrderDifferentialEquations {
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = y[0];
            }

            public void reset(double t, double[] y) {
                y[0] = 0.0;
            }
        }

        // define the test data
        double[] y0 = new double[] { 0.0 };
        double  t0  = 0.0;
        double  t1  = 1.0;
        double  t2  = 2.0;

        // define the integrator
        EmbeddedRungeKuttaIntegrator integrator =
                new EmbeddedRungeKuttaIntegrator("RK4", false,
                        new double[] { 0.25, 0.25, 0.25, 0.25 },
                        new double[][] { { 0.25, 0.0, 0.25, 0.0, 0.25, 0.0, 0.25, 0.0 },
                                        { 0.0, 0.25, 0.0, 0.25, 0.0, 0.25, 0.0, 0.25 } },
                        new double[] { 0.25, 0.25, 0.25, 0.25 },
                        new RungeKuttaStepInterpolator(),
                        0.001, 0.001,
                        new double[] { 1.0e-9, 1.0e-9 }) {

            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                double error = 0.0;
                for (int i = 0; i < y0.length; i++) {
                    error += Math.abs(yDotK[yDotK.length - 1][i] - yDotK[0][i]);
                }
                return error;
            }
        };

        // define the event
        Event event = new When(1.0);

        // integrate the differential equation with the event
        integrator.addEventsHandlerManager(new CombinedEventsManager(event));
        integrator.addStepHandler(new StepHandler() {
                public void handleStep(StepInterpolator interpolator, int currentStep) {
                    if (currentStep % 10 == 0) {
                        System.out.println("Step " + currentStep + " at "
                                + interpolator.getTime());
                    }
                }
            });
        double result = integrator.integrate(new TestDifferentialEquations(), t0, y0, t2, null);

        // check the result
        assertEquals("Integration failed", t2, result, 1.0e-9);
    }

    /**
     * Test method for
     * {@link EmbeddedRungeKuttaIntegrator#integrate(FirstOrderDifferentialEquations, double, double[], double, double[])}.
     * with a non-fsal integrator.
     */
    public void testIntegrateWithNonFsal() {
        // define the differential equation
        class TestDifferentialEquations implements FirstOrderDifferentialEquations {
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = y[0];
                yPrime[1] = y[1];
            }
        }

        // define the test data
        double[] y0 = new double[] { 1.0, 1.0 };
        double  t0  = 0.0;
        double  t   = 2.0;

        // define the integrator
        EmbeddedRungeKuttaIntegrator integrator =
                new EmbeddedRungeKuttaIntegrator("RKF45", false,
                        new double[] { 0.1, 0.3, 0.3, 0.3, 0.3, 0.1, 0.1, 0.1 },
                        new double[][] { { 0.1, 0.0, 0.1, 0.0, 0.1, 0.0, 0.1, 0.0 },
                                        { 0.0, 0.1, 0.0, 0.1, 0.0, 0.1, 0.0, 0.1 },
                                        { 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1 } },
                        new double[] { 0.1, 0.3, 0.3, 0.3, 0.3, 0.3, 0.3, 0.1 },
                        new RungeKuttaStepInterpolator(),
                        0.001, 0.001,
                        new double[] { 1.0e-9, 1.0e-9 }) {

            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                double error = 0.0;
                for (int i = 0; i < y0.length; i++) {
                    error += Math.abs(yDotK[yDotK.length - 1][i] - yDotK[0][i]);
                }
                return error;
            }

            @Override
            public int getOrder() {
                return 4;
            }
        };

        // integrate the differential equation
        integrator.addStepHandler(new StepHandler() {
            public void handleStep(StepInterpolator interpolator, int currentStep) {
                if (currentStep % 10 == 0) {
                    System.out.println("Step " + currentStep + " at "
                            + interpolator.getTime());
                }
            }
        });
        double result = integrator.integrate(new TestDifferentialEquations(), t0, y0, t, null);

        // check the result
        assertEquals("Integration failed", t, result, 1.0e-9);
    }

    /**
     * Test method for {@link EmbeddedRungeKuttaIntegrator#estimateError(double[][], double[], double[], double)}.
     */
    public void testEstimateError() {
        // define the differential equation
        class TestDifferentialEquations implements FirstOrderDifferentialEquations {
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = 1.0;
                yPrime[1] = 0.1 * y[1];
            }
        }

        // define the test data
        double[] y0 = new double[] { 1.0, 1.0 };
        double  t0  = 0.0;
        double  h  = 0.1;

        // define the integrator
        EmbeddedRungeKuttaIntegrator integrator =
                new EmbeddedRungeKuttaIntegrator("RK4", false,
                        new double[] { 0.25, 0.25, 0.25, 0.25 },
                        new double[][] { { 0.25, 0.0, 0.25, 0.0, 0.25, 0.0, 0.25, 0.0 },
                                        { 0.0, 0.25, 0.0, 0.25, 0.0, 0.25, 0.0, 0.25 } },
                        new double[] { 0.25, 0.25, 0.25, 0.25 },
                        new RungeKuttaStepInterpolator(),
                        0.001, 0.001,
                        new double[] { 1.0e-9, 1.0e-9 }) {

            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                double error = 0.0;
                for (int i = 0; i < y0.length; i++) {
                    error += Math.abs(yDotK[yDotK.length - 1][i] - yDotK[0][i]);
                }
                return error;
            }

            @Override
            public int getOrder() {
                return 4;
            }
        };

        // compute the error
        double error = integrator.estimateError(new double[][] { {}, {}, {}, {} }, y0, y0, h);

        // check the error
        assertEquals("Error estimation failed", 0.0, error, 1.0e-9);
    }

    /**
     * Test method for {@link EmbeddedRungeKuttaIntegrator#estimateError(double[][], double[], double[], double)}.
     * with a non-fsal integrator.
     */
    public void testEstimateErrorWithNonFsal() {
        // define the differential equation
        class TestDifferentialEquations implements FirstOrderDifferentialEquations {
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = y[0];
                yPrime[1] = y[1];
            }
        }

        // define the test data
        double[] y0 = new double[] { 1.0, 1.0 };
        double  t0  = 0.0;
        double  h  = 0.1;

        // define the integrator
        EmbeddedRungeKuttaIntegrator integrator =
                new EmbeddedRungeKuttaIntegrator("RKF45", false,
                        new double[] { 0.1, 0.3, 0.3, 0.3, 0.3, 0.3, 0.3, 0.1 },
                        new double[][] { { 0.1, 0.0, 0.1, 0.0, 0.1, 0.0, 0.1, 0.0 },
                                        { 0.0, 0.1, 0.0, 0.1, 0.0, 0.1, 0.0, 0.1 },
                                        { 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1 } },
                        new double[] { 0.1, 0.3, 0.3, 0.3, 0.3, 0.3, 0.3, 0.1 },
                        new RungeKuttaStepInterpolator(),
                        0.001, 0.001,
                        new double[] { 1.0e-9, 1.0e-9 }) {

            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                double error = 0.0;
                for (int i = 0; i < y0.length; i++) {
                    error += Math.abs(yDotK[yDotK.length - 1][i] - yDotK[0][i]);
                }
                return error;
            }

            @Override
            public int getOrder() {
                return 5;
            }
        };

        // compute the error
        double error = integrator.estimateError(new double[][] { {}, {}, {}, {}, {}, {}, {}, {} }, y0, y0, h);

        // check the error
        assertEquals("Error estimation failed", 0.0, error, 1.0e-9);
    }

}