package org.apache.commons.math3.ode.test;

import static org.junit.Assert.*;
import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.ode.events.*;
import org.apache.commons.math3.ode.sampling.*;
import org.apache.commons.math3.analysis.*;
import org.apache.commons.math3.analysis.interpolation.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.ode.AbstractIntegrator;
import org.junit.Test;

public class AbstractIntegratorTest {

    @Test
    public void testAbstractIntegrator() throws NumberIsTooSmallException, NoBracketingException, DimensionMismatchException, MaxCountExceededException {
        // Test empty integrator
        AbstractIntegrator integrator = new AbstractIntegrator("EmptyIntegrator") {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
                // Do nothing
            }
        };

        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = Math.sin(y[0]);
            }
        };

        double[] y0 = new double[]{0.0};
        double tEnd = 10.0;

        integrator.initIntegration(0.0, y0, tEnd);
        integrator.setExpandable(equations);

        double y[] = new double[1];
        integrator.integrate(equations, 0.0, y0, tEnd, y);

        assertEquals(10.0, integrator.getCurrentStepStart(), 1e-9);
        assertEquals(0.0, integrator.getCurrentSignedStepsize(), 1e-9);
        assertEquals(equations.getDimension(), integrator.getEvaluations(), 1);

        // Test integrator with step handlers
        integrator = new AbstractIntegrator("StepIntegrator") {
            private int stepCount = 0;

            @Override
            public void handleStep(StepInterpolator interpolator, boolean isLastStep) {
                stepCount++;
            }

            @Override
            public void integrate(ExpandableStatefulODE equations, double t) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
                // Do nothing
            }
        };

        integrator.clearEventHandlers();
        integrator.addStepHandler(new StepHandler() {
            @Override
            public void init(double t, double[] y, double tStep) {
                // Do nothing
            }

            @Override
            public void handleStep(StepInterpolator interpolator, boolean isLastStep) {
                if (isLastStep) {
                    fail("Should not call handleStep on last step");
                }
            }
        });
        integrator.addStepHandler(new StepHandler() {
            @Override
            public void init(double t, double[] y, double tStep) {
                assertEquals(0.0, integrator.getCurrentStepStart(), 1e-9);
                assertEquals(0.0, integrator.getCurrentSignedStepsize(), 1e-9);
                assertEquals(integrator.getEvaluations(), stepCount, 1);

                stepCount = 0;
            }

            @Override
            public void handleStep(StepInterpolator interpolator, boolean isLastStep) {
                if (isLastStep) {
                    assertTrue(integrator.getEvaluations() > stepCount);
                }
                stepCount++;
            }
        });

        integrator.initIntegration(0.0, y0, tEnd);
        integrator.setExpandable(equations);

        integrator.integrate(equations, 0.0, y0, tEnd, y);

        assertEquals(10.0, integrator.getCurrentStepStart(), 1e-9);
        assertEquals(0.0, integrator.getCurrentSignedStepsize(), 1e-9);
        assertEquals(equations.getDimension(), integrator.getEvaluations(), 1);
        assertEquals(integrator.getEvaluations(), stepCount, 1);

        // Test integrator with event handlers
        integrator = new AbstractIntegrator("EventIntegrator") {
            private int eventCount = 0;

            @Override
            public void handleStep(StepInterpolator interpolator, boolean isLastStep) {
                if (isLastStep) {
                    fail("Should not call handleStep on last step");
                }
            }

            @Override
            public void integrate(ExpandableStatefulODE equations, double t) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
                // Do nothing
            }
        };

        integrator.clearEventHandlers();
        integrator.addEventHandler(new AbstractEvent() {
            @Override
            public void init(double t, double[] y, double tStep) {
                eventCount++;
            }

            @Override
            public void evaluateStep(StepInterpolator interpolator) {
                return eventCount > 0;
            }

            @Override
            public double getEventTime() {
                return 5.0;
            }
        }, 1.0, 1.0e-6, 10);

        integrator.initIntegration(0.0, y0, tEnd);
        integrator.setExpandable(equations);

        integrator.integrate(equations, 0.0, y0, tEnd, y);

        assertEquals(10.0, integrator.getCurrentStepStart(), 1e-9);
        assertEquals(0.0, integrator.getCurrentSignedStepsize(), 1e-9);
        assertEquals(equations.getDimension(), integrator.getEvaluations(), 1);
        assertEquals(1, integrator.getEventHandlers().size());
        assertTrue(integrator.isLastStep());
        assertEquals(1, eventCount);
    }
}