import static org.junit.Assert.assertEquals;
import net.sf.javaml.core.SparseInstance;

public class SparseInstanceTest {
    @Test
    public void testSetNoAttributes() {
        SparseInstance sparseInstance = new SparseInstance();
        int expectedNoAttributes = 0;
        sparseInstance.setNoAttributes(expectedNoAttributes);
        int actualNoAttributes = sparseInstance.noAttributes();
        assertEquals(expectedNoAttributes, actualNoAttributes);
    }
}