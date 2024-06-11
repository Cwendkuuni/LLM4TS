import org.junit.Test;
import org.junit.Assert.*;

public class AbstractSimplexTest {
    @Test
    public void testAbstractSimplexEmptyConstructor() {
        AbstractSimplex simplex = new AbstractSimplex(0);
        Assert.assertEquals(0, simplex.getDimension());
        Assert.assertNull(simplex.getPoints());
    }
}