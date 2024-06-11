import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testEmbeddedRungeKuttaIntegrator() {
        // Testing a sample embedded Runge-Kutta integrator
        double[] c = {0.0, 0.23692688505667133, 0.67982246633870732, 0.8678873216718987};
        double[][] a = {
                {0.0, 0.0, 0.0, 0.0},
                {0.23692688505667133, 0.68869891326111834, 0.13715668323822938, 0.0},
                {0.67982246633870732, 0.36672331044481387, 0.83557573158262528, 0.3674587182832823},
                {0.8678873216718987, 0.33948369271908521, 1.0, 0.6}
        };
        double[] b = {0.23692688505667133, 0.68869891326111834, 0.83557573158262528};

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("Embedded Runge-Kutta", true, c, a, b, new RungeKuttaStepInterpolator(), 0.001, 0.01, null, null) {
            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                double error = 0;
                for (int i = 0; i < y1.length; i++) {
                    error += FastMath.pow(Math.abs(y1[i] - y0[i] - h * yDotK[0][i]), 2);
                }
                return Math.sqrt(error);
            }
        };

        ExpandableStatefulODE equations = new ExpandableStatefulODE() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = Math.exp(-t) * y[0];
            }
        };

        integrator.integrate(equations, 0.0);
        double[] initialState = equations.getCompleteState().clone();

        assertEquals("Initial state should be the same after the first integration step", initialState[0], 1.0, 1e-8);

        integrator.integrate(equations, 1.0);
        double[] finalState = equations.getCompleteState().clone();
        double expectedState = Math.exp(-1.0);

        assertEquals("Final state should match the expected state", expectedState, finalState[0], 1e-8);

        integrator.integrate(equations, 5.0);
        finalState = equations.getCompleteState().clone();
        expectedState = Math.exp(-5.0);

        assertEquals("Final state should match the expected state", expectedState, finalState[0], 1e-8);

        integrator.integrate(equations, 10.0);
        finalState = equations.getCompleteState().clone();
        expectedState = Math.exp(-10.0);

        assertEquals("Final state should match the expected state", expectedState, finalState[0], 1e-8);

        integrator.integrate(equations, 15.0);
        finalState = equations.getCompleteState().clone();
        expectedState = Math.exp(-15.0);

        assertEquals("Final state should match the expected state", expectedState, finalState[0], 1e-8);

        // Testing the error estimation
        integrator.integrate(equations, 0.0);
        integrator.integrate(equations, 1.0);
        integrator.integrate(equations, 2.0);
        integrator.integrate(equations, 3.0);
        integrator.integrate(equations, 4.0);

        double[] yDotK = integrator.getYDotK();
        double[] y0_copy = initialState.clone();
        double[] y1_copy = finalState.clone();

        double error = integrator.estimateError(yDotK, y0_copy, y1_copy, 0.001);

        assertEquals("Error estimation should be smaller than 1", Precision.isClose(error, 0.0, 1e-8), true);

        // Testing step size control
        integrator.setSafety(0.95);
        integrator.setMinReduction(0.2);
        integrator.setMaxGrowth(2.0);

        integrator.integrate(equations, 100.0);
        finalState = equations.getCompleteState().clone();

        assertEquals("State should be smaller than the max step", equations.getMaxStep(), finalState[0], 1e-8);

        integrator.integrate(equations, -100.0);
        finalState = equations.getCompleteState().clone();

        assertEquals("State should be greater than the min step", equations.getMinStep(), finalState[0], 1e-8);
    }

}