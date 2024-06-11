package org.apache.commons.collections.primitives.test;

import org.apache.commons.collections.primitives.ArrayByteList;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class ArrayByteListTest implements Serializable {

    @Test
    public void testDefaultConstructor() {
        ArrayByteList list = new ArrayByteList();
        Assert.assertEquals(0, list.size());
    }

    @Test
    public void testConstructorWithInitialCapacity() {
        ArrayByteList list = new ArrayByteList(5);
        Assert.assertEquals(5, list.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNegativeInitialCapacity() {
        new ArrayByteList(-1);
    }

    @Test
    public void testAddAll() {
        ArrayByteList list1 = new ArrayByteList(10);
        list1.add((byte) 1);
        list1.add((byte) 2);
        list1.add((byte) 3);

        ArrayByteList list2 = new ArrayByteList(3);
        list2.add((byte) 4);
        list2.add((byte) 5);
        list2.add((byte) 6);

        list1.addAll(list2);

        Assert.assertEquals(13, list1.size());
        Assert.assertEquals((byte) 1, list1.get(0));
        Assert.assertEquals((byte) 2, list1.get(1));
        Assert.assertEquals((byte) 3, list1.get(2));
        Assert.assertEquals((byte) 4, list1.get(3));
        Assert.assertEquals((byte) 5, list1.get(4));
        Assert.assertEquals((byte) 6, list1.get(5));
    }

    @Test
    public void testGet() {
        ArrayByteList list = new ArrayByteList(5);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.add((byte) 4);
        list.add((byte) 5);

        Assert.assertEquals((byte) 1, list.get(0));
        Assert.assertEquals((byte) 2, list.get(1));
        Assert.assertEquals((byte) 3, list.get(2));
        Assert.assertEquals((byte) 4, list.get(3));
        Assert.assertEquals((byte) 5, list.get(4));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetWithNegativeIndex() {
        ArrayByteList list = new ArrayByteList(5);
        list.get(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetWithIndexGreaterThanSize() {
        ArrayByteList list = new ArrayByteList(5);
        list.get(5);
    }

    @Test
    public void testSize() {
        ArrayByteList list = new ArrayByteList(5);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.add((byte) 4);
        list.add((byte) 5);

        Assert.assertEquals(5, list.size());
    }

    @Test
    public void testRemoveElementAt() {
        ArrayByteList list = new ArrayByteList(5);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.add((byte) 4);
        list.add((byte) 5);

        Assert.assertEquals((byte) 1, list.removeElementAt(0));
        Assert.assertEquals(4, list.size());
        Assert.assertEquals((byte) 2, list.get(0));
        Assert.assertEquals((byte) 3, list.get(1));
        Assert.assertEquals((byte) 4, list.get(2));
        Assert.assertEquals((byte) 5, list.get(3));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAtWithNegativeIndex() {
        ArrayByteList list = new ArrayByteList(5);
        list.removeElementAt(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAtWithIndexGreaterThanSize() {
        ArrayByteList list = new ArrayByteList(5);
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
        Assert.assertEquals((byte) 6, list.get(0));
        Assert.assertEquals((byte) 2, list.get(1));
        Assert.assertEquals((byte) 3, list.get(2));
        Assert.assertEquals((byte) 4, list.get(3));
        Assert.assertEquals((byte) 5, list.get(4));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetWithNegativeIndex() {
        ArrayByteList list = new ArrayByteList(5);
        list.set(-1, (byte) 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetWithIndexGreaterThanSize() {
        ArrayByteList list = new ArrayByteList(5);
        list.set(5, (byte) 1);
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
        Assert.assertEquals(6, list.size());
        Assert.assertEquals((byte) 0, list.get(0));
        Assert.assertEquals((byte) 1, list.get(1));
        Assert.assertEquals((byte) 2, list.get(2));
        Assert.assertEquals((byte) 3, list.get(3));
        Assert.assertEquals((byte) 4, list.get(4));
        Assert.assertEquals((byte) 5, list.get(5));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddWithNegativeIndex() {
        ArrayByteList list = new ArrayByteList(5);
        list.add(-1, (byte) 1);
    }

    @Test
    public void testEnsureCapacity() {
        ArrayByteList list = new ArrayByteList(5);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.add((byte) 4);
        list.add((byte) 5);

        list.ensureCapacity(10);
        Assert.assertEquals(10, list._data.length);
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
        Assert.assertEquals(5, list._data.length);
    }

    @Test
    public void testWriteAndReadObject() throws IOException, ClassNotFoundException {
        ArrayByteList list = new ArrayByteList(5);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.add((byte) 4);
        list.add((byte) 5);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(list);
        objectOutputStream.close();

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        ArrayByteList deserializedList = (ArrayByteList) objectInputStream.readObject();
        objectInputStream.close();

        Assert.assertEquals(5, deserializedList.size());
        Assert.assertEquals((byte) 1, deserializedList.get(0));
        Assert.assertEquals((byte) 2, deserializedList.get(1));
        Assert.assertEquals((byte) 3, deserializedList.get(2));
        Assert.assertEquals((byte) 4, deserializedList.get(3));
        Assert.assertEquals((byte) 5, deserializedList.get(4));
    }
}