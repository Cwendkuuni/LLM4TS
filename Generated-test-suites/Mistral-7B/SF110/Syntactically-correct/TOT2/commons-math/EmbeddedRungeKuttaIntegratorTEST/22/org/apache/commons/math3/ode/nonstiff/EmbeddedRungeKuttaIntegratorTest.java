import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class EmbeddedRungeKuttaIntegratorTest {
    @Test
    public void testGetOrder() {
        EmbeddedRungeKuttaIntegrator integrator = new MyEmbeddedRungeKuttaIntegrator(); // Replace MyEmbeddedRungeKuttaIntegrator with the actual subclass
        int expectedOrder = 5; // Replace with the actual order of the Runge-Kutta method
        int actualOrder = integrator.getOrder();
        assertEquals(expectedOrder, actualOrder);
    }
}