package org.apache.commons.math.ode.nonstiff.tests;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.TooManyIterationsException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.When;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class EmbeddedRungeKuttaIntegratorTest {

    private EmbeddedRungeKuttaIntegrator integrator;
    private FirstOrderDifferentialEquations equations;
    private StepHandler stepHandler;
    private double initialStepSize;

    @BeforeEach
    public void setUp() {
        double[] c = {0.0, 0.3, 0.3, 0.6, 1.0};
        double[][] a = {{0.0, 0.25, 0.0}, {0.0, 0, 0.25}, {0.0, 0.5, 0.5}, {1.0, 0, 0.0}, {1.0, 0.5, 0.5}};
        double[] b = {0.3, 0.375, 0.25, 0.625, 1.0};

        integrator = new EmbeddedRungeKutta78Method(
                "Embedded Runge-Kutta 7(8) method", true, c, a, b,
                new RungeKuttaStepInterpolator(), 1e-12, 1e-12, null, null);

        equations = mock(FirstOrderDifferentialEquations.class);
        stepHandler = mock(StepHandler.class);
        initialStepSize = 0.1;
    }

    @Test
    public void testIntegrateWithNoEvents() throws Exception {
        double[] y0 = {1.0, 2.0};
        double t0 = 0.0;
        double tEnd = 10.0;

        integrator.setEquations(equations);
        integrator.setMinStep(initialStepSize / 10.0);
        integrator.setMaxStep(initialStepSize * 10.0);

        double result = integrator.integrate(equations, t0, y0, tEnd, new double[y0.length]);

        Assertions.assertEquals(tEnd, result, 1e-12);
        verify(stepHandler, never()).handleStep(any(RungeKuttaStepInterpolator.class), anyBoolean());
    }

    @Test
    public void testIntegrateWithOneEvent() throws Exception {
        double[] y0 = {1.0, 2.0};
        double t0 = 0.0;
        double tEnd = 3.0;
        double eventTime = 2.0;

        integrator.setEquations(equations);
        integrator.setMinStep(initialStepSize / 10.0);
        integrator.setMaxStep(initialStepSize * 10.0);

        When event = mock(When.class);
        when(event.isSatisfied(anyDouble())).thenReturn(true);
        when(equations.computeDerivatives(anyDouble(), anyDouble())).thenThrow(new NumberIsTooSmallException());

        combinesEventsHandlers(integrator, event);

        double result = integrator.integrate(equations, t0, y0, tEnd, new double[y0.length]);

        Assertions.assertEquals(eventTime, result, 1e-12);
        verify(stepHandler, never()).handleStep(any(RungeKuttaStepInterpolator.class), anyBoolean());
    }

    @Test
    public void testIntegrateWithMultipleEvents() throws Exception {
        double[] y0 = {1.0, 2.0};
        double t0 = 0.0;
        double tEnd = 10.0;

        integrator.setEquations(equations);
        integrator.setMinStep(initialStepSize / 10.0);
        integrator.setMaxStep(initialStepSize * 10.0);

        When event1 = mock(When.class);
        When event2 = mock(When.class);
        When event3 = mock(When.class);
        when(event1.isSatisfied(anyDouble())).thenReturn(true);
        when(event2.isSatisfied(anyDouble())).thenReturn(true);
        when(event3.isSatisfied(anyDouble())).thenReturn(false);

        combinesEventsHandlers(integrator, event1, event2, event3);

        double result = integrator.integrate(equations, t0, y0, tEnd, new double[y0.length]);

        Assertions.assertTrue(result >= 2.0 && result <= 3.0);
        verify(stepHandler, atLeast(3)).handleStep(any(RungeKuttaStepInterpolator.class), anyBoolean());
    }

    @Test
    public void testIntegrateWithMaxIterations() throws Exception {
        double[] y0 = {1.0, 2.0};
        double t0 = 0.0;
        double tEnd = 10.0;

        integrator.setEquations(equations);
        integrator.setMinStep(initialStepSize / 10.0);
        integrator.setMaxStep(initialStepSize * 10.0);

        when(equations.computeDerivatives(anyDouble(), anyDouble())).thenThrow(new MaxCountExceededException());

        double result = integrator.integrate(equations, t0, y0, tEnd, new double[y0.length]);

        Assertions.fail();
    }

    @Test
    public void testIntegrateWithMaxSteps() throws Exception {
        double[] y0 = {1.0, 2.0};
        double t0 = 0.0;
        double tEnd = 10.0;
        double maxSteps = 1;

        integrator.setEquations(equations);
        integrator.setMinStep(initialStepSize / 10.0);
        integrator.setMaxStep(initialStepSize * 10.0);
        integrator.setMaxSteps(maxSteps);

        double result = integrator.integrate(equations, t0, y0, tEnd, new double[y0.length]);

        Assertions.fail();
    }

    private void combinesEventsHandlers(EmbeddedRungeKuttaIntegrator integrator, Event... events) {
        integrator.setEventsHandlersManager(new EventsManager());
        for (Event event : events) {
            integrator.addEndTimeChecker(event.getRelativelyTo(anyDouble()), event);
        }
    }

    private void combinesEventsHandlers(EmbeddedRungeKuttaIntegrator integrator, Event... events) {
        integrator.setEventsHandlersManager(new EventsManager());
        for (Event event : events) {
            integrator.addEndTimeChecker(event.getRelativelyTo(anyDouble()), event);
        }
    }

}