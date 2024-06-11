package org.apache.commons.collections.primitives;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.collections.primitives.ArrayByteList;
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
        assertEquals(5, list.size());
        assertNull(list.get(0));
        assertNull(list.get(4));
    }

    @Test
    public void testAddAll() {
        byte[] data = new byte[10];
        ArrayByteList list1 = new ArrayByteList();
        ArrayByteList list2 = new ArrayByteList(5);
        list2.addAll(data, 0, 5);

        list1.addAll(list2);
        assertEquals(10, list1.size());
        assertArrayEquals(data, list1._data);
    }

    @Test
    public void testGet() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        assertEquals((byte) 1, list.get(0));
    }

    @Test
    public void testSize() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        assertEquals(3, list.size());
    }

    @Test
    public void testRemoveElementAt() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        byte removed = list.removeElementAt(1);
        assertEquals((byte) 2, removed);
        assertEquals(2, list.size());
    }

    @Test
    public void testSet() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        byte oldValue = list.set(1, (byte) 4);
        assertEquals((byte) 2, oldValue);
        assertEquals((byte) 4, list.get(1));
    }

    @Test
    public void testAdd() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        byte newElement = (byte) 4;
        list.add(2, newElement);
        assertEquals(4, list.size());
        assertEquals((byte) 2, list.get(1));
        assertEquals((byte) 4, list.get(2));
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
        list.ensureCapacity(20);

        byte[] expectedData = new byte[20];
        System.arraycopy(list._data, 0, expectedData, 0, list._size);
        assertArrayEquals(expectedData, list._data);
    }

    @Test
    public void testTrimToSize() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.add((byte) 4);
        list.add((byte) 5);
        list.trimToSize();
        assertArrayEquals(list._data, new byte[5]);
    }

    @Test
    public void testNegativeIndex() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);

        assertThrows(IndexOutOfBoundsException.class, () -> list.get(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> list.removeElementAt(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> list.set(-1, (byte) 1));
        assertThrows(IndexOutOfBoundsException.class, () -> list.add(-1, (byte) 1));
    }

    @Test
    public void testIndexOutOfBounds() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);

        assertThrows(IndexOutOfBoundsException.class, () -> list.get(3));
        assertThrows(IndexOutOfBoundsException.class, () -> list.removeElementAt(3));
        assertThrows(IndexOutOfBoundsException.class, () -> list.set(3, (byte) 1));
        assertThrows(IndexOutOfBoundsException.class, () -> list.add(3, (byte) 1));
    }

    @Test
    public void testNullData() {
        ArrayByteList list = new ArrayByteList();
        list._data = null;
        assertNull(list._data);
        assertThrows(NullPointerException.class, list::size);
        assertThrows(NullPointerException.class, () -> list.get(0));
        assertThrows(NullPointerException.class, () -> list.removeElementAt(0));
        assertThrows(NullPointerException.class, () -> list.set(0, (byte) 1));
        assertThrows(NullPointerException.class, () -> list.add(0, (byte) 1));
    }

    @Test
    public void testNegativeCapacity() {
        ArrayByteList list = new ArrayByteList();
        list._size = 5;
        list._data = new byte[5];
        assertThrows(IllegalArgumentException.class, () -> list.ensureCapacity(-1));
    }

    @Test
    public void testSerializable() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);

        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream objStream = new ObjectOutputStream(byteStream);
        objStream.writeObject(list);
        objStream.close();

        ByteArrayInputStream byteInputStream = new ByteArrayInputStream(byteStream.toByteArray());
        ObjectInputStream objInputStream = new ObjectInputStream(byteInputStream);
        ArrayByteList deserializedList = (ArrayByteList) objInputStream.readObject();
        objInputStream.close();

        byte[] expectedData = new byte[3];
        System.arraycopy(list._data, 0, expectedData, 0, 3);
        assertArrayEquals(expectedData, deserializedList._data);
        assertEquals(3, deserializedList.size());
    }

    @Test
    public void testSerializationWithEmptyList() {
        ArrayByteList list = new ArrayByteList();

        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream objStream = new ObjectOutputStream(byteStream);
        objStream.writeObject(list);
        objStream.close();

        ByteArrayInputStream byteInputStream = new ByteArrayInputStream(byteStream.toByteArray());
        ObjectInputStream objInputStream = new ObjectInputStream(byteInputStream);
        ArrayByteList deserializedList = (ArrayByteList) objInputStream.readObject();
        objInputStream.close();

        assertNotNull(deserializedList);
        assertEquals(0, deserializedList.size());
    }
}