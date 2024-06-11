import static org.junit.Assert.*;
import net.sf.javaml.core.SparseInstance;

public class SparseInstanceTest {

    @Test
    public void testSetNoAttributes() {
        SparseInstance instance = new SparseInstance();
        int initialNoAttributes = instance.noAttributes();

        instance.setNoAttributes(10);
        int expectedNoAttributes = 10;
        int actualNoAttributes = instance.noAttributes();

        assertEquals(expectedNoAttributes, actualNoAttributes);
    }
}