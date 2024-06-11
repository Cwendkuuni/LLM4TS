package org.apache.commons.math3.ode.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.ode.events.*;
import org.apache.commons.math3.ode.sampling.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.ode.AbstractIntegrator;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.StepHandler;
import org.apache.commons.math3.ode.event.*;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.junit.Test;

public class AbstractIntegratorTest {

    @Test
    public void testIntegration() throws NumberIsTooSmallException, DimensionMismatchException, NoBracketingException, MaxCountExceededException {
        // Test integration with no events or step handlers
        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator") {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
                // Custom implementation of the integrate method for testing
            }
        };
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = Math.sin(y[0]);
            }
        };
        double[] y0 = {0.0};
        double tEnd = 10.0;
        double[] y = new double[1];

        integrator.initIntegration(0.0, y0, tEnd);
        integrator.integrate(equations, tEnd);
        assertEquals(Math.sin(1.0), y[0], 1e-6); // Check the final value is correct

        // Test integration with events
        integrator = new AbstractIntegrator("TestIntegrator") {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
                // Custom implementation of the integrate method for testing
            }

            @Override
            public void addEventHandler(EventHandler handler, double maxCheckInterval, double convergence, int maxIterationCount) {
                super.addEventHandler(handler, maxCheckInterval, convergence, maxIterationCount);
            }
        };

        integrator.addEventHandler(new AbstractEvent() {
            @Override
            public double getEventTime() {
                return 5.0;
            }

            @Override
            public boolean evaluatesToActive(ExpandableStatefulODE equations, double t, double[] y) {
                return y[0] > 2.0;
            }

            @Override
            public void handleEvent(double t, double[] y, double[] yDot) {
                yDot[0] += 3.0;
            }
        }, 0.1, 1e-6, 10);

        integrator.initIntegration(0.0, y0, tEnd);
        integrator.integrate(equations, tEnd);
        assertEquals(Math.sin(1.0) + 3.0, y[0], 1e-6); // Check the final value is correct after the event

        // Test integration with step handlers
        integrator = new AbstractIntegrator("TestIntegrator") {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
                // Custom implementation of the integrate method for testing
            }

            @Override
            public void addStepHandler(StepHandler handler) {
                super.addStepHandler(handler);
            }
        };

        integrator.addStepHandler(new StepHandler() {
            @Override
            public void handleStep(StepInterpolator interpolator, boolean isLastStep) {
                if (isLastStep) {
                    interpolator.setGlobalPreviousTime(interpolator.getGlobalCurrentTime());
                    interpolator.setGlobalCurrentTime(interpolator.getGlobalCurrentTime() + 0.1);
                    System.out.println("Last step");
                }
            }
        });

        integrator.initIntegration(0.0, y0, tEnd);
        integrator.integrate(equations, tEnd);
        System.out.println(integrator.getEvaluations()); // Check the number of evaluations is as expected
    }
}