package org.apache.commons.collections.primitives.test;

import static org.junit.Assert.*;

import org.apache.commons.collections.primitives.ArrayByteList;
import org.apache.commons.collections.primitives.ByteList;
import org.junit.Test;

public class ArrayByteListTest {

    @Test
    public void testArrayByteListConstruction() {
        ArrayByteList list = new ArrayByteList();
        assertEquals(0, list.size());
        ArrayByteList list2 = new ArrayByteList(5);
        assertEquals(5, list2.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testArrayByteListNegativeInitialCapacity() {
        new ArrayByteList(-1);
    }

    @Test
    public void testAddAll() {
        ByteList list = new ArrayByteList();
        list.add((byte) 1);
        ByteList that = new ArrayByteList();
        that.add((byte) 2);
        that.add((byte) 3);
        list.addAll(that);
        assertEquals(3, list.size());
        byte[] expected = {(byte) 1, (byte) 2, (byte) 3};
        assertArrayEquals(expected, list.toByteArray());
    }

    @Test
    public void testGet() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        assertEquals((byte) 1, list.get(0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetNegativeIndex() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.get(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetBeyondSize() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.get(0);
        list.add((byte) 2);
        list.get(2);
    }

    @Test
    public void testSet() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.set(0, (byte) 2);
        assertEquals((byte) 2, list.get(0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetNegativeIndex() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.set(-1, (byte) 2);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetBeyondSize() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        list.set(2, (byte) 3);
    }

    @Test
    public void testRemoveElementAt() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        list.removeElementAt(0);
        assertEquals(1, list.size());
        byte[] expected = {(byte) 2};
        assertArrayEquals(expected, list.toByteArray());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAtNegativeIndex() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.removeElementAt(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAtBeyondSize() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.removeElementAt(0);
        list.add((byte) 2);
        list.removeElementAt(2);
    }

    @Test
    public void testAdd() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        assertEquals(2, list.size());
        byte[] expected = {(byte) 1, (byte) 2};
        assertArrayEquals(expected, list.toByteArray());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddNegativeIndex() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add(-1, (byte) 2);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddBeyondSize() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        list.add(3, (byte) 3);
    }

    @Test
    public void testEnsureCapacity() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.ensureCapacity(5);
        assertEquals(3, list.size());
    }

    @Test
    public void testTrimToSize() {
        ArrayByteList list = new ArrayByteList();
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
        list.add((byte) 3);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bos);
        out.writeObject(list);

        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        ObjectInputStream in = new ObjectInputStream(bis);
        ArrayByteList deserializedList = (ArrayByteList) in.readObject();

        assertEquals(list.size(), deserializedList.size());
        assertArrayEquals(list.toByteArray(), deserializedList.toByteArray());
    }

}