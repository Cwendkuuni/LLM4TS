package org.apache.commons.math.ode.nonstiff.test;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.PrecisionException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.NonstiffIntegrator;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.When;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.test.ODETestUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.math3.ode.test.ODETestUtils.createTestEquations;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testEmbeddedRungeKuttaIntegrator() throws Exception {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaRk23Integrator();
        double[] y0 = {1.0, 0.0};
        double tStart = 0.0;
        double tEnd = 10.0;
        FirstOrderDifferentialEquations equations = createTestEquations(y0.length);
        double[] y = new double[y0.length];

        // Test integration with no events
        double step = integrator.integrate(equations, tStart, y0, tEnd, y);
        Assert.assertEquals(tEnd, step, 1e-13);
        ODETestUtils.assertEquals(y0, y, 1e-13);

        // Test integration with event
        equations = new TestEquationsWithEvent();
        double[] yEvent = {1.0, 1.0};
        When event = new When(3.0, 1.0);
        List<Event> events = new ArrayList<>();
        events.add(event);
        StepHandler stepHandler = new StepHandler() {
            @Override
            public void handleStep(StepHandlerContext context, int stepNumber) {
                Event event = (Event) context.getUserData();
                Assert.assertEquals(event.getTime(), context.getCurrentTime(), 1e-13);
            }
        };
        integrator.setEventsHandlersManager(new CombinedEventsManager(events));
        integrator.addStepHandler(stepHandler);
        step = integrator.integrate(equations, tStart, y0, tEnd, y);
        Assert.assertEquals(3.0, integrator.getLastStepTime(), 1e-13);
        ODETestUtils.assertEquals(yEvent, y, 1e-13);

        // Test integration with maximum number of steps reached
        equations = new TestEquationsWithEvent();
        double[] yMax = new double[y0.length];
        integrator.setMaxSteps(100);
        step = integrator.integrate(equations, tStart, y0, tEnd, yMax);
        Assert.assertEquals(MaxCountExceededException.class, integrator.getLastException().getClass());

        // Test integration with maximum error reached
        equations = new TestEquationsWithLargeError();
        integrator.setMaxError(1e-6);
        try {
            integrator.integrate(equations, tStart, y0, tEnd, y);
            Assert.fail();
        } catch (PrecisionException e) {
            // expected
        }

        // Test integration with minimum step size reached
        integrator.setMinStep(1e-10);
        step = integrator.integrate(equations, tStart, y0, tEnd, y);
        Assert.assertEquals(1e-10, step, 1e-13);
    }
}

// Test differential equations with an event at t = 3.0
class TestEquationsWithEvent implements FirstOrderDifferentialEquations {

    @Override
    public int getDimension() {
        return 2;
    }

    @Override
    public void computeDerivatives(double t, double[] y, double[] yPrime) {
        yPrime[0] = y[1];
        yPrime[1] = -y[0];
    }
}