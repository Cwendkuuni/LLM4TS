import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testEmbeddedRungeKuttaIntegrator() {
        double[][] coefficients = {{0.0, 0.5, 0.5, 1.0}, {0.0, 0.236926885, 0.236926885, 0.871119223}};
        int order = 2;
        double minStep = 0.001;
        double maxStep = 1.0;
        double scalAbsoluteTolerance = 1e-6;
        double scalRelativeTolerance = 1e-6;
        EmbeddedRungeKuttaIntegrator integrator = new RungeKutta25Integrator(
                "Runge-Kutta 2-5", true, coefficients[0], coefficients[1],
                new RungeKuttaStepInterpolator(), minStep, maxStep,
                scalAbsoluteTolerance, scalRelativeTolerance);

        ExpandableStatefulODE equations = new ExpandableStatefulODE() {
            @Override
            public int getNumberOfVariables() {
                return 1;
            }

            @Override
            public void initialize(double time) {
                setTime(time);
                setCompleteState(new double[]{0.0});
            }

            @Override
            public void computeDerivatives(double time, double[] state, double[] derivatives) {
                derivatives[0] = Math.sin(state[0]);
            }
        };

        double startTime = 0.0;
        double finalTime = 2 * Math.PI;

        integrator.integrate(equations, finalTime);

        double[] expectedState = new double[]{2.0 * Math.PI};
        double[] actualState = equations.getCompleteState();
        assertArrayEquals(expectedState, actualState, 1e-8);
        assertEquals(finalTime, equations.getTime(), 1e-8);

        // Test with wrong parameters
        try {
            integrator.integrate(null, finalTime);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            integrator.integrate(equations, -1.0);
            fail("Expected MathIllegalArgumentException");
        } catch (MathIllegalArgumentException e) {
            // Expected
        }

        // Test with small step size
        integrator.setMinStep(minStep / 10);
        integrator.integrate(equations, finalTime);
        double[] smallStepState = equations.getCompleteState();
        assertEquals(Precision.round(Math.sin(Math.PI / 2) * minStep / 2, 1e-8),
                smallStepState[0], 1e-8);

        // Test with large step size
        integrator.setMinStep(maxStep * 2);
        integrator.integrate(equations, finalTime);
        double[] largeStepState = equations.getCompleteState();
        assertEquals(expectedState[0], largeStepState[0], 1e-8);

        // Test with different tolerances
        double[] vecAbsoluteTolerance = new double[]{1e-7, 1e-8};
        double[] vecRelativeTolerance = new double[]{1e-7, 1e-8};
        integrator = new RungeKutta25Integrator(
                "Runge-Kutta 2-5", true, coefficients[0], coefficients[1],
                new RungeKuttaStepInterpolator(), minStep, maxStep,
                vecAbsoluteTolerance, vecRelativeTolerance);
        integrator.integrate(equations, finalTime);
        double[] toleranceState = equations.getCompleteState();
        assertEquals(expectedState[0], toleranceState[0], 1e-7);

    }
}