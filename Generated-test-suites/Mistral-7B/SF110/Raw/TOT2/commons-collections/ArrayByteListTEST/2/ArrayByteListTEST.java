
import static org.junit.Assert.*;

import org.apache.commons.collections.primitives.ArrayByteList;
import org.apache.commons.collections.primitives.ByteArray;
import org.apache.commons.collections.primitives.ByteArrayOutputStream;
import org.junit.Test;

public class ArrayByteListTest {

    @Test
    public void testEmptyArrayByteListConstructor() {
        ArrayByteList list = new ArrayByteList();
        assertEquals(0, list.size());
    }

    @Test
    public void testInitialCapacityArrayByteListConstructor() {
        ArrayByteList list = new ArrayByteList(5);
        assertEquals(5, list.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeInitialCapacityArrayByteListConstructor() {
        ArrayByteList list = new ArrayByteList(-5);
    }

    @Test
    public void testAddAllFromByteCollection() {
        byte[] sourceData = {1, 2, 3, 4};
        ByteArrayOutputStream source = new ByteArrayOutputStream();
        source.write(sourceData, 0, sourceData.length);
        ArrayByteList list = new ArrayByteList(new ByteArray(source.toByteArray()));
        assertEquals(sourceData.length, list.size());
        assertArrayEquals(sourceData, list.toByteArray());
    }

    // test methods get, size, removeElementAt, set, add, ensureCapacity, and trimToSize here

    @Test
    public void testGetValidIndex() {
        byte[] testData = {1, 2, 3, 4};
        ArrayByteList list = new ArrayByteList(testData);
        byte result = list.get(1);
        assertEquals((byte)2, result);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetInvalidIndex() {
        byte[] testData = {1, 2, 3, 4};
        ArrayByteList list = new ArrayByteList(testData);
        list.get(-1);
    }

    // test methods checkRange and checkRangeIncludingEndpoint here

}
