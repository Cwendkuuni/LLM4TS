package org.apache.commons.math.ode.nonstiff;

import org.apache.commons.math3.analysis.PolynomialFunctions;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.RootFinderException;
import org.apache.commons.math3.util.Precision;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import org.apache.commons.math.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math.ode.events.DefaultValueDescriptor;
import org.apache.commons.math.ode.events.Event;
import org.apache.commons.math.ode.events.RootEvent;
import org.apache.commons.math.ode.sampling.RungeKuttaStepInterpolator;

/**
 * Unit tests for {@link EmbeddedRungeKuttaIntegrator}.
 */
public class EmbeddedRungeKuttaIntegratorTest {

    private EmbeddedRungeKuttaIntegrator integrator;
    private FirstOrderDifferentialEquations equations;

    @Before
    public void setUp() {
        integrator = new EmbeddedRungeKuttaIntegrator("test", true,
                new double[] { 0.3, 0.4, 0.1, 0.2 },
                new double[][] { { 0.0, 0.3, 0.4, 0.1 },
                                { 0.0, 0.0, 0.5, 0.5 },
                                { 0.0, 0.5, 0.0, 0.5 } },
                new double[] { 0.5, 0.5 },
                new RungeKuttaStepInterpolator(0.01));
        equations = new DummyDifferentialEquations();
    }

    /**
     * Test integration of a simple ODE: y'(t) = -y(t).
     * @throws MaxCountExceededException if too many steps are taken
     * @throws RootFinderException if a root is found before the final time
     */
    @Test
    public void testSimpleOde() throws MaxCountExceededException, RootFinderException {

        final double t0 = 0.0;
        final double y0 = 1.0;
        final double t1 = 1.0;
        final double y1 = 0.0;

        integrator.setMinStep(0.001);
        integrator.setMaxStep(0.1);
        integrator.setScaledAbsoluteTolerance(1.0e-8);
        integrator.setScaledRelativeTolerance(1.0e-8);

        final double result = integrator.integrate(equations, t0, new double[] { y0 }, t1, null);
        assertEquals(t1, result, 1.0e-6);
        assertArrayEquals(new double[] { y1 }, integrator.getSolution(), 1.0e-6);

        reset(equations, integrator);

        // Test with a very small tolerance
        integrator.setScaledAbsoluteTolerance(1.0e-12);
        integrator.setScaledRelativeTolerance(1.0e-12);
        integrator.integrate(equations, t0, new double[] { y0 }, t1, null);
        assertArrayEquals(new double[] { y1 }, integrator.getSolution(), 1.0e-12);

        // Test with a very large tolerance
        integrator.setScaledAbsoluteTolerance(1.0);
        integrator.setScaledRelativeTolerance(1.0);
        integrator.integrate(equations, t0, new double[] { y0 }, t1, null);
        assertArrayEquals(new double[] { y1 }, integrator.getSolution(), 1.0);

    }

    /**
     * Test integration of an ODE with a root.
     * @throws MaxCountExceededException if too many steps are taken
     * @throws RootFinderException if a root is found before the final time
     */
    @Test
    public void testOdeWithRoot() throws MaxCountExceededException, RootFinderException {

        final double t0 = 0.0;
        final double y0 = 1.0;
        final double t1 = 1.2;
        final double y1 = 0.0;

        integrator.setMinStep(0.001);
        integrator.setMaxStep(0.1);
        integrator.setScaledAbsoluteTolerance(1.0e-8);
        integrator.setScaledRelativeTolerance(1.0e-8);

        // Add a root event at t = 1.1
        final Event rootEvent = new RootEvent(1.1, new DefaultValueDescriptor("root"));
        integrator.addEventHandler(rootEvent);

        final double result = integrator.integrate(equations, t0, new double[] { y0 }, t1, null);
        assertEquals(t1, result, 1.0e-6);
        assertArrayEquals(new double[] { y1 }, integrator.getSolution(), 1.0e-6);

        // Test with a very small tolerance
        integrator.setScaledAbsoluteTolerance(1.0e-12);
        integrator.setScaledRelativeTolerance(1.0e-12);
        integrator.integrate(equations, t0, new double[] { y0 }, t1, null);
        assertArrayEquals(new double[] { y1 }, integrator.getSolution(), 1.0e-12);

        // Test with a very large tolerance
        integrator.setScaledAbsoluteTolerance(1.0);
        integrator.setScaledRelativeTolerance(1.0);
        integrator.integrate(equations, t0, new double[] { y0 }, t1, null);
        assertArrayEquals(new double[] { y1 }, integrator.getSolution(), 1.0);

        // Check that the root event has been triggered
        assertTrue(rootEvent.isTriggered());

    }

