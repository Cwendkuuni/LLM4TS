import org.junit.Test;
import static org.junit.Assert.*;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testGetOrder() {
        EmbeddedRungeKuttaIntegrator integrator = new MyEmbeddedRungeKuttaIntegrator(); // replace MyEmbeddedRungeKuttaIntegrator with the actual implementation
        int order = integrator.getOrder();
        assertEquals(order, 5); // replace with the actual order of the integrator
    }
}