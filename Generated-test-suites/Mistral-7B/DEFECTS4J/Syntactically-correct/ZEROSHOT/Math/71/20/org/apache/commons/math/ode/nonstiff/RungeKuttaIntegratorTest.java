package org.apache.commons.math.ode.nonstiff.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.When;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.stepper.RungeKuttaIntegrator;
import org.apache.commons.math3.ode.test.AbstractODETest;
import org.apache.commons.math3.ode.test.DummyStepInterpolator;
import org.apache.commons.math3.ode.test.Equations;
import org.apache.commons.math3.ode.test.MockStepHandler;
import org.apache.commons.math3.ode.test.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.test.StepContext;
import org.apache.commons.math3.ode.test.TestEquations;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link RungeKuttaIntegrator}.
 */
public class RungeKuttaIntegratorTest extends AbstractODETest {

    /**
     * Sets up the test.
     */
    @Before
    public void setUp() {
        super.setUp();
        final FirstOrderDifferentialEquations equations = new TestEquations();
        integrator = new RungeKuttaIntegrator(
                "RK45",
                new double[] { 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5 },
                new double[][] { { 0.25, 0 }, { 0, 0.25 }, { 0.25, 0.25 }, { 0, 0.25 },
                                { 0.25, 0.25 }, { 0.25, 0.25 }, { 0, 0.25 }, { 0.25, 0.25 } },
                new double[] { 1.0 / 6.0, 1.0 / 3.0, 1.0 / 3.0, 1.0 / 6.0 },
                new RungeKuttaStepInterpolator(new DummyStepInterpolator()) {
                    @Override
                    public void reinitialize(RungeKuttaIntegrator integrator, double[] y,
                            double[][] yDot, boolean forward) {
                        super.reinitialize(integrator, y, yDot, forward);
                    }
                }, 0.001) {
            @Override
            public void sanityChecks(FirstOrderDifferentialEquations equations, double t0,
                    double[] y0, double t, double[] y) {
                // no-op
            }
        };
    }

    /**
     * Tests the integrator with a simple equation.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testSimple() {
        final double[] y0 = new double[] { 0.0 };
        final double tEnd = 1.0;
        final double[] yEnd = new double[] { Math.exp(1.0) };
        integrator.integrate(new Equations() {
            @Override
            public int getNumberOfEquations() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = y[0] * 0.01;
            }
        }, 0.0, y0, tEnd, yEnd);
    }

    /**
     * Tests the integrator with an event.
     */
    @Test
    public void testEvent() {
        final double[] y0 = new double[] { 0.0 };
        final double tEnd = 1.5;
        final double[] yEnd = new double[] { 1.0 };
        final StepHandler handler = new MockStepHandler();
        integrator.addStepHandler(handler);
        integrator.integrate(new TestEquations() {
            @Override
            public int getNumberOfEquations() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = y[0] * 0.01;
            }
        }, 0.0, y0, tEnd, yEnd);
        assertEquals(2, handler.getStepCount());
        handler.assertStep(0, 0.5, new double[] { 0.25 }, new double[] { 0.00625 });
        handler.assertStep(1, 1.0, new double[] { 1.0 }, new double[] { 0.625 });
    }

    /**
     * Tests the integrator with an event that stops the integration.
     */
    @Test
    public void testEventStop() {
        final double[] y0 = new double[] { 0.0 };
        final double tEnd = 1.5;
        final double[] yEnd = new double[] { 1.0 };
        integrator.addStepHandler(new MockStepHandler() {
            @Override
            public void handleStep(StepContext context, int stepNumber) {
                throw new MaxCountExceededException("Test event exceeded maximum count.");
            }
        });
        integrator.integrate(new TestEquations() {
            @Override
            public int getNumberOfEquations() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = y[0] * 0.01;
            }
        }, 0.0, y0, tEnd, yEnd);
        // The exception is not thrown when testing, but it is thrown during the integration
        // and the test is marked as failed as a result
    }

    /**
     * Tests the integrator with an event that triggers an intermediate step.
     */
    @Test
    public void testEventIntermediate() {
        final double[] y0 = new double[] { 0.0 };
        final double tEnd = 1.5;
        final double[] yEnd = new double[] { 1.0 };
        integrator.addStepHandler(new MockStepHandler() {
            @Override
            public void handleStep(StepContext context, int stepNumber) {
                if (stepNumber == 1) {
                    context.setStepSize(0.1);
                }
            }
        });
        integrator.integrate(new TestEquations() {
            @Override
            public int getNumberOfEquations() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = y[0] * 0.01;
            }
        }, 0.0, y0, tEnd, yEnd);
        assertEquals(3, integrator.getStepCount());
        assertEquals(0.5, integrator.getStepSize(), 1e-12);
        assertEquals(0.1, integrator.getLastStepSize(), 1e-12);
        integrator.assertStep(0, 0.5, new double[] { 0.25 }, new double[] { 0.00625 });
        integrator.assertStep(1, 0.6, new double[] { 0.375 }, new double[] { 0.03125 });
        integrator.assertStep(2, 1.0, new double[] { 1.0 }, new double[] { 0.625 });
    }

    /**
     * Tests the integrator with an event that triggers an immediate stop.
     */
    @Test
    public void testEventImmediateStop() {
        final double[] y0 = new double[] { 0.0 };
        final double tEnd = 1.5;
        integrator.addStepHandler(new MockStepHandler() {
            @Override
            public void handleStep(StepContext context, int stepNumber) {
                throw new When(1.2);
            }
        });
        integrator.integrate(new TestEquations() {
            @Override
            public int getNumberOfEquations() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = y[0] * 0.01;
            }
        }, 0.0, y0, tEnd, yEnd);
        // The exception is not thrown when testing, but it is thrown during the integration
        // and the test is marked as failed as a result
    }

}