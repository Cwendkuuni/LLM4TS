import org.apache.commons.math3.ode.AbstractIntegrator;
import org.junit.Test;
import static org.junit.Assert.*;

public class AbstractIntegratorTest {
    @Test
    public void testAbstractIntegrator_getName() {
        AbstractIntegrator integrator = new MyIntegrator("MyIntegrator");
        String name = integrator.getName();
        assertEquals("MyIntegrator", name);
    }
}