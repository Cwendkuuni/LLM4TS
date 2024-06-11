package org.apache.commons.collections.primitives;

import org.junit.Test;
import org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class ArrayByteListTest {

    @Test
    public void testEmptyConstruction() {
        ArrayByteList list = new ArrayByteList();
        assertEquals(0, list.size());
        assertNull(list._data);
    }

    @Test
    public void testConstructionWithCapacity() {
        ArrayByteList list = new ArrayByteList(5);
        assertEquals(0, list.size());
        assertArrayEquals(new byte[5], list._data);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeCapacity() {
        new ArrayByteList(-1);
    }

    @Test
    public void testAddAll() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);

        ArrayByteList anotherList = new ArrayByteList();
        anotherList.add((byte) 3);
        anotherList.add((byte) 4);

        list.addAll(anotherList);

        assertEquals(4, list.size());
        assertArrayEquals(new byte[]{(byte) 1, (byte) 2, (byte) 3, (byte) 4}, list._data);
    }

    @Test
    public void testGet() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);

        assertEquals((byte) 1, list.get(0));
        assertEquals((byte) 2, list.get(1));
        try {
            list.get(-1);
            fail();
        } catch (IndexOutOfBoundsException e) {
            // expected
        }
    }

    @Test
    public void testSet() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);

        list.set(0, (byte) 3);
        assertEquals((byte) 3, list.get(0));
        assertEquals((byte) 2, list.get(1));
    }

    @Test
    public void testRemoveElementAt() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);

        list.removeElementAt(0);
        assertEquals(1, list.size());
        assertArrayEquals(new byte[]{(byte) 2}, list._data);
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
    public void testSerialisation() throws IOException, ClassNotFoundException {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);

        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteStream);
        out.writeObject(list);
        out.close();

        ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(byteStream.toByteArray()));
        ArrayByteList deserializedList = (ArrayByteList) in.readObject();
        in.close();

        assertEquals(2, deserializedList.size());
        assertArrayEquals(new byte[]{(byte) 1, (byte) 2}, deserializedList._data);
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
    public void testEnsureCapacity() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.add((byte) 4);
        list.add((byte) 5);

        list.ensureCapacity(10);
        assertEquals(5, list.size());
        assertArrayEquals(new byte[]{(byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5}, list._data);
    }

    private void assertArrayEquals(byte[] expected, byte[] actual) {
        if (!java.util.Arrays.equals(expected, actual)) {
            fail("Expected " + java.util.Arrays.toString(expected) + ", but was " + java.util.Arrays.toString(actual));
        }
    }
}