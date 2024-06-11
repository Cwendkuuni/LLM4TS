package org.apache.commons.collections.primitives;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;

public class ArrayByteListTest implements Serializable {

    @Test
    public void testEmptyList() {
        ArrayByteList list = new ArrayByteList();
        Assert.assertEquals(0, list.size());
    }

    @Test
    public void testInitialCapacity() {
        ArrayByteList list = new ArrayByteList(5);
        Assert.assertEquals(5, list.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeInitialCapacity() {
        new ArrayByteList(-3);
    }

    @Test
    public void testAddAll() {
        byte[] data = {1, 2, 3};
        ArrayByteList list = new ArrayByteList(3);
        ArrayByteList that = new ArrayByteList(data);
        list.addAll(that);
        Assert.assertArrayEquals(data, list._data);
        Assert.assertEquals(3, list.size());
    }

    @Test
    public void testAddElement() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        Assert.assertEquals(1, list.size());
        Assert.assertEquals((byte) 1, list.get(0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetOutOfBounds() {
        ArrayByteList list = new ArrayByteList(3);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.get(4);
    }

    @Test
    public void testSet() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.set(0, (byte) 2);
        Assert.assertEquals(1, list.size());
        Assert.assertEquals((byte) 2, list.get(0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetOutOfBounds() {
        ArrayByteList list = new ArrayByteList(3);
        list.set(4, (byte) 1);
    }

    @Test
    public void testRemoveElementAt() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        list.removeElementAt(0);
        Assert.assertEquals(1, list.size());
        Assert.assertEquals((byte) 2, list.get(0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAtOutOfBounds() {
        ArrayByteList list = new ArrayByteList(3);
        list.removeElementAt(3);
    }

    @Test
    public void testSerialization() throws IOException, ClassNotFoundException {
        byte[] data = {1, 2, 3};
        ArrayByteList list = new ArrayByteList(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(list);
        ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(outputStream.toByteArray()));
        ArrayByteList deserializedList = (ArrayByteList) objectInputStream.readObject();
        Assert.assertArrayEquals(data, deserializedList._data);
        Assert.assertEquals(data.length, deserializedList.size());
    }
}