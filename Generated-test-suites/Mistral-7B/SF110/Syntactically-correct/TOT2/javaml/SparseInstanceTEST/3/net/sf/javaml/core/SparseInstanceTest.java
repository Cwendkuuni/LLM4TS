import static org.junit.Assert.*;
import net.sf.javaml.core.SparseInstance;

public class SparseInstanceTest {

    @Test
    public void testSetNoAttributes() {
        SparseInstance sparseInstance = new SparseInstance();
        int noAttributes = 5;
        sparseInstance.setNoAttributes(noAttributes);
        assertEquals(noAttributes, sparseInstance.noAttributes());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNoAttributesNegativeValue() {
        SparseInstance sparseInstance = new SparseInstance();
        int noAttributes = -5;
        sparseInstance.setNoAttributes(noAttributes);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNoAttributesZeroValue() {
        SparseInstance sparseInstance = new SparseInstance();
        int noAttributes = 0;
        sparseInstance.setNoAttributes(noAttributes);
    }

    // Test other methods and their edge cases and exceptions here

}