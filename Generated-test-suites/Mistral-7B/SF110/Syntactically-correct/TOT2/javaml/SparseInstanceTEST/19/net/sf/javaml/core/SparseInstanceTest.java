import static org.junit.Assert.*;

public class SparseInstanceTest {
    @Test
    public void testDefaultConstructor() {
        SparseInstance sparseInstance = new SparseInstance();
        assertEquals(-1, sparseInstance.noAttributes);
        assertTrue(sparseInstance.isEmpty());
    }
}