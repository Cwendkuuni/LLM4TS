package org.apache.commons.collections.primitives;

import static org.junit.Assert.*;

import org.junit.Test;

public class ArrayByteListTest {

    @Test
    public void testConstructorEmpty() {
        ArrayByteList list = new ArrayByteList();
        assertEquals(0, list.size());
        assertNull(list.get(0));
    }

    @Test
    public void testConstructorInitialCapacity() {
        ArrayByteList list = new ArrayByteList(5);
        assertEquals(0, list.size());
        assertNull(list.get(0));
        assertEquals(5, list._data.length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNegativeInitialCapacity() {
        new ArrayByteList(-1);
    }

    @Test
    public void testConstructorByteCollection() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(1);
        outputStream.write(2);
        outputStream.write(3);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ArrayByteList list = new ArrayByteList(inputStream);
        assertEquals(3, list.size());
        assertEquals((byte) 1, list.get(0));
        assertEquals((byte) 2, list.get(1));
        assertEquals((byte) 3, list.get(2));
    }

    @Test
    public void testGet() {
        ArrayByteList list = new ArrayByteList(5);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.add((byte) 4);
        list.add((byte) 5);
        assertEquals((byte) 1, list.get(0));
        assertEquals((byte) 2, list.get(1));
        assertEquals((byte) 3, list.get(2));
        assertEquals((byte) 4, list.get(3));
        assertEquals((byte) 5, list.get(4));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetOutOfBoundsNegativeIndex() {
        ArrayByteList list = new ArrayByteList(5);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.add((byte) 4);
        list.add((byte) 5);
        list.get(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetOutOfBoundsPositiveIndexGreaterThanSize() {
        ArrayByteList list = new ArrayByteList(5);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.add((byte) 4);
        list.add((byte) 5);
        list.get(5);
    }

    @Test
    public void testSize() {
        ArrayByteList list = new ArrayByteList(5);
        assertEquals(0, list.size());
        list.add((byte) 1);
        assertEquals(1, list.size());
        list.add((byte) 2);
        assertEquals(2, list.size());
        list.add((byte) 3);
        list.add((byte) 4);
        list.add((byte) 5);
        assertEquals(5, list.size());
    }

    @Test
    public void testRemoveElementAt() {
        ArrayByteList list = new ArrayByteList(5);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.add((byte) 4);
        list.add((byte) 5);
        assertEquals((byte) 1, list.removeElementAt(0));
        assertEquals(4, list.size());
        assertEquals((byte) 2, list.get(0));
        assertEquals((byte) 3, list.get(1));
        assertEquals((byte) 4, list.get(2));
        assertEquals((byte) 5, list.get(3));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAtOutOfBoundsNegativeIndex() {
        ArrayByteList list = new ArrayByteList(5);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.add((byte) 4);
        list.add((byte) 5);
        list.removeElementAt(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAtOutOfBoundsPositiveIndexGreaterThanSize() {
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
        list.set(0, (byte) 6);
        assertEquals((byte) 6, list.get(0));
        assertEquals((byte) 2, list.get(1));
        assertEquals((byte) 3, list.get(2));
        assertEquals((byte) 4, list.get(3));
        assertEquals((byte) 5, list.get(4));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetOutOfBoundsNegativeIndex() {
        ArrayByteList list = new ArrayByteList(5);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.add((byte) 4);
        list.add((byte) 5);
        list.set(-1, (byte) 6);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetOutOfBoundsPositiveIndexGreaterThanSize() {
        ArrayByteList list = new ArrayByteList(5);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.add((byte) 4);
        list.add((byte) 5);
        list.set(5, (byte) 6);
    }

    @Test
    public void testAdd() {
        ArrayByteList list = new ArrayByteList(5);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.add((byte) 4);
        list.add((byte) 5);
        list.add(0, (byte) 0);
        assertEquals(6, list.size());
        assertEquals((byte) 0, list.get(0));
        assertEquals((byte) 1, list.get(1));
        assertEquals((byte) 2, list.get(2));
        assertEquals((byte) 3, list.get(3));
        assertEquals((byte) 4, list.get(4));
        assertEquals((byte) 5, list.get(5));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddOutOfBoundsNegativeIndex() {
        ArrayByteList list = new ArrayByteList(5);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.add((byte) 4);
        list.add((byte) 5);
        list.add(-1, (byte) 0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddOutOfBoundsPositiveIndexGreaterThanSize() {
        ArrayByteList list = new ArrayByteList(5);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.add((byte) 4);
        list.add((byte) 5);
        list.add(6, (byte) 0);
    }

    @Test
    public void testEnsureCapacity() {
        ArrayByteList list = new ArrayByteList(5);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.add((byte) 4);
        list.add((byte) 5);
        list.add(6, (byte) 6);
        assertEquals(7, list._data.length);
    }

    @Test
    public void testTrimToSize() {
        ArrayByteList list = new ArrayByteList(5);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.add((byte) 4);
        list.add((byte) 5);
        list.trimToSize();
        assertEquals(5, list._data.length);
    }

    @Test
    public void testSerializable() throws IOException, ClassNotFoundException {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(outputStream);
        out.writeObject(list);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ObjectInputStream in = new ObjectInputStream(inputStream);
        ArrayByteList listDeserialized = (ArrayByteList) in.readObject();
        assertEquals(3, listDeserialized.size());
        assertEquals((byte) 1, listDeserialized.get(0));
        assertEquals((byte) 2, listDeserialized.get(1));
        assertEquals((byte) 3, listDeserialized.get(2));
    }

}