package org.apache.commons.math3.ode.nonstiff.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.nonstiff.test.MockExpandableStatefulODE;
import org.apache.commons.math3.util.Precision;

public class EmbeddedRungeKuttaIntegratorTest {
    @Test
    public void testAbstractMethods() {
        assertThrows(UnsupportedOperationException.class, () -> new EmbeddedRungeKuttaIntegrator("", false, null, null, null, null, 0.0, 0.0, null, null).getOrder());
    }

    @Test
    public void testConstructorWithMinStepMaxStep() {
        double[] c = {0.3, 0.4, 0.5, 0.6};
        double[][] a = {{0.0, 0.0, 0.0, 0.0}, {0.0, 0.2369268850561891, 0.25, 0.4736234203699001},
                {0.0, 0.1849055493453385, 0.6, 0.6123245691358665}, {0.0, 0.3351125422916674, 0.9, 1.0}};
        double[] b = {0.0, 0.6, 3.0 / 8.0, 3.0 / 8.0, 1.0};
        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(new EmbeddedRungeKutta78Method(), 0.01, 0.01, null, null);
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKutta54Integrator("integrator54", false, c, a, b, prototype, 0.001, 0.01, 1.0e-9, 1.0e-6);
        assertEquals(5, integrator.getOrder());
    }

    @Test
    public void testConstructorWithVecAbsoluteToleranceVecRelativeTolerance() {
        double[] c = {0.3, 0.4, 0.5, 0.6};
        double[][] a = {{0.0, 0.0, 0.0, 0.0}, {0.0, 0.2369268850561891, 0.25, 0.4736234203699001},
                {0.0, 0.1849055493453385, 0.6, 0.6123245691358665}, {0.0, 0.3351125422916674, 0.9, 1.0}};
        double[] b = {0.0, 0.6, 3.0 / 8.0, 3.0 / 8.0, 1.0};
        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(new EmbeddedRungeKutta78Method(), 0.01, 0.01, null, null);
        double[] vecAbsoluteTolerance = {1.0e-7, 1.0e-6, 1.0e-6, 1.0e-6};
        double[] vecRelativeTolerance = {1.0e-7, 1.0e-6, 1.0e-6, 1.0e-6};
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKutta54Integrator("integrator54", false, c, a, b, prototype, 0.001, 0.01, vecAbsoluteTolerance, vecRelativeTolerance);
        assertEquals(5, integrator.getOrder());
    }

    @Test
    public void testSanityChecks() {
        ExpandableStatefulODE equations = new MockExpandableStatefulODE();

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKutta54Integrator("integrator54", false, new double[0], null, null, null, 0.0, 0.0, null, null);
        assertThrows(IllegalArgumentException.class, () -> integrator.integrate(equations, 0.0));

        integrator = new EmbeddedRungeKutta54Integrator("integrator54", false, new double[0], null, null, null, 0.0, 0.0, new double[]{-1.0}, null);
        assertThrows(IllegalArgumentException.class, () -> integrator.integrate(equations, 0.0));

        integrator = new EmbeddedRungeKutta54Integrator("integrator54", false, new double[0], null, null, null, 0.0, 0.0, null, null);
        assertThrows(IllegalArgumentException.class, () -> integrator.integrate(null, 0.0));

        integrator = new EmbeddedRungeKutta54Integrator("integrator54", false, new double[0], null, null, null, 0.0, 0.0, null, null);
        assertThrows(IllegalArgumentException.class, () -> integrator.integrate(equations, Double.NaN));
    }

    @Test
    public void testIntegration() {
        ExpandableStatefulODE equations = new MockExpandableStatefulODE();
        double[] initState = {1.0, 1.0};
        double initialTime = 0.0;
        double finalTime = 2.0;

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKutta54Integrator("integrator54", false, new double[0], null, null, null, 0.001, 0.01, null, null);
        integrator.integrate(equations, initialTime);
        equations.setTime(initialTime + integrator.getStepSize());
        integrator.integrate(equations, initialTime + integrator.getStepSize());

        assertEquals(2, integrator.getNumberOfStepsTaken());

        double tolerance = 1.0e-6;
        double error = Precision.safeSub(equations.getCompleteState()[0], 1.5);
        assertEquals(error, 0.0, tolerance);
        error = Precision.safeSub(equations.getCompleteState()[1], 1.5);
        assertEquals(error, 0.0, tolerance);

        integrator.setMinReduction(0.1);
        integrator.setMaxGrowth(2.0);
        integrator.integrate(equations, initialTime);
        integrator.integrate(equations, initialTime + integrator.getStepSize());
        integrator.integrate(equations, initialTime + integrator.getStepSize());
        integrator.integrate(equations, initialTime + integrator.getStepSize());
        integrator.integrate(equations, initialTime + integrator.getStepSize());

        assertEquals(5, integrator.getNumberOfStepsTaken());
    }

    @Test
    public void testIntegrationWithFSAL() {
        ExpandableStatefulODE equations = new MockExpandableStatefulODE();
        double[] initState = {1.0, 1.0};
        double initialTime = 0.0;
        double finalTime = 2.0;

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaFehlberg78Integrator("integrator78", true, new double[0], null, null, null, 0.001, 0.01, null, null);
        integrator.integrate(equations, initialTime);
        equations.setTime(initialTime + integrator.getStepSize());
        integrator.integrate(equations, initialTime + integrator.getStepSize());

        assertEquals(2, integrator.getNumberOfStepsTaken());

        double tolerance = 1.0e-6;
        double error = Precision.safeSub(equations.getCompleteState()[0], 1.5);
        assertEquals(error, 0.0, tolerance);
        error = Precision.safeSub(equations.getCompleteState()[1], 1.5);
        assertEquals(error, 0.0, tolerance);

        integrator.setMinReduction(0.1);
        integrator.setMaxGrowth(2.0);
        integrator.integrate(equations, initialTime);
        integrator.integrate(equations, initialTime + integrator.getStepSize());
        integrator.integrate(equations, initialTime + integrator.getStepSize());
        integrator.integrate(equations, initialTime + integrator.getStepSize());
        integrator.integrate(equations, initialTime + integrator.getStepSize());

        assertEquals(5, integrator.getNumberOfStepsTaken());
    }

    @Test
    public void testIntegrationWithStepInterpolator() {
        ExpandableStatefulODE equations = new MockExpandableStatefulODE();
        double[] initState = {1.0, 1.0};
        double initialTime = 0.0;
        double finalTime = 2.0;

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKutta78Integrator("integrator78", false, new double[0], null, null, new StepInterpolator() {
            @Override
            public double interpolate(double t, double[] y, double[] ydot) {
                return Double.NaN;
            }
        }, 0.001, 0.01, null, null);
        integrator.integrate(equations, initialTime);
        equations.setTime(initialTime + integrator.getStepSize());
        integrator.integrate(equations, initialTime + integrator.getStepSize());

        assertThrows(IllegalStateException.class, () -> integrator.integrate(equations, initialTime + integrator.getStepSize()));
    }
}