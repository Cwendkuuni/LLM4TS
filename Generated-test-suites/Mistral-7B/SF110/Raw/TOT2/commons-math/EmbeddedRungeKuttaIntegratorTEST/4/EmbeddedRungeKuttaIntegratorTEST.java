import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testGetOrder() {
        EmbeddedRungeKuttaIntegrator integrator = new MyEmbeddedRungeKuttaIntegrator(); // replace MyEmbeddedRungeKuttaIntegrator with the actual class name
        int expectedOrder = 5; // replace with the actual order of the integrator
        int actualOrder = integrator.getOrder();
        assertEquals(expectedOrder, actualOrder);
    }
}