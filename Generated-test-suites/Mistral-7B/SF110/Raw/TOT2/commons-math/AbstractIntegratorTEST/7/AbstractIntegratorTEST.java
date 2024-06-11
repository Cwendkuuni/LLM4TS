import org.apache.commons.math3.ode.AbstractIntegrator;
import org.junit.Test;
import static org.junit.Assert.*;

public class AbstractIntegratorTest {

    @Test
    public void testAbstractIntegratorConstructor() {
        AbstractIntegrator integrator = new AbstractIntegrator();
        assertNotNull(integrator.getName());
        assertEquals(0, integrator.getStepHandlers().size());
        assertEquals(0, integrator.getEventHandlers().size());
        assertEquals(-1, integrator.getMaxEvaluations());
        assertEquals(0, integrator.getEvaluations());
        assertFalse(integrator.isLastStep());
        assertFalse(integrator.resetOccurred);
        assertNull(integrator.getExpandable());
    }
}