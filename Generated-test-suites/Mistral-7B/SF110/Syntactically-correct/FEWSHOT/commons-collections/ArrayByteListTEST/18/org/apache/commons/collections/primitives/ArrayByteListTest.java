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
        assertArrayEquals(new byte[5], list._data);
    }

    @Test
    public void testConstructorWithByteCollection() {
        ByteArrayDataSource dataSource = new ByteArrayDataSource(new byte[] { 1, 2, 3 });
        ArrayByteList list = new ArrayByteList(dataSource);
        assertEquals(3, list.size());
        assertArrayEquals(new byte[] { 1, 2, 3 }, list._data, 0, 3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNegativeInitialCapacity() {
        new ArrayByteList(-1);
    }

    @Test
    public void testGet() {
        byte[] data = new byte[] { 1, 2, 3, 4, 5 };
        ArrayByteList list = new ArrayByteList(data);
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
        assertEquals(3, list.get(2));
        assertEquals(4, list.get(3));
        assertEquals(5, list.get(4));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetOutOfBounds() {
        ArrayByteList list = new ArrayByteList();
        list.get(-1);
    }

    @Test
    public void testSize() {
        byte[] data = new byte[] { 1, 2, 3, 4, 5 };
        ArrayByteList list = new ArrayByteList(data);
        assertEquals(5, list.size());
    }

    @Test
    public void testRemoveElementAt() {
        byte[] data = new byte[] { 1, 2, 3, 4, 5 };
        ArrayByteList list = new ArrayByteList(data);
        assertEquals(1, list.removeElementAt(0));
        assertEquals(2, list.size());
        assertEquals(2, list.get(0));

        assertEquals(3, list.removeElementAt(1));
        assertEquals(1, list.size());
        assertEquals(1, list.get(0));

        assertNull(list.removeElementAt(0));
        assertEquals(0, list.size());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAtOutOfBounds() {
        ArrayByteList list = new ArrayByteList();
        list.removeElementAt(-1);
    }

    @Test
    public void testSet() {
        byte[] data = new byte[] { 1, 2, 3, 4, 5 };
        ArrayByteList list = new ArrayByteList(data);
        assertEquals(1, list.get(0));
        list.set(0, (byte) 0);
        assertEquals(0, list.get(0));

        list.set(3, (byte) 10);
        assertEquals(1, list.size());
        assertEquals(10, list.get(3));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetOutOfBounds() {
        ArrayByteList list = new ArrayByteList();
        list.set(-1, (byte) 0);
    }

    @Test
    public void testAdd() {
        ArrayByteList list = new ArrayByteList();
        list.add(0, (byte) 1);
        assertEquals(1, list.size());
        assertEquals(1, list.get(0));

        list.add(1, (byte) 2);
        assertEquals(2, list.size());
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));

        list.add(5, (byte) 3);
        assertEquals(3, list.size());
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
        assertEquals(3, list.get(2));
        assertEquals(3, list.get(4));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddOutOfBounds() {
        ArrayByteList list = new ArrayByteList();
        list.add(-1, (byte) 0);
    }

    @Test
    public void testEnsureCapacity() {
        ArrayByteList list = new ArrayByteList();
        list.ensureCapacity(10);
        assertEquals(10, list._data.length);
    }

    @Test
    public void testTrimToSize() {
        ArrayByteList list = new ArrayByteList();
        list.add(1);
        list.add(2);
        list.trimToSize();
        assertEquals(2, list.size());
    }

    @Test
    public void testSerialization() throws IOException, ClassNotFoundException {
        byte[] data = new byte[] { 1, 2, 3, 4, 5 };
        ArrayByteList list = new ArrayByteList(data);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream objOut = new ObjectOutputStream(out);
        objOut.writeObject(list);
        objOut.close();

        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        ObjectInputStream objIn = new ObjectInputStream(in);
        ArrayByteList deserializedList = (ArrayByteList) objIn.readObject();
        objIn.close();

        assertEquals(5, deserializedList.size());
        assertArrayEquals(data, deserializedList._data, 0, 5);
    }

    @Test(expected = IOException.class)
    public void testSerializationMissingWriteObject() throws IOException, ClassNotFoundException {
        ArrayByteList list = new ArrayByteList();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream objOut = new ObjectOutputStream(out);
        objOut.writeInt(list._data.length);
        objOut.close();

        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        ObjectInputStream objIn = new ObjectInputStream(in);
        objIn.readObject();
        objIn.close();
    }
}