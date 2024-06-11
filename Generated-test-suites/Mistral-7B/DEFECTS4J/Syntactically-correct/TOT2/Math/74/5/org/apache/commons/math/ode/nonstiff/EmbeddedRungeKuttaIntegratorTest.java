package org.apache.commons.math.ode.nonstiff.test;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.When;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;

/**
 * Test for the {@link EmbeddedRungeKuttaIntegrator} class.
 */
public class EmbeddedRungeKuttaIntegratorTest extends TestCase {

    /**
     * Main test method for the {@link EmbeddedRungeKuttaIntegrator} class.
     * @param args not used
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    /**
     * Create a suite of test methods for this class.
     * @return the test suite
     */
    public static Test suite() {
        return new TestSuite(EmbeddedRungeKuttaIntegratorTest.class);
    }

    /**
     * Test method for {@link EmbeddedRungeKuttaIntegrator#integrate(FirstOrderDifferentialEquations, double, double[], double, double[])}.
     */
    public void testIntegration() {
        double[] y0 = new double[] {1.0, 1.0};
        double t0 = 0.0;
        double t1 = 1.0;

        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getNumberOfEquations() {
                return y0.length;
            }

            @Override
            public void derive(double t, double[] y, double[] yPrime) {
                for (int i = 0; i < y.length; i++) {
                    yPrime[i] = y[i] * (t + 1.0);
                }
            }
        };

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("name", false,
                new double[] {0.3, 0.4, 0.5, 0.6}, new double[][] {
                    {0.0, 0.3, 0.4, 0.1, 0.2, 0.2, 0.1},
                    {0.1, 0.4, 0.0, 0.3, 0.2, 0.2, 0.2},
                    {0.2, 0.2, 0.3, 0.0, 0.4, 0.1, 0.1},
                    {0.3, 0.1, 0.2, 0.4, 0.0, 0.4, 0.1},
                    {0.4, 0.2, 0.1, 0.1, 0.0, 0.3, 0.3}
                }, new RungeKuttaStepInterpolator(), 0.001, 0.1,
                new double[] {1.0e-10, 1.0e-10}, new double[] {1.0e-10, 1.0e-10});

        integrator.setSafety(0.9);
        integrator.setMinReduction(0.2);
        integrator.setMaxGrowth(10.0);

        StepHandler handler = new StepHandler() {
            @Override
            public void handleStep(StepHandler event, int last) {
                System.out.println("Step " + last + " accepted at " + integrator.getLastTime());
            }
        };
        integrator.setStepHandlers(handler);

        integrator.integrate(equations, t0, y0, t1, new double[y0.length]);

