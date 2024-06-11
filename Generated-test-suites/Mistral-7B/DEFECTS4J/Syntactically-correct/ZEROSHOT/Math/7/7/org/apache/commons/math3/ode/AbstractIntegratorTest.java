import static org.junit.Assert.*;
import org.apache.commons.math3.ode.AbstractIntegrator;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.sampling.AbstractStepInterpolator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.junit.Test;

/**
 * Test file for {@link AbstractIntegrator} class.
 */
public class AbstractIntegratorTest {

    /**
     * Test method for {@link AbstractIntegrator#AbstractIntegrator(String)} .
     */
    @Test
    public void testAbstractIntegratorConstruction() {
        AbstractIntegrator integrator = new AbstractIntegrator("EulerIntegrator");
        assertNotNull(integrator);
        assertEquals("EulerIntegrator", integrator.getName());
        assertTrue(integrator.getEventHandlers().isEmpty());
        assertEquals(0, integrator.getMaxEvaluations());
        assertEquals(0, integrator.getEvaluations());
    }

    /**
     * Test method for {@link AbstractIntegrator#AbstractIntegrator()} .
     */
    @Test
    public void testAbstractIntegratorNullNameConstruction() {
        AbstractIntegrator integrator = new AbstractIntegrator();
        assertNotNull(integrator);
        assertEquals(null, integrator.getName());
    }

    /**
     * Test method for {@link AbstractIntegrator#addEventHandler(EventHandler, double, double, int, UnivariateSolver)}.
     */
    @Test
    public void testAddEventHandler() {
        AbstractIntegrator integrator = new AbstractIntegrator("RungeKuttaIntegrator");
        MyEventHandler eventHandler = new MyEventHandler();
        integrator.addEventHandler(eventHandler, 0.1, 1e-5, 5, new BracketingNthOrderBrentSolver(1e-5, 5));
        assertTrue(integrator.getEventHandlers().contains(eventHandler));
    }

    /**
     * Test method for {@link AbstractIntegrator#clearEventHandlers()}.
     */
    @Test
    public void testClearEventHandlers() {
        AbstractIntegrator integrator = new AbstractIntegrator("RungeKuttaIntegrator");
        MyEventHandler eventHandler1 = new MyEventHandler();
        integrator.addEventHandler(eventHandler1, 0.1, 1e-5, 5, new BracketingNthOrderBrentSolver(1e-5, 5));
        MyEventHandler eventHandler2 = new MyEventHandler();
        integrator.addEventHandler(eventHandler2, 0.2, 1e-6, 10, new BracketingNthOrderBrentSolver(1e-6, 10));
        assertTrue(integrator.getEventHandlers().size() == 2);
        integrator.clearEventHandlers();
        assertTrue(integrator.getEventHandlers().isEmpty());
    }

    /**
     * Test method for {@link AbstractIntegrator#getEventHandlers()}.
     */
    @Test
    public void testGetEventHandlers() {
        AbstractIntegrator integrator = new AbstractIntegrator("RungeKuttaIntegrator");
        MyEventHandler eventHandler1 = new MyEventHandler();
        integrator.addEventHandler(eventHandler1, 0.1, 1e-5, 5, new BracketingNthOrderBrentSolver(1e-5, 5));
        MyEventHandler eventHandler2 = new MyEventHandler();
        integrator.addEventHandler(eventHandler2, 0.2, 1e-6, 10, new BracketingNthOrderBrentSolver(1e-6, 10));
        List<EventHandler> eventHandlers = integrator.getEventHandlers();
        assertTrue(eventHandlers.size() == 2);
        assertTrue(eventHandlers.contains(eventHandler1));
        assertTrue(eventHandlers.contains(eventHandler2));
    }

    /**
     * Test method for {@link AbstractIntegrator#getDefaultStepSize()}.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetDefaultStepSize() {
        AbstractIntegrator integrator = new AbstractIntegrator("RungeKuttaIntegrator");
        integrator.getDefaultStepSize();
    }

    /**
     * Test method for {@link AbstractIntegrator#integrate(FirstOrderDifferentialEquations, double, double[], double, double[])}.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testIntegrate() {
        AbstractIntegrator integrator = new AbstractIntegrator("RungeKuttaIntegrator");
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[0] * 2;
            }
        };
        double[] y0 = { 1.0 };
        double t = 1.0;
        double[] y = { 0.0 };
        integrator.integrate(equations, 0.0, y0, t, y);
    }

    /**
     * Test method for {@link AbstractIntegrator#setEquations(ExpandableStatefulODE)}.
     */
    @Test(expected = NullPointerException.class)
    public void testSetEquationsNull() {
        AbstractIntegrator integrator = new AbstractIntegrator("RungeKuttaIntegrator");
        integrator.setEquations(null);
    }

