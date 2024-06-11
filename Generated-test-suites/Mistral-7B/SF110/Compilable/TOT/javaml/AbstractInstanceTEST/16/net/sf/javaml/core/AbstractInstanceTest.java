package net.sf.javaml.core;
import net.sf.javaml.core.AbstractInstance;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class AbstractInstanceTest {

    @Test
    public void testGetID() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        assertEquals(AbstractInstance.nextID, instance.getID());
        AbstractInstance instance2 = new DenseInstance(new double[]{4.0, 5.0, 6.0});
        assertEquals(AbstractInstance.nextID + 1, instance2.getID());
    }
}