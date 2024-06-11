import static org.junit.Assert.*;
import org.apache.commons.math3.analysis.solvers.BracketingNthOrderBrentSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateRealSolver;
import org.apache.commons.math3.ode.AbstractStepInterpolator;
import org.apache.commons.math3.ode.EventState;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.StepHandler;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.sampling.StepHandlerAdapter;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for the AbstractIntegrator class.
 */
public class AbstractIntegratorTest {

    /** Integrator under test. */
    private AbstractIntegrator integrator;

    /** Differential equations to integrate. */
    private FirstOrderDifferentialEquations equations;

    /** Step handler. */
    private StepHandler stepHandler;

    /** Event handler. */
    private EventHandler eventHandler;

    /**
     * Set up the test.
     */
    @Before
    public void setUp() {
        integrator = new AbstractIntegrator() {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t)
                    throws MathIllegalStateException, MathIllegalArgumentException {
                // do nothing
            }
        };
        equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = 1.0;
            }
        };
        stepHandler = new StepHandlerAdapter() {
            @Override
            public void handleStep(AbstractIntegrator integrator, boolean isLastStep) {
                assertEquals("Unexpected last step indicator.", isLastStep, integrator.isLastStep());
            }
        };
        integrator.addStepHandler(stepHandler);
        eventHandler = new EventHandler() {
            @Override
            public void stepAccepted(double t, double[] y) {
                integrator.setLastStep(true);
            }

            @Override
            public boolean stop() {
                return true;
            }
        };
        integrator.addEventHandler(eventHandler, 0.1, 1.0e-9, 10);
    }

    /**
     * Test method for {@link AbstractIntegrator#AbstractIntegrator(String)}.
     */
    @Test
    public void testConstructor() {
        // no test as constructor does not do anything specific
    }

    /**
     * Test method for {@link AbstractIntegrator#addEventHandler(EventHandler, double, double, int, UnivariateRealSolver)}.
     */
    @Test
    public void testAddEventHandler() {
        integrator.addEventHandler(eventHandler, 0.1, 1.0e-9, 10, new BracketingNthOrderBrentSolver(1.0e-9, 5));
        assertEquals("Unexpected number of event handlers.", 1, integrator.getEventHandlers().size());
    }

    /**
     * Test method for {@link AbstractIntegrator#addEventHandler(EventHandler, double, double, int)}.
     */
    @Test
    public void testAddEventHandlerNoSolver() {
        integrator.addEventHandler(eventHandler, 0.1, 1.0e-9, 10);
        assertEquals("Unexpected number of event handlers.", 1, integrator.getEventHandlers().size());
    }

    /**
     * Test method for {@link AbstractIntegrator#addStepHandler(StepHandler)}.
     */
    @Test
    public void testAddStepHandler() {
        integrator.addStepHandler(stepHandler);
        assertEquals("Unexpected number of step handlers.", 1, integrator.getStepHandlers().size());
    }

    /**
     * Test method for {@link AbstractIntegrator#clearEventHandlers()}.
     */
    @Test
    public void testClearEventHandlers() {
        integrator.addEventHandler(eventHandler, 0.1, 1.0e-9, 10, new BracketingNthOrderBrentSolver(1.0e-9, 5));
        integrator.clearEventHandlers();
        assertEquals("Unexpected number of event handlers.", 0, integrator.getEventHandlers().size());
    }

    /**
     * Test method for {@link AbstractIntegrator#clearStepHandlers()}.
     */
    @Test
    public void testClearStepHandlers() {
        integrator.addStepHandler(stepHandler);
        integrator.clearStepHandlers();
        assertEquals("Unexpected number of step handlers.", 0, integrator.getStepHandlers().size());
    }

    /**
     * Test method for {@link AbstractIntegrator#computeDerivatives(double, double[], double[])}.
     * @throws MaxCountExceededException
     */
    @Test(expected = MaxCountExceededException.class)
    public void testComputeDerivativesMaxEvaluationsExceeded()
            throws MaxCountExceededException {
        integrator.setMaxEvaluations(0);
        integrator.computeDerivatives(0.0, new double[0], new double[0]);
    }

    /**
     * Test method for {@link AbstractIntegrator#getEventHandlers()}.
     */
    @Test
    public void testGetEventHandlers() {
        integrator.addEventHandler(eventHandler, 0.1, 1.0e-9, 10, new BracketingNthOrderBrentSolver(1.0e-9, 5));
        assertEquals("Unexpected number of event handlers.", 1, integrator.getEventHandlers().size());
    }

    /**
     * Test method for {@link AbstractIntegrator#getEvaluations()}.
     */
    @Test
    public void testGetEvaluations() {
        integrator.setMaxEvaluations(5);
        integrator.computeDerivatives(0.0, new double[0], new double[0]);
        integrator.computeDerivatives(0.0, new double[0], new double[0]);
        integrator.computeDerivatives(0.0, new double[0], new double[0]);
        integrator.computeDerivatives(0.0, new double[0], new double[0]);
        integrator.computeDerivatives(0.0, new double[0], new double[0]);
        assertEquals("Unexpected number of evaluations.", 5, integrator.getEvaluations());
    }

    /**
     * Test method for {@link AbstractIntegrator#getMaxEvaluations()}.
     */
    @Test
    public void testGetMaxEvaluations() {
        integrator.setMaxEvaluations(10);
        assertEquals("Unexpected maximum number of evaluations.", 10, integrator.getMaxEvaluations());
    }

    /**
     * Test method for {@link AbstractIntegrator#getStepHandlers()}.
     */
    @Test
    public void testGetStepHandlers() {
        integrator.addStepHandler(stepHandler);
        assertEquals("Unexpected number of step handlers.", 1, integrator.getStepHandlers().size());
    }

    /**
     * Test method for {@link AbstractIntegrator#integrate(FirstOrderDifferentialEquations, double, double[], double, double[])}.
     */
    @Test
    public void testIntegrate() throws Exception {
        integrator.setEquations(equations);
        integrator.integrate(equations, 0.0, new double[1], 1.0, new double[1]);
        assertEquals("Unexpected integration result.", 1.0, integrator.getCurrentStepStart(), 1.0e-12);
    }

    /**
     * Test method for {@link AbstractIntegrator#isLastStep()}.
     */
    @Test
    public void testIsLastStep() {
        integrator.setLastStep(false);
        assertFalse("Unexpected last step indicator.", integrator.isLastStep());
        integrator.setLastStep(true);
        assertTrue("Unexpected last step indicator.", integrator.isLastStep());
    }

    /**
     * Test method for {@link AbstractIntegrator#setEquations(ExpandableStatefulODE)}.
     */
    @Test
    public void testSetEquations() {
        integrator.setEquations(equations);
        assertNotNull(" unexpected equations.", integrator.getEquations());
    }

    /**
     * Test method for {@link AbstractIntegrator#setLastStep(boolean)}.
     */
    @Test
    public void testSetLastStep() {
        integrator.setLastStep(false);
        assertFalse("Unexpected last step indicator.", integrator.isLastStep());
        integrator.setLastStep(true);
        assertTrue("Unexpected last step indicator.", integrator.isLastStep());
    }

    /**
     * Test method for {@link AbstractIntegrator#setMaxEvaluations(int)}.
     */
    @Test
    public void testSetMaxEvaluations() {
        integrator.setMaxEvaluations(5);
        assertEquals("Unexpected maximum number of evaluations.", 5, integrator.getMaxEvaluations());
    }

    /**
     * Test method for {@link AbstractIntegrator#setMaxEvaluations(int)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetMaxEvaluationsNegativeNumber() {
        integrator.setMaxEvaluations(-1);
    }

    /**
     * Test method for {@link AbstractIntegrator#setMaxEvaluations(int)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetMaxEvaluationsZero() {
        integrator.setMaxEvaluations(0);
    }

    /**
     * Test method for {@link AbstractIntegrator#setMaxEvaluations(int)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetMaxEvaluationsNull() {
        integrator.setMaxEvaluations(null);
    }

    /**
     * Test method for {@link AbstractIntegrator#setMaxEvaluations(int)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetMaxEvaluationsNonNumber() {
        integrator.setMaxEvaluations("test");
    }

    /**
     * Test method for {@link AbstractIntegrator#setStepSize(double)}.
     */
    @Test
    public void testSetStepSize() {
        integrator.setStepSize(1.0);
        assertEquals("Unexpected step size.", 1.0, integrator.getCurrentSignedStepsize(), 1.0e-12);
    }

    /**
     * Test method for {@link AbstractIntegrator#setStepSize(double)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetStepSizeNegativeNumber() {
        integrator.setStepSize(-1.0);
    }

    /**
     * Test method for {@link AbstractIntegrator#setStepSize(double)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetStepSizeZero() {
        integrator.setStepSize(0.0);
    }

    /**
     * Test method for {@link AbstractIntegrator#setStepSize(double)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetStepSizeNull() {
        integrator.setStepSize(null);
    }

    /**
     * Test method for {@link AbstractIntegrator#setStepSize(double)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetStepSizeNonNumber() {
        integrator.setStepSize("test");
    }

    /**
     * Test method for {@link AbstractIntegrator#step(double, double[], double[], double[])}.
     * @throws MathIllegalStateException
     * @throws MathIllegalArgumentException
     */
    @Test
    public void testStep() throws Exception {
        integrator.setEquations(equations);
        integrator.setStepSize(0.1);
        integrator.integrate(equations, 0.0, new double[1], 1.0, new double[1]);
        integrator.step(0.1, new double[1], new double[1], new double[1]);
        assertEquals("Unexpected integration result.", 1.1, integrator.getCurrentStepStart(), 1.0e-12);
    }

}