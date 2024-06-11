import static org.junit.Assert.assertEquals;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;

public class EmbeddedRungeKuttaIntegratorTest {
    @Test
    public void testGetOrder() {
        EmbeddedRungeKuttaIntegrator integrator = new MyEmbeddedRungeKuttaIntegrator(); // replace with actual subclass
        int order = integrator.getOrder();
        assertEquals(5, order); // replace with actual order
    }
}