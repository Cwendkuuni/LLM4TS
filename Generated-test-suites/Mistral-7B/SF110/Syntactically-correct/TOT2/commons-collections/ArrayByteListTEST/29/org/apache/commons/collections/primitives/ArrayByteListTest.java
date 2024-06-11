import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.collections.primitives.ArrayByteList;

public class ArrayByteListTest {
    
    @Test
    public void testGet() {
        ArrayByteList abl = new ArrayByteList();
        byte expectedValue = 1;
        abl.add((byte) 1);
        byte actualValue = abl.get(0);
        assertEquals(expectedValue, actualValue);
    }
}