import static org.junit.Assert.*;
import org.apache.commons.math3.ode.AbstractIntegrator;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.sampling.AbstractStepInterpolator;
import org.junit.Test;

/**
 * Test class for AbstractIntegrator.
 */
public class AbstractIntegratorTest {

    /**
     * Test method for {@link AbstractIntegrator#AbstractIntegrator(String)}.
     */
    @Test
    public void testAbstractIntegrator() {
        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator");
        assertNotNull(integrator);
        assertEquals("TestIntegrator", integrator.getName());
        assertTrue(integrator.getEventHandlers().isEmpty());
    }

    /**
     * Test method for {@link AbstractIntegrator#addEventHandler(EventHandler, double, double, int, UnivariateRealSolver)}.
     */
    @Test
    public void testAddEventHandler() {
        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator");
        EventHandler handler = mock(EventHandler.class);
        integrator.addEventHandler(handler, 0.5, 1.0e-6, 10, mock(UnivariateRealSolver.class));
        List<EventHandler> handlers = new ArrayList<>(integrator.getEventHandlers());
        assertEquals(1, handlers.size());
        assertEquals(handler, handlers.get(0));
    }

    /**
     * Test method for {@link AbstractIntegrator#clearEventHandlers()}.
     */
    @Test
    public void testClearEventHandlers() {
        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator");
        integrator.addEventHandler(mock(EventHandler.class), 0.5, 1.0e-6, 10, mock(UnivariateRealSolver.class));
        integrator.clearEventHandlers();
        assertTrue(integrator.getEventHandlers().isEmpty());
    }

    /**
     * Test method for {@link AbstractIntegrator#integrate(ExpandableStatefulODE, double)}.
     */
    @Test(expected = MathIllegalStateException.class)
    public void testIntegrateWithoutEquations() {
        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator");
        integrator.integrate(mock(ExpandableStatefulODE.class), 1.0);
    }

    /**
     * Test method for {@link AbstractIntegrator#integrate(ExpandableStatefulODE, double)}.
     */
    @Test
    public void testIntegrate() {
        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator");
        ExpandableStatefulODE equations = mock(ExpandableStatefulODE.class);
        integrator.setEquations(equations);
        double t = 1.0;
        integrator.integrate(equations, t);
        verify(equations).setTime(t);
        verify(equations).setPrimaryState(anyArray());
    }

    /**
     * Test method for {@link AbstractIntegrator#acceptStep(AbstractStepInterpolator, double[], double[], double)}.
     */
    @Test
    public void testAcceptStep() {
        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator");
        ExpandableStatefulODE equations = mock(ExpandableStatefulODE.class);
        integrator.setEquations(equations);
        AbstractStepInterpolator interpolator = mock(AbstractStepInterpolator.class);
        double[] y = new double[1];
        double[] yDot = new double[1];
        double tEnd = 2.0;
        when(equations.getDimension()).thenReturn(1);
        when(equations.computeDerivatives(eq(anyDouble()), eq(anyArray()), eq(anyArray()))).thenReturn(null);

        integrator.acceptStep(interpolator, y, yDot, tEnd);

        verify(interpolator).setSoftPreviousTime(eq(Double.NaN));
        verify(interpolator).setSoftCurrentTime(eq(1.0));
        verify(interpolator).setInterpolatedTime(eq(1.0));
        verify(interpolator).setInterpolatedState(eq(y));
        verifyZeroInteractions(interpolator);
        verify(interpolator).setInterpolatedTime(eq(2.0));
        verify(interpolator).setInterpolatedState(eq(y));
    }

    /**
     * Test method for {@link AbstractIntegrator#acceptStep(AbstractStepInterpolator, double[], double[], double)} with event handling.
     */
    @Test
    public void testAcceptStepWithEventHandling() {
        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator");
        ExpandableStatefulODE equations = mock(ExpandableStatefulODE.class);
        integrator.setEquations(equations);
        AbstractStepInterpolator interpolator = mock(AbstractStepInterpolator.class);
        double[] y = new double[1];
        double[] yDot = new double[1];
        double tEnd = 2.0;
        double eventTime = 1.5;
        EventHandler eventHandler = mock(EventHandler.class);
        integrator.addEventHandler(eventHandler, 0.5, 1.0e-6, 10, mock(UnivariateRealSolver.class));

        when(equations.getDimension()).thenReturn(1);
        when(equations.computeDerivatives(eq(anyDouble()), eq(anyArray()), eq(anyArray()))).thenReturn(null);

        // first step
        integrator.acceptStep(interpolator, y, yDot, tEnd);

        verify(interpolator).setSoftPreviousTime(eq(Double.NaN));
        verify(interpolator).setSoftCurrentTime(eq(1.0));
        verify(interpolator).setInterpolatedTime(eq(1.0));
        verify(interpolator).setInterpolatedState(eq(y));
        verifyZeroInteractions(interpolator);
        verify(interpolator).setInterpolatedTime(eq(2.0));
        verify(interpolator).setInterpolatedState(eq(y));
        verifyZeroInteractions(interpolator);

        // second step
        integrator.acceptStep(interpolator, y, yDot, tEnd);

        verify(interpolator).setSoftPreviousTime(eq(1.0));
        verify(interpolator).setSoftCurrentTime(eq(1.5));
        verify(interpolator).setInterpolatedTime(eq(1.5));
        verify(interpolator).setInterpolatedState(eq(y));
        verify(eventHandler).handleEvent(eq(eventTime), eq(y), anyArray());
        verifyZeroInteractions(interpolator);
        verify(interpolator).setInterpolatedTime(eq(2.0));
        verify(interpolator).setInterpolatedState(eq(y));
    }

}