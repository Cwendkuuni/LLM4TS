import org.junit.Test;
import org.junit.Assert;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testGetOrder() {
        EmbeddedRungeKuttaIntegrator integrator = new ConcreteEmbeddedRungeKuttaIntegrator(); // replace ConcreteEmbeddedRungeKuttaIntegrator with the actual class name
        int expectedOrder = 5; // replace with the expected order of the integrator
        int order = integrator.getOrder();
        Assert.assertEquals("Incorrect order of the integrator", expectedOrder, order);
    }
}