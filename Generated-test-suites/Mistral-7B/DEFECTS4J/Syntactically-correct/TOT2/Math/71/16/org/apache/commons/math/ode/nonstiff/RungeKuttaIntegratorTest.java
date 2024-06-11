import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.SingularMatrixException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.RelativisticEvent;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaIntegrator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.junit.Test;
import static org.junit.Assert.*;

public class RungeKuttaIntegratorTest {

    @Test
    public void testRungeKuttaIntegrator() throws Exception {

        // Test with a simple differential equation
        double[] yInit = {0.0, 1.0};
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return yInit.length;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                for (int i = 0; i < y.length; i++) {
                    yPrime[i] = y[i] * 2.0;
                }
            }
        };

        RungeKuttaIntegrator integrator = new EulerIntegrator(0.1, new EulerStepInterpolator(), equations);
        double tEnd = 10.0;
        double[] yEnd = {1.0, 100.0};

        integrator.integrate(equations, 0.0, yInit, tEnd, yEnd);
        assertArrayEquals(yEnd, yInit);

        // Test with a differential equation with a relative event
        equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = -Math.exp(-0.1 * t) * y[0];
            }

            @Override
            public RelativisticEvent getEvent(double t, double[] y) {
                return (Math.abs(y[0]) <= 0.001) ? new RelativisticEvent(t) : null;
            }
        };

        integrator = new RungeKutta4Integrator(0.1, new RungeKuttaStepInterpolator(4), equations);
        tEnd = 10.0;
        yEnd = new double[]{0.001};

        integrator.integrate(equations, 0.0, new double[]{1.0}, tEnd, yEnd);
        assertEquals(0.001, yEnd[0], 0.001);

        // Test with an exception when the number of steps exceed the maximum
        equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = y[0] * 2.0;
            }
        };

        integrator = new RungeKutta4Integrator(0.1, new RungeKuttaStepInterpolator(4), equations);
        tEnd = 1000000.0;
        double[] yInit2 = new double[]{1.0};

        try {
            integrator.integrate(equations, 0.0, yInit2, tEnd, yInit2);
            fail("Expected MaxCountExceededException");
        } catch (MaxCountExceededException e) {
            // Expected
        }

        // Test with an exception when the Jacobian matrix is singular
        equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = 0.0;
            }

            @Override
            public double[] computeJacobian(double t, double[] y) {
                return new double[]{0.0};
            }
        };

        integrator = new RungeKutta4Integrator(0.1, new RungeKuttaStepInterpolator(4), equations);
        tEnd = 1.0;
        yEnd = new double[]{1.0};

        try {
            integrator.integrate(equations, 0.0, yEnd, tEnd, yEnd);
            fail("Expected SingularMatrixException");
        } catch (SingularMatrixException e) {
            // Expected
        }

        // Test with a step handler
        StepHandler stepHandler = new StepHandler() {
            @Override
            public void handleStep(StepHandlerContext context, int stepNumber) {
                System.out.println("Step " + stepNumber + " completed");
            }
        };

        equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = y[0] * 2.0;
            }
        };

        integrator = new RungeKutta4Integrator(0.1, new RungeKuttaStepInterpolator(4), equations);
        tEnd = 10.0;
        yEnd = new double[]{1.0};
        integrator.addStepHandler(stepHandler);

        integrator.integrate(equations, 0.0, yEnd, tEnd, yEnd);

    }

}