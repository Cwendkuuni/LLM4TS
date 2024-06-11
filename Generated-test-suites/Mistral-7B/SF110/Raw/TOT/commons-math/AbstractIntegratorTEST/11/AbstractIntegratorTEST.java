package org.apache.commons.math3.ode.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.ode.AbstractIntegrator;
import org.apache.commons.math3.ode.Event;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.EventException;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.events.MultipleStepsEvent;
import org.apache.commons.math3.ode.events.StepHandler;
import org.apache.commons.math3.ode.sampling.EventList;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.apache.commons.math3.ode.util.ExpandableStatefulODE;
import org.apache.commons.math3.ode.util.EquationsMapper;
import org.apache.commons.math3.ode.util.InterpolatingStepHandler;
import org.apache.commons.math3.ode.util.StateInterpolator;
import org.apache.commons.math3.ode.util.StepContext;
import org.apache.commons.math3.ode.util.StepHandlerAdapter;
import org.apache.commons.math3.ode.util.UnivariateSolver;
import org.apache.commons.math3.util.Precision;
import org.junit.Before;
import org.junit.Test;

public class AbstractIntegratorTest {
    private static final double EPSILON = 1.0e-12;
    private static final double RELATIVE_TOLERANCE = 1.0e-9;
    private static final double ABSOLUTE_TOLERANCE = 1.0e-15;

    private AbstractIntegrator integrator;
    private FirstOrderDifferentialEquations equations;
    private double[] initialValues;
    private double[] expectedValues;

    @Before
    public void setUp() {
        integrator = new MyAbstractIntegrator("MyIntegrator");
        equations = new MyDifferentialEquations();
        initialValues = new double[] { 1.0, 2.0 };
        expectedValues = new double[] { 1.1, 1.2 };
        integrator.initIntegration(0.0, initialValues, 1.0);
    }

    @Test
    public void testIntegration() throws Exception {
        double[] y = new double[equations.getDimension()];
        double t = integrator.integrate(equations, 0.0, initialValues, 1.0, y);
        assertArrayEquals(expectedValues, y);
        assertEquals(1.0, t, RELATIVE_TOLERANCE + ABSOLUTE_TOLERANCE);
    }

    @Test
    public void testIntegrationWithEventHandlers() throws Exception {
        integrator.addEventHandler(new MyEventHandler(), 0.1, 0.01, 10);
        integrator.addEventHandler(new MyEventHandler2(), 0.2, 0.02, 5, new StepInterpolator(new RungeKuttaMethod()));
        integrator.addStepHandler(new InterpolatingStepHandler(new StepHandlerAdapter() {
            @Override
            public void handleStep(StepContext context, boolean lastStep) {
                assertEquals(MyIntegrator.class, context.getIntegrator().getClass());
                assertFalse(lastStep);
            }
        }));

        double[] y = new double[equations.getDimension()];
        double t = integrator.integrate(equations, 0.0, initialValues, 2.0, y);
        assertArrayEquals(expectedValues, y);
        assertEquals(2.0, t, RELATIVE_TOLERANCE + ABSOLUTE_TOLERANCE);

        List<Event> events = integrator.getEventHandlers().getEvents();
        assertEquals(2, events.size());
        Event event1 = events.get(0);
        assertEquals(MyEventHandler.class, event1.getHandler().getClass());
        assertEquals(0.1, event1.getTime(), RELATIVE_TOLERANCE + ABSOLUTE_TOLERANCE);
        Event event2 = events.get(1);
        assertEquals(MyEventHandler2.class, event2.getHandler().getClass());
        assertEquals(0.2, event2.getTime(), RELATIVE_TOLERANCE + ABSOLUTE_TOLERANCE);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testIntegrationWithIncorrectInitialValues() throws Exception {
        initialValues = new double[equations.getDimension() + 1];
        integrator.integrate(equations, 0.0, initialValues, 1.0, new double[equations.getDimension()]);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testIntegrationWithSmallStepSize() throws Exception {
        integrator.setStepSize(0.001);
        integrator.integrate(equations, 0.0, initialValues, 1.0, new double[equations.getDimension()]);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testIntegrationWithMaxEvaluationsReached() throws Exception {
        integrator.setMaxEvaluations(100);
        integrator.integrate(equations, 0.0, initialValues, 1.0, new double[equations.getDimension()]);
    }

    @Test(expected = NoBracketingException.class)
    public void testIntegrationWithNoBracketing() throws Exception {
        integrator.setExpandable(new ExpandableStatefulODE() {
            @Override
            public double computeTimeDerivative(double t, double[] y) {
                throw new UnsupportedOperationException();
            }

            @Override
            public void computeSecondaryDerivatives(double t, double[] y, double[] yDot) {
                // Not implemented
            }
        });
        integrator.integrate(equations, 0.0, initialValues, 1.0, new double[equations.getDimension()]);
    }

    @Test
    public void testAcceptStep() throws Exception {
        StepInterpolator interpolator = new StepInterpolator(new RungeKuttaMethod());
        StepHandler handler = new StepHandlerAdapter() {
            @Override
            public void handleStep(StepContext context, boolean lastStep) {
                assertEquals(MyIntegrator.class, context.getIntegrator().getClass());
                assertTrue(lastStep);
            }
        };
        integrator.addStepHandler(handler);

        double[] y = new double[equations.getDimension()];

        // Forward step
        integrator.acceptStep(interpolator, y, 1.1);
        handler.handleStep(interpolator.getStepContext(), true);
        assertEquals(1.1, integrator.getCurrentStepStart(), EPSILON);
        assertArrayEquals(expectedValues, y);

        // Backward step
        integrator.reset();
        integrator.acceptStep(interpolator.reverse(), y, 0.9);
        handler.handleStep(interpolator.getStepContext(), false);
        assertEquals(0.9, integrator.getCurrentStepStart(), EPSILON);
        assertArrayEquals(initialValues, y);
    }

    @Test
    public void testSanityChecks() throws Exception {
        ExpandableStatefulODE equations = new ExpandableStatefulODE() {
            @Override
            public double computeTimeDerivative(double t, double[] y) {
                return 0.0;
            }

            @Override
            public void computeSecondaryDerivatives(double t, double[] y, double[] yDot) {
                // Not implemented
            }
        };
        integrator.setExpandable(equations);

        assertThrows(NumberIsTooSmallException.class, () -> integrator.integrate(equations, 0.0, initialValues, 0.0, new double[equations.getDimension()]));
    }
}

class MyAbstractIntegrator extends AbstractIntegrator {
    public MyAbstractIntegrator(String name) {
        super(name);
    }

    @Override
    public void integrate(ExpandableStatefulODE p0, double p1) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
        // Implementation here
    }
}

class MyDifferentialEquations implements FirstOrderDifferentialEquations {
    @Override
    public int getDimension() {
        return 2;
    }

    @Override
    public double computeTimeDerivative(double t, double[] y) {
        return new double[] { y[1], y[0] };
    }
}

class MyEventHandler implements EventHandler {
    @Override
    public void handleEvent(double t, double[] y) {
        // Implementation here
    }
}

class MyEventHandler2 implements EventHandler {
    @Override
    public void handleEvent(double t, double[] y) {
        // Implementation here
    }

    @Override
    public Event[] getEvents() {
        return new Event[] { new MultipleStepsEvent(new double[] { 0.2 }, new Runnable() {
            @Override
            public void run() {
                // Implementation here
            }
        }) };
    }
}