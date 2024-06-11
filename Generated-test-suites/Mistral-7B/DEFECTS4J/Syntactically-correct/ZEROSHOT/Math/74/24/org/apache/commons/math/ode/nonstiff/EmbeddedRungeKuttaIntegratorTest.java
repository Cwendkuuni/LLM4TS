import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.MockEvent;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class EmbeddedRungeKuttaIntegratorTest {

    private EmbeddedRungeKuttaIntegrator integrator;
    private FirstOrderDifferentialEquations equations;
    private double[] initialValues = new double[]{1.0, 1.0};

    @Before
    public void setUp() {
        equations = new MockDifferentialEquations();
        integrator = new EmbeddedRungeKutta45Integrator(
                "Runge-Kutta 4(5) method",
                true,
                new double[]{0.25, 0.45, 0.2, 0.05555555555555551, 0},
                new double[][]{
                        {0.25, 0, 0, 0, 0},
                        {0, 0.25, 0.25, 0, 0},
                        {0, 0, 0.5, 0.5, 0},
                        {0, 0, 0, 0.5, 0.5},
                        {0, 0, 0, 1.0/3, 1.0/3},
                        {0, 0, 0, 0, 1.0/6}
                },
                new double[]{0.25, 0.25, 0.5, 0.5, 1.0/6, 1.0/2},
                new RungeKuttaStepInterpolator(),
                0.001, 100.0,
                1e-10, 1e-10
        );
    }

    @Test
    public void testIntegrate() throws Exception {
        double[] y = new double[initialValues.length];
        double tStart = 0.0;
        double tEnd = 5.0;
        double result = integrator.integrate(equations, tStart, initialValues, tEnd, y);
        assertArrayEquals(new double[]{2.718281828459045, 2.718281828459045}, y, 1e-10);
        assertEquals(5.0, result, 1e-10);
    }

    @Test(expected = DerivativeException.class)
    public void testDerivativeException() throws Exception {
        double[] y = new double[initialValues.length];
        double tStart = 0.0;
        double tEnd = 5.0;
        MockDifferentialEquations equationsWithException = new MockDifferentialEquationsWithException();
        integrator.integrate(equationsWithException, tStart, initialValues, tEnd, y);
    }

    @Test(expected = IntegratorException.class)
    public void testIntegratorException() throws Exception {
        double[] y = new double[initialValues.length];
        double tStart = 0.0;
        double tEnd = 5.0;
        MockDifferentialEquationsWithEndTimeCheck equationsWithEndTimeCheck = new MockDifferentialEquationsWithEndTimeCheck();
        integrator.integrate(equationsWithEndTimeCheck, tStart, initialValues, tEnd, y);
    }

    private static class MockDifferentialEquations implements FirstOrderDifferentialEquations {

        @Override
        public void computeDerivatives(double t, double[] y, double[] yPrime) {
            yPrime[0] = y[1];
            yPrime[1] = 0.0;
        }

    }

    private static class MockDifferentialEquationsWithException extends MockDifferentialEquations {

        @Override
        public void computeDerivatives(double t, double[] y, double[] yPrime) {
            throw new DerivativeException("Derivative exception");
        }

    }

    private static class MockDifferentialEquationsWithEndTimeCheck extends MockDifferentialEquations {

        private double endTime = 3.0;
        private boolean endReached = false;

        @Override
        public void computeDerivatives(double t, double[] y, double[] yPrime) {
            if (t >= endTime) {
                endReached = true;
            }
            yPrime[0] = y[1];
            yPrime[1] = 0.0;
        }

        public boolean evaluateStep(StepHandler interpolator) {
            if (endReached) {
                return true;
            }
            return false;
        }

        public double getEventTime() {
            return endTime;
        }

    }

    private static class MockStepHandler implements StepHandler {

        @Override
        public void handleStep(StepHandler interpolator, int stepNumber) {
            System.out.println("Step number: " + stepNumber);
        }

    }

}