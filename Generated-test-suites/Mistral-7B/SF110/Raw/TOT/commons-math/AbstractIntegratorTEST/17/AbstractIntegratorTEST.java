package org.apache.commons.math3.ode.test;

import org.apache.commons.math3.ode.AbstractIntegrator;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.EventRate;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.sampling.AbstractStepInterpolator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.sampling.UnmodifiableStepHandlerList;
import org.apache.commons.math3.ode.util.ExpandableStatefulODE;
import org.junit.Test;
import static org.junit.Assert.*;

public class AbstractIntegratorTest {

    @Test
    public void testIntegrator() {
        final String integratorName = "MyIntegrator";
        AbstractIntegrator integrator = new MyIntegrator(integratorName);
        integrator.addStepHandler(event -> {
            System.out.println("Step handler 1 was called.");
            return StepContinuation.CONTINUE;
        });
        integrator.addEventHandler(event -> {
            System.out.println("EventHandler 1 was called.");
            return new EventState(event, 0.1, 1.0, 5, new BracketingNthOrderBrentSolver(1.0, 5));
        }, 0.1, 1.0, 5, new BracketingNthOrderBrentSolver(1.0, 5));

        FirstOrderDifferentialEquations equations = new RK45DifferentialEquations();
        double initialTime = 0.0;
        double[] initialState = {0.0, 1.0};
        double finalTime = 10.0;

        try {
            double[] outputState = integrator.integrate(equations, initialTime, initialState, finalTime, new double[equations.getDimension()]);
            assertEquals(finalTime, integrator.getCurrentStepStart(), 1e-10);
            assertArrayEquals(new double[]{2.718281828459045, 2.718281828459045}, outputState, 1e-8);
            assertEquals(5, integrator.getEvaluations());
            for (StepHandler handler : integrator.getStepHandlers()) {
                assertEquals("Step handler 1", handler.getClass().getName());
            }
            assertTrue(integrator.isLastStep());
        } catch (Exception e) {
            fail("An exception should not have been thrown: " + e.getMessage());
        }
    }

    @Test
    public void testIntegrator_dimensionMismatch() {
        final String integratorName = "MyIntegrator";
        AbstractIntegrator integrator = new MyIntegrator(integratorName);

        FirstOrderDifferentialEquations equations = new RK45DifferentialEquations();
        double initialTime = 0.0;
        double[] initialState = {0.0, 1.0, 2.0}; // wrong dimension

        try {
            integrator.integrate(equations, initialTime, initialState, 1.0, new double[equations.getDimension()-1]);
            fail("An exception should have been thrown: dimension mismatch");
        } catch (DimensionMismatchException e) {
            // expected
        }
    }

    @Test
    public void testIntegrator_numberIsTooSmall() {
        final String integratorName = "MyIntegrator";
        AbstractIntegrator integrator = new MyIntegrator(integratorName);

        FirstOrderDifferentialEquations equations = new RK45DifferentialEquations();
        double initialTime = 0.0;
        double[] initialState = {0.0, 1.0};
        double finalTime = 1e-10; // too small

        try {
            integrator.integrate(equations, initialTime, initialState, finalTime, new double[equations.getDimension()]);
            fail("An exception should have been thrown: number is too small");
        } catch (NumberIsTooSmallException e) {
            // expected
        }
    }

    @Test
    public void testIntegrator_maxCountExceeded() {
        final String integratorName = "MyIntegrator";
        AbstractIntegrator integrator = new MyIntegrator(integratorName);
        integrator.setMaxEvaluations(10); // less than max

        FirstOrderDifferentialEquations equations = new RK45DifferentialEquations();
        double initialTime = 0.0;
        double[] initialState = {0.0, 1.0};
        double finalTime = 10.0;

        try {
            integrator.integrate(equations, initialTime, initialState, finalTime, new double[equations.getDimension()]);
            fail("An exception should have been thrown: maximum number of evaluations exceeded");
        } catch (MaxCountExceededException e) {
            // expected
        }
    }

    @Test
    public void testIntegrator_noBracketing() {
        final String integratorName = "MyIntegrator";
        AbstractIntegrator integrator = new MyIntegrator(integratorName);

        FirstOrderDifferentialEquations equations = new MyCustomDifferentialEquations(); // equations with no bracketing
        double initialTime = 0.0;
        double[] initialState = {0.0, 1.0};
        double finalTime = 1.0;

        try {
            integrator.integrate(equations, initialTime, initialState, finalTime, new double[equations.getDimension()]);
            fail("An exception should have been thrown: no bracketing");
        } catch (NoBracketingException e) {
            // expected
        }
    }

    static class MyIntegrator extends AbstractIntegrator {
        public MyIntegrator(String name) {
            super(name);
        }

        @Override
        public void integrate(ExpandableStatefulODE p0, double p1) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
            // implementation of the integrate method for testing purposes
        }
    }

    static class MyCustomDifferentialEquations implements FirstOrderDifferentialEquations {
        @Override
        public int getDimension() {
            return 2;
        }

        @Override
        public void computeDerivatives(double t, double[] y, double[] yDot) {
            yDot[0] = y[1];
            yDot[1] = -y[0];
        }
    }

    static class RK45DifferentialEquations implements FirstOrderDifferentialEquations {
        @Override
        public int getDimension() {
            return 2;
        }

        @Override
        public void computeDerivatives(double t, double[] y, double[] yDot) {
            yDot[0] = y[1];
            yDot[1] = -y[0];
        }
    }

    static class MyEventHandler implements EventHandler {
        @Override
        public void handleStep(double t, double[] y) {
            // implementation of the handleStep method for testing purposes
        }

        @Override
        public void init(double t0, double[] y0, double t) {
            // implementation of the init method for testing purposes
        }

        @Override
        public double getEventTime() {
            return 2.0;
        }
    }

    static class MyStepHandler implements StepHandler {
        @Override
        public StepContinuation handleStep(AbstractStepInterpolator interpolator, boolean lastStep) {
            // implementation of the handleStep method for testing purposes
            return StepContinuation.CONTINUE;
        }

        @Override
        public void init(double t0, double[] y0, double t) {
            // implementation of the init method for testing purposes
        }
    }
}