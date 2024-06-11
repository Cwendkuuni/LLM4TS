import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.EventManager;
import org.apache.commons.math3.ode.events.CombinedEventsManager;
import org.apache.commons.math3.ode.Integrator;
import org.apache.commons.math3.ode.sampling.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testEmbeddedRungeKuttaIntegrator() {
        double[] c = {0.0, 0.25, 0.5, 0.875, 1.0};
        double[][] a = {{0.0, 0.0, 0.0, 0.0, 0.0},
                        {0.25, 0.5, 0.0, 0.25, 0.25},
                        {0.5, 0.5, 0.5, 0.5, 0.0},
                        {0.875, 0.5, 1.0, 0.875, 0.4375},
                        {1.0, 0.0, 0.0, 1.0, 1.0}};
        double[] b = {0.0, 0.25, 0.5, 0.875, 1.0};
        double minStep = 0.001;
        double maxStep = 1.0;
        double scalAbsoluteTolerance = 1e-10;
        double scalRelativeTolerance = 1e-6;
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK4", false, c, a, b, new RungeKuttaStepInterpolator(), minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance) {
            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                double error = 0.0;
                for (int i = 0; i < y0.length; ++i) {
                    error += Math.pow(Math.abs(yDotK[yDotK.length - 1][i] - y1[i]), 2.0);
                }
                return Math.sqrt(error);
            }
        };

        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = Math.sin(y[0]);
            }
        };

        double[] y0 = {0.0};
        double t0 = 0.0;
        double tEnd = 3.141592653589793;
        double[] yEnd = {Math.PI};

        EventManager eventsManager = new CombinedEventsManager();
        Event event = new Event() {
            @Override
            public double getOrder() {
                return 1;
            }

            @Override
            public double getTime() {
                return Math.PI;
            }

            @Override
            public String getId() {
                return "myEvent";
            }

            @Override
            public void evaluateStep(double t, double[] y, double[] yDot, double h, double[] events) {
                events[0] = 1.0;
            }
        };
        eventsManager.addEvent(event);
        integrator.setEventsManager(eventsManager);

        try {
            double t = integrator.integrate(equations, t0, y0, tEnd, yEnd);
            assertEquals(tEnd, t, 1e-12);
            assertArrayEquals(yEnd, y, 1e-12);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Test failed due to exception: " + e.getMessage());
        }

        StepHandler stepHandler = new StepHandler() {
            @Override
            public void handleStep(StepHandlerContext context, int stepNumber) {
                double[] y = context.getY();
                double[] yDot = context.getYPrime();
                double t = context.getTime();
                System.out.println("Step " + stepNumber + ": y = " + y[0] + ", y' = " + yDot[0] + ", t = " + t);
            }
        };
        integrator.addStepHandler(stepHandler);

        try {
            double t = integrator.integrate(equations, t0, y0, tEnd, yEnd);
            assertEquals(tEnd, t, 1e-12);
            assertArrayEquals(yEnd, y, 1e-12);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Test failed due to exception: " + e.getMessage());
        }

    }

}