        assertEquals("Integration failed", y0.length, integrator.getNumberOfEquations());
        assertEquals("Integration failed", t1, integrator.getLastTime(), 1e-14);
        double[] y1 = integrator.getY();
        for (int i = 0; i < y0.length; i++) {
            assertEquals("Integration failed", y0[i], y1[i], 1e-14);
        }
    }

    /**
     * Test method for {@link EmbeddedRungeKuttaIntegrator#integrate(FirstOrderDifferentialEquations, double, double[], double, double[], double[], double[], double[], double[], double[])}.
     */
    public void testIntegrationWithEvents() {
        double[] y0 = new double[] {1.0, 1.0};
        double t0 = 0.0;
        double t1 = 1.0;

        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getNumberOfEquations() {
                return y0.length;
            }

            @Override
            public void derive(double t, double[] y, double[] yPrime) {
                for (int i = 0; i < y.length; i++) {
                    yPrime[i] = y[i] * (t + 1.0);
                }
            }
        };

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("name", false,
                new double[] {0.3, 0.4, 0.5, 0.6}, new double[][] {
                    {0.0, 0.3, 0.4, 0.1, 0.2, 0.2, 0.1},
                    {0.1, 0.4, 0.0, 0.3, 0.2, 0.2, 0.2},
                    {0.2, 0.2, 0.3, 0.0, 0.4, 0.1, 0.1},
                    {0.3, 0.1, 0.2, 0.4, 0.0, 0.4, 0.1},
                    {0.4, 0.2, 0.1, 0.1, 0.0, 0.3, 0.3}
                }, new RungeKuttaStepInterpolator(), 0.001, 0.1,
                new double[] {1.0e-10, 1.0e-10}, new double[] {1.0e-10, 1.0e-10});

        integrator.setSafety(0.9);
        integrator.setMinReduction(0.2);
        integrator.setMaxGrowth(10.0);

        StepHandler handler = new StepHandler() {
            @Override
            public void handleStep(StepHandler event, int last) {
                System.out.println("Step " + last + " accepted at " + integrator.getLastTime());
            }
        };
        integrator.setStepHandlers(handler);

        integrator.addEvent(new When(1.0, new Event() {
            @Override
            public void evaluateEvent(double t, double[] y, double[] yDot, double eventTime, int last) {
                System.out.println("Event at " + eventTime);
            }
        }));

        integrator.integrate(equations, t0, y0, t1, new double[y0.length], new double[y0.length],
                new double[] {}, new double[] {}, new double[] {}, new double[] {});

        assertEquals("Integration failed", y0.length, integrator.getNumberOfEquations());
        assertEquals("Integration failed", t1, integrator.getLastTime(), 1e-14);
        double[] y1 = integrator.getY();
        for (int i = 0; i < y0.length; i++) {
            assertEquals("Integration failed", y0[i], y1[i], 1e-14);
        }

        // check that the event has been triggered
        assertEquals("Event not triggered", 1, handler.getNumberOfSteps());
        assertEquals("Event not triggered at the right time", 1.0, handler.getSteps()[0].getTime(), 1e-14);

    }

    /**
     * Test method for {@link EmbeddedRungeKuttaIntegrator#integrate(FirstOrderDifferentialEquations, double, double[], double, double[], double[], double[], double[], double[], double[], double[])}.
     */
    public void testIntegrationWithEventsAndDenseOutput() {
        double[] y0 = new double[] {1.0, 1.0};
        double t0 = 0.0;
        double t1 = 1.0;

        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getNumberOfEquations() {
                return y0.length;
            }

            @Override
            public void derive(double t, double[] y, double[] yPrime) {
                for (int i = 0; i < y.length; i++) {
                    yPrime[i] = y[i] * (t + 1.0);
                }
            }
        };

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("name", false,
                new double[] {0.3, 0.4, 0.5, 0.6}, new double[][] {
                    {0.0, 0.3, 0.4, 0.1, 0.2, 0.2, 0.1},
                    {0.1, 0.4, 0.0, 0.3, 0.2, 0.2, 0.2},
                    {0.2, 0.2, 0.3, 0.0, 0.4, 0.1, 0.1},
                    {0.3, 0.1, 0.2, 0.4, 0.0, 0.4, 0.1},
                    {0.4, 0.2, 0.1, 0.1, 0.0, 0.3, 0.3}
                }, new RungeKuttaStepInterpolator(), 0.001, 0.1,
                new double[] {1.0e-10, 1.0e-10}, new double[] {1.0e-10, 1.0e-10},
                new double[] {1.0e-15, 1.0e-15}, new double[] {1.0e-15, 1.0e-15});

        integrator.setSafety(0.9);
        integrator.setMinReduction(0.2);
        integrator.setMaxGrowth(10.0);

        StepHandler handler = new StepHandler() {
            @Override
            public void handleStep(StepHandler event, int last) {
                System.out.println("Step " + last + " accepted at " + integrator.getLastTime());
            }
        };
        integrator.setStepHandlers(handler);

        integrator.addEvent(new When(1.0, new Event() {
            @Override
            public void evaluateEvent(double t, double[] y, double[] yDot, double eventTime, int last) {
                System.out.println("Event at " + eventTime);
            }
        }));

        integrator.integrate(equations, t0, y0, t1, new double[y0.length], null,
                new double[y0.length][], new double[y0.length][], new double[] {}, new double[] {}, null);

        assertEquals("Integration failed", y0.length, integrator.getNumberOfEquations());
        assertEquals("Integration failed", t1, integrator.getLastTime(), 1e-14);
        double[] y1 = integrator.getY();
        for (int i = 0; i < y0.length; i++) {
            assertEquals("Integration failed", y0[i], y1[i], 1e-14);
        }

        // check that the event has been triggered
        assertEquals("Event not triggered", 1, handler.getNumberOfSteps());
        assertEquals("Event not triggered at the right time", 1.0, handler.getSteps()[0].getTime(), 1e-14);

        // check that the dense output is correct
        double[][] output = integrator.getOutput();
        for (int i = 0; i < output.length; i++) {
            for (int j = 0; j < output[i].length; j++) {
                assertEquals("Integration failed, output[" + i + "][" + j + "]", y0[j], output[i][j], 1e-14);
            }
        }

    }

    /**
     * Test method for {@link EmbeddedRungeKuttaIntegrator#integrate(FirstOrderDifferentialEquations, double, double[], double, double[], double[], double[], double[], double[], double[], double[], double[])}.
     */
    public void testIntegrationWithSingularJacobian() {
        double[] y0 = new double[] {1.0, 1.0};
        double t0 = 0.0;
        double t1 = 1.0;

        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getNumberOfEquations() {
                return y0.length;
            }

            @Override
            public void derive(double t, double[] y, double[] yPrime) {
                double det = 0.0;
                for (int i = 0; i < y.length; i++) {
                    yPrime[i] = -y[i] * (t + 1.0);
                    det += y[i] * y[i];
                }
                assertEquals("Singular Jacobian", det != 0.0, true);
            }
        };

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("name", false,
                new double[] {0.3, 0.4, 0.5, 0.6}, new double[][] {
                    {0.0, 0.3, 0.4, 0.1, 0.2, 0.2, 0.1},
                    {0.1, 0.4, 0.0, 0.3, 0.2, 0.2, 0.2},
                    {0.2, 0.2, 0.3, 0.0, 0.4, 0.1, 0.1},
                    {0.3, 0.1, 0.2, 0.4, 0.0, 0.4, 0.1},
                    {0.4, 0.2, 0.1, 0.1, 0.0, 0.3, 0.3}
                }, new RungeKuttaStepInterpolator(), 0.001, 0.1,
                new double[] {1.0e-10, 1.0e-10}, new double[] {1.0e-10, 1.0e-10});

        integrator.setSafety(0.9);
        integrator.setMinReduction(0.2);
        integrator.setMaxGrowth(10.0);

        try {
            integrator.integrate(equations, t0, y0, t1, new double[y0.length], new double[y0.length],
                    new double[] {}, new double[] {}, new double[] {}, new double[] {}, new double[] {}, null);
            fail("Integration should have failed due to singular jacobian");
        } catch (IllegalArgumentException e) {
            // expected
        }

    }

}