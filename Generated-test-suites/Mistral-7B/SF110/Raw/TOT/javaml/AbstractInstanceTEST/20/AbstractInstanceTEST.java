
package net.sf.javaml.core;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

public class AbstractInstanceTest {

    @Test
    public void testGetID() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0});
        int id = instance.getID();
        assertEquals(AbstractInstance.nextID, id);
    }

    // ... other test methods ...

}
