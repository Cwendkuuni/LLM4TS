import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.ode.events.AbstractEvent;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.junit.Test;
import static org.junit.Assert.*;

public class AbstractIntegratorTest {
    @Test
    public void testAbstractIntegrator_getName() {
        AbstractIntegrator integrator = new MyAbstractIntegrator("Name");
        assertEquals("Name", integrator.getName());
    }

    @Test
    public void testAbstractIntegrator_addStepHandler() {
        AbstractIntegrator integrator = new MyAbstractIntegrator("Name");
        StepHandler handler = mock(StepHandler.class);
        integrator.addStepHandler(handler);
        assertEquals(1, integrator.getStepHandlers().size());
    }

    @Test(expected = DimensionMismatchException.class)
    public void testAbstractIntegrator_integrate_DimensionMismatchException() {
        ExpandableStatefulODE equations = mock(ExpandableStatefulODE.class);
        AbstractIntegrator integrator = new MyAbstractIntegrator("Name");
        integrator.integrate(equations, 0, new double[1], 1, new double[2]);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testAbstractIntegrator_acceptStep_NumberIsTooSmallException() {
        MyAbstractIntegrator integrator = new MyAbstractIntegrator("Name");
        StepInterpolator interpolator = mock(StepInterpolator.class);
        when(interpolator.isForward()).thenReturn(true);
        when(interpolator.getGlobalPreviousTime()).thenReturn(0.1);
        when(interpolator.getGlobalCurrentTime()).thenReturn(0.2);
        integrator.acceptStep(interpolator, null, null, 0.3);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testAbstractIntegrator_acceptStep_MaxCountExceededException() {
        MyAbstractIntegrator integrator = new MyAbstractIntegrator("Name");
        StepInterpolator interpolator = mock(StepInterpolator.class);
        when(interpolator.isForward()).thenReturn(true);
        when(interpolator.getGlobalPreviousTime()).thenReturn(0.1);
        when(interpolator.getGlobalCurrentTime()).thenReturn(0.2);
        for (int i = 0; i < 1000; i++) {
            integrator.acceptStep(interpolator, null, null, 0.3);
        }
    }
}

class MyAbstractIntegrator extends AbstractIntegrator {
    MyAbstractIntegrator(String name) {
        super(name);
    }
}