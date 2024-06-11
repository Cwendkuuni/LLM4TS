import static org.junit.Assert.assertEquals;
import net.sf.javaml.core.SparseInstance;

public class SparseInstanceTest {

    @Test
    public void testSetNoAttributes() {
        SparseInstance instance = new SparseInstance();
        int expectedNoAttributes = -1;
        instance.setNoAttributes(expectedNoAttributes);
        int actualNoAttributes = instance.noAttributes();
        assertEquals(expectedNoAttributes, actualNoAttributes);
    }
}