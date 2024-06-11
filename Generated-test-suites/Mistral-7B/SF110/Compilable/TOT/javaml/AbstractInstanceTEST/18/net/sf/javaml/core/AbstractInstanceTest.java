package net.sf.javaml.core;
import org.junit.Test;
import static org.junit.Assert.*;

public class AbstractInstanceTest {
    // ... (other imports and test setup)

    @Test
    public void testGetID() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0});
        int expectedID = AbstractInstance.nextID;
        int actualID = instance.getID();

        // Test that the ID is correctly set
        assertEquals(expectedID, actualID);

        // Test that the ID remains constant after being retrieved
        instance.setClassValue("test");
        assertEquals(expectedID, instance.getID());
    }
}