import org.junit.Test;
import static org.junit.Assert.*;

public class EmbeddedRungeKuttaIntegratorTest {
    @Test
    public void testGetOrder() {
        EmbeddedRungeKuttaIntegrator integrator = new MyEmbeddedRungeKuttaIntegrator();
        int order = integrator.getOrder();
        assertEquals(5, order); // Replace 5 with the actual order of the integrator
    }
}