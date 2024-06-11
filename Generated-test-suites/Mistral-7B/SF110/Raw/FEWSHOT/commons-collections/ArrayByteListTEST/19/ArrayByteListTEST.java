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

    @Test
    public void testInitialCapacity() {
        ArrayByteList list = new ArrayByteList(5);
        assertEquals(0, list.size());
        assertNull(list.get(0));
        assertEquals(5, list._data.length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeInitialCapacity() {
        new ArrayByteList(-1);
    }

    @Test
    public void testAddAll() {
        ArrayByteList list = new ArrayByteList();
        ByteCollection collection = new ByteArray();
        list.addAll(collection);
        assertEquals(collection.size(), list.size());
        for (int i = 0; i < collection.size(); ++i) {
            assertEquals(collection.getByte(i), list.get(i));
        }
    }

    @Test
    public void testGet() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 0);
        list.add((byte) 1);
        assertEquals((byte) 0, list.get(0));
        assertEquals((byte) 1, list.get(1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetNegativeIndex() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 0);
        list.get(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetIndexOutOfBounds() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 0);
        list.add((byte) 1);
        list.get(2);
    }

    @Test
    public void testGetLastIndex() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 0);
        list.add((byte) 1);
        assertEquals((byte) 1, list.get(list.size() - 1));
    }

    @Test
    public void testSet() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 0);
        list.set(0, (byte) 1);
        assertEquals((byte) 1, list.get(0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetNegativeIndex() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 0);
        list.set(-1, (byte) 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetIndexOutOfBounds() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 0);
        list.add((byte) 1);
        list.set(2, (byte) 1);
    }

    @Test
    public void testSize() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 0);
        list.add((byte) 1);
        list.add((byte) 2);
        assertEquals(3, list.size());
    }

    @Test
    public void testRemoveElementAt() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 0);
        list.add((byte) 1);
        list.add((byte) 2);
        assertEquals((byte) 1, list.removeElementAt(1));
        assertEquals(2, list.size());
        assertEquals((byte) 0, list.get(0));
        assertEquals((byte) 2, list.get(1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAtNegativeIndex() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 0);
        list.removeElementAt(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAtIndexOutOfBounds() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 0);
        list.add((byte) 1);
        list.removeElementAt(2);
    }

    @Test
    public void testRemoveElement() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 0);
        list.add((byte) 1);
        list.add((byte) 1);
        list.remove((byte) 1);
        assertEquals(2, list.size());
        assertNotEquals((byte) 1, list.get(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveElementNull() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 0);
        list.remove((byte) null);
    }

    @Test
    public void testAdd() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 0);
        list.add((byte) 1);
        list.add((byte) 2);
        assertEquals(3, list.size());
        assertEquals((byte) 0, list.get(0));
        assertEquals((byte) 1, list.get(1));
        assertEquals((byte) 2, list.get(2));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddNegativeIndex() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 0);
        list.add((byte) 1);
        list.add(1, (byte) 2);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddIndexOutOfBounds() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 0);
        list.add((byte) 1);
        list.add(5, (byte) 2);
    }

    @Test
    public void testAddMultiple() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 0);
        list.add((byte) 1);
        list.add(1, (byte) 2);
        list.add(2, (byte) 3);
        list.add(3, (byte) 4);
        assertEquals(6, list.size());
        assertEquals((byte) 0, list.get(0));
        assertEquals((byte) 1, list.get(1));
        assertEquals((byte) 2, list.get(2));
        assertEquals((byte) 3, list.get(3));
        assertEquals((byte) 4, list.get(4));
        assertEquals((byte) 0, list.get(5));
    }

    @Test
    public void testEnsureCapacity() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 0);
        list.add((byte) 1);
        list.add((byte) 1);
        list.add((byte) 1);
        list.add((byte) 1);
        list.add((byte) 1);
        list.add((byte) 1);
        list.ensureCapacity(11);
        assertEquals(8, list._data.length);
    }

    @Test
    public void testTrimToSize() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 0);
        list.add((byte) 1);
        list.add((byte) 1);
        list.add((byte) 1);
        list.add((byte) 1);
        list.add((byte) 1);
        list.add((byte) 1);
        list.trimToSize();
        assertEquals(7, list._data.length);
    }

    @Test
    public void testSerialization() throws IOException, ClassNotFoundException {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 0);
        list.add((byte) 1);
        list.add((byte) 2);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bytes);
        out.writeObject(list);
        ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bytes.toByteArray()));
        ArrayByteList deserializedList = (ArrayByteList) in.readObject();
        assertEquals(3, deserializedList.size());
        assertEquals((byte) 0, deserializedList.get(0));
        assertEquals((byte) 1, deserializedList.get(1));
        assertEquals((byte) 2, deserializedList.get(2));
    }
}