package org.apache.commons.math3.ode.nonstiff.test;

import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.sampling.*;
import org.apache.commons.math3.exception.*;
import org.junit.Assert;
import org.junit.Test;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testEmbeddedRungeKuttaIntegrator() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
        double[] c = {0.23692688505667133, 0.32756158007528861, 0.04731315242182756, 0.0, 0.0, 0.6780486126933168};
        double[][] a = {{-0.0, 0.46875d, 0.0, 0.0, 0.0, 0.0},
                        {-0.46875d, -0.53144d, 0.72337d, 0.0, 0.0, 0.0},
                        {0.0, 0.72337d, 0.83333d, 0.46875d, 0.0, 0.0},
                        {0.0, 0.0, 0.46875d, -0.93242d, 0.93242d, 0.37366d},
                        {0.0, 0.0, 0.0, 0.93242d, -1.11025d, 0.62137d},
                        {0.0, 0.0, 0.0, 0.37366d, 0.62137d, -0.28284d}};
        double[] b = {0.0, 0.0, 0.0, 0.0, 0.0, 1.0};
        RungeKuttaStepInterpolator prototype = new RungeKuttaMerson14StepInterpolator();
        EmbeddedRungeKuttaIntegrator integrator = new DormandPrince853Integrator("DP853", true, c, a, b, prototype, 1.0E-12, 1.0E-8);

        StepInterpolator interpolator = integrator.createInterpolator();
        ExpandableStatefulODE equations = new DummyODESystem();
        integrator.integrate(equations, 0.0);

        double[] initialState = equations.getCompleteState();
        double[] expectedState = new double[]{1.0, 1.0};

        // Test forward integration
        integrator.integrate(equations, 1.0, initialState);
        double[] state = equations.getCompleteState();
        Assert.assertArrayEquals(expectedState, state, 0.001);

        // Test backward integration
        integrator.integrate(equations, -1.0, state);
        state = equations.getCompleteState();
        Assert.assertArrayEquals(initialState, state, 0.001);

        // Test integration with tolerance
        integrator.setAbsTol(1.0E-15);
        integrator.integrate(equations, 10.0, initialState);
        state = equations.getCompleteState();
        Assert.assertArrayEquals(expectedState, state, 0.001);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testNumberIsTooSmallException() throws NumberIsTooSmallException {
        StepInterpolator interpolator = new RungeKuttaMerson14StepInterpolator();
        ExpandableStatefulODE equations = new DummyODESystem();
        double stepSize = 1.0E-18;
        EmbeddedRungeKuttaIntegrator integrator = new DormandPrince853Integrator("DP853", true, null, null, null, interpolator, stepSize, 1.0, null, null);
        integrator.integrate(equations, 1.0, new double[]{0.0});
    }

    @Test(expected = DimensionMismatchException.class)
    public void testDimensionMismatchException() throws DimensionMismatchException {
        StepInterpolator interpolator = new RungeKuttaMerson14StepInterpolator();
        ExpandableStatefulODE equations = new DummyODESystem();
        equations.setPrimaryMapper(new DummyMapper(3));
        double[] initialState = new double[]{1.0, 1.0, 1.0};
        EmbeddedRungeKuttaIntegrator integrator = new DormandPrince853Integrator("DP853", true, null, null, null, interpolator, 1.0, 1.0, null, null);
        integrator.integrate(equations, 1.0, initialState);
        equations.setPrimaryMapper(new DummyMapper(2));
        integrator.integrate(equations, 1.0, initialState);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testMaxCountExceededException() throws MaxCountExceededException {
        StepInterpolator interpolator = new RungeKuttaMerson14StepInterpolator();
        ExpandableStatefulODE equations = new DummyODESystem();
        double[] initialState = new double[]{0.0};
        EmbeddedRungeKuttaIntegrator integrator = new DormandPrince853Integrator("DP853", true, null, null, null, interpolator, 1.0, 1.0, null, null);
        integrator.setMaxSteps(1);
        integrator.integrate(equations, 1.0, initialState);
    }

    @Test(expected = NoBracketingException.class)
    public void testNoBracketingException() throws NoBracketingException {
        StepInterpolator interpolator = new RungeKuttaMerson14StepInterpolator();
        ExpandableStatefulODE equations = new DummyODESystem();
        double stepSize = 1.0;
        double[] initialState = new double[]{0.0};
        EmbeddedRungeKuttaIntegrator integrator = new DormandPrince853Integrator("DP853", true, null, null, null, interpolator, stepSize, 1.0, null, null);
        integrator.setMaxSteps(1);
        integrator.integrate(equations, 1.0, initialState);
        integrator.integrate(equations, -1.0, initialState);
    }
}