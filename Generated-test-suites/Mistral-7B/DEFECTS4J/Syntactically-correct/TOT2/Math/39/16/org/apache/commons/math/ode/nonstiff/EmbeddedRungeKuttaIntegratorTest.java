import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegratortest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

/**
 * Test suite for {@link EmbeddedRungeKuttaIntegrator}.
 */
public class EmbeddedRungeKuttaIntegratorTest {

    /**
     * Test method for {@link EmbeddedRungeKuttaIntegrator#integrate(ExpandableStatefulODE, double)}
     * with a simple test function.
     */
    @Test
    public void testSimpleExample() {
        final double[] initialValues = {0.0, 1.0};
        final ExpandableStatefulODE equations = new EmbeddedRungeKuttaIntegratortest.DummyODE();
        equations.initialize(initialValues);

        final EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator(
            "RK4", false,
            new double[]{0.25, 0.25, 0.5, 0.25},
            new double[][]{
                {0.25, 0, 0.25, 0},
                {0, 0.25, 0.25, 0},
                {0, 0, 0.5, 0},
                {0, 0, 0, 1}
            },
            new double[]{0.25, 0.25, 0.25, 0.25},
            new RungeKuttaStepInterpolator() {
                @Override
                public void copy() {
                    // do nothing
                }
            },
            1.0e-10, 1.0e-12
        );

        integrator.integrate(equations, 2.0);

        final double[] expectedValues = {3.0, 3.0};
        assertEquals(expectedValues[0], equations.getCompleteState()[0], 1.0e-6);
        assertEquals(expectedValues[1], equations.getCompleteState()[1], 1.0e-6);
    }

    /**
     * Test method for {@link EmbeddedRungeKuttaIntegrator#integrate(ExpandableStatefulODE, double)}
     * with a test function with singularity.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testSingularity() {
        final double[] initialValues = {0.0, 1.0};
        final ExpandableStatefulODE equations = new EmbeddedRungeKuttaIntegratortest.SingularODE();
        equations.initialize(initialValues);

        final EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator(
            "RK4", false,
            new double[]{0.25, 0.25, 0.5, 0.25},
            new double[][]{
                {0.25, 0, 0.25, 0},
                {0, 0.25, 0.25, 0},
                {0, 0, 0.5, 0},
                {0, 0, 0, 1}
            },
            new double[]{0.25, 0.25, 0.25, 0.25},
            new RungeKuttaStepInterpolator() {
                @Override
                public void copy() {
                    // do nothing
                }
            },
            1.0e-10, 1.0e-12
        );

        integrator.integrate(equations, 1.0);
    }

    /**
     * Test method for {@link EmbeddedRungeKuttaIntegrator#integrate(ExpandableStatefulODE, double)}
     * with a test function with large step.
     */
    @Test(expected = MathIllegalStateException.class)
    public void testLargeStep() {
        final double[] initialValues = {0.0, 1.0};
        final ExpandableStatefulODE equations = new EmbeddedRungeKuttaIntegratortest.DummyODE();
        equations.initialize(initialValues);

        final EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator(
            "RK4", false,
            new double[]{0.25, 0.25, 0.5, 0.25},
            new double[][]{
                {0.25, 0, 0.25, 0},
                {0, 0.25, 0.25, 0},
                {0, 0, 0.5, 0},
                {0, 0, 0, 1}
            },
            new double[]{0.25, 0.25, 0.25, 0.25},
            new RungeKuttaStepInterpolator() {
                @Override
                public void copy() {
                    // do nothing
                }
            },
            1.0e-10, 1.0e-12
        );

        integrator.setMaxStep(100.0);
        integrator.integrate(equations, 2.0);
    }

    /**
     * Test method for {@link EmbeddedRungeKuttaIntegrator#integrate(ExpandableStatefulODE, double)}
     * with a test function with small step.
     */
    @Test
    public void testSmallStep() {
        final double[] initialValues = {0.0, 1.0};
        final ExpandableStatefulODE equations = new EmbeddedRungeKuttaIntegratortest.DummyODE();
        equations.initialize(initialValues);

        final EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator(
            "RK4", false,
            new double[]{0.25, 0.25, 0.5, 0.25},
            new double[][]{
                {0.25, 0, 0.25, 0},
                {0, 0.25, 0.25, 0},
                {0, 0, 0.5, 0},
                {0, 0, 0, 1}
            },
            new double[]{0.25, 0.25, 0.25, 0.25},
            new RungeKuttaStepInterpolator() {
                @Override
                public void copy() {
                    // do nothing
                }
            },
            1.0e-14, 1.0e-14
        );

        integrator.integrate(equations, 2.0);

        final double[] expectedValues = {3.0, 3.0};
        assertArrayEquals(expectedValues, equations.getCompleteState(), 1.0e-12);
    }

    /**
     * Test method for {@link EmbeddedRungeKuttaIntegrator#integrate(ExpandableStatefulODE, double)}
     * with a test function with large order.
     */
    @Test
    public void testLargeOrder() {
        final double[] initialValues = {0.0, 1.0, 0.0};
        final ExpandableStatefulODE equations = new EmbeddedRungeKuttaIntegratortest.DummyODE2();
        equations.initialize(initialValues);

        final EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator(
            "RK5(4)", true,
            new double[]{0.2, 0.3, 0.2, 0.3, 0.2},
            new double[][]{
                {0.2, 0, 0.2, 0, 0.2, 0},
                {0, 0.2, 0.2, 0, 0.2, 0},
                {0, 0, 0.2, 0, 0.2, 0},
                {0, 0, 0, 0.2, 0.2, 0},
                {0, 0, 0, 0, 0.2, 1.0/30.0},
                {0, 0, 0, 0, 0, 1.0/30.0}
            },
            new double[]{0.2, 0.2, 0.2, 0.2, 0.2, 0.2},
            new RungeKuttaStepInterpolator() {
                @Override
                public void copy() {
                    // do nothing
                }
            },
            1.0e-12, 1.0e-14
        );

        integrator.integrate(equations, 2.0);

        final double[] expectedValues = {3.0, 3.0, 1.0/5.0};
        assertEquals(expectedValues[0], equations.getCompleteState()[0], 1.0e-6);
        assertEquals(expectedValues[1], equations.getCompleteState()[1], 1.0e-6);
        assertEquals(expectedValues[2], equations.getCompleteState()[2], 1.0e-6);
    }

}