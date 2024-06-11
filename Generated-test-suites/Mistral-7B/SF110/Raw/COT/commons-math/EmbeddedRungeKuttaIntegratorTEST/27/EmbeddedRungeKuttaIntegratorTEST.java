package org.apache.commons.math3.ode.nonstiff.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.ode.sampling.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.util.*;
import org.junit.Test;

import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegratorFactory;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testGetOrder() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbedgedRungeKutta25(); // a sample embedded Runge-Kutta integrator
        int expectedOrder = 25;
        assertEquals("Incorrect order", expectedOrder, integrator.getOrder());
    }

    @Test
    public void testGetSafety() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKutta25(); // a sample embedded Runge-Kutta integrator
        double expectedSafety = 0.9;
        assertEquals("Incorrect safety factor", expectedSafety, integrator.getSafety(), 0.001);
    }

    @Test
    public void testIntegrate_ValidInput() {
        double[] initialState = {0.0, 1.0};
        double time = 10.0;

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKutta25(); // a sample embedded Runge-Kutta integrator
        ExpandableStatefulODE equations = new DummyEquations(initialState);

        integrator.integrate(equations, time);

        // You can add an assertion here to check the final state
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testIntegrate_StepSizeTooSmall() {
        double[] initialState = {0.0, 1.0};
        double time = 10.0;
        double minStep = 0.0001; // very small step size

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKutta25(); // a sample embedded Runge-Kutta integrator
        ExpandableStatefulODE equations = new DummyEquations(initialState);

        integrator.setMinStep(minStep);
        integrator.integrate(equations, time);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testIntegrate_MismatchedDimensions() {
        double[] initialState = {0.0, 1.0};
        double time = 10.0;

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKutta25(); // a sample embedded Runge-Kutta integrator
        ExpandableStatefulODE equations = new DummyEquations(new double[]{0.0}); // state vector with fewer dimensions

        integrator.integrate(equations, time);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testIntegrate_MaxCountExceeded() {
        double[] initialState = {0.0, 1.0};
        double time = 10.0;
        int maxSteps = 1; // very few steps

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKutta25(); // a sample embedded Runge-Kutta integrator
        ExpandableStatefulODE equations = new DummyEquations(initialState);

        integrator.setMaxSteps(maxSteps);
        integrator.integrate(equations, time);
    }

    @Test(expected = NoBracketingException.class)
    public void testIntegrate_NoBracketing() {
        double[] initialState = {0.0, 1.0};
        double time = 10.0;
        double initialStep = 10.0; // very large initial step

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKutta25(); // a sample embedded Runge-Kutta integrator
        ExpandableStatefulODE equations = new DummyEquations(initialState);

        integrator.setMinStep(initialStep);
        integrator.integrate(equations, time);
    }
}

class DummyEquations implements ExpandableStatefulODE {

    private double[] state;

    public DummyEquations(double[] initialState) {
        this.state = initialState;
    }

    @Override
    public int getDimension() {
        return state.length;
    }

    @Override
    public void setTime(double time) {

    }

    @Override
    public void setCompleteState(double[] state) {
        this.state = state;
    }

    @Override
    public double[] getCompleteState() {
        return state.clone();
    }
}