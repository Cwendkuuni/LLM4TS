import org.apache.commons.collections.primitives.ArrayByteList;
import org.junit.Assert;
import org.junit.Test;

public class ArrayByteListTest {

    @Test
    public void testGet() {
        ArrayByteList list = new ArrayByteList(5);
        byte expected = (byte) 5;
        list.set(0, expected);
        byte actual = list.get(0);
        Assert.assertEquals(expected, actual);
    }
}