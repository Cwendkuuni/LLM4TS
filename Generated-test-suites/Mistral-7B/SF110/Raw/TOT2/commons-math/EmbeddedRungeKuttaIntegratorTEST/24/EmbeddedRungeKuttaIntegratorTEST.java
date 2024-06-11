import org.junit.Test;
import org.junit.Assert.*;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testGetOrder() {
        EmbeddedRungeKuttaIntegrator integrator = new MyEmbeddedRungeKuttaIntegrator(); // Assuming MyEmbeddedRungeKuttaIntegrator is a subclass of EmbeddedRungeKuttaIntegrator
        int order = integrator.getOrder();
        assertEquals(5, order); // Replace 5 with the expected order of your specific integrator
    }
}