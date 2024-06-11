package org.apache.commons.math.ode.nonstiff.test;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.EventException;
import org.apache.commons.math3.ode.events.SingularityEvent;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.SamplingInterval;
import org.apache.commons.math3.ode.events.StepHandlerAdapter;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for {@link EmbeddedRungeKuttaIntegrator}.
 */
public class EmbeddedRungeKuttaIntegratorTest {

    /**
     * Test of integrate method, of class EmbeddedRungeKuttaIntegrator.
     */
    @Test
    public void testIntegrate() {
        double[][] a = {{0.0, 0.5, 0.5, 0.5}, {0.0, 0.25, 0.25, 0.25}, {0.0, 0.0, 1.0, 1.0}};
        double[] b = {0.0, 0.5, 0.5, 0.5};
        EmbeddedRungeKuttaIntegrator integrator = new Fehlberg78(a, b, RungeKuttaStepInterpolator.DEFAULT);

        double[] y0 = {1.0, 2.0};
        double t0 = 0.0;
        double t1 = 1.0;
        double[] y1 = {3.0, 4.0};
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return y0.length;
            }

            @Override
            public void computeDerivatives(double t, double[] y) {
                for (int i = 0; i < y.length; ++i) {
                    y[i] = y[i] * (1.0 + t);
                }
            }
        };

        try {
            double result = integrator.integrate(equations, t0, y0, t1, y1);
            assertEquals(t1, result, 1e-12);
            assertArrayEquals(y1, y0, 1e-12);
        } catch (Exception e) {
            fail(e.getMessage());
        }

        // test with a singularity event
        integrator = new DormandPrince54(a, b, RungeKuttaStepInterpolator.DEFAULT);
        double[] y2 = {0.0, Math.sqrt(0.0)};
        double t2 = 0.0;
        FirstOrderDifferentialEquations equationsSingularity = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return y2.length;
            }

            @Override
            public void computeDerivatives(double t, double[] y) {
                throw new SingularityEvent();
            }

            @Override
            public Event[] getEvents(double t, double[] y, double h) {
                return new Event[]{new SingularityEvent()};
            }
        };

        try {
            integrator.integrate(equationsSingularity, t2, y2, t2 + 0.01, y2);
            fail("Expected an IntegratorException");
        } catch (IntegratorException ie) {
            // expected
        }

        // test with a step handler
        integrator = new DormandPrince54(a, b, RungeKuttaStepInterpolator.DEFAULT);
        StepHandler stepHandler = new StepHandlerAdapter() {
            @Override
            public void handleStep(StepHandlerAdapter event, double t, double[] y, double[] yDot, int step) {
                assertArrayEquals(y, y0, 1e-12);
                assertArrayEquals(yDot, new double[]{-1.0, 1.0}, 1e-12);
            }
        };
        integrator.setStepHandler(stepHandler);

        try {
            integrator.integrate(equations, t0, y0, t1, y1);
            fail("Expected an IntegratorException");
        } catch (IntegratorException ie) {
            // expected
        }

        // test with non-zero initial conditions
        integrator = new DormandPrince54(a, b, RungeKuttaStepInterpolator.DEFAULT);
        double[] y3 = {1.1, 2.1};
        try {
            integrator.integrate(equations, t0, y3, t1, y1);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException ie) {
            // expected
            assertEquals("Initial conditions and integration range do not match. Initial conditions: [1.1, 2.1], integration range: [0.0, 1.0]", ie.getMessage());
        }

        // test with non-monotonic integration range
        integrator = new DormandPrince54(a, b, RungeKuttaStepInterpolator.DEFAULT);
        double[] y4 = {1.0, 2.0};
        try {
            integrator.integrate(equations, 1.0, y4, 0.0, y1);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException ie) {
            // expected
            assertEquals("Integration range must be monotonic. From: 1.0, To: 0.0", ie.getMessage());
        }

        // test with a large step size
        integrator = new DormandPrince54(a, b, RungeKuttaStepInterpolator.DEFAULT);
        double[] y5 = {1.0, 2.0};
        double h = 10.0;
        try {
            integrator.integrate(equations, t0, y5, t1, y1, h);
            fail("Expected an IntegratorException");
        } catch (IntegratorException ie) {
            // expected
        }

        // test with a small step size
        integrator = new DormandPrince54(a, b, RungeKuttaStepInterpolator.DEFAULT);
        double[] y6 = {1.0, 2.0};
        double hMin = 1e-10;
        try {
            integrator.integrate(equations, t0, y6, t1, y1, hMin);
            fail("Expected an IntegratorException");
        } catch (IntegratorException ie) {
            // expected
        }

        // test with a small integration step size
        integrator = new DormandPrince54(a, b, RungeKuttaStepInterpolator.DEFAULT);
        double[] y7 = {1.0, 2.0};
        double hSmall = 1e-4;
        SamplingInterval interval = new SamplingInterval(t0, t1, hSmall);
        integrator.setStepSizeControl(StepControl.SIMPLE, new SamplingStepHandler(interval));
        try {
            integrator.integrate(equations, t0, y7, t1, y1);
            fail("Expected an IntegratorException");
        } catch (IntegratorException ie) {
            // expected
        }

        // test with a large integration step size
        integrator = new DormandPrince54(a, b, RungeKuttaStepInterpolator.DEFAULT);
        double[] y8 = {1.0, 2.0};
        double hLarge = 1e2;
        SamplingInterval intervalLarge = new SamplingInterval(t0, t1, hLarge);
        integrator.setStepSizeControl(StepControl.SIMPLE, new SamplingStepHandler(intervalLarge));
        try {
            integrator.integrate(equations, t0, y8, t1, y1);
            fail("Expected an IntegratorException");
        } catch (IntegratorException ie) {
            // expected
        }

        // test with a custom step handler
        integrator = new DormandPrince54(a, b, RungeKuttaStepInterpolator.DEFAULT);
        double[] y9 = {1.0, 2.0};
        double hCustom = 0.1;
        StepHandler stepHandlerCustom = new StepHandlerAdapter() {
            @Override
            public void handleStep(StepHandlerAdapter event, double t, double[] y, double[] yDot, int step) {
                if (step == 2) {
                    throw new IntegratorException("Test error");
                }
            }
        };
        integrator.setStepHandler(stepHandlerCustom);
        try {
            integrator.integrate(equations, t0, y9, t1, y1);
            fail("Expected an IntegratorException");
        } catch (IntegratorException ie) {
            // expected
            assertEquals("Test error", ie.getMessage());
        }

        // test with a custom event handler
        integrator = new DormandPrince54(a, b, RungeKuttaStepInterpolator.DEFAULT);
        double[] y10 = {1.0, 2.0};
        double t11 = 1.1;
        EventHandler eventHandler = new EventHandlerAdapter() {
            @Override
            public void handleEvent(Event event, double t, double[] y) {
                if (event instanceof SingularityEvent) {
                    throw new IntegratorException("Test error");
                }
            }
        };
        integrator.setEventHandler(eventHandler);
        try {
            integrator.integrate(equations, t0, y10, t11, y1);
            fail("Expected an IntegratorException");
        } catch (IntegratorException ie) {
            // expected
        }

        // test with a custom event handler
        integrator = new DormandPrince54(a, b, RungeKuttaStepInterpolator.DEFAULT);
        double[] y11 = {1.0, 2.0};
        double t12 = 1.0;
        EventHandler eventHandler2 = new EventHandlerAdapter() {
            @Override
            public void handleEvent(Event event, double t, double[] y) {
                if (event instanceof SingularityEvent) {
                    throw new IntegratorException("Test error");
                }
            }
        };
        integrator.setEventHandler(eventHandler2);
        try {
            integrator.integrate(equations, t0, y11, t12, y1);
            fail("Expected an IntegratorException");
        } catch (IntegratorException ie) {
            // expected
        }

        // test with no events
        integrator = new DormandPrince54(a, b, RungeKuttaStepInterpolator.DEFAULT);
        double[] y12 = {1.0, 2.0};
        try {
            integrator.integrate(new ConstantDifferentialEquations(equations, 1.0), t0, y12, t1, y1);
            fail("Expected an IntegratorException");
        } catch (IntegratorException ie) {
            // expected
        }

        // test with a null event manager
        integrator = new DormandPrince54(a, b, RungeKuttaStepInterpolator.DEFAULT);
        double[] y13 = {1.0, 2.0};
        integrator.setEventHandlerManager(null);
        try {
            integrator.integrate(equations, t0, y13, t1, y1);
            fail("Expected an IntegratorException");
        } catch (IntegratorException ie) {
            // expected
        }

        // test with an empty event manager
        integrator = new DormandPrince54(a, b, RungeKuttaStepInterpolator.DEFAULT);
        double[] y14 = {1.0, 2.0};
        integrator.setEventHandlerManager(new EventHandlerManager());
        try {
            integrator.integrate(equations, t0, y14, t1, y1);
            fail("Expected an IntegratorException");
        } catch (IntegratorException ie) {
            // expected
        }

        // test with a custom step interpolator
        integrator = new DormandPrince54(a, b, new RungeKuttaStepInterpolator() {
            @Override
            public double interpolate(double t, double[] y, double[] yDerivative, int interpolationOrder) {
                throw new UnsupportedOperationException();
            }

            @Override
            public void initialize(double t, double[] y, double[] yDerivative) {
                throw new UnsupportedOperationException();
            }
        });
        double[] y15 = {1.0, 2.0};
        try {
            integrator.integrate(equations, t0, y15, t1, y1);
            fail("Expected an UnsupportedOperationException");
        } catch (UnsupportedOperationException uoe) {
            // expected
        }

        // test with a null prototype step interpolator
        integrator = new DormandPrince54(a, b, null);
        double[] y16 = {1.0, 2.0};
        try {
            integrator.integrate(equations, t0, y16, t1, y1);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // expected
            assertEquals("Prototype interpolator cannot be null", iae.getMessage());
        }

        // test with a null prototype step interpolator constructor
        integrator = new DormandPrince54(a, b, null);
        double[] y17 = {1.0, 2.0};
        try {
            integrator.integrate(equations, t0, y17, t1, y1);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // expected
            assertEquals("Prototype interpolator constructor cannot be null", iae.getMessage());
        }

        // test with a prototype step interpolator constructor with illegal arguments
        integrator = new DormandPrince54(a, b, new RungeKuttaStepInterpolator() {
            @Override
            public double interpolate(double t, double[] y, double[] yDerivative, int interpolationOrder) {
                throw new UnsupportedOperationException();
            }

            @Override
            public void initialize(double t, double[] y, double[] yDerivative) {
                throw new UnsupportedOperationException();
            }
        });
        double[] y18 = {1.0, 2.0};
        try {
            integrator.integrate(equations, t0, y18, t1, y1);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // expected
            assertEquals("Prototype interpolator constructor: The interpolation order must be positive", iae.getMessage());
        }

        // test with a prototype step interpolator constructor with zero arguments
        integrator = new DormandPrince54(a, b, new RungeKuttaStepInterpolator() {
            @Override
            public double interpolate(double t, double[] y, double[] yDerivative, int interpolationOrder) {
                throw new UnsupportedOperationException();
            }

            @Override
            public void initialize(double t, double[] y, double[] yDerivative) {
                throw new UnsupportedOperationException();
            }
        });
        double[] y19 = {1.0, 2.0};
        try {
            integrator.integrate(equations, t0, y19, t1, y1);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // expected
            assertEquals("Prototype interpolator constructor: The interpolation order must be positive", iae.getMessage());
        }
    }

}