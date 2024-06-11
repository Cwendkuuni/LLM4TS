package org.apache.commons.collections.primitives.test;

import org.apache.commons.collections.primitives.ArrayByteList;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class ArrayByteListTest implements Serializable {

    @Test
    public void testEmptyConstructor() {
        ArrayByteList list = new ArrayByteList();
        Assert.assertEquals(0, list.size());
    }

    @Test
    public void testConstructorWithInitialCapacity() {
        ArrayByteList list = new ArrayByteList(5);
        Assert.assertEquals(0, list.size());
        Assert.assertNotNull(list._data);
        Assert.assertEquals(5, list._data.length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionWithNegativeInitialCapacity() {
        new ArrayByteList(-1);
    }

    @Test
    public void testConstructorWithByteCollection() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(1);
        outputStream.write(2);
        outputStream.write(3);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ArrayByteList expectedList = new ArrayByteList();
        expectedList.add((byte) 1);
        expectedList.add((byte) 2);
        expectedList.add((byte) 3);
        ArrayByteList list = new ArrayByteList(new ArrayByteList() {{
            addAll(expectedList);
        }});
        Assert.assertEquals(expectedList.size(), list.size());
        for (int i = 0; i < expectedList.size(); i++) {
            Assert.assertEquals(expectedList.get(i), list.get(i));
        }
    }

    @Test
    public void testGet() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        Assert.assertEquals((byte) 1, list.get(0));
        Assert.assertEquals((byte) 2, list.get(1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetWithNegativeIndex() {
        ArrayByteList list = new ArrayByteList();
        list.get(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetWithIndexGreaterThanSize() {
        ArrayByteList list = new ArrayByteList();
        list.get(1);
    }

    @Test
    public void testSize() {
        ArrayByteList list = new ArrayByteList();
        Assert.assertEquals(0, list.size());
        list.add((byte) 1);
        Assert.assertEquals(1, list.size());
        list.add((byte) 2);
        Assert.assertEquals(2, list.size());
    }

    @Test
    public void testRemoveElementAt() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        Assert.assertEquals((byte) 1, list.removeElementAt(0));
        Assert.assertEquals(2, list.size());
        Assert.assertEquals((byte) 2, list.get(0));
        Assert.assertEquals((byte) 3, list.get(1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAtWithNegativeIndex() {
        ArrayByteList list = new ArrayByteList();
        list.removeElementAt(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAtWithIndexGreaterThanSize() {
        ArrayByteList list = new ArrayByteList();
        list.removeElementAt(1);
    }

    @Test
    public void testSet() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.set(0, (byte) 2);
        Assert.assertEquals((byte) 2, list.get(0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetWithNegativeIndex() {
        ArrayByteList list = new ArrayByteList();
        list.set(-1, (byte) 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetWithIndexGreaterThanSize() {
        ArrayByteList list = new ArrayByteList();
        list.set(1, (byte) 1);
    }

    @Test
    public void testAdd() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        list.add(0, (byte) 0);
        Assert.assertEquals(3, list.size());
        Assert.assertEquals((byte) 0, list.get(0));
        Assert.assertEquals((byte) 1, list.get(1));
        Assert.assertEquals((byte) 2, list.get(2));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddWithNegativeIndex() {
        ArrayByteList list = new ArrayByteList();
        list.add(-1, (byte) 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddWithIndexGreaterThanSize() {
        ArrayByteList list = new ArrayByteList();
        list.add(1, (byte) 1);
    }

    @Test
    public void testAddAll() {
        ArrayByteList list1 = new ArrayByteList();
        list1.add((byte) 1);
        list1.add((byte) 2);
        ArrayByteList list2 = new ArrayByteList();
        list2.add((byte) 3);
        list2.add((byte) 4);
        list1.addAll(1, list2);
        Assert.assertEquals(5, list1.size());
        Assert.assertEquals((byte) 1, list1.get(0));
        Assert.assertEquals((byte) 2, list1.get(1));
        Assert.assertEquals((byte) 3, list1.get(2));
        Assert.assertEquals((byte) 4, list1.get(3));
        Assert.assertEquals((byte) 1, list1.get(4));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddAllWithNegativeIndex() {
        ArrayByteList list1 = new ArrayByteList();
        list1.add((byte) 1);
        list1.add((byte) 2);
        ArrayByteList list2 = new ArrayByteList();
        list2.add((byte) 3);
        list2.add((byte) 4);
        list1.addAll(-1, list2);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddAllWithIndexGreaterThanSize() {
        ArrayByteList list1 = new ArrayByteList();
        list1.add((byte) 1);
        list1.add((byte) 2);
        ArrayByteList list2 = new ArrayByteList();
        list2.add((byte) 3);
        list2.add((byte) 4);
        list1.addAll(4, list2);
    }

    @Test
    public void testEnsureCapacity() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.ensureCapacity(6);
        Assert.assertEquals(3, list.size());
        Assert.assertNotNull(list._data);
        Assert.assertEquals(12, list._data.length);
    }

    @Test
    public void testTrimToSize() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.trimToSize();
        Assert.assertEquals(3, list.size());
        Assert.assertEquals(3, list._data.length);
    }

    @Test
    public void testSerialization() throws IOException, ClassNotFoundException {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(list);
        ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(outputStream.toByteArray()));
        ArrayByteList deserializedList = (ArrayByteList) objectInputStream.readObject();
        Assert.assertEquals(2, deserializedList.size());
        Assert.assertEquals((byte) 1, deserializedList.get(0));
        Assert.assertEquals((byte) 2, deserializedList.get(1));
    }

    @Test
    public void testToString() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        Assert.assertEquals("[1, 2]", list.toString());
    }
}