import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.AbstractSimplex;

public class AbstractSimplexTest {
    @Test
    public void testAbstractSimplexConstructorNoArguments() {
        AbstractSimplex simplex = new AbstractSimplex(1);
        Assert.assertEquals(1, simplex.getDimension());
        Assert.assertNotNull(simplex.getPoints());
        Assert.assertEquals(2, simplex.getSize());
    }
}