import org.apache.commons.math3.analysis.solvers.BracketingNthOrderBrentSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateRealSolver;
import org.apache.commons.math3.ode.AbstractStepInterpolator;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.events.EventState;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AbstractIntegratorTest {

    private AbstractIntegrator integrator;

    @Before
    public void setUp() {
        integrator = new AbstractIntegrator() {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t)
                    throws MathIllegalStateException, MathIllegalArgumentException {
                // no implementation needed
            }
        };
    }

    @After
    public void tearDown() {
        integrator = null;
    }

    @Test
    public void testAddStepHandler() {
        integrator.addStepHandler(new StepHandler() {
            @Override
            public void handleStep(AbstractStepInterpolator interpolator, boolean isLastStep) {
                // no implementation needed
            }
        });
        assertFalse(integrator.getStepHandlers().isEmpty());
    }

    @Test
    public void testGetStepHandlers() {
        integrator.addStepHandler(new StepHandler() {
            @Override
            public void handleStep(AbstractStepInterpolator interpolator, boolean isLastStep) {
                // no implementation needed
            }
        });
        integrator.addStepHandler(new StepHandler() {
            @Override
            public void handleStep(AbstractStepInterpolator interpolator, boolean isLastStep) {
                // no implementation needed
            }
        });
        List<StepHandler> handlers = integrator.getStepHandlers();
        assertEquals(2, handlers.size());
    }

    @Test
    public void testClearStepHandlers() {
        integrator.addStepHandler(new StepHandler() {
            @Override
            public void handleStep(AbstractStepInterpolator interpolator, boolean isLastStep) {
                // no implementation needed
            }
        });
        integrator.clearStepHandlers();
        assertTrue(integrator.getStepHandlers().isEmpty());
    }

    @Test
    public void testAddEventHandler() {
        integrator.addEventHandler(new EventHandler() {
            @Override
            public void handleEvent(double t, double[] state) {
                // no implementation needed
            }
        }, 0.0, 1.0, 1, new BracketingNthOrderBrentSolver(1.0, 5));
        assertFalse(integrator.getEventHandlers().isEmpty());
    }

    @Test
    public void testGetEventHandlers() {
        integrator.addEventHandler(new EventHandler() {
            @Override
            public void handleEvent(double t, double[] state) {
                // no implementation needed
            }
        }, 0.0, 1.0, 1, new BracketingNthOrderBrentSolver(1.0, 5));
        integrator.addEventHandler(new EventHandler() {
            @Override
            public void handleEvent(double t, double[] state) {
                // no implementation needed
            }
        }, 0.0, 1.0, 1, new BracketingNthOrderBrentSolver(1.0, 5));
        List<EventHandler> handlers = integrator.getEventHandlers();
        assertEquals(2, handlers.size());
    }

    @Test
    public void testClearEventHandlers() {
        integrator.addEventHandler(new EventHandler() {
            @Override
            public void handleEvent(double t, double[] state) {
                // no implementation needed
            }
        }, 0.0, 1.0, 1, new BracketingNthOrderBrentSolver(1.0, 5));
        integrator.clearEventHandlers();
        assertTrue(integrator.getEventHandlers().isEmpty());
    }

    @Test
    public void testGetCurrentStepStart() {
        assertNaN(integrator.getCurrentStepStart());
    }

    @Test
    public void testSetMaxEvaluations() {
        integrator.setMaxEvaluations(10);
        assertEquals(10, integrator.getMaxEvaluations());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMaxEvaluationsNegative() {
        integrator.setMaxEvaluations(-1);
    }

    @Test
    public void testGetEvaluations() {
        reset(integrator);
        integrator.integrate(new StiffODESystem(), 0.0, new double[2], 1.0, new double[2]);
        assertEquals(1, integrator.getEvaluations());
    }

    @Test
    public void testResetEvaluations() {
        integrator.integrate(new StiffODESystem(), 0.0, new double[2], 1.0, new double[2]);
        integrator.resetEvaluations();
        assertEquals(0, integrator.getEvaluations());
    }

    @Test
    public void testSetEquations() {
        ExpandableStatefulODE equations = new ExpandableStatefulODE(new StiffODESystem());
        integrator.setEquations(equations);
        assertEquals(equations, integrator.getEquations());
    }

    private void reset(AbstractIntegrator integrator) {
        integrator.stepStart = Double.NaN;
        integrator.stepSize = Double.NaN;
        integrator.isLastStep = false;
        integrator.resetOccurred = false;
        integrator.eventsStates = new ArrayList<>();
        integrator.statesInitialized = false;
    }

}

class StiffODESystem implements FirstOrderDifferentialEquations {

    @Override
    public int getDimension() {
        return 2;
    }

    @Override
    public void computeDerivatives(double t, double[] y, double[] yDot) {
        yDot[0] = y[1];
        yDot[1] = -0.01 * y[0] + 0.02 * y[1];
    }

}