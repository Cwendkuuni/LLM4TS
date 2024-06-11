import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.math3.ode.AbstractIntegrator;

public class AbstractIntegratorTest {

    @Test
    public void testGetName() {
        AbstractIntegrator integrator = new MyAbstractIntegrator("MyIntegrator");
        String name = integrator.getName();
        Assert.assertEquals("MyIntegrator", name);
    }
}