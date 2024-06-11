package org.apache.commons.math3.ode.integrators;

import org.apache.commons.math3.ode.AbstractIntegrator;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.sampling.AbstractStepInterpolator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * Test suite for {@link AbstractIntegrator}.
 */
@RunWith(MockitoJUnitRunner.class)
public class AbstractIntegratorTest {

    private AbstractIntegrator integrator;

    @Before
    public void setup() {
        integrator = new AbstractIntegrator() {
            @Override
            protected void integrate(ExpandableStatefulODE equations, double t)
                throws NumberIsTooSmallException, DimensionMismatchException,
                       MaxCountExceededException, NoBracketingException {
                // do nothing
            }
        };
    }

    /**
     * Test integration with no events or step handlers.
     * @throws Exception if an error occurs during integration
     */
    @Test
    public void testEmptyIntegration() throws Exception {
        final ExpandableStatefulODE equations = Mockito.spy(new DummyODE(2));
        integrator.initIntegration(0, new double[2], 1);
        integrator.integrate(equations, 1);
        Assert.assertEquals(1, integrator.getEvaluations(), 0.0);
        Assert.assertArrayEquals(new double[] { 1, 1 }, equations.getPrimaryState(), 0.0);
    }

    /**
     * Test integration with one event handler that stops integration early.
     * @throws Exception if an error occurs during integration
     */
    @Test
    public void testEventHandling() throws Exception {
        final ExpandableStatefulODE equations = Mockito.spy(new DummyODE(2));
        final EventHandler eventHandler = Mockito.mock(EventHandler.class);
        integrator.addEventHandler(eventHandler, 0.5, 0.001, 10);
        integrator.initIntegration(0, new double[2], 1.2);
        integrator.integrate(equations, 1.1);
        Assert.assertEquals(1, integrator.getEvaluations(), 0.0);
        Assert.assertArrayEquals(new double[] { 1, 1 }, equations.getPrimaryState(), 0.0);
        Mockito.verify(eventHandler, Mockito.times(1)).handleEvent(Mockito.any(EventHandler.Event.class),
                                                                   Mockito.eq(equations.getPrimaryState()),
                                                                   Mockito.anyDouble());
    }

    /**
     * Test integration with one step handler that modifies the state.
     * @throws Exception if an error occurs during integration
     */
    @Test
    public void testStepHandling() throws Exception {
        final ExpandableStatefulODE equations = Mockito.spy(new DummyODE(2));
        final StepHandler stepHandler = Mockito.mock(StepHandler.class);
        integrator.addStepHandler(stepHandler);
        integrator.initIntegration(0, new double[2], 1);
        integrator.integrate(equations, 0.5);
        Assert.assertEquals(1, integrator.getEvaluations(), 0.0);
        Mockito.verify(stepHandler).handleStep(Mockito.any(AbstractStepInterpolator.class),
                                               Mockito.eq(false));
        Assert.assertArrayEquals(new double[] { 0.5, 0.5 }, equations.getPrimaryState(), 0.0);
    }

    /**
     * Test integration with one event handler that stops integration
     * early and one step handler that modifies the state.
     * @throws Exception if an error occurs during integration
     */
    @Test
    public void testEventAndStepHandling() throws Exception {
        final ExpandableStatefulODE equations = Mockito.spy(new DummyODE(2));
        final EventHandler eventHandler = Mockito.mock(EventHandler.class);
        final StepHandler stepHandler = Mockito.mock(StepHandler.class);
        integrator.addEventHandler(eventHandler, 0.5, 0.001, 10);
        integrator.addStepHandler(stepHandler);
        integrator.initIntegration(0, new double[2], 1.2);
        integrator.integrate(equations, 0.6);
        Assert.assertEquals(1, integrator.getEvaluations(), 0.0);
        Mockito.verify(eventHandler, Mockito.never()).handleEvent(Mockito.any(EventHandler.Event.class),
                                                                   Mockito.eq(equations.getPrimaryState()),
                                                                   Mockito.anyDouble());
        Mockito.verify(stepHandler, Mockito.never()).handleStep(Mockito.any(AbstractStepInterpolator.class),
                                                               Mockito.eq(false));
        Assert.assertArrayEquals(new double[] { 0.6, 0.6 }, equations.getPrimaryState(), 0.0);
        Mockito.verify(eventHandler, Mockito.times(1)).handleEvent(Mockito.any(EventHandler.Event.class),
                                                                   Mockito.eq(equations.getPrimaryState()),
                                                                   Mockito.eq(0.5));
    }

}

class DummyODE implements FirstOrderDifferentialEquations {

    private static final int DIMENSION = 2;

    public DummyODE(int dimension) {
        if (dimension != DIMENSION) {
            throw new IllegalArgumentException("Unexpected dimension: " + dimension);
        }
    }

    @Override
    public int getDimension() {
        return DIMENSION;
    }

    @Override
    public void computeDerivatives(double t, double[] y, double[] yDot) {
        yDot[0] = y[1];
        yDot[1] = 0;
    }

}