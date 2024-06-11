package org.apache.commons.math3.ode.nonstiff;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.ode.sampling.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.junit.Test;

public class EmbeddedRungeKuttaIntegratorTest {
    @Test
    public void testIntegrateExample() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
        final String name = "ExampleEmbeddedRungeKuttaIntegrator";
        double[] c = {0.23692688505667133, 0.12843133252833567, 0.05682517363118422};
        double[][] a = {
            {1.0 / 3.0, 1.0 / 4.0, 1.0 / 8.0},
            {1.0 / 4.0, 1.0 / 3.0, 1.0 / 6.0},
            {1.0 / 8.0, 1.0 / 6.0, 1.0 / 2.0},
        };
        double[] b = {1.0 / 3.0, 1.0 / 4.0, 1.0};
        RungeKuttaStepInterpolator prototype = new RungeKuttaFehlberg78Method();

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator(name, false, c, a, b, prototype, 1.0E-6, 1.0E-6) {
            @Override
            protected double estimateError(final double[][] p0, final double[] p1, final double[] p2, final double p3) {
                // Add your custom error estimation logic here if needed
                return 0.1;
            }
        };

        ExpandableStatefulODE equations = new DummyODESystem();

        integrator.integrate(equations, 0.0);
        double[] y0 = equations.getCompleteState();

        integrator.integrate(equations, 1.0);
        double[] y1 = equations.getCompleteState();

        assertArrayEquals(y1, y0);

        integrator.integrate(equations, 2.0);
        double[] y2 = equations.getCompleteState();

        assertArrayEquals(y2, y1);
    }

    @Test
    public void testSetMinReduction() {
        String name = "ExampleEmbeddedRungeKuttaIntegrator";
        double[] c = {};
        double[][] a = {};
        double[] b = {};
        RungeKuttaStepInterpolator prototype = null;
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator(name, false, c, a, b, prototype, 1.0E-6, 1.0E-6) {
            @Override
            protected double estimateError(final double[][] p0, final double[] p1, final double[] p2, final double p3) {
                return 0.1;
            }
        };

        integrator.setMinReduction(0.1);
        assertEquals(0.1, integrator.getMinReduction());
    }

    @Test
    public void testSetMaxGrowth() {
        String name = "ExampleEmbeddedRungeKuttaIntegrator";
        double[] c = {};
        double[][] a = {};
        double[] b = {};
        RungeKuttaStepInterpolator prototype = null;
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator(name, false, c, a, b, prototype, 1.0E-6, 1.0E-6) {
            @Override
            protected double estimateError(final double[][] p0, final double[] p1, final double[] p2, final double p3) {
                return 0.1;
            }
        };

        integrator.setMaxGrowth(2.0);
        assertEquals(2.0, integrator.getMaxGrowth());
    }
}

class DummyODESystem implements ExpandableStatefulODE {
    private double time;
    private double[] state;

    public void setTime(double time) {
        this.time = time;
    }

    public double getTime() {
        return this.time;
    }

    public double[] getCompleteState() {
        return this.state;
    }

    public int getDimension() {
        return this.state.length;
    }

    public void setCompleteState(double[] state) {
        this.state = state;
    }

    public void setState(int index, double value) {
        this.state[index] = value;
    }

    public double getState(int index) {
        return this.state[index];
    }

    public void addState(int index, double value) {
        this.state[index] += value;
    }

    public void addState(double[] state) {
        System.arraycopy(state, 0, this.state, 0, this.state.length);
    }

    public void setPrimaryMapper(ODEFunctionMapper mapper) {
        throw new UnsupportedOperationException("Method not supported.");
    }

    public void setSecondaryMappers(ODEFunctionMapper[] mappers) {
        throw new UnsupportedOperationException("Method not supported.");
    }

    public void mapDerivatives(double[] derivatives, double t, double[] y) {
        // Add your custom mapping logic here if needed
    }
}