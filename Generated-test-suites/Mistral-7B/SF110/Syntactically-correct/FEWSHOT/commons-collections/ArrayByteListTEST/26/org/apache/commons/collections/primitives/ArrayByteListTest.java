package org.apache.commons.collections.primitives;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;

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
        assertArrayEquals(new byte[5], list._data);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNegativeCapacity() {
        ArrayByteList list = new ArrayByteList(-1);
    }

    @Test
    public void testConstructorByteCollection() {
        ByteCollection that = new ArrayByteList();
        that.add((byte) 0);
        that.add((byte) 1);
        that.add((byte) 2);
        ArrayByteList list = new ArrayByteList(that);
        assertEquals(3, list.size());
        assertArrayEquals(new byte[] { 0, 1, 2 }, list._data, 0, 3);
    }

    @Test
    public void testGet() {
        ArrayByteList list = new ArrayByteList(5);
        list.add((byte) 0);
        list.add((byte) 1);
        list.add((byte) 2);
        assertEquals((byte) 0, list.get(0));
        assertEquals((byte) 1, list.get(1));
        assertEquals((byte) 2, list.get(2));
        assertNull(list.get(3));
        assertNull(list.get(-1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetOutOfBounds() {
        ArrayByteList list = new ArrayByteList(5);
        list.get(5);
    }

    @Test
    public void testSize() {
        ArrayByteList list = new ArrayByteList();
        assertEquals(0, list.size());
        list.add((byte) 0);
        assertEquals(1, list.size());
        list.add((byte) 1);
        assertEquals(2, list.size());
        list.add((byte) 2);
        assertEquals(3, list.size());
    }

    @Test
    public void testRemoveElementAt() {
        ArrayByteList list = new ArrayByteList(5);
        list.add((byte) 0);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.add((byte) 4);
        assertEquals((byte) 0, list.removeElementAt(0));
        assertEquals((byte) 1, list.removeElementAt(2));
        assertEquals(3, list.size());
        assertArrayEquals(new byte[] { 0, 3, 4 }, list._data, 0, 3);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAtOutOfBounds() {
        ArrayByteList list = new ArrayByteList(5);
        list.removeElementAt(-1);
    }

    @Test
    public void testSet() {
        ArrayByteList list = new ArrayByteList(5);
        list.add((byte) 0);
        list.add((byte) 1);
        list.add((byte) 2);
        list.set(0, (byte) 10);
        list.set(1, (byte) 11);
        list.set(2, (byte) 12);
        assertEquals((byte) 10, list.get(0));
        assertEquals((byte) 11, list.get(1));
        assertEquals((byte) 12, list.get(2));
        assertArrayEquals(new byte[] { 10, 11, 12 }, list._data, 0, 3);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetOutOfBounds() {
        ArrayByteList list = new ArrayByteList(5);
        list.set(-1, (byte) 0);
    }

    @Test
    public void testAdd() {
        ArrayByteList list = new ArrayByteList(5);
        list.add((byte) 0);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.add(1, (byte) 10);
        assertEquals(5, list.size());
        assertArrayEquals(new byte[] { 0, 10, 1, 2, 3 }, list._data, 0, 5);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddOutOfBounds() {
        ArrayByteList list = new ArrayByteList(5);
        list.add(-1, (byte) 0);
    }

    @Test
    public void testEnsureCapacity() {
        ArrayByteList list = new ArrayByteList(1);
        list.add((byte) 0);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.add((byte) 4);
        list.add((byte) 5);
        list.ensureCapacity(10);
        assertEquals(6, list.size());
        assertArrayEquals(new byte[] { 0, 1, 2, 3, 4, 5 }, list._data, 0, 6);
    }

    @Test
    public void testTrimToSize() {
        ArrayByteList list = new ArrayByteList(10);
        list.add((byte) 0);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.add((byte) 4);
        list.add((byte) 5);
        list.add((byte) 6);
        list.add((byte) 7);
        list.add((byte) 8);
        list.add((byte) 9);
        list.trimToSize();
        assertEquals(10, list.size());
        assertArrayEquals(new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 }, list._data, 0, 10);
    }

    @Test
    public void testSerializable() throws IOException, ClassNotFoundException {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 0);
        list.add((byte) 1);
        list.add((byte) 2);
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteStream);
        out.writeObject(list);
        ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(byteStream.toByteArray()));
        ArrayByteList deserializedList = (ArrayByteList) in.readObject();
        assertEquals(3, deserializedList.size());
        assertArrayEquals(new byte[] { 0, 1, 2 }, deserializedList._data, 0, 3);
    }

    @Test
    public void testModCount() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 0);
        list.add((byte) 1);
        list.add((byte) 2);
        list.ensureCapacity(4);
        list.trimToSize();
        list.set(0, (byte) 10);
        list.set(1, (byte) 11);
        list.add(1, (byte) 12);
        list.incrModCount();
        list.set(1, (byte) 13);
        list.incrModCount();
        list.add(1, (byte) 14);
        list.incrModCount();
        assertSame(list, list.getClass());
        assertEquals(6, list.size());
        assertArrayEquals(new byte[] { 10, 11, 13, 14, 2, 3 }, list._data, 0, 6);
        assertFalse(list.equals(list._data));
    }
}