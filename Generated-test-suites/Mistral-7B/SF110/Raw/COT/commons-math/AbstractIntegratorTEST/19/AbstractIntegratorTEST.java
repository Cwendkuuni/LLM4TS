package org.apache.commons.math3.ode.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.analysis.*;
import org.apache.commons.math3.analysis.interpolation.*;
import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.ode.events.*;
import org.apache.commons.math3.ode.sampling.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.util.*;
import org.junit.Test;

public class AbstractIntegratorTest {

    @Test
    public void testAbstractIntegratorConstruction() {
        AbstractIntegrator integrator = new AbstractIntegrator("Integrator1");
        assertEquals("Integrator1", integrator.getName());
    }

    @Test
    public void testAbstractIntegratorAddStepHandler() {
        AbstractIntegrator integrator = new AbstractIntegrator("Integrator1");
        StepHandler mockHandler = mock(StepHandler.class);
        integrator.addStepHandler(mockHandler);
        assertEquals(1, integrator.getStepHandlers().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAbstractIntegratorAddNullStepHandler() {
        AbstractIntegrator integrator = new AbstractIntegrator("Integrator1");
        integrator.addStepHandler(null);
    }

    @Test
    public void testAbstractIntegratorClearStepHandlers() {
        AbstractIntegrator integrator = new AbstractIntegrator("Integrator1");
        StepHandler mockHandler1 = mock(StepHandler.class);
        integrator.addStepHandler(mockHandler1);
        StepHandler mockHandler2 = mock(StepHandler.class);
        integrator.addStepHandler(mockHandler2);
        integrator.clearStepHandlers();
        assertEquals(0, integrator.getStepHandlers().size());
    }

    @Test
    public void testAbstractIntegratorAddEventHandler() {
        AbstractIntegrator integrator = new AbstractIntegrator("Integrator1");
        EventHandler mockEventHandler = mock(EventHandler.class);
        integrator.addEventHandler(mockEventHandler, 1.0, 1.0e-9, 10);
        assertEquals(1, integrator.getEventHandlers().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAbstractIntegratorAddNullEventHandler() {
        AbstractIntegrator integrator = new AbstractIntegrator("Integrator1");
        integrator.addEventHandler(null, 1.0, 1.0e-9, 10);
    }

    @Test
    public void testAbstractIntegratorClearEventHandlers() {
        AbstractIntegrator integrator = new AbstractIntegrator("Integrator1");
        EventHandler mockEventHandler1 = mock(EventHandler.class);
        integrator.addEventHandler(mockEventHandler1, 1.0, 1.0e-9, 10);
        EventHandler mockEventHandler2 = mock(EventHandler.class);
        integrator.addEventHandler(mockEventHandler2, 1.0, 1.0e-9, 10);
        integrator.clearEventHandlers();
        assertEquals(0, integrator.getEventHandlers().size());
    }

    @Test
    public void testAbstractIntegratorGetCurrentStepStart() {
        AbstractIntegrator integrator = new AbstractIntegrator("Integrator1");
        assertEquals(Double.NaN, integrator.getCurrentStepStart(), 0.0);
    }

    @Test
    public void testAbstractIntegratorSetMaxEvaluations() {
        AbstractIntegrator integrator = new AbstractIntegrator("Integrator1");
        integrator.setMaxEvaluations(10);
        assertEquals(10, integrator.getMaxEvaluations());
    }

    @Test
    public void testAbstractIntegratorGetMaxEvaluations() {
        AbstractIntegrator integrator = new AbstractIntegrator("Integrator1");
        integrator.setMaxEvaluations(-1);
        assertEquals(-1, integrator.getMaxEvaluations());
    }

    @Test
    public void testAbstractIntegratorGetEvaluations() {
        AbstractIntegrator integrator = new AbstractIntegrator("Integrator1");
        integrator.setMaxEvaluations(5);
        integrator.evaluate(new DifferentialEquations() {
            public double evaluateDerivative(double t, double[] y) {
                return 0.0;
            }

            public int getDimension() {
                return 1;
            }
        }, 0.0, new double[]{0.0}, 1.0, new double[1]);
        assertEquals(1, integrator.getEvaluations());
    }

    @Test
    public void testAbstractIntegratorIntegrate() throws Exception {
        AbstractIntegrator integrator = new AbstractIntegrator("Integrator1");
        EquationsDifferentialEquations equations = new EquationsDifferentialEquations() {
            public double evaluateDerivative(double t, double[] y) {
                return y[0];
            }

            public int getDimension() {
                return 1;
            }
        };
        double initialTime = 0.0;
        double[] initialState = new double[]{1.0};
        double finalTime = 1.0;
        double[] finalState = new double[]{2.0};
        integrator.integrate(equations, initialTime, initialState, finalTime, finalState);
        assertEquals(finalTime, integrator.getCurrentStepStart(), 0.0);
        assertEquals(Arrays.toString(finalState), Arrays.toString(finalState), 0.0);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testAbstractIntegratorIntegrateDimensionMismatch() throws Exception {
        AbstractIntegrator integrator = new AbstractIntegrator("Integrator1");
        EquationsDifferentialEquations equations = new EquationsDifferentialEquations() {
            public double evaluateDerivative(double t, double[] y) {
                return y[0];
            }

            public int getDimension() {
                return 2;
            }
        };
        double initialTime = 0.0;
        double[] initialState = new double[]{1.0};
        double finalTime = 1.0;
        double[] finalState = new double[]{2.0};
        integrator.integrate(equations, initialTime, initialState, finalTime, finalState);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testAbstractIntegratorIntegrateNumberIsTooSmall() throws Exception {
        AbstractIntegrator integrator = new AbstractIntegrator("Integrator1");
        EquationsDifferentialEquations equations = new EquationsDifferentialEquations() {
            public double evaluateDerivative(double t, double[] y) {
                return y[0];
            }

            public int getDimension() {
                return 1;
            }
        };
        double initialTime = 0.0;
        double[] initialState = new double[]{1.0};
        double finalTime = 1.0e-10;
        double[] finalState = new double[]{2.0};
        integrator.integrate(equations, initialTime, initialState, finalTime, finalState);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testAbstractIntegratorIntegrateMaxCountExceeded() throws Exception {
        AbstractIntegrator integrator = new AbstractIntegrator("Integrator1");
        integrator.setMaxEvaluations(0);
        EquationsDifferentialEquations equations = new EquationsDifferentialEquations() {
            public double evaluateDerivative(double t, double[] y) {
                return y[0];
            }

            public int getDimension() {
                return 1;
            }
        };
        double initialTime = 0.0;
        double[] initialState = new double[]{1.0};
        double finalTime = 1.0;
        double[] finalState = new double[]{2.0};
        integrator.integrate(equations, initialTime, initialState, finalTime, finalState);
    }

    @Test(expected = NoBracketingException.class)
    public void testAbstractIntegratorIntegrateNoBracketing() throws Exception {
        AbstractIntegrator integrator = new AbstractIntegrator("Integrator1");
        StepInterpolator interpolator = new StepInterpolator(new PolynomialInterpolator(1));
        ExpandableStatefulODE expandableODE = new ExpandableStatefulODE(new EquationsDifferentialEquations() {
            public double evaluateDerivative(double t, double[] y) {
                return y[0];
            }

            public int getDimension() {
                return 1;
            }
        });
        integrator.initIntegration(0.0, new double[]{1.0}, 1.0);
        integrator.integrate(expandableODE, 1.0);
    }
}