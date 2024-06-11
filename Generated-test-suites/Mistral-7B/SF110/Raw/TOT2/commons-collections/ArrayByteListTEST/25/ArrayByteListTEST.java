import org.apache.commons.collections.primitives.ArrayByteList;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArrayByteListTest {
    @Test
    public void testGet() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        byte actual = list.get(0);
        byte expected = (byte) 1;
        assertEquals(expected, actual);
    }
}