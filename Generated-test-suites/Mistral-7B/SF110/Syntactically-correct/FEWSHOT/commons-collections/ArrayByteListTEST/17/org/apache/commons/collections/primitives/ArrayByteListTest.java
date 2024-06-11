package org.apache.commons.collections.primitives;

import static org.junit.Assert.*;

import org.junit.Test;

public class ArrayByteListTest {

    @Test
    public void testEmptyList() {
        ArrayByteList list = new ArrayByteList();
        assertEquals(0, list.size());
        assertNull(list.get(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeInitialCapacity() {
        new ArrayByteList(-1);
    }

    @Test
    public void testInitialCapacity() {
        ArrayByteList list = new ArrayByteList(5);
        assertEquals(0, list.size());
        assertNull(list.get(0));
        assertEquals(5, list._data.length);
    }

    @Test
    public void testAddAllFromCollection() {
        ByteCollection collection = new ArrayByteList(3);
        collection.add((byte) 1);
        collection.add((byte) 2);
        collection.add((byte) 3);
        ArrayByteList list = new ArrayByteList(collection);
        assertEquals(3, list.size());
        byte[] expected = {(byte) 1, (byte) 2, (byte) 3};
        assertArrayEquals(expected, list._data);
    }

    @Test
    public void testGet() {
        ArrayByteList list = new ArrayByteList(5);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.add((byte) 4);
        list.add((byte) 5);
        byte expected = (byte) 2;
        byte result = list.get(1);
        assertEquals(expected, result);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetOutOfBounds() {
        ArrayByteList list = new ArrayByteList(5);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.add((byte) 4);
        list.add((byte) 5);
        byte result = list.get(5);
    }

    @Test
    public void testSize() {
        ArrayByteList list = new ArrayByteList();
        assertEquals(0, list.size());
        list.add((byte) 1);
        assertEquals(1, list.size());
        list.add((byte) 2);
        assertEquals(2, list.size());
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
        byte result = list.removeElementAt(2);
        assertEquals(expected, result);
        assertEquals(4, list.size());
        byte[] expectedData = {(byte) 1, (byte) 2, (byte) 4, (byte) 5};
        assertArrayEquals(expectedData, list._data);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAtOutOfBounds() {
        ArrayByteList list = new ArrayByteList(5);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.add((byte) 4);
        list.add((byte) 5);
        list.removeElementAt(5);
    }

    @Test
    public void testSet() {
        ArrayByteList list = new ArrayByteList(5);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.add((byte) 4);
        list.add((byte) 5);
        byte expected = (byte) 3;
        byte newValue = (byte) 10;
        byte result = list.set(2, newValue);
        assertEquals(expected, result);
        assertEquals(newValue, list.get(2));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetOutOfBounds() {
        ArrayByteList list = new ArrayByteList(5);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.add((byte) 4);
        list.add((byte) 5);
        byte newValue = (byte) 10;
        list.set(5, newValue);
    }

    @Test
    public void testAdd() {
        ArrayByteList list = new ArrayByteList(5);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.add((byte) 4);
        list.add((byte) 5);
        byte newElement = (byte) 6;
        int index = 2;
        list.add(index, newElement);
        assertEquals(newElement, list.get(index));
        assertEquals(6, list.size());
        byte[] expectedData = {(byte) 1, (byte) 2, (byte) 6, (byte) 4, (byte) 5};
        assertArrayEquals(expectedData, list._data);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddOutOfBounds() {
        ArrayByteList list = new ArrayByteList(5);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.add((byte) 4);
        list.add((byte) 5);
        byte newElement = (byte) 6;
        int index = 6;
        list.add(index, newElement);
    }

    @Test
    public void testEnsureCapacity() {
        ArrayByteList list = new ArrayByteList();
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
        byte[] expectedData = new byte[16];
        assertArrayEquals(expectedData, list._data);
    }

    @Test
    public void testTrimToSize() {
        ArrayByteList list = new ArrayByteList(16);
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
        list.trimToSize();
        byte[] expectedData = new byte[15];
        assertArrayEquals(expectedData, list._data);
        assertEquals(15, list.size());
    }

    @Test
    public void testSerialization() throws IOException, ClassNotFoundException {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(baos);
        out.writeObject(list);
        ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
        ArrayByteList deserializedList = (ArrayByteList) in.readObject();
        assertEquals(3, deserializedList.size());
        byte[] expectedData = {(byte) 1, (byte) 2, (byte) 3};
        assertArrayEquals(expectedData, deserializedList._data);
    }
}