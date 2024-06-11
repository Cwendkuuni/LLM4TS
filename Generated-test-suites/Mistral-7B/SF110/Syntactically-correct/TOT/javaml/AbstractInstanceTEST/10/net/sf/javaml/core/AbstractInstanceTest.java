import static org.junit.Assert.*;
import net.sf.javaml.core.AbstractInstance;

public class AbstractInstanceTest {
    @org.junit.Test
    public void testGetID() {
        // Test creating new instances
        AbstractInstance instance1 = new DenseInstance(new double[5]);
        AbstractInstance instance2 = new DenseInstance(new double[5]);
        assertEquals(instance1.getID(), instance1.getID()); // Should be the same ID
        assertEquals(instance1.getID(), instance2.getID() + 1); // Next ID for instance2

        // Test setting and getting classValue
        AbstractInstance instance3 = new DenseInstance(new double[5], "testClassValue");
        assertEquals("testClassValue", instance3.classValue());

        instance3.setClassValue("newTestClassValue");
        assertEquals("newTestClassValue", instance3.classValue());
    }
}