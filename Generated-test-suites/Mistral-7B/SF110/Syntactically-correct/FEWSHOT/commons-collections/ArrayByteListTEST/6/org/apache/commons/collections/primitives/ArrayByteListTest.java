package org.apache.commons.collections.primitives;

import static org.junit.Assert.*;

import org.junit.Test;

public class ArrayByteListTest {

    @Test
    public void testArrayByteListEmptyConstructor() {
        ArrayByteList list = new ArrayByteList();
        assertEquals(0, list.size());
        assertNull(list.get(0));
    }

    @Test
    public void testArrayByteListWithInitialCapacity() {
        ArrayByteList list = new ArrayByteList(5);
        assertEquals(0, list.size());
        assertNull(list.get(0));
        assertArrayEquals(new byte[5], list._data);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testArrayByteListNegativeInitialCapacity() {
        new ArrayByteList(-1);
    }

    @Test
    public void testArrayByteListCopyConstructor() {
        byte[] data = new byte[]{-1, 0, 1, 2, 3, 4};
        ArrayByteList originalList = new ArrayByteList(data);
        ArrayByteList copyList = new ArrayByteList(originalList);
        assertEquals(originalList.size(), copyList.size());
        assertArrayEquals(originalList._data, copyList._data);
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
    public void testGetOutOfBounds() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 0);
        list.add((byte) 1);
        list.get(2);
    }

    @Test
    public void testSize() {
        ArrayByteList list = new ArrayByteList();
        assertEquals(0, list.size());
        list.add((byte) 0);
        list.add((byte) 1);
        assertEquals(2, list.size());
    }

    @Test
    public void testRemoveElementAt() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 0);
        list.add((byte) 1);
        list.add((byte) 2);
        assertEquals((byte) 0, list.removeElementAt(0));
        assertEquals((byte) 1, list.get(0));
        assertEquals((byte) 2, list.get(1));
        assertEquals(2, list.size());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAtOutOfBounds() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 0);
        list.removeElementAt(1);
    }

    @Test
    public void testSet() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 0);
        list.add((byte) 1);
        list.set(0, (byte) 2);
        assertEquals((byte) 2, list.get(0));
        assertEquals((byte) 1, list.get(1));
        assertEquals(2, list.size());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetOutOfBounds() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 0);
        list.set(1, (byte) 2);
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
    public void testAddOutOfBounds() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 0);
        list.add(2, (byte) 2);
    }

    @Test
    public void testEnsureCapacity() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 0);
        list.add((byte) 1);
        list.add((byte) 2);
        list.ensureCapacity(5);
        assertEquals(3, list.size());
        assertArrayEquals(new byte[3], list._data);
    }

    @Test
    public void testEnsureCapacityGrow() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 0);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.add((byte) 4);
        list.ensureCapacity(10);
        assertEquals(5, list.size());
        assertArrayEquals(new byte[5], list._data);
    }

    @Test
    public void testTrimToSize() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 0);
        list.add((byte) 1);
        list.add((byte) 2);
        list.trimToSize();
        assertEquals(3, list.size());
        assertArrayEquals(new byte[3], list._data);
    }

    @Test
    public void testWriteObject() throws IOException {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 0);
        list.add((byte) 1);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(list);
        ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(outputStream.toByteArray()));
        ArrayByteList deserializedList = (ArrayByteList) objectInputStream.readObject();
        assertEquals(list.size(), deserializedList.size());
        assertArrayEquals(list._data, deserializedList._data);
    }

    @Test
    public void testReadObject() throws IOException, ClassNotFoundException {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 0);
        list.add((byte) 1);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(list);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        ArrayByteList deserializedList = (ArrayByteList) objectInputStream.readObject();
        assertEquals(list.size(), deserializedList.size());
        assertArrayEquals(list._data, deserializedList._data);
    }
}