package org.apache.commons.collections.primitives;

import static org.junit.Assert.*;

import org.junit.Test;

public class ArrayByteListTest {
    @Test
    public void testEmptyConstructor() {
        ArrayByteList list = new ArrayByteList();
        assertEquals(0, list.size());
        assertNull(list.get(0));
    }

    @Test
    public void testConstructorWithInitialCapacity() {
        ArrayByteList list = new ArrayByteList(5);
        assertEquals(0, list.size());
        assertNull(list.get(0));
        assertEquals(5, list._data.length);
    }

    @Test
    public void testConstructorWithByteCollection() {
        ByteArrayLinkedList byteList = new ByteArrayLinkedList();
        byteList.add((byte) 1);
        byteList.add((byte) 2);

        ArrayByteList list = new ArrayByteList(byteList);

        assertEquals(2, list.size());
        assertEquals((byte) 1, list.get(0));
        assertEquals((byte) 2, list.get(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNegativeInitialCapacity() {
        new ArrayByteList(-5);
    }

    @Test
    public void testGet() {
        ArrayByteList list = new ArrayByteList(5);
        list.add((byte) 1);
        list.add((byte) 2);

        assertEquals((byte) 1, list.get(0));
        assertEquals((byte) 2, list.get(1));
        assertEquals((byte) 0, list.get(2));
        assertEquals((byte) 0, list.get(3));
        assertEquals((byte) 0, list.get(4));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetWithNegativeIndex() {
        ArrayByteList list = new ArrayByteList(5);
        list.add((byte) 1);
        list.add((byte) 2);

        list.get(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetWithIndexGreaterThanSize() {
        ArrayByteList list = new ArrayByteList(5);
        list.add((byte) 1);
        list.add((byte) 2);

        list.get(6);
    }

    @Test
    public void testSize() {
        ArrayByteList list = new ArrayByteList();
        assertEquals(0, list.size());

        list.add((byte) 1);
        list.add((byte) 2);

        assertEquals(2, list.size());
    }

    @Test
    public void testRemoveElementAt() {
        ArrayByteList list = new ArrayByteList(5);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);

        assertEquals((byte) 1, list.removeElementAt(0));
        assertEquals((byte) 2, list.removeElementAt(1));
        assertEquals((byte) 3, list.get(0));
        assertEquals(1, list.size());

        assertNull(list.removeElementAt(0));
        assertEquals(0, list.size());

        assertNull(list.removeElementAt(0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAtWithNegativeIndex() {
        ArrayByteList list = new ArrayByteList(5);
        list.add((byte) 1);
        list.add((byte) 2);

        list.removeElementAt(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAtWithIndexGreaterThanSize() {
        ArrayByteList list = new ArrayByteList(5);
        list.add((byte) 1);
        list.add((byte) 2);

        list.removeElementAt(3);
    }

    @Test
    public void testSet() {
        ArrayByteList list = new ArrayByteList(5);
        list.add((byte) 1);
        list.add((byte) 2);

        list.set(0, (byte) 3);
        assertEquals((byte) 3, list.get(0));
        assertEquals((byte) 2, list.get(1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetWithNegativeIndex() {
        ArrayByteList list = new ArrayByteList(5);
        list.add((byte) 1);
        list.add((byte) 2);

        list.set(-1, (byte) 3);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetWithIndexGreaterThanSize() {
        ArrayByteList list = new ArrayByteList(5);
        list.add((byte) 1);
        list.add((byte) 2);

        list.set(3, (byte) 3);
    }

    @Test
    public void testAdd() {
        ArrayByteList list = new ArrayByteList(5);
        list.add((byte) 1);
        list.add((byte) 2);

        list.add(0, (byte) 0);
        assertEquals(3, list.size());
        assertEquals((byte) 0, list.get(0));
        assertEquals((byte) 1, list.get(1));
        assertEquals((byte) 2, list.get(2));

        list.add(4, (byte) 4);
        assertEquals(4, list.size());
        assertEquals((byte) 0, list.get(0));
        assertEquals((byte) 1, list.get(1));
        assertEquals((byte) 2, list.get(2));
        assertEquals((byte) 4, list.get(3));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddWithIndexLessThanZero() {
        ArrayByteList list = new ArrayByteList(5);
        list.add((byte) 1);
        list.add((byte) 2);

        list.add(-1, (byte) 0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddWithIndexGreaterThanCapacity() {
        ArrayByteList list = new ArrayByteList(5);
        list.add((byte) 1);
        list.add((byte) 2);

        list.add(10, (byte) 0);
    }

    @Test
    public void testEnsureCapacity() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);

        list.ensureCapacity(7);
    }

    @Test
    public void testTrimToSize() {
        ArrayByteList list = new ArrayByteList(10);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.trimToSize();

        assertEquals(3, list.size());
    }

    @Test
    public void testSerialization() throws IOException, ClassNotFoundException {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);

        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream outputStream = new ObjectOutputStream(byteStream);
        outputStream.writeObject(list);

        ObjectInputStream inputStream = new ObjectInputStream(new ByteArrayInputStream(byteStream.toByteArray()));
        ArrayByteList deserializedList = (ArrayByteList) inputStream.readObject();

        assertEquals(2, deserializedList.size());
        assertEquals((byte) 1, deserializedList.get(0));
        assertEquals((byte) 2, deserializedList.get(1));
    }
}