    /**
     * Test integration of a chaotic ODE: Lorenz system.
     * @throws MaxCountExceededException if too many steps are taken
     * @throws RootFinderException if a root is found before the final time
     */
    @Test
    public void testLorenzSystem() throws MaxCountExceededException, RootFinderException {

        final double t0 = 0.0;
        final double x0 = 0.1;
        final double y0 = 0.1;
        final double z0 = 0.01;
        final double t1 = 10.0;

        integrator.setMinStep(0.001);
        integrator.setMaxStep(1.0);
        integrator.setScaledAbsoluteTolerance(1.0e-4);
        integrator.setScaledRelativeTolerance(1.0e-4);

        final double[] solution = new double[3];
        integrator.integrate(new LorenzSystem(), t0, new double[] { x0, y0, z0 }, t1, solution);

        // Final solution should be (17.9856282, -4.9944272, 26.6323624)
        assertArrayEquals(new double[] { 17.9856282, -4.9944272, 26.6323624 }, solution, 1.0e-4);

    }

    /**
     * Test integration of an ODE with a discontinuous right-hand side.
     * @throws MaxCountExceededException if too many steps are taken
     * @throws RootFinderException if a root is found before the final time
     */
    @Test
    public void testOdeWithDiscontinuousRhs() throws MaxCountExceededException, RootFinderException {

        final double t0 = 0.0;
        final double y0 = 1.0;
        final double t1 = 2.0;

        integrator.setMinStep(0.001);
        integrator.setMaxStep(0.1);
        integrator.setScaledAbsoluteTolerance(1.0e-6);
        integrator.setScaledRelativeTolerance(1.0e-6);

        final double result = integrator.integrate(new DiscontinuousSystem(), t0, new double[] { y0 }, t1, null);
        assertEquals(t1, result, 1.0e-6);
        assertArrayEquals(new double[] { 1.5 }, integrator.getSolution(), 1.0e-6);

    }

    /**
     * Test that the integrator correctly reports an error when a step fails.
     */
    @Test(expected = IntegratorException.class)
    public void testStepFailure() {

        final double t0 = 0.0;
        final double y0 = 1.0;
        final double t1 = 1.0;

        integrator.setMinStep(0.001);
        integrator.setMaxStep(0.1);
        integrator.setScaledAbsoluteTolerance(1.0e-6);
        integrator.setScaledRelativeTolerance(1.0e-6);

        integrator.integrate(new DiscontinuousSystemWithError(), t0, new double[] { y0 }, t1, null);

    }

    /**
     * Reset the integrator and the differential equations for next test.
     * @param equations differential equations used for testing
     * @param integrator integrator to reset
     */
    private void reset(FirstOrderDifferentialEquations equations,
                       EmbeddedRungeKuttaIntegrator integrator) {
        equations.reset();
        integrator.reset();
    }

    /**
     * Dummy differential equations for testing.
     */
    private static class DummyDifferentialEquations implements FirstOrderDifferentialEquations {

        public void computeDerivatives(double t, double[] y, double[] ydot) {
            ydot[0] = -y[0];
        }

        public void reset() {
            // empty
        }

    }

    /**
     * Lorenz system.
     */
    private static class LorenzSystem implements FirstOrderDifferentialEquations {

        public void computeDerivatives(double t, double[] y, double[] ydot) {
            ydot[0] = 10.0 * (y[1] - y[0]);
            ydot[1] = y[2] - y[0];
            ydot[2] = y[0] * (30.0 - y[2]) - y[1];
        }

        public void reset() {
            // empty
        }

    }

    /**
     * Simple differential equations with a root.
     */
    private static class RootEventSystem implements FirstOrderDifferentialEquations {

        public void computeDerivatives(double t, double[] y, double[] ydot) {
            ydot[0] = -y[0];
        }

        public void reset() {
            // empty
        }

    }

    /**
     * Simple differential equations with a discontinuous right-hand side.
     */
    private static class DiscontinuousSystem implements FirstOrderDifferentialEquations {

        public void computeDerivatives(double t, double[] y, double[] ydot) {
            if (y[0] > 0) {
                ydot[0] = -y[0];
            } else {
                ydot[0] = y[0] * 3;
            }
        }

        public void reset() {
            // empty
        }

    }

    /**
     * Simple differential equations with a discontinuous right-hand side and an error.
     */
    private static class DiscontinuousSystemWithError implements FirstOrderDifferentialEquations {

        public void computeDerivatives(double t, double[] y, double[] ydot) {
            if (y[0] > 0) {
                ydot[0] = -y[0];
            } else {
                ydot[0] = y[0] * 3;
                throw new RuntimeException("Error during integration");
            }
        }

        public void reset() {
            // empty
        }

    }

}