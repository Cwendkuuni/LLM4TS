import org.junit.Test;
import static org.junit.Assert.*;

public class AbstractIntegratorTest {
    @Test
    public void testConstructor() {
        AbstractIntegrator integrator = new AbstractIntegrator("Integrator1");
        assertNotNull(integrator);
        assertEquals("Integrator1", integrator.getName());
        assertEquals(0, integrator.getStepHandlers().size());
    }
}