import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testEmbeddedRungeKuttaIntegrator() {

        // Create a test equation
        Function testFunction = new Function();

        // Create an ODE system with the test function
        ExpandableStatefulODE equations = new DifferentialEquations(testFunction);

        // Initialize the integrator
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK4", false,
                new double[] { 0.25, 0.25, 0.25, 0.25 },
                new double[][] { { 0.0, 0.75, 0.0, 1.0 / 6.0, 0.0, 1.0 / 2.0, 0.0, 1.0 / 6.0 },
                              { 0.0, 0, 0.5555555555555555, 0.8333333333333333, 1.0 / 12.0, 0.9, 1.0 / 4.0, 0.6 },
                              { 0.0, 0, 0.2777777777777778, 0.6875, 0.6 / 15.0, 1.3125, 1.0 / 6.0, 1.0 / 3.0 },
                              { 0.0, 0, 0.1666666666666667, 1.3333333333333333, 0.375, 1.0, 1.0 / 2.0, 0.75 } },
                new RungeKuttaStepInterpolator(), 0.001, 0.01, 1e-8, 1e-8);

        // Set the initial conditions
        double[] initialState = { 1.0, 0.0 };
        equations.setInitialValues(initialState);

        // Define the time interval
        double startTime = 0.0;
        double endTime = 5.0;

        // Integrate the ODE system using the embedded Runge-Kutta method
        integrator.integrate(equations, endTime);

        // Check the final state
        double[] finalState = equations.getCompleteState();
        double expectedFinalState = 2.25;
        assertEquals(expectedFinalState, finalState[0], 1e-6);

        // Test that the error estimation was not rejected
        assertTrue(integrator.isLastStep());

        // Test step size control
        double actualStepSize = integrator.getStepSize();
        double expectedStepSize = 0.02083332497091861;
        assertEquals(expectedStepSize, actualStepSize, 1e-6);

        // Test safety factor
        double actualSafetyFactor = integrator.getSafety();
        double expectedSafetyFactor = 0.9;
        assertEquals(expectedSafetyFactor, actualSafetyFactor, 1e-6);

        // Test minimal reduction
        double actualMinReduction = integrator.getMinReduction();
        double expectedMinReduction = 0.2;
        assertEquals(expectedMinReduction, actualMinReduction, 1e-6);

        // Test maximal growth
        double actualMaxGrowth = integrator.getMaxGrowth();
        double expectedMaxGrowth = 10.0;
        assertEquals(expectedMaxGrowth, actualMaxGrowth, 1e-6);

    }

    private static class Function implements ExpandableStatefulODE {

        @Override
        public int getDimension() {
            return 1;
        }

        @Override
        public int getOrder() {
            return 4;
        }

        @Override
        public void computeDerivatives(double t, double[] y, double[] yDot) {
            yDot[0] = 0.1 * y[0];
        }

    }

    private static class DifferentialEquations implements ExpandableStatefulODE {

        private Function function;
        private double[] initialValues;

        public DifferentialEquations(Function function) {
            this.function = function;
        }

        @Override
        public int getDimension() {
            return function.getDimension();
        }

        @Override
        public int getOrder() {
            return function.getOrder();
        }

        @Override
        public void setInitialValues(double[] initialValues) {
            this.initialValues = initialValues;
        }

        @Override
        public void computeDerivatives(double t, double[] y, double[] yDot) {
            function.computeDerivatives(t, y, yDot);
        }

        @Override
        public double getTime() {
            return 0.0;
        }

        @Override
        public void setTime(double time) {
            // Not used in this test.
        }

        @Override
        public double[] getCompleteState() {
            return initialValues;
        }

        @Override
        public void setCompleteState(double[] state) {
            // Not used in this test.
        }

        @Override
        public void setPrimaryMapper(StateMapper mapper) {
            // Not used in this test.
        }

        @Override
        public void setSecondaryMappers(StateMapper[] mappers) {
            // Not used in this test.
        }

    }

}