import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class EmbeddedRungeKuttaIntegratorTest {
    @Test
    public void testGetOrder() {
        EmbeddedRungeKuttaIntegrator integrator = new MyEmbeddedRungeKuttaIntegrator(); // Assuming MyEmbeddedRungeKuttaIntegrator is a subclass of EmbeddedRungeKuttaIntegrator
        int expectedOrder = 5; // Assuming the order of MyEmbeddedRungeKuttaIntegrator is 5
        int order = integrator.getOrder();
        assertEquals(expectedOrder, order);
    }
}