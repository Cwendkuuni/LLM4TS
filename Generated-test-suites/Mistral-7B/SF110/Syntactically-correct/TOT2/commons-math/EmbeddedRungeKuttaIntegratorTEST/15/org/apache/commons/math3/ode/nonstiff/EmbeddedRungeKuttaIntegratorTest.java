import org.junit.Test;
import org.junit.Assert.*;

public class EmbeddedRungeKuttaIntegratorTest {
    @Test
    public void testGetOrder() {
        EmbeddedRungeKuttaIntegrator integrator = new YourConcreteEmbeddedRungeKuttaIntegratorClass(); // Replace with the concrete class name
        int order = integrator.getOrder();
        assertEquals(expectedOrder, order); // Replace expectedOrder with the expected value
    }
}