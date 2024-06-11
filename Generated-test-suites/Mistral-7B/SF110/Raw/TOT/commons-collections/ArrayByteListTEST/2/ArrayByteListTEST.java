package org.apache.commons.collections.primitives.test;

import junit.framework.Assert;
import org.apache.commons.collections.primitives.ArrayByteList;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ArrayByteListTest {

    @org.junit.Test
    public void testEmptyConstructor() {
        ArrayByteList list = new ArrayByteList();
        Assert.assertEquals(0, list.size());
    }

    @org.junit.Test
    public void testInitialCapacityConstructor() {
        ArrayByteList list = new ArrayByteList(5);
        Assert.assertEquals(0, list.size());
        Assert.assertArrayEquals(new byte[5], list._data);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testNegativeInitialCapacityConstructor() {
        new ArrayByteList(-5);
    }

    @org.junit.Test
    public void testAddAllFromCollection() {
        ArrayByteList list = new ArrayByteList(5);
        ByteList that = new ArrayByteList();
        that.add((byte) 1);
        that.add((byte) 2);
        that.add((byte) 3);
        list.addAll(that);
        Assert.assertEquals(3, list.size());
        Assert.assertArrayEquals(new byte[]{1, 2, 3}, list._data);
    }

    @org.junit.Test
    public void testGet() {
        ArrayByteList list = new ArrayByteList(3);
        list.set(0, (byte) 1);
        list.set(1, (byte) 2);
        list.set(2, (byte) 3);
        Assert.assertEquals((byte) 1, list.get(0));
        Assert.assertEquals((byte) 2, list.get(1));
        Assert.assertEquals((byte) 3, list.get(2));
    }

    @org.junit.Test(expected = IndexOutOfBoundsException.class)
    public void testGetNegativeIndex() {
        ArrayByteList list = new ArrayByteList(3);
        list.get(-1);
    }

    @org.junit.Test(expected = IndexOutOfBoundsException.class)
    public void testGetBeyondLastIndex() {
        ArrayByteList list = new ArrayByteList(3);
        list.set(0, (byte) 1);
        list.set(1, (byte) 2);
        list.set(2, (byte) 3);
        list.get(3);
    }

    @org.junit.Test
    public void testSize() {
        ArrayByteList list = new ArrayByteList(3);
        list.set(0, (byte) 1);
        list.set(1, (byte) 2);
        list.set(2, (byte) 3);
        Assert.assertEquals(3, list.size());
    }

    @org.junit.Test
    public void testRemoveElementAt() {
        ArrayByteList list = new ArrayByteList(3);
        list.set(0, (byte) 1);
        list.set(1, (byte) 2);
        list.set(2, (byte) 3);
        Assert.assertEquals((byte) 1, list.removeElementAt(0));
        Assert.assertEquals(2, list.size());
        Assert.assertArrayEquals(new byte[]{2, 3}, list._data);
    }

    @org.junit.Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAtNegativeIndex() {
        ArrayByteList list = new ArrayByteList(3);
        list.removeElementAt(-1);
    }

    @org.junit.Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAtBeyondLastIndex() {
        ArrayByteList list = new ArrayByteList(3);
        list.set(0, (byte) 1);
        list.set(1, (byte) 2);
        list.set(2, (byte) 3);
        list.removeElementAt(3);
    }

    @org.junit.Test
    public void testSet() {
        ArrayByteList list = new ArrayByteList(3);
        list.set(0, (byte) 1);
        list.set(1, (byte) 2);
        list.set(2, (byte) 3);
        Assert.assertEquals((byte) 1, list.get(0));
        Assert.assertEquals((byte) 2, list.get(1));
        Assert.assertEquals((byte) 3, list.get(2));
        list.set(0, (byte) 4);
        Assert.assertEquals((byte) 4, list.get(0));
        Assert.assertArrayEquals(new byte[]{4, 2, 3}, list._data);
    }

    @org.junit.Test(expected = IndexOutOfBoundsException.class)
    public void testSetNegativeIndex() {
        ArrayByteList list = new ArrayByteList(3);
        list.set(-1, (byte) 1);
    }

    @org.junit.Test(expected = IndexOutOfBoundsException.class)
    public void testSetBeyondLastIndex() {
        ArrayByteList list = new ArrayByteList(3);
        list.set(0, (byte) 1);
        list.set(1, (byte) 2);
        list.set(2, (byte) 3);
        list.set(4, (byte) 4);
    }

    @org.junit.Test
    public void testAdd() {
        ArrayByteList list = new ArrayByteList(3);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        Assert.assertEquals(3, list.size());
        Assert.assertArrayEquals(new byte[]{1, 2, 3}, list._data);
    }

    @org.junit.Test
    public void testAddAtIndex() {
        ArrayByteList list = new ArrayByteList(3);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.add(1, (byte) 4);
        Assert.assertEquals(4, list.size());
        Assert.assertArrayEquals(new byte[]{1, 4, 2, 3}, list._data);
    }

    @org.junit.Test(expected = IndexOutOfBoundsException.class)
    public void testAddAtIndexNegativeIndex() {
        ArrayByteList list = new ArrayByteList(3);
        list.addAtIndex(-1, (byte) 1);
    }

    @org.junit.Test(expected = IndexOutOfBoundsException.class)
    public void testAddAtIndexBeyondLastIndex() {
        ArrayByteList list = new ArrayByteList(3);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.addAtIndex(4, (byte) 4);
    }

    @org.junit.Test
    public void testTrimToSize() {
        ArrayByteList list = new ArrayByteList(5);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.add((byte) 4);
        list.add((byte) 5);
        list.trimToSize();
        Assert.assertEquals(5, list.size());
        Assert.assertArrayEquals(new byte[]{1, 2, 3, 4, 5}, list._data);
    }

    @org.junit.Test
    public void testEnsureCapacity() {
        ArrayByteList list = new ArrayByteList(5);
        list.ensureCapacity(10);
        Assert.assertEquals(10, list._data.length);
    }

    @org.junit.Test
    public void testSerializable() throws IOException, ClassNotFoundException {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream objOut = new ObjectOutputStream(out);
        objOut.writeObject(list);

        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        ObjectInputStream objIn = new ObjectInputStream(in);
        ArrayByteList deserializedList = (ArrayByteList) objIn.readObject();

        Assert.assertEquals(3, deserializedList.size());
        Assert.assertArrayEquals(new byte[]{1, 2, 3}, deserializedList._data);
    }
}