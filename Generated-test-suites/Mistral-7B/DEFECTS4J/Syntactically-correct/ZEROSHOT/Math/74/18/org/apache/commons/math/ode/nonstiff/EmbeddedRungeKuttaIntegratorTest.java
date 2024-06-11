import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.EmptyEventManager;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.junit.Test;
import static org.junit.Assert.*;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testIntegrator() throws Exception {
        double[][] coefficients = {
                {0.0, 0.0, 1.0 / 3.0, 1.0 / 4.0},
                {0.0, 1.0 / 6.0, 1.2 / 3.0, 1.1 / 4.0},
                {0.0, 1.0 / 8.0, 0.8 / 3.0, 0.3 / 4.0},
                {0.0, 1.0 / 12.0, 0.3 / 3.0, 0.2 / 4.0}
        };
        double[] absoluteTolerance = {1.0e-15, 1.0e-15, 1.0e-15, 1.0e-15};
        double[] relativeTolerance = {1.0e-8, 1.0e-8, 1.0e-8, 1.0e-8};

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator(
                "RK4", true, coefficients[0], coefficients, coefficients[0],
                new RungeKuttaStepInterpolator(), 1.0e-9, 1.0e9, absoluteTolerance, relativeTolerance) {
            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                double errorSum = 0.0;
                for (int i = 0; i < y0.length; i++) {
                    errorSum += Math.pow(Math.abs(yDotK[yDotK.length - 1][i] - yDotK[0][i]), 2);
                }
                return Math.sqrt(errorSum);
            }
        };

        FirstOrderDifferentialEquations functions = new FirstOrderDifferentialEquations() {
            public int getDimension() {
                return y0.length;
            }

            public void computeDerivatives(double time, double[] y, double[] yPrime) {
                for (int i = 0; i < y.length; i++) {
                    yPrime[i] = Math.pow(time, 2.0) * Math.sin(y[i]);
                }
            }
        };

        double[] y0 = {1.0, 2.0, 3.0, 4.0};
        double startTime = 0.0;
        double endTime = 1.0;

        StepHandler stepHandler = new StepHandler() {
            @Override
            public void handleStep(StepHandlerContext context, int stepNumber) {
                assertEquals(1.0, context.getStepSize(), 1e-12);
                assertEquals(1.0 + stepNumber / 10.0, context.getY()[0], 1e-12);
                assertEquals(1.0 + stepNumber / 10.0, context.getY()[1], 1e-12);
                assertEquals(1.0 + stepNumber / 10.0, context.getY()[2], 1e-12);
                assertEquals(1.0 + stepNumber / 10.0, context.getY()[3], 1e-12);
            }
        };

        integrator.addStepHandler(stepHandler);
        EmptyEventManager eventManager = new EmptyEventManager();
        integrator.setEventsManager(eventManager);

        double[] result = integrator.integrate(functions, startTime, y0, endTime, new double[y0.length]);
        assertEquals(endTime, result[0], 1e-12);
        assertArrayEquals(new double[]{5.0, 10.0, 15.0, 20.0}, result, 1e-12);
    }

}