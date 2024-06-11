package org.apache.commons.collections.primitives;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

public class ArrayByteListTest {

    @Test
    public void testEmptyArrayByteList() {
        ArrayByteList list = new ArrayByteList();
        assertEquals(0, list.size());
        assertNull(list.get(0));
    }

    @Test
    public void testInitialCapacityArrayByteList() {
        ArrayByteList list = new ArrayByteList(5);
        assertEquals(0, list.size());
        assertNull(list.get(0));
        assertEquals(5, list._data.length);
    }

    @Test
    public void testArrayByteListFromByteCollection() {
        ByteArray getElements = new ByteArray(new byte[]{1, 2, 3, 4, 5});
        ArrayByteList list = new ArrayByteList(getElements);
        assertEquals(5, list.size());
        for (int i = 0; i < 5; i++) {
            assertEquals(getElements.get(i), list.get(i));
        }
    }

    @Test
    public void testGetByte() {
        ArrayByteList list = new ArrayByteList(5);
        list.set(0, (byte) 1);
        list.set(1, (byte) 2);
        list.set(2, (byte) 3);
        list.set(3, (byte) 4);
        list.set(4, (byte) 5);
        for (int i = 0; i < 5; i++) {
            assertEquals(i + 1, list.get(i));
        }
    }

    @Test
    public void testSize() {
        ArrayByteList list = new ArrayByteList(5);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.add((byte) 4);
        list.add((byte) 5);
        assertEquals(5, list.size());
    }

    @Test
    public void testRemoveElementAt() {
        ArrayByteList list = new ArrayByteList(5);
        list.set(0, (byte) 1);
        list.set(1, (byte) 2);
        list.set(2, (byte) 3);
        list.set(3, (byte) 4);
        list.set(4, (byte) 5);
        assertEquals(5, list.size());
        byte removed = list.removeElementAt(2);
        assertEquals(3, list.size());
        assertEquals(4, list.get(1));
        assertNull(list.get(2));
        assertEquals(5, list.get(3));
        assertEquals(4, removed);
    }

    @Test
    public void testSetByte() {
        ArrayByteList list = new ArrayByteList(5);
        list.set(0, (byte) 1);
        list.set(1, (byte) 2);
        list.set(2, (byte) 3);
        list.set(3, (byte) 4);
        list.set(4, (byte) 5);
        for (int i = 0; i < 5; i++) {
            list.set(i, (byte) (i + 10));
        }
        for (int i = 0; i < 5; i++) {
            assertEquals(i + 10, list.get(i));
        }
    }

    @Test
    public void testAddByte() {
        ArrayByteList list = new ArrayByteList(5);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.add((byte) 4);
        list.add((byte) 5);
        list.add(0, (byte) 0);
        list.add(4, (byte) 6);
        assertEquals(6, list.size());
        assertEquals(0, list.get(0));
        assertEquals(1, list.get(1));
        assertEquals(2, list.get(2));
        assertEquals(3, list.get(3));
        assertEquals(4, list.get(4));
        assertEquals(5, list.get(5));
        assertEquals(0, list.get(0));
        assertEquals(6, list.get(4));
    }

    @Test
    public void testEnsureCapacity() {
        ArrayByteList list = new ArrayByteList(5);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.add((byte) 4);
        list.add((byte) 5);
        list.add((byte) 6);
        list.add((byte) 7);
        list.add((byte) 8);
        list.add((byte) 9);
        list.add((byte) 10);
        list.add((byte) 11);
        list.add((byte) 12);
        list.add((byte) 13);
        list.add((byte) 14);
        list.add((byte) 15);
        list.add((byte) 16);
        list.add((byte) 17);
        list.add((byte) 18);
        list.add((byte) 19);
        list.add((byte) 20);
        list.add((byte) 21);
        list.add((byte) 22);
        list.add((byte) 23);
        list.add((byte) 24);
        list.add((byte) 25);
        list.add((byte) 26);
        list.add((byte) 27);
        list.add((byte) 28);
        list.add((byte) 29);
        list.add((byte) 30);
        assertEquals(30, list.size());
    }

    @Test
    public void testTrimToSize() {
        ArrayByteList list = new ArrayByteList(5);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.add((byte) 4);
        list.add((byte) 5);
        list.add((byte) 6);
        list.add((byte) 7);
        list.add((byte) 8);
        list.add((byte) 9);
        list.add((byte) 10);
        list.add((byte) 11);
        list.add((byte) 12);
        list.add((byte) 13);
        list.add((byte) 14);
        list.add((byte) 15);
        list.add((byte) 16);
        list.add((byte) 17);
        list.add((byte) 18);
        list.add((byte) 19);
        list.add((byte) 20);
        list.add((byte) 21);
        list.add((byte) 22);
        list.add((byte) 23);
        list.add((byte) 24);
        list.add((byte) 25);
        list.add((byte) 26);
        list.add((byte) 27);
        list.add((byte) 28);
        list.add((byte) 29);
        list.add((byte) 30);
        list.add((byte) 31);
        list.trimToSize();
        assertEquals(31, list.size());
        int initialLength = list._data.length;
        list.trimToSize();
        assertEquals(31, list.size());
        assertEquals(initialLength, list._data.length);
        assertThrows(IllegalStateException.class, () -> list.trimToSize());
    }

    @Test
    public void testConstructWithNullByteCollection() {
        assertThrows(NullPointerException.class, () -> new ArrayByteList(null));
    }

    @Test
    public void testCheckRange() {
        ArrayByteList list = new ArrayByteList(5);
        assertThrows(IndexOutOfBoundsException.class, () -> list.checkRange(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> list.checkRange(5));
    }

    @Test
    public void testCheckRangeIncludingEndpoint() {
        ArrayByteList list = new ArrayByteList(5);
        assertThrows(IndexOutOfBoundsException.class, () -> list.checkRangeIncludingEndpoint(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> list.checkRangeIncludingEndpoint(5));
    }
}