import static org.junit.Assert.assertEquals;
import net.sf.javaml.core.AbstractInstance;

public class AbstractInstanceTest {
    @Test
    public void testGetID() {
        AbstractInstance instance = new AbstractInstance(new Object());
        int expectedID = AbstractInstance.nextID;
        int actualID = instance.getID();
        assertEquals(expectedID, actualID);
    }
}