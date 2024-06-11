import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class EmbeddedRungeKuttaIntegratorTest {
    @Test
    public void testGetOrder() {
        EmbeddedRungeKuttaIntegrator integrator = new MyEmbeddedRungeKuttaIntegrator(); // Assuming MyEmbeddedRungeKuttaIntegrator is a subclass of EmbeddedRungeKuttaIntegrator
        int expectedOrder = 5; // Replace with the expected order of the specific integrator
        int actualOrder = integrator.getOrder();
        assertEquals(expectedOrder, actualOrder);
    }
}