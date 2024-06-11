package org.apache.commons.math3.ode.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.ode.events.AbstractEvent;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.EventRate;
import org.apache.commons.math3.ode.events.When;
import org.apache.commons.math3.ode.events.interfaces.StepHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.apache.commons.math3.ode.test.AbstractODETest;
import org.apache.commons.math3.ode.util.ExpandableStatefulODE;
import org.apache.commons.math3.ode.util.UnmodifiableODE;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

public class AbstractIntegratorTest extends AbstractODETest {

    private static class TestStepHandler implements StepHandler {
        private boolean called = false;

        @Override
        public void handleStep(StepInterpolator interpolator, boolean lastStep) {
            called = true;
        }

        public boolean wasCalled() {
            return called;
        }
    }

    @Test
    public void testIntegration() {
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[0];
            }

            @Override
            public int getDimension() {
                return 1;
            }
        };

        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator") {
            @Override
            public void integrate(ExpandableStatefulODE p0, double p1) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
                // do nothing
            }
        };

        integrator.addStepHandler(new TestStepHandler());

        double[] y0 = new double[]{1.0};
        double tEnd = 10.0;

        try {
            integrator.integrate(equations, 0.0, y0, tEnd, new double[y0.length]);
            assertTrue(integrator.getStepHandlers().get(0).wasCalled());
        } catch (Exception e) {
            fail("Integration should not have thrown an exception: " + e.getMessage());
        }
    }

    @Test(expected = DimensionMismatchException.class)
    public void testDimensionMismatch() {
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[0];
                yDot[1] = y[1]; // extra dimension
            }

            @Override
            public int getDimension() {
                return 1;
            }
        };

        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator") {
            @Override
            public void integrate(ExpandableStatefulODE p0, double p1) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
                // do nothing
            }
        };

        integrator.addStepHandler(new TestStepHandler());

        double[] y0 = new double[]{1.0, 2.0};
        double tEnd = 10.0;

        integrator.integrate(equations, 0.0, y0, tEnd, new double[y0.length]);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testMaxCountExceeded() {
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                // this system has no solution
            }

            @Override
            public int getDimension() {
                return 1;
            }
        };

        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator") {
            @Override
            public void integrate(ExpandableStatefulODE p0, double p1) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
                // do nothing
            }
        };

        integrator.setMaxEvaluations(1);
        integrator.addStepHandler(new TestStepHandler());

        double[] y0 = new double[]{1.0};
        double tEnd = 10.0;

        integrator.integrate(equations, 0.0, y0, tEnd, new double[y0.length]);
    }

    @Test
    public void testEventHandling() {
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[0];
            }

            @Override
            public int getDimension() {
                return 1;
            }
        };

        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator") {
            @Override
            public void integrate(ExpandableStatefulODE p0, double p1) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
                // do nothing
            }
        };

        integrator.addStepHandler(new TestStepHandler());

        Event event = new When(1.0) {
            @Override
            public double eventTime() {
                return 1.0;
            }
        };

        integrator.addEventHandler(event, 0.0, 0.0, 1);

        double[] y0 = new double[]{1.0};
        double tEnd = 10.0;

        try {
            integrator.integrate(equations, 0.0, y0, tEnd, new double[y0.length]);
            fail("An event should have been triggered");
        } catch (Exception e) {
            // expected
        }

        assertTrue(integrator.getStepHandlers().get(0).wasCalled());
    }

    @Test
    public void testEventHandlingWithMultipleEvents() {
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[0];
            }

            @Override
            public int getDimension() {
                return 1;
            }
        };

        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator") {
            @Override
            public void integrate(ExpandableStatefulODE p0, double p1) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
                // do nothing
            }
        };

        integrator.addStepHandler(new TestStepHandler());

        Event event1 = new When(1.0) {
            @Override
            public double eventTime() {
                return 1.0;
            }
        };
        Event event2 = new When(2.0) {
            @Override
            public double eventTime() {
                return 2.0;
            }
        };

        integrator.addEventHandler(event1, 0.0, 0.0, 1);
        integrator.addEventHandler(event2, 0.0, 0.0, 1);

        double[] y0 = new double[]{1.0};
        double tEnd = 10.0;

        try {
            integrator.integrate(equations, 0.0, y0, tEnd, new double[y0.length]);
            fail("Both events should have been triggered");
        } catch (Exception e) {
            // expected
        }

        assertTrue(integrator.getStepHandlers().get(0).wasCalled());
        assertTrue(integrator.getEventHandlerNames().containsAll(Arrays.asList("When(1.0)", "When(2.0)")));
    }

    @Test
    public void testEventHandlingWithRate() {
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[0];
            }

            @Override
            public int getDimension() {
                return 1;
            }
        };

        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator") {
            @Override
            public void integrate(ExpandableStatefulODE p0, double p1) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
                // do nothing
            }
        };

        integrator.addStepHandler(new TestStepHandler());

        Event event = new When(1.0) {
            @Override
            public double eventTime() {
                return 1.0;
            }

            @Override
            public EventRate getRate() {
                return new EventRate() {
                    @Override
                    public double rate(double t, double[] y, double[] yDot) {
                        return 1.0;
                    }
                };
            }
        };

        integrator.addEventHandler(event, 0.0, 0.0, 1);

        double[] y0 = new double[]{1.0};
        double tEnd = 10.0;

        try {
            integrator.integrate(equations, 0.0, y0, tEnd, new double[y0.length]);
            fail("An event should have been triggered multiple times");
        } catch (Exception e) {
            // expected
        }

        assertTrue(integrator.getStepHandlers().get(0).wasCalled());
        assertTrue(integrator.getEventHandlerNames().contains("When(1.0)"));
        assertEquals(2, integrator.getEventCount());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEventHandlingWithNegativeRate() {
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[0];
            }

            @Override
            public int getDimension() {
                return 1;
            }
        };

        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator") {
            @Override
            public void integrate(ExpandableStatefulODE p0, double p1) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
                // do nothing
            }
        };

        integrator.addStepHandler(new TestStepHandler());

        Event event = new When(1.0) {
            @Override
            public double eventTime() {
                return 1.0;
            }

            @Override
            public EventRate getRate() {
                return new EventRate() {
                    @Override
                    public double rate(double t, double[] y, double[] yDot) {
                        return -1.0; // negative rate
                    }
                };
            }
        };

        integrator.addEventHandler(event, 0.0, 0.0, 1);

        double[] y0 = new double[]{1.0};
        double tEnd = 10.0;

        integrator.integrate(equations, 0.0, y0, tEnd, new double[y0.length]);
    }

    @Test(expected = NoBracketingException.class)
    public void testNoBracketingException() {
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = -y[0];
            }

            @Override
            public int getDimension() {
                return 1;
            }
        };

        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator") {
            @Override
            public void integrate(ExpandableStatefulODE p0, double p1) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
                // do nothing
            }
        };

        integrator.addStepHandler(new TestStepHandler());

        Event event = new When(1.0) {
            @Override
            public double eventTime() {
                return 1.0;
            }
        };

        integrator.addEventHandler(event, 0.0, 0.0, 1);

        double[] y0 = new double[]{1.0};
        double tEnd = 0.0;

        integrator.integrate(equations, 0.0, y0, tEnd, new double[y0.length]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNoBracketingExceptionWithInvalidEvent() {
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[0];
            }

            @Override
            public int getDimension() {
                return 1;
            }
        };

        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator") {
            @Override
            public void integrate(ExpandableStatefulODE p0, double p1) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
                // do nothing
            }
        };

        integrator.addStepHandler(new TestStepHandler());

        Event event = new When(Double.NaN) {
            @Override
            public double eventTime() {
                return Double.NaN;
            }
        };

        integrator.addEventHandler(event, 0.0, 0.0, 1);

        double[] y0 = new double[]{1.0};
        double tEnd = 10.0;

        integrator.integrate(equations, 0.0, y0, tEnd, new double[y0.length]);
    }
}