import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class EmbeddedRungeKuttaIntegratorTest {

    private EmbeddedRungeKuttaIntegrator integrator;
    private FirstOrderDifferentialEquations equations;
    private double[] initialCondition;

    @Before
    public void setup() {
        equations = mock(FirstOrderDifferentialEquations.class);
        initialCondition = new double[]{0, 1};
        integrator = new EmbeddedRungeKuttaIntegrator("RK4", false,
                new double[]{0.25, 0.25, 0.25, 0.25},
                new double[][]{{0, 0, 0.5, 0.5, 0.5, 0.5},
                              {0.25, 0.25, 0.25, 0.5, 0.5, 0.5},
                              {0.46666666666666667, 0.4, 0.3333333333333333, 0.2, 0.1, 0.0}},
                new double[]{0.25, 0.25, 0.25, 0.25},
                new RungeKuttaStepInterpolator(),
                0.001, 10.0, null, null) {
            @Override
            protected double getOrder() {
                return 4;
            }
        };
    }

    @Test
    public void testIntegration() throws Exception {
        // Basic usage
        double[] result = integrator.integrate(equations, 0, initialCondition, 10, new double[initialCondition.length]);
        Assert.assertArrayEquals(new double[]{3.141592653589793, 2.718281828459045}, result, 0.0001);

        // Edge cases
        // Step size too small
        integrator.setMinStep(0.0);
        try {
            integrator.integrate(equations, 0, initialCondition, 10, new double[initialCondition.length]);
            fail("Expected IntegratorException");
        } catch (IntegratorException e) {
            // Expected
        }

        // Step size too large
        integrator.setMinStep(10000);
        try {
            integrator.integrate(equations, 0, initialCondition, 10, new double[initialCondition.length]);
            fail("Expected IntegratorException");
        } catch (IntegratorException e) {
            // Expected
        }

        // Relative tolerance too large
        integrator.setVecRelativeTolerance(new double[]{Double.POSITIVE_INFINITY});
        try {
            integrator.integrate(equations, 0, initialCondition, 10, new double[initialCondition.length]);
            fail("Expected IntegratorException");
        } catch (IntegratorException e) {
            // Expected
        }

        // Absolute tolerance too small
        integrator.setVecAbsoluteTolerance(new double[]{-1.0});
        try {
            integrator.integrate(equations, 0, initialCondition, 10, new double[initialCondition.length]);
            fail("Expected IntegratorException");
        } catch (IntegratorException e) {
            // Expected
        }

        // Step handler
        StepHandler stepHandler = mock(StepHandler.class);
        integrator.addStepHandler(stepHandler);
        integrator.integrate(equations, 0, initialCondition, 10, new double[initialCondition.length]);
        verify(stepHandler).handleStep(any(RungeKuttaStepInterpolator.class), eq(true));
    }
}