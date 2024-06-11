package org.apache.commons.math3.ode.nonstiff.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

/**
 * Test class for {@link EmbeddedRungeKuttaIntegrator}.
 */
public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testGetOrder() {
        EmbeddedRungeKuttaIntegrator integrator = new MyEmbeddedRungeKuttaIntegrator("MyIntegrator", false, new double[]{0.3, 0.4}, new double[][]{{0.0, 0.4444444444444445, 0.0, 0.0}, {0.0, 0, 0.5555555555555556, 0.0}}, new double[]{0.0, 0.0}, new StepInterpolator(){}, 0.001, 0.01, null, null);
        assertEquals(2, integrator.getOrder());
    }

    @Test
    public void testGetSafety() {
        EmbeddedRungeKuttaIntegrator integrator = new MyEmbeddedRungeKuttaIntegrator("MyIntegrator", false, new double[]{0.3, 0.4}, new double[][]{{0.0, 0.4444444444444445, 0.0, 0.0}, {0.0, 0, 0.5555555555555556, 0.0}}, new double[]{0.0, 0.0}, new StepInterpolator(){}, 0.001, 0.01, null, null);
        assertEquals(0.9, integrator.getSafety(), 0.001);
    }

    @Test
    public void testSetSafety() {
        EmbeddedRungeKuttaIntegrator integrator = new MyEmbeddedRungeKuttaIntegrator("MyIntegrator", false, new double[]{0.3, 0.4}, new double[][]{{0.0, 0.4444444444444445, 0.0, 0.0}, {0.0, 0, 0.5555555555555556, 0.0}}, new double[]{0.0, 0.0}, new StepInterpolator(){}, 0.001, 0.01, null, null);
        integrator.setSafety(0.8);
        assertEquals(0.8, integrator.getSafety(), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIntegrateWithNullEquations() {
        EmbeddedRungeKuttaIntegrator integrator = new MyEmbeddedRungeKuttaIntegrator("MyIntegrator", false, new double[]{0.3, 0.4}, new double[][]{{0.0, 0.4444444444444445, 0.0, 0.0}, {0.0, 0, 0.5555555555555556, 0.0}}, new double[]{0.0, 0.0}, new StepInterpolator(){}, 0.001, 0.01, null, null);
        integrator.integrate(null, 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIntegrateWithWrongDimension() {
        ExpandableStatefulODE equations = new DummyODE(2);
        EmbeddedRungeKuttaIntegrator integrator = new MyEmbeddedRungeKuttaIntegrator("MyIntegrator", false, new double[]{0.3, 0.4}, new double[][]{{0.0, 0.4444444444444445, 0.0, 0.0}, {0.0, 0, 0.5555555555555556, 0.0}}, new double[]{0.0, 0.0}, new StepInterpolator(){}, 0.001, 0.01, null, null);
        integrator.integrate(equations, 0.0);
        equations = new DummyODE(3);
    }

    @Test
    public void testIntegrateWithCorrectDimension() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
        ExpandableStatefulODE equations = new DummyODE(2);
        EquationsStatefulODEWrapper wrapper = new EquationsStatefulODEWrapper(equations);
        double[] initialState = new double[]{1.0, 1.0};
        equations.setCompleteState(initialState);
        EmbeddedRungeKuttaIntegrator integrator = new MyEmbeddedRungeKuttaIntegrator("MyIntegrator", false, new double[]{0.3, 0.4}, new double[][]{{0.0, 0.4444444444444445, 0.0, 0.0}, {0.0, 0, 0.5555555555555556, 0.0}}, new double[]{0.0, 0.0}, new StepInterpolator(){}, 0.001, 0.01, null, null);
        integrator.integrate(wrapper, 3.0);
        double[] finalState = wrapper.getCompleteState();
        double[] expectedFinalState = new double[]{3.2573316139389526, 2.839782453349141};
        assertEquals(ExpectedArray.create(expectedFinalState), finalState, 0.001);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testIntegrateWithTooSmallStep() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
        ExpandableStatefulODE equations = new DummyODE(2);
        EquationsStatefulODEWrapper wrapper = new EquationsStatefulODEWrapper(equations);
        double[] initialState = new double[]{1.0, 1.0};
        equations.setCompleteState(initialState);
        EmbeddedRungeKuttaIntegrator integrator = new MyEmbeddedRungeKuttaIntegrator("MyIntegrator", false, new double[]{0.3, 0.4}, new double[][]{{0.0, 0.4444444444444445, 0.0, 0.0}, {0.0, 0, 0.5555555555555556, 0.0}}, new double[]{0.0, 0.0}, new StepInterpolator(){}, 0.0001, 0.01, null, null);
        integrator.integrate(wrapper, 3.0);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testIntegrateWithMaxCountExceeded() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
        ExpandableStatefulODE equations = new DummyODE(2);
        EquationsStatefulODEWrapper wrapper = new EquationsStatefulODEWrapper(equations);
        double[] initialState = new double[]{1.0, 1.0};
        equations.setCompleteState(initialState);
        EmbeddedRungeKuttaIntegrator integrator = new MyEmbeddedRungeKuttaIntegrator("MyIntegrator", false, new double[]{0.3, 0.4}, new double[][]{{0.0, 0.4444444444444445, 0.0, 0.0}, {0.0, 0, 0.5555555555555556, 0.0}}, new double[]{0.0, 0.0}, new StepInterpolator(){}, 0.001, 0.001, null, null);
        integrator.integrate(wrapper, 3.0);
    }

    @Test(expected = NoBracketingException.class)
    public void testIntegrateWithNoBracketing() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
        ExpandableStatefulODE equations = new DummyODE(2);
        EquationsStatefulODEWrapper wrapper = new EquationsStatefulODEWrapper(equations);
        double[] initialState = new double[]{1.0, 1.0};
        equations.setCompleteState(initialState);
        EmbeddedRungeKuttaIntegrator integrator = new MyEmbeddedRungeKuttaIntegrator("MyIntegrator", false, new double[]{0.3, 0.4}, new double[][]{{0.0, 0.4444444444444445, 0.0, 0.0}, {0.0, 0, 0.5555555555555556, 0.0}}, new double[]{0.0, 0.0}, new StepInterpolator(){}, 0.001, 0.1, null, null);
        integrator.integrate(wrapper, 1.5);
    }

    @Test
    public void testResetInternalState() {
        EmbeddedRungeKuttaIntegrator integrator = new MyEmbeddedRungeKuttaIntegrator("MyIntegrator", false, new double[]{0.3, 0.4}, new double[][]{{0.0, 0.4444444444444445, 0.0, 0.0}, {0.0, 0, 0.5555555555555556, 0.0}}, new double[]{0.0, 0.0}, new StepInterpolator(){}, 0.001, 0.01, null, null);
        integrator.integrate(new DummyODE(2), 3.0);
        integrator.resetInternalState();
    }
}

class MyEmbeddedRungeKuttaIntegrator extends EmbeddedRungeKuttaIntegrator {
    public MyEmbeddedRungeKuttaIntegrator(String name, boolean fsal, double[] c, double[][] a, double[] b, RungeKuttaStepInterpolator prototype, double minStep, double maxStep, double[] vecAbsoluteTolerance, double[] vecRelativeTolerance) {
        super(name, fsal, c, a, b, prototype, minStep, maxStep, vecAbsoluteTolerance, vecRelativeTolerance);
    }

    @Override
    protected double estimateError(final double[][] p0, final double[] p1, final double[] p2, final double p3) {
        // Implement your custom error estimation logic here
        return 0.0;
    }
}

class DummyODE implements ExpandableStatefulODE {
    private static int dimension;

    public DummyODE(int dim) {
        dimension = dim;
    }

    @Override
    public int getDimension() {
        return dimension;
    }

    @Override
    public void setTime(double time) {
        // Empty implementation
    }

    @Override
    public double getTime() {
        return 0.0;
    }

    @Override
    public void setCompleteState(double[] y) {
        // Empty implementation
    }

    @Override
    public double[] getCompleteState() {
        return new double[0];
    }

    @Override
    public int getStateDimension() {
        return dimension;
    }

    @Override
    public void setParameter(String name, double value) {
        // Empty implementation
    }

    @Override
    public double getParameter(String name) {
        return 0.0;
    }

    @Override
    public int getNumberOfParameters() {
        return 0;
    }

    @Override
    public void addEquations(ODEFunction function, JacobianFunction jacobianFunction) {
        // Empty implementation
    }

    @Override
    public void addEquations(ODEFunction function) {
        // Empty implementation
    }
}

class EquationsStatefulODEWrapper implements ExpandableStatefulODE {
    private final ExpandableStatefulODE equations;

    public EquationsStatefulODEWrapper(ExpandableStatefulODE equations) {
        this.equations = equations;
    }

    @Override
    public int getDimension() {
        return equations.getDimension();
    }

    @Override
    public void setTime(double time) {
        equations.setTime(time);
    }

    @Override
    public double getTime() {
        return equations.getTime();
    }

    @Override
    public void setCompleteState(double[] y) {
        equations.setCompleteState(y);
    }

    @Override
    public double[] getCompleteState() {
        return equations.getCompleteState();
    }

    @Override
    public int getStateDimension() {
        return equations.getStateDimension();
    }

    @Override
    public void setParameter(String name, double value) {
        equations.setParameter(name, value);
    }

    @Override
    public double getParameter(String name) {
        return equations.getParameter(name);
    }

    @Override
    public int getNumberOfParameters() {
        return equations.getNumberOfParameters();
    }

    @Override
    public void addEquations(ODEFunction function, JacobianFunction jacobianFunction) {
        equations.addEquations(function, jacobianFunction);
    }

    @Override
    public void addEquations(ODEFunction function) {
        equations.addEquations(function);
    }
}

class ExpectedArray {
    private static double[] array;

    public static double[] create(double[] expectedArray) {
        array = expectedArray;
        return array;
    }
}