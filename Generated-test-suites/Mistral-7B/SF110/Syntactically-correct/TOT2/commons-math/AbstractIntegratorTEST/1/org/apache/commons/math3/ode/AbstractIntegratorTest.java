import org.apache.commons.math3.ode.AbstractIntegrator;
import org.junit.Test;
import static org.junit.Assert.*;

public class AbstractIntegratorTest {

    @Test
    public void testAbstractIntegrator() {
        AbstractIntegrator integrator = new AbstractIntegrator();
        assertNull(integrator.getName());
        assertEquals(0, integrator.getStepHandlers().size());
        assertFalse(integrator.isLastStep());
        assertFalse(integrator.resetOccurred);
        assertEquals(-1, integrator.getMaxEvaluations());
        assertEquals(0, integrator.getEvaluations());
        assertNull(integrator.getExpandable());
        assertNull(integrator.getEvaluationsCounter());
    }
}