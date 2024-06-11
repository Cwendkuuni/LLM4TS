import org.junit.Test;
import static org.junit.Assert.*;

public class AbstractInstanceTest {
    // ... other imports and class setup

    @Test
    public void testGetID() {
        AbstractInstance instance = new AbstractInstance(new Object());
        int id = instance.getID();
        int expectedID = AbstractInstance.nextID;
        assertEquals(expectedID, id);
    }
}