package org.apache.commons.math.ode.nonstiff.test;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NoRootFoundException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.DefaultValueEvent;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.EventManager;
import org.apache.commons.math3.ode.events.SimpleEvent;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.sampling.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.test.AbstractODETest;
import org.apache.commons.math3.ode.test.ODETestUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test class for {@link EmbeddedRungeKuttaIntegrator}.
 */
public class EmbeddedRungeKuttaIntegratorTest extends AbstractODETest {

    /**
     * Test method for {@link EmbeddedRungeKuttaIntegrator#EmbeddedRungeKuttaIntegrator(String, boolean, double[], double[][], double[], RungeKuttaStepInterpolator, double, double, double[], double[])}.
     */
    @Test
    public void testEmbeddedRungeKuttaIntegrator_1ArgsConstructor() {
        final double[] c = new double[]{0.0, 0.23692688505667133, 0.6787241750241227, 0.8650633635288556, 1.0};
        final double[][] a = new double[][]{{0.0, 0.0, 0.0, 0.0, 0.0},
                                             {0.23692688505667133, 0.25, 0.0, 0.0, 0.0},
                                             {0.6787241750241227, 0.0, 0.5, 0.0, 0.0},
                                             {0.8650633635288556, 0.0, 0.0, 0.875, 0.0},
                                             {1.0, 0.0, 0.0, 0.0, 1.0}};
        final double[] b = new double[]{0.0, 0.25, 0.5, 0.875, 1.0};
        final RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator();

        try {
            new EmbeddedRungeKuttaIntegrator("fehlberg23", false, c, a, b, prototype, 0.01, 0.05, null, null);
        } catch (IllegalArgumentException e) {
            throw new TestFailedException("Unexpected exception", e);
        }

        final EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("fehlberg23", false, c, a, b, prototype, 0.01, 0.05, null, null);

        assertEquals("fehlberg23", integrator.getName());
        assertFalse(integrator.isFSAL());
        assertEquals(5, integrator.getOrder());
        assertEquals(0.01, integrator.getMinStep(), 1e-9);
        assertEquals(0.05, integrator.getMaxStep(), 1e-9);

        // test to check the configuration of the constructed integrator
        ODETestUtils.testOrder(integrator, 1, 1e-9, 1e-9);
    }

    /**
     * Test method for {@link EmbeddedRungeKuttaIntegrator#EmbeddedRungeKuttaIntegrator(String, boolean, double[], double[][], double[], RungeKuttaStepInterpolator, double, double[], double[])}.
     */
    @Test
    public void testEmbeddedRungeKuttaIntegrator_2ArgsConstructor() {
        final double[] c = new double[]{0.0, 0.23692688505667133, 0.6787241750241227, 0.8650633635288556, 1.0};
        final double[][] a = new double[][]{{0.0, 0.0, 0.0, 0.0, 0.0},
                                             {0.23692688505667133, 0.25, 0.0, 0.0, 0.0},
                                             {0.6787241750241227, 0.0, 0.5, 0.0, 0.0},
                                             {0.8650633635288556, 0.0, 0.0, 0.875, 0.0},
                                             {1.0, 0.0, 0.0, 0.0, 1.0}};
        final double[] b = new double[]{0.0, 0.25, 0.5, 0.875, 1.0};
        final RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator();

        try {
            new EmbeddedRungeKuttaIntegrator("fehlberg23", false, c, a, b, prototype, 0.01, null, new double[]{1e-9, 1e-9});
        } catch (IllegalArgumentException e) {
            throw new TestFailedException("Unexpected exception", e);
        }

        final EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("fehlberg23", false, c, a, b, prototype, 0.01, null, new double[]{1e-9, 1e-9});

        assertEquals("fehlberg23", integrator.getName());
        assertFalse(integrator.isFSAL());
        assertEquals(5, integrator.getOrder());
        assertEquals(0.01, integrator.getMinStep(), 1e-9);
        assertEquals(Double.POSITIVE_INFINITY, integrator.getMaxStep(), 1e-9);
        assertEquals(new double[]{1e-9, 1e-9}, integrator.getTolerances());

        // test to check the configuration of the constructed integrator
        ODETestUtils.testOrder(integrator, 1, 1e-9, 1e-9);
    }

