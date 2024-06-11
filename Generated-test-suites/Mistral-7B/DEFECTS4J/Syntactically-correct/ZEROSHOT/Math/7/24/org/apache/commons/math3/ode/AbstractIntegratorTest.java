import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.ode.AbstractStepInterpolator;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.test.AbstractODETest;
import org.junit.Test;

public class AbstractIntegratorTest extends AbstractODETest {

    @Test
    public void testAbstractIntegrator() {
        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator") {
            @Override
            protected void integrate(ExpandableStatefulODE equations, double t)
                    throws NumberIsTooSmallException, DimensionMismatchException,
                           MaxCountExceededException, NoBracketingException {
                // empty integration method
            }
        };
        assertEquals("TestIntegrator", integrator.getName());
        assertNull(integrator.getStepHandlers());
        assertFalse(integrator.isLastStep());
        assertFalse(integrator.resetOccurred());
        assertNull(integrator.getEventsStates());
        assertFalse(integrator.getStateInitialized());
    }

    @Test
    public void testAbstractIntegratorAddStepHandlers() {
        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator") {
            @Override
            protected void integrate(ExpandableStatefulODE equations, double t)
                    throws NumberIsTooSmallException, DimensionMismatchException,
                           MaxCountExceededException, NoBracketingException {
                // empty integration method
            }
        };
        StepHandler stepHandler1 = new StepHandler() {
            @Override
            public void handleStep(AbstractStepInterpolator interpolator, boolean isLastStep) {
                // empty step handler
            }
        };
        integrator.addStepHandler(stepHandler1);
        StepHandler stepHandler2 = new StepHandler() {
            @Override
            public void handleStep(AbstractStepInterpolator interpolator, boolean isLastStep) {
                // empty step handler
            }
        };
        integrator.addStepHandler(stepHandler2);
        List<StepHandler> stepHandlers = integrator.getStepHandlers();
        assertFalse(stepHandlers.isEmpty());
        assertArrayEquals(new StepHandler[] { stepHandler1, stepHandler2 }, stepHandlers.toArray(new StepHandler[0]));
    }

    @Test
    public void testAbstractIntegratorAddEventHandler() {
        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator") {
            @Override
            protected void integrate(ExpandableStatefulODE equations, double t)
                    throws NumberIsTooSmallException, DimensionMismatchException,
                           MaxCountExceededException, NoBracketingException {
                // empty integration method
            }
        };
        FirstOrderDifferentialEquations equations = createMockDifferentialEquations();
        expander.setEquations(equations);
        integrator.initIntegration(0.0, expander.getPrimaryState(), 1.0);
        EventHandler eventHandler = new EventHandler() {
            @Override
            public void init(double t, double[] y, double[] yDot) {
                // empty initialization method
            }

            @Override
            public void stepAccepted(double t, double[] y) {
                // empty step acceptance method
            }

            @Override
            public double getEventTime() {
                return 0.5;
            }

            @Override
            public void reset(double t, double[] y) {
                // empty reset method
            }

            @Override
            public boolean evaluateStep(AbstractStepInterpolator interpolator) {
                return true;
            }

            @Override
            public void handleEvent(double t, double[] y, double[] yDot, double[] yDotNew) {
                // empty event handling method
            }
        };
        integrator.addEventHandler(eventHandler, 0.001, 1.0e-8, 10);
        List<EventHandler> eventHandlers = integrator.getEventHandlers();
        assertFalse(eventHandlers.isEmpty());
        assertEquals(eventHandler, eventHandlers.get(0));
    }

    @Test
    public void testAbstractIntegratorClearEventHandlers() {
        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator") {
            @Override
            protected void integrate(ExpandableStatefulODE equations, double t)
                    throws NumberIsTooSmallException, DimensionMismatchException,
                           MaxCountExceededException, NoBracketingException {
                // empty integration method
            }
        };
        FirstOrderDifferentialEquations equations = createMockDifferentialEquations();
        expander.setEquations(equations);
        integrator.initIntegration(0.0, expander.getPrimaryState(), 1.0);
        EventHandler eventHandler1 = new EventHandler() {
            // ...
        };
        integrator.addEventHandler(eventHandler1, 0.001, 1.0e-8, 10);
        EventHandler eventHandler2 = new EventHandler() {
            // ...
        };
        integrator.addEventHandler(eventHandler2, 0.002, 1.0e-8, 10);
        List<EventHandler> eventHandlers = integrator.getEventHandlers();
        assertFalse(eventHandlers.isEmpty());
        integrator.clearEventHandlers();
        eventHandlers = integrator.getEventHandlers();
        assertTrue(eventHandlers.isEmpty());
    }

    @Test
    public void testAbstractIntegratorGetCurrentStepStart() {
        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator") {
            @Override
            protected void integrate(ExpandableStatefulODE equations, double t)
                    throws NumberIsTooSmallException, DimensionMismatchException,
                           MaxCountExceededException, NoBracketingException {
                // empty integration method
            }
        };
        integrator.initIntegration(0.0, expander.getPrimaryState(), 1.0);
        double currentStepStart = integrator.getCurrentStepStart();
        assertEquals(Double.NaN, currentStepStart, 0.0);
        integrator.acceptStep(createMockStepInterpolator(), expander.getPrimaryState().clone(),
                new double[expander.getDimension()], 0.5);
        currentStepStart = integrator.getCurrentStepStart();
        assertEquals(0.0, currentStepStart, 0.0);
    }

    @Test
    public void testAbstractIntegratorGetCurrentSignedStepsize() {
        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator") {
            @Override
            protected void integrate(ExpandableStatefulODE equations, double t)
                    throws NumberIsTooSmallException, DimensionMismatchException,
                           MaxCountExceededException, NoBracketingException {
                // empty integration method
            }
        };
        integrator.initIntegration(0.0, expander.getPrimaryState(), 1.0);
        double currentSignedStepsize = integrator.getCurrentSignedStepsize();
        assertEquals(Double.NaN, currentSignedStepsize, 0.0);
        integrator.acceptStep(createMockStepInterpolator(), expander.getPrimaryState().clone(),
                new double[expander.getDimension()], 0.5);
        currentSignedStepsize = integrator.getCurrentSignedStepsize();
        assertEquals(-0.5, currentSignedStepsize, 0.0);
    }

    @Test
    public void testAbstractIntegratorGetEvaluations() {
        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator") {
            @Override
            protected void integrate(ExpandableStatefulODE equations, double t)
                    throws NumberIsTooSmallException, DimensionMismatchException,
                           MaxCountExceededException, NoBracketingException {
                // empty integration method
            }
        };
        integrator.initIntegration(0.0, expander.getPrimaryState(), 1.0);
        int evaluations = integrator.getEvaluations();
        assertEquals(0, evaluations);
        integrator.acceptStep(createMockStepInterpolator(), expander.getPrimaryState().clone(),
                new double[expander.getDimension()], 0.5);
        evaluations = integrator.getEvaluations();
        assertEquals(1, evaluations);
    }

    @Test
    public void testAbstractIntegratorIsLastStep() {
        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator") {
            @Override
            protected void integrate(ExpandableStatefulODE equations, double t)
                    throws NumberIsTooSmallException, DimensionMismatchException,
                           MaxCountExceededException, NoBracketingException {
                // empty integration method
            }
        };
        integrator.initIntegration(0.0, expander.getPrimaryState(), 1.0);
        assertFalse(integrator.isLastStep());
        integrator.acceptStep(createMockStepInterpolator(), expander.getPrimaryState().clone(),
                new double[expander.getDimension()], 0.5);
        assertFalse(integrator.isLastStep());
        integrator.acceptStep(createMockStepInterpolator(), expander.getPrimaryState().clone(),
                new double[expander.getDimension()], 0.5);
        assertTrue(integrator.isLastStep());
    }

    @Test
    public void testAbstractIntegratorResetOccurred() {
        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator") {
            @Override
            protected void integrate(ExpandableStatefulODE equations, double t)
                    throws NumberIsTooSmallException, DimensionMismatchException,
                           MaxCountExceededException, NoBracketingException {
                // empty integration method
            }
        };
        integrator.initIntegration(0.0, expander.getPrimaryState(), 1.0);
        assertFalse(integrator.resetOccurred());
        integrator.acceptStep(createMockStepInterpolator(), expander.getPrimaryState().clone(),
                new double[expander.getDimension()], 0.5);
        assertFalse(integrator.resetOccurred());
        integrator.acceptStep(createMockStepInterpolator(), expander.getPrimaryState().clone(),
                new double[expander.getDimension()], 0.5);
        integrator.acceptStep(createMockStepInterpolator(), expander.getPrimaryState().clone(),
                new double[expander.getDimension()], 0.5);
        EventHandler eventHandler = new EventHandler() {
            @Override
            public void stepAccepted(double t, double[] y) {
                integrator.reset();
            }
        };
        integrator.addEventHandler(eventHandler, 0.0, 0.0, 0);
        integrator.acceptStep(createMockStepInterpolator(), expander.getPrimaryState().clone(),
                new double[expander.getDimension()], 0.5);
        assertTrue(integrator.resetOccurred());
    }

    @Test
    public void testAbstractIntegratorGetEventsStates() {
        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator") {
            @Override
            protected void integrate(ExpandableStatefulODE equations, double t)
                    throws NumberIsTooSmallException, DimensionMismatchException,
                           MaxCountExceededException, NoBracketingException {
                // empty integration method
            }
        };
        integrator.initIntegration(0.0, expander.getPrimaryState(), 1.0);
        assertNull(integrator.getEventsStates());
        integrator.acceptStep(createMockStepInterpolator(), expander.getPrimaryState().clone(),
                new double[expander.getDimension()], 0.5);
        List<EventState> eventsStates = integrator.getEventsStates();
        assertFalse(eventsStates.isEmpty());
        EventState eventState = eventsStates.get(0);
        assertEquals(eventState.getEventHandler(), eventState.getEventHandler());
        assertEquals(eventState.getMaxCheckInterval(), eventState.getMaxCheckInterval(), 0.0);
        assertEquals(eventState.getConvergence(), eventState.getConvergence(), 0.0);
        assertEquals(eventState.getMaxIterationCount(), eventState.getMaxIterationCount());
        assertEquals(eventState.getSolver(), eventState.getSolver());
    }

    @Test
    public void testAbstractIntegratorSetStateInitialized() {
        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator") {
            @Override
            protected void integrate(ExpandableStatefulODE equations, double t)
                    throws NumberIsTooSmallException, DimensionMismatchException,
                           MaxCountExceededException, NoBracketingException {
                // empty integration method
            }
        };
        integrator.initIntegration(0.0, expander.getPrimaryState(), 1.0);
        assertFalse(integrator.getStateInitialized());
        integrator.setStateInitialized(true);
        assertTrue(integrator.getStateInitialized());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAbstractIntegratorNegativeMaxEvaluations() {
        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator") {
            @Override
            protected void integrate(ExpandableStatefulODE equations, double t)
                    throws NumberIsTooSmallException, DimensionMismatchException,
                           MaxCountExceededException, NoBracketingException {
                // empty integration method
            }
        };
        integrator.initIntegration(0.0, expander.getPrimaryState(), 1.0);
        integrator.setMaxEvaluations(-1);
    }

    @Test
    public void testAbstractIntegratorMaxEvaluations() {
        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator") {
            @Override
            protected void integrate(ExpandableStatefulODE equations, double t)
                    throws NumberIsTooSmallException, DimensionMismatchException,
                           MaxCountExceededException, NoBracketingException {
                // empty integration method
            }
        };
        integrator.initIntegration(0.0, expander.getPrimaryState(), 1.0);
        integrator.setMaxEvaluations(1);
        assertEquals(1, integrator.getMaxEvaluations());
        integrator.acceptStep(createMockStepInterpolator(), expander.getPrimaryState().clone(),
                new double[expander.getDimension()], 0.5);
        integrator.acceptStep(createMockStepInterpolator(), expander.getPrimaryState().clone(),
                new double[expander.getDimension()], 0.5);
        assertFalse(integrator.hasReachedMaxEvaluations());
        integrator.acceptStep(createMockStepInterpolator(), expander.getPrimaryState().clone(),
                new double[expander.getDimension()], 0.5);
        assertTrue(integrator.hasReachedMaxEvaluations());
    }

}