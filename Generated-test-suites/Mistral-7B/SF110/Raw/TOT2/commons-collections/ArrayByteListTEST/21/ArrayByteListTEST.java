import static org.junit.Assert.*;
import org.apache.commons.collections.primitives.ArrayByteList;

public class ArrayByteListTest {
    @Test
    public void testGetMethod() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);

        byte expected = (byte) 1;
        byte actual = list.get(0);

        assertEquals(expected, actual);
    }
}