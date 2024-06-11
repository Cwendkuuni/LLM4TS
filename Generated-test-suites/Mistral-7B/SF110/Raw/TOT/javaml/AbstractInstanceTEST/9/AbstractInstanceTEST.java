import static org.junit.Assert.*; // Import assertions
import net.sf.javaml.core.AbstractInstance;

public class AbstractInstanceTest {
    @org.junit.Test
    public void testGetID() {
        AbstractInstance instance = new DenseInstance(new double[1]); // Create an instance
        int expectedID = AbstractInstance.nextID; // Get the expected ID based on the static variable
        int actualID = instance.getID(); // Get the actual ID
        assertEquals(expectedID, actualID); // Assert that the IDs match
    }
}