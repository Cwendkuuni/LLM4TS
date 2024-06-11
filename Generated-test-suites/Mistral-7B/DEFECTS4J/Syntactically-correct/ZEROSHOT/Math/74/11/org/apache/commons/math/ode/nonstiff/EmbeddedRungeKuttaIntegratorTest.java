import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.MaxStepEvent;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class EmbeddedRungeKuttaIntegratorTest {

    private EmbeddedRungeKuttaIntegrator integrator;
    private FirstOrderDifferentialEquations equations;
    private double[] initialCondition;
    private double startTime;
    private double endTime;
    private StepHandler stepHandler;

    @Before
    public void setUp() {
        double[] c = {0.0, 0.3, 0.6, 1.0};
        double[] a1 = {0.0, 0.4478713925195485, 0.6321205588555825, 1.0};
        double[] a2 = {0.0, 0, 0.2588190451025201, 0.6};
        double[] b = {0.0, 0.25, 0.642957812521271, 0.8321205588555825};
        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(a1, a2, b, 2);
        integrator = new EmbeddedRungeKuttaIntegrator("RK2", false, c, null, b, prototype, 1e-12, 1e-12) {
            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                return 1.0 + h / 2.0;
            }
        };
        integrator.setMinReduction(1.0e-4);
        integrator.setMaxGrowth(10.0);
        equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = Math.sin(y[0]);
            }
        };
        initialCondition = new double[]{Math.PI / 4.0};
        startTime = 0.0;
        endTime = 10.0;
        stepHandler = new StepHandler() {
            @Override
            public void handleStep(StepHandlerContext context, int stepNumber) {
                assertTrue(context.isAccepted());
                assertEquals(1.0, context.getStepSize(), 1e-14);
            }
        };
    }

    @Test
    public void testIntegration() throws Exception {
        double[] result = integrator.integrate(equations, startTime, initialCondition, endTime, new double[initialCondition.length]);
        assertEquals(endTime, result[0], 1e-12);
        assertTrue(Arrays.equals(new double[]{Math.sin(Math.PI / 2.0)}, result, 1e-12));
        stepHandler.reset();
        integrator.addStepHandler(stepHandler);
        integrator.integrate(equations, startTime, initialCondition, endTime, new double[initialCondition.length]);
        stepHandler.assertHandledSteps(2);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testMaxCountExceeded() throws Exception {
        double[] c = {0.0, 0.3, 0.6, 1.0, 1.0, 1.0};
        double[] a1 = {0.0, 0.4478713925195485, 0.6321205588555825, 1.0, 1.0, 1.0};
        double[] a2 = {0.0, 0, 0.2588190451025201, 0.6, 1.0, 1.0};
        double[] b = {0.0, 0.25, 0.642957812521271, 0.8321205588555825, 1.0, 1.0};
        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(a1, a2, b, 6);
        integrator = new EmbeddedRungeKuttaIntegrator("RK6", false, c, null, b, prototype, 1e-12, 1e-12) {
            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                return 1.0 + h / 2.0;
            }
        };
        integrator.setMinReduction(1.0e-4);
        integrator.setMaxGrowth(10.0);
        equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = Math.sin(y[0]);
            }
        };
        initialCondition = new double[]{Math.PI / 4.0};
        startTime = 0.0;
        endTime = 10.0;
        stepHandler = new StepHandler() {
            @Override
            public void handleStep(StepHandlerContext context, int stepNumber) {
                if (stepNumber > 1000) {
                    throw new MaxCountExceededException("Maximum number of steps reached.");
                }
            }
        };
        integrator.addStepHandler(stepHandler);
        integrator.integrate(equations, startTime, initialCondition, endTime, new double[initialCondition.length]);
    }

}