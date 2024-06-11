package org.apache.commons.math.ode.nonstiff;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.When;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaEventStepHandler;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;

/**
 * Tests for {@link EmbeddedRungeKuttaIntegrator}.
 */
public class EmbeddedRungeKuttaIntegratorTest extends TestCase {

    /**
     * A simple test for {@link EmbeddedRungeKuttaIntegrator} with no events.
     */
    public void testSimpleIntegration() {
        double[][] c = { { 0.0, 0.5, 0.5, 0.5 }, { 0.0, 0.25, 0.25, 0.25 } };
        double[] a = { 0.0, 0.5, 0.5, 0.5 };
        double[] b = { 1.0 / 3.0, 1.0 / 4.0, 1.0 / 3.0, 1.0 / 6.0 };
        EmbeddedRungeKuttaIntegrator rk4 = new RungeKutta4Integrator(
                "RK4", false, c, a, b, new RungeKuttaStepInterpolator(), 1e-12, 1e-12);

        MultivariateFunction equations = new MultivariateFunction() {
            public double value(double[] arg0) {
                double x = arg0[0];
                double y = arg0[1];
                double dxdt = 2.0 * x + y;
                double dydt = -x;
                return new double[] { dxdt, dydt };
            }
        };

        double[] y0 = { 1.0, 1.0 };
        double t0 = 0.0;
        double t1 = 2.0;

        double result = rk4.integrate(equations, t0, y0, t1, new double[2]);
        double expected = 3.0;
        assertEquals(expected, result, 1e-12);
    }

    /**
     * A test for {@link EmbeddedRungeKuttaIntegrator} with an event.
     */
    public void testIntegrationWithEvent() {
        double[][] c = { { 0.0, 0.5, 0.5, 0.5 }, { 0.0, 0.25, 0.25, 0.25 } };
        double[] a = { 0.0, 0.5, 0.5, 0.5 };
        double[] b = { 1.0 / 3.0, 1.0 / 4.0, 1.0 / 3.0, 1.0 / 6.0 };
        EmbeddedRungeKuttaIntegrator rk4 = new RungeKutta4Integrator(
                "RK4", false, c, a, b, new RungeKuttaStepInterpolator(), 1e-12, 1e-12);

        MultivariateFunction equations = new MultivariateFunction() {
            public double value(double[] arg0) {
                double x = arg0[0];
                double y = arg0[1];
                double dxdt = 2.0 * x + y;
                double dydt = -x;
                return new double[] { dxdt, dydt };
            }
        };

        StepHandler eventHandler = new RungeKuttaEventStepHandler() {
            public void handleStep(final double time, final double[] y) {
                if (y[0] > 2.0) {
                    System.out.println("Event detected: " + time + ", " + java.util.Arrays.toString(y));
                }
            }
        };

        double[] y0 = { 1.0, 1.0 };
        double t0 = 0.0;
        double t1 = 3.0;

        CombinedEventsManager manager = new CombinedEventsManager();
        manager.addEvent(new When(new Event() {
            public boolean check(double time, double[] y) {
                return y[0] > 2.0;
            }
        }, 2.0));

        rk4.setEventsHandlersManager(manager);
        rk4.setStepHandlers(new StepHandler[] { eventHandler });

        double result = rk4.integrate(equations, t0, y0, t1, new double[2]);
        double expected = 3.0;
        assertEquals(expected, result, 1e-12);
        assertTrue(manager.isEventDetected());
    }

    /**
     * Create a suite of tests.
     *
     * @return the test suite.
     */
    public static TestSuite suite() {
        TestSuite suite = new TestSuite();
        suite.addTest(new EmbeddedRungeKuttaIntegratorTest("testSimpleIntegration"));
        suite.addTest(new EmbeddedRungeKuttaIntegratorTest("testIntegrationWithEvent"));
        return suite;
    }

    /**
     * Constructor.
     *
     * @param name name of the test.
     */
    public EmbeddedRungeKuttaIntegratorTest(String name) {
        super(name);
    }

}