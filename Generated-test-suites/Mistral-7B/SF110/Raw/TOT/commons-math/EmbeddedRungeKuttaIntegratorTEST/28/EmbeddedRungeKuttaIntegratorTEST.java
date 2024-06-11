package org.apache.commons.math3.ode.nonstiff.test;

import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.test.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testSimpleIntegration() {
        double[] c = {0.5, 0.25, 0};
        double[][] a = {{0, 1, 0}, {0.5, 0.5, 1}, {0, 0, 0.5}};
        double[] b = {0, 0.5, 0.5};
        EmbeddedRungeKuttaIntegrator integrator = new MyEmbeddedRungeKuttaIntegrator("Test integrator", true, c, a, b, null, 1.0e-10, 1.0e10, 1.0e-12, 1.0e-6);

        double[] initialValues = {1.0, 2.0};
        ExpandableStatefulODE equations = new MockODESystem(initialValues);

        integrator.integrate(equations, 5.0);

        double[] expected = {3.665207113178804, 3.4211352322312585};
        assertArrayEquals(expected, equations.getCompleteState(), 1e-7);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testIntegrationWithTooSmallStep() {
        double[] c = {0.5, 0.25, 0};
        double[][] a = {{0, 1, 0}, {0.5, 0.5, 1}, {0, 0, 0.5}};
        double[] b = {0, 0.5, 0.5};
        EmbeddedRungeKuttaIntegrator integrator = new MyEmbeddedRungeKuttaIntegrator("Test integrator", true, c, a, b, null, 1.0e-10, 1.0e10, 1.0e-12, 1.0e-6);

        double[] initialValues = {1.0, 2.0};
        ExpandableStatefulODE equations = new MockODESystem(initialValues);

        integrator.setMinStep(1.0e-15); // too small step
        integrator.integrate(equations, 5.0);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testIntegrationWithMismatchedDimensions() {
        double[] c = {0.5, 0.25, 0};
        double[][] a = {{0, 1, 0}, {0.5, 0.5, 1}, {0, 0, 0.5}};
        double[] b = {0, 0.5, 0.5};
        EmbeddedRungeKuttaIntegrator integrator = new MyEmbeddedRungeKuttaIntegrator("Test integrator", true, c, a, b, null, 1.0e-10, 1.0e10, 1.0e-12, 1.0e-6);

        double[] initialValues = {1.0};
        ExpandableStatefulODE equations = new MockODESystem(initialValues); // equations have only one dimension, but integrator expects 2

        integrator.integrate(equations, 5.0);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testIntegrationWithTooManySteps() {
        double[] c = {0.5, 0.25, 0};
        double[][] a = {{0, 1, 0}, {0.5, 0.5, 1}, {0, 0, 0.5}};
        double[] b = {0, 0.5, 0.5};
        EmbeddedRungeKuttaIntegrator integrator = new MyEmbeddedRungeKuttaIntegrator("Test integrator", true, c, a, b, null, 1.0e-10, 1.0e10, 1.0e-12, 1.0e-6);

        double[] initialValues = {1.0, 2.0};
        ExpandableStatefulODE equations = new MockODESystem(initialValues);

        integrator.setMaxSteps(1000000); // too many steps
        integrator.integrate(equations, 5.0);
    }

    @Test(expected = NoBracketingException.class)
    public void testIntegrationWithNoBracketing() {
        double[] c = {0.5, 0.25, 0};
        double[][] a = {{0, 1, 0}, {0.5, 0.5, 1}, {0, 0, 0.5}};
        double[] b = {0, 0.5, 0.5};
        EmbeddedRungeKuttaIntegrator integrator = new MyEmbeddedRungeKuttaIntegrator("Test integrator", true, c, a, b, null, 1.0e-10, 1.0e10, 1.0e-12, 1.0e-6);

        double[] initialValues = {1.0, 2.0};
        ExpandableStatefulODE equations = new MockODESystem(initialValues);

        integrator.setMinStep(1.0e11); // too big step
        integrator.integrate(equations, 5.0);
    }

    @Test
    public void testIntegratorWithCustomStepHandler() {
        double[] c = {0.5, 0.25, 0};
        double[][] a = {{0, 1, 0}, {0.5, 0.5, 1}, {0, 0, 0.5}};
        double[] b = {0, 0.5, 0.5};
        EmbeddedRungeKuttaIntegrator integrator = new MyEmbeddedRungeKuttaIntegrator("Test integrator", true, c, a, b, null, 1.0e-10, 1.0e10, 1.0e-12, 1.0e-6);

        double[] initialValues = {1.0, 2.0};
        ExpandableStatefulODE equations = new MockODESystem(initialValues);

        StepHandler stepHandler = new TestStepHandler();
        integrator.setStepHandler(stepHandler);

        integrator.integrate(equations, 5.0);

        assertTrue(stepHandler.isCalled());
    }

    private static class MyEmbeddedRungeKuttaIntegrator extends EmbeddedRungeKuttaIntegrator {
        public MyEmbeddedRungeKuttaIntegrator(String name, boolean fsal, double[] c, double[][] a, double[] b, RungeKuttaStepInterpolator prototype, double minStep, double maxStep, double[] vecAbsoluteTolerance, double[] vecRelativeTolerance) {
            super(name, fsal, c, a, b, prototype, minStep, maxStep, vecAbsoluteTolerance, vecRelativeTolerance);
        }

        @Override
        protected double estimateError(final double[][] p0, final double[] p1, final double[] p2, final double p3) {
            // custom error estimation logic here
            return 0.0;
        }
    }
}