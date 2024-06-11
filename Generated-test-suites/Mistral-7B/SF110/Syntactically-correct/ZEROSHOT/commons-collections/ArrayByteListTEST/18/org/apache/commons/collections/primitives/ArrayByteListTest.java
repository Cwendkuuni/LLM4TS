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
        Assert.assertEquals(0, list.size());
        Assert.assertArrayEquals(new byte[5], list._data);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNegativeCapacity() {
        new ArrayByteList(-1);
    }

    @Test
    public void testAddAllFromByteCollection() {
        ArrayByteList list = new ArrayByteList(3);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);

        ArrayByteList that = new ArrayByteList();
        that.add((byte) 4);
        that.add((byte) 5);

        list.addAll(that);
        Assert.assertEquals(5, list.size());
        Assert.assertArrayEquals(new byte[]{1, 2, 3, 4, 5}, list._data);
    }

    @Test
    public void testGet() {
        ArrayByteList list = new ArrayByteList(3);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);

        Assert.assertEquals((byte) 1, list.get(0));
        Assert.assertEquals((byte) 2, list.get(1));
        Assert.assertEquals((byte) 3, list.get(2));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetWithNegativeIndex() {
        ArrayByteList list = new ArrayByteList(3);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);

        list.get(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetWithIndexGreaterThanSize() {
        ArrayByteList list = new ArrayByteList(3);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);

        list.get(4);
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
        ArrayByteList list = new ArrayByteList(4);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.add((byte) 4);

        byte removed = list.removeElementAt(1);
        Assert.assertEquals((byte) 2, removed);
        Assert.assertEquals(3, list.size());
        Assert.assertArrayEquals(new byte[]{1, 3, 4}, list._data);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAtWithNegativeIndex() {
        ArrayByteList list = new ArrayByteList(3);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);

        list.removeElementAt(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAtWithIndexGreaterThanSize() {
        ArrayByteList list = new ArrayByteList(3);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);

        list.removeElementAt(4);
    }

    @Test
    public void testSet() {
        ArrayByteList list = new ArrayByteList(3);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);

        byte newByte = (byte) 5;
        list.set(1, newByte);
        Assert.assertEquals(newByte, list.get(1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetWithNegativeIndex() {
        ArrayByteList list = new ArrayByteList(3);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);

        list.set(-1, (byte) 5);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetWithIndexGreaterThanSize() {
        ArrayByteList list = new ArrayByteList(3);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);

        list.set(4, (byte) 5);
    }

    @Test
    public void testAdd() {
        ArrayByteList list = new ArrayByteList(3);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);

        list.add(0, (byte) 0);
        Assert.assertEquals(4, list.size());
        Assert.assertArrayEquals(new byte[]{0, 1, 2, 3}, list._data);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddWithNegativeIndex() {
        ArrayByteList list = new ArrayByteList(3);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);

        list.add(-1, (byte) 0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddWithIndexGreaterThanSize() {
        ArrayByteList list = new ArrayByteList(3);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);

        list.add(5, (byte) 0);
    }

    @Test
    public void testEnsureCapacity() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);

        list.ensureCapacity(10);
        Assert.assertArrayEquals(new byte[10], list._data);
    }

    @Test
    public void testTrimToSize() {
        ArrayByteList list = new ArrayByteList(3);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);

        list.trimToSize();
        Assert.assertEquals(3, list.size());
        Assert.assertArrayEquals(new byte[]{1, 2, 3}, list._data);
    }

    @Test
    public void testWriteAndReadObject() throws IOException, ClassNotFoundException {
        ArrayByteList list = new ArrayByteList(3);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);

        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteStream);
        out.writeObject(list);
        out.close();

        ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(byteStream.toByteArray()));
        ArrayByteList deserializedList = (ArrayByteList) in.readObject();
        in.close();

        Assert.assertEquals(list.size(), deserializedList.size());
        Assert.assertArrayEquals(list._data, deserializedList._data);
    }

    @Test
    public void testCheckRange() {
        ArrayByteList list = new ArrayByteList(3);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);

        list.checkRange(0);
        list.checkRange(3);
        list.checkRange(10);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRangeWithNegativeIndex() {
        ArrayByteList list = new ArrayByteList(3);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);

        list.checkRange(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRangeWithIndexGreaterThanSize() {
        ArrayByteList list = new ArrayByteList(3);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);

        list.checkRange(5);
    }

    @Test
    public void testCheckRangeIncludingEndpoint() {
        ArrayByteList list = new ArrayByteList(3);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);

        list.checkRangeIncludingEndpoint(0);
        list.checkRangeIncludingEndpoint(3);
        list.checkRangeIncludingEndpoint(10);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRangeIncludingEndpointWithNegativeIndex() {
        ArrayByteList list = new ArrayByteList(3);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);

        list.checkRangeIncludingEndpoint(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRangeIncludingEndpointWithIndexGreaterThanSize() {
        ArrayByteList list = new ArrayByteList(3);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);

        list.checkRangeIncludingEndpoint(5);
    }
}