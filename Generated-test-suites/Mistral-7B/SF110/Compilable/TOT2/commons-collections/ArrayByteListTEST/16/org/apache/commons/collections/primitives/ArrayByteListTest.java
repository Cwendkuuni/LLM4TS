package org.apache.commons.collections.primitives;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import org.apache.commons.collections.primitives.ArrayByteList;

public class ArrayByteListTest {

    @Test
    public void testGet() {
        ArrayByteList list = new ArrayByteList(5);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.add((byte) 4);
        list.add((byte) 5);
        byte expected = (byte) 3;
        byte actual = list.get(2);
        assertEquals(expected, actual);
    }

    @Test
    public void testSize() {
        ArrayByteList list = new ArrayByteList(5);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.add((byte) 4);
        list.add((byte) 5);
        int expected = 5;
        int actual = list.size();
        assertEquals(expected, actual);
    }

    @Test
    public void testRemoveElementAt() {
        ArrayByteList list = new ArrayByteList(5);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.add((byte) 4);
        list.add((byte) 5);

        byte expected = (byte) 3;
        byte actual = list.removeElementAt(2);
        assertEquals(expected, actual);

        int sizeExpected = 4;
        int sizeActual = list.size();
        assertEquals(sizeExpected, sizeActual);
    }

    // Test cases for edge cases and exception handling scenarios for get(int index), size(), and removeElementAt(int index) methods go here
}