    /**
     * Test method for {@link EmbeddedRungeKuttaIntegrator#integrate(FirstOrderDifferentialEquations, double, double[], double, double[])}.
     */
    @Test
    public void testIntegrate() {
        final double[] c = new double[]{0.0, 0.23692688505667133, 0.6787241750241227, 0.8650633635288556, 1.0};
        final double[][] a = new double[][]{{0.0, 0.0, 0.0, 0.0, 0.0},
                                             {0.23692688505667133, 0.25, 0.0, 0.0, 0.0},
                                             {0.6787241750241227, 0.0, 0.5, 0.0, 0.0},
                                             {0.8650633635288556, 0.0, 0.0, 0.875, 0.0},
                                             {1.0, 0.0, 0.0, 0.0, 1.0}};
        final double[] b = new double[]{0.0, 0.25, 0.5, 0.875, 1.0};
        final RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator();

        final EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("fehlberg23", false, c, a, b, prototype, 0.01, 0.05, null, null);

        final double[] y0 = new double[]{0.0, 1.0};
        final double  t0  = 0.0;
        final double  t   = 5.0;

        final FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getNumberOfEquations() {
                return y0.length;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                for (int i = 0; i < y.length; ++i) {
                    yPrime[i] = y[i] * (1.0 + t);
                }
            }
        };

        final double result = integrator.integrate(equations, t0, y0, t, new double[y0.length]);

        final double[] y = new double[y0.length];
        System.arraycopy(y0, 0, y, 0, y.length);
        final double[] yDotK = new double[c.length + 1][y0.length];

        integrator.integrate(equations, t0, y0, 0.1, yDotK);

        for (int i = 0; i < y.length; ++i) {
            assertEquals(y[i], yDotK[c.length][i], 1e-8);
        }

        final double stepSize = integrator.getStepSize();
        assertEquals(0.1, stepSize, 1e-9);

        final double[] y1 = integrator.getLastState();

        for (int i = 0; i < y.length; ++i) {
            assertEquals(y1[i], y[i], 1e-8);
        }

        // test with event
        final List<Event> events = new ArrayList<>();
        final double timeToEvent = 2.5;
        final double eventValue = 1.0;
        events.add(new DefaultValueEvent(timeToEvent, eventValue));
        final EventManager eventManager = new EventManager();
        eventManager.addRelatingObjects(integrator, events);

        integrator.integrate(equations, t0, y0, t, new double[y0.length]);

        assertTrue(eventManager.isEventOccurred());
        assertEquals(timeToEvent, eventManager.getLastEventTime(), 1e-8);
        assertEquals(eventValue, eventManager.getLastEventValue(), 1e-8);

        // test with a non-empty stepHandlers list
        final List<StepHandler> stepHandlers = new ArrayList<>();
        stepHandlers.add(new StepHandler() {
            @Override
            public void handleStep(StepHandlerContext context, int index) {
                assertTrue(context.getIntegrator() instanceof EmbeddedRungeKuttaIntegrator);
                assertTrue(context.isAccepted());
                assertTrue(context.isLast());
            }
        });
        integrator.setStepHandlers(stepHandlers);

        integrator.integrate(equations, t0, y0, t, new double[y0.length]);

        final StepHandlerContext context = stepHandlers.get(0).getContext();
        assertTrue(context.isAccepted());
        assertTrue(context.isLast());

        // test with an empty stepHandlers list
        integrator.setStepHandlers(new ArrayList<>());
        integrator.integrate(equations, t0, y0, t, new double[y0.length]);

        assertFalse(stepHandlers.isEmpty());

        // test with MaxCountExceededException
        final int maxEvaluations = 1;
        integrator.setMaxEvaluations(maxEvaluations);
        integrator.integrate(equations, t0, y0, t, new double[y0.length]);

        assertTrue(integrator.isStepRejected());
        assertTrue(integrator.isMaxEvaluationsReached());
        assertEquals(maxEvaluations, integrator.getCurrentStepEvaluations());

        // test with NoRootFoundException
        integrator.setEventHandlersManager(eventManager);
        integrator.addRootFinder(new SimpleEventDetector(timeToEvent, eventValue));
        integrator.integrate(equations, t0, y0, t, new double[y0.length]);

        assertTrue(integrator.isStepRejected());
        assertTrue(integrator.isStepRejectedDueToEvents());

        // test with MathIllegalArgumentException
        integrator.setStepSize(Double.NaN);
        integrator.integrate(equations, t0, y0, t, new double[y0.length]);

        assertTrue(integrator.isStepRejected());
        assertTrue(integrator.isStepRejectedDueToArguments());
    }

}