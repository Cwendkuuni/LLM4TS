import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaIntegrator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RungeKuttaIntegratorTest {

    private RungeKuttaIntegrator integrator;
    private FirstOrderDifferentialEquations equations;
    private StepHandler stepHandler;

    @BeforeEach
    public void setup() {
        integrator = new EulerIntegrator(0.1, new EulerStepInterpolator());
        equations = (firstOrderDifferentialEquations) -> new double[] {0, firstOrderDifferentialEquations.getValue(0)};
        stepHandler = new StepCounterStepHandler();
    }

    @Test
    public void testIntegration() throws Exception {
        // Test basic functionality with simple equations
        double[] initialCondition = {0.0, 1.0};
        double startTime = 0.0;
        double endTime = 10.0;
        double[] expectedResult = {5.7746, 25.0663};
        integrator.integrate(equations, startTime, initialCondition, endTime, new double[initialCondition.length]);
        double[] result = integrator.getSolution();
        Assertions.assertArrayEquals(expectedResult, result);

        // Test with custom step handler
        integrator = new RungeKuttaIntegrator(0.1, new RK4StepInterpolator(), new RK4Integrator(), new double[]{0.25, 0.25, 0.25, 0.25}, new double[]{0.25, 0.25, 0.25, 0.25}) {
            @Override
            public void addStepHandler(StepHandler stepHandler) {
                super.addStepHandler(stepHandler);
                this.stepHandlers.add(0, stepHandler);
            }
        };
        integrator.setStepHandler(stepHandler);
        integrator.integrate(equations, startTime, initialCondition, endTime, new double[initialCondition.length]);
        Assertions.assertEquals(3, stepHandler.getNumberOfSteps());

        // Test with events
        equations = (firstOrderDifferentialEquations) -> new double[] {firstOrderDifferentialEquations.getValue(0), firstOrderDifferentialEquations.getValue(1)};
        double eventTime = 5.0;
        integrator = new RungeKuttaIntegrator(0.1, new RK4StepInterpolator(), new RK4Integrator(), new double[]{0.25, 0.25, 0.25, 0.25}, new double[]{0.25, 0.25, 0.25, 0.25}) {
            @Override
            public CombinedEventsManager getEventsManager() {
                return new CombinedEventsManager() {
                    @Override
                    public Event[] getEvents(double time) {
                        if (time >= eventTime) {
                            return new Event[]{new Event(time)};
                        }
                        return new Event[0];
                    }
                };
            }
        };
        integrator.integrate(equations, startTime, initialCondition, endTime, new double[initialCondition.length]);
        Assertions.assertEquals(5, stepHandler.getNumberOfSteps());
    }

    private static class StepCounterStepHandler implements StepHandler {
        private int numberOfSteps = 0;

        @Override
        public void handleStep(AbstractIntegrator integrator, double[] y, double step) {
            numberOfSteps++;
        }

        public int getNumberOfSteps() {
            return numberOfSteps;
        }

        public void reset() {
            numberOfSteps = 0;
        }
    }

}