import org.junit.Test;
import static org.junit.Assert.*;

public class ArrayByteListTest {
    @Test
    public void testEmptyConstructor() {
        ArrayByteList list = new ArrayByteList();
        assertEquals(0, list.size());
    }

    @Test
    public void testInitialCapacityConstructor() {
        ArrayByteList list = new ArrayByteList(5);
        assertEquals(5, list.size());
    }

    @Test
    public void testByteCollectionConstructor() {
        ByteCollection collection = new byte[5];
        ArrayByteList list = new ArrayByteList(collection);
        assertEquals(5, list.size());
    }

    // Add test cases for each method here...
}