    /**
     * Test method for {@link AbstractIntegrator#integrateStep(AbstractStepInterpolator, double[], double[], double[])}.
     */
    @Test
    public void testIntegrateStep() {
        AbstractIntegrator integrator = new AbstractIntegrator("RungeKuttaIntegrator") {
            @Override
            protected void integrate(ExpandableStatefulODE equations, double t)
                    throws NumberIsTooSmallException, DimensionMismatchException,
                           MaxCountExceededException, NoBracketingException {
                // empty implementation
            }
        };
        MyDifferentialEquations equations = new MyDifferentialEquations();
        double[] y0 = { 1.0 };
        double t0 = 0.0;
        double t = 1.0;
        double[] y = { 0.0 };
        AbstractStepInterpolator interpolator = new AbstractStepInterpolator() {
            @Override
            public double getGlobalPreviousTime() {
                return 0.0;
            }

            @Override
            public double getGlobalCurrentTime() {
                return 0.5;
            }

            @Override
            public void setGlobalCurrentTime(double time) {
                // empty implementation
            }

            @Override
            public void setSoftPreviousTime(double time) {
                // empty implementation
            }

            @Override
            public void setSoftCurrentTime(double time) {
                // empty implementation
            }

            @Override
            public void setInterpolatedTime(double time) {
                // empty implementation
            }

            @Override
            public double[] getInterpolatedState() {
                return y0;
            }
        };
        integrator.setEquations(equations);
        integrator.initIntegration(t0, y0, t);
        double acceptedStep = integrator.acceptStep(interpolator, y, null, t);
        assertEquals(0.5, acceptedStep, 1e-8);
    }

    /**
     * Test method for {@link AbstractIntegrator#setMaxEvaluations(int)}.
     */
    @Test
    public void testSetMaxEvaluations() {
        AbstractIntegrator integrator = new AbstractIntegrator("RungeKuttaIntegrator");
        integrator.setMaxEvaluations(10);
        assertEquals(10, integrator.getMaxEvaluations());
    }

    /**
     * Test method for {@link AbstractIntegrator#getMaxEvaluations()}.
     */
    @Test
    public void testGetMaxEvaluations() {
        AbstractIntegrator integrator = new AbstractIntegrator("RungeKuttaIntegrator");
        integrator.setMaxEvaluations(10);
        assertEquals(10, integrator.getMaxEvaluations());
    }

    /**
     * Test method for {@link AbstractIntegrator#getEvaluations()}.
     */
    @Test
    public void testGetEvaluations() {
        AbstractIntegrator integrator = new AbstractIntegrator("RungeKuttaIntegrator");
        assertEquals(0, integrator.getEvaluations());
    }

    /**
     * Test method for {@link AbstractIntegrator#setStateInitialized()}.
     */
    @Test
    public void testGetStateInitialized() {
        AbstractIntegrator integrator = new AbstractIntegrator("RungeKuttaIntegrator");
        assertFalse(integrator.isStateInitialized());
    }

    /**
     * Test method for {@link AbstractIntegrator#setStateInitialized(boolean)}.
     */
    @Test
    public void testSetStateInitialized() {
        AbstractIntegrator integrator = new AbstractIntegrator("RungeKuttaIntegrator");
        integrator.setStateInitialized(true);
        assertTrue(integrator.isStateInitialized());
    }

    /**
     * Test method for {@link AbstractIntegrator#isLastStep()}.
     */
    @Test
    public void testIsLastStep() {
        AbstractIntegrator integrator = new AbstractIntegrator("RungeKuttaIntegrator");
        assertFalse(integrator.isLastStep());
    }

    /**
     * Test method for {@link AbstractIntegrator#resetOccurred()}.
     */
    @Test
    public void testResetOccurred() {
        AbstractIntegrator integrator = new AbstractIntegrator("RungeKuttaIntegrator");
        assertFalse(integrator.isResetOccurred());
    }

    /**
     * Test method for {@link AbstractIntegrator#setResetOccurred(boolean)}.
     */
    @Test
    public void testSetResetOccurred() {
        AbstractIntegrator integrator = new AbstractIntegrator("RungeKuttaIntegrator");
        integrator.setResetOccurred(true);
        assertTrue(integrator.isResetOccurred());
    }

    /**
     * Test class {@link MyEventHandler} which extends {@link EventHandler}.
     */
    public static class MyEventHandler implements EventHandler {

        /** The event flag. */
        private boolean eventOccurred;

        /**
         * Constructor.
         */
        public MyEventHandler() {
            eventOccurred = false;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void handleStateChange(double t, double[] y) {
            // empty implementation
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void handleEvent(double t, double[] y, double eventTime) {
            eventOccurred = true;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void init(double t, double[] y, double eventTime) {
            // empty implementation
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean evaluateStep(AbstractStepInterpolator interpolator) {
            return false;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void stepAccepted(double t, double[] y) {
            // empty implementation
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean stop() {
            return eventOccurred;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void reinitializeBegin(AbstractStepInterpolator interpolator) {
            // empty implementation
        }
    }

    /**
     * Test class {@link MyDifferentialEquations} which implements {@link FirstOrderDifferentialEquations}.
     */
    public static class MyDifferentialEquations implements FirstOrderDifferentialEquations {

        /** The dimension. */
        private static final int DIMENSION = 1;

        /**
         * {@inheritDoc}
         */
        @Override
        public int getDimension() {
            return DIMENSION;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void computeDerivatives(double t, double[] y, double[] yDot) {
            yDot[0] = y[0] * 2;
        }
    }

}