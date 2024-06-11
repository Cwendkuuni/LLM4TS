import static org.junit.Assert.assertEquals;
import net.sf.javaml.core.AbstractInstance;

public class AbstractInstanceTest {
    @org.junit.Test
    public void testGetID() {
        // Create a new AbstractInstance and check its ID
        AbstractInstance instance = new AbstractInstance(new Object());
        assertEquals("ID should be initialized correctly", 0, instance.getID());
        
        // Create another AbstractInstance and check they have different IDs
        AbstractInstance instance2 = new AbstractInstance(new Object());
        assertEquals("New instances should have different IDs", 1, instance2.getID());
        
        // Check that the ID remains the same even after multiple getID() calls
        assertEquals("ID should remain constant", 0, instance.getID());
        
        // Check that the ID is incremented correctly for new instances
        AbstractInstance instance3 = new AbstractInstance(new Object());
        assertEquals("New instances should have ID greater than previous ones", 2, instance3.getID());
    }
}