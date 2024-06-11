import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.ode.events.*;
import org.apache.commons.math3.ode.sampling.*;
import org.apache.commons.math3.exception.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class AbstractIntegratorTest {

    @Test
    public void testConstructor() {
        AbstractIntegrator integrator = new AbstractIntegrator("Integrator1");
        assertEquals("Integrator1", integrator.getName());
        assertNotNull(integrator.getStepHandlers());
        assertTrue(integrator.getStepHandlers().isEmpty());
        assertNull(integrator.getEvaluationsCounter());
        assertNull(integrator.getExpandable());
        assertTrue(integrator.getMaxEvaluations() == -1);
        assertTrue(integrator.getEvaluations() == 0);
    }

    @Test
    public void testAddStepHandler() {
        AbstractIntegrator integrator = new AbstractIntegrator("Integrator1");
        StepHandler handler = new MockStepHandler();
        integrator.addStepHandler(handler);
        assertTrue(integrator.getStepHandlers().contains(handler));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testClearStepHandlers() {
        AbstractIntegrator integrator = new AbstractIntegrator("Integrator1");
        integrator.clearStepHandlers();
        assertTrue(integrator.getStepHandlers().isEmpty());
        integrator.addStepHandler(new MockStepHandler());
        integrator.clearStepHandlers();
        assertTrue(integrator.getStepHandlers().isEmpty());
        try {
            integrator.addStepHandler(new MockStepHandler());
        } catch (UnsupportedOperationException e) {
            throw new AssertionError("Unexpected exception", e);
        }
    }

    @Test
    public void testAddEventHandler() {
        AbstractIntegrator integrator = new AbstractIntegrator("Integrator1");
        EventHandler handler = new MockEventHandler();
        integrator.addEventHandler(handler, 1.0, 1.0, 10);
        assertTrue(integrator.getEventHandlers().contains(handler));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddEventHandlerInvalidArguments() {
        AbstractIntegrator integrator = new AbstractIntegrator("Integrator1");
        EventHandler handler = new MockEventHandler();
        integrator.addEventHandler(handler, 1.0, 1.0, -10);
    }

    @Test
    public void testGetCurrentStepStart() {
        AbstractIntegrator integrator = new AbstractIntegrator("Integrator1");
        double currentStepStart = integrator.getCurrentStepStart();
        assertEquals(Double.NaN, currentStepStart);
    }

    @Test
    public void testGetCurrentSignedStepsize() {
        AbstractIntegrator integrator = new AbstractIntegrator("Integrator1");
        double currentSignedStepsize = integrator.getCurrentSignedStepsize();
        assertEquals(Double.NaN, currentSignedStepsize);
    }

    @Test
    public void testSetMaxEvaluations() {
        AbstractIntegrator integrator = new AbstractIntegrator("Integrator1");
        integrator.setMaxEvaluations(5);
        assertEquals(5, integrator.getMaxEvaluations());
    }

    @Test
    public void testGetMaxEvaluations() {
        AbstractIntegrator integrator = new AbstractIntegrator("Integrator1");
        integrator.setMaxEvaluations(5);
        assertEquals(5, integrator.getMaxEvaluations());
        integrator.setMaxEvaluations(-1);
        assertEquals(-1, integrator.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        AbstractIntegrator integrator = new AbstractIntegrator("Integrator1");
        assertEquals(0, integrator.getEvaluations());
    }

    @Test
    public void testIntegrate() throws Exception {
        AbstractIntegrator integrator = new AbstractIntegrator("Integrator1");
        FirstOrderDifferentialEquations equations = new MockDifferentialEquations();
        double t0 = 0.0;
        double[] y0 = new double[]{1.0};
        double t = 1.0;
        double[] y = new double[]{2.0};
        integrator.integrate(equations, t0, y0, t, y);
        assertEquals(1.0, y[0], 1e-6);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testIntegrateDimensionMismatch() throws Exception {
        AbstractIntegrator integrator = new AbstractIntegrator("Integrator1");
        FirstOrderDifferentialEquations equations = new MockDifferentialEquations();
        double t0 = 0.0;
        double[] y0 = new double[]{1.0, 2.0};
        double t = 1.0;
        double[] y = new double[]{2.0};
        integrator.integrate(equations, t0, y0, t, y);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testIntegrateNumberIsTooSmall() throws Exception {
        AbstractIntegrator integrator = new AbstractIntegrator("Integrator1");
        FirstOrderDifferentialEquations equations = new MockDifferentialEquations();
        double t0 = 0.0;
        double[] y0 = new double[]{1.0};
        double t = 1.0e-10;
        double[] y = new double[]{2.0};
        integrator.integrate(equations, t0, y0, t, y);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testIntegrateMaxEvaluations() throws Exception {
        AbstractIntegrator integrator = new AbstractIntegrator("Integrator1");
        FirstOrderDifferentialEquations equations = new MockDifferentialEquations();
        double t0 = 0.0;
        double[] y0 = new double[]{1.0};
        double t = 1.0;
        double[] y = new double[]{2.0};
        integrator.setMaxEvaluations(0);
        integrator.integrate(equations, t0, y0, t, y);
    }

    @Test(expected = NoBracketingException.class)
    public void testIntegrateNoBracketing() throws Exception {
        AbstractIntegrator integrator = new AbstractIntegrator("Integrator1");
        FirstOrderDifferentialEquations equations = new MockDifferentialEquations() {
            @Override
            public double value(double t, double[] y) {
                return 0.0;
            }

            @Override
            public int getDimension() {
                return 1;
            }
        };
        double t0 = 0.0;
        double[] y0 = new double[]{1.0};
        double t = 1.0;
        double[] y = new double[]{2.0};
        integrator.integrate(equations, t0, y0, t, y);
    }
}