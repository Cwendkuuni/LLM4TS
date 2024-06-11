package org.apache.commons.math3.ode.nonstiff.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.ode.sampling.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.junit.Test;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testConstructor() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK4", true, new double[]{0.25, 0.25, 0.25, 0.25}, new double[][]{{0.25, 0.25, 0.25, 0.25}, {0, 0.25, 0.5, 0.25}, {0, 0.25, 0.25, 0.5}, {0, 0, 0.25, 0.25, 0.25}}, new double[]{0, 0.6, 0.6, 0.6}, new RungeKuttaStepInterpolator(new RK4Integrator()), 0.01, 0.1, null, null);
        assertNotNull(integrator);
    }

    @Test
    public void testGetOrder() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK4", true, new double[]{0.25, 0.25, 0.25, 0.25}, new double[][]{{0.25, 0.25, 0.25, 0.25}, {0, 0.25, 0.5, 0.25}, {0, 0.25, 0.25, 0.5}, {0, 0, 0.25, 0.25, 0.25}}, new double[]{0, 0.6, 0.6, 0.6}, new RungeKuttaStepInterpolator(new RK4Integrator()), 0.01, 0.1, null, null);
        assertEquals(4, integrator.getOrder());
    }

    @Test
    public void testGetSafety() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK4", true, new double[]{0.25, 0.25, 0.25, 0.25}, new double[][]{{0.25, 0.25, 0.25, 0.25}, {0, 0.25, 0.5, 0.25}, {0, 0.25, 0.25, 0.5}, {0, 0, 0.25, 0.25, 0.25}}, new double[]{0, 0.6, 0.6, 0.6}, new RungeKuttaStepInterpolator(new RK4Integrator()), 0.01, 0.1, null, null);
        assertEquals(0.9, integrator.getSafety(), 0.001);
    }

    @Test
    public void testSetSafety() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK4", true, new double[]{0.25, 0.25, 0.25, 0.25}, new double[][]{{0.25, 0.25, 0.25, 0.25}, {0, 0.25, 0.5, 0.25}, {0, 0.25, 0.25, 0.5}, {0, 0, 0.25, 0.25, 0.25}}, new double[]{0, 0.6, 0.6, 0.6}, new RungeKuttaStepInterpolator(new RK4Integrator()), 0.01, 0.1, null, null);
        integrator.setSafety(0.8);
        assertEquals(0.8, integrator.getSafety(), 0.001);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testIntegrateWithNumberIsTooSmallException() {
        ExpandableStatefulODE equations = new DummyODE();
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK4", true, new double[]{0.25, 0.25, 0.25, 0.25}, new double[][]{{0.25, 0.25, 0.25, 0.25}, {0, 0.25, 0.5, 0.25}, {0, 0.25, 0.25, 0.5}, {0, 0, 0.25, 0.25, 0.25}}, new double[]{0, 0.6, 0.6, 0.6}, new RungeKuttaStepInterpolator(new RK4Integrator()), 0.001, 0.1, null, null);
        integrator.integrate(equations, 0.1);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testIntegrateWithDimensionMismatchException() {
        ExpandableStatefulODE equations = new DummyODEWithMismatchedDimensions();
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK4", true, new double[]{0.25, 0.25, 0.25, 0.25}, new double[][]{{0.25, 0.25, 0.25, 0.25}, {0, 0.25, 0.5, 0.25}, {0, 0.25, 0.25, 0.5}, {0, 0, 0.25, 0.25, 0.25}}, new double[]{0, 0.6, 0.6, 0.6}, new RungeKuttaStepInterpolator(new RK4Integrator()), 0.01, 0.1, null, null);
        integrator.integrate(equations, 0.1);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testIntegrateWithMaxCountExceededException() {
        ExpandableStatefulODE equations = new DummyODEWithMaxCountExceeded();
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK4", true, new double[]{0.25, 0.25, 0.25, 0.25}, new double[][]{{0.25, 0.25, 0.25, 0.25}, {0, 0.25, 0.5, 0.25}, {0, 0.25, 0.25, 0.5}, {0, 0, 0.25, 0.25, 0.25}}, new double[]{0, 0.6, 0.6, 0.6}, new RungeKuttaStepInterpolator(new RK4Integrator()), 0.01, 0.1, null, null);
        integrator.setMaxSteps(1);
        integrator.integrate(equations, 1.0);
    }

    @Test(expected = NoBracketingException.class)
    public void testIntegrateWithNoBracketingException() {
        ExpandableStatefulODE equations = new DummyODEWithNoBracketing();
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK4", true, new double[]{0.25, 0.25, 0.25, 0.25}, new double[][]{{0.25, 0.25, 0.25, 0.25}, {0, 0.25, 0.5, 0.25}, {0, 0.25, 0.25, 0.5}, {0, 0, 0.25, 0.25, 0.25}}, new double[]{0, 0.6, 0.6, 0.6}, new RungeKuttaStepInterpolator(new RK4Integrator()), 0.01, 0.1, null, null);
        integrator.integrate(equations, 1.0);
    }

    @Test
    public void testIntegrateWithBasicScenario() {
        ExpandableStatefulODE equations = new DummyODE();
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK4", true, new double[]{0.25, 0.25, 0.25, 0.25}, new double[][]{{0.25, 0.25, 0.25, 0.25}, {0, 0.25, 0.5, 0.25}, {0, 0.25, 0.25, 0.5}, {0, 0, 0.25, 0.25, 0.25}}, new double[]{0, 0.6, 0.6, 0.6}, new RungeKuttaStepInterpolator(new RK4Integrator()), 0.01, 0.1, null, null);
        double[] y = new double[]{0.1, 0.2};
        integrator.integrate(equations, 1.0, y, y);
        assertEquals(1.1, y[0], 0.001);
        assertEquals(1.2, y[1], 0.001);
    }

    @Test
    public void testIntegrateWithEdgeCases() {
        ExpandableStatefulODE equations = new DummyODEWithEdgeCases();
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK4", true, new double[]{0.25, 0.25, 0.25, 0.25}, new double[][]{{0.25, 0.25, 0.25, 0.25}, {0, 0.25, 0.5, 0.25}, {0, 0.25, 0.25, 0.5}, {0, 0, 0.25, 0.25, 0.25}}, new double[]{0, 0.6, 0.6, 0.6}, new RungeKuttaStepInterpolator(new RK4Integrator()), 0.01, 0.1, null, null);
        double[] y = new double[]{Double.MIN_VALUE, Double.MAX_VALUE};
        integrator.integrate(equations, 1.0, y, y);
        assertEquals(Double.MIN_VALUE + 1.0, y[0], 0.001);
        assertEquals(Double.MAX_VALUE + 1.0, y[1], 0.001);
    }
}