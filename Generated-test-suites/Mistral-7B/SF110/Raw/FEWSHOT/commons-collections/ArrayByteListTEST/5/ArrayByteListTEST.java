package org.apache.commons.collections.primitives;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.junit.Test;

public class ArrayByteListTest {

    @Test
    public void testEmptyList() {
        ArrayByteList list = new ArrayByteList();
        assertEquals(0, list.size());
        assertNotNull(list._data);
        assertTrue(list._data.length > 0);
    }

    @Test
    public void testInitialCapacity() {
        ArrayByteList list = new ArrayByteList(10);
        assertEquals(0, list.size());
        assertNotNull(list._data);
        assertEquals(10, list._data.length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeInitialCapacity() {
        new ArrayByteList(-1);
    }

    @Test
    public void testConstructFromCollection() {
        byte[] data = { 1, 2, 3, 4, 5 };
        ArrayByteList list = new ArrayByteList((ByteCollection) new ByteArray(data));
        assertEquals(5, list.size());
        assertArrayEquals(data, list._data);
    }

    @Test
    public void testGet() {
        ArrayByteList list = new ArrayByteList(5);
        byte[] data = { 1, 2, 3, 4, 5 };
        list.addAll(data);
        for (int i = 0; i < data.length; ++i) {
            assertEquals(data[i], list.get(i));
        }
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetOutOfBounds() {
        ArrayByteList list = new ArrayByteList(5);
        list.addAll(new byte[] { 1, 2, 3, 4, 5 });
        list.get(-1);
    }

    @Test
    public void testSize() {
        ArrayByteList list = new ArrayByteList(5);
        assertEquals(0, list.size());
        list.addAll(new byte[] { 1, 2, 3, 4, 5 });
        assertEquals(5, list.size());
    }

    @Test
    public void testRemoveElementAt() {
        ArrayByteList list = new ArrayByteList(5);
        byte[] data = { 1, 2, 3, 4, 5 };
        list.addAll(data);
        byte removed = list.removeElementAt(2);
        assertEquals(3, list.size());
        assertEquals(2, removed);
        assertArrayEquals(new byte[] { 1, 2, 4, 4, 5 }, list._data);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAtOutOfBounds() {
        ArrayByteList list = new ArrayByteList(5);
        list.removeElementAt(-1);
    }

    @Test
    public void testSet() {
        ArrayByteList list = new ArrayByteList(5);
        byte[] data = { 1, 2, 3, 4, 5 };
        list.addAll(data);
        byte newValue = 10;
        list.set(2, newValue);
        assertEquals(newValue, list.get(2));
        assertArrayEquals(new byte[] { 1, 2, 10, 4, 5 }, list._data);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetOutOfBounds() {
        ArrayByteList list = new ArrayByteList(5);
        list.set(-1, (byte) 0);
    }

    @Test
    public void testAdd() {
        ArrayByteList list = new ArrayByteList(5);
        byte newValue = 10;
        list.add(2, newValue);
        assertEquals(6, list.size());
        assertEquals(newValue, list.get(2));
        assertArrayEquals(new byte[] { 1, 2, 10, 4, 5 }, list._data);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddOutOfBounds() {
        ArrayByteList list = new ArrayByteList(5);
        list.add(-1, (byte) 0);
    }

    @Test
    public void testEnsureCapacity() {
        ArrayByteList list = new ArrayByteList(5);
        list.ensureCapacity(10);
        assertEquals(10, list._data.length);
    }

    @Test
    public void testTrimToSize() {
        ArrayByteList list = new ArrayByteList(5);
        list.addAll(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 });
        list.trimToSize();
        assertEquals(10, list.size());
        assertEquals(10, list._data.length);
    }

    @Test
    public void testSerialization() throws IOException, ClassNotFoundException {
        ArrayByteList list = new ArrayByteList(5);
        list.addAll(new byte[] { 1, 2, 3, 4, 5 });
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(list);
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);
        ArrayByteList deserializedList = (ArrayByteList) ois.readObject();
        assertEquals(5, deserializedList.size());
        assertArrayEquals(list._data, deserializedList._data);
    }
}