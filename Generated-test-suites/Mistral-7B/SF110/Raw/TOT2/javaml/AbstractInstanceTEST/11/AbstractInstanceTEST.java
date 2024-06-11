import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class AbstractInstanceTest {

    @Test
    public void testGetID() {
        AbstractInstance instance = new AbstractInstance(new Object());
        assertEquals(0, instance.getID());
        AbstractInstance instance2 = new AbstractInstance(new Object());
        assertEquals(1, instance2.getID());
    }
}