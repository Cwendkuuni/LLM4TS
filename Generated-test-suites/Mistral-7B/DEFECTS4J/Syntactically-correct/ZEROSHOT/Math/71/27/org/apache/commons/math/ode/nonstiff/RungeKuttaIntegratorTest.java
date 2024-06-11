package org.apache.commons.math.ode.nonstiff.test;

import org.apache.commons.math3.exception.MaxIterException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NumberFormatException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.When;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaIntegrator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for RungeKuttaIntegrator.
 */
public class RungeKuttaIntegratorTest {

    /**
     * Test method for {@link RungeKuttaIntegrator#RungeKuttaIntegrator(String, double[], double[][], double[], RungeKuttaStepInterpolator, double)}.
     * @throws Exception if an error occurs
     */
    @Test
    public void testRungeKuttaIntegrator() throws Exception {
        double[] c = {0.0, 0.5, 0.5, 1.0};
        double[][] a = {{0.0, 0.0, 0.0, 1.0}, {0.5, 0.5, 1.0, 1.5}, {0.5, 1.0, 1.5, 2.0}, {1.0, 1.0, 2.0, 2.5}};
        double[] b = {0.3333333333333333, 0.4, 0.6666666666666667, 1.0};
        RungeKuttaIntegrator integrator = new RungeKuttaIntegrator("RK4", c, a, b, new RungeKuttaStepInterpolator(), 0.01) {
            @Override
            protected void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[1];
                yDot[1] = -0.01 * y[0];
            }
        };
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 2;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                integrator.computeDerivatives(t, y, yDot);
            }
        };
        double[] initialValues = {1.0, 2.5};
        double startTime = 0.0;
        double endTime = 10.0;
        double stepSize = 0.01;

        // test integration
        double result = integrator.integrate(equations, startTime, initialValues, endTime, new double[2]);
        Assert.assertEquals(endTime, result, 1e-10);

        // test integration with events
        double[] y = new double[2];
        StepHandler eventHandler = new StepHandler() {
            private int eventCount = 0;

            @Override
            public void handleStep(StepHandler event, int stepNumber) {
                if (event instanceof When) {
                    When when = (When) event;
                    Assert.assertEquals(3.0, when.getEventTime(), 1e-10);
                    eventCount++;
                }
            }
        };
        integrator.addStepHandler(eventHandler);
        integrator.setEventsHandlerManager(new CombinedEventsManager() {
            @Override
            public Event[] getEvents(double t, double[] y) {
                return new Event[] {new When(3.0)};
            }
        });
        result = integrator.integrate(equations, startTime, initialValues, endTime, y);
        Assert.assertEquals(endTime, result, 1e-10);
        Assert.assertEquals(1, eventCount);

        // test integration with non-default step size
        integrator.setStepSize(0.02);
        result = integrator.integrate(equations, startTime, initialValues, endTime, y);
        Assert.assertEquals(endTime, result, 1e-10);

        // test integration with error conditions
        integrator.setEventsHandlerManager(new CombinedEventsManager() {
            @Override
            public Event[] getEvents(double t, double[] y) {
                throw new NumberFormatException();
            }
        });
        try {
            integrator.integrate(equations, startTime, initialValues, endTime, y);
            Assert.fail();
        } catch (Exception e) {
            // expected
        }
        integrator.setEventsHandlerManager(new CombinedEventsManager() {
            @Override
            public Event[] getEvents(double t, double[] y) {
                throw new MathIllegalArgumentException();
            }
        });
        try {
            integrator.integrate(equations, startTime, initialValues, endTime, y);
            Assert.fail();
        } catch (Exception e) {
            // expected
        }
        integrator.setEventsHandlerManager(new CombinedEventsManager() {
            @Override
            public Event[] getEvents(double t, double[] y) {
                throw new MaxIterException();
            }
        });
        try {
            integrator.integrate(equations, startTime, initialValues, endTime, y);
            Assert.fail();
        } catch (Exception e) {
            // expected
        }
    }

}

class RungeKuttaStepInterpolator extends AbstractStepInterpolator {

    @Override
    public double interpolate(double t) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void storeTime(double t) {
        // empty
    }

}