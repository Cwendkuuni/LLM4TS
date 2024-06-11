import org.junit.Test;
import static org.junit.Assert.*;

public class SparseInstanceTest {

    @Test
    public void testSetNoAttributes() {
        SparseInstance sparseInstance = new SparseInstance();
        sparseInstance.setNoAttributes(5);
        assertEquals(5, sparseInstance.noAttributes());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNegativeNoAttributes() {
        SparseInstance sparseInstance = new SparseInstance();
        sparseInstance.setNoAttributes(-5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNoAttributesGreaterThanCurrentSize() {
        SparseInstance sparseInstance = new SparseInstance();
        sparseInstance.setNoAttributes(10);
        sparseInstance.addAttribute(1, 1.0);
        sparseInstance.setNoAttributes(11);
    }
}