import static org.junit.Assert.*;
import org.apache.commons.math3.analysis.solvers.BracketingNthOrderBrentSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateRealSolver;
import org.apache.commons.math3.ode.AbstractStepInterpolator;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.AbstractEventHandlerAdapter;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

public class AbstractIntegratorTest {

    @Test
    public void testAbstractIntegrator() {
        String name = "TestIntegrator";
        AbstractIntegrator integrator = new AbstractIntegrator(name);

        // Test adding and getting StepHandlers
        StepHandler handler1 = new StepHandler() {
            @Override
            public void handleStep(StepInterpolator interpolator, boolean isLastStep) {
                System.out.println("StepHandler1 handled step " + isLastStep);
            }
        };
        integrator.addStepHandler(handler1);
        List<StepHandler> stepHandlers = integrator.getStepHandlers();
        assertEquals(1, stepHandlers.size());
        assertEquals(handler1, stepHandlers.get(0));

        // Test clearing StepHandlers
        integrator.clearStepHandlers();
        stepHandlers = integrator.getStepHandlers();
        assertEquals(0, stepHandlers.size());

        // Test adding and getting EventHandlers
        AbstractEventHandlerAdapter eventHandler1 = new AbstractEventHandlerAdapter() {
            @Override
            public void handleEvent(double t, double[] y) {
                System.out.println("EventHandler1 handled event at " + t);
            }
        };
        integrator.addEventHandler(eventHandler1, 0.1, 1e-5, 10);
        List<EventHandler> eventHandlers = integrator.getEventHandlers();
        assertEquals(1, eventHandlers.size());
        assertEquals(eventHandler1, eventHandlers.get(0));

        // Test clearing EventHandlers
        integrator.clearEventHandlers();
        eventHandlers = integrator.getEventHandlers();
        assertEquals(0, eventHandlers.size());

        // Test getting and setting name
        assertEquals(name, integrator.getName());
        integrator.setName("NewName");
        assertEquals("NewName", integrator.getName());

        // Test getting and setting MaxEvaluations
        integrator.setMaxEvaluations(10);
        assertEquals(10, integrator.getMaxEvaluations());
        integrator.setMaxEvaluations(-1);
        assertEquals(-1, integrator.getMaxEvaluations());

        // Test getting and resetting Evaluations
        integrator.integrate(new DummyDifferentialEquations(), 0.0, new double[0], 1.0, new double[0]);
        assertEquals(1, integrator.getEvaluations());
        integrator.resetEvaluations();
        assertEquals(0, integrator.getEvaluations());

        // Test setting Equations
        ExpandableStatefulODE equations = new DummyExpandableStatefulODE();
        integrator.setEquations(equations);
        assertEquals(equations, integrator.getEquations());

        // Test integrating with given equations, initial time and initial state
        double t = integrator.integrate(equations, 0.0, new double[0], 1.0, new double[0]) - 0.0;
        assertEquals(1.0, t, 1e-5);

        // Test integrating with given equations, initial time, initial state and target time
        t = integrator.integrate(equations, 0.0, new double[0], 1.5, new double[0]) - 1.0;
        assertEquals(0.5, t, 1e-5);

        // Test integrating with given equations, initial time, initial state, target time and stepping interpolator
        StepInterpolator interpolator = new AbstractStepInterpolator() {};
        integrator.integrate(equations, 0.0, new double[0], 1.5, new double[0], interpolator);
        assertTrue(interpolator.isLastStep());

        // Test integrating with given equations, initial time, initial state, target time, stepping interpolator and event handlers
        integrator.addEventHandler(eventHandler1, 0.1, 1e-5, 10, new BracketingNthOrderBrentSolver(1e-5, 5));
        integrator.integrate(equations, 0.0, new double[0], 1.5, new double[0], interpolator);
        assertTrue(eventHandler1.isEventOccurred());

        // Test accepting a step with given interpolator, state, derivatives and final time
        interpolator.setSoftCurrentTime(0.5);
        interpolator.setSoftPreviousTime(0.0);
        double[] state = new double[equations.getDimension()];
        double[] derivatives = new double[equations.getDimension()];
        integrator.acceptStep(interpolator, state, derivatives, 0.5);
        assertTrue(integrator.isLastStep());

        // Test sanity checks
        integrator = new AbstractIntegrator();
        equations = new DummyExpandableStatefulODE();
        equations.setTime(0.01);
        try {
            integrator.integrate(equations, 0.0, new double[0], 0.001, new double[0]);
            fail("NumberIsTooSmallException not thrown");
        } catch (NumberIsTooSmallException e) {
            // expected
        }

    }

    /**
     * Dummy differential equations for testing.
     */
    static class DummyDifferentialEquations implements FirstOrderDifferentialEquations {
        @Override
        public int getDimension() {
            return 1;
        }

        @Override
        public void computeDerivatives(double t, double[] y, double[] yDot) {
            yDot[0] = y[0];
        }

        @Override
        public void setTime(double time) {
            // no-op
        }

        @Override
        public void setPrimaryState(double[] state) {
            // no-op
        }

        @Override
        public double getTime() {
            return 0.0;
        }

        @Override
        public double[] getPrimaryState() {
            return new double[0];
        }
    }

    /**
     * Dummy expandable stateful equations for testing.
     */
    static class DummyExpandableStatefulODE extends ExpandableStatefulODE {
        @Override
        public void computeDerivatives(double t, double[] y, double[] yDot) {
            yDot[0] = y[0];
        }

        @Override
        public void setTime(double time) {
            super.setTime(time);
        }

        @Override
        public void setPrimaryState(double[] state) {
            super.setPrimaryState(state);
        }

        @Override
        public double getTime() {
            return super.getTime();
        }

        @Override
        public double[] getPrimaryState() {
            return super.getPrimaryState();
        }
    }

}