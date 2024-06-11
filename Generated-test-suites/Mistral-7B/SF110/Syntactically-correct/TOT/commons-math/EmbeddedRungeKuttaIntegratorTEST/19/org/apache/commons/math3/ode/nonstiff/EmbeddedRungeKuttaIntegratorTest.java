package org.apache.commons.math3.ode.nonstiff.test;

import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test for EmbeddedRungeKuttaIntegrator abstract class.
 * This test case covers the basic functionality of the integrator.
 */
public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testSimpleIntegration() {
        double[] c = {0.23692688505667133, 0.0};
        double[][] a = {{0.25, 0.0, 0.0}, {0.0, 0.5, 0.0}, {0.0, 0.0, 0.625}};
        double[] b = {0.25, 0.0, 0.0};

        StepInterpolator prototype = new RungeKuttaFehlberg78Method();
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RKF78", false, c, a, b, prototype, 0.001, 0.01, 1.0e-12, 1.0e-12) {
            @Override
            protected double estimateError(final double[][] p0, final double[] p1, final double[] p2, final double p3) {
                // Simple error estimation for testing purposes
                return 1.0e-5;
            }
        };

        ExpandableStatefulODE equations = new DummyODESystem();

        integrator.integrate(equations, 0.0);
        double[] y0 = equations.getCompleteState();

        integrator.integrate(equations, 1.0);
        double[] y1 = equations.getCompleteState();

        assertArrayEquals("Initial and final states should match", y0, y1);

        assertEquals("Integrator step size should be greater than zero", integrator.getStepSize(), Precision.greaterThan(integrator.getStepSize(), 0.0));
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testNumberIsTooSmallException() {
        double[] c = {0.23692688505667133, 0.0};
        double[][] a = {{0.25, 0.0, 0.0}, {0.0, 0.5, 0.0}, {0.0, 0.0, 0.625}};
        double[] b = {0.25, 0.0, 0.0};

        StepInterpolator prototype = new RungeKuttaFehlberg78Method();
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RKF78", false, c, a, b, prototype, 0.001, 0.01, 1.0e-12, 1.0e-12) {
            @Override
            protected double estimateError(final double[][] p0, final double[] p1, final double[] p2, final double p3) {
                // Simple error estimation for testing purposes
                return 1.0e-5;
            }
        };

        ExpandableStatefulODE equations = new DummyODESystem();

        integrator.integrate(equations, 0.0);
        double[] y0 = equations.getCompleteState();

        integrator.setMinStep(0.0);
        integrator.integrate(equations, 1.0);
        fail("Expected NumberIsTooSmallException");
    }

    @Test(expected = DimensionMismatchException.class)
    public void testDimensionMismatchException() {
        double[] c = {0.23692688505667133, 0.0};
        double[][] a = {{0.25, 0.0, 0.0}, {0.0, 0.5, 0.0}, {0.0, 0.0, 0.625}};
        double[] b = {0.25, 0.0, 0.0};
        double[] y0 = new double[]{1.0, 2.0, 3.0};

        StepInterpolator prototype = new RungeKuttaFehlberg78Method();
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RKF78", false, c, a, b, prototype, 0.001, 0.01, 1.0e-12, 1.0e-12) {
            @Override
            protected double estimateError(final double[][] p0, final double[] p1, final double[] p2, final double p3) {
                // Simple error estimation for testing purposes
                return 1.0e-5;
            }
        };

        ExpandableStatefulODE equations = new DummyODESystem();
        equations.setCompleteState(y0);

        integrator.integrate(equations, 0.0);
        fail("Expected DimensionMismatchException");
    }

    @Test(expected = MaxCountExceededException.class)
    public void testMaxCountExceededException() {
        double[] c = {0.23692688505667133, 0.0};
        double[][] a = {{0.25, 0.0, 0.0}, {0.0, 0.5, 0.0}, {0.0, 0.0, 0.625}};
        double[] b = {0.25, 0.0, 0.0};

        StepInterpolator prototype = new RungeKuttaFehlberg78Method();
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RKF78", false, c, a, b, prototype, 0.001, 0.01, 1.0e-12, 1.0e-12) {
            @Override
            protected double estimateError(final double[][] p0, final double[] p1, final double[] p2, final double p3) {
                // Simple error estimation for testing purposes
                return 1.0e-5;
            }
        };

        ExpandableStatefulODE equations = new DummyODESystem();
        equations.setMaxSteps(10);

        integrator.integrate(equations, 1.0);
        fail("Expected MaxCountExceededException");
    }

    @Test(expected = NoBracketingException.class)
    public void testNoBracketingException() {
        double[] c = {0.23692688505667133, 0.0};
        double[][] a = {{0.25, 0.0, 0.0}, {0.0, 0.5, 0.0}, {0.0, 0.0, 0.625}};
        double[] b = {0.25, 0.0, 0.0};

        StepInterpolator prototype = new RungeKuttaFehlberg78Method();
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RKF78", false, c, a, b, prototype, 0.001, 0.01, 1.0e-12, 1.0e-12) {
            @Override
            protected double estimateError(final double[][] p0, final double[] p1, final double[] p2, final double p3) {
                // Simple error estimation for testing purposes
                return 1.0e-5;
            }
        };

        ExpandableStatefulODE equations = new DummyODESystem();

        integrator.integrate(equations, 1.0);
        double[] y1 = equations.getCompleteState();

        integrator.setStepSize(1.1 * integrator.getStepSize());

        try {
            integrator.integrate(equations, 1.0);
            fail("Expected NoBracketingException");
        } catch (NoBracketingException e) {
            // Expected exception
        }
    }

    @Test
    public void testSafetyFactor() {
        double[] c = {0.23692688505667133, 0.0};
        double[][] a = {{0.25, 0.0, 0.0}, {0.0, 0.5, 0.0}, {0.0, 0.0, 0.625}};
        double[] b = {0.25, 0.0, 0.0};

        StepInterpolator prototype = new RungeKuttaFehlberg78Method();
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RKF78", false, c, a, b, prototype, 0.001, 0.01, 1.0e-12, 1.0e-12) {
            @Override
            protected double estimateError(final double[][] p0, final double[] p1, final double[] p2, final double p3) {
                // Simple error estimation for testing purposes
                return 1.0e-5;
            }
        };

        ExpandableStatefulODE equations = new DummyODESystem();

        integrator.integrate(equations, 0.0);
        double initialSafety = integrator.getSafety();

        integrator.setSafety(0.8);

        integrator.integrate(equations, 1.0);

        assertEquals("Safety factor should be set to the specified value", 0.8, integrator.getSafety(), 1.0e-5);
        integrator.setSafety(initialSafety);
    }
}