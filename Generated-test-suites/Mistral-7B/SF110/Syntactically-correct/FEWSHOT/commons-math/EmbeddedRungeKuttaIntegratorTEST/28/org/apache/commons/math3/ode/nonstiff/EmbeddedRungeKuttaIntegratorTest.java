package org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator; // Assuming the test class is in a package named org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.ode.sampling.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.exception.*;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testAbstractMethods() {
        try {
            new EmbeddedRungeKuttaIntegrator("Name", false, null, null, null, null, 0.0, 0.0, null, null).getOrder();
            fail("getOrder() should not be called on an abstract class");
        } catch (NoSuchMethodException e) {
            // Expected
        }

        try {
            new EmbeddedRungeKuttaIntegrator("Name", false, null, null, null, null, 0.0, 0.0, null, null).estimateError(null, null, null, 0.0);
            fail("estimateError() should not be called on an abstract class");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testGettersAndSetters() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("Name", false, new double[]{1.0}, new double[][]{{0.0, 0.0}, {0.0, 1.0}}, new double[]{1.0}, new RungeKuttaStepInterpolator(new RungeKuttaMethods.RK23()), 0.01, 0.1, new double[]{0.001}, new double[]{0.01});

        assertEquals(false, integrator.isFSAL());
        assertEquals(1.0, integrator.getOrder(), 0.0);
        assertEquals(1.0, integrator.getSafety(), 0.0);
        assertEquals(0.2, integrator.getMinReduction(), 0.0);
        assertEquals(10.0, integrator.getMaxGrowth(), 0.0);

        integrator.setSafety(0.9);
        assertEquals(0.9, integrator.getSafety(), 0.0);
        integrator.setMinReduction(0.3);
        assertEquals(0.3, integrator.getMinReduction(), 0.0);
        integrator.setMaxGrowth(5.0);
        assertEquals(5.0, integrator.getMaxGrowth(), 0.0);
    }

    @Test
    public void testIntegrate_SingleStep() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
        ExpandableStatefulODE equations = new DummyODE();
        double t = 0.0;

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("Name", false, new double[]{1.0}, new double[][]{{0.0, 0.0}, {0.0, 1.0}}, new double[]{1.0}, new RungeKuttaStepInterpolator(new RungeKuttaMethods.RK23()), 0.01, 0.1, new double[]{0.001}, new double[]{0.01});
        integrator.integrate(equations, t);

        assertEquals(0.01, equations.getTime(), 0.0);
    }

    @Test
    public void testIntegrate_MultipleSteps() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
        ExpandableStatefulODE equations = new DummyODE();
        double[] y0 = {1.0, 1.0};
        double t = 1.0;

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("Name", false, new double[]{1.0}, new double[][]{{0.0, 0.0}, {0.0, 1.0}}, new double[]{1.0}, new RungeKuttaStepInterpolator(new RungeKuttaMethods.RK23()), 0.01, 0.1, new double[]{0.001}, new double[]{0.01});
        integrator.integrate(equations, t, y0);

        double[] expected = {1.6666666666666666, 2.3333333333333335};
        assertEquals(expected[0], equations.getCompleteState()[0], 0.001);
        assertEquals(expected[1], equations.getCompleteState()[1], 0.001);
    }

    @Test
    public void testIntegrate_FractionalStepSize() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
        ExpandableStatefulODE equations = new DummyODE();
        double[] y0 = {1.0, 1.0};
        double t = 2.0;

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("Name", false, new double[]{1.0}, new double[][]{{0.0, 0.0}, {0.0, 1.0}}, new double[]{1.0}, new RungeKuttaStepInterpolator(new RungeKuttaMethods.RK23()), 0.01, 0.1, new double[]{0.001}, new double[]{0.01});
        integrator.integrate(equations, t, y0, 0.5);

        double[] expected = {1.3333333333333333, 1.9166666666666666};
        assertEquals(expected[0], equations.getCompleteState()[0], 0.001);
        assertEquals(expected[1], equations.getCompleteState()[1], 0.001);
    }

    @Test
    public void testIntegrate_ReversedDirection() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
        ExpandableStatefulODE equations = new DummyODE();
        double[] y0 = {1.0, 1.0};
        double t = 1.0;

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("Name", false, new double[]{1.0}, new double[][]{{0.0, 0.0}, {0.0, 1.0}}, new double[]{1.0}, new RungeKuttaStepInterpolator(new RungeKuttaMethods.RK23()), 0.01, 0.1, new double[]{0.001}, new double[]{0.01});
        integrator.integrate(equations, t, y0, false);

        double[] expected = {0.6666666666666667, 0.3333333333333332};
        assertEquals(expected[0], equations.getCompleteState()[0], 0.001);
        assertEquals(expected[1], equations.getCompleteState()[1], 0.001);
    }

    @Test
    public void testStepStartAndStepSize() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
        ExpandableStatefulODE equations = new DummyODE();
        double[] y0 = {1.0, 1.0};
        double t = 1.0;

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("Name", false, new double[]{1.0}, new double[][]{{0.0, 0.0}, {0.0, 1.0}}, new double[]{1.0}, new RungeKuttaStepInterpolator(new RungeKuttaMethods.RK23()), 0.01, 0.1, new double[]{0.001}, new double[]{0.01});
        integrator.integrate(equations, t, y0);

        assertEquals(t, integrator.getStepStart(), 0.0);
        assertEquals(0.01, integrator.getStepSize(), 0.0);
    }

    @Test
    public void testIsLastStep() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
        ExpandableStatefulODE equations = new DummyODE();
        double[] y0 = {1.0, 1.0};
        double t = 1.0;

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("Name", false, new double[]{1.0}, new double[][]{{0.0, 0.0}, {0.0, 1.0}}, new double[]{1.0}, new RungeKuttaStepInterpolator(new RungeKuttaMethods.RK23()), 0.01, 0.1, new double[]{0.001}, new double[]{0.01});
        integrator.integrate(equations, t, y0);

        assertFalse(integrator.isLastStep());

        integrator.integrate(equations, t, y0);

        assertTrue(integrator.isLastStep());
    }
}

class DummyODE implements ExpandableStatefulODE {
    double time;
    double[] state;

    @Override
    public ExpandableStatefulODE setTime(double time) {
        this.time = time;
        return this;
    }

    @Override
    public double getTime() {
        return time;
    }

    @Override
    public double[] getCompleteState() {
        return state;
    }

    @Override
    public int getDimension() {
        return state.length;
    }

    @Override
    public double[] getState() {
        return state;
    }

    @Override
    public void setState(double[] state) {
        this.state = state;
    }
}