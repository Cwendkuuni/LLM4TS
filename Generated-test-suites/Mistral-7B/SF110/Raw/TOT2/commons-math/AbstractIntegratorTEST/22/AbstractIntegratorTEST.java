import org.apache.commons.math3.ode.AbstractIntegrator;
import org.junit.Assert;
import org.junit.Test;

public class AbstractIntegratorTest {

    @Test
    public void testAbstractIntegratorConstructor() {
        String name = "Test Integrator";
        AbstractIntegrator integrator = new AbstractIntegrator(name);
        Assert.assertEquals(name, integrator.getName());
        Assert.assertNull(integrator.stepHandlers);
        Assert.assertEquals(Double.NaN, integrator.stepStart, 0.0);
        Assert.assertEquals(Double.NaN, integrator.stepSize, 0.0);
        Assert.assertFalse(integrator.isLastStep);
        Assert.assertFalse(integrator.resetOccurred);
        Assert.assertEquals(0, integrator.eventsStates.size());
        Assert.assertFalse(integrator.statesInitialized);
    }
}