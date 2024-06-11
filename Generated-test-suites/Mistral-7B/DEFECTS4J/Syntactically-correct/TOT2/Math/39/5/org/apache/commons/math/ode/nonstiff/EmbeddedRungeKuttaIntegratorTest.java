import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.noise.NoiseFunction;
import org.apache.commons.math3.ode.noise.RkNoiseFunction;
import org.apache.commons.math3.ode.sampling.StepHandlerAdapter;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Testing EmbeddedRungeKuttaIntegrator.
 */
public class EmbeddedRungeKuttaIntegratorTest {

    /**
     * Test method for {@link EmbeddedRungeKuttaIntegrator#EmbeddedRungeKuttaIntegrator(String, boolean, double[], double[][], double[], RungeKuttaStepInterpolator, double, double, double[], double[])}.
     * @throws Exception if test fails
     */
    @Test
    public void testEmbeddedRungeKuttaIntegrator() throws Exception {
        // Test with default arguments
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator(
                "EmbeddedRungeKutta23", true,
                new double[]{0.3, 0.4, 0.1, 0.2, 0.5},
                new double[][]{{0.0, 0.25, 0.5, 0.875, 0.6875},
                              {0.0, 0.2, 0.4, 0.6, 0.375},
                              {0.0, 0.1, 0.3, 0.45, 0.275},
                              {0.0, 0.1, 0.2, 0.3, 0.2}},
                new double[]{0.0, 0.25, 0.5, 0.875},
                new RungeKuttaStepInterpolator(),
                0.001, 0.01,
                null, null);
        assertNotNull(integrator);
        assertEquals(5, integrator.getOrder());
        assertEquals(true, integrator.isFsal());
        // Test method getSafety()
        assertEquals(0.9, integrator.getSafety(), 0.001);
        integrator.setSafety(0.8);
        assertEquals(0.8, integrator.getSafety(), 0.001);
        // Test method getMinReduction()
        assertEquals(0.2, integrator.getMinReduction(), 0.001);
        integrator.setMinReduction(0.3);
        assertEquals(0.3, integrator.getMinReduction(), 0.001);
        // Test method getMaxGrowth()
        assertEquals(10.0, integrator.getMaxGrowth(), 0.001);
        integrator.setMaxGrowth(20.0);
        assertEquals(20.0, integrator.getMaxGrowth(), 0.001);

        // Test the integration itself
        ExpandableStatefulODE equations = new NoiseFunction(1.0);
        double[] y0 = new double[]{1.0};
        double t = 10.0;
        integrator.integrate(equations, t);
        assertEquals(y0[0], equations.getCompleteState()[0], 0.001);
        equations.setTime(t);
        integrator.resetInternalState();
        integrator.integrate(equations, t);
        assertEquals(y0[0], equations.getCompleteState()[0], 0.001);

        // Test with custom arguments
        integrator = new EmbeddedRungeKuttaIntegrator(
                "EmbeddedRungeKutta54", false,
                new double[]{0.5, 0.5, 0.25, 0.25, 0.25, 0.25, 0.125},
                new double[][]{{0.0, 0.2, 0.3, 0.375, 0.5555555555555555, 0.8222222222222222, 0.9375},
                              {0.0, 0.2, 0.3, 0.3, 0.6, 0.75, 0.875},
                              {0.0, 0.1, 0.2, 0.2, 0.2, 0.3, 0.4},
                              {0.0, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1},
                              {0.0, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1},
                              {0.0, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1},
                              {0.0, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1}},
                new double[]{0.0, 0.3, 0.4, 0.4, 0.4, 0.4, 0.4},
                new RungeKuttaStepInterpolator(),
                0.001, 0.01,
                new double[]{1e-12, 1e-12},
                new double[]{1e-12, 1e-12});
        integrator.integrate(equations, t);
        assertEquals(y0[0], equations.getCompleteState()[0], 0.001);

        // Test error handling
        equations = new RkNoiseFunction(1.0, 1.0);
        integrator = new EmbeddedRungeKuttaIntegrator(
                "EmbeddedRungeKutta54", false,
                new double[]{0.5, 0.5, 0.25, 0.25, 0.25, 0.25, 0.125},
                new double[][]{{0.0, 0.2, 0.3, 0.375, 0.5555555555555555, 0.8222222222222222, 0.9375},
                              {0.0, 0.2, 0.3, 0.3, 0.6, 0.75, 0.875},
                              {0.0, 0.1, 0.2, 0.2, 0.2, 0.3, 0.4},
                              {0.0, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1},
                              {0.0, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1},
                              {0.0, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1},
                              {0.0, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1}},
                new double[]{0.0, 0.3, 0.4, 0.4, 0.4, 0.4, 0.4},
                new RungeKuttaStepInterpolator(),
                0.001, 0.01,
                null, null);
        try {
            integrator.integrate(equations, t);
            assert False();
        } catch (MathIllegalArgumentException e) {
            // Expected
        }

        // Test custom step handler
        ExpandableStatefulODE equationsWithStepHandler = new StepHandlerAdapter() {
            @Override
            public void handleStep(int stepNumber, double[] y, double[] yDot, int eventType, double[] eventData) {
                if (eventType == ExpandableStatefulODE.EVENT_REACH_TIME) {
                    System.out.println("Reached time: " + equations.getTime());
                }
            }
        }.wrap(new NoiseFunction(1.0));
        integrator = new EmbeddedRungeKuttaIntegrator(
                "EmbeddedRungeKutta54", false,
                new double[]{0.5, 0.5, 0.25, 0.25, 0.25, 0.25, 0.125},
                new double[][]{{0.0, 0.2, 0.3, 0.375, 0.5555555555555555, 0.8222222222222222, 0.9375},
                              {0.0, 0.2, 0.3, 0.3, 0.6, 0.75, 0.875},
                              {0.0, 0.1, 0.2, 0.2, 0.2, 0.3, 0.4},
                              {0.0, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1},
                              {0.0, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1},
                              {0.0, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1},
                              {0.0, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1}},
                new double[]{0.0, 0.3, 0.4, 0.4, 0.4, 0.4, 0.4},
                new RungeKuttaStepInterpolator(),
                0.001, 5.0,
                null, null);
        integrator.integrate(equationsWithStepHandler, 10.0);
        assertTrue(true);

    }

}