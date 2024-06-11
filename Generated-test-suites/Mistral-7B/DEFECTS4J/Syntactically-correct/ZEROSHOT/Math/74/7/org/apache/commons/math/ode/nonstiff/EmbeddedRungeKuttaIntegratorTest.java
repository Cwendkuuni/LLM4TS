import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.When;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class EmbeddedRungeKuttaIntegratorTest {

    private EmbeddedRungeKuttaIntegrator integrator;
    private FirstOrderDifferentialEquations equations;

    @Before
    public void setUp() {
        // Set up the differential equations
        equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = math.pow(y[0], 2);
            }
        };

        // Set up the integrator
        integrator = new EmbeddedRungeKuttaIntegrator("Test", false,
                new double[] {0.3, 0.4, 0.1, 0.2, 0.1},
                new double[][] {{0.0, 0.33333333333333333, 0.4, 0.2, 0.1},
                                {0.0, 0.2, 0.5, 0.33333333333333333, 0.1},
                                {0.0, 0.2, 0.33333333333333333, 0.6, 0.1},
                                {0.0, 0.1, 0.33333333333333333, 0.6, 0.7},
                                {0.0, 0.1, 0.1, 0.7, 0.8}},
                new RungeKuttaStepInterpolator(), 0.01, 1.0,
                new double[] {1e-6, 1e-6}, new double[] {1e-8, 1e-8});

        integrator.setSafety(0.9);
        integrator.setMinReduction(0.2);
        integrator.setMaxGrowth(10.0);
    }

    @Test
    public void testIntegration() throws Exception {
        double[] y0 = new double[] {1.0};

        // Test integration from t=0 to t=1
        double tEnd = 1.0;
        double result = integrator.integrate(equations, 0.0, y0, tEnd, new double[1]);
        double[] yResult = new double[] {result};
        assertEquals(1.0, yResult[0], 1e-8);

        // Test integration from t=1 to t=2
        tEnd = 2.0;
        reset(integrator);
        result = integrator.integrate(equations, 1.0, y0, tEnd, yResult);
        assertEquals(3.5898544746636487E13, yResult[0], 1e-8);

        // Test integration from t=2 to t=3
        tEnd = 3.0;
        reset(integrator);
        result = integrator.integrate(equations, 2.0, y0, tEnd, yResult);
        assertEquals(8.1635313669823845E30, yResult[0], 1e-8);

        // Test integration with an event
        integrator.addStepHandler(new StepHandler() {
            @Override
            public void handleStep(StepHandlerContext context, int stepNumber) {
                if (context.isLastStep()) {
                    throw new When(1.5);
                }
            }
        });

        // Test integration from t=0 to t=2 with an event at t=1.5
        tEnd = 2.0;
        reset(integrator);
        Event event = new Event(1.5);
        integrator.addEvent(event);
        integrator.setEventsManager(new CombinedEventsManager());

        result = integrator.integrate(equations, 0.0, y0, tEnd, yResult);
        assertEquals(1.1213203435596936E17, yResult[0], 1e-8);
    }

    private void reset(EmbeddedRungeKuttaIntegrator integrator) {
        integrator.resetInternalState();
        integrator.resetEvaluations();
        integrator.setEquations(equations);
    }
}