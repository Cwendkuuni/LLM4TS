import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.ode.events.AbstractEvent;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.junit.Test;
import static org.junit.Assert.*;

public class AbstractIntegratorTest {

    @Test
    public void testConstructor() {
        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator");
        assertNotNull(integrator);
        assertEquals("TestIntegrator", integrator.getName());
        assertTrue(integrator.getStepHandlers().isEmpty());
        assertTrue(integrator.getEventHandlers().isEmpty());
        assertEquals(-1, integrator.getMaxEvaluations());
        assertEquals(0, integrator.getEvaluations());
        assertNull(integrator.getExpandable());
    }

    @Test
    public void testAddStepHandler() {
        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator");
        StepHandler handler = mock(StepHandler.class);
        integrator.addStepHandler(handler);
        assertTrue(integrator.getStepHandlers().contains(handler));
        assertEquals(1, integrator.getStepHandlers().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddEventHandlerNullHandler() {
        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator");
        integrator.addEventHandler(null, 0.0, 0.0, 0, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddEventHandlerNullSolver() {
        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator");
        AbstractEvent handler = mock(AbstractEvent.class);
        integrator.addEventHandler(handler, 0.0, 0.0, 0, null);
    }

    @Test
    public void testIntegrateNormalCase() {
        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator");
        ExpandableStatefulODE equations = mock(ExpandableStatefulODE.class);
        when(equations.getDimension()).thenReturn(2);
        when(equations.getPrimaryMapper().getEquationData(anyDoubleArray())).thenReturn(new double[2]);
        integrator.setExpandable(equations);

        double t0 = 0.0;
        double[] y0 = new double[]{1.0, 1.0};
        double t = 1.0;
        double[] y = new double[]{2.0, 2.0};

        integrator.integrate(equations, t0, y0, t, y);

        verify(equations).computeDerivatives(eq(t0), eq(y0), anyDoubleArray());
        verify(equations, times(2)).getPrimaryMapper().getEquationData(anyDoubleArray());
        verify(equations, times(2)).getTime();
        assertEquals(1.0, y[0], 0.0);
        assertEquals(1.0, y[1], 0.0);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testIntegrateDimensionMismatch() {
        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator");
        ExpandableStatefulODE equations = mock(ExpandableStatefulODE.class);
        when(equations.getDimension()).thenReturn(2);
        when(equations.getPrimaryMapper().getEquationData(anyDoubleArray())).thenReturn(new double[1]);
        integrator.setExpandable(equations);

        double t0 = 0.0;
        double[] y0 = new double[]{1.0, 1.0};
        double t = 1.0;
        double[] y = new double[]{2.0};

        integrator.integrate(equations, t0, y0, t, y);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testIntegrateSmallStep() {
        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator");
        ExpandableStatefulODE equations = mock(ExpandableStatefulODE.class);
        integrator.setExpandable(equations);
        when(equations.getDimension()).thenReturn(2);
        when(equations.getPrimaryMapper().getEquationData(anyDoubleArray())).thenReturn(new double[2]);

        double t0 = 0.0;
        double[] y0 = new double[]{1.0, 1.0};
        double t = 0.01;
        double[] y = new double[]{2.0, 2.0};

        integrator.integrate(equations, t0, y0, t, y);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testIntegrateMaxEvaluation() {
        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator");
        ExpandableStatefulODE equations = mock(ExpandableStatefulODE.class);
        integrator.setExpandable(equations);
        when(equations.getDimension()).thenReturn(2);
        when(equations.getPrimaryMapper().getEquationData(anyDoubleArray())).thenReturn(new double[2]);

        double t0 = 0.0;
        double[] y0 = new double[]{1.0, 1.0};
        double t = 1.0;
        double[] y = new double[]{2.0, 2.0};

        integrator.setMaxEvaluations(0);
        integrator.integrate(equations, t0, y0, t, y);
    }

    @Test(expected = NoBracketingException.class)
    public void testIntegrateNoBracketing() {
        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator");
        ExpandableStatefulODE equations = mock(ExpandableStatefulODE.class);
        integrator.setExpandable(equations);
        when(equations.getDimension()).thenReturn(2);
        when(equations.getPrimaryMapper().getEquationData(anyDoubleArray())).thenReturn(new double[2]);

        double t0 = 0.0;
        double[] y0 = new double[]{1.0, 1.0};
        double t = 1.0;
        double[] y = new double[]{2.0, 2.0};

        doThrow(new NoBracketingException("test"))
                .when(equations)
                .computeDerivatives(eq(t0), eq(y0), anyDoubleArray());

        integrator.integrate(equations, t0, y0, t, y);
    }
}