import static org.junit.Assert.assertEquals;
import net.sf.javaml.core.SparseInstance;

public class SparseInstanceTest {
    @Test
    public void testSetNoAttributes() {
        SparseInstance instance = new SparseInstance();
        int noAttributes = 5;
        instance.setNoAttributes(noAttributes);
        assertEquals(noAttributes, instance.noAttributes());
    }
}