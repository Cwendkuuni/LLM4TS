package org.apache.commons.math.ode.nonstiff.test;

import junit.framework.TestCase;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.ContinuousEvent;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.Rk23fe;

/**
 * Tests the {@link EmbeddedRungeKuttaIntegrator} class.
 */
public class EmbeddedRungeKuttaIntegratorTest extends TestCase {

    /**
     * Test integration of a simple ODE.
     * @throws Exception if an error occurs
     */
    public void testRk23feIntegration() throws Exception {
        final double[] y0 = {0.0, 1.0};
        final double  t0  = 0.0;
        final double  tEnd = 10.0;
        final double[] yEnd = {1.73205080756888, 2.44948974278317};

        final FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 2;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = -y[1];
                yDot[1] = y[0];
            }
        };

        final EmbeddedRungeKuttaIntegrator integrator = new Rk23fe("Rk23fe", true,
                                                                 new double[] {0.25, 0.4, 0.4, 0.1},
                                                                 new double[][] {{0.0, 0.0, 0.5, 0.5},
                                                                                 {0.25, 0.25, 0.25, 0.25},
                                                                                 {0.25, 0.25, 0.25, 0.25}},
                                                                 new double[] {0.3333333333333333, 0.3333333333333333,
                                                                                0.3333333333333333, 0.5});

        integrator.setMinStep(1.0e-10);
        integrator.setMaxStep(1.0e10);
        integrator.setScaledAbsoluteTolerance(1.0e-12);
        integrator.setScaledRelativeTolerance(1.0e-12);

        final StepHandler stepHandler = new StepHandler() {
            @Override
            public void handleStep(StepHandlerEvent event) {
                if (event.isLastStep()) {
                    final double[] yLast = event.getY();
                    final double[] yExpected = yEnd;
                    for (int i = 0; i < yLast.length; ++i) {
                        assertEquals(yExpected[i], yLast[i], 1.0e-12);
                    }
                }
            }
        };

        integrator.setStepHandler(stepHandler);
        integrator.addEndTimeChecker(tEnd, new ContinuousEvent(tEnd) {});

        integrator.integrate(equations, t0, y0, tEnd, null);
    }

    /**
     * Test integration with an event.
     * @throws Exception if an error occurs
     */
    public void testRk23feWithEvent() throws Exception {
        final double[] y0 = {0.0, 1.0};
        final double  t0  = 0.0;
        final double  tEnd = 3.0;
        final double[] yEnd = {1.0, 1.0};

        final FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 2;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = -y[1];
                yDot[1] = y[0];
            }
        };

        final EmbeddedRungeKuttaIntegrator integrator = new Rk23fe("Rk23fe", true,
                                                                 new double[] {0.25, 0.4, 0.4, 0.1},
                                                                 new double[][] {{0.0, 0.0, 0.5, 0.5},
                                                                                 {0.25, 0.25, 0.25, 0.25},
                                                                                 {0.25, 0.25, 0.25, 0.25}},
                                                                 new double[] {0.3333333333333333, 0.3333333333333333,
                                                                                0.3333333333333333, 0.5});

        integrator.setMinStep(1.0e-10);
        integrator.setMaxStep(1.0e10);
        integrator.setScaledAbsoluteTolerance(1.0e-12);
        integrator.setScaledRelativeTolerance(1.0e-12);

        final StepHandler stepHandler = new StepHandler() {
            @Override
            public void handleStep(StepHandlerEvent event) {
                if (event.isLastStep()) {
                    final double[] yLast = event.getY();
                    final double[] yExpected = yEnd;
                    for (int i = 0; i < yLast.length; ++i) {
                        assertEquals(yExpected[i], yLast[i], 1.0e-12);
                    }
                }
            }
        };

        integrator.setStepHandler(stepHandler);
        integrator.addEndTimeChecker(tEnd, new ContinuousEvent(tEnd) {
            @Override
            protected boolean occurs(double t, double[] y, double[] yDot, double[] yPrime, double[] eventTime,
                                     int eventIndex) {
                if (Math.abs(y[1] - 1.0) < 1.0e-10) {
                    throw new MathIllegalArgumentException("Event detected at step " +
                                                          event.getStepNumber() +
                                                          " instead of " +
                                                          tEnd);
                }
                return false;
            }
        });

        integrator.integrate(equations, t0, y0, tEnd, null);
    }

    /**
     * Test integration with an incorrect initial value.
     * @throws Exception if an error occurs
     */
    public void testRk23feWithIncorrectInitialValue() throws Exception {
        final double[] y0 = {0.0, 2.0};
        final double  t0  = 0.0;
        final double  tEnd = 10.0;

        final FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 2;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = -y[1];
                yDot[1] = y[0];
            }
        };

        final EmbeddedRungeKuttaIntegrator integrator = new Rk23fe("Rk23fe", true,
                                                                 new double[] {0.25, 0.4, 0.4, 0.1},
                                                                 new double[][] {{0.0, 0.0, 0.5, 0.5},
                                                                                 {0.25, 0.25, 0.25, 0.25},
                                                                                 {0.25, 0.25, 0.25, 0.25}},
                                                                 new double[] {0.3333333333333333, 0.3333333333333333,
                                                                                0.3333333333333333, 0.5});

        integrator.setMinStep(1.0e-10);
        integrator.setMaxStep(1.0e10);
        integrator.setScaledAbsoluteTolerance(1.0e-12);
        integrator.setScaledRelativeTolerance(1.0e-12);

        final StepHandler stepHandler = new StepHandler() {
            @Override
            public void handleStep(StepHandlerEvent event) {
                final double[] yLast = event.getY();
                fail("Unexpected result at step " + event.getStepNumber() +
                     ": " + Arrays.toString(yLast));
            }
        };

        integrator.setStepHandler(stepHandler);

        try {
            integrator.integrate(equations, t0, y0, tEnd, null);
            fail("Expected IntegratorException");
        } catch (IntegratorException ie) {
            // success
        }
    }

}