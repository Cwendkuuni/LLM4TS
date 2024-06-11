import org.junit.Test;
import org.junit.Assert;

public class AbstractIntegratorTest {

    @Test
    public void testGetName() {
        AbstractIntegrator integrator = new AbstractIntegrator("TestIntegrator") {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
                // Do nothing in this test case
            }
        };
        String name = integrator.getName();
        Assert.assertEquals("TestIntegrator", name);
    }
}