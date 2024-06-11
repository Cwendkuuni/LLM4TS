import org.junit.Test;
import static org.junit.Assert.*;

public class AbstractSimplexTest {
    @Test
    public void testAbstractSimplexWithValidInput() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        assertEquals(3, simplex.getDimension());
        // Add more assertions to check the state of the object after creation
    }

    @Test(expected = NullArgumentException.class)
    public void testAbstractSimplexWithNullInput() {
        AbstractSimplex simplex = new AbstractSimplex(null);
    }

    @Test(expected = ZeroException.class)
    public void testAbstractSimplexWithZeroDimension() {
        AbstractSimplex simplex = new AbstractSimplex(0);
    }
}