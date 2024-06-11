import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.ode.AbstractIntegrator;

public class AbstractIntegratorTest {
    @Test
    public void testAbstractIntegratorDefaultInitialization() {
        AbstractIntegrator integrator = new AbstractIntegrator();
        assertNull(integrator.getName());
        assertTrue(integrator.stepHandlers.isEmpty());
        assertNull(integrator.eventsStates);
        assertFalse(integrator.statesInitialized);
        assertEquals(-1, integrator.getMaxEvaluations());
        assertEquals(0, integrator.getEvaluations());
        assertNull(integrator.expandable);
    }
}