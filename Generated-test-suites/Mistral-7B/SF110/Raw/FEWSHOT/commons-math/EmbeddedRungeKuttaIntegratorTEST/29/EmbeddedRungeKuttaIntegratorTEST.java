package org.apache.commons.math3.ode.nonstiff;

import static org.junit.Assert.*;

import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.ode.sampling.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RK23Integrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.junit.Test;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testRK23Integrator() {
        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator();
        EmbeddedRungeKuttaIntegrator integrator = new RK23Integrator("RK23", true, new double[]{-0.2369268850566713D, 0.2758620001639073D}, new double[][]{{0.2369268850566713D, 0.2758620001639073D}, {0.2369268850566713D, 0.9487160954787105D}, {0.6797932443740952D, 0.0436938297298237D}, {1.0D, 0D}}, new double[]{0.4472135955225201D, 0.0D}, prototype, 0.01D, 0.04D, null, null);
        ExpandableStatefulODE equations = new DummyODESystem();
        integrator.integrate(equations, 1.0);
        assertEquals(1.0, equations.getTime(), 1e-12);
        assertArrayEquals(new double[]{1.0, 0.0}, equations.getCompleteState(), 1e-12);
        integrator.resetInternalState();

        equations.setTime(0.5);
        integrator.integrate(equations, 0.5);
        assertEquals(0.5, equations.getTime(), 1e-12);
        assertArrayEquals(new double[]{0.5, 0.25}, equations.getCompleteState(), 1e-12);
        integrator.resetInternalState();

        equations.setTime(1.5);
        integrator.integrate(equations, 1.5);
        assertEquals(1.5, equations.getTime(), 1e-12);
        assertArrayEquals(new double[]{1.5, 1.0125}, equations.getCompleteState(), 1e-12);
        integrator.resetInternalState();

        equations.setTime(2.5);
        integrator.integrate(equations, 2.5);
        assertEquals(2.5, equations.getTime(), 1e-12);
        assertArrayEquals(new double[]{2.5, 2.401}, equations.getCompleteState(), 1e-12);
        integrator.resetInternalState();
    }

    @Test
    public void testRK23Integrator_WithRelativeAndAbsoluteTolerances() {
        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator();
        EmbeddedRungeKuttaIntegrator integrator = new RK23Integrator("RK23", true, new double[]{-0.2369268850566713D, 0.2758620001639073D}, new double[][]{{0.2369268850566713D, 0.2758620001639073D}, {0.2369268850566713D, 0.9487160954787105D}, {0.6797932443740952D, 0.0436938297298237D}, {1.0D, 0D}}, new double[]{0.4472135955225201D, 0.0D}, prototype, 0.01D, 0.04D, new double[]{1e-6, 1e-6}, new double[]{1e-6, 1e-6});
        ExpandableStatefulODE equations = new DummyODESystem();
        integrator.integrate(equations, 1.0);
        assertEquals(1.0, equations.getTime(), 1e-12);
        assertArrayEquals(new double[]{1.0, 0.0}, equations.getCompleteState(), 1e-7);
        integrator.resetInternalState();

        equations.setTime(0.5);
        integrator.integrate(equations, 0.5);
        assertEquals(0.5, equations.getTime(), 1e-12);
        assertArrayEquals(new double[]{0.5, 0.25}, equations.getCompleteState(), 1e-7);
        integrator.resetInternalState();

        equations.setTime(1.5);
        integrator.integrate(equations, 1.5);
        assertEquals(1.5, equations.getTime(), 1e-12);
        assertArrayEquals(new double[]{1.5, 1.0125}, equations.getCompleteState(), 1e-7);
        integrator.resetInternalState();

        equations.setTime(2.5);
        integrator.integrate(equations, 2.5);
        assertEquals(2.5, equations.getTime(), 1e-12);
        assertArrayEquals(new double[]{2.5, 2.401}, equations.getCompleteState(), 1e-7);
        integrator.resetInternalState();
    }
}

class DummyODESystem implements ExpandableStatefulODE {
    private double time = 0.0;
    private double[] state = new double[]{0.0};

    public void setTime(double t) {
        this.time = t;
    }

    public double getTime() {
        return this.time;
    }

    public void setCompleteState(double[] state) {
        this.state = state;
    }

    public double[] getCompleteState() {
        return this.state;
    }

    public void setDerivatives(double[] derivatives) {
    }

    public double[] getDerivatives() {
        return new double[]{1.0};
    }

    public int getDimension() {
        return 1;
    }

    public int getMainDimension() {
        return 1;
    }

    public int getPrimaryIndex(int variable) {
        return 0;
    }

    public int getSecondaryIndex(int variable, int primary) {
        throw new UnsupportedOperationException();
    }

    public void setPrimaryMapper(Mapper mapper) {
    }

    public void setSecondaryMappers(Mapper[] mappers) {
    }
}