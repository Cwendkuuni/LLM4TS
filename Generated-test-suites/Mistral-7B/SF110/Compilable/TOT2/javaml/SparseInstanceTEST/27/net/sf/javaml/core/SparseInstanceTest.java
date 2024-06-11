package net.sf.javaml.core;
import net.sf.javaml.core.SparseInstance;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class SparseInstanceTest {

    @Test
    public void testSetNoAttributes() {
        SparseInstance instance = new SparseInstance();
        instance.setNoAttributes(5);
        assertEquals(5, instance.noAttributes());
    }
}