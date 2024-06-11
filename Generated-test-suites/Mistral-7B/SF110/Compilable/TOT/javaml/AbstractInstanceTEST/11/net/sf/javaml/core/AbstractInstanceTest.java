package net.sf.javaml.core;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class AbstractInstanceTest {
    @Test
    public void testGetID() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        AbstractInstance instance2 = new DenseInstance(new double[]{4.0, 5.0, 6.0});

        assertEquals(instance1.getID(), instance1.getID()); // Test ID for same instance
        assertEquals(instance1.getID(), 0); // Initial ID should be 0
        assertEquals(instance2.getID(), 1); // Next ID should be 1 for new instance
    }
}