import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.MockEvent;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegratorTest;
import org.apache.commons.math3.ode.sampling.RungeKuttaStepInterpolator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class EmbeddedRungeKuttaIntegratorTest {

    @org.junit.Test
    public void testEmbeddedRungeKuttaIntegrator() {

        // Create a mock differential equation system
        FirstOrderDifferentialEquations equations = new MockDifferentialEquations();

        // Create an instance of the EmbeddedRungeKuttaIntegrator to test
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRk4Integrator("RK4", false,
                new double[]{0.25, 0.25, 0.25, 0.25},
                new double[][]{
                        {0.25, 0}, {0, 0.25}, {0.25, 0.25}, {0, 0.25},
                        {0.25, 0.25}, {0.25, 0.25}},
                new double[]{0.25, 0.25},
                new RungeKuttaStepInterpolator(),
                0.001, 1.0, new double[]{1e-8, 1e-8});

        // Test integrating a simple differential equation
        double[] y0 = {1.0, 1.0};
        double tEnd = 2.0;
        double result = integrator.integrate(equations, 0.0, y0, tEnd, null);
        double[] yExpected = {2.718281828, 2.718281828};
        assertEquals(yExpected[0], y0[0], 1e-5);
        assertEquals(yExpected[1], y0[1], 1e-5);
        assertEquals(result, 2.718281828, 1e-5);

        // Test if the integrator rejects a step
        integrator.setMinReduction(0.1);
        integrator.setMaxGrowth(0.1);
        double[] y1 = new double[]{1.0, 1.0};
        double tStart = 1.0;
        y1[0] += 1e-6;
        double stepSize = integrator.getStepSize();
        integrator.integrate(equations, tStart, y1, tEnd, null);
        assertTrue(integrator.isLastStepAccepted());
        double error = integrator.getErrorEstimate();
        assertEquals(error, 1.01, 1e-2);
        integrator.reset();
        integrator.setMinReduction(0.01);
        integrator.setMaxGrowth(10.0);
        double h = integrator.initializeStep(equations, true, integrator.getOrder(),
                null, tStart, y1, yDotK[1], yTmp, yDotK[2]);
        assertFalse(integrator.isLastStepAccepted());

        // Test event handling
        CombinedEventsManager eventsManager = new CombinedEventsManager();
        MockEvent event = new MockEvent(1.5, 1.0);
        eventsManager.addEvent(event);
        integrator.setEventsManager(eventsManager);
        integrator.integrate(equations, 0.0, y0, 2.0, null);
        assertTrue(event.isTriggered());
    }
}

class MockDifferentialEquations implements FirstOrderDifferentialEquations {

    @Override
    public void computeDerivatives(double t, double[] y, double[] yDot) {
        yDot[0] = y[1];
        yDot[1] = -y[0];
    }

}

class MockEvent extends org.apache.commons.math3.ode.events.AbstractEvent {

    private double triggerTime;
    private double value;

    public MockEvent(double triggerTime, double value) {
        this.triggerTime = triggerTime;
        this.value = value;
    }

    @Override
    public boolean isTriggered(double t, double[] y) {
        return (t >= triggerTime);
    }

    @Override
    public void evaluateEvent(double t, double[] y, double[] yDot) {
        // Do nothing
    }

    public double getEventTime() {
        return triggerTime;
    }

}